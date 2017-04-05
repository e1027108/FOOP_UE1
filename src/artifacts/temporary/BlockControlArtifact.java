package artifacts.temporary;

import artifacts.Artifact;
import artifacts.Artifacts;
import artifacts.logic.ArtifactConstants;
import artifacts.logic.ArtifactConstants.Setting;
import game.Point;

public class BlockControlArtifact extends Artifact implements TemporaryArtifact {

	private int duration;

	/**
	 * @param Point
	 *            placement
	 */
	public BlockControlArtifact(Point placement) {
		super(placement, ArtifactConstants.BLOCK_CONTROL_DESPAWN_TIMER, Artifacts.BLOCK_CONTROL);
		this.duration = ArtifactConstants.BLOCK_CONTROL_DURATION;
	}

	@Override
	public int getDuration() {
		return duration;
	}
}
