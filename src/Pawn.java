import java.awt.Color;
import java.awt.Graphics;

public class Pawn extends Guy {
	public String side;
	public boolean dying;
	
	public Pawn(int x, int y, int size, int speed, int damage, int health, String side) {
		super(x, y, size, speed, 0);
		this.side = side;
		this.dying = false;
		this.damage = damage;
		this.maxHealth = health;
		this.health = health;
	}
	
	public void walk(){
		if(side == "good"){
			this.x += this.speed;
		}
		else{
			this.x -= this.speed;
		}
	}
	
	public void paintComponent(Graphics g){
		if(side == "good"){
			Color purple =  new Color(100, 20, 240);
			super.paintComponent(g, purple);
		}
		else{
			Color pink =  new Color(255, 130, 130);
			super.paintComponent(g, pink);
		}
	}

}
