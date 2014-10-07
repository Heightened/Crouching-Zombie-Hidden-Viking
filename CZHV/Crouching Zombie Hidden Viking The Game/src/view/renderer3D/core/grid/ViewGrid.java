package view.renderer3D.core.grid;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.Model;
import view.renderer3D.core.Camera;
import view.renderer3D.core.MatrixCZHV;
import view.renderer3D.core.ShaderObject;

public class ViewGrid {
	private final int numGridCells = 16;
	
	private Model quadModel;
	private int gridX;
	private int gridY;
	private float gridSizeX;
	private float gridSizeY;
	
	public ViewGrid(Model quadModel, float gridSizeX, float gridSizeY){
		this.quadModel = quadModel;
		this.gridSizeX = gridSizeX;
		this.gridSizeY = gridSizeY;
	}
	
	public void update(Camera camera){
		gridX = (int)(camera.getWorldPosition().x/gridSizeX);
		gridY = (int)(camera.getWorldPosition().z/gridSizeY);
		
		gridX = clamp(gridX, -2, numGridCells-1);
		gridY = clamp(gridY, -2, numGridCells);
	}
	
	public int clamp(int in, int down, int up){
		int out = in;
		if (out < down){
			out = down;
		}else if (out > up){
			out = up;
		}
		return out;
	}
	
	public void draw(ShaderObject shader){
		shader.putUnifFloat4("color", new Vector4f(1,1,1,1));
		for (int x = gridX -1; x < gridX + 2; x++){
			for (int y = gridY -2; y < gridY + 1; y++){	
	        	//shader.putUnifFloat4("color", (x - gridX + 2)/4f, (y - gridY + 2)/4f,0,1);//gridcolor
	        	//shader.putUnifFloat4("color", 1, 1, 1, 1);//normal
				FloatBuffer modelMatrix = BufferUtils.createFloatBuffer(16);
				Matrix4f modelMat = new Matrix4f();
				MatrixCZHV.getModelMatrix(new Vector3f((x)*gridSizeX, -0.035f, (y)*gridSizeY), new Vector3f(gridSizeX,1,gridSizeY), new Vector3f(0,0,0), modelMat);
				MatrixCZHV.MatrixToBuffer(modelMat, modelMatrix);
				shader.putMat4("modelMatrix", modelMatrix);
				quadModel.draw(shader);
			}
		}
	}
}
