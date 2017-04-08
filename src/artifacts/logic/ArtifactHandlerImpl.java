package artifacts.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import artifacts.Artifact;
import artifacts.Artifacts;
import artifacts.logic.ArtifactConstants.Setting;
import game.Game;
import game.GameGrid;
import game.Snake;

public class ArtifactHandlerImpl implements ArtifactHandler {

	private ArtifactPlacementStrategy artifactPlacementStrategy;
	private ArtifactSpawnTimer artifactSpawnTimer;
	private Game game;

	public ArtifactHandlerImpl(Game game) {
		this.game = game;
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
	 * 
	 * @throws InterruptedException
	 */
	@Override
	public void placeNextArtifact() throws InterruptedException {
		for (Snake snek : this.game.getSnakes()) {
			if (snek.isAlive()) {
				Artifacts type = getNextArtifactType();
				int spawnTimer = artifactSpawnTimer.getSpawnTime();
				Thread.sleep(spawnTimer * 1000);
				artifactPlacementStrategy.placeArtifact(type);
				break;
			}
		}
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

	@Override
	public void run() {
		while (true) {
			try {
				placeNextArtifact();
			} catch (InterruptedException e) {
				System.out.println(this.getClass().getName() + ": Caught Interrupt - shutting down");
				return;
			}
		}
	}

	@Override
	public void checkDespawn() {
		for (Artifact art : this.game.getGrid().getArtifacts()) {
			if (System.currentTimeMillis() <= (art.getSpawnTime() + art.getDespawnTimer())) {
				this.game.getGrid().getArtifacts().remove(art);
			}
		}
	}
}
