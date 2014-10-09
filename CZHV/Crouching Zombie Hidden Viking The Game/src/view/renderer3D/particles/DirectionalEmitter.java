package view.renderer3D.particles;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.Drawable3D;

public class DirectionalEmitter extends Emitter {
	Vector4f location, direction;
	Vector3f force;
	float spread;

	int lookupSize = 1000;
	//Matrix4f[] modelMatrixLookup = new Matrix4f[lookupSize];
	Matrix4f[] translationSpeedLookup = new Matrix4f[lookupSize];
	Matrix4f[] transformationSpeedLookup = new Matrix4f[lookupSize];
	Matrix4f baseModelMatrix = new Matrix4f();

	public DirectionalEmitter(
	int maxParticles, int emissionRate, int lifespanMinimum, int lifespanVariance, Drawable3D[] types, 
	Vector3f location, Vector3f direction, Vector3f force, float spread) {
		super(maxParticles, emissionRate, lifespanMinimum, lifespanVariance, types);
		
		this.location = new Vector4f(location.x, location.y, location.z, 1);//location;
		this.direction = new Vector4f(direction.x, direction.y, direction.z, 0);//direction;
		this.force = force;//new Vector4f(force.x, force.y, force.z, 1);
		this.spread = spread;
		
		baseModelMatrix.translate(location);
		
		//Populate matrix lookup tables
		Vector3f upVector3f = new Vector3f(0,1,0);
		
		for (int i = 0; i < lookupSize; i++) {
			//GENERATE modelMatrixLookup

			//generate initial rotation
//			modelMatrixLookup[i] = new Matrix4f();
			
			//GENERATE translationSpeedLookup
			
			//modify pitch randomly to account for spread
			float angle = Vector3f.angle(upVector3f, direction);			
			angle += ((Math.random() * 2 - 1) * spread * Math.PI);
			
			//modify yaw randomly to account for spread
			Vector3f axis = new Vector3f();
			Vector3f.cross(upVector3f, direction, axis);
			Matrix4f rotationMatrix = new Matrix4f();
			rotationMatrix.rotate((float) ((Math.random() * 2 - 1) * spread * Math.PI), upVector3f);
			Vector4f axis4f = new Vector4f(axis.x, axis.y, axis.z, 0);
			Matrix4f.transform(rotationMatrix, axis4f, axis4f);
			axis = new Vector3f(axis4f.x, axis4f.y, axis4f.z);
			
			//modify and apply direction vector
			rotationMatrix = new Matrix4f();
			rotationMatrix.rotate(angle, axis);
			Vector4f directionMod = new Vector4f();
			Matrix4f.transform(rotationMatrix, this.direction, directionMod);
			translationSpeedLookup[i] = new Matrix4f();
			translationSpeedLookup[i].translate(new Vector3f(directionMod.x, directionMod.y, directionMod.z));
			
			//GENERATE transformationSpeedLookup
			
			//generate random rotation
			transformationSpeedLookup[i] = new Matrix4f();
		}
	}
	
	@Override
	public void emit() {
		for (int i = 0; i < emissionRate; i++) {				
			int m = (int) (Math.random() * lookupSize);
			int tls = (int) (Math.random() * lookupSize);
			int tfs = (int) (Math.random() * lookupSize);
			int t = (int) (Math.random() * types.length);
			int lifespan = lifespanMinimum + (int) (Math.random() * lifespanVariance);
			
			//particles.add(new Particle(transformationSpeedLookup[m], translationSpeedLookup[tls], transformationSpeedLookup[tfs], force, types[t], lifespan));
			particles.add(new Particle(baseModelMatrix, translationSpeedLookup[tls], transformationSpeedLookup[tfs], force, types[t], lifespan));
		}
	}
}
