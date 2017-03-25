package artifacts.temporary;

public interface TemporaryArtifact {

	/**
	 * Every temporary artifact needs a duration value to tell the game when the
	 * effect will wear off again.
	 * 
	 * @return {@code int duration} the duration of the artifacts effect.
	 */
	int getDuration();
}
