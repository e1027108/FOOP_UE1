package foop.artifacts.permanent;

import foop.artifacts.Artifact;

public class HealthDecreaseArtifact extends Artifact {

	private int decrease;

	public HealthDecreaseArtifact(int decrease) {
		this.decrease = decrease;
	}

	public int getDecrease() {
		return decrease;
	}
}
