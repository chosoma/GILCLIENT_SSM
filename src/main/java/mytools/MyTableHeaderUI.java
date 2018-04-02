package mytools;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class MyTableHeaderUI extends BasicTableHeaderUI {

	public static ComponentUI createUI(JComponent h) {
		return new MyTableHeaderUI();
	}

	private int oldColumn = -1, newColumn = -1;

	@Override
	protected void rolloverColumnUpdated(int oldColumn, int newColumn) {
		this.oldColumn = oldColumn;
		this.newColumn = newColumn;
		header.repaint();
		// header.repaint(header.getHeaderRect(oldColumn));
		// header.repaint(header.getHeaderRect(newColumn));
	}

	static Paint paint0 = new GradientPaint(0, 0, new Color(249, 252, 253), 0,
			MyUtil.HeadHeight - 1, new Color(211, 220, 233));
	static Paint paint1 = new GradientPaint(0, 0, new Color(223, 226, 228), 0,
			MyUtil.HeadHeight - 1, new Color(189, 198, 210));
	static Color colorLine = new Color(158, 182, 206);

	public void paint(Graphics g, JComponent c) {
		Graphics2D g2 = (Graphics2D) g.create();
		TableColumnModel columnModel = header.getColumnModel();
		// 表头背景色
		g2.setPaint(paint0);
		g2.fillRect(0, 0, header.getWidth(), header.getHeight() - 1);
		// 表头横线
		// g2.setColor(new Color(104, 140, 175));
		// g2.drawLine(0, 0, header.getWidth(), 0);
		// 表头网格线
		g2.setColor(colorLine);
		g2.drawLine(0, header.getHeight() - 1, header.getWidth() - 1,
				header.getHeight() - 1);// 底线
		// g2.drawLine(0, 1, 0, header.getHeight() - 1);// 左边第一条线
		TableColumn aColumn;
		int columnOff = -1;
		// 竖线
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			aColumn = columnModel.getColumn(i);
			columnOff += aColumn.getWidth();
			g2.drawLine(columnOff, 0, columnOff, header.getHeight() - 1);
		}
		if (newColumn > -1) {
			Rectangle clip = header.getHeaderRect(newColumn);
			g2.translate(clip.x, clip.y);
			int h = clip.height;
			int w = clip.width;
			// 表头横线
			// g2.setColor(new Color(104, 140, 175));
			// g2.drawLine(0, 0, w - 1, 0);
			// 表头网格线
			g2.setColor(new Color(158, 182, 206));
			g2.drawLine(0, h - 1, w - 1, h - 1);
			g2.drawLine(w - 1, 0, w - 1, h - 1);
			// 背景色
			g2.setPaint(paint1);
			g2.fillRect(0, 0, w - 1, h - 2);
			g2.translate(-clip.x, -clip.y);
		}

		g2.dispose();
		super.paint(g, c);
	}

}
