package artifacts.temporary;

import artifacts.Artifact;
import artifacts.ArtifactConstants;
import artifacts.Artifacts;
import game.Point;

public class SpeedDecreaseArtifact extends Artifact implements TemporaryArtifact {

	private int duration, decrease;

	/**
	 * @param Point
	 *            placement
	 */
	public SpeedDecreaseArtifact(Point placement, int despawnTimer, Artifacts type) {
		super(placement, despawnTimer, type);
		this.duration = ArtifactConstants.SPEED_DECREASE_DURATION;
		this.decrease = ArtifactConstants.SPEED_DECREASE;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	public int getDecrease() {
		return decrease;
	}

	@Override
	public Artifacts getArtifactsMapping() {
		return super.getArtifactsMapping();
	}
}
