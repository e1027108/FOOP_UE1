package artifacts.permanent;

import artifacts.Artifact;
import artifacts.ArtifactConstants;
import artifacts.Artifacts;
import game.Point;

/**
 * This artifact is the only way to decrease a snakes size (which directly
 * influences maxHealth)
 */
public class SizeDecreaseArtifact extends Artifact {

	private int decrease;

	/**
	 * @param Point
	 *            placement
	 */
	public SizeDecreaseArtifact(Point placement, int despawnTimer, Artifacts type) {
		super(placement, despawnTimer, type);
		this.decrease = ArtifactConstants.SIZE_DECREASE;
	}

	public int getDecrease() {
		return decrease;
	}

	@Override
	public Artifacts getArtifactsMapping() {
		return super.getArtifactsMapping();
	}
}
