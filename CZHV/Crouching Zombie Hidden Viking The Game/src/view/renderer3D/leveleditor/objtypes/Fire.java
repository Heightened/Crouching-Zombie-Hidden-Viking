package view.renderer3D.leveleditor.objtypes;

import org.lwjgl.util.vector.Vector3f;

public class Fire extends LVLEditorObject{

	public Fire() {
		super("fire", new Vector3f(1,0.5f,0.2f));
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public LVLEditorObject getInstance(){
		Fire l = new Fire();
		return l;
	}
	
}
