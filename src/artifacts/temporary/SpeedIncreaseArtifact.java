package artifacts.temporary;

import artifacts.Artifact;
import artifacts.ArtifactConstants;
import artifacts.Artifacts;
import game.Point;

public class SpeedIncreaseArtifact extends Artifact implements TemporaryArtifact {

	private int duration, increase;

	/**
	 * @param Point
	 *            placement
	 */
	public SpeedIncreaseArtifact(Point placement, int despawnTimer, Artifacts type) {
		super(placement, despawnTimer, type);
		this.duration = ArtifactConstants.SPEED_INCREASE_DURATION;
		this.increase = ArtifactConstants.SPEED_INCREASE;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	public int getIncrease() {
		return increase;
	}

	@Override
	public Artifacts getArtifactsMapping() {
		return super.getArtifactsMapping();
	}
}
