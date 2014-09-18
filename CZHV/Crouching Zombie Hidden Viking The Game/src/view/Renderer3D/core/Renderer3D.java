package view.renderer3D.core;

import java.awt.Dimension;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.inputoutput.FileToString;

public class Renderer3D {
	private Camera camera;
	private VBO quadVBO;
	private TextureObject tex;
	private ShaderObject shader;
	private ShaderObject quadShader;
	private Matrix4f MVP;
	private ArrayList<Dummy3DObj> objList;
	private DEMOselecter selecter;
	public Renderer3D(){
		setupDisplay();
		MVP = new Matrix4f();
		camera = new Camera(viewMatrix, viewMat);
		quadVBO = new VBO(VBO.STATIC_DRAW);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(8*6);//8 floats per vert, 6 verts
		int scale = 1;
		//tri 1
		putVertex(buffer, 0, 0, 0);
		putVertex(buffer, 1, 0, 0);
		putVertex(buffer, 1, 1, 0);
		//tri2
		putVertex(buffer, 0, 0, 0);
		putVertex(buffer, 0, 1, 0);
		putVertex(buffer, 1, 1, 0);
		
		buffer.flip();
		quadVBO.bind();
		quadVBO.put(buffer);
		quadVBO.unbind();
		
		tex = new TextureObject("tex.png");
		tex.setup();
		tex.setMINMAG(GL11.GL_LINEAR);
		tex.setWRAPST(GL11.GL_REPEAT);
		tex.unbind();
		
		shader = new ShaderObject("test shader");
		shader.addVertexSource(FileToString.read("test.vert"));
		shader.addFragmentSource(FileToString.read("test.frag"));
		shader.compileVertex();
		shader.compileFragment();
		shader.link();
		shader.bind();
		shader.findUniforms();
		shader.findAttributes();
		shader.unbind();
		
		quadShader = new ShaderObject("fullscreen quad shader");
		quadShader.addVertexSource(FileToString.read("orthscreenspace.vert"));
		quadShader.addFragmentSource(FileToString.read("orthscreenspace.frag"));
		quadShader.compileVertex();
		quadShader.compileFragment();
		quadShader.link();
		quadShader.bind();
		quadShader.findUniforms();
		quadShader.findAttributes();
		quadShader.unbind();

		objList = new ArrayList<>();
		for (int i = 0; i < 5; i++){
			for (int j = 0; j < 5; j++){
				objList.add(new Dummy3DObj(new Vector4f(1+0.2f*i,0.1f,1+0.2f*j,1)));
			}
		}
		selecter = new DEMOselecter( objList);
	}
	
	public void putVertex(FloatBuffer buffer, float x, float y, float z){
		buffer.put(x).put(y).put(z);
		buffer.put(0).put(1).put(0);
		buffer.put(x).put(z);
	}

	long totaltime = 0;
	int framecounter = 0;
	int framedelay = 10;
	float currentTime = 0;
	Vector4f dummyColor = new Vector4f(0,0,1,1);
	Vector4f selectedColor = new Vector4f(0,0,0.5f,1);
	Vector4f selectboxColor = new Vector4f(0,0,0.5f,0.5f);
	Vector4f floorColor = new Vector4f(1,1,1,1);
	public void update(){
		MVP.setIdentity();
		Matrix4f.mul(projMat, viewMat, MVP);
		
		selecter.update(MVP);
		
		framecounter++;
		if (framecounter == 100){
			framecounter = 0;
			totaltime = System.currentTimeMillis() - totaltime;
			System.out.println("100 frames in " + (totaltime/100 - framedelay) + " ms");
			totaltime = System.currentTimeMillis();
		}
		sleep(framedelay);
		camera.lookThrough();
		
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glDisable(GL11.GL_CULL_FACE);

		
		currentTime += 0.001f;
		
		shader.bind();
		shader.putUnifFloat("time", currentTime);
		shader.bindTexture("texture", tex);
		shader.putMat4("viewMatrix", viewMatrix);
		shader.putMat4("projectionMatrix", projectionMatrix);
		
		shader.putUnifFloat4("color", floorColor);
		
        
        for (Dummy3DObj dummy : objList){
	        if (dummy.isSelected()){
	    		shader.putUnifFloat4("color", selectedColor);
	        }else{
	    		shader.putUnifFloat4("color", dummyColor);
	        }
	        
	        dummy.draw(shader);
        }
        
        shader.unbind();
        
        quadShader.bind();
        
        selecter.draw(quadShader, quadVBO, selectboxColor);;
		
        quadShader.unbind();

		TOOLBOX.checkGLERROR(true);
		Display.update();
	}

	public void sleep(int time){
		try{
			Thread.sleep(time);
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	public FloatBuffer projectionMatrix;
	public FloatBuffer viewMatrix;
	public Matrix4f projMat;
	public Matrix4f viewMat;
	public static final Dimension screenSize = new Dimension(1024, 720);
	public final void setupDisplay(){
		PixelFormat pixelFormat = new PixelFormat();
		ContextAttribs contextAtrributes = new ContextAttribs(3, 3)
		.withForwardCompatible(true)
		.withProfileCore(true);
		try{
			Display.setDisplayMode(new DisplayMode(screenSize.width, screenSize.height));
			Display.create(pixelFormat, contextAtrributes);
		}catch (Exception e){
			e.printStackTrace();
		}

		DisplayMode dm = Display.getDisplayMode();
		float aspect = (float) dm.getWidth() / (float) dm.getHeight();


		float fieldOfView = 70f;
		float aspectRatio = aspect;
		float near_plane = 0.05f;
		float far_plane = 300;

        float y_scale = 1/(float)Math.tan(Math.toRadians(fieldOfView / 2f));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = far_plane - near_plane;
		
		projMat = new Matrix4f();
		viewMat = new Matrix4f();
		
		projMat.m00 = x_scale;
		projMat.m11 = y_scale;
		projMat.m22 = -((far_plane + near_plane) / frustum_length);
		projMat.m23 = -1;
		projMat.m32 = -((2 * near_plane * far_plane) / frustum_length);
		projMat.m33 = 0;    
		
		viewMatrix = BufferUtils.createFloatBuffer(16);
		projectionMatrix = BufferUtils.createFloatBuffer(16);
		
		projMat.store(projectionMatrix);
		projectionMatrix.flip();
		
		try{
			Mouse.create();
			Keyboard.create();
		} catch (Exception e){
			e.printStackTrace();
		}

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc (GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        //enable fixed pipeline bindings for VBO
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        
		TOOLBOX.checkGLERROR(true);
	}

	public static void main(String[] args){
		Renderer3D r3 = new Renderer3D();
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			r3.update();
		}
	}
}
