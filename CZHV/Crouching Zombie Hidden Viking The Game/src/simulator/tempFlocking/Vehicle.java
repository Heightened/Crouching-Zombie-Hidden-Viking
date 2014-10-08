package simulator.tempFlocking;
import java.util.Iterator;
import java.util.LinkedList;

import model.character.GameCharacter;
import model.map.Cell;
import model.map.ChunkedMap;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.Dummy3DObj;
import view.renderer3D.core.Renderer3D;


public class Vehicle extends Dummy3DObj{
	Vector2f steering;
	Vector2f velocity;
	Vector2f targetVelocity;
	final float max_speed = 2f;//final for performance
	final float max_force = 0.15f;
	final float mass = 10;
	Vector4f prevPosition;
	private Vector4f target;
	private float targetRadius;
	
	int gridx = 0;
	int gridy = 0;

	public Vehicle(){
		prevPosition = new Vector4f(position);
		steering = new Vector2f(0,0);
		targetVelocity = new Vector2f(0,0);
		velocity = new Vector2f(0,0);
		this.target = prevPosition;
		targetRadius = Renderer3D.cellSize*10;
	}
	
	public void setFlockingTargetCell(Cell c){
		float scaling = Renderer3D.cellSize;
		float tX = ((float) c.getX() - 0.5f) * scaling;
		float tZ = ((float) c.getY() - 0.5f) * scaling;
		this.target = new Vector4f(tX, 0, tZ, 1);
		this.targetRadius = c.getSpaceRadius() * scaling;
	}
	
	public void setFlockingTargetRadius(float r){
		this.targetRadius = r * Renderer3D.cellSize;
	}

	//float targetSlowRadius = 100;
	public void update(ChunkedMap map, int gridx, int gridy){

		steering.x = 0;
		steering.y = 0;
		for (int x = gridx - 1; x < gridx+2; x++ ){
			for (int y = gridy - 1; y < gridy+2; y++ ){
				Iterator<GameCharacter> iter = map.getCharacters(x, y).iterator();
				while(iter.hasNext()){
					Vehicle v = iter.next();
					if (v != this){
						//System.out.println("NEIGHBOUR " + v.position + " " + position);
						Vector2f vec = fleeTarget(v.position, Renderer3D.cellSize*2);//all performance issues here
						/*
Ik heb in simulator speed x10 gedaan (door ms te delen too 100 ipv 1000), en toen kreeg ik dit:
Exception in thread "Thread-11" java.lang.NullPointerException
	at simulator.tempFlocking.Vehicle.update(Vehicle.java:48)
	at simulator.tempFlocking.FlockingManager.loop(FlockingManager.java:36)
	at simulator.Simulator.run(Simulator.java:49)
						 */
						steering.x += vec.x*2f;
						steering.y += vec.y*2f;
					}
				}
			}
		}
		
		
		//addSteeringForce( fleeTarget(new Vector4f(1,0,1,1), 0.3f), 5);
		addSteeringForce( seekTarget(this.target, targetRadius), 3);

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
		
	//	position.x += velocity.x;
	//	position.z += velocity.y;
	//	position.x %= FlockingManager.screenSize.x;
	//	position.z %= FlockingManager.screenSize.y;
	//	if (position.x < 0){
	//		position.x += FlockingManager.screenSize.x;
	//	}
	//	if (position.z < 0){
	//		position.z += FlockingManager.screenSize.y;
	//	}
	}
	
	public Vector2f getVelocity(){
		return velocity;
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
