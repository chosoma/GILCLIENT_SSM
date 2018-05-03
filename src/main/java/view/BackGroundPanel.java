package view;

import java.awt.*;

import javax.swing.JPanel;

public class BackGroundPanel extends JPanel {

	private Image image;
	private Image logo;

	BackGroundPanel(Image image, Image logo) {
		this();
		this.image = image;
		this.logo = logo;
	}

	private BackGroundPanel() {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());
		this.setBackground(new Color(179, 211, 240));
	}

	public void paintComponent(Graphics g) {
//		System.out.println(getWidth()+":"+getHeight());
		int height = 100;
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setStroke(new BasicStroke(9f, 0, 0));
		g2.setColor(Color.lightGray);
//		for (int i = 0; i < height; i++) {
//			g2.drawLine(0, i, getWidth(), i);
//		}
		g2.drawImage(image, 0, 80, getWidth(), getHeight()-80, this);
		g2.drawImage(logo, getWidth() - 301, 0, 301, 80, this);
		g2.dispose();
		super.paintComponent(g);
	}

}
