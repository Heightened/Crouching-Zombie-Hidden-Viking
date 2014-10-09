package view.renderer3D.particles;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.Drawable3D;
import view.renderer3D.core.MatrixCZHV;
import view.renderer3D.core.ShaderObject;

public abstract class Emitter {
	protected class Particle {
		Matrix4f modelMatrix, translationMatrix, transformationMatrix;
		Matrix4f translationSpeed, transformationSpeed;
		Vector3f force;
		Drawable3D type;
		int lifespan, timeAlive;
		
		FloatBuffer bufferedModelMatrix;
		
		public Particle(Matrix4f modelMatrix, Matrix4f translationSpeed, Matrix4f transformationSpeed, Vector3f force, Drawable3D type, int lifespan) {
			this.modelMatrix = new Matrix4f(modelMatrix);
			this.translationMatrix = new Matrix4f();
			this.transformationMatrix = new Matrix4f(modelMatrix);
			this.translationSpeed = translationSpeed;
			this.transformationSpeed = transformationSpeed;
			this.force = force;
			this.type = type;
			this.lifespan = lifespan;
			
			bufferedModelMatrix = BufferUtils.createFloatBuffer(16);
		}
		
		public boolean isAlive() {
			return (timeAlive < lifespan);
		}
		
		public void update() {
			//Accelerate in force direction
			Matrix4f.translate(force, translationSpeed, translationSpeed);
			//Apply translation
			Matrix4f.mul(translationSpeed, translationMatrix, translationMatrix);
			//Apply rotation
			Matrix4f.mul(transformationSpeed, transformationMatrix, transformationMatrix);
			//Calculate modelMatrix
			Matrix4f.mul(translationMatrix, transformationMatrix, modelMatrix);
			
			MatrixCZHV.MatrixToBuffer(modelMatrix, bufferedModelMatrix);

			timeAlive++;
		}
		
		public void draw(ShaderObject shader) {
			shader.putMat4("modelMatrix", bufferedModelMatrix);
			type.draw(shader);
			//System.out.println("Draw\n" + modelMatrix.toString());
		}
	}

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
		shader.putUnifFloat4("color", new Vector4f(1,0.5f,0.5f,1));
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
