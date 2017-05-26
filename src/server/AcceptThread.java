package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import messagehandler.message.AckMessage;
import messagehandler.message.Message.MessageType;

public class AcceptThread extends Thread {

	private int players;
	private Server server;

	public AcceptThread(Server server, int players) {
		super();
		this.server = server;
		this.players = players;
	}

	@Override
	public void run() {
		while (true) {
			try {
				int listSize = server.getClientThreads().size();
				if(listSize < players) {
					Socket clientSocket = server.getServerSocket().accept();
					int playerNum = calculateNum();
					ClientThread clientThread = new ClientThread(clientSocket, server, playerNum);
					server.getClientThreads().add(clientThread);
					clientThread.start();
					clientThread.inform(playerNum);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private int calculateNum() {
		ArrayList<Integer> free = new ArrayList<Integer>();
		free.add(1);
		free.add(2);
		free.add(3);
		free.add(4);

		for (ClientThread ct : server.getClientThreads()) {
			free.remove((Object) ct.getPlayerReferenceNumber());
		}
		return free.get(0);
	}
}
