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
	private static final int DISTANCE = 5; //TODO, if we use bigger grids (28 seems little), greater than 5 would be advised

	public SnakeAI(String name, char dir, Game game) {
		super(name,dir);

		this.game = game;
	}

	public void determineNextDirection(){
		char next;

		ArrayList<Object> closeObjects = scanVicinity(DISTANCE,true);
		
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
		// TODO if we have a list of objects in range, we can value them
		// TODO currently, as described below, ther is no real option of getting all objects

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
		//TODO value possible outcomes
		double value = 0;

		if(o instanceof Snake){
			//TODO, how far away, how much health do I have, am I invincible, etc...
		}
		else if(o instanceof Artifact){
			//TODO, is it a positive artifact or not, how far away, etc...
		}

		return value;
	}

	/**
	 * scans vicinity of ai to "see" what there is
	 * @param distance how far we can "see"
	 * @param head check vicinity of head (true) or the body (false)//TODO feasible?
	 * @return list of artifacts and other snakes found
	 */
	private ArrayList<Object> scanVicinity(int distance, boolean head) {
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
