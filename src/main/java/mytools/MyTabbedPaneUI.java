package mytools;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class MyTabbedPaneUI extends BasicTabbedPaneUI {
	private Color[] selectedColorSet;
	private Color[] hoverColorSet;
	private int lastRollOverTab = -1;
	private Color contentColor;
	public static Color PanelColor = new Color(153, 186, 243, 200);

	public static ComponentUI createUI(JComponent c) {
		return new MyTabbedPaneUI();
	}

	private MyTabbedPaneUI() {
		contentColor = new Color(122, 150, 223);
		// 选中色
		selectedColorSet = new Color[] { new Color(175, 192, 235), contentColor };
		// 鼠标悬浮时色
		hoverColorSet = new Color[] { new Color(225, 234, 252),
				new Color(153, 186, 243) };
	}

	protected void installDefaults() {
		super.installDefaults();
		RollOverListener l = new RollOverListener();
		tabPane.addMouseMotionListener(l);

		tabInsets = new Insets(2, 10, 2, 10);
		// 选中tab外占边框
		selectedTabPadInsets = new Insets(2, 2, 2, 2);
		// tab色insets边框
		tabAreaInsets = new Insets(3, 10, 0, 0);
		// 内部面板边距
		contentBorderInsets = new Insets(2, 0, 0, 0);
	}

	protected boolean scrollableTabLayoutEnabled() {
		return false;
	}

	protected void paintTabBackground(Graphics g, int tabPlacement,
									  int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		Graphics2D g2d = (Graphics2D) g.create();
		Rectangle rect = rects[tabIndex];
		int width = rect.width;
		int xpos = rect.x;
		int ypos = rect.y;
		if (tabIndex > -1) {
			width -= 4;
			xpos += 2;
		}
		Color[] colorSet;
		if (isSelected) {
			colorSet = selectedColorSet;
			h -= ypos;
		} else if (getRolloverTab() == tabIndex) {
			colorSet = hoverColorSet;
		} else {
			return;
		}
		g2d.setPaint(new GradientPaint(0, ypos, colorSet[0], 0, h, colorSet[1]));
		g2d.fillRect(xpos, ypos, width, h);
		g2d.dispose();
	}

	protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
		int width = tabPane.getWidth();
		Insets insets = tabPane.getInsets();

		int x = insets.left;
		int y = insets.top
				+ calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
		int w = width - insets.right - insets.left;
		int h = contentBorderInsets.top;

		if (tabPane.getTabCount() > 0) {
			Graphics g2 = g.create();
			g2.setColor(contentColor);
			g2.fillRect(x, y, w, h);
			g2.setColor(new Color(253, 253, 253));
			g2.fillRect(x, y + h, w, tabPane.getHeight() - y - h);
			g2.dispose();

		}
		super.paintTabArea(g, tabPlacement, selectedIndex);
	}

	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
								  int x, int y, int w, int h, boolean isSelected) {
	}

	protected void paintContentBorder(Graphics g, int tabPlacement,
									  int selectedIndex) {

	}

	protected void paintFocusIndicator(Graphics g, int tabPlacement,
									   Rectangle[] rects, int tabIndex, Rectangle iconRect,
									   Rectangle textRect, boolean isSelected) {
		// Do nothing
	}

	private class RollOverListener implements MouseMotionListener {
		public void mouseDragged(MouseEvent e) {
		}

		public void mouseMoved(MouseEvent e) {
			int currentRollOver = getRolloverTab();

			// 鼠标从非选项部分移至选中选项
			if (lastRollOverTab == -1
					&& currentRollOver == tabPane.getSelectedIndex()) {
				lastRollOverTab = currentRollOver;
				return;
			}

			// 鼠标从选中选项移至非选项部分
			if (lastRollOverTab == tabPane.getSelectedIndex()
					&& currentRollOver == -1) {
				lastRollOverTab = currentRollOver;
				return;
			}

			if (currentRollOver != lastRollOverTab) {
				lastRollOverTab = currentRollOver;
				tabPane.repaint();
			}
		}
	}
}