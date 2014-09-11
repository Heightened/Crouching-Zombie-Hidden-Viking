import javax.swing.JFrame;


public class Vehicle {
	Vector2f steering;
	Vector2f velocity;
	Vector2f targetVelocity;
	float max_speed = 20;
	float max_force = 1;
	float mass = 3;
	Vector2f position;
	Vector2f target;

	public Vehicle(float posx, float posy){
		position = new Vector2f(posx, posy);
		steering = new Vector2f(0,0);
		target = new Vector2f(0,0);
		targetVelocity = new Vector2f(0,0);
		velocity = new Vector2f(0,0);
	}

	float time = 0;
	float targetSlowRadius = 50;
	public void update(FlockingMain main){
		target = new Vector2f(DrawPanel.mousex,DrawPanel.mousey);

		clearSteeringForce();
		for (Vehicle v : main.vlist){
			if (v != this){
				addSteeringForce(evade(v, 15),0.5f);
			}
		}
		addSteeringForce(fleeTarget(new Vector2f(250,250), 50),2f);
		addSteeringForce( seekTarget(target, targetSlowRadius), 1);

		steering.truncate(max_force);
		steering.scale(1/mass);

		velocity.x += steering.x;
		velocity.y += steering.y;

		velocity.truncate(max_speed);

		position.x += velocity.x;
		position.y += velocity.y;
		position.x %= 500;
		position.y %= 500;
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

	public Vector2f pursuit(Vehicle target){
		Vector2f temp = new Vector2f(target.position.x - position.x,target.position.y - position.y);
		float distance = temp.getLength();
		float temp2 = distance/max_speed;
		return seekTarget(target.getFuturePosition(temp2), 0);
	}
	
	public Vector2f evade(Vehicle target, float radius){
		Vector2f temp = new Vector2f(target.position.x - position.x,target.position.y - position.y);
		float distance = temp.getLength();
		float temp2 = distance/max_speed;
		return fleeTarget(target.getFuturePosition(temp2), radius);
	}

	public Vector2f seekTarget(Vector2f target, float slowRadius){
		Vector2f steering = new Vector2f(0,0);
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

		steering.x = targetVelocity.x - velocity.x;
		steering.y = targetVelocity.y - velocity.y;
		return steering;
	}

	public Vector2f fleeTarget(Vector2f target, float fleeRadius){
		Vector2f steering = new Vector2f(0,0);
		targetVelocity.x = target.x - position.x;
		targetVelocity.y = target.y - position.y;

		float distance = targetVelocity.getLength();

		if (distance < fleeRadius){
			targetVelocity.normalize();
			targetVelocity.scale(max_speed);
			targetVelocity.scale(fleeRadius/distance);
			targetVelocity.truncate(max_speed);
			targetVelocity.invert();
		}else{
			targetVelocity.x = 0;
			targetVelocity.y = 0;
		}

		steering.x = targetVelocity.x - velocity.x;
		steering.y = targetVelocity.y - velocity.y;
		return steering;
	}
}
