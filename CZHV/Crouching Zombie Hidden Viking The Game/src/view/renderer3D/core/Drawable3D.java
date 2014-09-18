package view.renderer3D.core;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Drawable3D {
	private VBO vbo = new VBO(VBO.STATIC_DRAW);
	
//	public Drawable3D() {
//		
//	}
	
	public void initialize(int size, Float[] vertices, Float[] uvcoords, Float[] normals) {
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(8 * size); //position, normal, uv (, tangent, color)
		
		for (int i = 0; i < size; i++) {
			//buffer vertex position vector
			vertexBuffer.put(vertices[i*3]).put(vertices[i*3 + 1]).put(vertices[i*3 + 2]);
			//buffer vertex texture coordinates
			vertexBuffer.put(uvcoords[i*2]).put(uvcoords[i*2 + 1]);
			//buffer vertex normal vector
			vertexBuffer.put(normals[i*3]).put(normals[i*3 + 1]).put(normals[i*3 + 2]);
		}
		
		vertexBuffer.flip();
		vbo.bind();
		vbo.put(vertexBuffer);
		vbo.unbind();
	}
	
	public void draw() {
		vbo.bind();
		vbo.prepareForDraw();
		vbo.draw();
		vbo.unbind();
	}
}