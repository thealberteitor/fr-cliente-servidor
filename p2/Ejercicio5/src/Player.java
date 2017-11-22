public class Player {
	public int pos_x;
	public int pos_y;
	public char letra;

	public Player(int x, int y, char l){
		pos_x=x;
		pos_y=y;
		letra=l;
	}

	public void Derecha(){
		if(pos_y!=19)	pos_y++;
	}

	public void Izquierda(){
		if(pos_y!=0)	pos_y--;
	}
	
	public void Abajo(){
		if(pos_x!=19)	pos_x++;
	}

	public void Arriba(){
		if(pos_x!=0)	pos_x--;
	}
	
	public double distancia(Player otro){
		double resta_x=otro.pos_x-pos_x;
	    	double resta_y=otro.pos_y-pos_y;
		return Math.sqrt(resta_x*resta_x+resta_y*resta_y);
	}
}	
