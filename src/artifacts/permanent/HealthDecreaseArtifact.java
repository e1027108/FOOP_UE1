package artifacts.permanent;

import artifacts.Artifact;
import artifacts.ArtifactConstants;
import artifacts.Artifacts;
import game.Point;

/**
 * This artifact decreases the actual health of a snake by a given value (stored
 * in {@link ArtifactConstants}). Can kill a snake
 */
public class HealthDecreaseArtifact extends Artifact {

	private int decrease;

	/**
	 * @param Point
	 *            placement
	 */
	public HealthDecreaseArtifact(Point placement, int despawnTimer, Artifacts type) {
		super(placement, despawnTimer, type);
		this.decrease = ArtifactConstants.HEALTH_DECREASE;
	}

	public int getDecrease() {
		return decrease;
	}

	@Override
	public Artifacts getArtifactsMapping() {
		return super.getArtifactsMapping();
	}
}
