package view.icon;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class VariIcon implements Icon, Serializable {
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        if (c.isEnabled()) {
            g2.drawImage(MyIconFactory.vari, 0, 0, 100, 100, c);
        }
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return 100;
    }

    @Override
    public int getIconHeight() {
        return 100;
    }
}
