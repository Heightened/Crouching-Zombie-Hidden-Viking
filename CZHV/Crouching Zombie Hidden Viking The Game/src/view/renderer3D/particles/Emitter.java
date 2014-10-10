package view.renderer3D.particles;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import view.renderer3D.core.Drawable3D;
import view.renderer3D.core.ShaderObject;

public abstract class Emitter {
	int maxParticles, emissionRate;
	int lifespanMinimum, lifespanVariance;
	Drawable3D[] types;
	Queue<Particle> particles;
	
	public Emitter(int maxParticles, int emissionRate, int lifespanMinimum, int lifespanVariance, Drawable3D[] types) {
		this.maxParticles = maxParticles;
		this.emissionRate = emissionRate;
		this.lifespanMinimum = lifespanMinimum;
		this.lifespanVariance = lifespanVariance;
		this.types = types;
		particles = new LinkedList<Particle>();
	}
	
	public void draw(ShaderObject shader) {
		Iterator<Particle> iterator = particles.iterator();
		Particle particle;
		while (true) {
			try {
				particle = iterator.next();
				if (particle.isAlive()) {
					particle.draw(shader);
				}
			} catch (NoSuchElementException e) {
				break;
			}
		}
	}
	
	public void updateDraw(ShaderObject shader) {
		//Enforce particle limit		
		for (int i = 0; i < (particles.size() - maxParticles); i++) {
			particles.remove();
		}
		
		//Loop through particles
		//shader.putUnifFloat4("color", new Vector4f(1,0.5f,0.5f,1));
		Iterator<Particle> iterator = particles.iterator();
		Particle particle;
		while (true) {
			try {
				particle = iterator.next();
				if (particle.isAlive()) {
					particle.update();
					particle.draw(shader);
				}
			} catch (NoSuchElementException e) {
				break;
			}
		}
	}
	
	public abstract void emit();
}
