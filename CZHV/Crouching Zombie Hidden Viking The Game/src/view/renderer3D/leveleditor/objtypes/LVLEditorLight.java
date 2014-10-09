package view.renderer3D.leveleditor.objtypes;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class LVLEditorLight extends LVLEditorObject{
	
	public LVLEditorLight(){
		super("Light", LVLEditorObject.LIGHT);
	}
	
	@Override
	public void writeToInterface(JList list){
		super.writeToInterface(list);
		DefaultListModel listModel = (DefaultListModel) list.getModel();
	}
	
	@Override
	public LVLEditorObject getInstance(){
		return new LVLEditorLight();
	}
}
