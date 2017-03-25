package artifacts.logic;

import artifacts.Artifact;

/**
 * This class centralizes the spawn time factors for the different
 * {@link Artifact} subclasses.
 */
public class ArtifactConstants {

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
	 * spawn factors to ensure different occurrences according to artifact
	 * effects
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

}
