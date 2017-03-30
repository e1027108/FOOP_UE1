package artifacts.logic;

import java.util.ArrayList;
import java.util.List;

import artifacts.Artifact;
import artifacts.Artifacts;
import artifacts.permanent.HealthDecreaseArtifact;
import artifacts.permanent.HealthIncreaseArtifact;
import artifacts.permanent.SizeDecreaseArtifact;
import artifacts.permanent.SizeIncreaseArtifact;
import artifacts.temporary.BlockControlArtifact;
import artifacts.temporary.InvulnerabilityArtifact;
import artifacts.temporary.ReverseControlArtifact;
import artifacts.temporary.SpeedDecreaseArtifact;
import artifacts.temporary.SpeedIncreaseArtifact;
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
		Artifact artifact = null;
		switch (artifactType) {
		case HEALTH_INCREASE:
			artifact = new HealthIncreaseArtifact(point);
			break;
		case HEALTH_DECREASE:
			artifact = new HealthDecreaseArtifact(point);
			break;
		case SIZE_INCREASE:
			artifact = new SizeIncreaseArtifact(point);
			break;
		case SIZE_DECREASE:
			artifact = new SizeDecreaseArtifact(point);
			break;
		case BLOCK_CONTROL:
			artifact = new BlockControlArtifact(point);
			break;
		case INVULNERABILITY:
			artifact = new InvulnerabilityArtifact(point);
			break;
		case REVERSE_CONTROL:
			artifact = new ReverseControlArtifact(point);
			break;
		case SPEED_INCREASE:
			artifact = new SpeedIncreaseArtifact(point);
			break;
		case SPEED_DECREASE:
			artifact = new SpeedDecreaseArtifact(point);
			break;
		default:
			break;
		}
		artifact.start();
		this.game.addChild(artifact);
	}

	@Override
	public List<Point> createBlackList() {
		List<Point> blackList = new ArrayList<Point>();

		for (Artifact art : this.game.getActiveChildren()) {
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