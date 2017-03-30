package artifacts;

import game.GameGrid;
import game.Point;

/**
 * @author christoph <br>
 *         Base class for the artifacts found on the {@link GameGrid}.
 */
public abstract class Artifact extends Thread {

	private Point placement;
	private int despawnTimer;
	private boolean active;

	protected Artifact(Point placement, int despawnTimer) {
		this.placement = placement;
		this.despawnTimer = despawnTimer;
		this.active = true;
	}

	@Override
	public void run() {
		System.out.println("--- Artifact thread started ---");
		try {
			Thread.sleep(despawnTimer * 1000);
			active = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Point getPlacement() {
		return placement;
	}

	public int getDespawnTimer() {
		return despawnTimer;
	}

	public boolean isActive() {
		return active;
	}

}
