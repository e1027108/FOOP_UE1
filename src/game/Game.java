package game;

import java.util.ArrayList;

public class Game {

	private GameGrid grid;
	private int numPlayers;
	private String firstPlayer;

	private char[] directions = { 'N', 'S', 'E', 'W' };

	private ArrayList<Snake> snakes;

	// TODO: change error message --> just temporary
	public Game(int num, String name) {
		firstPlayer = name;
		if (num > 0 && num < 5) {
			numPlayers = num;
			grid = new GameGrid(28);
		} else {
			System.out.println("Only 1 - 4 Players allowed.");
			System.exit(0);
		}
	}

	public void run() {

		// create players
		snakes = new ArrayList<Snake>();

		// init humanPlayer
		Snake player = new SnakeImpl(firstPlayer, directions[0]);
		snakes.add(player);

		// init AIs for now
		for (int i = 2; i <= numPlayers; i++) {
			player = new SnakeImpl("AI_" + i, directions[i - 1]);
			snakes.add(player);
		}

		// initialize snake positions
		grid.initPositions(snakes);

		// game loop
		loop();
	}

	public void loop() {

		for (Snake s : snakes) {
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

}
