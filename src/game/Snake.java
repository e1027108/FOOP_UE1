package game;

import java.util.List;

public interface Snake {

	void move();

	Directions getDirection();

	void changeDirection(Directions d);

	Point[] getBody();

	List<Point> getBodyList();

	void initPosition(int x, int y);

	boolean isAlive();

	void setAlive(boolean alive);

	Point getLastTailPosition();

	String getName();

	int getHealth();

	double getSpeed();
	
	int getSize();

	int getGridID();
}
