package view.renderer3D.leveleditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import view.renderer3D.leveleditor.objtypes.LVLEditorObject;

public class OptionsPanel  implements ActionListener,ListSelectionListener, DocumentListener {
	ArrayList<LVLEditorObject> objTypes;
	private JFrame frame;

	/**
	 * Create the application.
	 */
	public OptionsPanel() {
		initialize();
		frame.setVisible(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(new Point((int)screenSize.getWidth()-frame.getWidth(),100));
		objTypes = new ArrayList<>();
	}
	
	public void addObjType(LVLEditorObject o){
		objTypes.add(o);
		ObjTypeBox.addItem(o.name);
	}

	public static void setSelectedObject(LVLEditorObject obj){
		if (obj != null){
			obj.writeToInterface(VariableList);
		}else{
			DefaultListModel listModel = (DefaultListModel) VariableList.getModel();
	        listModel.removeAllElements();
		}
	}
	
	public void close(){
		frame.setVisible(false);
		frame.dispose();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	JComboBox ObjTypeBox;
	static JList VariableList;
	JTextArea VariableInputArea;
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 335, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ObjTypeBox = new JComboBox();
		frame.getContentPane().add(ObjTypeBox, BorderLayout.NORTH);
		
		VariableInputArea = new JTextArea();
		VariableInputArea.getDocument().addDocumentListener(this);
		frame.getContentPane().add(VariableInputArea, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(128, 1, 89, 23);
		panel.add(btnCreate);
		btnCreate.addActionListener(this);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(227, 1, 89, 23);
		panel.add(btnRemove);
		btnRemove.addActionListener(this);
		
		DefaultListModel listmodel=new DefaultListModel();
		VariableList = new JList(listmodel);
		VariableList.setBounds(0, 2, 126, 218);
		VariableList.getSelectionModel().addListSelectionListener(this);
		panel.add(VariableList);
		
		JButton btnLoadLevel = new JButton("Import Level");
		btnLoadLevel.setBounds(128, 85, 188, 23);
		panel.add(btnLoadLevel);
		btnLoadLevel.addActionListener(this);
		
		JButton btnExportLevel = new JButton("Export Level");
		btnExportLevel.setBounds(128, 119, 188, 23);
		panel.add(btnExportLevel);
		btnExportLevel.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton source = (JButton)e.getSource();
		if (source.getText().equals("Create")){
			String objName = (String)ObjTypeBox.getSelectedItem();
			for (LVLEditorObject o : objTypes){
				if (o.name.equals(objName)){}
					System.out.println("Create " + o.name);
					Selection.setSelection( o.getInstance());
					Selection.setMoving();
				}
			
		}
		if (source.getText().equals("Import Level")){
			System.out.println("Importing...");
			JFileChooser c = new JFileChooser();
			c.setCurrentDirectory(new File(System.getProperty("user.dir")));
			int returnVal = c.showOpenDialog(this.frame);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		        XMLShit.read(c.getSelectedFile());
				System.out.println("Imported");
		    }
		}
		if (source.getText().equals("Export Level")){
			System.out.println("Exporting...");
			JFileChooser c = new JFileChooser();
			c.setCurrentDirectory(new File(System.getProperty("user.dir")));
			int returnVal = c.showOpenDialog(this.frame);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		        XMLShit.write(c.getSelectedFile());
				System.out.println("Exported");
		    }
		}
	}

	public String selectedVariable = "";
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		DefaultListSelectionModel source = (DefaultListSelectionModel)e.getSource();
		selectedVariable = ((String)VariableList.getSelectedValue());
		if (selectedVariable == null){
			return;
		}
		if (selectedVariable.indexOf(":") == -1){
			return;
		}
		selectedVariable = selectedVariable.substring(0, selectedVariable.indexOf(':'));
		System.out.println("Selected " + selectedVariable);
		if (Selection.currentSelection != null){
			String output = Selection.currentSelection.getVariableString(selectedVariable);
			VariableInputArea.setText(output);
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		 textAreaChanged();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		 textAreaChanged();
		
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		 textAreaChanged();
		
	}
	
	public void textAreaChanged(){
		String text = VariableInputArea.getText();
		Selection.currentSelection.parseVariableString(selectedVariable, text);
	}
}
