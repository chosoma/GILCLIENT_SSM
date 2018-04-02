package mytools;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import sun.swing.DefaultLookup;

public class MyTCR_CheckBox extends JCheckBox implements TableCellRenderer {

    public MyTCR_CheckBox() {
        setBorderPainted(true);
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    private boolean isSelected = false;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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

//        this.setSelected((Boolean) value);

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
