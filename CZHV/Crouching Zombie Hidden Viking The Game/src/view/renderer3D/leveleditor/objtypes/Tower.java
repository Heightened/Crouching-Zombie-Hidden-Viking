package view.renderer3D.leveleditor.objtypes;

import org.lwjgl.util.vector.Vector3f;

public class Tower extends LVLEditorObject {

	public Tower() {
		super("tower", new Vector3f(0.1f,0.1f,0.1f));
		// TODO Auto-generated constructor stub
	}

	@Override
	public LVLEditorObject getInstance(){
		Tower l = new Tower();
		return l;
	}
}
