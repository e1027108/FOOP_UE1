package artifacts;

import artifacts.ArtifactConstants.Setting;
import artifacts.permanent.HealthDecreaseArtifact;
import artifacts.permanent.HealthIncreaseArtifact;
import artifacts.permanent.SizeDecreaseArtifact;
import artifacts.permanent.SizeIncreaseArtifact;
import artifacts.temporary.BlockControlArtifact;
import artifacts.temporary.InvulnerabilityArtifact;
import artifacts.temporary.ReverseControlArtifact;
import artifacts.temporary.SpeedDecreaseArtifact;
import artifacts.temporary.SpeedIncreaseArtifact;
import game.Point;

public class ArtifactFactory {

	private ArtifactFactory() {
	}

	public static Artifact getArtifact(Point point, Artifacts type) {
		Artifact artifact = null;

		switch (type) {
		case HEALTH_INCREASE:
			artifact = new HealthIncreaseArtifact(point,
					(int) ArtifactConstants.artifactSettingsMap.get(type).get(Setting.DESPAWN_TIMER), type);
			break;
		case HEALTH_DECREASE:
			artifact = new HealthDecreaseArtifact(point,
					(int) ArtifactConstants.artifactSettingsMap.get(type).get(Setting.DESPAWN_TIMER), type);
			break;
		case SIZE_INCREASE:
			artifact = new SizeIncreaseArtifact(point,
					(int) ArtifactConstants.artifactSettingsMap.get(type).get(Setting.DESPAWN_TIMER), type);
			break;
		case SIZE_DECREASE:
			artifact = new SizeDecreaseArtifact(point,
					(int) ArtifactConstants.artifactSettingsMap.get(type).get(Setting.DESPAWN_TIMER), type);
			break;
		case BLOCK_CONTROL:
			artifact = new BlockControlArtifact(point,
					(int) ArtifactConstants.artifactSettingsMap.get(type).get(Setting.DESPAWN_TIMER), type);
			break;
		case INVULNERABILITY:
			artifact = new InvulnerabilityArtifact(point,
					(int) ArtifactConstants.artifactSettingsMap.get(type).get(Setting.DESPAWN_TIMER), type);
			break;
		case REVERSE_CONTROL:
			artifact = new ReverseControlArtifact(point,
					(int) ArtifactConstants.artifactSettingsMap.get(type).get(Setting.DESPAWN_TIMER), type);
			break;
		case SPEED_INCREASE:
			artifact = new SpeedIncreaseArtifact(point,
					(int) ArtifactConstants.artifactSettingsMap.get(type).get(Setting.DESPAWN_TIMER), type);
			break;
		case SPEED_DECREASE:
			artifact = new SpeedDecreaseArtifact(point,
					(int) ArtifactConstants.artifactSettingsMap.get(type).get(Setting.DESPAWN_TIMER), type);
			break;
		default:
			break;
		}

		return artifact;
	}
}
