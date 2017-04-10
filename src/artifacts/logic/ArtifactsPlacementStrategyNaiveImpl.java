package artifacts.logic;

import java.util.ArrayList;
import java.util.List;

import artifacts.Artifact;
import artifacts.ArtifactFactory;
import artifacts.Artifacts;
import game.Game;
import game.GameGrid;
import game.Point;
import game.Snake;

/**
 * This class implements a quiet naive strategy of placing artifacts. Just
 * blacklist snakes and already placed artifacts - done.
 */
public class ArtifactsPlacementStrategyNaiveImpl implements ArtifactPlacementStrategy {

	private ArtifactCoordinateGenerator artifactCoordinateGenerator;
	private GameGrid gameGrid;
	private Game game;

	public ArtifactsPlacementStrategyNaiveImpl(ArtifactCoordinateGenerator artifactCoordinateGenerator,
			GameGrid gameGrid, Game game) {
		this.artifactCoordinateGenerator = artifactCoordinateGenerator;
		this.game = game;
		this.gameGrid = game.getGrid();
	}

	@Override
	public void placeArtifact(Artifacts artifactType) {
		Point point = artifactCoordinateGenerator.createPlacement(createBlackList());
		Artifact artifact = ArtifactFactory.getArtifact(point, artifactType);
		gameGrid.addArtifact(artifact);
	}

	@Override
	public List<Point> createBlackList() {
		List<Point> blackList = new ArrayList<Point>();

		for (Artifact art : this.gameGrid.getArtifacts()) {
			blackList.add(art.getPlacement());
		}

		for (Snake snek : this.game.getSnakes()) {
			blackList.addAll(snek.getBodyList());
		}

		System.out.println("---Black List---");
		for (Point p : blackList) {
			System.out.println(p.toString());
		}
		System.out.println("----------------");

		return blackList;
	}

}