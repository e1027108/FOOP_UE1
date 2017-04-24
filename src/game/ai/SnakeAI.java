package game.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import artifacts.Artifact;
import artifacts.permanent.HealthDecreaseArtifact;
import artifacts.permanent.HealthIncreaseArtifact;
import artifacts.permanent.SizeDecreaseArtifact;
import artifacts.permanent.SizeIncreaseArtifact;
import artifacts.temporary.BlockControlArtifact;
import artifacts.temporary.InvulnerabilityArtifact;
import artifacts.temporary.ReverseControlArtifact;
import artifacts.temporary.SpeedDecreaseArtifact;
import artifacts.temporary.SpeedIncreaseArtifact;
import game.Directions;
import game.Game;
import game.Point;
import game.Snake;
import game.SnakeImpl;

public class SnakeAI extends SnakeImpl {

	private Game game;

	private static final double P_OTHER = 0.1;
	private static final double P_OTHER_SECONDARY = 0.4;
	private static final int DISTANCE = 6;

	public SnakeAI(String name, int gridID, Directions dir, Game game) {
		super(name, gridID, dir);

		this.game = game;
	}

	public void determineNextDirection() {
		Directions next;
		ArrayList<Object> closeObjects;

		//block control doesn't change direction, otw:
		if(!hasBlockControl()){
			closeObjects = scanVicinity(DISTANCE);

			Object important = getValuedObject(closeObjects);

			next = getPreferredDirection(important);
			
			//can deviate from best choice
			next = getRandomDirection(next, P_OTHER);
			
			if(!isValidDirection(next)){
				next = returnValidDirection(next);
			}
			
			direction = next;
		}
	}

	/**
	 * checks all possible directions and returns
	 * something valid if possible (if not, next stays next)
	 * @param next the direction we want to check
	 * @return next if valid, otw other direction
	 */
	private Directions returnValidDirection(Directions next) {
		List<Directions> toUse;

		if(vertical.contains(next)){
			toUse = horizontal;
		}
		else{
			toUse = vertical;
		}

		for(Directions d: toUse){
			if(isValidDirection(d)){
				return d;
			}
		}

		return next;
	}

	/**
	 * determines if the snake is headed into itself or into the wall
	 * @param d the planned direction
	 * @return crash yes/no
	 */
	private boolean isValidDirection(Directions d) {
		Point nextPoint = null;
		int xpos = position.getFirst().getX();
		int ypos = position.getFirst().getY();

		if(d == Directions.N){
			nextPoint = new Point(xpos-1,ypos);
		}
		else if(d == Directions.S){
			nextPoint = new Point(xpos+1,ypos);
		}
		else if(d == Directions.E){
			nextPoint = new Point(xpos,ypos+1);
		}
		else if(d == Directions.W){
			nextPoint = new Point(xpos,ypos-1);
		}

		if(nextPoint.getX() < 0 || nextPoint.getY() < 0 
				|| nextPoint.getX() >= game.getGrid().getSize() || nextPoint.getY() >= game.getGrid().getSize()){
			return false;
		}
		else if(d == Directions.S && direction == Directions.N || d == Directions.N && direction == Directions.S ||
				d == Directions.E && direction == Directions.W || d == Directions.W && direction == Directions.E){
			return false;
		}
		else{
			for(Point p: position){
				if(p.equals(nextPoint)){ //uses Point.equals(Point other)
					return false;
				}
			}
		}

		return true;
	}

	private Directions getPreferredDirection(Object goalObject) {
		Directions newdirection = direction;
		double value;

		if (goalObject != null) {
			value = valueObject(goalObject);
		}
		else {
			return direction;
			//return getRandomDirection(P_OTHER);
		}

		Point goalPosition = getGoalPosition(goalObject);
		int goalx = goalPosition.getX();
		int goaly = goalPosition.getY();
		int currx = position.getFirst().getX();
		int curry = position.getFirst().getY();

		if (currx < goalx) {
			if (value > 0) {
				newdirection = Directions.S;
			} else if (value < 0) {
				newdirection = Directions.N;
			}
		} else if (currx > goalx) {
			if (value > 0) {
				newdirection = Directions.N;
			} else if (value < 0) {
				newdirection = Directions.S;
			}
		} else if (curry < goaly) {
			if (value > 0) {
				newdirection = Directions.E;
			} else if (value < 0) {
				newdirection = Directions.W;
			}
		} else if (curry > goaly) {
			if (value > 0) {
				newdirection = Directions.W;
			} else if (value < 0) {
				newdirection = Directions.E;
			}
		} else {
			newdirection = direction;
		}

		// preventing from driving into self
		if ((newdirection == Directions.N && direction == Directions.S)
				|| (newdirection == Directions.S && direction == Directions.N)
				|| (newdirection == Directions.E && direction == Directions.W)
				|| (newdirection == Directions.W && direction == Directions.E)) {
			newdirection = direction;
		}

		return newdirection;
	}

	private Point getGoalPosition(Object goalObject) {
		if (goalObject instanceof Snake) {
			return ((Snake) goalObject).getBody()[0];
		} else { // hope there will never be a non-snake, non-artifact object
			return ((Artifact) goalObject).getPlacement();
		}
	}

	private Directions getRandomDirection(Directions initial, double changeChance) {
		Random r = new Random();
		Point pos = position.getFirst();
		int gridsize = game.getGrid().getSize();

		//we don't want to hug the border
		if(pos.getX() == gridsize - 1 || pos.getX() == 0 ||
				pos.getY() == gridsize - 1 || pos.getY() == 0){
			changeChance = P_OTHER_SECONDARY;
		}

		double d = r.nextDouble();
		
		if (d <= changeChance) {
			List<Directions> directions = new ArrayList<Directions>();

			if (vertical.contains(direction)) {
				directions = horizontal;
			} else {
				directions = vertical;
			}

			double rnd = r.nextDouble();

			if (rnd <= (double) 1 / 2) {
				return directions.get(0);
			} else {
				return directions.get(1);
			}
		} else {
			return initial;
		}
	}

	/**
	 * returns the best valued object to go towards
	 * 
	 * @param closeObjects
	 *            possible objects
	 * @return best object
	 */
	private Object getValuedObject(ArrayList<Object> closeObjects) {
		Random rnd = new Random();		
		double currentValue = 0;
		Object currentObject = null;

		if (closeObjects == null || closeObjects.isEmpty()) {
			return currentObject;
		}

		// finds the most severe value
		for (Object o : closeObjects) {
			double objectValue = valueObject(o);
			//if same value --> rnd
			if (Math.abs(objectValue) > Math.abs(currentValue) || (Math.abs(objectValue) == Math.abs(currentValue) && rnd.nextDouble() <= .5) ) { 
				currentValue = objectValue;
				currentObject = o;
			}
		}

		return currentObject;
	}

	/*
	 * value from -1 to +1
	 */
	private double valueObject(Object o) {
		double value = 0;
		int distance = measureDistance(o);

		if (o instanceof Snake) {
			int ox = ((Snake) o).getBody()[0].getX();
			int oy = ((Snake) o).getBody()[0].getY();

			Point closest = getClosestPoint(ox, oy);

			distance = Math.abs(closest.getX() - ox) + Math.abs(closest.getY() - oy);

			if (!closest.equals(position.getFirst())) {
				value = -.8 * (((double) DISTANCE / distance) / DISTANCE);
			} else {
				value = .8 * ((double) 1 / distance);
			}

			//these make value more severe if own or enemy health "critical"
			if(getHealth() < (getMaxHealth() * .5) && value < 0){
				value -= -.1;
			}
			else if(((Snake) o).getHealth() < ((Snake) o).getMaxHealth() * .5 && value >= 0){
				value += .1;
			}
		} 
		else if (o instanceof HealthIncreaseArtifact) { 
			// 0: full health already, 0.1: overheal, higher range depending on distance and health
			value = .9 * ((double) 1 / distance);

			if(getMaxHealth() - getHealth() < ((HealthIncreaseArtifact) o).getIncrease()){
				value = .1;
			}
			else if(getHealth() == getMaxHealth()){
				value = 0;
			}
		}
		else if (o instanceof SizeIncreaseArtifact) { 
			// AI always wants to grow
			value = .4 * ((double) 1 / distance);
		}
		else if (o instanceof InvulnerabilityArtifact) {
			if(this.isInvulnerable()){
				value = 0;
			}
			else{
				value = 1 * ((double) 1 / distance);
			}
		}
		else if (o instanceof SpeedIncreaseArtifact) { 
			if (getSpeed() > DEFAULT_SPEED) {
				value = .1;
			}
			else {
				value = .5 * ((double) 1 / distance);
			}
		}
		// negative pickups
		else if (o instanceof HealthDecreaseArtifact) {
			if (this.getHealth() == this.getMaxHealth()) {
				value = -(.1);
			}
			else if (this.getHealth() - ((HealthDecreaseArtifact) o).getDecrease() <= 0) {
				value = -1;
			}
			else {
				double remainderPercentage = (this.getHealth() - ((HealthDecreaseArtifact) o).getDecrease()) / this.getMaxHealth();

				// the closer the less we want it, less health --> less desire
				value = -.9 * (((double) DISTANCE / distance) / DISTANCE) * ((remainderPercentage - 1) * -1); 
			}
		}
		else if (o instanceof SizeDecreaseArtifact) {
			value = -.4 * (((double) DISTANCE / distance) / DISTANCE); // we never want this
		}
		else if (o instanceof BlockControlArtifact) {
			value = -.9 * (((double) DISTANCE / distance) / DISTANCE); // we really never want this
		}
		else if (o instanceof ReverseControlArtifact) {
			// we are perfect, this doesn't harm us, all your base are belong to us
			// -.1 because we want others to be able to pick it up
			value = -.1;
		}
		else if (o instanceof SpeedDecreaseArtifact) {
			if (this.getSpeed() > DEFAULT_SPEED) {
				value = -.3 * (((double) DISTANCE / distance) / DISTANCE);
			}
			else {
				value = -.5 * (((double) DISTANCE / distance) / DISTANCE);
			}
		}
		// as yet not defined artifacts
		else {
			value = 0;
		}

		if(value < 0 && isInvulnerable()){
			value = 0;
		}

		return value;
	}

	/**
	 * measures the manhattan distance of ai snake's head to the artifact specified
	 * for other snakes we measure the shortest head - body distance
	 * (time cost for turning around is added since snakes can't go into themselves)
	 * @param o the goal object
	 * @return the effective distance between objects
	 */
	private int measureDistance(Object o) {
		int xhead = position.getFirst().getX();
		int yhead = position.getFirst().getY();
		int ox = 0;
		int oy = 0;
		int distance = 0;

		if (o instanceof Snake) {
			ox = ((Snake) o).getBody()[0].getX();
			oy = ((Snake) o).getBody()[0].getY();

			Point closest = getClosestPoint(ox, oy);

			distance = Math.abs(closest.getX() - ox) + Math.abs(closest.getY() - oy);

			// other snake hunts me --> if the enemy snake needs to turn around + 1
			if (!closest.equals(position.getFirst())) {
				if (oy > closest.getY() && ((Snake) o).getDirection() == Directions.W
						|| oy < closest.getY() && ((Snake) o).getDirection() == Directions.E) {
					distance++;
				}
				if (ox > closest.getX() && ((Snake) o).getDirection() == Directions.N
						|| ox < closest.getX() && ((Snake) o).getDirection() == Directions.S) {
					distance++;
				}
			}
		} else if (o instanceof Artifact) {
			ox = ((Artifact) o).getPlacement().getX();
			oy = ((Artifact) o).getPlacement().getY();

			distance = Math.abs(xhead - ox) + Math.abs(yhead - oy);
		}

		/*
		 * you can't turn around directly but must go around your own body -->
		 * add +2 if at same height/width since you must 1. change pos in
		 * relation to your body and 2. change back into that lane
		 */
		if (ox == xhead) { // x is up and down
			if (oy > yhead && direction == Directions.E || oy < yhead && direction == Directions.W) { // same means we are currently on it!
				distance += 2;
			}
		}
		if (oy == yhead) { // y is left and right
			if (ox > xhead && direction == Directions.S || ox < xhead && direction == Directions.N) { // same means we are currently on it!
				distance += 2;
			}
		}

		return distance;
	}

	/**
	 * returns the point of the own body closest to the provided coordinates
	 * @param ox point's x position
	 * @param oy point's y position
	 * @return a point that's closest
	 */
	private Point getClosestPoint(int ox, int oy) {
		Point closest = null;

		for (Point p : position) {
			if (closest == null) {
				closest = p;
			} else if (Math.abs(p.getX() - ox) + Math.abs(p.getY() - oy) < Math.abs(closest.getX() - ox)
					+ Math.abs(closest.getY() - oy)) {
				closest = p;
			}
		}

		return closest;
	}

	/**
	 * scans vicinity of ai to "see" what there is
	 * @param distance how far we can "see"
	 * @return list of artifacts and other snakes found
	 */
	private ArrayList<Object> scanVicinity(int distance) {
		ArrayList<Object> relevantObjects = new ArrayList<Object>();
		int xhead = position.getFirst().getX();
		int yhead = position.getFirst().getY();

		for (Artifact a : game.getGrid().getArtifacts()) {
			if (inVicinity(xhead, yhead, a.getPlacement(), distance)) {
				relevantObjects.add(a);
			}
		}

		for (Snake s : game.getSnakes()) {
			if (!s.equals(this)) {
				// getBody()[0] is the head of another snake
				if (inVicinity(xhead, yhead, s.getBody()[0], distance)) { 
					relevantObjects.add(s);
				}
			}
		}

		return relevantObjects;
	}

	/**
	 * measures if other object is within distance
	 * @param xhead our head's xposition
	 * @param yhead our head's yposition
	 * @param other the other object
	 * @param distance the distance up to which can be seen
	 * @return if the other object is in the vicinity
	 */
	private boolean inVicinity(int xhead, int yhead, Point other, int distance) {
		int oxpos = other.getX();
		int oypos = other.getY();

		if (Math.abs(xhead - oxpos) <= distance && Math.abs(yhead - oypos) <= distance) {
			return true;
		}

		return false;
	}

}
