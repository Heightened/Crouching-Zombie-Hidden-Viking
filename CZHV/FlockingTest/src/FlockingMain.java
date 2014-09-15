import java.awt.Dimension;
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
	public static final Dimension screenSize = new Dimension(1600,800);
	public static final int GRID_CELL_SIZE = 64;
	Vector2f mouseTarget = new Vector2f(0,0);
	Vector2f centerTarget = new Vector2f(screenSize.width+50,500);
	public FlockingMain(){

	/*	int variable = 70;
		int compare = 50;
		for (int i = 0; i <= 20; i++){
			variable = 40+i;
			int result = compare - variable;
			result += 10;
			result %= 10;
			
			System.out.println(result);
		}
		
		
		System.exit(0);*/
		
		grid = new Grid(screenSize.width/GRID_CELL_SIZE+2,screenSize.height/GRID_CELL_SIZE+2);
		vlist = new ArrayList<Vehicle>();
		Vector2f target = mouseTarget;
		for (int i = 0; i < 20; i++){
			for (int j = 0; j < 20; j++){
				vlist.add(new Vehicle(20*i,20*j, target));
			}
			if (target == mouseTarget){
				target = centerTarget;
			}else{
				target = mouseTarget;
			}
		}
		
		
		frame = new JFrame();
		frame.setSize(1000,800);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
				Thread.sleep(10);
			} catch(Exception e){
				e.printStackTrace();
			}
			mouseTarget.x = DrawPanel.mousex;
			mouseTarget.y = DrawPanel.mousey;
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
