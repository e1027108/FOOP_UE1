package artifacts.permanent;

import artifacts.Artifact;

public class SizeIncreaseArtifact extends Artifact {

	int increase;

	public SizeIncreaseArtifact(int increase) {
		this.increase = increase;
	}

	public int getIncrease() {
		return increase;
	}
}
