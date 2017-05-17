package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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
		int listSize = server.getClientThreads().size();
		while (true) {
			try {
				if(listSize < players -1) {
					Socket clientSocket = server.getServerSocket().accept();
					int playerNum = calculateNum();
					ClientThread clientThread = new ClientThread(clientSocket, server, playerNum);
					server.getClientThreads().add(clientThread);
					clientThread.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private int calculateNum() {
		ArrayList<Integer> free = new ArrayList<Integer>();
		free.add(2);
		free.add(3);
		free.add(4);

		for (ClientThread ct : server.getClientThreads()) {
			free.remove((Object) ct.getPlayerReferenceNumber());
		}
		return free.get(0);
	}
}