package game;

import java.util.ArrayList;

import artifacts.logic.ArtifactHandler;
import artifacts.logic.ArtifactHandlerImpl;
import game.ai.SnakeAI;

public class Game {

	private GameGrid grid;
	private int numPlayers;
	private String firstPlayer;
	private ArtifactHandler artifactHandler;

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
			player = new SnakeAI("AI 0", directions[0], grid);
		}
		snakes.add(player);

		// init AIs for now
		for (int i = 2; i <= numPlayers; i++) {
			player = new SnakeAI("AI_" + i, directions[i - 1], grid);
			snakes.add(player);
		}

		// initialize snake positions
		grid.initPositions(snakes);

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

		// TODO richtiger platz für diesen call?
		this.artifactHandler.placeNextArtifact();

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

}
