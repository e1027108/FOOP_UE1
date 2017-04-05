package artifacts.permanent;

import artifacts.Artifact;
import artifacts.Artifacts;
import artifacts.logic.ArtifactConstants;
import game.Point;

public class HealthIncreaseArtifact extends Artifact {

	private int increase;

	/**
	 * @param Point
	 *            placement
	 */
	public HealthIncreaseArtifact(Point placement) {
		super(placement, ArtifactConstants.HEALTH_INCREASE_DESPAWN_TIMER, Artifacts.HEALTH_INCREASE);
		this.increase = ArtifactConstants.HEALTH_INCREASE;
	}

	public int getIncrease() {
		return increase;
	}
}
