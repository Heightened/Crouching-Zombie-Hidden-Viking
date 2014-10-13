package view.renderer3D.leveleditor.objtypes;

import org.lwjgl.util.vector.Vector3f;

public class ZombieSpawnPoint extends LVLEditorObject{

	public ZombieSpawnPoint() {
		super("ZombieSpawnPoint", new Vector3f(0.2f,1f,0.2f));
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public LVLEditorObject getInstance(){
		ZombieSpawnPoint l = new ZombieSpawnPoint();
		return l;
	}
}
