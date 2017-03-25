package artifacts.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.GameGrid;
import game.Point;

public class ArtifactCoordinateGeneratorImpl implements ArtifactCoordinateGenerator {

	private Random random;
	private GameGrid gameGrid;

	/**
	 * @param GameGrid
	 *            gameGrid
	 */
	public ArtifactCoordinateGeneratorImpl(GameGrid gameGrid) {
		this.random = new Random();
		this.gameGrid = gameGrid;
	}

	@Override
	public Point createPlacement(List<Point> blackList) {
		List<Integer> possibleX = new ArrayList<Integer>();
		List<Integer> possibleY = new ArrayList<Integer>();
		for (int i = 0; i < gameGrid.getSize(); i++) {
			possibleX.add(i);
			possibleY.add(i);
		}

		List<Integer> listX = getXFromBlackList(blackList);
		List<Integer> listY = getYFromBlackList(blackList);

		possibleX.removeAll(listX);
		possibleY.removeAll(listY);

		int x = possibleX.get(random.nextInt(possibleX.size()));
		int y = possibleY.get(random.nextInt(possibleY.size()));
		return new Point(x, y);
	}

	@Override
	public List<Integer> getXFromBlackList(List<Point> blackList) {
		List<Integer> listX = new ArrayList<Integer>();
		for (Point p : blackList) {
			listX.add(p.getX());
		}
		return listX;
	}

	@Override
	public List<Integer> getYFromBlackList(List<Point> blackList) {
		List<Integer> listY = new ArrayList<Integer>();
		for (Point p : blackList) {
			listY.add(p.getY());
		}
		return listY;
	}

}
