package view.renderer3D.core;

import java.awt.Dimension;
import java.awt.Point;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.Game;
import model.character.GameCharacter;
import model.item.Item;
import model.map.Cell;
import model.map.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import simulator.tempFlocking.FlockingManager;
import simulator.tempFlocking.Vehicle;
import view.renderer3D.core.grid.ViewGrid;
import view.renderer3D.core.lighting.LightManager;
import view.renderer3D.core.resources.Model;
import view.renderer3D.core.shadows.ShadowManager;
import view.renderer3D.inputoutput.FileToString;
import view.renderer3D.particles.ParticleTest;
import czhv.mainClass;

public class Renderer3D implements RendererInfoInterface{
	private Camera camera;
	private VBO quadVBO;
	private VBO lineVBO;
	private TextureObject tex;
	private TextureObject normtex;
	private ShaderObject lightShader;
	private ShaderObject quadShader;
	private ShaderObject lineShader;
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
	private Map map;
	private ViewGrid viewGrid;
	private Collection<Cell> activeCells;
	private Collection<Cell> impassibleCells;
	public Renderer3D(Game game){
		setupDisplay();
		this.game = game;
		map = game.getMap();
		for (int i = 0; i < 50; i++){
			for (int j = 0; j < 50; j++){
				game.getFlockingMap().getActiveCells(2*i, 2*j);
			}
		} 
		impassibleCells = map.getImpassibleCells();
    	shadowManager = new ShadowManager(this);
		lightManager = new LightManager(shadowManager);
		new LevelLoader("level.xml", this);
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
		
		lineVBO = new VBO(VBO.STATIC_DRAW);
		buffer = BufferUtils.createFloatBuffer(8*2);//8 floats per vert, 2 verts
		//line
		putVertex(buffer, 1, 0, 0);
		putVertex(buffer, 0, 1, 0);
		
		buffer.flip();
		lineVBO.bind();
		lineVBO.put(buffer);
		lineVBO.unbind();
		
		tex = new TextureObject("tex.png");
		tex.setup();
		tex.setMINMAG(GL11.GL_LINEAR);
		tex.setWRAPST(GL11.GL_REPEAT);
		tex.mipMap();
		tex.unbind();
		
		normtex = new TextureObject("gradient_map.png");
		normtex.setup();
		normtex.setMINMAG(GL11.GL_LINEAR);
		normtex.setWRAPST(GL11.GL_REPEAT);
		normtex.mipMap();
		normtex.unbind();
		
		
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
		
		lineShader = new ShaderObject("line shader");
		lineShader.addVertexSource(FileToString.read("lineshader.vert"));
		lineShader.addFragmentSource(FileToString.read("lineshader.frag"));
		lineShader.compileVertex();
		lineShader.compileFragment();
		lineShader.link();
		lineShader.bind();
		lineShader.findUniforms();
		lineShader.findAttributes();
		lineShader.unbind();
		
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
		
		selecter = new DEMOselecter( objList);
		
		quadModel = new Model("quad.obj");
		
		viewGrid = new ViewGrid(quadModel, 2.5f, 1.4f);
		
		Matrix4f model = new Matrix4f();
    	MatrixCZHV.getModelMatrix(new Vector3f(1,-0.035f,1), new Vector3f(1,1,1), new Vector3f(0,0,0), model);
    	modelz = BufferUtils.createFloatBuffer(16);
    	MatrixCZHV.MatrixToBuffer(model, modelz);
    	
    	fireTest = new ParticleTest();
	}
	
	ParticleTest fireTest;
	
	public LightManager getLightManager(){
		return lightManager;
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
		if (Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			mainClass.exit();
		}

		activeCells = map.getActiveCells();
		
		for (Cell cell : activeCells){
			List<GameCharacter> gameChars = cell.getCharacterHolder().getItem();
			for (GameCharacter gameChar : gameChars){
				if (gameChar != null){
					gameChar.update();
				}
			}
		}
		
		MVP.setIdentity();
		Matrix4f.mul(projMat, viewMat, MVP);
		
		//selecter.update(MVP);
		lightManager.setGridOffset(camera.getWorldPosition().x-2f,0, camera.getWorldPosition().z-3f);
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
		

		viewGrid.update(camera);
		
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		
		
		
		currentTime += 0.001f;
		
		lightShader.bind();
		lightManager.bind(lightShader);
		//lightShader.putUnifFloat("time", currentTime);
		//lightShader.bindTexture("texture", tex);
		lightShader.bindTexture("normsamp", normtex);
		lightShader.bindTexture("shadowMap", shadowManager.getShadowDepthTexture());
		//viewMatrix = lightManager.getLight(1).calcViewMatrix().getViewMatrix();
		lightShader.putMat4("viewMatrix", viewMatrix);
		lightShader.putMat4("projectionMatrix", projectionMatrix);
		
		lightShader.putMat4("shadowProjectionMatrix", lightManager.getLight(1).getProjectionMatrix());
		lightShader.putMat4("shadowMVP", lightManager.getLight(1).getViewMatrix());
		lightShader.putMat4("biasMatrix", shadowManager.getBiasMatrix());
		
		Vector3f pos = camera.getPosition();
		lightShader.putUnifFloat4("eyeposition", -pos.x, -pos.y, -pos.z, 1);

		viewGrid.draw(lightShader);
		
		bufferGeo(lightShader);
    	
		fireTest.update(lightShader);
		
        lightManager.unbind();
        lightShader.unbind();
        
        quadShader.bind();
        
        selecter.draw(quadShader, quadVBO, selectboxColor);
		
        quadShader.unbind();

        if (game.AI_DRAW_HIERARCHY){
        	drawLines();
        }
        
		TOOLBOX.checkGLERROR(true);

		Display.update();
	}
	
	public Vector3f lineColor = new Vector3f(1,0,0);
	public void drawLines(){
		lineShader.bind();
		lineShader.putMat4("viewMatrix", viewMatrix);
		lineShader.putMat4("projectionMatrix", projectionMatrix);
		
		for (Cell cell : activeCells){
			List<GameCharacter> gameChars = cell.getCharacterHolder().getItem();
			for (GameCharacter gameChar : gameChars){
				for (GameCharacter follower : gameChar.getFollowers()){
						drawLine(new Vector3f(gameChar.getAbsX()*cellSize,0.05f,gameChar.getAbsY()*cellSize), new Vector3f(follower.getAbsX()*cellSize,0.05f,follower.getAbsY()*cellSize), lineColor);
				}
			}
		}
		
		lineShader.unbind();
	}
	
	public void drawLine(Vector3f start, Vector3f end, Vector3f color){
		lineShader.putUnifFloat4("startPos", start.x, start.y, start.z, 1);
		lineShader.putUnifFloat4("endPos", end.x, end.y, end.z, 1);
		lineShader.putUnifFloat4("color", color.x, color.y, color.z, 1);
		
		lineVBO.bind();
		lineVBO.prepareForDraw(lineShader);
		lineVBO.drawLines();
		lineVBO.unbind();
	}
	
	public static final float cellSize = 0.1f;
	public void bufferGeo(ShaderObject shader){	
		for (Cell cell : activeCells){
			List<GameCharacter> gameChars = cell.getCharacterHolder().getItem();
			for (GameCharacter c : gameChars){
				c.setPosition(c.getAbsX()*cellSize, 0, c.getAbsY()*cellSize);
				if (c.isSelected()){
					shader.putUnifFloat4("color", selectedColor);
				}else if (c.isInfected()){
					shader.putUnifFloat4("color", zombieColor);
				}else{
					shader.putUnifFloat4("color", vikingColor);
				}
		        c.draw(shader);
			}
			model.item.Item i = cell.getItemHolder().getItem();
			if (i != null){
				i.setPosition(cell.getX()*cellSize + 0.5f*cellSize, 0, cell.getY()*cellSize + 0.5f*cellSize);
	        	shader.putUnifFloat4("color", itemColor);
		        i.draw(shader);
			}
			
		}
		Dummy3DObj d = new Dummy3DObj();
		for (Cell cell : impassibleCells){
				d.setPosition(cell.getX()*cellSize + 0.5f*cellSize, 0.02f, cell.getY()*cellSize + 0.5f*cellSize);
	        	shader.putUnifFloat4("color", decorColor);
		        d.draw(shader);
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
        
        
		TOOLBOX.checkGLERROR(true);
	}

	@Override
	public Object click(float x, float y) {
		//check interface, return button
		Vector2f mouse = selecter.normalize(x, y);//selecter.getNormalizedMouse();
		mouse.y *= -1;
		Line3D ray = MatrixCZHV.getPickingRayStartDir(mouse.x, mouse.y, camera.getWorldPosition(), viewMat, projMat);
		//else check characters
		for (Cell c : activeCells){
			List<GameCharacter> gameChars = c.getCharacterHolder().getItem();
			for (GameCharacter gameChar : gameChars){
				if (gameChar.checkAABB(ray)){
					return c;
				}
			}
		}
		//else return world position
		Vector3f colPoint = ray.collideXZPlane(0);
		int cellx = (int)(colPoint.x/cellSize);
		int celly = (int)(colPoint.z/cellSize);
		for (Cell c : activeCells){
			if (c.getX() == cellx && c.getY() == celly){
				return c;
			}
		}
		System.out.println(colPoint);
		return new Vector2f(colPoint.x/cellSize, colPoint.z/cellSize);
	}
	
	@Override
	public Collection<Cell> squareSelect(Point start, Point end){
		Vector2f startMouse = selecter.normalize(start.x, start.y);
		Vector2f endMouse = selecter.normalize(end.x, end.y);
		startMouse.y *= -1;
		endMouse.y *= -1;
		Collection<Cell> retCollection = new ArrayList<Cell>();
		Line3D ray1 = MatrixCZHV.getPickingRayStartDir(startMouse.x, startMouse.y, camera.getWorldPosition(), viewMat, projMat);
		Vector3f startCol = ray1.collideXZPlane(0);
		Line3D ray2 = MatrixCZHV.getPickingRayStartDir(endMouse.x, endMouse.y, camera.getWorldPosition(), viewMat, projMat);
		Vector3f endCol = ray2.collideXZPlane(0);
		float minX = Math.min(startCol.x, endCol.x);
		float maxX = Math.max(startCol.x, endCol.x);
		float minZ = Math.min(startCol.z, endCol.z);
		float maxZ = Math.max(startCol.z, endCol.z);
		for (Cell c : activeCells){
			if (c.getX()*cellSize > minX && c.getX()*cellSize < maxX &&
					c.getY()*cellSize > minZ && c.getY()*cellSize < maxZ){
				retCollection.add(c);
			}
		}
		return retCollection;
	}
}
