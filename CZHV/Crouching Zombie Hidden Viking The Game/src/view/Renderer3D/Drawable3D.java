package view.renderer3D;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Drawable3D {
	FloatBuffer vertices; //position, normal, uv, tangent, color
	
	public void initialize(int size) {
		vertices = BufferUtils.createFloatBuffer(16 * size);
	}
}