package artifacts;

import java.util.EnumMap;
import java.util.Map;

import artifacts.logic.ArtifactCoordinateGenerator;
import artifacts.logic.ArtifactHandler;
import artifacts.logic.ArtifactPlacementStrategy;
import artifacts.logic.ArtifactSpawnTimer;

/**
 * This class centralizes all settings for the different {@link Artifact}
 * subclasses, {@link ArtifactCoordinateGenerator},
 * {@link ArtifactPlacementStrategy}, {@link ArtifactSpawnTimer} and
 * {@link ArtifactHandler}.
 */
public class ArtifactConstants {

	public enum Setting {
		INCDEC, DURATION, DESPAWN_TIMER, SPAWN_FACTOR, IMAGE, CODE
	};

	public static Map<Artifacts, Map<Setting, Object>> artifactSettingsMap;

	/* permanent increases/decreases */
	public static final int HEALTH_INCREASE = 1;
	public static final int HEALTH_DECREASE = 1;
	public static final int SIZE_INCREASE   = 1;
	public static final int SIZE_DECREASE   = 1;

	/* temporary increases/decreases */
	public static final int SPEED_INCREASE = 1;
	public static final int SPEED_DECREASE = 1;

	/* duration of effect */
	public static final int BLOCK_CONTROL_DURATION   = 2;
	public static final int INVULNERABILITY_DURATION = 2;
	public static final int REVERSE_CONTROL_DURATION = 2;
	public static final int SPEED_INCREASE_DURATION  = 2;
	public static final int SPEED_DECREASE_DURATION  = 2;

	/* time until despawn - IN SECONDS */
	public static final int HEALTH_INCREASE_DESPAWN_TIMER = 15;
	public static final int HEALTH_DECREASE_DESPAWN_TIMER = 15;
	public static final int SIZE_INCREASE_DESPAWN_TIMER   = 15;
	public static final int SIZE_DECREASE_DESPAWN_TIMER   = 15;
	public static final int BLOCK_CONTROL_DESPAWN_TIMER   = 15;
	public static final int INVULNERABILITY_DESPAWN_TIMER = 15;
	public static final int REVERSE_CONTROL_DESPAWN_TIMER = 15;
	public static final int SPEED_INCREASE_DESPAWN_TIMER  = 15;
	public static final int SPEED_DECREASE_DESPAWN_TIMER  = 15;

	/*
	 * spawn probability factors to ensure different occurrences according to
	 * artifact effects -> Should be a positive Integer!
	 */
	public static final int HEALTH_INCREASE_SPAWN_FACTOR = 1;
	public static final int HEALTH_DECREASE_SPAWN_FACTOR = 1;
	public static final int SIZE_INCREASE_SPAWN_FACTOR   = 1;
	public static final int SIZE_DECREASE_SPAWN_FACTOR   = 1;
	public static final int BLOCK_CONTROL_SPAWN_FACTOR   = 1;
	public static final int INVULNERABILITY_SPAWN_FACTOR = 1;
	public static final int REVERSE_CONTROL_SPAWN_FACTOR = 1;
	public static final int SPEED_INCREASE_SPAWN_FACTOR  = 1;
	public static final int SPEED_DECREASE_SPAWN_FACTOR  = 1;

	/* spawn timer for the next artifact */
	public static final int NEXT_SPAWN_INTERVAL_START = 2;
	public static final int NEXT_SPAWN_INTERVAL_END   = 8;

	/* images */
	public static final String HEALTH_INCREASE_IMAGE = "img/health_increase.png";
	public static final String HEALTH_DECREASE_IMAGE = "img/health_decrease.png";
	public static final String SIZE_INCREASE_IMAGE   = "img/size_increase.png";
	public static final String SIZE_DECREASE_IMAGE   = "img/size_decrease.png";
	public static final String BLOCK_CONTROL_IMAGE   = "img/block_control.png";
	public static final String REVERSE_CONTROL_IMAGE = "img/reverse_control.png";
	public static final String INVULNERABILITY_IMAGE = "img/invulnerability.png";
	public static final String SPEED_INCREASE_IMAGE  = "img/speed_increase.png";
	public static final String SPEED_DECREASE_IMAGE  = "img/speed_decrease.png";

	/* grid codes: permanent 1x, temporary 2x */
	public static final int HEALTH_INCREASE_CODE = 10;
	public static final int HEALTH_DECREASE_CODE = 11;
	public static final int SIZE_INCREASE_CODE   = 12;
	public static final int SIZE_DECREASE_CODE   = 13;

	public static final int BLOCK_CONTROL_CODE   = 20;
	public static final int REVERSE_CONTROL_CODE = 21;
	public static final int INVULNERABILITY_CODE = 22;
	public static final int SPEED_INCREASE_CODE  = 23;
	public static final int SPEED_DECREASE_CODE  = 24;

	static {
		/**
		 * in this static block, we create a map of all existing
		 * timers/factors/etc. grouped by effect and artifact type. with this
		 * map we can dynamically find all constants for one type.
		 */

		artifactSettingsMap = new EnumMap<Artifacts, Map<Setting, Object>>(Artifacts.class);

		Map<Setting, Object> healthIncrease = new EnumMap<Setting, Object>(Setting.class);
		Map<Setting, Object> healthDecrease = new EnumMap<Setting, Object>(Setting.class);
		Map<Setting, Object> sizeIncrease = new EnumMap<Setting, Object>(Setting.class);
		Map<Setting, Object> sizeDecrease = new EnumMap<Setting, Object>(Setting.class);
		Map<Setting, Object> speedIncrease = new EnumMap<Setting, Object>(Setting.class);
		Map<Setting, Object> speedDecrease = new EnumMap<Setting, Object>(Setting.class);
		Map<Setting, Object> blockControl = new EnumMap<Setting, Object>(Setting.class);
		Map<Setting, Object> invulnerability = new EnumMap<Setting, Object>(Setting.class);
		Map<Setting, Object> reverseControl = new EnumMap<Setting, Object>(Setting.class);

		healthIncrease.put(Setting.INCDEC, HEALTH_INCREASE);
		healthIncrease.put(Setting.DESPAWN_TIMER, HEALTH_INCREASE_DESPAWN_TIMER);
		healthIncrease.put(Setting.SPAWN_FACTOR, HEALTH_INCREASE_SPAWN_FACTOR);
		healthIncrease.put(Setting.IMAGE, HEALTH_INCREASE_IMAGE);
		healthIncrease.put(Setting.CODE, HEALTH_INCREASE_CODE);

		healthDecrease.put(Setting.INCDEC, HEALTH_DECREASE);
		healthDecrease.put(Setting.DESPAWN_TIMER, HEALTH_DECREASE_DESPAWN_TIMER);
		healthDecrease.put(Setting.SPAWN_FACTOR, HEALTH_DECREASE_SPAWN_FACTOR);
		healthDecrease.put(Setting.IMAGE, HEALTH_DECREASE_IMAGE);
		healthDecrease.put(Setting.CODE, HEALTH_DECREASE_CODE);

		sizeIncrease.put(Setting.INCDEC, SIZE_INCREASE);
		sizeIncrease.put(Setting.DESPAWN_TIMER, SIZE_INCREASE_DESPAWN_TIMER);
		sizeIncrease.put(Setting.SPAWN_FACTOR, SIZE_INCREASE_SPAWN_FACTOR);
		sizeIncrease.put(Setting.IMAGE, SIZE_INCREASE_IMAGE);
		sizeIncrease.put(Setting.CODE, SIZE_INCREASE_CODE);

		sizeDecrease.put(Setting.INCDEC, SIZE_DECREASE);
		sizeDecrease.put(Setting.DESPAWN_TIMER, SIZE_DECREASE_DESPAWN_TIMER);
		sizeDecrease.put(Setting.SPAWN_FACTOR, SIZE_DECREASE_SPAWN_FACTOR);
		sizeDecrease.put(Setting.IMAGE, SIZE_DECREASE_IMAGE);
		sizeDecrease.put(Setting.CODE, SIZE_DECREASE_CODE);

		speedIncrease.put(Setting.INCDEC, SPEED_INCREASE);
		speedIncrease.put(Setting.DURATION, SPEED_INCREASE_DURATION);
		speedIncrease.put(Setting.DESPAWN_TIMER, SPEED_INCREASE_DESPAWN_TIMER);
		speedIncrease.put(Setting.SPAWN_FACTOR, SPEED_INCREASE_SPAWN_FACTOR);
		speedIncrease.put(Setting.IMAGE, SPEED_INCREASE_IMAGE);
		speedIncrease.put(Setting.CODE, SPEED_INCREASE_CODE);

		speedDecrease.put(Setting.INCDEC, SPEED_DECREASE);
		speedDecrease.put(Setting.DURATION, SPEED_DECREASE_DURATION);
		speedDecrease.put(Setting.DESPAWN_TIMER, SPEED_DECREASE_DESPAWN_TIMER);
		speedDecrease.put(Setting.SPAWN_FACTOR, SPEED_DECREASE_SPAWN_FACTOR);
		speedDecrease.put(Setting.IMAGE, SPEED_DECREASE_IMAGE);
		speedDecrease.put(Setting.CODE, SPEED_DECREASE_CODE);

		blockControl.put(Setting.DURATION, BLOCK_CONTROL_DURATION);
		blockControl.put(Setting.DESPAWN_TIMER, BLOCK_CONTROL_DESPAWN_TIMER);
		blockControl.put(Setting.SPAWN_FACTOR, BLOCK_CONTROL_SPAWN_FACTOR);
		blockControl.put(Setting.IMAGE, BLOCK_CONTROL_IMAGE);
		blockControl.put(Setting.CODE, BLOCK_CONTROL_CODE);

		invulnerability.put(Setting.DURATION, INVULNERABILITY_DURATION);
		invulnerability.put(Setting.DESPAWN_TIMER, INVULNERABILITY_DESPAWN_TIMER);
		invulnerability.put(Setting.SPAWN_FACTOR, INVULNERABILITY_SPAWN_FACTOR);
		invulnerability.put(Setting.IMAGE, INVULNERABILITY_IMAGE);
		invulnerability.put(Setting.CODE, INVULNERABILITY_CODE);

		reverseControl.put(Setting.DURATION, REVERSE_CONTROL_DURATION);
		reverseControl.put(Setting.DESPAWN_TIMER, REVERSE_CONTROL_DESPAWN_TIMER);
		reverseControl.put(Setting.SPAWN_FACTOR, REVERSE_CONTROL_SPAWN_FACTOR);
		reverseControl.put(Setting.IMAGE, REVERSE_CONTROL_IMAGE);
		reverseControl.put(Setting.CODE, REVERSE_CONTROL_CODE);

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