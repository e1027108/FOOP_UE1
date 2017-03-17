package game;

/**
 * @author christoph <br>
 *         This interface ties {@link SnakeImpl} and the decorator
 *         {@link SnakeModifier} together.
 */
public interface Snake {

	void move();

	char getDirection();

	Point[] getBody();

	int getSize();

	void initPosition(int x, int y);
}

package game;

import java.util.ArrayDeque;
import java.util.Queue;

public class Snake {

	private String name;

	// TODO: colour

	private char direction;
	private int health;
	private double speed;
	private int size;
	private int timeToReact;

	private boolean alive;

	private Point lastTailPosition;

	private ArrayDeque<Point> position;

	public Snake(String n, char dir) {
		name = n;
		direction = dir;
		health = 5;
		speed = 1.0;
		size = 4;
		timeToReact = 1;

		setAlive(true);

		position = new ArrayDeque<Point>();
	}

	public void initPosition(int x, int y) {
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
	public void move() {
		Point head = position.peekFirst();
		if (direction == 'N') {
			position.addFirst(new Point(head.getX() - 1, head.getY()));
		}
		if (direction == 'S') {
			position.addFirst(new Point(head.getX() + 1, head.getY()));
		}
		if (direction == 'E') {
			position.addFirst(new Point(head.getX(), head.getY() - 1));
		}
		if (direction == 'W') {
			position.addFirst(new Point(head.getX(), head.getY() + 1));
		}
		lastTailPosition = position.pollLast();
	}

	public char getDirection() {
		return direction;
	}

	public Point[] getBody() {
		ArrayDeque<Point> pos = position.clone();
		Point[] body = new Point[size];
		for (int i = 0; i < size; i++) {
			body[i] = pos.poll();
		}
		return body;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Point getLastTailPosition() {
		return lastTailPosition;
	}

}

