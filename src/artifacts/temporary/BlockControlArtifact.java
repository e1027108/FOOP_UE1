package artifacts.temporary;

import artifacts.Artifact;
import artifacts.ArtifactConstants;
import artifacts.Artifacts;
import game.Point;

public class BlockControlArtifact extends Artifact implements TemporaryArtifact {

	private int duration;

	/**
	 * @param Point
	 *            placement
	 */
	public BlockControlArtifact(Point placement, int despawnTimer, Artifacts type) {
		super(placement, despawnTimer, type);
		this.duration = ArtifactConstants.BLOCK_CONTROL_DURATION;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public Artifacts getArtifactsMapping() {
		return super.getArtifactsMapping();
	}
}
