package game.ai;

import java.util.ArrayList;
import java.util.Random;

import artifacts.Artifact;
import game.GameGrid;
import game.Point;
import game.Snake;
import game.SnakeImpl;

public class SnakeAI extends SnakeImpl{

	private GameGrid home;
	
	private static final double P_OTHER = 0.1;

	public SnakeAI(String name, char dir, GameGrid home) {
		super(name,dir);

		this.home = home;
	}

	public void determineNextDirection(){
		char next;

		ArrayList<Object> closeObjects = scanVicinity(5,true);

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
	 * @param head check vicinity of head (true) or the body (false)
	 * @return list of artifacts and other snakes found
	 */
	private ArrayList<Object> scanVicinity(int distance, boolean head) {
		//TODO go through list of artifacts or points (that hold artifacts)
		//TODO need also list of snakes
		
		return null;
	}

}
