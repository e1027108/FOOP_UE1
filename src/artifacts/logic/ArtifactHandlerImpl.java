package artifacts.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import artifacts.Artifacts;
import artifacts.logic.ArtifactConstants.Setting;
import game.Game;
import game.GameGrid;
import game.Snake;

/**
 * TODO only place artifacts when at least one snake is alive!
 */
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
	public void placeNextArtifact() {
		Artifacts type = getNextArtifactType();
		artifactPlacementStrategy.placeArtifact(type);
	}

	/**
	 * This method adds each type of artifact to a list exactly [individual
	 * SPAWN_FACTOR of this class] times to realize different chances on being
	 * chosen as the next artifact.
	 */
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

	// TODO snek.isAlive. what happens if you move a snake against the wall? is
	// it then alive/dead?
	// if not, change below!
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		System.out.println("--- Started artifact spawn thread ---");
		while (true) {
			List<Snake> sneks = (List<Snake>) this.game.getSnakes().clone();
			try {
				for (Snake snek : sneks) {
					if (snek.isAlive()) {
						Thread.sleep(this.artifactSpawnTimer.getSpawnTime() * 1000);
						System.out.println("--- trying to place next artifact ---");
						placeNextArtifact();
					}
				}
			} catch (InterruptedException e) {
				System.out.println("--- interrupted artifact handler thread ---");
			}
		}
	}

}
