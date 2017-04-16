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
		Snake bittenSnake = null;
		for (Snake s : snakes) {

			Point[] body = s.getBody();
			Point headPosition = body[0];

			/*
			 * do most of the collision detection when point = body[0] (head)
			 */
			if (!insideGrid(headPosition)) {
				/* fire collision detection : BORDER */
				fireCollisionDetection(CollisionTypes.BORDER, s, headPosition, 0);
			}
			else {
				int gridValue = grid[headPosition.getX()][headPosition.getY()];
				if (gridValue != 0) {
					// own_body
					if (gridValue == s.getGridID()) {
						fireCollisionDetection(CollisionTypes.OWN_BODY, s, headPosition, gridValue);
					}
					// other_snake
					else if (gridValue <= 4) {
						fireCollisionDetection(CollisionTypes.OTHER_SNAKE, s, headPosition, gridValue);
						bittenSnake = snakes.get(gridValue-1);
					}
					// artifact
					else {
						fireCollisionDetection(CollisionTypes.ARTIFACT, s, headPosition, gridValue);
					}
				}
			}
			
			// remove dead bodyparts of OTHER_SNAKE
			if (bittenSnake != null) {
				removeDeadBodyParts(bittenSnake);
				bittenSnake = null;
			}
			
			// remove all this bodyparts from grid
			removeDeadBodyParts(s);

			// body might have changed due to collisions
			body = s.getBody();

			// set ids for the whole snake body
			for (int j = 0; j < body.length; j++) {
				Point bodyPartPosition = body[j];
				grid[bodyPartPosition.getX()][bodyPartPosition.getY()] = s.getGridID();
			}
			
		}
		/* draw/remove artifacts */
		for (Artifact art : getArtifacts()) {
			int x = art.getPlacement().getX();
			int y = art.getPlacement().getY();
			if (art.isActive()) {
				grid[x][y] = (int) ArtifactConstants.artifactSettingsMap.get(art.getArtifactsMapping())
						.get(Setting.CODE);
			}
		}
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
				return artifacts.contains(artifact);
			}
		});

		this.executor.execute(futureAdd);
		System.out.println("Successfully placed artifact at " + artifact.getPlacement().toString());
		for (Artifact art : artifacts) {
			System.out.println(art.getPlacement() + ", " + art.isActive());
		}
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
	
	public boolean insideGrid(Point p) {
		if (p.getX() >= 0 && p.getX() < size && p.getY() >= 0 && p.getY() < size) {
			return true;
		}
		return false;
	}
	
	public void removeDeadBodyParts(Snake s) {
		for (Point p : s.getDeadParts()) {
			if (insideGrid(p)) {
				if (grid[p.getX()][p.getY()] == s.getGridID()) {
					grid[p.getX()][p.getY()] = 0;
				}
			}
		}
	
	}
}