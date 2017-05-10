package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import messagehandler.ClientMessageHandler;
import messagehandler.ServerMessageHandler;
import messagehandler.message.Message;

public class ClientThread extends Thread {

	private ServerMessageHandler serverMessageHandler;
	private ClientMessageHandler clientMessageHandler;
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;

	public ClientThread(Socket clientSocket) throws IOException {
		super();
		this.clientSocket = clientSocket;
		this.serverMessageHandler = new ServerMessageHandler();
		this.clientMessageHandler = new ClientMessageHandler();
		this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.out = new PrintWriter(clientSocket.getOutputStream(), true);
			
	}

	@Override
	public void run() {
		while (true) {
			try {
				// TODO updates an alle clients auch hier rausschicken?
				String messageString = in.readLine();
				if (messageString != null) {
					Message message = clientMessageHandler.decode(messageString);
					switch (message.getType()) {
					case DIC:
						break;
					case DIS:
						break;
					case PLR:
						break;
					default:
						System.err.println("Message type: \"" + message.getType() + "\" is not a valid client message");
						break;
					}

					System.out.println("ClientThread - received message: " + clientMessageHandler.encode(message));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
