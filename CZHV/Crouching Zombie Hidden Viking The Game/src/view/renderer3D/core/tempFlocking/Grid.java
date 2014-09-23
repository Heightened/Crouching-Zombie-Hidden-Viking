package view.renderer3D.core.tempFlocking;

public class Grid {
	int[][] pointers;
	Vehicle[][][] grid;
	int sizex;
	int sizey;
	
	public Grid(int sizex, int sizey){
		this.sizex = sizex;
		this.sizey = sizey;
		pointers = new int[sizex][sizey];
		grid = new Vehicle[sizex][sizey][40];
	}
	
	public void addVehicle(Vehicle v){
		grid[v.gridx][v.gridy][pointers[v.gridx][v.gridy]] = v;
		pointers[v.gridx][v.gridy] = (pointers[v.gridx][v.gridy]+1)%40;
	}
	
	public Vehicle[] getArray(int x, int y){
		if (x >= 0 && y >= 0){
			return grid[x][y];
		}else{
			return grid[0][0];
		}
	}
	
	public int getSize(int x, int y){
		if (x >= 0 && y >= 0){
			return pointers[x][y];
		}else {
			return 0;
		}
	}
	
	public void reset(){
		for (int i = 0; i < sizex; i++){
			for (int j = 0; j < sizey; j++){
				pointers[i][j] = 0;
			}
		}
	}
}
