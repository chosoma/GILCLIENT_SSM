package view;

import java.awt.*;

import javax.swing.JPanel;

public class BackGroundPanel extends JPanel {

    private Image image;

    BackGroundPanel(Image image) {
        this();
        this.image = image;
    }

    private BackGroundPanel() {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setBackground( new Color(179, 211, 240));
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        g2.dispose();
        super.paintComponent(g);
    }

}
