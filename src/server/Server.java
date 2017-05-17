package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dto.GameDto;
import messagehandler.message.PlayerInfo;

public class Server {

	private static Server server;
	private static ServerSocket serverSocket;
	private static List<ClientThread> clientThreads;
	private static AcceptThread acceptThread;
	
	private GameDto info;
	private HashMap<Integer,PlayerInfo> playerList;

	private Server(int port, int players) throws IOException {
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
	
}
