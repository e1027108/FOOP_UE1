package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import artifacts.Artifact;
import artifacts.ArtifactConstants;
import artifacts.Artifacts;
import artifacts.logic.ArtifactCoordinateGenerator;
import artifacts.logic.ArtifactCoordinateGeneratorImpl;
import artifacts.logic.ArtifactHandler;
import artifacts.logic.ArtifactHandlerImpl;
import artifacts.logic.ArtifactPlacementStrategy;
import artifacts.logic.ArtifactsPlacementStrategyNaiveImpl;
import game.CollisionTarget.CollisionTypes;
import game.ai.SnakeAI;

public class Game implements CollisionListener {

	private GameGrid grid;
	private int numPlayers;
	private String firstPlayer;
	private ArtifactHandler artifactHandler;
	private List<Thread> children;

	private ArrayList<Snake> snakes;
	
	private ArtifactPlacementStrategy placementStrategy;
	private ArtifactCoordinateGenerator coordinateGenerator;

	// TODO: change error message --> just temporary
	public Game(int num, String name, int gridSize) {
		firstPlayer = name;
		if (num > 0 && num < 5) {
			numPlayers = num;
			grid = new GameGrid(gridSize);
		} else if (num == 0) {
			numPlayers = 4;
			grid = new GameGrid(gridSize);
			firstPlayer = null; // TODO this needs a better solution
		} else {
			System.out.println("Only 1 - 4 Players allowed.");
			System.exit(0);
		}
		
		this.grid.addCollisionListener(this);

		this.children = new ArrayList<Thread>();
		this.artifactHandler = new ArtifactHandlerImpl(this);
		
		this.coordinateGenerator = new ArtifactCoordinateGeneratorImpl(grid);		
		this.placementStrategy = new ArtifactsPlacementStrategyNaiveImpl(coordinateGenerator, grid, this);
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
		Iterator<Snake> snakeIterator = snakes.iterator();
		while (snakeIterator.hasNext()) {
			Snake s = snakeIterator.next();
			if (s.checkHealth()) {
				if (s instanceof SnakeAI) {
					((SnakeAI) s).determineNextDirection();
				}
				s.checkStatus();
				s.move();
			}
		}
		grid.draw(snakes);
		removeDeadSnakes();
	}

	public void removeDeadSnakes() {
		for (int i = 0; i < snakes.size(); i++) {
			if (!snakes.get(i).isAlive()) {
				snakes.remove(i);
			}
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

	public Snake getSnakeByGridID(int gridID) {
		for (Snake snek : snakes) {
			if (snek.getGridID() == gridID) {
				return snek;
			}
		}
		return null;
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
	public void collisionDetected(CollisionTypes coll, Snake snek, Point headPosition, int gridID) {
		SnakeImpl bitingSnake = (SnakeImpl) snek;		
		
		/* handle artifacts */
		if(coll == CollisionTypes.ARTIFACT) {
			eatArtifact(snek, headPosition, gridID);
		}
		/* handle other collisions */
		if(coll == CollisionTypes.BORDER) {
			bitingSnake.changeHealthModifier(GameConstants.BORDER_HEALTH_DECREASE);
			bitingSnake.printStatus();
			respawnSnake(bitingSnake);
		}
		if(coll == CollisionTypes.OWN_BODY) {
			bitingSnake.changeHealthModifier(GameConstants.OWN_BITE_HEALTH_DECREASE);
			respawnSnake(bitingSnake);		
		}
		if(coll == CollisionTypes.OTHER_SNAKE) {
			SnakeImpl bittenSnake = (SnakeImpl) getSnakeByGridID(gridID);
			if(bittenSnake.getBody()[0].equals(bitingSnake.getBody()[0])) { // do they bite each others head? -> respawn both
				bitingSnake.changeHealthModifier(GameConstants.SNAKE_BITE_HEALTH_DECREASE);
				respawnSnake(bitingSnake);
			}			
			bittenSnake.changeHealthModifier(GameConstants.SNAKE_BITE_HEALTH_DECREASE);
			respawnSnake(bittenSnake);
		}
	}

	private void eatArtifact(Snake snek, Point point, int gridID) {
		// TODO handle artifacts
		switch (gridID) {
		case 10: // HEALTH_INCREASE
			System.out.println("i ate a " + Artifacts.HEALTH_INCREASE);
			snek.changeHealthModifier(ArtifactConstants.HEALTH_INCREASE);
			break;
		case 11: // HEALTH_DECREASE
			System.out.println("i ate a " + Artifacts.HEALTH_DECREASE);
			snek.changeHealthModifier(-1 * ArtifactConstants.HEALTH_DECREASE);
			break;
		case 12: // SIZE_INCREASE
			System.out.println("i ate a " + Artifacts.SIZE_INCREASE);
			snek.changeSizeModifier(ArtifactConstants.SIZE_INCREASE);
			break;
		case 13: // SIZE_DECREASE
			System.out.println("i ate a " + Artifacts.SIZE_DECREASE);
			snek.changeSizeModifier(-1 * ArtifactConstants.SIZE_DECREASE);
			this.grid.removeDeadBodyParts(snek);
			break;
		case 20: // BLOCK_CONTROL
			System.out.println("i ate a " + Artifacts.BLOCK_CONTROL);
			snek.setBlockControl(true);
			break;
		case 21: // REVERSE_CONTROL
			System.out.println("i ate a " + Artifacts.REVERSE_CONTROL);
			snek.setReverseControl(true);
			break;
		case 22: // INVULNERABILITY
			System.out.println("i ate a " + Artifacts.INVULNERABILITY);
			snek.setInvulnerability(true);
			break;
		case 23: // SPEED_INCREASE
			System.out.println("i ate a " + Artifacts.SPEED_INCREASE);
			break;
		case 24: // SPEED_DECREASE
			System.out.println("i ate a " + Artifacts.SPEED_DECREASE);
			snek.changeSpeedDecrease(ArtifactConstants.SPEED_DECREASE);
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
	
	private void respawnSnake(SnakeImpl s) {
		Point respawnPlace = placementStrategy.getRespawnPlace();
		s.resetPosition(respawnPlace);
	}
	
}
