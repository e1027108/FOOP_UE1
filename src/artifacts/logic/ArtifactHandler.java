package artifacts.logic;

import artifacts.Artifacts;

/**
 * This class manages the spawn time/chance of artifacts and the
 * creation/placement on the game grid.
 */
public interface ArtifactHandler {

	void placeNextArtifact();

	Artifacts getNextArtifactType();
}
