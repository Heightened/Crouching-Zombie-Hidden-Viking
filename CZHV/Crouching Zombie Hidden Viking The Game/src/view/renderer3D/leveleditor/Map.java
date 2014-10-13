package view.renderer3D.leveleditor;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import view.renderer3D.core.ShaderObject;
import view.renderer3D.leveleditor.objtypes.LVLEditorObject;

public class Map {
	public ArrayList<LVLEditorObject> objList;
	
	public Map(){
		objList = new ArrayList<LVLEditorObject>();
	}
	
	public void add(LVLEditorObject obj){
		objList.add(obj);
	}
	
	public void clear(){
		Selection.clearSelection();
		objList.clear();
	}
	
	public void remove(LVLEditorObject obj){
		objList.remove(obj);
	}
	
	public void draw(ShaderObject shader){
		for (LVLEditorObject obj:  objList){
			Vector3f color = obj.getEditorColor();
			shader.putUnifFloat4("color", color.x, color.y, color.z, 1);
			obj.update();
			obj.draw(shader);
		}
	}
	
}
