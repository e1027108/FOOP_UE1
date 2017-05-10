package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.util.Duration;
import messagehandler.ClientMessageHandler;
import messagehandler.ServerMessageHandler;
import messagehandler.message.Message;
import messagehandler.message.Message.MessageType;

public class Client {

	private static ClientMessageHandler clientMessageHandler;
	private static ServerMessageHandler serverMessageHandler;
	private static String host;
	private static final int PORT = 1234;
	private static Client client;
	private static Socket sock;
	private static BufferedReader in;
	private static PrintWriter out;

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
		out.println(clientMessageHandler.encode(new Message(MessageType.PLR)));
	}
	
	public Duration getGameDuration() {
		Duration ret = null;
		// TODO get duration from server
		return ret;
	}
}
