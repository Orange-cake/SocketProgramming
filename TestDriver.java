import java.io.IOException;

public class TestDriver{
	public static void main(String[] args)throws IOException{
		int TEST_PORT = 6066;
		try{
			Thread t = new Server(TEST_PORT);
			t.start();
		}catch(IOException e){
			e.printStackTrace();
		}

		Client client = new Client("localhost", TEST_PORT);
		client.connect();
		client.send("foo");
		client.send("bar");
		client.send("buzz");
		//TODO: Change IO to byte string, see number of 0's as disconnection?
		client.disconnect();
	}
}
