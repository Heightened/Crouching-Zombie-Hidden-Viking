package view.renderer3D.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Drawable3D {
	private VBO vbo = new VBO(VBO.STATIC_DRAW);
	
//	public Drawable3D() {
//		
//	}
	
	public void initialize(int size, float[] vertices, float[] uvcoords, float[] normals) {
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(8 * size); //position, uv, normal, (, tangent, color)
		//BufferUtils.createByteBuffer(32 * size);
		for (int i = 0; i < size; i++) {
			//buffer vertex position vector
			vertexBuffer.put(vertices[i*3]).put(vertices[i*3 + 1]).put(vertices[i*3 + 2]);
			//buffer vertex normal vector
			vertexBuffer.put(normals[i*3]).put(normals[i*3 + 1]).put(normals[i*3 + 2]);
			//buffer vertex texture coordinates
			vertexBuffer.put(uvcoords[i*2]).put(uvcoords[i*2 + 1]);
		}
		
		vertexBuffer.flip();
		vbo.bind();
		vbo.put(vertexBuffer);
		vbo.unbind();
	}
	
	public void initialize(int size, float[] vertices, float[] uvcoords, float[] normals, float[] tangents) {
		ByteBuffer vertexBuffer = BufferUtils.createByteBuffer(32 * size); //position, uv, normal, tangent (, color)
		for (int i = 0; i < size; i++) {
			//buffer vertex position vector
			vertexBuffer.putFloat(vertices[i*3]).putFloat(vertices[i*3 + 1]).putFloat(vertices[i*3 + 2]);
			//buffer vertex normal vector
			//System.out.println("normal " + normals[i*3+0] + " " + normals[i*3+1] + " " + normals[i*3+2] );
			vertexBuffer.put((new HalfPrecisionFloat(normals[i*3])).bytes);
			vertexBuffer.put((new HalfPrecisionFloat(normals[i*3 + 1])).bytes);
			vertexBuffer.put((new HalfPrecisionFloat(normals[i*3 + 2])).bytes);
			//System.out.println("endnormals");
			//buffer vertex tangent vector
			vertexBuffer.put((new HalfPrecisionFloat(tangents[i*3])).bytes);
			vertexBuffer.put((new HalfPrecisionFloat(tangents[i*3 + 1])).bytes);
			vertexBuffer.put((new HalfPrecisionFloat(tangents[i*3 + 2])).bytes);
			//buffer vertex texture coordinates
			vertexBuffer.put((new HalfPrecisionFloat(uvcoords[i*2])).bytes);
			vertexBuffer.put((new HalfPrecisionFloat(uvcoords[i*2 + 1])).bytes);
			//padding
			vertexBuffer.put((new HalfPrecisionFloat(uvcoords[i*2 + 1])).bytes);
			vertexBuffer.put((new HalfPrecisionFloat(uvcoords[i*2 + 1])).bytes);
		}
		
		vertexBuffer.flip();
		vbo.bind();
		vbo.put(vertexBuffer);
		vbo.unbind();
	}
	
	public void draw(ShaderObject shader) {
		vbo.bind();
		vbo.prepareForDrawAdvanced(shader);
		vbo.draw();
		vbo.unbind();
	}
}