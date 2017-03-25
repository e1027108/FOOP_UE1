package artifacts.temporary;

import artifacts.Artifact;
import artifacts.logic.ArtifactConstants;
import game.Point;

public class SpeedIncreaseArtifact extends Artifact implements TemporaryArtifact {

	private int duration, increase;

	/**
	 * @param Point
	 *            placement
	 */
	public SpeedIncreaseArtifact(Point placement) {
		super(placement, ArtifactConstants.SPEED_INCREASE_DESPAWN_TIMER);
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
}
