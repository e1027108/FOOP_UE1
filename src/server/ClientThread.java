package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import messagehandler.ClientMessageHandler;
import messagehandler.MessageHandler;
import messagehandler.ServerMessageHandler;
import messagehandler.message.AckMessage;
import messagehandler.message.DirectionChangeMessage;
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
				String messageString = in.readLine();
				if (messageString != null) {
					Message message = clientMessageHandler.decode(messageString);
					switch (messageString.substring(0,3)) {
					case MessageHandler.DIRECTION_CHANGE:
						DirectionChangeMessage dirchange = (DirectionChangeMessage) message;
						server.getGame().getSnakeByGridID(playerReferenceNumber).changeDirection(dirchange.getDirection());
						server.updateAll();
						break;
					case MessageHandler.DISCONNECT:
						server.getAllPlayers().remove(server.getPlayer(playerReferenceNumber));
						server.getGame().getSnakes().remove(playerReferenceNumber - 1);
						clientSocket.close();
						this.interrupt();
						server.informPlayerLeft(playerReferenceNumber);
						break;
					case MessageHandler.PLAYER_READY:
						server.getPlayer(playerReferenceNumber).setReady(true);
						server.updateAll();
						if(playerReferenceNumber != 1){
							server.sendText("Player " + playerReferenceNumber + " is ready.");
						}
						break;
					case MessageHandler.INITIALIZATION:
						InfoMessage clientInfo = (InfoMessage) message;
						PlayerInfo toAdd = new PlayerInfo(playerReferenceNumber, clientInfo.getInfos().get(0).getName(),
								clientInfo.getInfos().get(0).getColor());
						server.addPlayer(toAdd);
						server.updateAll();
						break;
					default:
						System.err.println("Message type: \"" + message.getType() + "\" is not a valid client message");
						break;
					}

					//System.out.println("ClientThread - received message: " + clientMessageHandler.encode(message));
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

	public void inform(int playerNum) {
		System.out.println("Sent: " + playerNum);
		out.println(serverMessageHandler.encode(new AckMessage(MessageType.ACK,playerNum)));
	}
}
