package artifacts.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import artifacts.Artifacts;
import artifacts.logic.ArtifactConstants.Setting;
import game.Game;
import game.GameGrid;

public class ArtifactHandlerImpl implements ArtifactHandler {

	private ArtifactPlacementStrategy artifactPlacementStrategy;
	private ArtifactSpawnTimer artifactSpawnTimer;

	public ArtifactHandlerImpl(Game game) {
		this.artifactPlacementStrategy = new ArtifactsPlacementStrategyNaiveImpl(
				new ArtifactCoordinateGeneratorImpl(game.getGrid()), game.getGrid(), game);
		this.artifactSpawnTimer = new ArtifactSpawnTimerRandomImpl();
	}

	/**
	 * This method takes following steps to place a new artifact on the
	 * {@link GameGrid} <br>
	 * - determine type of the next artifact <br>
	 * - get amount of time to wait and <br>
	 * {@link Thread}.sleep it out. <br>
	 * - hand the type over to {@link ArtifactPlacementStrategy}, which calles
	 * {@link ArtifactCoordinateGenerator} and places the artifact on the grid
	 */
	@Override
	public void placeNextArtifact() {

		Artifacts type = getNextArtifactType();
		int spawnTimer = artifactSpawnTimer.getSpawnTime();
		try {
			Thread.sleep(spawnTimer);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		artifactPlacementStrategy.placeArtifact(type);
	}

	@Override
	public Artifacts getNextArtifactType() {
		Random random = new Random();
		List<Artifacts> artifacts = new ArrayList<Artifacts>();
		for (Artifacts art : Artifacts.values()) {
			for (int i = 0; i < (Integer) ArtifactConstants.artifactSettingsMap.get(art)
					.get(Setting.SPAWN_FACTOR); i++) {
				artifacts.add(art);
			}
		}
		return artifacts.get(random.nextInt(artifacts.size()));
	}

}
