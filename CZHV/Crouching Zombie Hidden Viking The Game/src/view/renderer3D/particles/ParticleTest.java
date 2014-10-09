package view.renderer3D.particles;

import org.lwjgl.util.vector.Vector3f;

import view.renderer3D.core.Drawable3D;
import view.renderer3D.core.ShaderObject;

public class ParticleTest {
	Emitter test;
	
	public ParticleTest() {
		test = new DirectionalEmitter(1000, 10, 50, 50, new Drawable3D[] {new TriangleParticle(0.01f)}, new Vector3f(1,0.1f,1), new Vector3f(0,0.001f,0), new Vector3f(0,0.00001f,0), 0.8f, 0.05f);
	}
	
	public void update(ShaderObject shader) {
		test.emit();
		test.updateDraw(shader);
	}
}
