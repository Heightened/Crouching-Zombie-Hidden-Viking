package view.renderer3D.core;

import java.awt.Dimension;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import model.Game;

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

import czhv.mainClass;
import view.renderer3D.Model;
import view.renderer3D.core.lighting.LightManager;
import view.renderer3D.core.shadows.ShadowManager;
import view.renderer3D.core.tempFlocking.FlockingManager;
import view.renderer3D.core.tempFlocking.Vehicle;
import view.renderer3D.inputoutput.FileToString;

public class Renderer3D {
	private Camera camera;
	private VBO quadVBO;
	private TextureObject tex;
	private ShaderObject lightShader;
	private ShaderObject quadShader;
	private Matrix4f MVP;
	private ArrayList<Vehicle> objList;
	private DEMOselecter selecter;
	private LightManager lightManager;
	private ShadowManager shadowManager;
	
	private FlockingManager flockingManager;
	private Vector4f flockingTarget;
	private Model quadModel;
	private FloatBuffer modelz;
	private Game game;
	public Renderer3D(Game game){
		setupDisplay();
		this.game = game;
    	shadowManager = new ShadowManager(this);
		lightManager = new LightManager(shadowManager);
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
		tex.mipMap();
		tex.unbind();
		
		lightShader = new ShaderObject("lighting shader");
		lightShader.addVertexSource(FileToString.read("defaultlighting\\defaultlighting.vert"));
		lightShader.addFragmentSource(FileToString.read("defaultlighting\\defaultlighting.frag"));
		lightShader.compileVertex();
		lightShader.compileFragment();
		lightShader.link();
		lightShader.bind();
		lightShader.findUniforms();
		lightShader.findAttributes();
		lightShader.unbind();
		
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
		
		flockingTarget = new Vector4f(0.5f,0,0.5f,1);
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				objList.add(new Vehicle( new Vector4f(0.2f*i,0,0.2f*j,1),flockingTarget, new Vector3f(0,0,0)));
			}
		}
		
		flockingManager = new FlockingManager(objList);
		
		selecter = new DEMOselecter( objList);
		
		quadModel = new Model("quad.obj");
		
		Matrix4f model = new Matrix4f();
    	MatrixCZHV.getModelMatrix(new Vector3f(1,-0.035f,1), new Vector3f(1,1,1), new Vector3f(0,0,0), model);
    	modelz = BufferUtils.createFloatBuffer(16);
    	MatrixCZHV.MatrixToBuffer(model, modelz);
    	
	}
	
	public void putVertex(FloatBuffer buffer, float x, float y, float z){
		buffer.put(x).put(y).put(z);
		buffer.put(0).put(1).put(0);
		buffer.put(x).put(z);
	}

	long totaltime = 0;
	int framecounter = 0;
	int framedelay = 10;
	public static float currentTime = 0;
	Vector4f dummyColor = new Vector4f(1,1,1,1);
	Vector4f selectedColor = new Vector4f(0,1,1,1);
	Vector4f selectboxColor = new Vector4f(0,0,0.5f,0.5f);
	Vector4f floorColor = new Vector4f(1,1,1,1);
	private long frametime = 0;
	private long totalframetime = 0;
	public void update(){
		if (Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			mainClass.exit();
		}
		flockingManager.loop();
		
		MVP.setIdentity();
		Matrix4f.mul(projMat, viewMat, MVP);
		
		selecter.update(MVP);
		lightManager.update();
		
		shadowManager.update();
		
		
		long sleeptime = System.currentTimeMillis();
		sleep(framedelay);
		sleeptime = System.currentTimeMillis() - sleeptime;

		frametime = System.currentTimeMillis() - frametime;
		totalframetime += frametime - sleeptime;
		frametime = System.currentTimeMillis();
		
		
		framecounter++;
		if (framecounter == 100){
			framecounter = 0;
			System.out.println("100 frames in " + (totalframetime/100f) + " ms");
			totalframetime = 0;
		}
		
		camera.lookThrough();
		
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glDisable(GL11.GL_CULL_FACE);

		
		currentTime += 0.001f;
		
		lightShader.bind();
		lightManager.bind(lightShader);
		//lightShader.putUnifFloat("time", currentTime);
		lightShader.bindTexture("texture", tex);
		lightShader.bindTexture("shadowMap", shadowManager.getShadowDepthTexture());
		//viewMatrix = lightManager.getLight(1).calcViewMatrix().getViewMatrix();
		lightShader.putMat4("viewMatrix", viewMatrix);
		lightShader.putMat4("projectionMatrix", projectionMatrix);
		
		lightShader.putMat4("shadowProjectionMatrix", lightManager.getLight(1).getProjectionMatrix());
		lightShader.putMat4("shadowMVP", lightManager.getLight(1).getViewMatrix());
		lightShader.putMat4("biasMatrix", shadowManager.getBiasMatrix());
		
		Vector3f pos = camera.getPosition();
		lightShader.putUnifFloat4("eyeposition", -pos.x, -pos.y, -pos.z, 1);

		if (Mouse.isButtonDown(1)){
			Vector2f mouse = selecter.getNormalizedMouse();
			Line3D ray = MatrixCZHV.getPickingRayStartDir(mouse.x, mouse.y, camera.getWorldPosition(), viewMat, projMat);
			Vector3f colPoint = ray.collideXZPlane(0);
			flockingTarget.x = colPoint.x;
			flockingTarget.y = 0;
			flockingTarget.z = colPoint.z;
			flockingTarget.w = 1;
		}
		
		bufferGeo(lightShader);
    	
		
        lightManager.unbind();
        lightShader.unbind();
        
        quadShader.bind();
        
        selecter.draw(quadShader, quadVBO, selectboxColor);;
		
        quadShader.unbind();

		TOOLBOX.checkGLERROR(true);

		Display.update();
	}
	
	public void bufferGeo(ShaderObject shader){
    	shader.putUnifFloat4("color", dummyColor);
    	
		shader.putMat4("modelMatrix", modelz);
		
    	quadModel.draw(shader);
		
        
        for (Dummy3DObj dummy : objList){
	        if (dummy.isSelected()){
	        	shader.putUnifFloat4("color", selectedColor);
	        }else{
	        	shader.putUnifFloat4("color", dummyColor);
	        }
	        
	        dummy.draw(shader);
        }
        
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
}
