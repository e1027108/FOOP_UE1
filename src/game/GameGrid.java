package game;

import java.util.ArrayList;

public class GameGrid {

	private int size;
	private int[][] grid;

	public GameGrid(int s) {
		size = s;
		grid = new int[size][size];
	}

	public void initPositions(ArrayList<Snake> snakes) {
		for (Snake s : snakes) {
			int x = Math.floorDiv(size, 5);
			int y = Math.floorDiv(size, 5);
			switch (s.getDirection()) {
			case 'N':
				s.initPosition(x, y);
				break;
			case 'E':
				s.initPosition(x, size - y);
				break;
			case 'S':
				s.initPosition(size - x, size - y);
				break;
			case 'W':
				s.initPosition(size - x, y);
				break;
			default:
				System.out.println("Error InitPos");
			}
		}
	}

	// after every move draw the grid again
	// TODO: collision detection
	public void draw(ArrayList<Snake> snakes) {
		int colour = 1;
		for (Snake s : snakes) {

			Point[] body = s.getBody();
			for (int j = 0; j < body.length; j++) {
				Point point = body[j];
				grid[point.getX()][point.getY()] = colour;
			}
			colour++;
		}
	}

	// debug output
	public void printGrid() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}
	}

}
