package artifacts.permanent;

import artifacts.Artifact;

public class HealthIncreaseArtifact extends Artifact {

	private static final String HEALTH_INCREASE = "HEALTH_INCREASE";
	private int increase;

	public HealthIncreaseArtifact(int increase) {
		super(ArtifactSpawnFactors.getClassFactor(HEALTH_INCREASE));
		this.increase = increase;
	}

	public int getIncrease() {
		return increase;
	}
}
