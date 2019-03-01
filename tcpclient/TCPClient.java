//Testad 6/02

package tcpclient;

import java.net.*;
import java.io.*;

public class TCPClient {

	public static String askServer(String hostname, int port, String messageToServer) throws IOException {

		if (messageToServer == null)
			return askServer(hostname, port);

		String modifiedSentence;
		StringBuilder finalToServer = new StringBuilder();

		Socket clientSocket = new Socket(hostname, port);
		clientSocket.setSoTimeout(7000); // In milliseconds

		DataOutputStream myTextToServerOut = new DataOutputStream(clientSocket.getOutputStream());

		BufferedReader ReadFromServerIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		myTextToServerOut.writeBytes(messageToServer + '\n');

		while (!(clientSocket.isClosed())) {

			try {
				while ((modifiedSentence = ReadFromServerIn.readLine()) != "\n" && modifiedSentence != null) {
					finalToServer.append(modifiedSentence + '\n');
				}

				clientSocket.close();
				return finalToServer.toString();
			}

			catch (IOException exc) {

				// System.out.println("Nej");
				clientSocket.close();
				return finalToServer.toString();

			}

		}

		return finalToServer.toString();

	}

	public static String askServer(String hostname, int port) throws IOException {

		int maxTimeOut = 1000;
		int timeOutCount = 0;
		String modifiedSentence;
		StringBuilder finalToServer = new StringBuilder();

		Socket clientSocket = new Socket(hostname, port);
		clientSocket.setSoTimeout(7000); // In milliseconds

		BufferedReader ReadFromServerIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		while (!(clientSocket.isClosed())) {

			try {
				while ((modifiedSentence = ReadFromServerIn.readLine()) != "\n" && modifiedSentence != null) {
					finalToServer.append(modifiedSentence + '\n');
					timeOutCount++;
					if (timeOutCount >= maxTimeOut)
						return finalToServer.toString();
				}
				clientSocket.close();
				return finalToServer.toString();
			} catch (IOException exc) {
				clientSocket.close();
				return finalToServer.toString();
			}
		}

		return finalToServer.toString();

	}
}
