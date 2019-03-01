import java.net.*;
import java.io.*;
import tcpclient.TCPClient;

public class ConcHTTPAsk{

	public static void main(String[] args) {
		
		int myPort = Integer.parseInt(args[0]);

		try{
			ServerSocket serverSocket = new ServerSocket(myPort);
	
			while(true){
				Socket clientSocket = serverSocket.accept();
				new Thread(new MyRunnable(clientSocket)).start();
			}
		}
		catch(Exception e){
			System.out.println("At least one thread returned an exception error");
		}

	}
}