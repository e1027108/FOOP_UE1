package game;

import java.util.ArrayList;
import java.util.List;

import artifacts.Artifact;
import artifacts.Artifacts;
import artifacts.logic.ArtifactHandler;
import artifacts.logic.ArtifactHandlerImpl;
import game.CollisionTarget.CollisionTypes;
import game.ai.SnakeAI;

public class Game implements CollisionListener {

	private GameGrid grid;
	private int numPlayers;
	private String firstPlayer;
	private ArtifactHandler artifactHandler;
	private List<Thread> children;

	private ArrayList<Snake> snakes;

	// TODO: change error message --> just temporary
	public Game(int num, String name, int gridSize) {
		System.out.println(num);

		firstPlayer = name;
		if (num > 0 && num < 5) {
			numPlayers = num;
			grid = new GameGrid(gridSize);
		} else if (num == 0) {
			numPlayers = 1;
			grid = new GameGrid(gridSize);
			firstPlayer = null; // TODO this needs a better solution
		} else {
			System.out.println("Only 1 - 4 Players allowed.");
			System.exit(0);
		}
		
		this.grid.addCollisionListener(this);

		this.children = new ArrayList<Thread>();
		this.artifactHandler = new ArtifactHandlerImpl(this);
	}

	public void run() {
		Snake player;

		// create players
		snakes = new ArrayList<Snake>();

		// init humanPlayer?
		if (firstPlayer != null) {
			player = new SnakeImpl(firstPlayer, 1, Directions.N);
		} else {
			player = new SnakeAI("AI 0", 1, Directions.N, this);
		}
		snakes.add(player);

		// init AIs for now
		List<Directions> directions = new ArrayList<Directions>();
		directions.add(Directions.N);
		directions.add(Directions.S);
		directions.add(Directions.E);
		directions.add(Directions.W);
		for (int i = 2; i <= numPlayers; i++) {
			player = new SnakeAI("AI_" + i, i, directions.get(i - 1), this);
			snakes.add(player);
		}

		// initialize snake positions
		grid.initPositions(snakes);

		/*
		 * start a seperate thread with artifact handler, waiting for artifact
		 * spawn should not affect this thread
		 */
		Thread artifactChild = new Thread(this.artifactHandler);
		children.add(artifactChild);
		artifactChild.setDaemon(true);
		artifactChild.start();

		// game loop
		loop();
	}

	public void loop() {

		for (Snake s : snakes) {
			if (s instanceof SnakeAI) {
				((SnakeAI) s).determineNextDirection();
			}
			s.move();
		}
		grid.draw(snakes);
		removeDeadSnakes();

	}

	public void removeDeadSnakes() {
		for (int i = 0; i < snakes.size(); i++) {
			if (!snakes.get(i).isAlive())
				snakes.remove(i);
		}
	}

	public Snake getSnake(String name) {
		for (Snake s : snakes) {
			if (name.equals(s.getName()))
				return s;
		}
		return null;
	}

	public ArrayList<Snake> getSnakes() {
		return snakes;
	}

	public GameGrid getGrid() {
		return grid;
	}

	public void closeChildren() {
		for (Thread ch : children) {
			ch.interrupt();
		}
	}

	public ArtifactHandler getArtifactHandler() {
		return this.artifactHandler;
	}

	@Override
	public void collisionDetected(CollisionTypes coll, Snake snek, Point point, int gridID) {
		System.out.println("Detected collision of type: " + coll);
		/* handle artifacts */
		if(coll == CollisionTypes.ARTIFACT) {
			System.out.println("ate an artifact");
			eatArtifact(snek, point, gridID);
		}
	}

	private void eatArtifact(Snake snek, Point point, int gridID) {
		// TODO handle artifacts
		switch (gridID) {
		case 10: // HEALTH_INCREASE
			System.out.println("i ate a " + Artifacts.HEALTH_INCREASE);
			break;
		case 11: // HEALTH_DECREASE
			System.out.println("i ate a " + Artifacts.HEALTH_DECREASE);
			break;
		case 12: // SIZE_INCREASE
			System.out.println("i ate a " + Artifacts.SIZE_INCREASE);
			break;
		case 13: // SIZE_DECREASE
			System.out.println("i ate a " + Artifacts.SIZE_DECREASE);
			break;
		case 20: // BLOCK_CONTROL
			System.out.println("i ate a " + Artifacts.BLOCK_CONTROL);
			break;
		case 21: // REVERSE_CONTROL
			System.out.println("i ate a " + Artifacts.REVERSE_CONTROL);
			break;
		case 22: // INVULNERABILITY
			System.out.println("i ate a " + Artifacts.INVULNERABILITY);
			break;
		case 23: // SPEED_INCREASE
			System.out.println("i ate a " + Artifacts.SPEED_INCREASE);
			break;
		case 24: // SPEED_DECREASE
			System.out.println("i ate a " + Artifacts.SPEED_DECREASE);
			break;
		default:
			break;
		}

		// set artifact inactive
		for (Artifact art : this.grid.getArtifacts()) {
			if (art.getPlacement().equals(point)) {
				art.setActive(false);
			}
		}
	}

}
