package artifacts.temporary;

import artifacts.Artifact;

public class SpeedDecreaseArtifact extends Artifact implements TemporaryArtifact {

	private int timer, decrease;

	public SpeedDecreaseArtifact(int timer, int decrease) {
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
