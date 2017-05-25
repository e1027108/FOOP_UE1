package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import artifacts.Artifact;
import dto.GameDto;
import game.Game;
import game.Point;
import game.Snake;
import javafx.scene.paint.Color;
import messagehandler.ServerMessageHandler;
import messagehandler.message.ArtifactInfo;
import messagehandler.message.InfoMessage;
import messagehandler.message.Message;
import messagehandler.message.Message.MessageType;
import messagehandler.message.PlayerInfo;

public class Server {

	private static Server server;
	private static ServerSocket serverSocket;
	private static List<ClientThread> clientThreads;
	private static AcceptThread acceptThread;
	private static ServerMessageHandler serverMessageHandler;
	private static GameDto info;
	private static HashMap<Integer, PlayerInfo> playerList;
	private static Game game;

	private Server(int port, int players) throws IOException {
		serverMessageHandler = new ServerMessageHandler();
		serverSocket = new ServerSocket(port);
		clientThreads = new ArrayList<ClientThread>();
		acceptThread = new AcceptThread(this, players);
		acceptThread.setDaemon(true);
		acceptThread.start();
	}

	public static Server getServer(int port, int players) throws IOException {
		if (server == null) {
			server = new Server(port, players);
		}
		return server;
	}

	public static Server getServer() throws IOException {
		if (server == null) {
			return null;
		}
		return server;
	}

	public List<ClientThread> getClientThreads() {
		return clientThreads;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setGameInfo(GameDto gameInfo) {
		info = gameInfo;
	}

	public GameDto getGameInfo() {
		return info;
	}

	public void addPlayer(PlayerInfo player) {
		if(playerList == null) {
			playerList = new HashMap<Integer,PlayerInfo>();
		}
		playerList.put(player.getNumber(), player);
	}

	public void addAIPlayer(String name, int id, Color color) {
		PlayerInfo p = new PlayerInfo();
		p.setName(name);
		p.setNumber(id);
		p.setColor(color);
		addPlayer(p);
	}

	public PlayerInfo getPlayer(int playerNum) {
		return playerList.get(playerNum);
	}

	public HashMap<Integer,PlayerInfo> getAllPlayers() {
		return playerList;
	}

	public void interruptAcceptThread() {
		this.acceptThread.interrupt();
	}

	/**
	 * TODO!! -------------------------------------------------
	 * 			ClientThread list eintraege werden nicht entfernt, 
	 * 			auch wenn der client mit ALT+F4 geschlossen wird und
	 * 			das socket ding nicht mehr reagieren kann.
	 * 		---------------------------------------------------
	 */
	public void updateAll() {
		List<PlayerInfo> playerContainer = new ArrayList<PlayerInfo>();
		playerContainer.addAll(playerList.values());
		List<ArtifactInfo> artifactContainer = new ArrayList<ArtifactInfo>();

		if(game != null){
			artifactContainer.addAll(convertArtifactsToInfo(game.getGrid().getArtifacts()));
		}

		InfoMessage info = new InfoMessage(MessageType.UPD, playerContainer, artifactContainer,
				(int) server.getGameInfo().getGameDuration().toSeconds());

		String msg = serverMessageHandler.encode(info);
		for (ClientThread ct : clientThreads) {
			ct.getOut().println(msg);
		}
	}


	private List<ArtifactInfo> convertArtifactsToInfo(List<Artifact> artifacts) {
		ArrayList<ArtifactInfo> ais = new ArrayList<ArtifactInfo>();

		for(Artifact a: artifacts){
			if(a.isActive()){
				ais.add(new ArtifactInfo(a.getArtifactsMapping(), a.getPlacement()));
			}
		}

		return ais;
	}

	public void updatePlayerList(ArrayList<Snake> snakes) {
		for(Snake s : snakes) {
			// TODO: update all fields
			playerList.get(s.getGridID()).setHealth(s.getHealth());
			playerList.get(s.getGridID()).setMaxHealth(s.getMaxHealth());
			playerList.get(s.getGridID()).setBody((ArrayList<Point>)s.getBodyList());
		}
	}

	public void startGame(int gridsize) {		
		game = new Game(info.getPlayers(), gridsize, this);
		Thread t = new Thread(game);
		t.start();

		String msg = serverMessageHandler.encode(new Message(MessageType.STR));
		for (ClientThread ct : clientThreads) {
			ct.getOut().println(msg);
		}
	}

	public Game getGame() {
		return game;
	}
}
