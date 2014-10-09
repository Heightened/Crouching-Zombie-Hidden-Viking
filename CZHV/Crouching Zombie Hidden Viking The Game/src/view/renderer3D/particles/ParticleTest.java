package view.renderer3D.particles;

import org.lwjgl.util.vector.Vector3f;

import view.renderer3D.core.Drawable3D;
import view.renderer3D.core.ShaderObject;

public class ParticleTest {
	Emitter particletest;
	
	public ParticleTest() {
		particletest = new DirectionalEmitter(1000, 10, 100, 100, new Drawable3D[] {new TriangleParticle(0.01f)}, new Vector3f(1,0,1), new Vector3f(0.01f,0.01f,0), new Vector3f(0,-0.00005f,0), 0.3f);
	}
	
	public void update(ShaderObject shader) {
		particletest.emit();
		particletest.updateDraw(shader);
	}
}
