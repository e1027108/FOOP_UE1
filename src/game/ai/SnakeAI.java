package game.ai;

import java.util.ArrayList;
import java.util.Random;

import artifacts.Artifact;
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

		Object important = getValuedObject(closeObjects);

		next = getPreferredDirection(important);

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

		if(value > 0){
			//TODO choose a direction that reduces distance
		}
		else{
			//TODO choose a direction that increases distance/collision "probability"
		}

		//TODO, check if you'll bite yourself or maneuver into a self-biting position before returning direction

		return newdirection;
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
	 * value from -100 to +100
	 */
	private double valueObject(Object o) {
		double value = 0;
		int distance = measureDistance(o);

		if(o instanceof Snake){
			//TODO how much health do I have, am I invincible, etc...
		}
		else if(o instanceof Artifact){
			//TODO, is it a positive artifact or not, etc...
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

		for(Artifact a: game.getActiveChildren()){
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
