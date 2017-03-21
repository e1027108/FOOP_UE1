package artifacts.temporary;

import artifacts.Artifact;

public class SpeedIncreaseArtifact extends Artifact implements TemporaryArtifact {

	private int timer, increase;

	public SpeedIncreaseArtifact(int timer, int increase) {
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
