package foop.artifacts.temporary;

public interface TemporaryArtifact {

	/**
	 * Every temporary artifact needs a timer to tell the game when the effect
	 * will wear off again.
	 * 
	 * @return {@code int timer} the duration of the artifacts effect.
	 */
	int getTimer();
}
