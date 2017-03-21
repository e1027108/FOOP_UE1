package artifacts.permanent;

import artifacts.Artifact;

public class HealthDecreaseArtifact extends Artifact {

	private static final String HEALTH_DECREASE = "HEALTH_DECREASE";
	private int decrease;

	public HealthDecreaseArtifact(int decrease, int spawnFactor) {
		super(ArtifactSpawnFactors.getClassFactor(HEALTH_DECREASE));
		this.decrease = decrease;
	}

	public int getDecrease() {
		return decrease;
	}
}
