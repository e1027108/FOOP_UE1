package artifacts.logic;

import java.util.List;

import artifacts.Artifacts;
import game.Point;

/**
 * This interface forces implementing classes to have methods for
 * creating/placing an artifact and for finding the blacklist of already
 * occupied points on the game grid.
 */
public interface ArtifactPlacementStrategy {

	void placeArtifact(Artifacts artifactType);

	List<Point> createBlackList();
	
	Point getRespawnPlace();
}
