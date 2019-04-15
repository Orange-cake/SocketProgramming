/**
 * @author John Heinlein
 */
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

public class TestDriver{
	public static void main(String[] args)throws IOException{
		List<String> cmdargs = Arrays.asList(args);
		Boolean sentinel;

		int TEST_PORT = 6066;
		int argsPort = TEST_PORT;

		if(args.length > 0){
			try{
				argsPort = Integer.parseInt(args[0].substring(6));
			}catch(NumberFormatException e){
				System.out.println("Command-line port is not valid.\njava TestDriver Port [-v] [-t]");
				System.exit(1);
			}
		}

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

		try{
			/*
			PalindromeCheckerServer server = new PalindromeCheckerServer(TEST_PORT);
			PalindromeCheckerClient client = new PalindromeCheckerClient("localhost", TEST_PORT);
			*/

			PalindromeCheckerServer server = new PalindromeCheckerServer(argsPort);
			PalindromeCheckerClient client = new PalindromeCheckerClient("localhost", argsPort);

			if(cmdargs.contains(new String("-v"))){
				server.verbose(true);
				client.verbose(true);
			}
			if(cmdargs.contains(new String("-t"))){
				server.useTimestamps(true);
				client.useTimestamps(true);
			}

			server.start();
			client.connect();

			for(int i = 0; i < testStrings.length; i++){
				client.send(testStrings[i]);
			}

			client.disconnect();
		}catch(IOException e){
			e.printStackTrace();
		}


	}
}
