import java.util.ArrayList;

import javax.swing.JFrame;


public class FlockingMain {
	JFrame frame;
	DrawPanel panel;
	Grid grid;
	public static void main(String[] args){
		FlockingMain m = new FlockingMain();
	}

	ArrayList<Vehicle> vlist;	
	public FlockingMain(){
		grid = new Grid(20,20);
		vlist = new ArrayList<>();
		for (int i = 0; i < 20; i++){
			for (int j = 0; j < 20; j++){
				vlist.add(new Vehicle(20*i,20*j));
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

	int framecounter = 0;
	long time = 0;
	public void loop(){
		while(true){
			framecounter++;
			try{
				Thread.sleep(1);
			} catch(Exception e){
				e.printStackTrace();
			}
			for (Vehicle v : vlist){
				v.update(this, grid);
				v.update(this, grid);
			}
			if (framecounter == 100){
				framecounter = 0;
				time = System.nanoTime() - time;
				System.out.println("AVG MS " + time/100/1000000f);
				time = System.nanoTime();
			}
			grid.reset();
			for (Vehicle v : vlist){
				grid.addVehicle(v);
			}
			panel.repaint();
		}
	}
}
