import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import view.renderer3D.core.tempFlocking.Vehicle;


public class DrawPanel extends JPanel implements MouseMotionListener{
	
	JFrame frame;
	ArrayList<Vehicle> vlist;
	public DrawPanel(JFrame frame, ArrayList<Vehicle> vlist){
		this.frame = frame;
		this.vlist = vlist;
		addMouseMotionListener(this);
	}
	
	Color backgroundcol = new Color(255,200,0);
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(backgroundcol);
		g2.fillRect(0,0,frame.getWidth(), frame.getHeight());
		g2.setColor(Color.black);
		g2.fillOval(225, 225, 50, 50);
		for (Vehicle v : vlist){
			drawVehicle(g2, v);
		}
		
	}
	
	public void drawVehicle(Graphics2D g2, Vehicle v){
		g2.setColor(Color.CYAN);
		g2.fillOval((int)(v.position.x-7), (int)(v.position.y-7), 14, 14);
		g2.setColor(Color.blue);
		g2.drawLine((int)(v.position.x), (int)(v.position.y), (int)(v.position.x + v.steering.x*40), (int)(v.position.y + v.steering.y*40));
		g2.setColor(Color.MAGENTA);
		g2.drawLine((int)(v.position.x), (int)(v.position.y), (int)(v.position.x + v.velocity.x*20), (int)(v.position.y + v.velocity.y*20));
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	static int mousex;
	static int mousey;
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		mousex = arg0.getX();
		mousey = arg0.getY();
	}
}
