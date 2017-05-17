package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import messagehandler.ClientMessageHandler;
import messagehandler.ServerMessageHandler;
import messagehandler.message.InfoMessage;
import messagehandler.message.Message;
import messagehandler.message.Message.MessageType;
import messagehandler.message.PlayerInfo;

public class ClientThread extends Thread {

	private ServerMessageHandler serverMessageHandler;
	private ClientMessageHandler clientMessageHandler;
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	private Server server;
	
	private int playerReferenceNumber;
	
	public ClientThread(Socket clientSocket, Server server, int playerNum) throws IOException {
		super();
		this.clientSocket = clientSocket;
		this.serverMessageHandler = new ServerMessageHandler();
		this.clientMessageHandler = new ClientMessageHandler();
		this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		this.out = new PrintWriter(clientSocket.getOutputStream(), true);
		this.server = server;
		this.playerReferenceNumber = playerNum;
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
					case INI:
						// update player info with name and color from client
						InfoMessage clientInfo = (InfoMessage) message;
						server.getPlayer(playerReferenceNumber).setName(clientInfo.getInfos().get(0).getName());
						server.getPlayer(playerReferenceNumber).setColor(clientInfo.getInfos().get(0).getColor());
						
						// return InfoMessage with updated player info and duration to client
						PlayerInfo player = server.getPlayer(playerReferenceNumber);
						ArrayList<PlayerInfo> container = new ArrayList<PlayerInfo>();
						container.add(player);
						InfoMessage info = new InfoMessage(MessageType.BAI, container,
								(int) server.getGameInfo().getGameDuration().toSeconds());
						out.println(serverMessageHandler.encode(info));
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
