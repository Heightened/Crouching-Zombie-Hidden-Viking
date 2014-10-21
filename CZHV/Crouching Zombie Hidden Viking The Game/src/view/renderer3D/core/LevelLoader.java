package view.renderer3D.core;

import java.io.File;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import view.renderer3D.core.lighting.Light;
import view.renderer3D.leveleditor.objtypes.*;
import view.renderer3D.leveleditor.xml.XMLFeces;

public class LevelLoader {
	private Renderer3D renderer;
	public LevelLoader(String filename, Renderer3D renderer){
		this.renderer = renderer;
		ArrayList<LVLEditorObject> list = XMLFeces.read(new File(filename));
		for (LVLEditorObject obj : list){
			processObj(obj);
		}
	}
	
	private void processObj(LVLEditorObject obj){
		if (obj instanceof LVLEditorLight){
			LVLEditorLight converted = (LVLEditorLight)obj;
			Light l = new Light(new Vector3f(converted.position.x, converted.position.y, converted.position.z), 
					converted.color,
					converted.specularColor,
					converted.radius,converted.cutoff,
					new Vector4f(converted.direction.x,converted.direction.y,converted.direction.z, 1));
			renderer.getLightManager().addLight(l);
			System.out.println("NEW LIGHT");
		}
		if (obj instanceof Fire){
			Fire converted = (Fire)obj;
			
		}
		if (obj instanceof VikingSpawnPoint){
			VikingSpawnPoint converted = (VikingSpawnPoint)obj;
			
		}
		if (obj instanceof ZombieSpawnPoint){
			ZombieSpawnPoint converted = (ZombieSpawnPoint)obj;
			
		}
		if (obj instanceof Wall){
			Wall converted = (Wall)obj;
			
		}
		if (obj instanceof Gate){
			Gate converted = (Gate)obj;
			
		}
		if (obj instanceof Tower){
			Tower converted = (Tower)obj;
			
		}
	}
}
