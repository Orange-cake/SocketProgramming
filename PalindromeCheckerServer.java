import java.net.*;
import java.io.*;

/**
 * ServerThread represents a socket on a thread, handling one client over that port
 */
public class ServerThread extends Thread {
	private ServerSocket serverSocket;
	private int port;
	private String lastInput;
	private String lastOutput;
	private final String DISCONNECT_DELIM = "END";

	/**
	 * @param port Port to open for client
	 * @param timeout Time in ms without input until port closes
	 * @throws IOException
	 */
	public ServerThread(int port, int timeout) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
		this.port = port;
		log("Running with timeout: " + timeout);
	}

	/**
	 * Timeout defaults to 10 seconds
	 * @param port Port to open for client
	 * @throws IOException
	 */
	public ServerThread(int port) throws IOException{	//Set default timeout if none specified
		this(port, 10000);
	}

	//Overridden from Thread, called with start() in driver program
	public void run(){
		try{
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
					log("Checking for palindrome...");
					log("Input palindrome: " + PalindromeChecker.checkPalindrome(lastInput).toString());
				}
			}
		}catch(SocketTimeoutException s){ 	log("Socket timed out");
		}catch(SocketException s){			log("Socket closed");
		}catch(IOException e){				e.printStackTrace();}

	}
	void log(String msg){ System.out.println("[" + new java.sql.Timestamp(System.currentTimeMillis()) + " Server] " + msg); }
	int getPort(){return this.port;}
}