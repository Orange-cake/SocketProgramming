/**
 * @author John Heinlein
 */
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

public class ClientDriver{
	public static void main(String[] args){
		//Number of arguments sanitation
		if(args.length < 2 || args.length > 4){
			if(args.length != 0){
				System.out.println("ERR: Expected at two to four arguments, received " + args.length);
			}
			System.out.println("Usage: \"ClientDriver address port [-v] [-t]\"");
			System.exit(1);
		}
		List<String> cmdargs = Arrays.asList(args);	//For order-agnostic checking of flags
		Scanner s = new Scanner(System.in);			//For user input

		//Default assignments just because the try/catch doesn't guarantee an assignment and it makes
		//my IDE angry. These will be reassigned or the program will exit.v
		int port = 6066;
		String address = args[0]; //Address should be first argument

		//Parse port number
		try{
			port= Integer.parseInt(args[1]);
		}catch(NumberFormatException e){
			System.out.println("Given port is not valid");
			System.exit(1);
		}

		//Instantiate client
		PalindromeCheckerClient client = new PalindromeCheckerClient(address, port);

		//Evaluate flags
		if(cmdargs.contains("-v")){ client.verbose(true); }
		if(cmdargs.contains("-t")){ client.useTimestamps(true); }

		//Run client
		try{
			client.connect();	//Connect to specified addr and port
			String out;			//String to send
			while(true){		//Just keep prompting for input
				System.out.print("String to check (END to quit): ");
				out = s.nextLine();
				client.send(out);
				if(out.equals("END")){ System.exit(0);}	//Delim check
			}
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}

