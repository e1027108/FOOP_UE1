package server;

import java.io.IOException;
import java.net.Socket;

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
		int playerNum = 2;
		while (server.getClientThreads().size() < players - 1) {
			try {
				Socket clientSocket = server.getServerSocket().accept();
				ClientThread clientThread = new ClientThread(clientSocket, server, playerNum);
				server.getClientThreads().add(clientThread);
				clientThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("done accepting players");
	}
}
