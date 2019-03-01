import java.net.*;
import java.io.*;
import tcpclient.TCPClient;

//http://localhost:8888/ask?hostname=time.nist.gov&port=13
//http://localhost:8888/ask?hostname=whois.iis.se&port=43&string=kth.se

//java ConcHTTPAsk 8888

public class MyRunnable implements Runnable {
//Runnable ar fran javas egna bibliotek
	Socket clientSocket;
	
	public MyRunnable(Socket argumentSocket){ //Skapa instans/trad av MyRunnabe klassen fran extern fil
		clientSocket = argumentSocket;
	}

	public void run(){		//Kor i en trad
		
		
		try{		//Samma kod som fran task3
			String host, port, inputString;
			host = port = inputString = "";

			BufferedReader messageFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String urlQuerys = messageFromClient.readLine();
			String[] queryFiltered = urlQuerys.split("[ =&?/]");		//Exkludera [ =&?/] fran array
			
			DataOutputStream messageToClient = new DataOutputStream(clientSocket.getOutputStream());

			if(queryFiltered[2].equals("ask")){
	    		for(int i = 0; i < queryFiltered.length; i++){
					switch (queryFiltered[i]) {

						case "hostname":
							host = queryFiltered[i+1];
							i++;
							continue;
						case "port":
							port = queryFiltered[i+1];
							i++;
							continue;
						case "string":
							inputString = queryFiltered[i+1];
							i++;
							continue;

					}
				}

				try{	
	    			String tcpClientAnswer = TCPClient.askServer(host,Integer.parseInt(port),inputString); //Kor tcpClient, task1, fran min mapp
						String httpHeaderResponseCode  = "HTTP/1.1 200 OK\r\n\r\n";
						String serverDetails = "\nThe above was the server response from \nHost: " + host + "\nPort: " + port + "\nInput String: " + inputString;
						
						messageToClient.writeBytes(httpHeaderResponseCode + tcpClientAnswer + serverDetails +"\r\n");		//tcpClientAnswer is our actual result and messageToClient is shown in browser
						System.out.println("\nStart of request");
						
						
						System.out.println("GET /ask?host=" + host + "&port=" + port + "&inputString:" + inputString + " HTTP/1.1" + "\nHost: localhost:8888");

						System.out.println("End of request. Ready for next client");
	    		}
	    		catch( Exception e){
	    			messageToClient.writeBytes("HTTP/1.1 404 Not found\r\n");
					System.out.println("HTTP/1.1 404 Not found was sent to client");
	    		}
	    	}
	    	else{messageToClient.writeBytes("HTTP/1.1 400 Bad request\r\n"); 
				// System.out.println("HTTP/1.1 400 Bad request");
				
				}
	    	
			clientSocket.close();
		    messageFromClient.close();
		    messageToClient.close();
	      	
		}
		catch(IOException e){
			System.out.println("Exception/Server timed out");
		}
	}
}