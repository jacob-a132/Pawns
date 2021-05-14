import java.awt.Color;
import java.awt.Graphics;

public class BadGuy extends Guy{
	int cd;
	int powercd;
	
	public BadGuy(int x, int y, int size, int speed, int jumpHeight) {
		super(x, y, size, speed, jumpHeight);
		this.cd = 15;
		this.powercd = 0;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g, Color.red);
	}
}
