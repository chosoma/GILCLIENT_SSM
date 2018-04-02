package mytools.mySplitPane;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

import sun.swing.DefaultLookup;
import view.icon.OneTouchIconH;

public class MySplitPaneDivider extends BasicSplitPaneDivider {

	private int oneTouchSize;
	private int oneTouchOffset;
	protected JButton button;
	private Dimension buttonSize = new Dimension(7, 63);

	/**
	 * If true the one touch buttons are centered on the divider.
	 */

	public MySplitPaneDivider(MySplitPaneUI ui) {
		super(ui);
		// oneTouchSize = DefaultLookup.getInt(ui.getSplitPane(), ui,
		// "SplitPane.oneTouchButtonSize", ONE_TOUCH_SIZE);
		oneTouchSize = buttonSize.width;
		oneTouchOffset = DefaultLookup.getInt(ui.getSplitPane(), ui,
				"SplitPane.oneTouchButtonOffset", ONE_TOUCH_OFFSET);
		setLayout(new DividerLayout());
		if (splitPane.isOpaque()) {
			splitPane.setOpaque(true);
			setBackground(UIManager.getColor("SplitPane.background"));
		} else {
			splitPane.setOpaque(false);
		}
		// setBackground(UIManager.getColor("SplitPane.background"));
	}

	protected void oneTouchExpandableChanged() {
		if (!DefaultLookup.getBoolean(splitPane, splitPaneUI,
				"SplitPane.supportsOneTouchButtons", true)) {
			return;
		}
		if (splitPane.isOneTouchExpandable() && button == null) {
			button = new JButton(new OneTouchIconH(buttonSize));
			button.setToolTipText("隐藏");
			button.setFocusable(false);
			// 无边框
			button.setBorder(null);
			// 取消绘制按钮内容区域
			button.setContentAreaFilled(false);
			// 设置按钮按下后无虚线框
			button.setFocusPainted(false);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean isLeft = button.isSelected();
					if (isLeft) {
						rightButton.doClick();
						button.setToolTipText("隐藏");
					} else {
						leftButton.doClick();
						button.setToolTipText("显示");
					}
					button.setSelected(!isLeft);
				}
			});
			add(button);
		}
		super.oneTouchExpandableChanged();

	}

	protected class DividerLayout implements LayoutManager {
		public Dimension minimumLayoutSize(Container parent) {
			// NOTE: This isn't really used, refer to
			// BasicSplitPaneDivider.getPreferredSize for the reason.
			// I leave it in hopes of having this used at some point.
			if (parent != MySplitPaneDivider.this || splitPane == null) {
				return new Dimension(0, 0);
			}
			Dimension buttonMinSize = null;

			if (splitPane.isOneTouchExpandable() && leftButton != null) {
				buttonMinSize = leftButton.getMinimumSize();
			}

			Insets insets = getInsets();
			int width = getDividerSize();
			int height = width;

			if (orientation == JSplitPane.VERTICAL_SPLIT) {
				if (buttonMinSize != null) {
					int size = buttonMinSize.height;
					if (insets != null) {
						size += insets.top + insets.bottom;
					}
					height = Math.max(height, size);
				}
				width = 1;
			} else {
				if (buttonMinSize != null) {
					int size = buttonMinSize.width;
					if (insets != null) {
						size += insets.left + insets.right;
					}
					width = Math.max(width, size);
				}
				height = 1;
			}

			return new Dimension(width, height);
		}

		public void layoutContainer(Container parent) {

			if (button != null && parent == MySplitPaneDivider.this) {
				if (splitPane.isOneTouchExpandable()) {
					Insets insets = getInsets();

					if (orientation == JSplitPane.VERTICAL_SPLIT) {
						int extraX = (insets != null) ? insets.left : 0;
						int blockSize = getHeight();

						if (insets != null) {
							blockSize -= (insets.top + insets.bottom);
							blockSize = Math.max(blockSize, 0);
						}
						blockSize = Math.min(blockSize, oneTouchSize);

						int y = (parent.getSize().height - blockSize) / 2;

						extraX = parent.getSize().width / 2 - blockSize;

						leftButton.setBounds(extraX + oneTouchOffset, y,
								blockSize * 2, blockSize);
						rightButton
								.setBounds(extraX + oneTouchOffset
												+ oneTouchSize * 2, y, blockSize * 2,
										blockSize);
					} else {
						int extraY = (insets != null) ? insets.top : 0;
						int blockSize = getWidth();

						if (insets != null) {
							blockSize -= (insets.left + insets.right);
							blockSize = Math.max(blockSize, 0);
						}
						blockSize = Math.min(blockSize, oneTouchSize);

						int x = (parent.getSize().width - blockSize) / 2;

						extraY = parent.getSize().height / 2
								- buttonSize.height;
						button.setBounds(x, extraY + oneTouchOffset,
								buttonSize.width, buttonSize.height);
					}
				} else {
					leftButton.setBounds(-5, -5, 1, 1);
					rightButton.setBounds(-5, -5, 1, 1);
				}
			}
		}

		public Dimension preferredLayoutSize(Container parent) {
			return minimumLayoutSize(parent);
		}

		public void addLayoutComponent(String name, Component comp) {
		}

		public void removeLayoutComponent(Component comp) {
		}
	}

}
