import java.net.*;
import java.io.*;

/**
 * Client that connects to server on given address and port
 * TODO: Handle multiple connections
 */
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

	/**
	 * Sends a string to server
	 * @param message UTF-8 String to send to server
	 * @throws IOException
	 */
	//TODO: Make byte array for the sake of sending delim; read as ASCII if not delim?
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

	/* TODO: Might finish later?
	public void send(byte[] bytes)throws IOException{
		try{
			InputStream fromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(fromServer);

			log("Telling server to expect " + bytes.length + " bytes");
			out.write(bytes.length);

			log("Waiting for acknowledgement of " + bytes.length + " bytes");
			String ack = in.readUTF();
			if(!ack.equals("ACK")){
				log("\tServer did not acknowledge, terminating send");
				return;
			}
			log("Ack received");
			log("Writing " + bytes.length + " bytes to server...");
			out.write(bytes, 0, bytes.length);
			log("\tWrote" + bytes.length + " bytes to server");
		}catch(IOException e){e.printStackTrace();}
	}
	*/
	public void disconnect()throws IOException{
		try{
			log("Disconnecting from server...");
			out.writeUTF(DISCONNECT_DELIM);
			client.close();
			log("Disconnected");
		}catch(IOException e){e.printStackTrace();}
	}
	//TODO: Separate output stream for UI logging? i.e. not standard out
	private void log(String msg){
		System.out.println("["+new java.sql.Timestamp(System.currentTimeMillis()) +" Client] "+msg);
	}
}