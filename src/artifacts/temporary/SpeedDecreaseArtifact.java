package artifacts.temporary;

import artifacts.Artifact;
import artifacts.logic.ArtifactConstants;
import game.Point;

public class SpeedDecreaseArtifact extends Artifact implements TemporaryArtifact {

	private int duration, decrease;

	/**
	 * @param Point
	 *            placement
	 */
	public SpeedDecreaseArtifact(Point placement) {
		super(placement, ArtifactConstants.SPEED_DECREASE_DESPAWN_TIMER);
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
}
