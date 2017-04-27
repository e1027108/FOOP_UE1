package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static final int PORT = 1234;
	private static final String HOST = "127.0.0.1";

	public static void main(String... args) throws IOException {
		while (true) {
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Server: listening");
			try {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Server: accepted");
				try {
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					out.println("Hallo, test test :D");
				} finally {
					clientSocket.close();
				}
			} finally {
				serverSocket.close();
			}
		}
	}

}
