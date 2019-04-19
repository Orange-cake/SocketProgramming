/**
 * @author John Heinlein
 */
import java.net.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * PalindromeCheckerClient that connects to server on given address and port
 */
public class PalindromeCheckerClient{
	private String 				name;
	private int 				port;
	private Socket 				client;
	private DataOutputStream 	out;
	private DataInputStream 	in;
	private Boolean 			timestamps = false;
	private Boolean 			verbose = false;

	public static void main(String[] args){
		//Number of arguments sanitation
		if(args.length > 4){
			System.out.println("Usage: \"PalindromeCheckerClient [-port=portnum] [address] [-v] [-t]\"");
			System.exit(1);
		}
		List<String> cmdargs = Arrays.asList(args);	//For flag parsing
		Scanner scan = new Scanner(System.in);		//For user input

		//Parse port number and address
		int port = 1200; //Defaults if port not found in args
		String addr = "localhost";
		Pattern portReg = Pattern.compile("-port=[0-9]+");
		Pattern addrReg = Pattern.compile("\\S+\\.\\S+");
		for (String s:cmdargs) {
			if(portReg.matcher(s).matches()){
				port = Integer.parseInt(s.substring(6));
			}
			if(addrReg.matcher(s).matches()){
				addr = s;
			}
		}

		//Instantiate client
		PalindromeCheckerClient client = new PalindromeCheckerClient(addr, port);

		//Evaluate flags
		if(cmdargs.contains("-v")){ client.verbose(true); }
		if(cmdargs.contains("-t")){ client.useTimestamps(true); }

		//Run client
		try{
			client.connect();	//Connect to specified addr and port
			String out;			//String to send
			while(true){		//Just keep prompting for input
				System.out.print("String to check (None to quit): ");
				out = scan.nextLine();

				if(out.equals("")){
					client.disconnect();
					System.exit(0);
				}
				client.send(out);
			}
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	public PalindromeCheckerClient(String serverName, int serverPort){
		this.name = serverName;
		this.port = serverPort;
	}

	public void connect()throws IOException{
		try{
			logv("Connecting to " + this.name + " on port " + this.port);
			try{
				client = new Socket(this.name, this.port);
			}catch(ConnectException e){
				log("Connection refused, is the server running?");
				System.exit(1);
			}
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
			logv("Writing '" + message + "' to server...");

			out.writeUTF(message);
			log("Wrote: '" + message + "'");

			String lastRead = in.readUTF();

			log("Server reply: '" + lastRead + "'");
		}catch(IOException e){ e.printStackTrace(); }
	}

	public void disconnect()throws IOException{
		try{
			logv("Disconnecting from server...");
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
