import java.net.*;
import java.io.*;

/**
 * Server-Client communication exchange:
 * 		- Server waits for connection
 * 		- Client connects
 * 		- Client sends initial byte (int) to determine following data
 */
public class Server extends Thread {
	final String DISCONNECT_DELIM = "END";
	//public Server(int port, int timeout) throws IOException { }

	public void run(int port, int timeout){
		try{
			String lastInput;
			ServerSocket serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(timeout);
			log("Running with timeout " + timeout);

			log("Waiting for client on port " + serverSocket.getLocalPort() + "...");
			Socket server = serverSocket.accept();    //Hangs here waiting for connection
			log("Just connected to " + server.getRemoteSocketAddress());

			DataInputStream in = new DataInputStream(server.getInputStream());
			DataOutputStream out = new DataOutputStream(server.getOutputStream());

			while(true){
				lastInput = in.readUTF();
				log("Received message: " + lastInput);
				if(lastInput.equals(DISCONNECT_DELIM)){
					log("Delim received, closing socket");
					server.close();
					break;
				}else{
					out.writeUTF("Received message: \'" + lastInput + "\'");
				}
			}
		}catch(SocketTimeoutException s){ 	log("Socket timed out");
		}catch(SocketException s){			log("Socket closed");
		}catch(IOException e){				e.printStackTrace();}

	}

	public void openPort(int portNum){

	}
	void log(String msg){ System.out.println("[" + new java.sql.Timestamp(System.currentTimeMillis()) + "\tServer] " + msg); }
}