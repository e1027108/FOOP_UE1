package client;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import gui.GameController;
import messagehandler.ServerMessageHandler;
import messagehandler.message.AckMessage;
import messagehandler.message.InfoMessage;
import messagehandler.message.Message;
import messagehandler.message.PlayerInfo;
import messagehandler.message.PlayerLeftMessage;
import messagehandler.message.TextMessage;

public class ReadThread extends Thread {

	private Client client;
	private Socket sock;
	private ServerMessageHandler serverMessageHandler;

	public ReadThread(Client client, Socket sock) {
		super();
		this.serverMessageHandler = new ServerMessageHandler();
		this.client = client;
		this.sock = sock;
	}

	@Override
	public void run() {
		while (true) {
			try {
				String msg = client.getIn().readLine();
				Message message = serverMessageHandler.decode(msg);
				//System.out.println(msg);
				switch (message.getType()) {
				case UPD:
					InfoMessage im = (InfoMessage)message;
					client.setRemainingTime(im.getRemainingTime());
					client.setPlayerList(im.getInfos());
					client.setArtifactList(im.getArtifacts());
					break;
				case SAD:
					GameController.engine.addMessage("You lost!");
					break;
				case STR:
					client.setGameActive(true);
					break;
				case END:
					client.setGameActive(false);
					break;
				case TXT:
					GameController.engine.addMessage(((TextMessage) message).getMessage());
					break;
				case PLL:
					client.removePlayer(((PlayerLeftMessage) message).getPlayerNumber()); //This doesn't activate the timeline :(
					GameController.engine.addMessage("Player " + ((PlayerLeftMessage) message).getPlayerNumber() + " left the game!");
					break;
				case ACK:
					client.getState().setNumber(((AckMessage) message).getNumber());
					System.out.println("Player number: " + client.getState().getNumber());
					break;
				default:
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
