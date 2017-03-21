package artifacts.permanent;

import artifacts.Artifact;

/**
 * This class centralizes the spawn time factors for the different
 * {@link Artifact} subclasses.
 */
public class ArtifactSpawnFactors {

	// permanent effects
	private static final String HEALTH_INCREASE = "HEALTH_INCREASE";
	private static final String HEALTH_DECREASE = "HEALTH_DECREASE";
	private static final String SIZE_INCREASE = "SIZE_INCREASE";
	private static final String SIZE_DECREASE = "SIZE_DECREASE";

	// temporary effects
	private static final String BLOCK_CONTROL = "BLOCK_CONTROL";
	private static final String INVULNERABILITY = "INVULNERABILITY";
	private static final String REVERSE_CONTROL = "REVERSE_CONTROL";
	private static final String SPEED_DECREASE = "SPEED_DECREASE";
	private static final String SPEED_INCREASE = "SPEED_INCREASE";

	// TODO change factors to appropriate values
	private static final int healthDecreaseFactor = 10;
	private static final int healthInreaseFactor = 10;
	private static final int sizeDecreaseFactor = 10;
	private static final int sizeIncreaseFactor = 10;
	private static final int blockControlFactor = 10;
	private static final int invulnerabilityFactor = 10;
	private static final int reverseControlFactor = 10;
	private static final int speedDecreaseFactor = 10;
	private static final int speedIncreaseFactor = 10;

	public static int getClassFactor(String artifact) {
		int retFactor = 0;
		switch (artifact) {
		case "HEALTH_INCREASE":
			retFactor = healthInreaseFactor;
			break;
		case "HEALTH_DECREASE":
			retFactor = healthDecreaseFactor;
			break;
		case "SIZE_INCREASE":
			retFactor = sizeIncreaseFactor;
			break;
		case "BLOCK_CONTROL":
			retFactor = sizeDecreaseFactor;
			break;
		case "INVULNERABILITY":
			retFactor = sizeDecreaseFactor;
			break;
		case "REVERSE_CONTROL":
			retFactor = sizeDecreaseFactor;
			break;
		case "SPEED_DECREASE":
			retFactor = sizeDecreaseFactor;
			break;
		case "SPEED_INCREASE":
			retFactor = sizeDecreaseFactor;
			break;
		case "SIZE_DECREASE":
			retFactor = sizeDecreaseFactor;
			break;
		default:
			break;
		}
		return retFactor;
	}
}
