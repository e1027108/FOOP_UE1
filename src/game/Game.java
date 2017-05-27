package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import java.util.Map.Entry;

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
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;
import messagehandler.message.PlayerInfo;
import server.Server;

public class Game implements CollisionListener, Runnable{

	private GameGrid grid;
	private int numPlayers;
	private String firstPlayer;
	private ArtifactHandler artifactHandler;
	private List<Thread> children;

	private ArrayList<Snake> snakes;
	
	private ArtifactPlacementStrategy placementStrategy;
	private ArtifactCoordinateGenerator coordinateGenerator;
	
	private Server server;
	
	private Duration duration;
	
	// percentage on the color scale that we want to be off, at least (needs to be at most 1/6 (=0.166..), otw can't/ find 3 other colors)
	private final static double RANGE_VALUE = .12;

	// TODO: change error message --> just temporary
	public Game(int num, int gridSize, Duration duration, Server server) {
		firstPlayer = server.getPlayer(1).getName();
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
		
		this.duration = duration;
		this.server = server;
	}

	public void run() {
		Snake player;

		// create players
		snakes = new ArrayList<Snake>();

		// inits human players and fills remaining slots with AIs
		if (firstPlayer != null) {
			for(PlayerInfo p : server.getAllPlayers().values()) {
				player = new SnakeImpl(p.getName(), p.getNumber(), Directions.N);
				snakes.add(player);
			}
			int i = server.getAllPlayers().size() + 1;
			while (i <= server.getGameInfo().getPlayers()) {
				player = new SnakeAI("ai_" + i, i, Directions.N, this);
				Color AIcolor = assignAIColor(server.getAllPlayers().values());
				server.addAIPlayer(SnakeAI.AI_PREFIX + i, i, AIcolor);
				snakes.add(player);
				i++;				
			}
		} else {
			
			// init AIs for now
			List<Directions> directions = new ArrayList<Directions>();
			directions.add(Directions.N);
			directions.add(Directions.S);
			directions.add(Directions.E);
			directions.add(Directions.W);
			for (int i = 1; i <= numPlayers; i++) {
				player = new SnakeAI("ai_" + i, i, directions.get(i - 1), this);
				Color AIcolor = assignAIColor(server.getAllPlayers().values());
				server.addAIPlayer(SnakeAI.AI_PREFIX + i, i, AIcolor);
				snakes.add(player);
			}
		}

		// initialize snake positions
		for(Snake s : snakes) {
			System.out.println(s.getGridID() + " " + s.getName());
		}
		grid.initPositions(snakes);

		/*
		 * start a seperate thread with artifact handler, waiting for artifact
		 * spawn should not affect this thread
		 */
		Thread artifactChild = new Thread(this.artifactHandler);
		children.add(artifactChild);
		artifactChild.setDaemon(true);
		artifactChild.start();
		
		long time = (long) this.duration.toMillis();
		long last = System.currentTimeMillis();

		// game loop
		while (true) {
			if(time > 0 && getSnakes().size() > 1) {
				step();		
				server.updatePlayerList(getSnakes());
				server.updateAll();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long now = System.currentTimeMillis();
				long diff = now - last;
				last = now;
				time = time - diff;
				this.duration = new Duration(time);
			}
			else {
				System.out.println("End of Game");
				artifactChild.interrupt();
				server.endGame();
				break;
			}
		}
	}

	private Color assignAIColor(Collection<PlayerInfo> players) {
		ArrayList<Color> reserved = new ArrayList<Color>();
		
		for(PlayerInfo p : players){
			reserved.add(p.getColor());
		}

		return findGoodColor(reserved);
	}
	
	private Color findGoodColor(ArrayList<Color> reserved) {
		ArrayList<Pair<Double, Double>> redIntervals = new ArrayList<Pair<Double, Double>>();
		ArrayList<Pair<Double, Double>> greenIntervals = new ArrayList<Pair<Double, Double>>();
		ArrayList<Pair<Double, Double>> blueIntervals = new ArrayList<Pair<Double, Double>>();

		for (Color c : reserved) {
			if (c != null) {
				redIntervals.add(buildInterval(c.getRed()));
				greenIntervals.add(buildInterval(c.getGreen()));
				blueIntervals.add(buildInterval(c.getBlue()));
			}
		}

		return new Color(findValidColorValue(redIntervals), findValidColorValue(blueIntervals),
				findValidColorValue(greenIntervals), 1);
	}
	
	private double findValidColorValue(ArrayList<Pair<Double, Double>> intervals) {
		Random r = new Random();
		double value = 0;
		boolean valid = false;

		while (!valid) {
			value = r.nextDouble();
			for (Pair<Double, Double> p : intervals) {
				if (value < p.getKey() || value > p.getValue()) {
					valid = true;
				} else {
					valid = false;
					break;
				}
			}
		}

		return value;
	}

	private Pair<Double, Double> buildInterval(Double value) {
		double posrange = RANGE_VALUE;
		double negrange = RANGE_VALUE;

		if (value - negrange <= 0) {
			negrange = value;
		}
		if (value + posrange >= 1) {
			posrange = 1 - value;
		}

		return new Pair<Double, Double>(value - negrange, value + posrange);
	}
	
	public void step() {
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
				// TODO: is there another way to identify a snake as ai?
				if(!snakes.get(i).getName().contains("ai_")) {
					server.sendLooseMessage(i);
				}
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
		s.resetPosition(respawnPlace, grid.getSize());
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	
}
