package foop.artifacts.temporary;

import foop.artifacts.Artifact;

public class BlockControlArtifact extends Artifact implements TemporaryArtifact {

	private int timer;

	public BlockControlArtifact(int timer) {
		this.timer = timer;
	}

	@Override
	public int getTimer() {
		return timer;
	}
}
