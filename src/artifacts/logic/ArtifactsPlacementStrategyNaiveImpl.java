package artifacts.logic;

import game.GameGrid;
import game.Point;

public class ArtifactsPlacementStrategyNaiveImpl implements ArtifactPlacementStrategy {

	private ArtifactCoordinateGenerator artifactCoordinateGenerator;

	public ArtifactsPlacementStrategyNaiveImpl(ArtifactCoordinateGenerator artifactCoordinateGenerator) {
		this.artifactCoordinateGenerator = artifactCoordinateGenerator;
	}

	@Override
	public void placeArtifact(GameGrid gameGrid) {
		Point point = artifactCoordinateGenerator.createPlacement();
		// TODO add Point to (?) GameGrid
	}

}