package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.util.Duration;
import messagehandler.ClientMessageHandler;
import messagehandler.ServerMessageHandler;
import messagehandler.message.InfoMessage;
import messagehandler.message.Message.MessageType;
import messagehandler.message.PlayerInfo;

public class Client {

	private static ClientMessageHandler clientMessageHandler;
	private static ServerMessageHandler serverMessageHandler;
	private static String host;
	private static final int PORT = 1234;
	private static Client client;
	private static Socket sock;
	private static BufferedReader in;
	private static PrintWriter out;
	private static ReadThread readThread;
	private static List<PlayerInfo> playerList;
	private static boolean gameActive;
	
	private PlayerInfo state;
	private int remainingTime;

	private Client(String host) {
		Client.host = host;
		gameActive = false;
		clientMessageHandler = new ClientMessageHandler();
		serverMessageHandler = new ServerMessageHandler();
	}
	
	public static Client getClient(String host) throws IOException {
		if(client == null) {
			client = new Client(host);
			client.connect();
		}
		return client;
	}
	
	public static Client getClient() {
		if(client == null) {
			return null;
		}
		return client;
	}
	
	private void connect() throws IOException {
		sock = new Socket(host, PORT);
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new PrintWriter(sock.getOutputStream(), true);
		System.out.println("Client: setup done");
	}
	
	public void init(String name, Color color) throws IOException {
		// send color and name information to server
		state = new PlayerInfo();
		state.setName(name);
		state.setColor(color);
		ArrayList<PlayerInfo> container = new ArrayList<PlayerInfo>();
		container.add(state);
		out.println(clientMessageHandler.encode(new InfoMessage(MessageType.INI, container, 0)));
		readThread = new ReadThread(this, sock);
		readThread.setDaemon(true);
		readThread.start();
	}
	
	public Duration getGameDuration() {
		Duration ret = null;
		ret = Duration.valueOf(String.valueOf(remainingTime) + "s");	
		return ret;
	}
	
	public int getPlayerNumber() {
		return state.getNumber();
	}
	
	public void printState() {
		System.out.println(state.toString());
	}

	public BufferedReader getIn() {
		return in;
	}

	public List<PlayerInfo> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<PlayerInfo> playerList) {
		Client.playerList = playerList;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	public boolean isGameActive() {
		return gameActive;
	}

	public void setGameActive(boolean gameActive) {
		Client.gameActive = gameActive;
	}

	public PlayerInfo getState() {
		return state;
	}

	public void setState(PlayerInfo state) {
		this.state = state;
	}
}
