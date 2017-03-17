package foop.artifacts.permanent;

import foop.artifacts.Artifact;

public class HealthIncreaseArtifact extends Artifact {

	private int increase;

	public HealthIncreaseArtifact(int increase) {
		this.increase = increase;
	}

	public int getIncrease() {
		return increase;
	}
}
