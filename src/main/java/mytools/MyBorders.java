package mytools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders;

public class MyBorders {

	public static class ButtonBorder extends AbstractBorder implements
			UIResource {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5555555555555555555L;
		protected static Insets borderInsets = new Insets(2, 2, 3, 3);

		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.translate(x, y);
			if (c.isEnabled()) {
				g2.setColor(new Color(32, 100, 202));
			} else {
				g2.setColor(MyUtil.InactiveControlTextColor);
			}
			g2.drawRect(1, 1, width - 4, height - 4);
			g2.setColor(MyUtil.Shadow);

			g2.setComposite(MyUtil.AlphaComposite_100.derive(0.5f));// dark
			g2.drawLine(3, height - 2, width - 3, height - 2);// HORIZONTAL
			g2.drawLine(width - 2, 3, width - 2, height - 3);// VERTICAL
			g2.setComposite(MyUtil.AlphaComposite_100.derive(0.3f));// light
			g2.drawLine(3, height - 1, width - 3, height - 1);// HORIZONTAL
			g2.drawLine(width - 1, 3, width - 1, height - 3);// VERTICAL
			// conner-dark
			g2.drawLine(width - 2, 2, width - 2, 2);// rightTop
			g2.drawLine(width - 2, height - 2, width - 2, height - 2);// rightBottom
			g2.drawLine(2, height - 2, 1, height - 2);// leftBottom
			// conner-light
			g2.setComposite(MyUtil.AlphaComposite_100.derive(0.2f));
			g2.drawLine(width - 2, 1, width - 1, 2);// rightTop
			g2.drawLine(width - 1, height - 2, width - 2, height - 1);// rightBottom
			g2.drawLine(2, height - 1, 1, height - 2);// leftBottom
			// conner
			g2.setComposite(MyUtil.AlphaComposite_100.derive(0.08f));
			g2.drawLine(width - 1, 1, width - 1, 1);// rightTop
			g2.drawLine(width - 1, height - 1, width - 1, height - 1);// rightBottom
			g2.drawLine(1, height - 1, 1, height - 1);// leftBottom
			g2.dispose();
		}

		public Insets getBorderInsets(Component c) {
			return borderInsets;
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			return borderInsets;
		}

	}

	private static Border buttonBorder;

	public static Border getButtonBorder() {
		if (buttonBorder == null) {
			buttonBorder = new BorderUIResource.CompoundBorderUIResource(
					new ButtonBorder(),
					new BasicBorders.MarginBorder());
		}
		return buttonBorder;
	}

	protected static Insets TabelHeadBorderInsets = new Insets(1, 0, 1, 1);

	static class TableHeadBorder extends AbstractBorder implements UIResource {

		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			int w = width - 1;
			int h = height - 1;
			g.setColor(UIManager.getColor("Table.gridColor"));
			g.drawLine(x, y, w, x);
			g.drawLine(x, h, w, h);
			g.drawLine(w, y, w, h);

		}

		public Insets getBorderInsets(Component c) {
			return TabelHeadBorderInsets;
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			return TabelHeadBorderInsets;
		}

	}

	private static Border TableHeadBorder;

	public static Border getTableHeadBorder() {
		if (TableHeadBorder == null) {
			TableHeadBorder = new BorderUIResource.CompoundBorderUIResource(
					new TableHeadBorder(),
					new BasicBorders.MarginBorder());
		}
		return TableHeadBorder;
	}

}
