package artifacts.logic;

import java.util.List;

import game.Point;

/**
 * This interface binds all generators to implement methods to calculate a
 * blacklist of forbidden points on the grid (body of a snake, other
 * artifacts,..).
 */
public interface ArtifactCoordinateGenerator {

	Point createPlacement(List<Point> blackList);

	List<Integer> getXFromBlackList(List<Point> blackList);

	List<Integer> getYFromBlackList(List<Point> blackList);
}
