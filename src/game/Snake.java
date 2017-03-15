package game;

import java.util.ArrayDeque;
import java.util.Queue;

public class Snake {
	
	private String name;
	
	//	TODO: colour
	
	private char direction;
	private int health;
	private double speed;
	private int size;
	private int timeToReact;
	
	private Queue<Point> position;
	
	
	public Snake(String n, char dir) {
		name = n;
		direction = dir;
		health = 5;
		speed = 1.0;
		size = 4;
		timeToReact = 1;
		
		position = new ArrayDeque<Point>();
	}
	
	public void initPosition(int x, int y) {
		for(int i = 0; i < size; i++) {
			if(direction == 'N') {
				position.add(new Point(x+i, y));
			}
			if(direction == 'E') {
				position.add(new Point(x, y-i));
			}
			if(direction == 'S') {
				position.add(new Point(x-i, y));
			}
			if(direction == 'W') {
				position.add(new Point(x, y+i));
			}
		}
	}
	
	//	TODO: move snake into defined direction
	public void move() {
		
	}
	
	public char getDirection() {
		return direction;
	}
	
	public Point [] getBody() {
		Point [] body = new Point[size];
		for(int i = 0; i < size; i++) {
			body[i] = position.poll();
		}
		return body;
	}

}
