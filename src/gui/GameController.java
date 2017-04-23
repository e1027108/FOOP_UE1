package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import artifacts.Artifact;
import dto.DataTransferrer;
import dto.GameDto;
import game.Directions;
import game.Game;
import game.GameGrid;
import game.Point;
import game.Snake;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class GameController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane gamePane, playPane, player1Pane, player2Pane, player3Pane, player4Pane;

	@FXML
	private TilePane gridPane;

	@FXML
	private Label player1Lbl, player2Lbl, player3Lbl, player4Lbl, timeLbl;

	@FXML
	private Button disconnectBtn, readyBtn;

	@FXML
	private TextArea msgArea;

	private GameDto info;
	// percentage on the color scale that we want to be off, at least (needs to be at most 1/6 (=0.166..), otw can't/ find 3 other colors)
	private final static double RANGE_VALUE = .12;
	private final static Duration MOVE_DURATION = Duration.millis(100);
	private static final int GRID_SIZE = 39;

	private Game game;

	private Timeline timeline;

	private final Color emptyCellColor = Color.valueOf("FFFFFF");

	private int tileSize;

	private Color[] colors;

	private MessageEngine engine;
	private Thread msgThread;

	@FXML
	public void initialize() {
		engine = new MessageEngine(msgArea);
		msgThread = new Thread(engine);
		msgThread.start();
		
		info = DataTransferrer.getInfo();

		if (info == null) {
			engine.addError("Error: could not find game information, please disconnect!");
		} else {
			setPlayerStyle(1, info.getName(), info.getColor());
		}

		//TODO feed messages into queue
		engine.addMessage("Welcome to Snake, " + info.getName() + "!");
		engine.addMessage("Control your snake with the WASD keys.");
		engine.addMessage("The longest snake at the end, wins!");

		timeLbl.setText(((int) info.getGameDuration().toSeconds()) + "s");

		assignAIColors();

		gridPane.setStyle("-fx-background-color: #FFFFFF;");

		tileSize = (int) Math.floor((gridPane.getPrefHeight() - (GRID_SIZE - 1))/ GRID_SIZE);
		System.out.println(tileSize);

		// TODO if AI only do not contact/host any server

		// TODO initialize with info from network/joincontroller

		// TODO place ready button in your own pane, create ready indicators for
		// all players

		// TODO if host create a start game button (right of disconnect button?
		// disabled until all are ready, function for re-disabling necessary)
	}

	// TODO add start button for host, additional information (health ...)

	private void setPlayerStyle(int player, String name, Color color) {
		//Integer.toHexString(color.hashCode())

		String hexcode = String.valueOf(color);
		hexcode = hexcode.substring(2, 8);

		String style = "-fx-background-color: #" + hexcode + ";";

		if (player == 1) {
			player1Lbl.setText(name);
			player1Pane.setStyle(style);
		} else if (player == 2) {
			player2Lbl.setText(name);
			player2Pane.setStyle(style);
		} else if (player == 3) {
			player3Lbl.setText(name);
			player3Pane.setStyle(style);
		} else if (player == 4) {
			player4Lbl.setText(name);
			player4Pane.setStyle(style);
		} else {
			// TODO some error
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void onStart() {
		game = new Game(info.getPlayers(), info.getName(), GRID_SIZE);
		game.run();

		initGrid();

		Duration d = MOVE_DURATION;
		timeline = new Timeline();
		timeline.setCycleCount((int) (info.getGameDuration().toMillis()/d.toMillis()));
		//timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(false);

		KeyFrame loopFrame = new KeyFrame(d, new EventHandler() {
			private int count = 0;

			@Override
			public void handle(Event event) {
				timeLbl.setText(getRemainingTime() + "s");
				game.loop();
				if (game.getSnakes().size() == 0) {
					timeline.stop();
					return;
				}
				update();
			}

			private int getRemainingTime(){
				count++;
				return (int) (info.getGameDuration().toSeconds() - (int) (count/10));
			}
		});
		timeline.getKeyFrames().add(loopFrame);

		timeline.setOnFinished(new EventHandler () {
			@Override
			public void handle(Event event) {
				timeline.stop();
				evaluateGame(game);
				return;
			}
		});

		timeline.play();

		if (!info.isAi()) {
			gamePane.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if (game.getSnakes().size() == 0) {
						timeline.stop();
						return;
					}
					if (event.getCode() == KeyCode.W) {
						game.getSnake(info.getName()).changeDirection(Directions.N);
					}
					if (event.getCode() == KeyCode.S) {
						game.getSnake(info.getName()).changeDirection(Directions.S);
					}
					if (event.getCode() == KeyCode.D) {
						game.getSnake(info.getName()).changeDirection(Directions.E);
					}
					if (event.getCode() == KeyCode.A) {
						game.getSnake(info.getName()).changeDirection(Directions.W);
					}
				}

			});
		}

	}

	protected void evaluateGame(Game game) {
		String endMessage = "";
		ArrayList<Snake> winners = new ArrayList<Snake>();

		//collect all snakes with max size
		for(Snake s: game.getSnakes()){
			if(winners.isEmpty()){
				if(s.isAlive()){
					winners.add(s);
				}
			}
			else{
				if(s.isAlive()){
					if(s.getSize() > winners.get(0).getSize()){
						winners.clear();
						winners.add(s);
					}
					else if(s.getSize() == winners.get(0).getSize()){
						winners.add(s);
					}
				}
			}
		}

		if(winners.size() == 0){
			endMessage = "All snakes are dead, no one wins!";
		}
		else if(winners.size() == 1){
			endMessage = winners.get(0).getName() + " wins!";
		}
		else{
			//collect multiple winner's names in comma-seperated list
			for(Snake w: winners){
				endMessage += w.getName() + ", ";
			}
			
			endMessage += "win!";
			
			//remove useless commas, make it sound like a sentence
			endMessage = endMessage.replace(", win", " win");
			String back = endMessage.substring(endMessage.lastIndexOf(','), endMessage.length() - 1);
			endMessage = endMessage.replace(back, " and" + back.substring(1,back.length()));
		}
		
		engine.addMessage(endMessage);
	}

	/**
	 * THIS method is responsible for visualizing the game grid. the
	 * {@link GameGrid}.draw() method handles the setting of the IDs.
	 */
	private void update() {

		Rectangle r;

		this.game.getArtifactHandler().checkDespawn();

		// set/remove artifacts
		for (Artifact a : game.getGrid().getArtifacts()) {
			Point pos = a.getPlacement();
			r = (Rectangle) gridPane.getChildren().get((pos.getX() * GRID_SIZE) + pos.getY());
			ImagePattern icon;
			try {
				if (a.isActive()) {
					icon = new ImagePattern(new Image(new FileInputStream(a.getImage())));
					r.setFill(icon);
				} else {
					r.setFill(emptyCellColor);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		for (Snake s : game.getSnakes()) {
			Point[] body = s.getBody();
			for (Point p : body) {
				r = (Rectangle) gridPane.getChildren().get((p.getX() * GRID_SIZE) + p.getY());
				r.setFill(colors[s.getGridID() - 1]);
			}

			for (Point p : s.getDeadParts()){
				if(p.getX() >= 0 && p.getX() < GRID_SIZE && p.getY() >= 0 && p.getY() < GRID_SIZE) {
					r = (Rectangle) gridPane.getChildren().get((p.getX() * GRID_SIZE) + p.getY());
					r.setFill(emptyCellColor);
				}
			}
			s.clearDeadParts();
		}

		game.removeDeadSnakes();
	}

	private void initGrid() {

		gridPane.getChildren().clear();

		gridPane.setPrefColumns(GRID_SIZE);
		gridPane.setPrefRows(GRID_SIZE);
		gridPane.setVgap(1);
		gridPane.setHgap(1);
		gridPane.setAlignment(Pos.CENTER);

		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				Rectangle r = new Rectangle(0, 0, tileSize, tileSize);
				r.setFill(emptyCellColor);
				gridPane.getChildren().add(r);
			}
		}
	}

	@FXML
	private void onDisconnectClick() {
		// TODO send info to other clients (host/other difference)
		// TODO clean up server/client

		// reset artifacts list to empty and close artifact handler thread
		if (game != null) {
			game.closeChildren();
			this.game.getGrid().shutdown();
			timeline.stop();
		}
		showJoin();
	}

	@FXML
	private void onReadyClick() {
		// TODO change text in button, send ready info (--> green checkmark or something)
		onStart();
	}

	private void showJoin() {
		Parent joinPane;
		try {
			joinPane = FXMLLoader.load(getClass().getResource("/join.fxml"));
		} catch (IOException e) {
			engine.addError("Load menu failed, please restart game!");
			return;
		}
		Scene scene = new Scene(joinPane);
		Stage stage = (Stage) ((Node) gamePane).getScene().getWindow();
		stage.setScene(scene);
		stage.setTitle("Create or join a game of Snake!");
		stage.show();
	}

	// TODO move that to server
	private void assignAIColors() {
		int relevantPlayerNumber = info.getPlayers();

		if (relevantPlayerNumber == 0) {
			relevantPlayerNumber = 4;
		}

		Color[] reserved = new Color[relevantPlayerNumber];

		reserved[0] = info.getColor();

		for (int i = 0; i < reserved.length; i++) {
			if (reserved[i] == null) {
				reserved[i] = findGoodColor(reserved);
			}

			if (i != 0) {
				setPlayerStyle(i + 1, "AI " + i, reserved[i]);
			}

			colors = reserved;

		}
	}

	private Color findGoodColor(Color[] reserved) {
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

}
