import javax.swing.JFrame;
import java.awt.BorderLayout;

public class Window extends JFrame {

	public Window(String title, DrawingCanvas canvas) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(title);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		this.add(canvas);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}