package view.icon;

import java.awt.*;
import java.io.Serializable;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

public class MinIcon implements Icon, Serializable {

    private Image icon;

    public MinIcon(Image icon) {
        this.icon = icon;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        if (c.isEnabled()) {
            g2.drawImage(icon, 0, 0, 28, 28, c);

        }
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return 28;
    }

    @Override
    public int getIconHeight() {
        return 28;
    }
}
