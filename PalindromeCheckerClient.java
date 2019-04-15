/**
 * @author John Heinlein
 */
import java.net.*;
import java.io.*;
import java.sql.Timestamp;

/**
 * PalindromeCheckerClient that connects to server on given address and port
 */
public class PalindromeCheckerClient{
	private String 				name;
	private int 				port;
	private Socket 				client;
	private DataOutputStream 	out;
	private DataInputStream 	in;
	private final String 		DISCONNECT_DELIM;
	private Boolean 			timestamps = false;
	private Boolean 			verbose = false;

	public PalindromeCheckerClient(String serverName, int serverPort, String delim){
		this.name = serverName;
		this.port = serverPort;
		this.DISCONNECT_DELIM = delim;
	}
	public PalindromeCheckerClient(String serverName, int serverPort){
		this(serverName, serverPort, "END");
	}

	public void connect()throws IOException{
		try{
			logv("Connecting to " + this.name + " on port " + this.port);
			client = new Socket(this.name, this.port);
			log("Connected to " + client.getRemoteSocketAddress());

			OutputStream toServer = client.getOutputStream();
			out = new DataOutputStream(toServer);
			logv("Created DataOutputStream to send to server");

			InputStream fromServer = client.getInputStream();
			in = new DataInputStream(fromServer);
			logv("Created DataInputStream to receive from server");
		}catch(IOException e){ e.printStackTrace(); }
	}

	/**
	 * Sends a string to server
	 * @param message UTF-8 String to send to server
	 * @throws IOException
	 */
	public void send(String message)throws IOException{
		try{
			log("Writing '" + message + "' to server...");
			out.writeUTF(message);
			logv("Wrote: '" + message + "'");

			InputStream fromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(fromServer);

			String lastRead = in.readUTF();
			log("Server reply: '" + lastRead + "'");
		}catch(IOException e){ e.printStackTrace(); }
	}

	public void disconnect()throws IOException{
		try{
			logv("Disconnecting from server...");
			out.writeUTF(DISCONNECT_DELIM);
			client.close();
			log("Disconnected");
		}catch(IOException e){e.printStackTrace();}
	}

	/**
	 * @param b True to enable timestamps in server messages, False to disable
	 */
	public void useTimestamps(Boolean b){this.timestamps = b;}

	/**
	 * @param b True to enable (more) verbose logging
	 */
	public void verbose(Boolean b){this.verbose = b;}

	/**
	 * Will only print if verbose(true) has been called
	 */
	private void logv(String msg){if(verbose) log(msg);}

	/**
	 * @param msg Message to log to standard output
	 */
	private void log(String msg){
		System.out.println("[" + ( (timestamps)?new Timestamp(System.currentTimeMillis()) + " ":"") + "Client] " + msg);
	}
}
