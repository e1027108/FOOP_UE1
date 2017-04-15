package artifacts.permanent;

import artifacts.Artifact;
import artifacts.ArtifactConstants;
import artifacts.Artifacts;
import game.Point;

/**
 * This artifact is the only way to increase a snakes size (which directly
 * influences maxHealth)
 */
public class SizeIncreaseArtifact extends Artifact {

	private int increase;

	/**
	 * @param Point
	 *            placement
	 */
	public SizeIncreaseArtifact(Point placement, int despawnTimer, Artifacts type) {
		super(placement, despawnTimer, type);
		this.increase = ArtifactConstants.SIZE_INCREASE;
	}

	public int getIncrease() {
		return increase;
	}

	@Override
	public Artifacts getArtifactsMapping() {
		return super.getArtifactsMapping();
	}
}
