import java.awt.*;

public class Floor {
	public int x;
	public int y;
	public int width;
	public int height;
	public Color color;
	
	public Floor(int x, int y, int width, int height, Color color){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	public void paintComponent(Graphics g){
		g.setColor(this.color);
		g.fillRect(x, y, width, height);
	}
}