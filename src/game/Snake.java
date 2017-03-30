package game;

import java.util.List;

/**
 * @author christoph <br>
 *         This interface ties {@link SnakeImpl} and the decorator
 *         {@link SnakeModifier} together.
 */
public interface Snake {

	void move();

	char getDirection();

	void setDirection(char d);

	Point[] getBody();

	List<Point> getBodyList();

	int getSize();

	void initPosition(int x, int y);

	boolean isAlive();

	void setAlive(boolean alive);

	Point getLastTailPosition();

	String getName();

	int getHealth();

	double getSpeed();
}
