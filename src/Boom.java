import java.awt.Color;
import java.awt.Graphics;

public class Boom extends Guy {
	public int death;
	public String side;

	public Boom(int x, int y, int size, String side) {
		super(x, y, size, 0, 0);
		this.death = 0;
		this.side = side;
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.orange);
		g.fillOval(x, y, size, size);
		
		g.setColor(Color.red);
		g.fillOval(x+size/10, y+size/10, size*4/5, size*4/5);
	}
}
