import java.io.IOException;

public class TestDriver{
	public static void main(String[] args)throws IOException{
		int TEST_PORT = 6066;
		String[] testStrings = new String[]{
				"Acrobats stab orca",
				"poor guy dump",
				"23454032",
				"A man, a plan, a cat, a ham, a yak, a yam, a hat, a canal-Panama",
				"As I pee, sir, I see Pisa",
				"Air an aria.",
				"123454321",
				"Was it a car or a cat I saw"
		};
		/*
		for(int i =0; i < testStrings.length; i++){
            System.out.println(PalindromeChecker.checkPalindrome(testStrings[i], true));
        }
		 */
		try{
			Thread t = new ServerThread(TEST_PORT);
			t.start();
		}catch(IOException e){
			e.printStackTrace();
		}

		Client client = new Client("localhost", TEST_PORT);
		client.connect();
		for(int i = 0; i < testStrings.length; i++){
			client.send(testStrings[i]);
		}
		//TODO: Change IO to byte string, see number of 0's as disconnection?
		client.disconnect();
	}
}
