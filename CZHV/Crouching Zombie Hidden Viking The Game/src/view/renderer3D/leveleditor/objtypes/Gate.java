package view.renderer3D.leveleditor.objtypes;

import org.lwjgl.util.vector.Vector3f;

public class Gate extends LVLEditorObject {

	public Gate() {
		super("gate", new Vector3f(0.1f,0.1f,0.1f));
		// TODO Auto-generated constructor stub
	}

	@Override
	public LVLEditorObject getInstance(){
		Gate l = new Gate();
		return l;
	}
}
