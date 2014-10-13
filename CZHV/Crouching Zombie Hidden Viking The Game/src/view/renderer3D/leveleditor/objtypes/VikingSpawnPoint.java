package view.renderer3D.leveleditor.objtypes;

import org.lwjgl.util.vector.Vector3f;

public class VikingSpawnPoint extends LVLEditorObject{

	public VikingSpawnPoint() {
		super("VikingSpawnPoint", new Vector3f(0.2f,0.2f,1f));
		// TODO Auto-generated constructor stub
	}

	@Override
	public LVLEditorObject getInstance(){
		VikingSpawnPoint l = new VikingSpawnPoint();
		return l;
	}
}
