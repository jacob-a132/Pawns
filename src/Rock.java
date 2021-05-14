import java.awt.*;

public class Rock extends Guy {
	int death;
	String side;
	
	public Rock(int x, int y, int size, int speed, int damage, String side){
		super(x,y,size,speed,0);
		this.health = 1;
		this.damage = damage;
		this.canJump = false;
		this.fallSpeed = -1;
		this.death = 0;
		this.side = side;
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.gray);
		g.fillOval(x, y, size, size);
	}
	public void destroy(Graphics g){
		g.setColor(Color.gray);
		this.updateCentre();
		g.fillArc(x, centre[1], size, size/2, 0, -180);
	}
}
