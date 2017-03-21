package artifacts.temporary;

import artifacts.Artifact;
import artifacts.permanent.ArtifactSpawnFactors;

public class SpeedDecreaseArtifact extends Artifact implements TemporaryArtifact {

	private static final String SPEED_DECREASE = "SPEED_DECREASE";
	private int timer, decrease;

	public SpeedDecreaseArtifact(int timer, int decrease) {
		super(ArtifactSpawnFactors.getClassFactor(SPEED_DECREASE));
		this.timer = timer;
		this.decrease = decrease;
	}

	@Override
	public int getTimer() {
		return timer;
	}

	public int getDecrease() {
		return decrease;
	}
}
