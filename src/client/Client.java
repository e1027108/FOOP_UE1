package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {

	private static final String HOST = "127.0.0.1";
	private static final int PORT = 1234;
	private Socket sock;

	public static void main(String... args) throws IOException {
		Socket sock = new Socket(HOST, PORT);
		BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		String ret = in.readLine();
		System.out.println("Client: " + ret);
		in.close();
		sock.close();
	}
}
