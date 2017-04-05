package game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the {@link Snake} interface and holds the start/default
 * values of the players snake.
 */
public class SnakeImpl implements Snake {

	private String name;

	// TODO: colour

	protected char direction;
	private int health;
	private double speed;
	private int size;
	private int timeToReact;

	private boolean alive;

	private Point lastTailPosition;

	protected ArrayDeque<Point> position;

	public SnakeImpl(String n, char dir) {
		name = n;
		direction = dir;
		health = 5;
		speed = 1.0;
		size = 4;
		timeToReact = 1;

		setAlive(true);

		position = new ArrayDeque<Point>();
	}

	@Override
	public void initPosition(int x, int y) { // TODO if char codes change, AI
												// must be changed too
		for (int i = 0; i < size; i++) {
			if (direction == 'N') {
				position.add(new Point(x + i, y));
			}
			if (direction == 'E') {
				position.add(new Point(x, y - i));
			}
			if (direction == 'S') {
				position.add(new Point(x - i, y));
			}
			if (direction == 'W') {
				position.add(new Point(x, y + i));
			}
		}
	}

	// TODO: move snake into defined direction
	@Override
	public void move() {
		Point head = position.peekFirst();
		if (direction == 'N') {
			position.addFirst(new Point(head.getX() - 1, head.getY()));
		}
		if (direction == 'S') {
			position.addFirst(new Point(head.getX() + 1, head.getY()));
		}
		if (direction == 'E') {
			position.addFirst(new Point(head.getX(), head.getY() + 1));
		}
		if (direction == 'W') {
			position.addFirst(new Point(head.getX(), head.getY() - 1));
		}
		lastTailPosition = position.pollLast();
	}

	@Override
	public char getDirection() {
		return direction;
	}

	@Override
	public Point[] getBody() {
		ArrayDeque<Point> pos = position.clone();
		Point[] body = new Point[size];
		for (int i = 0; i < size; i++) {
			body[i] = pos.poll();
		}
		return body;
	}

	@Override
	public List<Point> getBodyList() {
		List<Point> bodyList = new ArrayList<Point>();
		Point[] body = getBody();
		for (int i = 0; i < body.length; i++) {
			bodyList.add(body[i]);
		}

		return bodyList;
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	@Override
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	@Override
	public Point getLastTailPosition() {
		return lastTailPosition;
	}

	@Override
	public int getSize() {
		return this.size;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void changeDirection(char d) {
		if ((d == 'N' && direction != 'S') || 
			(d == 'S' && direction != 'N') || 
			(d == 'E' && direction != 'W') || 
			(d == 'W' && direction != 'E')) {
			direction = d;
		}
	}

	@Override
	public int getHealth() {
		return this.health;
	}

	@Override
	public double getSpeed() {
		return this.speed;
	}
}
