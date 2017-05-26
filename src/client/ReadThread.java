package client;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import messagehandler.ServerMessageHandler;
import messagehandler.message.AckMessage;
import messagehandler.message.InfoMessage;
import messagehandler.message.Message;
import messagehandler.message.PlayerInfo;

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
					break;
				case STR:
					client.setGameActive(true);
					break;
				case END:
					client.setGameActive(false);
					break;
				case TXT:
					break;
				case PLL:
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
