package artifacts.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This implementation of {@link ArtifactSpawnTimer} randomly selects a spawn
 * timer from an interval (lower and upper bound are defined in
 * {@link ArtifactConstants}).
 */
public class ArtifactSpawnTimerRandomImpl implements ArtifactSpawnTimer {

	public ArtifactSpawnTimerRandomImpl() {
	}

	@Override
	public int getSpawnTime() {
		Random random = new Random();
		List<Integer> spawnTimeInterval = new ArrayList<Integer>();
		for (int i = ArtifactConstants.NEXT_SPAWN_INTERVAL_START; i < ArtifactConstants.NEXT_SPAWN_INTERVAL_END; i++) {
			spawnTimeInterval.add(i);
		}
		return spawnTimeInterval.get(random.nextInt(spawnTimeInterval.size()));
	}

}
