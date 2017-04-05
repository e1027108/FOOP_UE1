package artifacts.permanent;

import artifacts.Artifact;
import artifacts.Artifacts;
import artifacts.logic.ArtifactConstants;
import game.Point;

public class SizeDecreaseArtifact extends Artifact {

	private int decrease;

	/**
	 * @param Point
	 *            placement
	 */
	public SizeDecreaseArtifact(Point placement) {
		super(placement, ArtifactConstants.SIZE_DECREASE_DESPAWN_TIMER, Artifacts.SIZE_DECREASE);
		this.decrease = ArtifactConstants.SIZE_DECREASE;
	}

	public int getDecrease() {
		return decrease;
	}
}
