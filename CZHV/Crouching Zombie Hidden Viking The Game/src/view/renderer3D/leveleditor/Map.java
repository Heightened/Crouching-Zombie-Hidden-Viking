package view.renderer3D.leveleditor;

import java.util.ArrayList;

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
			if (obj.name.equals("Light")){
				shader.putUnifFloat4("color", 0.5f, 0.5f, 1f, 1f);
				obj.update();
				obj.draw(shader);
			}
		}
	}
	
}
