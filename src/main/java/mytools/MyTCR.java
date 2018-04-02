package mytools;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import sun.swing.DefaultLookup;

public class MyTCR extends JLabel implements TableCellRenderer {
	private SimpleDateFormat timeFormat = MyUtil.getDateFormat();
	public MyTCR() {
		super();
		setHorizontalAlignment(SwingConstants.CENTER);
		// setOpaque(true);
	}

	private boolean isSelected = false;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		this.isSelected = isSelected;
		if (isSelected) {
			setForeground(UIManager.getColor("Table.selectionForeground"));
			// setBackground(MyUtil.tableSel_B);
		} else {
			if (row % 2 == 0) {
				setForeground(MyUtil.tableOdd_F);
				setBackground(MyUtil.tableOdd_B);
			} else {
				setForeground(MyUtil.tableEven_F);
				setBackground(MyUtil.tableEven_B);
			}
		}
		setFont(table.getFont());

		if (hasFocus) {
			Border border = null;
			if (isSelected) {
				border = DefaultLookup.getBorder(this, ui,
						"Table.focusSelectedCellHighlightBorder");
			}
			if (border == null) {
				border = DefaultLookup.getBorder(this, ui,
						"Table.focusCellHighlightBorder");
			}
			setBorder(border);

			if (!isSelected && table.isCellEditable(row, column)) {
				Color col;
				col = DefaultLookup.getColor(this, ui,
						"Table.focusCellForeground");
				if (col != null) {
					super.setForeground(col);
				}
				col = DefaultLookup.getColor(this, ui,
						"Table.focusCellBackground");
				if (col != null) {
					super.setBackground(col);
				}
			}
		} else {
			setBorder(BorderFactory.createEmptyBorder());
		}

		String s = null;
		if (value != null) {
			if (value instanceof Timestamp || value instanceof Date) {
				Date t = (Date) value;
				s = timeFormat.format(t);
			} else {
				s = value.toString();
			}
		}
		setText(s);
		return this;
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (isSelected) {
			setOpaque(false);
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setPaint(new GradientPaint(0, 1, MyUtil.tableSel_B[0], 0,
					getHeight() - 1, MyUtil.tableSel_B[1]));
			g2.fillRect(0, 0, getWidth(), getHeight());
			g2.dispose();
		} else {
			setOpaque(true);

		}

		super.paintComponent(g);
	}

}
