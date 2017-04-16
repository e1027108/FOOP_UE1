package game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;

import artifacts.Artifact;
import artifacts.ArtifactConstants;
import artifacts.ArtifactConstants.Setting;
import gui.GameController;

public class GameGrid extends CollisionTarget {

	public static final int NUM_OF_THREADS = 2;

	private int size;
	private int[][] grid;
	private List<Artifact> artifacts;
	private ExecutorService executor;

	public GameGrid(int s) {
		size = s;
		grid = new int[size][size];
		this.artifacts = new ArrayList<Artifact>();

		/*
		 * create thread pool for concurrent handling of artifacts list. all
		 * threads are daemons.
		 */
		this.executor = Executors.newFixedThreadPool(NUM_OF_THREADS, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = Executors.defaultThreadFactory().newThread(r);
				t.setDaemon(true);
				return t;
			}
		});
	}

	public void initPositions(ArrayList<Snake> snakes) {
		for (Snake s : snakes) {
			int x = Math.floorDiv(size, 2);
			int y = Math.floorDiv(size, 4);
			switch (s.getDirection()) {
			case N:
				s.initPosition(x, y);
				break;
			case E:
				s.initPosition(x, size - y);
				break;
			case S:
				s.initPosition(size - x, size - y);
				break;
			case W:
				s.initPosition(size - x, y);
				break;
			default:
				System.out.println("Error InitPos");
			}
		}
	}

	public int getSize() {
		return size;
	}

	/**
	 * after every move draw the grid again. THIS draw method means setting the
	 * IDs on the grid[][], the visualization is done by the
	 * {@link GameController}.update() method!
	 */
	
	public void draw(ArrayList<Snake> snakes) {

		/* draw/remove artifacts */
		for (Artifact art : getArtifacts()) {
			int x = art.getPlacement().getX();
			int y = art.getPlacement().getY();
			if (art.isActive()) {
				grid[x][y] = (int) ArtifactConstants.artifactSettingsMap.get(art.getArtifactsMapping())
						.get(Setting.CODE);
			}
		}

		for (Snake s : snakes) {

			Point[] body = s.getBody();
			Point point = body[0];

			/*
			 * do most of the collision detection when point = body[0] (head)
			 */
			if (point.getX() < 0 || point.getX() >= size || point.getY() < 0 || point.getY() >= size) {
				s.setAlive(false);
				/* fire collision detection : BORDER */
				fireCollisionDetection(CollisionTypes.BORDER, s, point, 0);
				break;
			}

			int gridValue = grid[point.getX()][point.getY()];
			if (gridValue != 0) {
				// own_body
				if (gridValue == s.getGridID()) {
					fireCollisionDetection(CollisionTypes.OWN_BODY, s, point, gridValue);
				}
				// other_snake
				else if (gridValue <= 4) {
					fireCollisionDetection(CollisionTypes.OTHER_SNAKE, s, point, gridValue);
				}
				// artifact
				else {
					fireCollisionDetection(CollisionTypes.ARTIFACT, s, point, gridValue);
				}
			}

			// set ids for the whole snake body
			for (int j = 0; j < body.length; j++) {
				point = body[j];
				grid[point.getX()][point.getY()] = s.getGridID();
			}
			// remove tail from grid
			Point tail = s.getLastTailPosition();
			if (tail != null) {
				if (grid[tail.getX()][tail.getY()] == s.getGridID()) {
					grid[tail.getX()][tail.getY()] = 0;
				}
			}
		}

		// TODO: mapping of colour for every type of artifact
		/*
		 * colour = 5; for (Artifact a : artifacts) { if
		 * (a.getClass().equals(HealthIncreaseArtifact.class)) { Point pos =
		 * a.getPlacement(); grid[pos.getX()][pos.getY()] = colour; } }
		 */
	}

	// debug output
	public void printGrid() {
		System.out.println("-------------------------------------");
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("-------------------------------------");
	}

	public int[][] getGrid() {
		return grid;
	}

	public void addArtifact(Artifact artifact) {
		FutureTask<Boolean> futureAdd = new FutureTask<Boolean>(new Callable<Boolean>() {
			@Override
			public Boolean call() {
				artifacts.add(artifact);
				System.out.println("--- Artifacts ---");
				for (Artifact art : artifacts) {
					System.out.println(art.getPlacement() + ", isActive: " + art.isActive());
				}
				System.out.println("-----------------");
				return artifacts.contains(artifact);
			}
		});

		this.executor.execute(futureAdd);
	}

	public List<Artifact> getArtifacts() {
		FutureTask<List<Artifact>> futureRead = new FutureTask<List<Artifact>>(new Callable<List<Artifact>>() {
			@Override
			public List<Artifact> call() {
				return artifacts;
			}
		});

		this.executor.execute(futureRead);
		while (!futureRead.isDone()) {
		}
		try {
			return futureRead.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public void shutdown() {
		this.artifacts = new ArrayList<Artifact>();
	}
}
