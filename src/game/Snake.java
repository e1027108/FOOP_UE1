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
