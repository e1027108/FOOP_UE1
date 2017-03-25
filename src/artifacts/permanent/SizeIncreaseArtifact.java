package artifacts.permanent;

import artifacts.Artifact;
import artifacts.logic.ArtifactConstants;
import game.Point;

public class SizeIncreaseArtifact extends Artifact {

	int increase;

	/**
	 * @param Point
	 *            placement
	 */
	public SizeIncreaseArtifact(Point placement) {
		super(placement, ArtifactConstants.SIZE_INCREASE_DESPAWN_TIMER);
		this.increase = ArtifactConstants.SIZE_INCREASE;
	}

	public int getIncrease() {
		return increase;
	}
}
