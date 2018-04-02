package view.homePage;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class GlassPanel extends JPanel {

    float alpha = 0.35f;

    public GlassPanel() {
        this.init();
    }

    public GlassPanel(float alpha) {
        this.alpha = alpha;
        this.init();
    }

    public GlassPanel(LayoutManager layout) {
        super(layout);
        this.init();
    }

    public GlassPanel(LayoutManager layout, float alpha) {
        super(layout);
        this.alpha = alpha;
        this.init();
    }

    private void init() {
        this.setOpaque(false);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        Insets insets = getInsets();
        int x = insets.left, y = insets.top, w = getWidth() - x - insets.right, h = getHeight() - y - insets.bottom;
        g2.setPaint(new GradientPaint(x, y, new Color(255, 255, 255, 150), w, h, new Color(255, 255, 255, 250)));
        // g2.setColor(Color.WHITE);
        g2.fillRect(x, y, w, h);
        g2.setColor(Color.WHITE);
        g2.drawLine(x, y, w, y);
        g2.dispose();
        super.paintComponent(g);
    }
}
