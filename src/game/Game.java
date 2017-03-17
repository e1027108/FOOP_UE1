package game;

import java.util.ArrayList;

public class Game {

	private GameGrid grid;
	private int numPlayers;

	private char[] directions = { 'N', 'S', 'E', 'W' };

	private ArrayList<Snake> snakes;

	// TODO: change error message --> just temporary
	public Game(int num) {
		if (num > 0 && num < 5) {
			numPlayers = num;
			grid = new GameGrid(num * 10);
		} else
			System.out.println("Only 1 - 4 Players allowed.");
		System.exit(0);
	}

	public void run() {

		// create players
		snakes = new ArrayList<Snake>();
		for (int i = 1; i <= numPlayers; i++) {
			Snake player = new SnakeImpl("Player_" + i, directions[i - 1]);
			snakes.add(player);
		}

		// initialize snake positions
		grid.initPositions(snakes);

		// game loop
		// change condition for 1 Player game
		while (snakes.size() > 1) {
			grid.draw(snakes);
			grid.printGrid();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (Snake s : snakes) {
				s.move();
			}
			removeDeadSnakes();
		}
	}

	public void removeDeadSnakes() {
		for (int i = 0; i < snakes.size(); i++) {
			if (!snakes.get(i).isAlive())
				snakes.remove(i);
		}
	}

}
