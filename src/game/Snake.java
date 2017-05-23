package game;

import java.util.List;

public interface Snake {

	void move();

	Directions getDirection();

	void changeDirection(Directions d);

	Point[] getBody();

	List<Point> getBodyList();

	void initPosition(int x, int y);

	void resetPosition(Point point, int gridSize);

	boolean isAlive();

	void setAlive(boolean alive);

	String getName();

	int getHealth();
	
	int getMaxHealth();

	double getSpeed();
	
	int getSize();

	int getGridID();

	List<Point> getDeadParts();

	void clearDeadParts();

	void setInvulnerability(boolean invulnerability);

	boolean isInvulnerable();

	void changeSizeModifier(int change);

	void changeHealthModifier(int change);

	void changeSpeedIncrease(int change);

	void changeSpeedDecrease(int change);

	boolean hasBlockControl();

	void setBlockControl(boolean blockControl);

	boolean hasReverseControl();

	void setReverseControl(boolean reverseControl);

	void checkStatus();

	void printStatus();

	boolean checkHealth();
}
