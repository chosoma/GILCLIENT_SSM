package view.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

public class OneTouchIconH implements Icon, Serializable {
	private Dimension buttonSize;

	public OneTouchIconH(Dimension buttonSize) {
		if (buttonSize != null) {
			this.buttonSize = buttonSize;
		} else {
			this.buttonSize = new Dimension(7, 63);
		}
	}

	/**
	 * Paints the horizontal bars for the
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {

		if (c.isEnabled()) {
			Graphics g2 = g.create();
			g2.translate(x, y);
			int StartOff = getIconHeight() / 2;
			int arrowWidth = getIconWidth() - 2;
			g2.setColor(new Color(193, 213, 248));
			g2.fillRect(0, 0, getIconWidth(), getIconHeight());
			ButtonModel model = ((JButton) c).getModel();
			if (model.isRollover()) {
				if (model.isPressed()) {
					g2.setColor(Color.DARK_GRAY);
				} else {
					g2.setColor(new Color(253, 253, 253));
				}
			} else {
				g2.setColor(new Color(242, 242, 242));
			}
			if (model.isSelected()) {// rightArrow
				for (int i = 0; i < arrowWidth; i++) {
					g2.drawLine(arrowWidth - i, StartOff - i, arrowWidth - i,
							StartOff + i);
				}
			} else {// leftArrow
				for (int i = 0; i < arrowWidth; i++) {
					g2.drawLine(i + 1, StartOff - i, i + 1, StartOff + i);
				}
			}
			g2.dispose();
		}
	}

	/**
	 * Created a stub to satisfy the interface.
	 */
	public int getIconWidth() {
		return buttonSize.width;
	}

	/**
	 * Created a stub to satisfy the interface.
	 */
	public int getIconHeight() {
		return buttonSize.height;
	}

}
