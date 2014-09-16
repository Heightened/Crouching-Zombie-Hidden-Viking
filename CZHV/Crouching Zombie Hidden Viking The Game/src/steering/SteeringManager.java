package steering;


public class SteeringManager {
	private Vector steer;
	private Steerable host;
	
	public SteeringManager(Steerable host){
		setHost(host);
		reset();
	}
	
	
	public void seek(Vector targetPos, int slowRadius){
		//steer+seek;
	}
	
	public void flee(Vector targetPos){
		//steer+flee();
	}
	
	public void wander(){
		//steer+wander();
	}
	
	public void evade(Vector targetPos){
		//steer+evade();
	}
	
	public void pursuit(Vector targetPos){
		//steer+pursuit();
	}
	
	public void update(){
		host.setSteer(steer);
	}
	
	public void reset(){
		this.setSteer(new Vector(0,0,0));
	}
	
	public Steerable getHost() {
		return host;
	}

	public void setHost(Steerable host) {
		this.host = host;
	}

	public Vector getSteer() {
		return steer;
	}

	public void setSteer(Vector steer) {
		this.steer = steer;
	}
	
	
}
