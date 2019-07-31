import java.net.*;
import java.io.*;
import java.util.*;
class ServerThreads{
    
    public static void main(String args[]){
	ServerThreads server = new ServerThreads();
	server.launchServer();
    }
    public void launchServer(){
	int portNumber = 33001;
	RaidList raid = new RaidList();
	try{
	    InetAddress host = InetAddress.getLocalHost();
	    String hostName = host.getHostName();
	    System.out.println("Local host is " + host.toString());
	    System.out.println("Local host name is " + hostName);
	}catch(UnknownHostException e){
	    System.out.println("Could not get the local host address");
	    return;
	}
	ServerSocket server;
	try {
	    server = new ServerSocket(portNumber);
	} catch (IOException e) {
	    System.out.println("IO exception trying to create server socket");
	    return;
	}
	Integer i = 0;
	while (true){
	    Socket socket;
	    try {
		socket = server.accept();
		ThreadConnect temp = new ThreadConnect(socket, i.toString(), raid);
		temp.start();
	    } catch (IOException e) {
		System.out.println("IO exception waiting for client request");
		return;
	    }
	i++;
	}
	
    }
    
    
}
class ThreadConnect implements Runnable {
    private Thread t;
    private Socket socket;
    private String threadName;
    private RaidList raid;
    private final int RAID_LOCATION = 1;
    private final int RAIDER_UPDATE = 2;
    private final int RAID_LOCATION_UPDATE = 3;
    private final int RAID_LOCATION_REQUEST = 4;
    ThreadConnect(Socket s, String n, RaidList r){
	socket = s;
	threadName = n;
	raid = r;
	
    }
    public void run(){
	OutputStream os;
	InputStream is;
	Scanner sc;
	long userCode;
	try{
	    os = socket.getOutputStream();
	    is = socket.getInputStream();
	    sc = new Scanner(is);
	}catch (Exception e){
	    System.out.println(e.toString());
	    return;
	}
	
	try{
	    while(sc.hasNext()== false){}
	    userCode = sc.nextInt();
	    int code = sc.nextInt();
	    if(code == 1){
		int time = sc.nextInt();
		Double lattitude = sc.nextDouble();
		Double longitude = sc.nextDouble();
		String name = sc.next();
		int id = raid.addGym(name, longitude, lattitude);
		System.out.println(raid.count());
		System.out.println(longitude.toString());
		System.out.println(lattitude.toString());
		String temp;
		temp =  Integer.toString(id).toString() + "\n" + name + "\n" + lattitude.toString() +"\n"+ longitude.toString() + "\n";
		
		os.write(temp.getBytes());
	    }
	    else if(code == 2){
		String[] raids = raid.getActive();
		for(int i = 0; i < raids.length; i++){
		    os.write(raids[i].getBytes());
		}
		os.write("Done".getBytes());
	    }
	    else if(code == 5){
		raid.updateUser(userCode);
	    }
	}catch (Exception e){
	    System.out.println(e.toString());
		return;
	}
    }
    
    public void start(){
	if (t == null){
	    t = new Thread (this, threadName);
	    t.start();
	}
    }
}
