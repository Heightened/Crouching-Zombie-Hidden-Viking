package view.renderer3D.core;

import java.nio.FloatBuffer;

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
import org.lwjgl.util.vector.Vector3f;

import view.renderer3D.inputoutput.FileToString;

public class Renderer3D {
	private Camera camera;
	private VBO testVBO;
	private TextureObject tex;
	private ShaderObject shader;
	public Renderer3D(){
		setupDisplay();
		camera = new Camera(viewMatrix);
		testVBO = new VBO(VBO.STATIC_DRAW);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(8*3);//8 floats per vert, 3 verts
		int scale = 1;
		//vert 1
		buffer.put(-scale).put(1).put(-scale);
		buffer.put(0).put(1).put(0);
		buffer.put(0).put(0);
		//vert 2
		buffer.put(scale).put(1).put(-scale);
		buffer.put(0).put(1).put(0);
		buffer.put(1).put(0);
		//vert 3
		buffer.put(scale).put(1).put(scale);
		buffer.put(0).put(1).put(0);
		buffer.put(1).put(1);
		
		buffer.flip();
		testVBO.bind();
		testVBO.put(buffer);
		testVBO.unbind();
		
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
	}

	long totaltime = 0;
	int framecounter = 0;
	int framedelay = 10;
	float currentTime = 0;
	public void update(){
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
		Matrix4f modelmat = new Matrix4f();
		MatrixCZHV.getModelMatrix(new Vector3f(0,0,0), new Vector3f(1,1,1), new Vector3f(0,currentTime*1000,0), modelmat);
		FloatBuffer b = BufferUtils.createFloatBuffer(16);
		shader.putMat4("modelMatrix", MatrixCZHV.MatrixToBuffer(modelmat, b));
		shader.putMat4("viewMatrix", viewMatrix);
		shader.putMat4("projectionMatrix", projectionMatrix);
		
		
        testVBO.bind();
        testVBO.prepareForDraw(shader);
        testVBO.draw();
        testVBO.unbind();
        
        shader.unbind();

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
	public final void setupDisplay(){
		PixelFormat pixelFormat = new PixelFormat();
		ContextAttribs contextAtrributes = new ContextAttribs(3, 3)
		.withForwardCompatible(true)
		.withProfileCore(true);
		try{
			Display.setDisplayMode(new DisplayMode(1024, 720));
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
		
		Matrix4f projmat = new Matrix4f();
		
		projmat.m00 = x_scale;
		projmat.m11 = y_scale;
		projmat.m22 = -((far_plane + near_plane) / frustum_length);
		projmat.m23 = -1;
		projmat.m32 = -((2 * near_plane * far_plane) / frustum_length);
		projmat.m33 = 0;   

		viewMatrix = BufferUtils.createFloatBuffer(16);
		projectionMatrix = BufferUtils.createFloatBuffer(16);
		projmat.store(projectionMatrix);
		projectionMatrix.flip();
		
		try{
			Mouse.create();
			Keyboard.create();
		} catch (Exception e){
			e.printStackTrace();
		}

        GL11.glEnable(GL11.GL_BLEND);
        
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
