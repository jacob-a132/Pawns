import java.awt.*;
import javax.swing.*;

public class Main {
	
	public Main(){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Loop());
		int p = 800;
		int screenX = p*8/5;
		int screenY = p;
		frame.setPreferredSize(new Dimension(screenX, screenY));
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String args[]){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				@SuppressWarnings("unused")
				Main game = new Main();
			}
		});
	}
}
