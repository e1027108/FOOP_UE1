package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {

	private static final String CLIENT_THREAD = "AcceptClients";
	private static Server server;
	private ServerSocket serverSocket;
	private List<Socket> clientSockets;

	private static Thread acceptClients;

	private Server() {
	}

	private Server(int port) throws IOException {
		ServerSocket serverSocket = new ServerSocket();
		clientSockets = new ArrayList<Socket>();
		server = null;
		acceptClients = new Thread(CLIENT_THREAD);
		acceptClients.setDaemon(true);
	}

	public static Server getServer(int port) throws IOException {
		if (server == null) {
			server = new Server(port);
		}
		return server;
	}

	public static Server getServer() throws IOException {
		if (server == null) {
			return null;
		}
		return server;
	}

	public void init() throws IOException, SocketException {
		acceptClients.start();
		while (true) {
			if (Thread.currentThread().getName().equals(CLIENT_THREAD)) {
				Socket clientSocket = serverSocket.accept();
				clientSockets.add(clientSocket);
				System.out.println("Server: accepted new client");
			}
			for (Socket cs : clientSockets) {
				PrintWriter out = new PrintWriter(cs.getOutputStream(), true);
				out.println("Hallo, test test :D");
			}
		}
	}

	// TODO more methods/threads for other server tasks

	public void initDone() {
		acceptClients.interrupt();
	}

}
