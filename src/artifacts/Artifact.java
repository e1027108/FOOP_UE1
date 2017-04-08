package artifacts;

import artifacts.logic.ArtifactConstants;
import artifacts.logic.ArtifactConstants.Setting;
import game.GameGrid;
import game.Point;

/**
 * @author christoph <br>
 *         Base class for the artifacts found on the {@link GameGrid}.
 */
public abstract class Artifact {

	private Point placement;
	private long spawnTime;
	private int despawnTimer;
	protected String image;

	protected Artifact(Point placement, int despawnTimer, Artifacts type) {
		this.spawnTime = System.currentTimeMillis();
		this.placement = placement;
		this.despawnTimer = despawnTimer;
		this.image = (String) ArtifactConstants.artifactSettingsMap.get(type).get(Setting.IMAGE);
	}

	public Point getPlacement() {
		return placement;
	}

	public int getDespawnTimer() {
		return despawnTimer;
	}

	public String getImage() {
		return image;
	}

	public long getSpawnTime() {
		return spawnTime;
	}

}
