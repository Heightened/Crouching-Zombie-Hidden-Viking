package view.renderer3D.particles;

import org.lwjgl.util.vector.Vector3f;

import view.renderer3D.core.Drawable3D;

public class ColoredDirectionalEmitter extends DirectionalEmitter {	
	Gradient gradient;
	
	public ColoredDirectionalEmitter(
			int maxParticles, int emissionRate, int lifespanMinimum, int lifespanVariance, Drawable3D[] types, 
			Vector3f location, Vector3f direction, Vector3f force, float spread, float rotation, Gradient gradient) {
		super(maxParticles, emissionRate, lifespanMinimum, lifespanVariance, types, location, direction, force, spread, rotation);
		this.gradient = gradient;
	}

	@Override
	public void emit() {
		for (int i = 0; i < emissionRate; i++) {				
			int m = (int) (Math.random() * lookupSize);
			int tls = (int) (Math.random() * lookupSize);
			int tfs = (int) (Math.random() * lookupSize);
			int t = (int) (Math.random() * types.length);
			int lifespan = lifespanMinimum + (int) (Math.random() * lifespanVariance);
			
			particles.add(new ColoredParticle(location3f, translationSpeedLookup[tls], transformationSpeedLookup[tfs], force, types[t], lifespan, gradient));
		}
	}
}
