# SocketProgramming
### CIS 419.01 Socket Programming Partner Project
Code by John Heinlein, testing by Ian Charissis
## How to Use:

### ServerDriver
```
Usage: java PalindromeCheckerServer [-port=portnum] [-v] [-t] [-timeout=timeout]
	      portnum: Port to listen on (Defaults to 1200)
	           -v: Enable more verbose logging
		   -t: Enable timestamps in logs
	-time=timeout: Enable a timeout of "timeout" milliseconds of no input before the socket is closed. Defualts to no timeout.
```
Running with no arguments will run a server on port1200
### ClientDriver
```
Usage: java PalindromeCheckerClient [-port=portnum] address [-v] [-t]
	      address: Address to connect to (defaults to localhost)
	-port=portnum: Port to connect to (defaults to 1200)
		   -v: Enable more verbose logging
		   -t: Enable timestamps in logs
```

The client will continually prompt the user for a string to check until the input matches the delim.
