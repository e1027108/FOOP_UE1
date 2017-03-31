package game.ai;

import java.util.ArrayList;
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
import game.Game;
import game.GameGrid;
import game.Point;
import game.Snake;
import game.SnakeImpl;

public class SnakeAI extends SnakeImpl{

	private Game game;

	private static final double P_OTHER = 0.1;
	private static final int DISTANCE = 5; //TODO, if we use bigger grids (28 seems small), greater than 5 would be advised

	public SnakeAI(String name, char dir, Game game) {
		super(name,dir);

		this.game = game;
	}

	public void determineNextDirection(){
		char next;

		ArrayList<Object> closeObjects = scanVicinity(DISTANCE);

		System.out.println("test: " + closeObjects);

		Object important = getValuedObject(closeObjects); //TODO randomize which object is chosen a bit

		next = getPreferredDirection(important); //TODO prevent from going into itself!

		direction = next;
	}

	private char getPreferredDirection(Object goalObject) {
		char newdirection = direction; //TODO replace with error direction, if availiable
		double value;

		if(goalObject != null){
			value = valueObject(goalObject); //can't really get this and the object, or should i work with Pair<Object, Double> ?
		}
		else{
			return getRandomDirection(P_OTHER);
		}

		Point goalPosition = getGoalPosition(goalObject); //TODO reuse elsewhere
		int goalx = goalPosition.getX();
		int goaly = goalPosition.getY();
		int currx = position.getFirst().getX();
		int curry = position.getFirst().getY();

		if(currx < goalx && direction != 'W'){
			if(value > 0){
				newdirection = 'E';
			}
			else if(value < 0){
				newdirection = 'W';
			}
		}
		else if(currx > goalx && direction != 'E'){
			if(value > 0){
				newdirection = 'W';
			}
			else if(value < 0){
				newdirection = 'E';
			}
		}
		else if(curry < goaly && direction != 'S'){
			if(value > 0){
				newdirection = 'N';
			}
			else if(value < 0){
				newdirection = 'S';
			}
		}
		else if(curry > goaly && direction != 'N'){
			if(value > 0){
				newdirection = 'S';
			}
			else if(value < 0){
				newdirection = 'N';
			}
		}
		else{
			newdirection = direction;
		}
		
		//TODO, check if you'll bite yourself or maneuver into a self-biting position before returning direction

		return newdirection;
	}

	private Point getGoalPosition(Object goalObject) {
		if(goalObject instanceof Snake){
			return ((Snake) goalObject).getBody()[0];
		}
		else{ //hope there will never be a non-snake, non-artifact object
			return ((Artifact) goalObject).getPlacement();
		}
	}

	private char getRandomDirection(double otherProbability) { //otherProbability is the chance of a direction change
		Random r = new Random();

		if(r.nextDouble() <= otherProbability){
			String vertical = "NS";
			String horizontal = "EW";
			String directions;

			if(vertical.contains("" + direction)){
				directions = horizontal;
			}
			else {
				directions = vertical;
			}

			double rnd = r.nextDouble();

			System.out.println(rnd);

			if(rnd <= (double) 1/2){
				System.out.println("0");
				return directions.charAt(0);
			}
			else {
				System.out.println("1");
				return directions.charAt(1);
			}
		}
		else{
			return direction;
		}
	}

	private Object getValuedObject(ArrayList<Object> closeObjects) {
		double currentValue = 0;
		Object currentObject = null;

		if(closeObjects == null || closeObjects.isEmpty()){
			return currentObject;
		}

		for(Object o: closeObjects){
			double objectValue = valueObject(o);
			if(Math.abs(objectValue) > Math.abs(currentValue)){ //finds the most severe value
				currentValue = objectValue;
				currentObject = o;
			}
		}

		return currentObject;
	}

	/*
	 * value from -1 to +1
	 */ //TODO maybe push parts of this to a value file?
	private double valueObject(Object o) {
		double value = 0;
		int distance = measureDistance(o);

		if(o instanceof Snake){
			//TODO how much health do I have, am I invincible, etc...
		}
		else if(o instanceof Artifact){ //TODO all current values currently assume DISTANCE = 5, more general computation if changed
			//positive pickups
			if(o instanceof HealthIncreaseArtifact){ // 0: full health already, 0.1: overheal, [0.45,1] depending on distance and health
				value = .9 * ((double) 1 / distance); //high values, reducing in size if far away
				/*if(this.getHealth() - this.getHealth() < ((HealthIncreaseArtifact) o).getIncrease()){ //TODO replace left by this.getHealth-this.getMaxHealth to check for overheal
					value = .1;
				}
				else if(this.getHealth() == this.getHealth()){ //TODO replace by this.getHealth == this.getMaxHealth
					value = 0;
				}*/
			}
			else if(o instanceof SizeIncreaseArtifact){ //assumed, that you kinda always want to grow
				value = .4 * ((double) 1 / distance);
			}
			else if(o instanceof InvulnerabilityArtifact){ //TODO need info if already invulnerable
				/*if(this.isInvulnerable())
					value = 0;
				}
				else{*/
				value = 1 * ((double) 1 / distance);
				//}
			}
			else if(o instanceof SpeedIncreaseArtifact){ //assume you always want to become faster, but less if you already are faster
				if(this.getSpeed() > this.getSpeed()){ //TODO replace right by this.getStandardSpeed()
					value = .3 * ((double) 1 / distance);
				}
				else{
					value = .5 * ((double) 1 / distance);
				}
			}
			//negative pickups
			else if(o instanceof HealthDecreaseArtifact){//don't care if high health
				if(this.getHealth() == this.getHealth()){ //TODO compare with maximumhealth instead
					value = -(.1);
				}
				else if(this.getHealth() - ((HealthDecreaseArtifact) o).getDecrease() <= 0){
					value = -1;
				}
				else{
					double remainderPercentage = (this.getHealth() - ((HealthDecreaseArtifact) o).getDecrease())/this.getHealth(); //TODO by max health

					value = -.9 * (((double) 5 / distance) / 5) * ((remainderPercentage - 1) * -1); //the closer the less we want it, less health --> less desire
				}
			}
			else if(o instanceof SizeDecreaseArtifact){
				value = -.4 * (((double) 5 / distance) / 5); //we never want this
			}
			else if(o instanceof BlockControlArtifact){
				value = -.9 * (((double) 5 / distance) / 5); //we really never want this
			}
			else if(o instanceof ReverseControlArtifact){
				value = -1 * (((double) 5 / distance) / 5); //we can't stand this at all, this would actively do all the wrong things
			}
			else if(o instanceof SpeedDecreaseArtifact){
				if(this.getSpeed() > this.getSpeed()){ //TODO replace right by this.getStandardSpeed()
					value = -.3 * (((double) 5 / distance) / 5);
				}
				else{
					value = -.5 * (((double) 5 / distance) / 5);
				}
			}
			//as yet not defined artifacts
			else{
				value = 0;
			}
		}

		return value;
	}

	private int measureDistance(Object o) {
		int xhead = position.getFirst().getX();
		int yhead = position.getFirst().getY();
		int ox = 0;
		int oy = 0;
		int distance = 0;

		if(o instanceof Snake){
			ox = ((Snake) o).getBody()[0].getX();
			oy = ((Snake) o).getBody()[0].getY();

			Point closest = null;

			for(Point p: position){ //TODO repeat this above to figure out if positive or negative value
				if(closest == null){
					closest = p;
				}
				else if(Math.abs(p.getX()-ox)+Math.abs(p.getY()-oy) < Math.abs(closest.getX()-ox)+Math.abs(closest.getY()-oy)){
					closest = p;
				}
			}

			distance = Math.abs(closest.getX()-ox)+Math.abs(closest.getY()-oy);

			if(!closest.equals(position.getFirst())){ //other snake hunts me --> if the enemy snake needs to turn around + 1
				if(oy > closest.getY() && ((Snake) o).getDirection() == 'W' || oy < closest.getY() && ((Snake) o).getDirection() == 'E'){
					direction++;
				}
				if(ox > closest.getX() && ((Snake) o).getDirection() == 'N' || ox < closest.getX() && ((Snake) o).getDirection() == 'S'){
					direction++;
				}
			}
		}
		else if(o instanceof Artifact){
			ox = ((Artifact) o).getPlacement().getX();
			oy = ((Artifact) o).getPlacement().getY();

			distance = Math.abs(xhead-ox)+Math.abs(yhead-oy);
		}

		/*you can't turn around directly but must go around your own body
		 *--> add +2 if at same height/width since you must 1. change pos
		 *in relation to your body and 2. change back into that lane
		 */
		if(ox == xhead){ // x is up and down
			if(oy > yhead && direction == 'E' || oy < yhead && direction == 'W'){ //same would mean we are currently on it!
				direction += 2;
			}
		}
		if(oy == yhead){ // y is left and right
			if(ox > xhead && direction == 'S' || ox < xhead && direction == 'N'){ //same would mean we are currently on it!
				direction += 2;
			}
		}

		return distance;
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

		for(Artifact a: game.getGrid().getArtifacts()){
			if(inVicinity(xhead, yhead, a.getPlacement(), distance)){
				relevantObjects.add(a);
			}
		}

		for(Snake s: game.getSnakes()){
			if(!s.equals(this)){
				if(inVicinity(xhead, yhead, s.getBody()[0], distance)){ //getBody()[0] is the head of another snake, right?
					relevantObjects.add(s);
				}
			}
		}

		return relevantObjects;
	}

	private boolean inVicinity(int xhead, int yhead, Point opoint, int distance) {
		int xoverflow = 0;
		int yoverflow = 0;
		int oxpos = opoint.getX();
		int oypos = opoint.getY();
		GameGrid grid = game.getGrid();

		//checks if close to edge
		if(xhead + distance > grid.getSize()){
			xoverflow = xhead + distance - grid.getSize();
		}
		else if(xhead - distance < 0){
			xoverflow = xhead - distance;
		}

		if(yhead + distance > grid.getSize()){
			yoverflow = yhead + distance - grid.getSize();
		}
		else if(yhead - distance < 0){
			yoverflow = yhead - distance;
		}

		//checks the other side of the grid (up to distance), if close to edge
		if(xoverflow != 0 || yoverflow != 0){
			boolean xfine = true;
			boolean yfine = true;

			if(xoverflow < 0 && xoverflow + grid.getSize() >= oxpos){
				xfine = false;
			}
			else if(xoverflow > 0 && xoverflow < oxpos){
				xfine = false;
			}

			if(yoverflow < 0 && yoverflow + grid.getSize() >= oypos){
				yfine = false;
			}
			else if(yoverflow > 0 && yoverflow < oypos){
				yfine = false;
			}

			if(yfine && xfine){
				return true;
			}
		}
		//standard check
		else if(Math.abs(xhead-oxpos) <= distance && Math.abs(yhead-oypos) <= distance){
			return true;
		}

		return false;
	}

}
