import javax.swing.JFrame;

import org.omg.CORBA.FREE_MEM;


public class Vehicle {
	Vector2f steering;
	Vector2f velocity;
	Vector2f targetVelocity;
	final float max_speed = 5;//final for performance
	final float max_force = 1;
	final float mass = 6;
	Vector2f position;
	Vector2f target;
	
	int gridx = 0;
	int gridy = 0;

	public Vehicle(float posx, float posy, Vector2f target){
		position = new Vector2f(posx, posy);
		steering = new Vector2f(0,0);
		this.target = target;
		targetVelocity = new Vector2f(0,0);
		velocity = new Vector2f(0,0);
	}

	float targetSlowRadius = 100;
	Vector2f center = new Vector2f(250,250);
	public void update(FlockingMain main, Grid grid){
		gridx = (int)(position.x/FlockingMain.GRID_CELL_SIZE);
		gridy = (int)(position.y/FlockingMain.GRID_CELL_SIZE);

		steering.x = 0;
		steering.y = 0;
		for (int x = gridx - 1; x < gridx+2; x++ ){
			for (int y = gridy - 1; y < gridy+2; y++ ){
				Vehicle[] tempv = grid.getArray(x, y);
				int size = grid.getSize(x, y);
				for (int i = 0; i < size; i++){
					Vehicle v = tempv[i];
					if (v != this){
						Vector2f vec = fleeTarget(v.position, 30);//all performance issues here
						steering.x += vec.x*4f;
						steering.y += vec.y*4f;
					}
				}
			}
		}
		addSteeringForce( seekTarget(target, 100), 5);
	    addSteeringForce( fleeTarget(center, targetSlowRadius), 10);

		steering.truncate(max_force);
		steering.scale(1/mass);

		velocity.x += steering.x;
		velocity.y += steering.y;

		velocity.truncate(max_speed);

		position.x += velocity.x;
		position.y += velocity.y;
		position.x %= FlockingMain.screenSize.width;
		position.y %= FlockingMain.screenSize.height;
		if (position.x < 0){
			position.x += FlockingMain.screenSize.width;
		}
		if (position.y < 0){
			position.y += FlockingMain.screenSize.height;
		}
	}
	
	public void clearSteeringForce(){
		steering.x = 0;
		steering.y = 0;
	}
	
	public void addSteeringForce(Vector2f force, float weight){
		steering.x += force.x*weight;
		steering.y += force.y*weight;
	}

	public Vector2f getFuturePosition(float time){
		return new Vector2f(position.x + velocity.x*time, position.y + velocity.y*time);
	}

	Vector2f temp = new Vector2f(0,0);
	public Vector2f pursuit(Vehicle target){
		temp.x = target.position.x - position.x;
		temp.y = target.position.y - position.y;
		float distance = temp.getLength();
		float temp2 = distance/max_speed;
		return seekTarget(target.getFuturePosition(temp2), 0);
	}
	
	public Vector2f evade(Vehicle target, float radius){
		temp.x = target.position.x - position.x;
		temp.y = target.position.y - position.y;
		float distance = temp.getLength();
		float temp2 = distance/max_speed/8;
		return fleeTarget(target.getFuturePosition(temp2), radius);
	}

	Vector2f tempsteering = new Vector2f(0,0);
	public Vector2f seekTarget(Vector2f target, float slowRadius){
		targetVelocity.x = target.x - position.x;
		targetVelocity.y = target.y - position.y;

		float distance = targetVelocity.getLength();

		if (distance < slowRadius){
			targetVelocity.normalize();
			targetVelocity.scale(max_speed);
			targetVelocity.scale(distance/slowRadius);
		}else{
			targetVelocity.normalize();
			targetVelocity.scale(max_speed);
		}

		tempsteering.x = targetVelocity.x - velocity.x;
		tempsteering.y = targetVelocity.y - velocity.y;
		return tempsteering;
	}

	public Vector2f correction = new Vector2f(0,0);
	//OPTIMIZED AND UGLY
	public Vector2f fleeTarget(Vector2f target, float fleeRadius){
		float targetvx = target.x - position.x;//local float faster than shared vertex
		float targetvy = target.y - position.y;
		
		//compute length of targetVelocity
		float number = targetvx*targetvx + targetvy*targetvy;//faster than math.pow
		//magic starts here
		float xhalf = 0.5f*number;
	    int i = Float.floatToIntBits(number);
	    i = 0x5f3759df - (i>>1);
	    number = Float.intBitsToFloat(i);
	    number = number*(1.5f - xhalf*number*number);
	    float distance = 1/number;//faster than math.sqrt
	    //end compute length

	    float factor = fleeRadius - distance;
	    
	    targetvx = targetvx/distance*factor;
	    targetvy = targetvy/distance*factor;
	    
	    
		if (distance < fleeRadius){
			targetvx/=factor;
			targetvy/=factor;
			targetvx*= max_speed;
			targetvy*= max_speed;
			targetvx*=factor/fleeRadius;
			targetvy*=factor/fleeRadius;
			targetvx*= max_speed;
			targetvy*= max_speed;
			targetvx*=-1;
			targetvy*=-1;
		}else{
			targetvx = velocity.x;
			targetvy = velocity.y;
		}

		tempsteering.x = targetvx - velocity.x;
		tempsteering.y = targetvy - velocity.y;
		return tempsteering;
	}
}
