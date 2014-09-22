package view.renderer3D.core;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Vector4f;

public class ShaderObject {
	private int shaderID;
	private int vertexID;
	private int fragmentID;
	
	private StringBuilder vertexSource;
	private StringBuilder fragmentSource;
	
	private String name;
	
	private boolean bound = false;
	
	public ShaderObject(String name){
		this.name = name;
		shaderID = GL20.glCreateProgram();
		vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		vertexSource = new StringBuilder();
		fragmentSource = new StringBuilder();
	}
	
	public void addVertexSource(String s){
		vertexSource.append(s).append('\n');
	}
	
	public void addFragmentSource(String s){
		fragmentSource.append(s).append('\n');
	}
	
	public void addVertexSource(ArrayList<String> s){
		for (String line : s){
			addVertexSource(line);
		}
	}
	
	public void addFragmentSource(ArrayList<String> s){
		for (String line : s){
			addFragmentSource(line);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void compileVertex(){
		if (vertexSource.length() == 0){
			System.out.println("COMPILING VERTEX WITHOUT SOURCE FOR " + name);
			System.exit(1);
		}
		GL20.glShaderSource(vertexID,  vertexSource);
        GL20.glCompileShader(vertexID);
        if (GL20.glGetShader(vertexID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println(name + " VERT wasn't able to be compiled correctly.");
            printLogInfo(vertexID);
        }
	}
	
	@SuppressWarnings("deprecation")
	public void compileFragment(){
		if (fragmentSource.length() == 0){
			System.out.println("COMPILING FRAGMENT WITHOUT SOURCE FOR " + name);
			System.exit(1);
		}
		GL20.glShaderSource(fragmentID,  fragmentSource);
        GL20.glCompileShader(fragmentID);
        if (GL20.glGetShader(fragmentID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println(name + " FRAG wasn't able to be compiled correctly.");
            printLogInfo(fragmentID);
        }
	}
	
	public boolean link(){
		boolean retBool = true;
        GL20.glAttachShader(shaderID, vertexID);
        retBool = retBool && !TOOLBOX.checkGLERROR(true);
        GL20.glAttachShader(shaderID, fragmentID);
        retBool = retBool && !TOOLBOX.checkGLERROR(true);
        GL20.glLinkProgram(shaderID);
        retBool = retBool && !TOOLBOX.checkGLERROR(true);
        GL20.glValidateProgram(shaderID);
        return retBool && !TOOLBOX.checkGLERROR(true);
	}
	
	
	private HashMap<String,Integer> uniformLocations;
	private HashMap<String,Integer> samplerLocations;
	private HashMap<String,Integer> uniformBlockLocations;
	private HashMap<String,Integer> uniformBlockBindingPoints;
	@SuppressWarnings("deprecation")
	public void findUniforms(){
		classInv();
		TOOLBOX.checkGLERROR(true);
		int textureCounter = 0;
		int blockCounter = 0;
		int numUnifs = GL20.glGetProgrami(shaderID, GL20.GL_ACTIVE_UNIFORMS);
		int numUnifBlocks = GL20.glGetProgrami(shaderID, GL31.GL_ACTIVE_UNIFORM_BLOCKS);
		TOOLBOX.checkGLERROR(true);
		uniformLocations = new HashMap<String, Integer>(numUnifs);
		samplerLocations = new HashMap<String, Integer>(numUnifs);
		uniformBlockLocations = new HashMap<String, Integer>(numUnifBlocks);
		uniformBlockBindingPoints = new HashMap<String, Integer>(numUnifBlocks);
		for (int i = 0; i < numUnifs; i++){
			String name = GL20.glGetActiveUniform(shaderID, i, 100);
			int type = GL20.glGetActiveUniformType(shaderID, i);
			int location = GL20.glGetUniformLocation(shaderID, name);
			uniformLocations.put(name, location);
			//link GL_TEXTURE0-X to proper texture
			if (type == GL20.GL_SAMPLER_2D){
				samplerLocations.put(name, textureCounter);
				GL20.glUniform1i(location, textureCounter);
				textureCounter++;
			}
		}
		for (int i = 0; i < numUnifBlocks; i++){
			String name = GL31.glGetActiveUniformBlockName(shaderID, i, 100);
			int blockIndex = GL31.glGetUniformBlockIndex(shaderID, name);
			uniformBlockLocations.put(name, blockIndex);
			uniformBlockBindingPoints.put(name, blockCounter);
	        GL31.glUniformBlockBinding(shaderID, blockIndex, blockCounter);
	        blockCounter++;
		}
		TOOLBOX.checkGLERROR(true);
	}
	

	private HashMap<String,Integer> vertexAttrLocations;
	public void findAttributes(){
		TOOLBOX.checkGLERROR(true);
		int numAttr = GL20.glGetProgrami(shaderID, GL20.GL_ACTIVE_ATTRIBUTES);
		vertexAttrLocations = new HashMap<String, Integer>(numAttr);
		for (int i = 0; i < numAttr; i++){
			String name = GL20.glGetActiveAttrib(shaderID, i, 100);
			int type = GL20.glGetActiveAttribType(shaderID, i);
			int loc = GL20.glGetAttribLocation(shaderID, name);
			vertexAttrLocations.put(name, loc);
			GL20.glBindAttribLocation(shaderID, loc, name);
		}
		TOOLBOX.checkGLERROR(true);
	}
	
	public Integer getAttrLocation(String name){
		return vertexAttrLocations.get(name);
	}
	
	public int getUnifBlockBindingPoint(String name){
		return uniformBlockBindingPoints.get(name);
	}
	public int getUnifBlockLocation(String name){
		return uniformBlockLocations.get(name);
	}
	
	public void putUnifFloat(String name, float value){
		GL20.glUniform1f(uniformLocations.get(name), value);
	}
	
	public void putUnifFloat4(String name, Vector4f value){
		Integer loc = uniformLocations.get(name);
		if (loc == null){
			return;
		}
		GL20.glUniform4f(loc, value.x, value.y, value.z, value.w);
	}
	
	public void putUnifFloat4(String name, float x, float y, float z, float w){
		GL20.glUniform4f(uniformLocations.get(name), x, y, z, w);
	}
	
	public void putMat4(String name, FloatBuffer value){
		GL20.glUniformMatrix4(uniformLocations.get(name), false, value);
	}
	
	public void bindTexture(String texName, TextureObject tex){
		int activeNum = samplerLocations.get(texName);
		tex.bind(activeNum);
	}
	
	public void bind(){
		GL20.glUseProgram(shaderID);
		bound = true;
	}
	
	public void unbind(){
		GL20.glUseProgram(0);
		bound = false;
	}

	private void classInv(){
		if (!bound){
			System.err.println("OPERATING ON " + this + " WHILE NOT BOUND");
			TOOLBOX.printStackTraceFromHere();
			System.exit(0);
		}
	}
	
	public void delete(){
		GL20.glDeleteProgram(shaderID);
		GL20.glDeleteShader(vertexID);
		GL20.glDeleteShader(fragmentID);
	}
	
	public boolean printLogInfo(int obj){
        IntBuffer iVal = BufferUtils.createIntBuffer(1);
        ARBShaderObjects.glGetObjectParameterARB(obj,ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);
        
        int length = iVal.get();
        if (length > 1) {
            // We have some info we need to output.
            ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
            iVal.flip();
            ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
            byte[] infoBytes = new byte[length];
            infoLog.get(infoBytes);
            String out = new String(infoBytes);
			String[] lines = out.split("\n");
			for (String s : lines){
				System.err.println(s);
			}
        }
        else return true;
        return false;
    }
}
