package artifacts.temporary;

import artifacts.Artifact;
import artifacts.logic.ArtifactConstants;
import game.Point;

public class ReverseControlArtifact extends Artifact implements TemporaryArtifact {

	private int duration;

	/**
	 * @param Point
	 *            placement
	 */
	public ReverseControlArtifact(Point placement) {
		super(placement, ArtifactConstants.REVERSE_CONTROL_DESPAWN_TIMER);
		this.duration = ArtifactConstants.REVERSE_CONTROL_DURATION;
	}

	@Override
	public int getDuration() {
		return duration;
	}
}
