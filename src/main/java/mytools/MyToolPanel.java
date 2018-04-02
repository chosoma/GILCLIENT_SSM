package mytools;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class MyToolPanel extends JPanel {
	private Color c1, c2;
	static Color DefaultC1 = new Color(251, 251, 251), DefaultC2 = new Color(
			235, 235, 235);

	public MyToolPanel() {
		super();
		init();
	}

	public MyToolPanel(LayoutManager layout) {
		super(layout);
		init();
	}

	public MyToolPanel(Color c1, Color c2) {
		super();
		this.c1 = c1;
		this.c2 = c2;
		init();
	}

	public MyToolPanel(Color c1, Color c2, LayoutManager layout) {
		super(layout);
		this.c1 = c1;
		this.c2 = c2;
		init();
	}

	private void init() {
		this.setOpaque(false);// 很必要
		if (c1 == null) {
			c1 = DefaultC1;
		}
		if (c2 == null) {
			c2 = DefaultC2;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int h = getHeight(), w = getWidth();
		g2.setPaint(new GradientPaint(0, 0, c1, 0, h, c2));
		g2.fillRect(0, 0, w, h);
		g2.setColor(Color.WHITE);
		g2.setComposite(MyUtil.AlphaComposite_50F);
		g2.drawLine(0, 0, w, 0);
		g2.setColor(Color.DARK_GRAY);
		g2.setComposite(MyUtil.AlphaComposite_30F);
		g2.drawLine(0, h - 1, w, h - 1);
		g2.dispose();
		super.paintComponent(g);
	}

}
