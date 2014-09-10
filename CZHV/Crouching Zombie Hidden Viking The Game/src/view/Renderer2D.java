package view;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

public class Renderer2D {
	private JFrame frame;
	private DrawPanel panel;
	public Renderer2D(int x, int y){
		frame = new JFrame();
		frame.setSize(500,500);
		panel = new DrawPanel(0, 0, new Dimension(frame.getWidth(), frame.getHeight()));
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
		frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	panel.setDimension(frame.getWidth(), frame.getHeight());
            }
        });
	}
	
	public void render(){
		panel.repaint();
	}
	
	public void setView(int x, int y){
		panel.setViewPosition(x, y);
	}
	
	public static void main(String[] args){
		Renderer2D r = new Renderer2D(0,0);
		for (int i = 0; i < 100; i++){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//r.setView(i,i);
			r.render();
		}
	}
}
