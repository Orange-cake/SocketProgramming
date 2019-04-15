/**
 * @author John Heinlein
 */
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

public class ClientDriver{
	public static void main(String[] args){
		if(args.length == 0){
			System.out.println("Usage: ClientDriver address port [-v] [-t]");
			System.exit(1);
		}
		List<String> cmdargs = Arrays.asList(args);
		Scanner s = new Scanner(System.in);

		Boolean sentinel = true;

		String address = "localhost";
		int port = 6066;

		if(args.length < 2 || args.length > 4){
			System.out.println("ERR: Expected at two to four arguments, received " + args.length);
			System.out.println("Usage: \"ClientDriver address port [-v] [-t]");
		}

		address = args[0];
		try{
			port= Integer.parseInt(args[1]);
		}catch(NumberFormatException e){
			System.out.println("Given port is not valid");
			System.exit(1);
		}

		PalindromeCheckerClient client = new PalindromeCheckerClient(address, port);
		if(cmdargs.contains("-v")){ client.verbose(true); }
		if(cmdargs.contains("-t")){ client.useTimestamps(true); }

		try{
			client.connect();
			String out;
			while(true){
				System.out.print("String to check (END to quit): ");
				out = s.nextLine();
				if(out.equals("END")){ System.exit(1);}
				client.send(out);
			}
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}

