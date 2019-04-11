import java.net.*;
import java.io.*;

public class Server extends Thread {
	private ServerSocket serverSocket;
	int port;
	String lastInput;
	String lastOutput;
	final String DISCONNECT_DELIM = "END";

	public Server(int port, int timeout) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
		this.port = port;
		log("Running with timeout: " + timeout);
	}
	public Server(int port) throws IOException{	//Set default timeout if none specified
		this(port, 10000);
	}

	public void run(){
		try{
			Boolean sentinel = true;
			log("Waiting for client on port " + serverSocket.getLocalPort() + "...");
			Socket server = serverSocket.accept();	//Hangs here waiting for connection
			log("Just connected to " + server.getRemoteSocketAddress());

			DataInputStream in = new DataInputStream(server.getInputStream());
			DataOutputStream out = new DataOutputStream(server.getOutputStream());

			while(sentinel){
				lastInput = in.readUTF();
				log("Received message: " + lastInput);
				if(lastInput.equals(DISCONNECT_DELIM)){
					log("Delim received, closing socket...");
					server.close();
				}else{
					out.writeUTF("Received message: \'" + lastInput + "\'");
				}
			}
		}catch(SocketTimeoutException s){ 	log("Socket timed out");
		}catch(SocketException s){			log("Socket closed");
		}catch(IOException e){				e.printStackTrace();}

	}

	void log(String msg){ System.out.println("[Server @ " + new java.sql.Timestamp(System.currentTimeMillis()) + "] " + msg); }
}