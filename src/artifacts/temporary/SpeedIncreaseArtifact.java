package artifacts.temporary;

import artifacts.Artifact;
import artifacts.permanent.ArtifactSpawnFactors;

public class SpeedIncreaseArtifact extends Artifact implements TemporaryArtifact {

	private static final String SPEED_INCREASE = "SPEED_INCREASE";
	private int timer, increase;

	public SpeedIncreaseArtifact(int timer, int increase) {
		super(ArtifactSpawnFactors.getClassFactor(SPEED_INCREASE));
		this.timer = timer;
		this.increase = increase;
	}

	@Override
	public int getTimer() {
		return timer;
	}

	public int getIncrease() {
		return increase;
	}
}
