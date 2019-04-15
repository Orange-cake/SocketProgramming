/**
 * @author John Heinlein
 */
import java.net.*;
import java.io.*;
import java.sql.Timestamp;
/**
 * PalindromeCheckerServer represents a socket on a thread, handling one client over that port
 */
public class PalindromeCheckerServer extends Thread {
	private ServerSocket serverSocket;
	private int port;
	private String lastInput;
	private String lastOutput;
	private final String DISCONNECT_DELIM = "END";

	private Boolean verbose = false;
	private Boolean timestamps = false;

	/**
	 * Create a new server thread
	 * @param port Port to open on server
	 * @param timeout Time in ms without input until port closes
	 * @throws IOException
	 */
	public PalindromeCheckerServer(int port, int timeout) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(timeout);
		this.port = port;
		log("Running with timeout: " + timeout);
	}

	/**
	 * Create new server thread with a default timeout of ten seconds
	 * @param port Port to open for client
	 * @throws IOException
	 */
	public PalindromeCheckerServer(int port) throws IOException{
		this(port, 10000);
	}

	/**
	 * Overridden from Thread, called with start() in driver program.
	 */
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
					logv("Delim received, closing socket");
					server.close();
					break;
				}else{
					logv("Checking for palindrome...");
					if(checkPalindrome(lastInput)){
						logv("Input is palindrome");
						out.writeUTF(lastInput + " is a palindrome");
					}else{
						logv("Input is not a palindrome");
						out.writeUTF(lastInput + " is not a palindrome");
					}
				}
			}
		}catch(SocketTimeoutException s){ 	log("Socket timed out");
		}catch(SocketException s){			log("Socket closed");
		}catch(IOException e){				e.printStackTrace();}

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
	 * Get port of current socket
	 * @return Port of current socket
	 */
	public int getPort(){return this.port;}

	/**
	 * Will only print if verbose(true) has been called
 	 */
	private void logv(String msg){if(verbose) log(msg);}

	/**
	 * @param msg Message to log to standard output
	 */
	private void log(String msg){
		System.out.println("[" + ( (timestamps)?new Timestamp(System.currentTimeMillis()) + " ":"") + "Server] " + msg);
	}

	private boolean checkPalindrome(String in){
		//Sanitize
		in = in.replaceAll("\\W", "");
		in = in.toLowerCase();

		//Convert to char arrays
		char[] inAsChar = in.toCharArray();
		char[] tmp = new char[inAsChar.length];

		for(int i=in.length()-1, j=0; i>=0; i--, j++){
			tmp[j] = inAsChar[i];
		}
		//Verbose logging
		if(verbose){
			logv("\t\tOriginal: " + new String(inAsChar));
			logv("\t\tReversed: " + new String(tmp));
			logv("\t\tEqual?    " + java.util.Arrays.equals(inAsChar, tmp));
		}
		return java.util.Arrays.equals(inAsChar, tmp);
	}
}