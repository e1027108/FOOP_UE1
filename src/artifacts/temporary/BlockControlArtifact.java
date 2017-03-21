package artifacts.temporary;

import artifacts.Artifact;
import artifacts.permanent.ArtifactSpawnFactors;

public class BlockControlArtifact extends Artifact implements TemporaryArtifact {

	private static final String BLOCK_CONTROL = "BLOCK_CONTROL";
	private int timer;

	public BlockControlArtifact(int timer) {
		super(ArtifactSpawnFactors.getClassFactor(BLOCK_CONTROL));
		this.timer = timer;
	}

	@Override
	public int getTimer() {
		return timer;
	}
}
