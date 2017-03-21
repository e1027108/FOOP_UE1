package artifacts.permanent;

import artifacts.Artifact;

public class SizeIncreaseArtifact extends Artifact {

	private static final String SIZE_INCREASE = "SIZE_INCREASE";
	int increase;

	public SizeIncreaseArtifact(int increase) {
		super(ArtifactSpawnFactors.getClassFactor(SIZE_INCREASE));
		this.increase = increase;
	}

	public int getIncrease() {
		return increase;
	}
}
