package mytools;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;

public class ChangeButton extends JButton {
	private Insets insets = new Insets(1, 1, 3, 3);

	public ChangeButton() {
		super();
	}

	public ChangeButton(String text) {
		super(text, null);
	}

	public ChangeButton(Icon icon) {
		super(null, icon);
	}

	public ChangeButton(String text, Icon icon) {
		super(text, icon);
	}

	public Insets getInsets() {
		return insets;
	}

	protected void paintComponent(Graphics g) {
		if (isEnabled()) {
			if (model.isRollover()) {
				int width = getWidth();
				int height = getHeight();
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setComposite(MyUtil.AlphaComposite_100);
				if (model.isPressed()) {
					g2.setColor(new Color(221,221,221));
					g2.fillRect(insets.left, insets.top, width - insets.left
							- insets.right, height - insets.top - insets.bottom);
				} else {
					g2.setColor(Color.WHITE);
					g2.drawLine(insets.left, insets.top, width - insets.right
							- 1, insets.top);
					g2.drawLine(insets.left, insets.top + 1, insets.left,
							height - insets.bottom - 1);
					g2.setPaint(new GradientPaint(insets.left, insets.top + 1,
							new Color(249, 249, 249), insets.left, height
							- insets.bottom - 1, new Color(211, 211,
							211)));
					g2.fillRect(insets.left + 1, insets.top + 1, width
							- insets.left - insets.right - 1, height
							- insets.top - insets.bottom - 1);
				}
				MyUtil.DrawBorder(g2, new Color(106, 106, 106), width, height);
				g2.dispose();
			}
		}
		super.paintComponent(g);
	}

	public boolean isFocusable() {
		return false;
	}

	// 设置按钮按下后无虚线框
	public boolean isFocusPainted() {
		return false;
	}

	// 取消绘制按钮内容区域
	public boolean isContentAreaFilled() {
		return false;
	}

	// 文字位置
	// public int getVerticalTextPosition() {
	// return SwingConstants.BOTTOM;
	// }
	//
	// public int getHorizontalTextPosition() {
	// return SwingConstants.CENTER;
	// }

	// public Color getForeground() {
	// return Color.WHITE;
	// }

	public boolean isBorderPainted() {
		return false;
	}
}
