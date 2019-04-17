/**
 * @author John Heinlein
 */
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class ServerDriver{
	public static void main(String[] args){
		//If no arguments are given, print usage and exit gracefully
		if(args.length < 1 || args.length > 4){
			if(args.length != 0){
				System.out.println("ERR: Expected 1 to 4 args, received " + args.length);
			}
			System.out.println("Usage: ServerDriver port [-v] [-t] [-time timeout]");
			System.exit(0);
		}
		String timeoutStr = "-time"; 	//CLI argument for timeout duration
		List<String> cmdargs = Arrays.asList(args);	//For order-agnostic checking of flags

		//Default assignment just because the try/catch doesn't guarantee an assignment and it makes
		//my IDE angry. This will be reassigned or the program will exit.
		int port = 6066;

		//Parse port
		try{
			port = Integer.parseInt(args[0]);
		}catch(NumberFormatException e){
			System.out.println("Given port must be a valid int\bExpected: \"ServerDriver port\"");
			System.exit(1);
		}

		//Evaluate the flags and run the server
		try{
			int timeout = 0;		//If not given, timeout is infinite

			//Parse timeout
			if(cmdargs.contains(timeoutStr)){
				timeout = Integer.parseInt(args[cmdargs.indexOf(timeoutStr) + 1]);
			}

			PalindromeCheckerServer server = new PalindromeCheckerServer(port, timeout);

			//Evaluate flags
			if(cmdargs.contains("-v")){ server.verbose(true); }
			if(cmdargs.contains("-t")){ server.useTimestamps(true); }

			server.start();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
