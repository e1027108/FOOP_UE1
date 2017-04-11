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

	protected Directions direction;
	private static final int health = 5;
	private static final double speed = 1.0;
	private static final int size = 4;
	private static final int timeToReact = 1;

	private boolean alive;

	private Point lastTailPosition;

	protected ArrayDeque<Point> position;

	public SnakeImpl(String n, Directions dir) {
		name = n;
		direction = dir;
		
		setAlive(true);

		position = new ArrayDeque<Point>();
	}

	@Override
	public void initPosition(int x, int y) { // TODO if char codes change, AI
												// must be changed too
		for (int i = 0; i < size; i++) {
			if (direction == Directions.N) {
				position.add(new Point(x + i, y));
			}
			if (direction == Directions.E) {
				position.add(new Point(x, y - i));
			}
			if (direction == Directions.S) {
				position.add(new Point(x - i, y));
			}
			if (direction == Directions.W) {
				position.add(new Point(x, y + i));
			}
		}
	}

	// TODO: move snake into defined direction
	@Override
	public void move() {
		Point head = position.peekFirst();
		if (direction == Directions.N) {
			position.addFirst(new Point(head.getX() - 1, head.getY()));
		}
		if (direction == Directions.S) {
			position.addFirst(new Point(head.getX() + 1, head.getY()));
		}
		if (direction == Directions.E) {
			position.addFirst(new Point(head.getX(), head.getY() + 1));
		}
		if (direction == Directions.W) {
			position.addFirst(new Point(head.getX(), head.getY() - 1));
		}
		lastTailPosition = position.pollLast();
	}

	@Override
	public Directions getDirection() {
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
	public String getName() {
		return this.name;
	}

	@Override
	public void changeDirection(Directions d) {
		if ((d == Directions.N && direction != Directions.S) || (d == Directions.S && direction != Directions.N)
				|| (d == Directions.E && direction != Directions.W)
				|| (d == Directions.W && direction != Directions.E)) {
			direction = d;
		}
	}
	
	public int getCurrentHealth(int diff) {
		return health + diff;
		
	}
	
	public double getCurrentSpeed(double diff) {
		return speed + diff;
	}
	
	public int getCurrentSize(int diff) {
		return size + diff;
	}
	
	public int getCurrentTimeToReact(int diff) {
		return timeToReact + diff;
	}

	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public double getSpeed() {
		return speed;
	}
	
	@Override
	public int getSize() {
		return size;
	}

}
