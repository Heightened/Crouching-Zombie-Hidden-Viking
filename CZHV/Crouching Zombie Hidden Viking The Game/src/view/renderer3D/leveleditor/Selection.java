package view.renderer3D.leveleditor;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import view.renderer3D.core.Camera;
import view.renderer3D.core.Line3D;
import view.renderer3D.core.MatrixCZHV;
import view.renderer3D.core.Renderer3D;
import view.renderer3D.core.ShaderObject;
import view.renderer3D.leveleditor.objtypes.LVLEditorObject;

public class Selection {
	public static LVLEditorObject currentSelection;
	public static boolean moving = false;
	
	public static void setSelection(LVLEditorObject obj){
		if (currentSelection != null){
			LevelEditor.addToMap(currentSelection);
		}
		LevelEditor.map.remove(obj);
		currentSelection = obj;
		OptionsPanel.setSelectedObject(obj);
	}
	
	public static void clearSelection(){
		if (currentSelection != null){
			LevelEditor.addToMap(currentSelection);
		}
		currentSelection = null;
		OptionsPanel.setSelectedObject(null);
	}
	
	public static void setMoving(){
		moving = true;
	}
	
	
	static boolean click = false;
	public static void update(Camera camera, Matrix4f viewMat, Matrix4f projMat){
		boolean clicked = false;
		if (Mouse.isButtonDown(0)){
			if (click == false){
				clicked = true;
			}
			click = true;
		}else{
			click = false;
		}
		if (currentSelection != null){
			if (moving){
				Line3D ray = getMouseRay(camera, viewMat, projMat);
				Vector3f colPoint = ray.collideXZPlane(0);
				currentSelection.setPosition(colPoint.x, 0, colPoint.z);
				if (clicked){
					moving = false;
					clearSelection();
					System.out.println("stopped moving");
					return;
				}
			}else{
				
			}
		}
		if (clicked){
			System.out.println("CHECKING");
			Line3D ray = getMouseRay(camera, viewMat, projMat);
			ArrayList<LVLEditorObject> m = LevelEditor.map.objList;
			LVLEditorObject found = null;
			for (LVLEditorObject obj : m){
				if (obj.checkAABB(ray)){
					found = obj;
					break;
				}
			}
			if (found != null){
				setSelection(found);
			}else{
				clearSelection();
			}
		}
		
		
	}
	
	public static Line3D getMouseRay(Camera camera, Matrix4f viewMat, Matrix4f projMat){
		Vector2f mouse = getNormalizedMouse();//selecter.getNormalizedMouse();
		mouse.y *= -1;
		return  MatrixCZHV.getPickingRayStartDir(mouse.x, mouse.y, camera.getWorldPosition(), viewMat, projMat);
	}
	
	public static Vector2f getNormalizedMouse(){
		Vector2f ret = normalize(new Vector2f(Mouse.getX(), Mouse.getY()));
		//ret.y = - ret.y;
		return ret;
	}
	

	public static Vector2f normalize(Vector2f in){
		return new Vector2f((in.x/Renderer3D.screenSize.width)*2-1, (in.y/Renderer3D.screenSize.height)*2-1);
	}
	
	public static void draw(ShaderObject shader){
		if (currentSelection != null){
			shader.putUnifFloat4("color", 1f, 0f, 0f, 1f);
			currentSelection.update();
			currentSelection.draw(shader);
		}
	}
}
