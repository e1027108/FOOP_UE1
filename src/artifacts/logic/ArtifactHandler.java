package artifacts.logic;

import artifacts.Artifacts;

/**
 * This class manages the spawn time/chance of artifacts and the
 * creation/placement on the game grid.
 */
public interface ArtifactHandler extends Runnable {

	void placeNextArtifact() throws InterruptedException;

	Artifacts getNextArtifactType();
}
