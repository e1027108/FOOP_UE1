package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dto.GameDto;
import messagehandler.ServerMessageHandler;
import messagehandler.message.InfoMessage;
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
	 * 			ClientThread list einträge werden nicht entfernt, 
	 * 			auch wenn der client mit ALT+F4 geschlossen wird und
	 * 			das socket ding nicht mehr reagieren kann.
	 * 		---------------------------------------------------
	 */
	public void updateAll() {
		List<PlayerInfo> container = new ArrayList<PlayerInfo>();
		container.addAll(playerList.values());

		InfoMessage info = new InfoMessage(MessageType.UPD, container,
				(int) server.getGameInfo().getGameDuration().toSeconds());
		
		String msg = serverMessageHandler.encode(info);
		System.out.println("UPD Message: " + msg);
		for (ClientThread ct : clientThreads) {
			ct.getOut().println(msg);
		}
	}
}
