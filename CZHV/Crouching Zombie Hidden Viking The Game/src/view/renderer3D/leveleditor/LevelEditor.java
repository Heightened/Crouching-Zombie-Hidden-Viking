package view.renderer3D.leveleditor;

import java.awt.Dimension;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.Camera;
import view.renderer3D.core.MatrixCZHV;
import view.renderer3D.core.ShaderObject;
import view.renderer3D.core.TOOLBOX;
import view.renderer3D.core.grid.ViewGrid;
import view.renderer3D.core.lighting.LightManager;
import view.renderer3D.core.resources.Model;
import view.renderer3D.inputoutput.FileToString;
import view.renderer3D.leveleditor.objtypes.Fire;
import view.renderer3D.leveleditor.objtypes.LVLEditorLight;
import view.renderer3D.leveleditor.objtypes.LVLEditorObject;
import view.renderer3D.leveleditor.objtypes.VikingSpawnPoint;
import view.renderer3D.leveleditor.objtypes.ZombieSpawnPoint;

public class LevelEditor{
	private Camera camera;
	private ShaderObject lightShader;
	private ShaderObject quadShader;
	private Matrix4f MVP;
	private LightManager lightManager;
	private FloatBuffer modelz;
	private ViewGrid viewGrid;
	private Model quadModel;
	private OptionsPanel optionsPanel;
	
	public static void main(String[] args){
		System.out.println("Level Editor");
		OptionsPanel p = new OptionsPanel();
		
		LevelEditor e = new LevelEditor(p);
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			e.update();
		}
		p.close();
	}
	
	public LevelEditor(OptionsPanel optionsPanel){
		this.optionsPanel = optionsPanel;
		this.map = new Map();
		optionsPanel.addObjType(new LVLEditorLight());
		optionsPanel.addObjType(new Fire());
		optionsPanel.addObjType(new VikingSpawnPoint());
		optionsPanel.addObjType(new ZombieSpawnPoint());
		setupDisplay();
		lightManager = new LightManager(null);
		MVP = new Matrix4f();
		camera = new Camera(viewMatrix, viewMat);
		
		lightShader = new ShaderObject("lighting shader");
		lightShader.addVertexSource(FileToString.read("defaultlighting/defaultlighting.vert"));
		lightShader.addFragmentSource(FileToString.read("defaultlighting/defaultlighting.frag"));
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
		
		quadModel = new Model("quad.obj");
		
		viewGrid = new ViewGrid(quadModel, 2.5f, 1.4f);
		
		Matrix4f model = new Matrix4f();
    	MatrixCZHV.getModelMatrix(new Vector3f(1,-0.035f,1), new Vector3f(1,1,1), new Vector3f(0,0,0), model);
    	modelz = BufferUtils.createFloatBuffer(16);
    	MatrixCZHV.MatrixToBuffer(model, modelz);
    	
    	System.out.println("LEVEL EDITOR INITIALIZED");
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
	Vector4f zombieColor = new Vector4f(0,1,0,1);
	Vector4f vikingColor = new Vector4f(0,0,1,1);
	Vector4f itemColor = new Vector4f(1,0,0,1);
	Vector4f decorColor = new Vector4f(0,0,0,1);
	Vector4f selectedColor = new Vector4f(0,1,1,1);
	Vector4f selectboxColor = new Vector4f(0,0,0.5f,0.5f);
	Vector4f floorColor = new Vector4f(1,1,1,1);
	private long frametime = 0;
	private long totalframetime = 0;
	public void update(){
		MVP.setIdentity();
		Matrix4f.mul(projMat, viewMat, MVP);
		
		//selecter.update(MVP);
		lightManager.setGridOffset(camera.getWorldPosition().x-2f,0, camera.getWorldPosition().z-3f);
		lightManager.update();
		
		
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
		

		viewGrid.update(camera);
		
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		
		
		
		currentTime += 0.001f;
		
		lightShader.bind();
		lightManager.bind(lightShader);
		//lightShader.putUnifFloat("time", currentTime);
		//lightShader.bindTexture("texture", tex);
		//lightShader.bindTexture("shadowMap", shadowManager.getShadowDepthTexture());
		//viewMatrix = lightManager.getLight(1).calcViewMatrix().getViewMatrix();
		lightShader.putMat4("viewMatrix", viewMatrix);
		lightShader.putMat4("projectionMatrix", projectionMatrix);
		
		//lightShader.putMat4("shadowProjectionMatrix", lightManager.getLight(1).getProjectionMatrix());
		//lightShader.putMat4("shadowMVP", lightManager.getLight(1).getViewMatrix());
		//lightShader.putMat4("biasMatrix", shadowManager.getBiasMatrix());
		
		Vector3f pos = camera.getPosition();
		lightShader.putUnifFloat4("eyeposition", -pos.x, -pos.y, -pos.z, 1);

		viewGrid.draw(lightShader);
		
		bufferGeo(lightShader);
    	
		
        lightManager.unbind();
        lightShader.unbind();
        
        quadShader.bind();
		
        quadShader.unbind();

		TOOLBOX.checkGLERROR(true);

		Display.update();
	}
	
	public static Map map;
	public static final float cellSize = 0.1f;
	public void bufferGeo(ShaderObject shader){	
		if (map != null){
			map.draw(shader);
		}
		Selection.update(camera, viewMat, projMat);
		Selection.draw(shader);
	}

	public static void addToMap(LVLEditorObject obj){
		map.add(obj);
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
        
        
		TOOLBOX.checkGLERROR(true);
	}

}
