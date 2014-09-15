package steering;


public class SteeringManager {
	private Vector3D steer;
	private Steerable host;
	
	public SteeringManager(Steerable host){
		setHost(host);
		reset();
	}
	
	
	public void seek(Vector3D targetPos, int slowRadius){
		//steer+seek;
	}
	
	public void flee(Vector3D targetPos){
		//steer+flee();
	}
	
	public void wander(){
		//steer+wander();
	}
	
	public void evade(Vector3D targetPos){
		//steer+evade();
	}
	
	public void pursuit(Vector3D targetPos){
		//steer+pursuit();
	}
	
	public void update(){
		host.setSteer(steer);
	}
	
	public void reset(){
		this.setSteer(new Vector3D(0,0,0));
	}
	
	public Steerable getHost() {
		return host;
	}

	public void setHost(Steerable host) {
		this.host = host;
	}

	public Vector3D getSteer() {
		return steer;
	}

	public void setSteer(Vector3D steer) {
		this.steer = steer;
	}
	
	
}
