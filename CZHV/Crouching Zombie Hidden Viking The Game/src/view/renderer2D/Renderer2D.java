package view.renderer2D;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;

import javax.swing.JFrame;

import model.map.Cell;
import model.map.Map;

public class Renderer2D {
	public JFrame frame;
	private DrawPanel panel;
	private Lights lights;
	private Map m;
	public Renderer2D(int x, int y){
		m = new Map(128,128);
		m.populate();
		lights = new Lights(); 
		frame = new JFrame();
		frame.setSize(500,500);
		frame.setLocation(400,200);
		panel = new DrawPanel(0, 0, new Dimension(frame.getWidth(), frame.getHeight()), lights.getLightMap());
		lights.setViewPosition(panel.getViewPosition());
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	panel.setDimension(frame.getWidth(), frame.getHeight());
            }
        });
	}
	
	public void render(){
		Collection<Cell> c = m.getActiveCells();
		panel.setCells(c);
		panel.setSolidCells(m.getImpassibleCells());
		lights.render();
		panel.repaint();
	}
	
	public void register(RenderInfoController r){
		 panel.register(r);
	}
	
	public void setView(int x, int y){
		panel.setViewPosition(x, y);
	}
	
	public static void main(String[] args){
		Renderer2D r = new Renderer2D(0,0);
		
		while(true){
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//r.setView(i,i);
			r.render();
		}
	}
}