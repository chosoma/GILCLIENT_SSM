package mytools.menu;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuUI;

public class MyMenuUI extends BasicMenuUI {

	public static ComponentUI createUI(JComponent c) {
		return new MyMenuUI();
	}

	private Color c1 = new Color(225, 235, 250), c2 = new Color(155, 185, 245);
	private Color borderColor = new Color(208, 215, 229);

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		// 设为透明,这样才会更新菜单低下的内容(这个请了解Opaque的作用)
		menuItem.setOpaque(false);
		menuItem.setBorder(null);
	}

	/*
	 * 重写了背景的绘制方法,不管Opaque的属性,只按不透明方式画菜单背景
	 */
	@Override
	protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
		Graphics2D g2 = (Graphics2D) g.create();
		ButtonModel model = menuItem.getModel();
		int menuWidth = menuItem.getWidth();
		int menuHeight = menuItem.getHeight();
		if (model.isArmed()
				|| (menuItem instanceof JMenu && model.isSelected())) {
			g2.setPaint(new GradientPaint(0, 0, c1, 0, menuHeight, c2));
			g2.fillRect(2, 2, menuWidth, menuHeight);
			g2.setColor(borderColor);
			g2.drawRect(1, 1, menuWidth - 2, menuHeight - 2);
		} else {
			g2.setPaint(new GradientPaint(0, 0, c1, 0, menuHeight, c2));
			g2.fillRect(2, 2, menuWidth, menuHeight);
		}
		g2.dispose();
	}

}
