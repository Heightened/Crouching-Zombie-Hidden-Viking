package view.renderer3D.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBHalfFloatVertex;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VBO {
	public static final int STATIC_DRAW = ARBVertexBufferObject.GL_STATIC_DRAW_ARB;
	public static final int DYNAMIC_DRAW = ARBVertexBufferObject.GL_DYNAMIC_DRAW_ARB;

	private int index;
	private int VAO;
	private boolean bound = false;
	private int type;
	private int vertCount = 0;
	private int stride = 32;
	public VBO(int type) {
		this.type = type;
		index = ARBBufferObject.glGenBuffersARB();
		VAO = GL30.glGenVertexArrays();
	}

	public void bind(){
		if (bound == true){
			System.out.println("BUFFER ALREADY BOUND");
			System.exit(0);
		}
		bound = true;
		GL30.glBindVertexArray(VAO);
	}

	public void unbind(){
		classInv();
		bound = false;
		GL30.glBindVertexArray(0);
	}

	public void put(FloatBuffer buffer) {
		classInv();
		vertCount = buffer.capacity()*4/stride;
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, index);
		ARBBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer, type);
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
	}
	
	public void put(ByteBuffer buffer) {
		classInv();
		vertCount = buffer.capacity() / stride;
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, index);
		ARBBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, buffer, type);
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
	}

	private int currentShader = -1;
	public void prepareForDraw(ShaderObject shader){
		classInv();
		if (shader.getID() == currentShader){
			return;
		}
		currentShader = shader.getID();
		
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, index);
		setAttrPointer(shader,"in_position", 3, GL11.GL_FLOAT, false, 32, 0);
		setAttrPointer(shader,"in_normal", 3, GL11.GL_FLOAT, false, 32, 12);
		setAttrPointer(shader,"in_texcoord", 2, GL11.GL_FLOAT, false, 32, 24);
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
	}
	
	public void prepareForDrawAdvanced(ShaderObject shader, boolean halfFloat) {
		classInv();
		if (shader.getID() == currentShader){
			return;
		}
		currentShader = shader.getID();
		
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, index);
		setAttrPointer(shader,"in_position", 3, GL11.GL_FLOAT, false, 32, 0);
		setAttrPointer(shader,"in_normal", 3, GL30.GL_HALF_FLOAT, false, 32, 12);
		setAttrPointer(shader,"in_tangent", 3, GL30.GL_HALF_FLOAT, false, 32, 18);
		setAttrPointer(shader,"in_texcoord", 2, GL30.GL_HALF_FLOAT, false, 32, 24);
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
	}
	
	public void prepareForDrawAdvanced(ShaderObject shader) {
		classInv();
		if (shader.getID() == currentShader){
			return;
		}
		currentShader = shader.getID();
		
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, index);
		setAttrPointer(shader,"in_position", 3, GL11.GL_FLOAT, false, 32, 0);
		setAttrPointer(shader,"in_normal", 3, GL11.GL_SHORT, true, 32, 12);
		setAttrPointer(shader,"in_tangent", 3, GL11.GL_SHORT, true, 32, 18);
		setAttrPointer(shader,"in_texcoord", 2, GL11.GL_SHORT, true, 32, 24);
		ARBBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
	}
	
	private void setAttrPointer(ShaderObject shader, String name, int size, int type, boolean normalize, int stride, int offset){
		Integer loc = shader.getAttrLocation(name);
		if (loc == null){
			return;
		}
        GL20.glEnableVertexAttribArray(loc);
		GL20.glVertexAttribPointer( loc, size, type, normalize, stride, offset);
	}
	
	public void draw(){
		classInv();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertCount);
	}
	
	public void drawLines(){
		classInv();
        GL11.glDrawArrays(GL11.GL_LINES, 0, vertCount);
	}
	
	public void deAllocate(){
		ARBBufferObject.glDeleteBuffersARB(index);
	}

	public void classInv(){
		if (!bound){
			System.out.println("Operating on unbound buffer " + index);
			TOOLBOX.printStackTraceFromHere();
			System.exit(0);
		}
	}
	
}
