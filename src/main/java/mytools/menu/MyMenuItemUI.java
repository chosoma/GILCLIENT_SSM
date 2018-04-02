package mytools.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

public class MyMenuItemUI extends BasicMenuItemUI {
	public static ComponentUI createUI(JComponent c) {
		return new MyMenuItemUI();
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		// 设为透明,这样才会更新菜单低下的内容(这个请了解Opaque的作用)
		// menuItem.setOpaque(false);
		menuItem.setBorder(null);
	}

	/*
	 * 重写了背景的绘制方法,不管Opaque的属性,只按不透明方式画菜单背景
	 */
	@Override
	protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
		ButtonModel model = menuItem.getModel();
		int menuWidth = menuItem.getWidth();
		int menuHeight = menuItem.getHeight();
		Graphics2D g2 = (Graphics2D) g.create();
		if (model.isArmed()
				|| (menuItem instanceof JMenu && model.isSelected())) {
			g2.setColor(menuItem.getBackground());
			g2.drawRect(0, 0, menuWidth - 1, menuHeight - 1);
			g2.setPaint(new GradientPaint(0, 0, new Color(87, 139, 199), 0,
					menuHeight, new Color(64, 121, 185)));
			g2.fillRect(1, 0, menuWidth - 2, menuHeight);
		} else {
			// AlphaComposite composite = AlphaComposite.getInstance(
			// AlphaComposite.SRC_OVER, 0.95f);
			// g2.setComposite(composite);
			g2.setColor(menuItem.getBackground());
			g2.fillRect(0, 0, menuWidth, menuHeight);
		}
		g2.dispose();
	}

	protected Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon,
												 Icon arrowIcon, int defaultTextIconGap) {
		Dimension dimension = super.getPreferredMenuItemSize(c, checkIcon,
				arrowIcon, defaultTextIconGap);
		dimension.height += 8;
		dimension.width += 2;
		return dimension;
	}
}
