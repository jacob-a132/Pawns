import java.awt.*;

public class Guy {
	public int x;
	public int y;
	public int size;
	public int speed;
	public int jumpHeight;
	public int health;
	public int maxHealth;
	public int damage;
	public int respawn;
	public int home;
	public int deaths;
	public int numThrows;
	public int powersUsed;
	public int healRate;
	public double fallSpeed;
	public boolean alive;
	public boolean canJump;
	public boolean canThrow;
	public boolean powerReady;
	public double throwTime;
	public int[] centre = new int[2];
	
	public Guy(int x, int y, int size, int speed, int jumpHeight){
		this.x = x;
		this.y = y;
		this.size = size;
		this.speed = speed;
		this.jumpHeight = -jumpHeight;
		this.maxHealth = 150;
		this.health = 150;
		this.damage = 0;
		this.fallSpeed = 0;
		this.respawn = 0;
		this.home = 10;
		this.deaths = 0;
		this.numThrows = 0;
		this.alive = true;
		this.canJump = true;
		this.canThrow = true;
		this.throwTime = 0;
		this.healRate = 20;
		this.centre[0] = this.x + (this.size / 2);
		this.centre[1] = this.y + (this.size / 2);
	}
	
	public void paintComponent(Graphics g, Color color){
		g.setColor(color);
		g.fillOval(x, y, size, size);
		
		g.setColor(Color.red);
		g.fillRect(x, y-9*size/20, size, size/4);
		g.setColor(Color.green);
		g.fillRect(x, y-9*size/20, size*health/maxHealth, size/4);
	}
	
	public void updateCentre(){
		this.centre[0] = this.x + (this.size / 2);
		this.centre[1] = this.y + (this.size / 2);
	}
}
