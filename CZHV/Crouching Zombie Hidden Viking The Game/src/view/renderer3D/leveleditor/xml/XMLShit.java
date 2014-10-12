package view.renderer3D.leveleditor.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import view.renderer3D.leveleditor.LevelEditor;
import view.renderer3D.leveleditor.Selection;
import view.renderer3D.leveleditor.objtypes.LVLEditorObject;

public class XMLShit {
	public static void read(File filename){
		LevelEditor.map.clear();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filename);
			XMLInputFactory xmlInFact = XMLInputFactory.newInstance();
			XMLStreamReader reader = xmlInFact.createXMLStreamReader(fis);
			while(reader.hasNext()) {
				int index = reader.next();
				if(index == XMLStreamReader.START_ELEMENT){
					if (reader.getLocalName().equals("root")){
						continue;
					}
					int attributes = reader.getAttributeCount();
                	HashMap<String, String> values = new HashMap<>();
                    for(int i=0;i<attributes;++i) {
                        values.put(reader.getAttributeLocalName(i),  reader.getAttributeValue(i));
                    }
                    try{
                    	LevelEditor.map.add( (LVLEditorObject)(XMLSerializeInterface.parse(values).getInstance()));
                    }catch(Exception e){
                    	e.printStackTrace();
                    }
				}
			}
		}
		catch(IOException exc) {
			exc.printStackTrace();
		}
		catch(XMLStreamException exc) {
			exc.printStackTrace();
		}
	}

	public static void write(File filename){
		Selection.clearSelection();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filename);
			XMLOutputFactory xmlOutFact = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = xmlOutFact.createXMLStreamWriter(fos);
			writer.writeStartDocument();
			
			ArrayList<LVLEditorObject> list = LevelEditor.map.objList;
			writer.writeStartElement("root");
			for (LVLEditorObject obj : list){
				writer.writeStartElement(obj.name);
				try{
					obj.writeToXML(writer);
				}catch(Exception e){
					e.printStackTrace();
				}
				writer.writeEndElement();
			}

			writer.writeEndElement();
			writer.flush();
		}
		catch(IOException exc) {
		}
		catch(XMLStreamException exc) {
		}
		finally {
		}
	}
}
