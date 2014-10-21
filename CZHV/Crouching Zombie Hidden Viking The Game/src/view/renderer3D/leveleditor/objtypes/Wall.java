package view.renderer3D.leveleditor.objtypes;

import org.lwjgl.util.vector.Vector3f;

public class Wall extends LVLEditorObject {

	public Wall() {
		super("wall", new Vector3f(0.1f,0.1f,0.1f));
		// TODO Auto-generated constructor stub
	}

	@Override
	public LVLEditorObject getInstance(){
		Wall l = new Wall();
		return l;
	}
}
