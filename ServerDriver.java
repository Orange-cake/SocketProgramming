/**
 * @author John Heinlein
 */
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class ServerDriver{
	public static void main(String[] args){
		if(args.length == 0){
			System.out.println("Usage: ServerDriver port [-v] [-t] [-time=timeout]");
			System.exit(1);
		}
		List<String> cmdargs = Arrays.asList(args);

		int port = 6066;
		try{
			port = Integer.parseInt(args[0]);
		}catch(NumberFormatException e){
			System.out.println("Given port must be a valid int\bExpected: \"ServerDriver port\"");
			System.exit(1);
		}

		try{
			int timeout = 0;
			if(cmdargs.contains("-time=")){
				timeout = Integer.parseInt( args[ cmdargs.indexOf("-time=") ].substring(6) );
			}
			PalindromeCheckerServer server = new PalindromeCheckerServer(port, timeout);
			if(cmdargs.contains("-v")){ server.verbose(true); }
			if(cmdargs.contains("-t")){ server.useTimestamps(true); }
			server.start();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
