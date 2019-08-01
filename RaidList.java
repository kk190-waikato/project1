import java.io.*;

public class RaidList {
    private Raid head;
    private User userHead;

    public RaidList(){
	userHead = new User(0, null);
	head = new Raid(-1, null, null, 0, 0);
    }
    public int addGym(String name, Double long_, Double latt_) {
	int id = count();
	Raid new_ = new Raid(id, name, head.getNext(), long_, latt_);
	head.setNext(new_);
	return id;
    }
    public void addUser(long id, String name){
	User new_ = new User(id, name);
	new_.setNext(userHead.getNext());
	userHead.setNext(new_);
    }
    public void updateUser(long id_){
	User temp = findUser(id_);
	if(temp == null){
	    addUser(id_, Long.toString(id_));
	}
	else{
	    temp.update();
	}
    }
    public void addMessage(String name, int raidId, String messageConent){
	//TODO
    }
    


    
    public void activate(int id_, int level_) {
	
    }
    
    public void deactivate(int i) {
	find(i).deactivate();
    }


    public Boolean addGoing(int id, long userCode) {
        Raid temp = find(id);
	if(temp == null){
	    return false;
	}
	temp.addGoing(userCode);
	return true;
    }
    public Boolean notGoing(int i) {
	Raid temp = find(i);
	if(temp == null){
	    return false;
	}
	temp.notGoing();
	return true;
    }
    public Boolean addInterest(int i, long userCode){
	Raid temp = find(i);
	if(temp == null){
	    return false;
	}
	temp.addInterested(userCode);
	return true;
    }
    public Boolean notInterest(int i){
	Raid temp = find(i);
	if(temp == null){
	    return false;
	}
	temp.notInterest();
	return false;
    }
    
    public String[] getActive() {
	Raid temp = head;
	if(count() == 0){
	    String[] ret = new String[1];
	    ret[0] = "None\n";
	    return ret;
	}
	String[] ret = new String[count()];
	while(temp.getNext() != null){
	    temp = temp.getNext();
	    if(temp.isActive() == true) {
		ret[temp.getId()] = temp.getRaiderUpdate();
	    }
	}
	return ret;
    }
    
    //list specific functions
    public int count() {
	Raid temp = head;
	int count = 0;
	while(temp.getNext() != null) {
	    temp = temp.getNext();
	    count++;
	}
	return count;
    }
    private Raid find(int i) {
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
    private User findUser(long i){
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
    private String pokemon;
    private Boolean hatched;
    private Raid next;
    private String name;
    private Double longitude;
    private Double lattitude;
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
	pokemon = "";
	hatched = false;
    }
    public void addGoing(long userCode){
	for(int i = 0; i < raiderGoing.length; i++){
	    if(raiderGoing[i] == 0){}
	    else{
		raiderGoing[i] = userCode;
		break;
	    }
	}
    }
    public void addInterested(long userCode){
	for(int i = 0; i < raiderInterested.length; i++){
	    if(raiderInterested[i] == 0){}
	    else{
		raiderInterested[i] = userCode;
	    }
	}
    }
    
    public void addThereSoon(long userCode){
	for(int i = 0; i < raiderThereSoon.length; i++){
	    if(raiderThereSoon[i] == 0){}
	    else{
		raiderThereSoon[i] = userCode;
	    }
	}
    }
    public void addReady(long userCode){
	for(int i = 0; i < raiderReady.length; i++){
	    if(raiderReady[i] == 0){}
	    else{
		raiderReady[i] = userCode;
	    }
	}
    }
    public void notGoing(){people_going--;}
    public void notInterest(){people_interested--;}
    
    //this method turns all the class info a string to send to the user
    public String getRaiderUpdate(){
	String ret = "";
	int ready = 0;
	int interested = 0;
	int thereSoon = 0;
	int going = 0;
	
	
	for(int i = 0; i<raiderReady.length; i++){
	    if(raiderGoing[i] == 0){
		going = i;
		break;
	    }
	}
	for(int i = 0; i<raiderReady.length; i++){
	    if(raiderInterested[i] == 0){
		interested = i;
		break;
	    }
	}
	for(int i = 0; i<raiderReady.length; i++){
	    if(raiderThereSoon[i] == 0){
		thereSoon = i;
		break;
	    }
	}
	for(int i = 0; i<raiderReady.length; i++){
	    if(raiderReady[i] == 0){
		ready = i;
		break;
	    }
	}
	ret = Integer.toString(id) + "\n" + Integer.toString(interested) + "\n" + Integer.toString(going) + "\n" + Integer.toString(thereSoon) + "\n" + Integer.toString(ready) + "\n";
	
	return ret;
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

    public void setPokemon(String s) {
	pokemon = s;
    }
    public String getPokemon() {
	return pokemon;
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
