package view.renderer3D.particles;

import java.util.Arrays;

import view.renderer3D.core.Drawable3D;

public class TriangleParticle extends Drawable3D {
	public TriangleParticle(float scale) {
		int size = 6;
		float[] vertices = new float[] {
			0, scale, 0,
			scale * (float) Math.cos(11/6.0f * Math.PI), scale * (float) Math.sin(7/6.0f * Math.PI), 0,
			scale * (float) Math.cos(7/6.0f * Math.PI), scale * (float) Math.sin(7/6.0f * Math.PI), 0,
			0, scale, 0,
			scale * (float) Math.cos(7/6.0f * Math.PI), scale * (float) Math.sin(7/6.0f * Math.PI), 0,
			scale * (float) Math.cos(11/6.0f * Math.PI), scale * (float) Math.sin(7/6.0f * Math.PI), 0
		};		
		float[] uvcoords = new float[] {
			0.5f, 1,
			0, 1,
			1, 0,
			0.5f, 1,
			1, 0,
			0, 1,
		};
		float[] normals = new float[] {
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			0, -1, 0,
			0, -1, 0,
			0, -1, 0
		};
		float[] tangents = new float[] {
			0, 1, 0,
			0, 1, 0,
			0, 1, 0,
			0, -1, 0,
			0, -1, 0,
			0, -1, 0
		};
		
		initialize(size, vertices, uvcoords, normals, tangents);
	}
}
