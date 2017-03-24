package game.ai;

import java.util.ArrayList;

import artifacts.Artifact;
import game.GameGrid;
import game.Point;
import game.Snake;
import game.SnakeImpl;

public class SnakeAI extends SnakeImpl{

	private int distance;
	private GameGrid home;
	
	public SnakeAI(String name, char dir, int distance, GameGrid home) {
		super(name,dir);
		
		this.distance = distance;
		this.home = home;
	}
	
	public void determineNextDirection(){
		char next;
		
		ArrayList<Object> closeObjects = scanVicinity(distance,true);
		
		Object important = getValuedObject(closeObjects);
		
		next = getPreferredDirection(important);
		
		direction = next;
	}

	private char getPreferredDirection(Object important) {
		char newdirection = 'N'; //TODO replace with error direction, if availiable
		double value = valueObject(important); //can't really get this and the object, or should i work with Pair<Object, Double> ?
		
		if(value > 0){
			//TODO choose a direction that reduces distance
		}
		else{
			//TODO choose a direction that increases distance/collision "probability"
		}
		
		return newdirection;
	}

	private Object getValuedObject(ArrayList<Object> closeObjects) {
		// TODO if we have a list of objects in range, we can value them
		// TODO currently, as described below, ther is no real option of getting all objects
		
		double currentValue = 0;
		Object currentObject = null;
		
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
