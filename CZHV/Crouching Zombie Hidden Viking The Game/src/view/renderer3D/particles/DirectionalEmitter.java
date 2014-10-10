package view.renderer3D.particles;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.Drawable3D;

public class DirectionalEmitter extends Emitter {
	Vector4f location4f, direction;
	Vector3f location3f, force;
	float spread, rotation;

	int lookupSize = 1000;
	//Matrix4f[] modelMatrixLookup = new Matrix4f[lookupSize];
	//Matrix4f[] translationSpeedLookup = new Matrix4f[lookupSize];
	Vector3f[] translationSpeedLookup = new Vector3f[lookupSize];
	Matrix4f[] transformationSpeedLookup = new Matrix4f[lookupSize];

	public DirectionalEmitter(
			int maxParticles, int emissionRate, int lifespanMinimum, int lifespanVariance, Drawable3D[] types, 
			Vector3f location, Vector3f direction, Vector3f force, float spread, float rotation) {
		super(maxParticles, emissionRate, lifespanMinimum, lifespanVariance, types);
		
		this.location4f = new Vector4f(location.x, location.y, location.z, 1);//location;
		this.location3f = location;
		this.direction = new Vector4f(direction.x, direction.y, direction.z, 0);//direction;
		this.force = force;//new Vector4f(force.x, force.y, force.z, 1);
		this.spread = spread;
		
		//Populate matrix lookup tables
		Vector3f upVector3f = new Vector3f(0,1,0);
		Vector4f upVector4f = new Vector4f(0,1,0,0);
		
		for (int i = 0; i < lookupSize; i++) {
			//GENERATE modelMatrixLookup

			//generate initial rotation
//			modelMatrixLookup[i] = new Matrix4f();
			
			//GENERATE translationSpeedLookup
			
			//generate random values
			float theta = (float) ((Math.random() * 2 - 1) * spread * Math.PI); // [-PI * spread, PI * spread]
			float phi = (float) ((Math.random() * 2 - 1) * 1/2 * Math.PI); // [-PI * 1/2, PI * 1/2]
			
			//generate corresponding rotationMatrix
			Matrix4f rotationMatrix = new Matrix4f();
			rotationMatrix.rotate(theta, upVector3f);
			Vector3f axis = new Vector3f((float) Math.cos(theta), (float) Math.sin(theta), 0.0f);
			rotationMatrix.rotate(phi, axis);
			
			//transform direction vector
			Vector4f directionMod = new Vector4f();
			Matrix4f.transform(rotationMatrix, this.direction, directionMod);
			
			//generate randomly modified translation
			translationSpeedLookup[i] = new Vector3f(directionMod.x, directionMod.y, directionMod.z);
			
			//GENERATE transformationSpeedLookup
			
			//generate random values
			theta = (float) ((Math.random() * 2 - 1) * rotation * Math.PI); // [-PI * rotation, PI * rotation]
			phi = (float) ((Math.random() * 2 - 1) * 1/2 * rotation * Math.PI); // [-PI * 1/2 * rotation, PI * 1/2 * rotation]
			
			//generate corresponding rotationMatrix
			rotationMatrix = new Matrix4f();
			rotationMatrix.rotate(theta, upVector3f);
			axis = new Vector3f((float) Math.cos(theta), (float) Math.sin(theta), 0.0f);
			rotationMatrix.rotate(phi, axis);
			
			//generate random rotation
			transformationSpeedLookup[i] = rotationMatrix;//new Matrix4f();
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
			particles.add(new Particle(location3f, translationSpeedLookup[tls], transformationSpeedLookup[tfs], force, types[t], lifespan));
		}
	}
}
