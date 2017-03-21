package artifacts.permanent;

import artifacts.Artifact;

public class SizeDecreaseArtifact extends Artifact {

	private static final String SIZE_DECREASE = "SIZE_DECREASE";
	private int decrease;

	public SizeDecreaseArtifact(int decrease) {
		super(ArtifactSpawnFactors.getClassFactor(SIZE_DECREASE));
		this.decrease = decrease;
	}

	public int getDecrease() {
		return decrease;
	}
}
