package view.renderer3D.leveleditor.objtypes;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

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
	
	@Override
	public void writeToXML(XMLStreamWriter writer) throws XMLStreamException{
		super.writeToXML(writer);
	}
	
	
}
