package artifacts.permanent;

import artifacts.Artifact;
import artifacts.ArtifactConstants;
import artifacts.Artifacts;
import game.Point;

/**
 * This artifact increases the actual health of a snake (up to maxHealth) by a
 * given value (stored in {@link ArtifactConstants})
 */
public class HealthIncreaseArtifact extends Artifact {

	private int increase;

	/**
	 * @param Point
	 *            placement
	 */
	public HealthIncreaseArtifact(Point placement, int despawnTimer, Artifacts type) {
		super(placement, despawnTimer, type);
		this.increase = ArtifactConstants.HEALTH_INCREASE;
	}

	public int getIncrease() {
		return increase;
	}

	@Override
	public Artifacts getArtifactsMapping() {
		return super.getArtifactsMapping();
	}
}
