package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import messagehandler.ClientMessageHandler;
import messagehandler.ServerMessageHandler;
import messagehandler.message.InfoMessage;
import messagehandler.message.Message;
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
						InfoMessage clientInfo = (InfoMessage) message;
						server.addPlayer(new PlayerInfo(playerReferenceNumber, clientInfo.getInfos().get(0).getName(),
								clientInfo.getInfos().get(0).getColor()));
						server.updateAll();
						break;
					default:
						System.err.println("Message type: \"" + message.getType() + "\" is not a valid client message");
						break;
					}

					System.out.println("ClientThread - received message: " + clientMessageHandler.encode(message));
				}
			} catch (SocketException se){
				server.getAllPlayers().remove(playerReferenceNumber);
				server.getClientThreads().remove(this);
				this.interrupt();
			} catch (IOException e) {
				server.getAllPlayers().remove(playerReferenceNumber);
				server.getClientThreads().remove(this);
				this.interrupt();
				e.printStackTrace();
			}
		}
	}

	public BufferedReader getIn() {
		return in;
	}

	public PrintWriter getOut() {
		return out;
	}

	public int getPlayerReferenceNumber() {
		return playerReferenceNumber;
	}
}
