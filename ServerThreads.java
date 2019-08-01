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
    private final Integer RAID_LOCATION = 0x00;
    private final Integer SEND_RAID_LOCATION = 0x01;
    private final Integer REQUEST_RAID_LOCATION = 0X02;
    
    private final Integer RAID_LOCATION_UPDATE = 0x10;
    
    private final Integer RAIDER_UPDATE = 0x20;
    private final Integer SEND_RAIDER_UPDATE = 0x21;
    private final Integer REQUEST_RAIDER_UPDATE = 0x22;
    
    
    private final Integer USERNAME_UPDATE = 0x30;
    private final Integer MESSAGE = 0x40;
    private final Integer SEND_MESSAGE = 0x41;
    private final Integer REQUEST_MESSAGE = 0x42;
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
	    sc.useDelimiter("\n");
	}catch (Exception e){
	    System.out.println(e.toString());
	    return;
	}
	
	try{
	    while(sc.hasNext()== false){}
	    userCode = sc.nextLong();
	    int code = sc.nextInt();
	    System.out.println("connection");
	    if(code == SEND_RAID_LOCATION){//user sending raid location
		int time = sc.nextInt();
		String name = sc.next();
		Double lattitude = sc.nextDouble();
		Double longitude = sc.nextDouble();
		int id = raid.addGym(name, longitude, lattitude);
		System.out.println(raid.count());
		System.out.println(longitude.toString());
		System.out.println(lattitude.toString());
		String temp;
		temp =  RAID_LOCATION.toString() + "\n"+ Integer.toString(id).toString() + "\n" + name + "\n" + lattitude.toString() +"\n"+ longitude.toString() + "\n";
		
		os.write(temp.getBytes());
	    }
	    //user requesting status of raiders
	    else if(code == SEND_RAIDER_UPDATE){
		String[] raids = raid.getActive();
		os.write(RAIDER_UPDATE.toString().getBytes());
		for(int i = 0; i < raids.length; i++){
		    os.write(raids[i].getBytes());
		}
	    }
	    else if(code == REQUEST_RAID_LOCATION){//user requesting location of all the raids
		String[] locations = raid.getLocations();
		os.write(RAID_LOCATION.toString().getBytes());
		for(int i = 0; i < locations.length; i++){
		    os.write(locations[i].getBytes());
		    System.out.println(locations[i]);
		    System.out.println("");
		}
		
	    }
	    else if(code == 10000){//user sending there status to the server
		int id = sc.nextInt();
		int status = sc.nextInt();
		if(sc.hasNext() == true){
		    int time  = sc.nextInt();
		    int level = sc.nextInt();
		    String type = sc.next();
		    raid.setRaidTime(id, time);
		    raid.setRaidLevel(id, level);
		    raid.setRaidType(id, type);
		}
		
		raid.userStatus(id, code, status);
		
	    }


	    else if(code == USERNAME_UPDATE){
		raid.updateUser(userCode);
	    }
	    else if(code == SEND_MESSAGE){
		String name = sc.next();
		int raidId = sc.nextInt();
		while(sc.hasNext() == true){
		    String content = sc.next();
		    raid.addMessage(name,raidId, content);
		}
	    }
	    else if(code == REQUEST_MESSAGE){}
	    socket.close();
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
