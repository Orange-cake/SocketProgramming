/**
 * @author John Heinlein
 */
import java.net.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * PalindromeCheckerServer represents a socket on a thread, handling one client over that port
 */
public class PalindromeCheckerServer extends Thread {
	private ServerSocket serverSocket;
	private int port;
	private String lastInput;
	//private String lastOutput;

	private Boolean verbose = false;
	private Boolean timestamps = false;

	public static void main(String[] args){
		//If no arguments are given, print usage and exit gracefully
		if(args.length > 4){
			System.out.println("Usage: PalindromeCheckerServer [-port=portnum] [-v] [-t] [-timeout=timeout]");
			System.exit(0);
		}
		List<String> cmdargs = Arrays.asList(args);	//For order-agnostic checking of flags

		//Parse port and timeout
		int port = 1200;
		int timeout = 0;
		Pattern portReg = Pattern.compile("-port=[0-9]+");
		Pattern timeReg = Pattern.compile("-timeout=[0-9]+");
		for (String s:cmdargs) {
			if(portReg.matcher(s).matches()){
				port = Integer.parseInt(s.substring(6));
			}if(timeReg.matcher(s).matches()){
				timeout = Integer.parseInt(s.substring(9));
			}
		}

		//Evaluate the flags and run the server
		try{
			PalindromeCheckerServer server = new PalindromeCheckerServer(port, timeout);

			//Evaluate flags
			if(cmdargs.contains("-v")){ server.verbose(true); }
			if(cmdargs.contains("-t")){ server.useTimestamps(true); }

			server.start();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
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
				try{
					lastInput = in.readUTF();
				}catch(EOFException eof){
					log("Client disconnected, shutting down");
					break;
				}
				log("Received message: " + lastInput);
				logv("\tFrom: " + server.getRemoteSocketAddress());

				if(checkPalindrome(lastInput)){
					log("Input is palindrome");
					out.writeUTF(lastInput + " is a palindrome");
				}else{
					log("Input is not a palindrome");
					out.writeUTF(lastInput + " is not a palindrome");
				}
			}
		}catch(SocketTimeoutException t){ 	log("Socket timed out");
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