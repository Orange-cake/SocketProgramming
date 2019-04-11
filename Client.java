import java.net.*;
import java.io.*;

//TODO: Break into methods for the UI to hook into
public class Client{
	private String 	name;
	private int 	port;
	private Socket 	client;
	private DataOutputStream 	out;
	private DataInputStream 	in;
	private final String 		DISCONNECT_DELIM;

	public Client(String serverName, int serverPort, String delim){
		this.name = serverName;
		this.port = serverPort;
		this.DISCONNECT_DELIM = delim;
	}
	public Client(String serverName, int serverPort){
		this(serverName, serverPort, "END");
	}

	public void connect()throws IOException{
		try{
			log("Connecting to " + this.name + " on port " + this.port);
			client = new Socket(this.name, this.port);
			log("Connected to " + client.getRemoteSocketAddress());

			OutputStream toServer = client.getOutputStream();
			out = new DataOutputStream(toServer);
			log("Created DataOutputStream to send to server");

			InputStream fromServer = client.getInputStream();
			in = new DataInputStream(fromServer);
			log("Created DataInputStream to receive from server");
		}catch(IOException e){ e.printStackTrace(); }
	}

	//TODO: Make byte array for the sake of sending delim; read as ASCII if not delim
	public void send(String message)throws IOException{
		try{
			log("Writing to server...");
			out.writeUTF(message);
			log("Wrote to server");

			InputStream fromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(fromServer);

			String lastRead = in.readUTF();
			log("Server reply: " + lastRead);
		}catch(IOException e){ e.printStackTrace(); }
	}

	public void disconnect()throws IOException{
		try{
			log("Disconnecting from server...");
			out.writeUTF(DISCONNECT_DELIM);
			client.close();
			log("Disconnected");
		}catch(IOException e){e.printStackTrace();}
	}
	//TODO: Separate output stream for UI logging? i.e. not standard out
	private void log(String msg){ System.out.println("[Client @ " + new java.sql.Timestamp(System.currentTimeMillis()) + "] " + msg); }
}
