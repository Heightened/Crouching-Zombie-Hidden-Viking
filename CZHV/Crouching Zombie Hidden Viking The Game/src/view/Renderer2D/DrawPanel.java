package view.Renderer2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import model.character.Character;

import javax.swing.JPanel;

import model.map.Cell;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener{
	private static final int CELL_SIZE = 16;
	private Point viewPosition;
	private Dimension dimension;
	private BufferedImage lightmap;
	private ArrayList<RenderInfoController> renderControllers;
	private Collection<Cell> cells;
	private Collection<Cell> solidCells;
	
	
	public DrawPanel(int viewX, int viewY, Dimension dimension, BufferedImage lightmap){
		this.lightmap = lightmap;
		viewPosition = new Point(viewX, viewY);
		this.dimension = dimension;
		renderControllers = new ArrayList<>();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void setCells(Collection<Cell> c){
		cells = c;
	}
	
	public void setSolidCells(Collection<Cell> c){
		solidCells = c;
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.green);
		g2.fillRect(0, 0, dimension.width, dimension.height);
		if (cells != null){
			for (Cell c : cells){
				Character character = c.getCharacterHolder().getItem();
				drawCharacter(g2, Color.BLUE, c.getX(), c.getY());
			}
			for (Cell c : solidCells){
				drawSolid(g2,c.getX(), c.getY());
			}
		}
		drawGrid(g2);
		drawSolid(g2, 8,5);
		//g2.drawImage(lightmap,-viewPosition.x%16-16,-viewPosition.y%16-16,128*16,128*16,null);
	}
	
	public Point getViewPosition(){
		return viewPosition;
	}
	
	public void setDimension(int width, int height){
		this.dimension.width = width;
		this.dimension.height = height;
	}
	
	public void setViewPosition(int x, int y){
		viewPosition.setLocation(x, y);
	}
	
	public void register(RenderInfoController r){
		renderControllers.add(r);
	}
	
	public void drawSolid(Graphics2D g2, int cellx, int celly){
		Point worldpos = cellToWorldSpace(cellx, celly);
		g2.setColor(Color.BLACK);
		g2.fillRect(worldpos.x, worldpos.y, CELL_SIZE+1, CELL_SIZE+1);
	}
	
	public void drawCharacter(Graphics2D g2, Color c, int cellx, int celly){
		drawOval(g2, c, cellToWorldSpace(cellx, celly));
	}
	
	public Point cellToWorldSpace(int cellx, int celly){
		return new Point(cellx*CELL_SIZE - viewPosition.x, celly*CELL_SIZE - viewPosition.y);
	}
	
	public Point worldToCellSpace(int mousex, int mousey){
		return new Point((mousex + viewPosition.x)/CELL_SIZE,(mousey + viewPosition.y)/CELL_SIZE);
	}
	
	public void drawOval(Graphics2D g2, Color c, Point position){
		g2.setColor(c);
		g2.fillOval(position.x-CELL_SIZE, position.y-CELL_SIZE, CELL_SIZE*3, CELL_SIZE*3);
	}
	
	private Color gridColor = new Color(0.3f,0.3f,0.3f,0.5f);
	public void drawGrid(Graphics2D g2){
		g2.setColor(gridColor);
		int numLinesHor = (int)(dimension.getWidth())/CELL_SIZE;
		int numLinesVer = (int)(dimension.getHeight())/CELL_SIZE;
		int dx = viewPosition.x%CELL_SIZE;
		int dy = viewPosition.y%CELL_SIZE;
		for (int x = -1; x < numLinesHor; x++){	
			g2.drawLine(x*CELL_SIZE-dx, -dy-CELL_SIZE, x*CELL_SIZE-dx, dimension.height-dy);	
		}
		for (int y = 0; y < numLinesVer; y++){
			g2.drawLine(-dx-CELL_SIZE, y*CELL_SIZE-dy, dimension.width - dx, y*CELL_SIZE - dy);
		}	
	}
	

	private int mousex;
	private int mousey;
	private int viewx;
	private int viewy;
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		int mousex = arg0.getX();
		int mousey = arg0.getY();
		Point p = worldToCellSpace(mousex, mousey);
		for (RenderInfoController r : renderControllers){
			r.clickWorldPos(p);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		mousex = e.getX();
		mousey = e.getY();
		viewx = viewPosition.x;
		viewy = viewPosition.y;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		int dx = arg0.getX() - mousex;
		int dy = arg0.getY() - mousey;
		viewPosition.x = viewx - dx;
		viewPosition.y = viewy - dy;
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
