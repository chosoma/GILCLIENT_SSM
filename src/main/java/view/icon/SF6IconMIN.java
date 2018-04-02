package view.icon;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class SF6IconMIN implements Icon, Serializable {
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        if (c.isEnabled()) {
            g2.drawImage(MyIconFactory.SF6_28, 0, 0, 28, 28, c);
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
