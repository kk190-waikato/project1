import java.io.*;

public class RaidList {
    
    private Raid head;
    private User userHead;
    
    public RaidList(){
	userHead = new User(0, null);
	head = new Raid(-1, null, null, 0, 0);
    }
    public synchronized int addGym(String name, Double long_, Double latt_) {
	int id = count();
	Raid new_ = new Raid(id, name, head.getNext(), long_, latt_);
	head.setNext(new_);
	return id;
    }
    public synchronized void addUser(long id, String name){
	User new_ = new User(id, name);
	new_.setNext(userHead.getNext());
	userHead.setNext(new_);
    }
    public synchronized void updateUser(long id_){
	User temp = findUser(id_);
	if(temp == null){
	    addUser(id_, Long.toString(id_));
	}
	else{
	    temp.update();
	}
    }
    public synchronized void setRaidTime(int id, int time){
	Raid temp = find(id);
	temp.setTime(time);
    }
    public synchronized void setRaidLevel(int id, int level){
	Raid temp = find(id);
	temp.setLevel(level);
    }
    public synchronized void setRaidType(int id, String type){
	Raid temp = find(id);
	temp.setType(type);
    }
    public synchronized void setRaidState(int id, int state){
	Raid temp = find(id);
	temp.setState(state);
    }
    
    public synchronized void addMessage(String name, int raidId, String messageConent){
	//TODO
    }
    


    
    public synchronized void deactivate(int i) {
	find(i).deactivate();
    }


    public synchronized void userStatus(int id, long userCode, int status) {
	Raid temp = find(id);
	switch(status){
	case 0:
	    temp.addInterested(userCode);
	    break;
	case 1:
	    temp.addGoing(userCode);
	    break;
	case 2:
	    temp.addThereSoon(userCode);
	    break;
	case 3:
	    temp.addReady(userCode);
	    break;

	}
    }
    
    

    
    public synchronized String[] getLocations(){
	Raid temp = head;
	if(count() == 0){
	    String[] ret = new String[1];
	    ret[0] = "";
	    return ret;
	}
	String[] ret = new String[count()];
	while(temp.getNext() != null){
	    temp = temp.getNext();
	    ret[temp.getId()] = temp.getRaidLocation();
	    
	}
	return ret;
    }
    public synchronized String[] getRaidUpdate(){
	Raid temp = head;
	if(count() == 0){
	    String[] ret = new String[1];
	    ret[0] = "";
	    return ret;
	}
	String[] ret = new String[count()];
	while(temp.getNext() != null){
	    temp = temp.getNext();
	    ret[temp.getId()] = temp.getRaidUpdate();
	}
	return ret;
    }
    public synchronized String[] getRaiderUpdate() {
	Raid temp = head;
	if(count() == 0){
	    String[] ret = new String[1];
	    ret[0] = "";
	    return ret;
	}
	String[] ret = new String[count()];
	while(temp.getNext() != null){
	    temp = temp.getNext();
	    ret[temp.getId()] = temp.getRaiderUpdate();
	    
	}
	return ret;
    }
    
    //list specific functions
    public synchronized int count() {
	Raid temp = head;
	int count = 0;
	while(temp.getNext() != null) {
	    temp = temp.getNext();
	    count++;
	}
	return count;
    }
    private synchronized Raid find(int i) {
	Raid temp = head;
	while (true) {
	    if(temp.getId() == i){
		return temp;
	    }
	    if(temp.getNext() == null){
		return null;
	    }
	    else{
		temp = temp.getNext();
	    }
	}
    }
    private synchronized User findUser(long i){
	User temp = userHead;
	while(true){
	    if(temp.getCode() == i){
                return temp;
            }
            if(temp.getNext() == null){
                return null;
            }
            else{
                temp = temp.getNext();
            }
	    
	}
    }
}

class Raid {
    private Boolean active;
    private int id;
    private int people_interested;
    private int people_going;
    private int level;
    private int time;
    private int state;
    private String type;
    private Boolean hatched;
    private Raid next;
    private String name;
    private Double longitude;
    private Double lattitude;
    private int raidersInterested;
    private int raidersGoing;
    private int raidersThereSoon;
    private int raidersReady;
    private long[] raiderInterested;
    private long[] raiderGoing;
    private long[] raiderThereSoon;
    private long[] raiderReady;
    
    public Raid(int id_, String name_, Raid next_, double long_, double latt_) {
	id = id_;
	name = name_;
	next = next_;
	longitude = long_;
	lattitude = latt_;
	//length of array is 7 becuase with that amount of people the raid is trivial
	raiderInterested = new long[7];
	raiderGoing = new long[7];
	raiderThereSoon = new long[7];
	raiderReady = new long[7];
    }
    
    public void activate(){
	
	active = true;
	
    }
    
    public void deactivate(){
	
	people_interested = 0;
	people_going = 0;
	level = 0;
	type = "";
	hatched = false;
    }
    
    public void addGoing(long userCode){
	if(raidersGoing > 6){
	    raidersGoing++;
	    return;
	}
	raidersGoing++;
	raiderGoing[raidersGoing - 1] = userCode;
    }
    public void addInterested(long userCode){
	if(raidersInterested > 6 ){
	    raidersInterested++;
	    return;
	}
	raidersInterested++;
	raiderInterested[raidersInterested - 1] = userCode;
	
    }
    
    public void addThereSoon(long userCode){
	if(raidersThereSoon > 6){
	    raidersThereSoon++;
	    return;
	}
	raidersThereSoon++;
	raiderThereSoon[raidersThereSoon -1] = userCode;
    }
    public void addReady(long userCode){
	for(int i = 0; i < raiderReady.length; i++){
	    if(raiderReady[i] == 0){}
	    else{
		raiderReady[i] = userCode;
	    }
	}
    }
    public String getRaidLocation(){
	return Integer.toString(id) + "\n" + name + "\n" + lattitude.toString() + "\n" + longitude.toString() + "\n";
    }
    public String getRaiderUpdate(){
	String raiders =  Integer.toString(id) + "\n" + Integer.toString(raidersInterested) + "\n" + Integer.toString(raidersGoing) + "\n" + Integer.toString(raidersThereSoon) + "\n" + Integer.toString(raidersReady) + "\n";
	return raiders;
    }
    
    //this method gets the users that have states in the raid
    public String getRaidUpdate(){
     	String raidInfo = Integer.toString(id) + "\n" + Integer.toString(state) + "\n"; 
	if(state != 0x1){
	    raidInfo += Integer.toString(time) + "\n" + Integer.toString(level)+ "\n"+ type + "\n";
	}
	return raidInfo;
    }



    //getter/setter for private variables
    public int getId() {
	return id;
    }
    public Raid getNext() {
	return next;
    }
    public void setNext(Raid r) {
	next = r;
    }
    public Boolean isActive() {
	return active;
    }

    public void setType(String s) {
	type = s;
    }
    public void setState(int s){
	state =s;
    }
    public String getType() {
	return type;
    }
    public void setLevel(int i) {
	level = i;
    }
    public int getLevel() {
	return level;
    }

    public String getName() {
	return name;
    }
    public void setTime(int i){
	time = i;
    }
}

class User{
    private long code;
    private User next;
    private String name;
    private long lastConnect;
    public User(long code_, String name_){
	code = code_;
	name = name_;
	lastConnect = System.currentTimeMillis();
    }
    public long getCode(){
	return code;
    }
    public void setNext(User next_){
	next = next_;
    }
    public User getNext(){
	return next;
    }
    public long lastActive(){
	return System.currentTimeMillis() - lastConnect;
    }
    public void update(){
	lastConnect = System.currentTimeMillis();
    }
}
