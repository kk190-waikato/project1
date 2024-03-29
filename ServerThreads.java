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
	RaidChecker raid_ = new RaidChecker(raid, "raidChecker");
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
    private final Integer SEND_RAID_LOCATION_UPDATE = 0x11;
    private final Integer REQUEST_RAID_LOCATION_UPDATE = 0x12;
    
    private final Integer RAIDER_UPDATE = 0x20;
    private final Integer SEND_RAIDER_UPDATE = 0x21;
    private final Integer REQUEST_RAIDER_UPDATE = 0x22;
    
    private final Integer USERNAME_UPDATE = 0x30;
    private final Integer SEND_USERNAME_UPDATE = 0x31;
    private final Integer REQUEST_USERNAME_UPDATE = 0x32;
  
    
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
	    raid.updateUser(userCode);
	    System.out.println("Code: " + code);
	    if(code == SEND_RAID_LOCATION){//user sending raid location
		int time = sc.nextInt();
		String name = sc.next();
		Double lattitude = sc.nextDouble();
		Double longitude = sc.nextDouble();
		int id = raid.addGym(name, longitude, lattitude);
		//System.out.println(raid.count());
		//System.out.println(longitude.toString());
		//System.out.println(lattitude.toString());
		
	    }
	    else if(code == REQUEST_RAID_LOCATION){//user requesting location of all the raids
		String[] locations = raid.getLocations();
		os.write(RAID_LOCATION.toString().getBytes());
		os.write("\n".getBytes());
		for(int i = 0; i < locations.length; i++){
		    os.write(locations[i].getBytes());
		    //System.out.println(locations[i]);
		}
		
	    }
	    else if(code == SEND_RAID_LOCATION_UPDATE) {//user sending information about the raid
		int id = sc.nextInt();
		int status = sc.nextInt();
		
		int time  = sc.nextInt();
		int level = sc.nextInt();
		String type = sc.next();
		raid.setRaidTime(id, time);
		raid.setRaidLevel(id, level);
		raid.setRaidType(id, type);
		raid.setRaidState(id, status);
		    
		
	    }
	    
	    else if(code == REQUEST_RAIDER_UPDATE){//user requesting status from a raid
		String[] status = raid.getRaiderUpdate();
		os.write((RAIDER_UPDATE.toString() + "\n").getBytes());
		for(int i =0; i < status.length; i++){
		    os.write(status[i].getBytes());
		    //System.out.println(status[i]);
		}
	    }
	    else if(code == SEND_RAIDER_UPDATE){
		int id = sc.nextInt();
		int status = sc.nextInt();
		raid.userStatus(id, userCode, status);
		
	    }
	    else if(code == REQUEST_RAID_LOCATION_UPDATE) {//user requesting info about the raid
		String[] status = raid.getRaidUpdate();
		os.write((RAID_LOCATION_UPDATE.toString() + "\n").getBytes());
		for(int i = 0; i < status.length; i++ ){
		    os.write(status[i].getBytes());
		    //System.out.println(status[i]);
		}    
	    }
	    else if(code == SEND_USERNAME_UPDATE){
		String name = sc.next();
		raid.updateUser(userCode, name);
	    }
	    else if(code == SEND_MESSAGE){
		int raidId = sc.nextInt();
	  
		String content = sc.next();
		raid.addMessage(userCode,raidId, content);
		
	    }
	    else if(code == REQUEST_MESSAGE){
		int raidId = sc.nextInt();
		os.write(MESSAGE.toString().getBytes());
		os.write(("\n" + Integer.toString(raidId) + "\n").getBytes());
		os.write(raid.getMessages(raidId).getBytes());
		System.out.println(raid.getMessages(raidId));
	    }


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


class RaidChecker implements Runnable{
    private RaidList raid;
    private Thread t;
    private String threadName;
    public RaidChecker(RaidList raid_, String name_){
	raid = raid_;
	threadName = name_;
    }
    
    public void start(){
	if (t == null){
	    t = new Thread (this, threadName);
	    t.start();
	}
    }
    public void run(){
	try{
	    while(true){
		t.sleep(1000*60);
		raid.clean();
		
	    }
	}
	catch(Exception e){
	    
	}
	
    }
}
