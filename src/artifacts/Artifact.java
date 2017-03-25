package artifacts;

import game.GameGrid;
import game.Point;

/**
 * @author christoph <br>
 *         Base class for the artifacts found on the {@link GameGrid}.
 */
public abstract class Artifact {

	private Point placement;
	private int despawnTimer;

	protected Artifact(Point placement, int despawnTimer) {
		this.placement = placement;
		this.despawnTimer = despawnTimer;
	}

	public Point getPlacement() {
		return placement;
	}

	public int getDespawnTimer() {
		return despawnTimer;
	}

}
