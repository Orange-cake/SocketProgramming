# SocketProgramming
### CIS 419.01 Socket Programming Partner Project
Code by John Heinlein, testing by Ian Charissis
## How to Use:

### TestDriver
```
Usage:
	TestDriver port [-v] [-t]
		 port: Port to listen on
		   -v: Verbose logging
                   -t: Enable timestamps in logs
```

TestDriver will inialize a client and server and send an array of test strings to the server over localhost on the given port.

### ServerDriver
```
Usage: java ServerDriver port [-v] [-t] [-time=timeout]
	         port: Port to listen on 
	           -v: Verbose logging
		   -t: Enable timestamps in logs
	-time=timeout: Enable a timeout of "timeout" milliseconds of no input before the socket is closed. Defualts to no timeout.
```
The server object implements Thread and functions properly in my cursory testing of having a multithreaded driver, but this is yet unimplemented.
### ClientDriver
```
Usage: java ClientDriver address port [-v] [-t]
	      address: IP Address to connect to
		 port: Port to connect to
		   -v: Verbose logging
		   -t: Enable timestamps in logs
```

The client will continually prompt the user for a string to check.
Entering 'END' will signal the Client to send a delim string to the server (also 'END'). The server recognizes this and closes the socket.
