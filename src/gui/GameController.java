package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import artifacts.Artifact;
import client.Client;
import dto.DataTransferrer;
import dto.GameDto;
import game.Directions;
import game.Game;
import game.GameGrid;
import game.Point;
import game.Snake;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import messagehandler.message.ArtifactInfo;
import messagehandler.message.PlayerInfo;
import server.Server;

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
	private ProgressBar player1LifeBar, player2LifeBar, player3LifeBar, player4LifeBar;

	@FXML
	private TextArea msgArea;

	@FXML
	private ImageView block1img, block2img, block3img, block4img, rev1img, rev2img, rev3img, rev4img, inv1img, inv2img, inv3img, inv4img;

	private GameDto info;
	private final static Duration MOVE_DURATION = Duration.millis(100);
	public static final int GRID_SIZE = 39;
	private static enum imgType { B, R, I };

	private Game game;

	private Client client;
	private Server server;
	private boolean host;

	private Timeline timeline;

	private final Color emptyCellColor = Color.valueOf("FFFFFF");

	private int tileSize;

	private Color[] colors;

	private MessageEngine engine;
	private Thread msgThread;

	private String[] playerNames;
	private ProgressBar[] playerLifeBars;

	private ImageView[] playerInvulnerable;
	private ImageView[] playerBlocked;
	private ImageView[] playerReversed;

	@FXML
	public void initialize() {
		engine = new MessageEngine(msgArea);
		msgThread = new Thread(engine);
		msgThread.setDaemon(true);
		msgThread.start();

		info = DataTransferrer.getInfo();

		playerNames = new String[4];
		playerLifeBars = new ProgressBar[] { player1LifeBar, player2LifeBar, player3LifeBar, player4LifeBar };

		playerInvulnerable = new ImageView[] { inv1img, inv2img, inv3img, inv4img };
		playerBlocked = new ImageView[] { block1img, block2img, block3img, block4img };
		playerReversed = new ImageView[] { rev1img, rev2img, rev3img, rev4img };

		if (info == null) {
			engine.addError("Error: could not find game information, please disconnect!");
		} else {
			if (host = info.isHost()) {
				try {
					server = Server.getServer(1234, info.getPlayers());
				} catch (IOException e) {
					System.out.println("server IOException:");
					e.printStackTrace();
				}
			}
			try {
				System.out.println("IP ADDRESS: " + info.getIp().toString().split("/")[1]);
				client = Client.getClient(info.getIp().toString().split("/")[1]);
			} catch (IOException e) {
				System.out.println("client IOException:");
				e.printStackTrace();
			}
		}

		engine.addMessage("Welcome to Snake, " + info.getName() + "!");
		engine.addMessage("Control your snake with the WASD keys.");
		engine.addMessage("The longest snake at the end, wins!");

		gridPane.setStyle("-fx-background-color: #FFFFFF;");

		tileSize = (int) Math.floor((gridPane.getPrefHeight() - (GRID_SIZE - 1)) / GRID_SIZE);



		// TODO if AI only do not contact/host any server

		// TODO initialize with info from network/joincontroller

		// TODO get duration from server
		if (host) {
			timeLbl.setText(((int) info.getGameDuration().toSeconds()) + "s");
			server.setGameInfo(info);
		} 
		try {
			client.init(info.getName(), info.getColor());
		} catch (IOException e) {
			engine.addError("Client initialization problem!");
			return;
		}

		// Duration duration = client.getGameDuration();
		// setPlayerStyle(client.getPlayerNumber(), info.getName(),
		// info.getColor());
		// client.printState();
		// timeLbl.setText((int) duration.toSeconds() + "s");

		onStartClient();




		// TODO place ready button in your own pane, create ready indicators for
		// all players

		// TODO if host create a start game button (right of disconnect button?
		// disabled until all are ready, function for re-disabling necessary)
	}

	// TODO add start button for host, additional information (health ...)

	private void setPlayerStyle(int player, String name, Color color) {

		playerNames[player - 1] = name;

		String hexcode = String.valueOf(color);
		hexcode = hexcode.substring(2, 8);

		String style = "-fx-background-color: #" + hexcode + ";";
		String lifeBarStyle = "-fx-accent: lawngreen;";

		ProgressBar life = null;

		if (player == 1) {
			player1Lbl.setText(name);
			player1Pane.setStyle(style);
			life = player1LifeBar;
		} else if (player == 2) {
			player2Lbl.setText(name);
			player2Pane.setStyle(style);
			life = player2LifeBar;
		} else if (player == 3) {
			player3Lbl.setText(name);
			player3Pane.setStyle(style);
			life = player3LifeBar;
		} else if (player == 4) {
			player4Lbl.setText(name);
			player4Pane.setStyle(style);
			life = player4LifeBar;
		} else {
			// TODO some error
		}

		life.setProgress(1.0);
		life.setStyle(lifeBarStyle);
	}

	private void onStartServer() {
		// TODO AI auffuellen
		server.interruptAcceptThread();
		server.startGame(GRID_SIZE);		
		game = server.getGame();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void onStartClient() {
		initGrid();

		Duration d = MOVE_DURATION;
		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(false);

		KeyFrame loopFrame = new KeyFrame(d, new EventHandler() {
			@Override
			public void handle(Event event) {
				timeLbl.setText(client.getRemainingTime() + "s");
				for (PlayerInfo pi : client.getPlayerList()) {
					setPlayerStyle(pi.getNumber(), pi.getName(), pi.getColor());
				}
				if (client.isGameActive()) {
					updateClient();
				}
			}
		});
		timeline.getKeyFrames().add(loopFrame);

		timeline.setOnFinished(new EventHandler() {
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
					if(client.isGameActive()){
						if (client.getPlayerList().size() == 0) {
							timeline.stop();
							return;
						}
						if (event.getCode() == KeyCode.W) {
							client.sendDirection(Directions.N);
						}
						if (event.getCode() == KeyCode.S) {
							client.sendDirection(Directions.S);
						}
						if (event.getCode() == KeyCode.D) {
							client.sendDirection(Directions.E);
						}
						if (event.getCode() == KeyCode.A) {
							client.sendDirection(Directions.W);
						}
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
	 * THIS method is responsible for visualizing the game grid.
	 */
	private void updateClient() {
		initGrid();

		Rectangle r;


		/* TODO ----------------
		this.game.getArtifactHandler().checkDespawn();


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
		} ---------------------TODO--
		 */

		for (PlayerInfo s : client.getPlayerList()) {
			// life bars
			ProgressBar life = playerLifeBars[s.getNumber()-1];
			life.setProgress((double) s.getHealth() / s.getMaxHealth());
			// snakes
			ArrayList<Point> body = s.getBody();
			for (Point p : body) {
				r = (Rectangle) gridPane.getChildren().get((p.getX() * GRID_SIZE) + p.getY());
				r.setFill(s.getColor());
			}
		}
		for (ArtifactInfo a : client.getArtifactList()){
			Point pos = a.getPosition();
			r = (Rectangle) gridPane.getChildren().get((pos.getX() * GRID_SIZE) + pos.getY());
			ImagePattern icon;
			try {
				icon = new ImagePattern(new Image(new FileInputStream(a.getImage())));
				r.setFill(icon);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
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
			engine.interrupt();
			msgThread.interrupt();
			this.game.getGrid().shutdown();
			timeline.stop();
		}
		showJoin();
	}

	@FXML
	private void onReadyClick() {
		// TODO change text in button, send ready info (--> green checkmark or something)
		onStartServer();
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

	private void updateLifeBars() {
		for (int i = 0; i < playerNames.length; i++) {
			if (playerNames[i] != null) {
				Snake s = game.getSnake(playerNames[i]);
				ProgressBar life = playerLifeBars[i];
				if (s != null) {
					life.setProgress((double) s.getHealth() / s.getMaxHealth());
				}
			}
		}
	}

	//give b=block, r=reverse, i=invisible as effect
	private void setPlayerStatus(int player, imgType effect, boolean on){
		Image toSet = null;

		if(on){
			try {
				switch(effect){
				case B:
					toSet = new Image(new FileInputStream("img/block_control.png"));
					break;
				case I:
					toSet = new Image(new FileInputStream("img/invulnerability.png"));
					break;
				case R:
					toSet =  new Image(new FileInputStream("img/reverse_control.png"));
					break;
				default:
					//nothing
					break;
				}
			}
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		getImageView(effect)[player-1].setImage(toSet);
	}

	private ImageView[] getImageView(imgType effect) {
		if(effect == imgType.B){
			return playerBlocked;
		}
		if(effect == imgType.I){
			return playerInvulnerable;
		}
		if(effect == imgType.R){
			return playerReversed;
		}
		return null;
	}

}
