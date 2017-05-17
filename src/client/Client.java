package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.util.Duration;
import messagehandler.ClientMessageHandler;
import messagehandler.ServerMessageHandler;
import messagehandler.message.InfoMessage;
import messagehandler.message.Message;
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
	
	private PlayerInfo state;
	private int remainingTime;

	private Client(String host) {
		Client.host = host;
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
	
	public void init(String name, Color color) {
		// send color and name information to server
		state = new PlayerInfo();
		state.setName(name);
		state.setColor(color);
		ArrayList<PlayerInfo> container = new ArrayList<PlayerInfo>();
		container.add(state);
		out.println(clientMessageHandler.encode(new InfoMessage(MessageType.INI, container, 0)));
		InfoMessage info;
		try {
			info = (InfoMessage) serverMessageHandler.decode(in.readLine());
			state = info.getInfos().get(0);
			remainingTime = info.getRemainingTime();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	
}
