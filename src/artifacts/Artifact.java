package artifacts;

import game.GameGrid;

/**
 * @author christoph <br>
 *         Base class for the artifacts found on the {@link GameGrid}.
 */
public abstract class Artifact {

	private int spawnFactor;

	protected Artifact(int spawnFactor) {
		this.spawnFactor = spawnFactor;
	}

	public int getSpawnFactor() {
		return spawnFactor;
	}

}
