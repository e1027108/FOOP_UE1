package artifacts.temporary;

import artifacts.Artifact;
import artifacts.permanent.ArtifactSpawnFactors;

public class ReverseControlArtifact extends Artifact implements TemporaryArtifact {

	private static final String REVERSE_CONTROL = "REVERSE_CONTROL";
	private int timer;

	public ReverseControlArtifact(int timer) {
		super(ArtifactSpawnFactors.getClassFactor(REVERSE_CONTROL));
		this.timer = timer;
	}

	@Override
	public int getTimer() {
		return timer;
	}
}
