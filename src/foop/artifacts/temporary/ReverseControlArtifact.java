package foop.artifacts.temporary;

import foop.artifacts.Artifact;

public class ReverseControlArtifact extends Artifact implements TemporaryArtifact {

	private int timer;

	public ReverseControlArtifact(int timer) {
		this.timer = timer;
	}

	@Override
	public int getTimer() {
		return timer;
	}
}
