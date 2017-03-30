package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import artifacts.Artifact;
import artifacts.logic.ArtifactHandler;
import artifacts.logic.ArtifactHandlerImpl;
import game.ai.SnakeAI;

public class Game {

	private GameGrid grid;
	private int numPlayers;
	private String firstPlayer;
	private ArtifactHandler artifactHandler;
	private Thread artifactSpawnThread;
	private List<Artifact> children;

	private char[] directions = { 'N', 'S', 'E', 'W' };

	private ArrayList<Snake> snakes;

	// TODO: change error message --> just temporary
	public Game(int num, String name) {
		System.out.println(num);

		firstPlayer = name;
		if (num > 0 && num < 5) {
			numPlayers = num;
			grid = new GameGrid(28);
		} else if (num == 0) {
			numPlayers = 1;
			grid = new GameGrid(28);
			firstPlayer = null; // TODO this needs a better solution
		} else {
			System.out.println("Only 1 - 4 Players allowed.");
			System.exit(0);
		}

		this.children = Collections.synchronizedList(new ArrayList<Artifact>());
		this.artifactHandler = new ArtifactHandlerImpl(this);
	}

	public void run() {
		Snake player;

		// create players
		snakes = new ArrayList<Snake>();

		// init humanPlayer?
		if (firstPlayer != null) {
			player = new SnakeImpl(firstPlayer, directions[0]);
		} else {
			player = new SnakeAI("AI 0", directions[0], this);
		}
		snakes.add(player);

		// init AIs for now
		for (int i = 2; i <= numPlayers; i++) {
			player = new SnakeAI("AI_" + i, directions[i - 1], this);
			snakes.add(player);
		}

		// initialize snake positions
		grid.initPositions(snakes);

		/*
		 * start a seperate thread with artifact handler, waiting for artifact
		 * spawn should not affect this thread
		 */
		artifactSpawnThread = new Thread(artifactHandler);
		artifactSpawnThread.setDaemon(true);
		artifactSpawnThread.start();

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

	public synchronized void addChild(Artifact child) {
		this.children.add(child);
		System.out.println("--- added child thread to list ---");
	}

	public synchronized void closeChildren() {
		this.artifactSpawnThread.interrupt();
		for (Artifact ch : children) {
			ch.interrupt();
		}
	}

	public synchronized List<Artifact> getActiveChildren() {
		List<Artifact> ret = new ArrayList<Artifact>();
		for (Artifact art : this.children) {
			if (art.isActive()) {
				ret.add(art);
			}
		}
		return ret;
	}
}
