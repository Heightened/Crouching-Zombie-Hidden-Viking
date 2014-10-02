package simulator.tempFlocking;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.Dummy3DObj;


public class Vehicle extends Dummy3DObj{
	Vector2f steering;
	Vector2f velocity;
	Vector2f targetVelocity;
	final float max_speed = 0.7f;//final for performance
	final float max_force = 0.05f;
	final float mass = 10;
	Vector4f prevPosition;
	Vector4f target;
	
	int gridx = 0;
	int gridy = 0;

	public Vehicle(){
		super();
		prevPosition = new Vector4f(position);
		steering = new Vector2f(0,0);
		this.target = target;
		targetVelocity = new Vector2f(0,0);
		velocity = new Vector2f(0,0);
	}

	float targetSlowRadius = 100;
	public void update(FlockingManager main, Grid grid){
		gridx = (int)(position.x/FlockingManager.GRID_CELL_SIZE);
		gridy = (int)(position.z/FlockingManager.GRID_CELL_SIZE);

		steering.x = 0;
		steering.y = 0;
		for (int x = gridx - 1; x < gridx+2; x++ ){
			for (int y = gridy - 1; y < gridy+2; y++ ){
				Vehicle[] tempv = grid.getArray(x, y);
				int size = grid.getSize(x, y);
				for (int i = 0; i < size; i++){
					Vehicle v = tempv[i];
					if (v != this){
						Vector2f vec = fleeTarget(v.position, 0.13f);//all performance issues here
						steering.x += vec.x*1f;
						steering.y += vec.y*1f;
					}
				}
			}
		}
		
		addSteeringForce( fleeTarget(new Vector4f(1,0,1,1), 0.3f), 5);
		addSteeringForce( seekTarget(target, 100), 5);

		truncate(steering, max_force);
		steering.scale(1/mass);

		velocity.x += steering.x;
		velocity.y += steering.y;

		truncate(velocity, max_speed);
		
		float angle = (float)Math.atan2(velocity.x, velocity.y)/(float)Math.PI;
		//System.out.println(angle);
		if (steering.length() > 0.005f){
		//	rotation.y = angle*180;
		}
		
		position.x += velocity.x;
		position.z += velocity.y;
		position.x %= FlockingManager.screenSize.x;
		position.z %= FlockingManager.screenSize.y;
		if (position.x < 0){
			position.x += FlockingManager.screenSize.x;
		}
		if (position.z < 0){
			position.z += FlockingManager.screenSize.y;
		}
	}
	
	public void truncate(Vector2f in, float max){
		float len = in.length();
        if (len > max)
        {
            in.normalise();
            in.scale(max);
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

	public Vector4f getFuturePosition(float time){
		return new Vector4f(position.x + velocity.x*time, position.y, position.z + velocity.y*time, 1);
	}

	Vector2f temp = new Vector2f(0,0);
	public Vector2f pursuit(Vehicle target){
		temp.x = target.position.x - position.x;
		temp.y = target.position.z - position.z;
		float distance = temp.length();
		float temp2 = distance/max_speed;
		return seekTarget(target.getFuturePosition(temp2), 0);
	}
	
	public Vector2f evade(Vehicle target, float radius){
		temp.x = target.position.x - position.x;
		temp.y = target.position.z - position.z;
		float distance = temp.length();
		float temp2 = distance/max_speed/8;
		return fleeTarget(target.getFuturePosition(temp2), radius);
	}

	Vector2f tempsteering = new Vector2f(0,0);
	public Vector2f seekTarget(Vector4f target, float slowRadius){
		targetVelocity.x = target.x - position.x;
		targetVelocity.y = target.z - position.z;

		float distance = targetVelocity.length();

		if (distance < slowRadius){
			targetVelocity.normalise();
			targetVelocity.scale(max_speed);
			targetVelocity.scale(distance/slowRadius);
		}else{
			targetVelocity.normalise();
			targetVelocity.scale(max_speed);
		}

		tempsteering.x = targetVelocity.x - velocity.x;
		tempsteering.y = targetVelocity.y - velocity.y;
		return tempsteering;
	}

	public Vector2f correction = new Vector2f(0,0);
	//OPTIMIZED AND UGLY
	public Vector2f fleeTarget(Vector4f target, float fleeRadius){
		float targetvx = target.x - position.x;//local float faster than shared vertex
		float targetvy = target.z - position.z;
		
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
