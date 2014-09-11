import java.util.ArrayList;

import javax.swing.JFrame;


public class FlockingMain {
	JFrame frame;
	DrawPanel panel;
	public static void main(String[] args){
		FlockingMain m = new FlockingMain();
	}

	ArrayList<Vehicle> vlist;	
	public FlockingMain(){
		vlist = new ArrayList<>();
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				vlist.add(new Vehicle(80*i,80*j));
			}
		}
		
		
		frame = new JFrame();
		frame.setSize(500,500);
		frame.setLocation(200, 200);
		panel = new DrawPanel(frame, vlist);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loop();
	}

	public void loop(){
		while(true){
			try{
				Thread.sleep(1);
			} catch(Exception e){
				e.printStackTrace();
			}
			for (Vehicle v : vlist){
				v.update(this);
				v.update(this);
			}
			panel.repaint();
		}
	}
}
