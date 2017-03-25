package artifacts.permanent;

import artifacts.Artifact;
import artifacts.logic.ArtifactConstants;
import game.Point;

public class HealthDecreaseArtifact extends Artifact {

	private int decrease;

	/**
	 * @param Point
	 *            placement
	 */
	public HealthDecreaseArtifact(Point placement) {
		super(placement, ArtifactConstants.HEALTH_DECREASE_DESPAWN_TIMER);
		this.decrease = ArtifactConstants.HEALTH_DECREASE;
	}

	public int getDecrease() {
		return decrease;
	}
}
