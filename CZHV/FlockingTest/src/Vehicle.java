import javax.swing.JFrame;


public class Vehicle {
	Vector2f steering;
	Vector2f velocity;
	Vector2f targetVelocity;
	float max_speed = 5;
	float max_force = 1;
	float mass = 6;
	Vector2f position;
	Vector2f target;
	
	int gridx = 0;
	int gridy = 0;

	public Vehicle(float posx, float posy){
		position = new Vector2f(posx, posy);
		steering = new Vector2f(0,0);
		target = new Vector2f(0,0);
		targetVelocity = new Vector2f(0,0);
		velocity = new Vector2f(0,0);
	}

	float targetSlowRadius = 100;
	Vector2f center = new Vector2f(250,250);
	public void update(FlockingMain main, Grid grid){
		gridx = (int)(position.x/32);
		gridy = (int)(position.y/32);
		
		target.x = DrawPanel.mousex;
		target.y = DrawPanel.mousey;

		clearSteeringForce();
		for (int x = gridx - 1; x < gridx+2; x++ ){
			for (int y = gridy - 1; y < gridy+2; y++ ){
				Vehicle[] tempv = grid.getArray(x, y);
				int size = grid.getSize(x, y);
				for (int i = 0; i < size; i++){
					Vehicle v = tempv[i];
					if (v != null && v != this){
						addSteeringForce(evade(v, 30),4f);
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
		position.x %= 500;
		position.y %= 500;
		if (position.x < 0){
			position.x += 500;
		}
		if (position.y < 0){
			position.y += 500;
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

	public Vector2f fleeTarget(Vector2f target, float fleeRadius){
		targetVelocity.x = position.x - target.x;
		targetVelocity.y = position.y - target.y;
		
		float distance = targetVelocity.getLength();
		
		Vector2f correction = new Vector2f(targetVelocity.x, targetVelocity.y);
		correction.normalize();
		correction.scale(fleeRadius);
		targetVelocity.x =  targetVelocity.x - correction.x;
		targetVelocity.y = targetVelocity.y - correction.y;

		float factor = targetVelocity.getLength();

		if (distance < fleeRadius){
			targetVelocity.normalize();
			targetVelocity.scale(max_speed);
			targetVelocity.scale(factor/fleeRadius);
			targetVelocity.truncate(max_speed);
			targetVelocity.invert();
		}else{
			targetVelocity.x = velocity.x;
			targetVelocity.y = velocity.y;
		}

		tempsteering.x = targetVelocity.x - velocity.x;
		tempsteering.y = targetVelocity.y - velocity.y;
		return tempsteering;
	}
}
