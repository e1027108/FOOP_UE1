package client;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import messagehandler.ServerMessageHandler;
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
				switch (message.getType()) {
				case UPD:
					InfoMessage im = (InfoMessage)message;
					client.setRemainingTime(im.getRemainingTime());
					List<PlayerInfo> infos = (im.getInfos());
					System.out.println("HALLO hahahahahah");
					for (PlayerInfo pi : infos) {
						System.out.println("Server info: " + pi.getName() + ", " + pi.getColor());
					}
					client.setPlayerList(infos);
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
				default:
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
