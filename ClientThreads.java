import java.io.*;
import java.net.*;

public class ClientThreads {
    
    public static void main(String[] args) {
	
	if (args.length != 2) {
	    System.out.println("Usage:  client <host> <port>");
	    return;
	}
	
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
	
	// establish socket connection to server
	Socket socket;
	try {
	    socket = new Socket(hostName, portNumber);
	} catch (UnknownHostException e) {
	    System.out.println("Couldn't establish socket connection");
	    return;
	} catch (IOException e) {
	    System.out.println("IO exception on attempting to connect socket");
	    return;
	}
	
	InputStream is;
	try {
	    is = socket.getInputStream();
	    byte[] buffer = new byte[50];
	    int n;
	    while (true){
		n = is.read(buffer);
		System.out.println(new String (buffer, 0, n));
		if((new String(buffer, 0, n)).compareTo("close") == 0)
		    break;
		
	    }

	    is.close();
	} catch (IOException e) {
	    System.out.println("IO exception trying to read from socket");
	    return;
	}
    }
}
