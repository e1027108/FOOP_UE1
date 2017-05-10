package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

	private static Server server;
	private static ServerSocket serverSocket;
	private static List<ClientThread> clientThreads;
	private static AcceptThread acceptThread;

	private Server(int port, int players) throws IOException {
		serverSocket = new ServerSocket(port);
		clientThreads = new ArrayList<ClientThread>();
		acceptThread = new AcceptThread(this, players);
		acceptThread.setDaemon(true);
		acceptThread.start();
	}

	public static Server getServer(int port, int players) throws IOException {
		if (server == null) {
			server = new Server(port, players);
		}
		return server;
	}

	public static Server getServer() throws IOException {
		if (server == null) {
			return null;
		}
		return server;
	}

	public List<ClientThread> getClientThreads() {
		return clientThreads;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}
}
