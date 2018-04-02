package mytools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicGraphicsUtils;

import com.sun.awt.AWTUtilities;

/**
 * 窗体拖动时显示的虚线框
 *
 * @author black
 *
 */
public class MyDashedBorder extends JDialog {

	public MyDashedBorder() {
		// 去除JDialog边框
		this.setUndecorated(true);
		// 设置JDialog背景透明
		AWTUtilities.setWindowOpaque(this, false);
		JLabel jlb = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(Color.GRAY);
				BasicGraphicsUtils.drawDashedRect(g, 0, 0, this.getWidth(),
						this.getHeight());
				BasicGraphicsUtils.drawDashedRect(g, 1, 1, this.getWidth() - 2,
						this.getHeight() - 2);
				BasicGraphicsUtils.drawDashedRect(g, 2, 2, this.getWidth() - 4,
						this.getHeight() - 4);
			}

		};
		this.getContentPane().add(jlb);
	}

}
