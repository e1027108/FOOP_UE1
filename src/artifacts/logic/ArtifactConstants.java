package artifacts.logic;

import java.util.EnumMap;
import java.util.Map;

import artifacts.Artifact;
import artifacts.Artifacts;

/**
 * This class centralizes all settings for the different {@link Artifact}
 * subclasses, {@link ArtifactCoordinateGenerator},
 * {@link ArtifactPlacementStrategy}, {@link ArtifactSpawnTimer} and
 * {@link ArtifactHandler}.
 */
public class ArtifactConstants {

	public enum Setting {
		INCDEC, DURATION, DESPAWN_TIMER, SPAWN_FACTOR
	};

	public static Map<Artifacts, Map<Setting, Object>> artifactSettingsMap;

	/* permanent increases/decreases */
	public static final int HEALTH_INCREASE = 10;
	public static final int HEALTH_DECREASE = 10;
	public static final int SIZE_INCREASE = 1;
	public static final int SIZE_DECREASE = 1;

	/* temporary increases/decreases */
	public static final int SPEED_INCREASE = 1;
	public static final int SPEED_DECREASE = 1;

	/* duration of effect */
	public static final int BLOCK_CONTROL_DURATION = 2;
	public static final int INVULNERABILITY_DURATION = 2;
	public static final int REVERSE_CONTROL_DURATION = 2;
	public static final int SPEED_INCREASE_DURATION = 2;
	public static final int SPEED_DECREASE_DURATION = 2;

	/* time until despawn */
	public static final int HEALTH_INCREASE_DESPAWN_TIMER = 3;
	public static final int HEALTH_DECREASE_DESPAWN_TIMER = 3;
	public static final int SIZE_INCREASE_DESPAWN_TIMER = 3;
	public static final int SIZE_DECREASE_DESPAWN_TIMER = 3;
	public static final int BLOCK_CONTROL_DESPAWN_TIMER = 3;
	public static final int INVULNERABILITY_DESPAWN_TIMER = 3;
	public static final int REVERSE_CONTROL_DESPAWN_TIMER = 3;
	public static final int SPEED_INCREASE_DESPAWN_TIMER = 3;
	public static final int SPEED_DECREASE_DESPAWN_TIMER = 3;

	/*
	 * spawn probability factors to ensure different occurrences according to
	 * artifact effects -> Should be a positive Integer!
	 */
	public static final int HEALTH_INCREASE_SPAWN_FACTOR = 10;
	public static final int HEALTH_DECREASE_SPAWN_FACTOR = 10;
	public static final int SIZE_INCREASE_SPAWN_FACTOR = 10;
	public static final int SIZE_DECREASE_SPAWN_FACTOR = 10;
	public static final int BLOCK_CONTROL_SPAWN_FACTOR = 10;
	public static final int INVULNERABILITY_SPAWN_FACTOR = 10;
	public static final int REVERSE_CONTROL_SPAWN_FACTOR = 10;
	public static final int SPEED_INCREASE_SPAWN_FACTOR = 10;
	public static final int SPEED_DECREASE_SPAWN_FACTOR = 10;

	/* spawn timer for the next artifact */
	public static final int NEXT_SPAWN_INTERVAL_START = 15;
	public static final int NEXT_SPAWN_INTERVAL_END = 45;

	static {
		Map<Setting, Object> healthIncrease = new EnumMap(Setting.class);
		Map<Setting, Object> healthDecrease = new EnumMap(Setting.class);
		Map<Setting, Object> sizeIncrease = new EnumMap(Setting.class);
		Map<Setting, Object> sizeDecrease = new EnumMap(Setting.class);
		Map<Setting, Object> speedIncrease = new EnumMap(Setting.class);
		Map<Setting, Object> speedDecrease = new EnumMap(Setting.class);
		Map<Setting, Object> blockControl = new EnumMap(Setting.class);
		Map<Setting, Object> invulnerability = new EnumMap(Setting.class);
		Map<Setting, Object> reverseControl = new EnumMap(Setting.class);

		healthIncrease.put(Setting.INCDEC, HEALTH_INCREASE);
		healthIncrease.put(Setting.DESPAWN_TIMER, HEALTH_INCREASE_DESPAWN_TIMER);
		healthIncrease.put(Setting.SPAWN_FACTOR, HEALTH_INCREASE_SPAWN_FACTOR);

		healthDecrease.put(Setting.INCDEC, HEALTH_DECREASE);
		healthDecrease.put(Setting.DESPAWN_TIMER, HEALTH_DECREASE_DESPAWN_TIMER);
		healthDecrease.put(Setting.SPAWN_FACTOR, HEALTH_DECREASE_SPAWN_FACTOR);

		sizeIncrease.put(Setting.INCDEC, SIZE_INCREASE);
		sizeIncrease.put(Setting.DESPAWN_TIMER, SIZE_INCREASE_DESPAWN_TIMER);
		sizeIncrease.put(Setting.SPAWN_FACTOR, SIZE_INCREASE_SPAWN_FACTOR);

		sizeDecrease.put(Setting.INCDEC, SIZE_DECREASE);
		sizeDecrease.put(Setting.DESPAWN_TIMER, SIZE_DECREASE_DESPAWN_TIMER);
		sizeDecrease.put(Setting.SPAWN_FACTOR, SIZE_DECREASE_SPAWN_FACTOR);

		speedIncrease.put(Setting.INCDEC, SPEED_INCREASE);
		speedIncrease.put(Setting.DURATION, SPEED_INCREASE_DURATION);
		speedIncrease.put(Setting.DESPAWN_TIMER, SPEED_INCREASE_DESPAWN_TIMER);
		speedIncrease.put(Setting.SPAWN_FACTOR, SPEED_INCREASE_SPAWN_FACTOR);

		speedDecrease.put(Setting.INCDEC, SPEED_DECREASE);
		speedDecrease.put(Setting.DURATION, SPEED_DECREASE_DURATION);
		speedDecrease.put(Setting.DESPAWN_TIMER, SPEED_DECREASE_DESPAWN_TIMER);
		speedDecrease.put(Setting.SPAWN_FACTOR, SPEED_DECREASE_SPAWN_FACTOR);

		blockControl.put(Setting.DURATION, BLOCK_CONTROL_DURATION);
		blockControl.put(Setting.DESPAWN_TIMER, BLOCK_CONTROL_DESPAWN_TIMER);
		blockControl.put(Setting.SPAWN_FACTOR, BLOCK_CONTROL_SPAWN_FACTOR);

		invulnerability.put(Setting.DURATION, INVULNERABILITY_DURATION);
		invulnerability.put(Setting.DESPAWN_TIMER, INVULNERABILITY_DESPAWN_TIMER);
		invulnerability.put(Setting.SPAWN_FACTOR, INVULNERABILITY_SPAWN_FACTOR);

		reverseControl.put(Setting.DURATION, REVERSE_CONTROL_DURATION);
		reverseControl.put(Setting.DESPAWN_TIMER, REVERSE_CONTROL_DESPAWN_TIMER);
		reverseControl.put(Setting.SPAWN_FACTOR, REVERSE_CONTROL_SPAWN_FACTOR);

		artifactSettingsMap.put(Artifacts.HEALTH_INCREASE, healthIncrease);
		artifactSettingsMap.put(Artifacts.HEALTH_DECREASE, healthDecrease);
		artifactSettingsMap.put(Artifacts.SIZE_INCREASE, sizeIncrease);
		artifactSettingsMap.put(Artifacts.SIZE_DECREASE, sizeDecrease);
		artifactSettingsMap.put(Artifacts.SPEED_INCREASE, speedIncrease);
		artifactSettingsMap.put(Artifacts.SPEED_DECREASE, speedDecrease);
		artifactSettingsMap.put(Artifacts.BLOCK_CONTROL, blockControl);
		artifactSettingsMap.put(Artifacts.INVULNERABILITY, invulnerability);
		artifactSettingsMap.put(Artifacts.REVERSE_CONTROL, reverseControl);
	}
}