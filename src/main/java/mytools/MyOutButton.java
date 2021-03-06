package mytools;

import view.Shell;

import javax.swing.*;
import java.awt.*;

public class MyOutButton extends JButton {

    private int type;

    public MyOutButton() {
        super();
    }

    public MyOutButton(Icon icon) {
        super(icon);
    }

    public MyOutButton(Icon icon, boolean selected) {
        super(icon);
        model.setSelected(selected);
    }

    public MyOutButton(String text) {
        super(text);
    }

    public MyOutButton(String text, boolean selected) {
        super(text);
        model.setSelected(selected);
    }

    public int getType() {
        return type;
    }

    public MyOutButton(String text, Icon icon, int type) {
        super(text, icon);
        this.type = type;
        this.addActionListener(Shell.getInstance());
        this.setBorder(BorderFactory.createBevelBorder(0));
    }

    public MyOutButton(String text, Icon icon, boolean selected) {
        super(text, icon);
        model.setSelected(selected);
    }

    public boolean isSelected() {
        return model.isSelected();
    }

    public void setSelected(boolean selected) {
        model.setSelected(selected);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();
        Color[] colors = null;
        float alpha = 1.0f;
        if (model.isSelected()) {
            colors = MyUtil.SelectColors;// 选中色
            alpha = 0.6f;
        } else if (model.isPressed()) {
            colors = MyUtil.PressColors;// 鼠标按下时色
            alpha = 0.2f;
        } else if (model.isRollover()) {
            colors = MyUtil.HoverColors;// 鼠标悬浮时色
            alpha = 0.4f;
            g.translate(-1, -1);
        }
        if (colors != null) {
            AlphaComposite composite = AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, alpha);
            g2.setComposite(composite);
            int w = getWidth(), h = getHeight(), off = h * 2 / 5;
            g2.setColor(MyUtil.BUTTON_OUTER_BORDER);// 外边框颜色
            g2.drawRect(0, 0, w - 1, h - 1);

            g2.setColor(MyUtil.BUTTON_INNER_BORDER);// 内边框颜色
            g2.drawRect(1, 1, w - 3, h - 3);

            g2.setPaint(new GradientPaint(0, 2, colors[0], 0, off, colors[1]));
            g2.fillRect(2, 2, w - 4, off - 2);
            g2.setPaint(new GradientPaint(0, off, colors[2], 0, h - 2,
                    colors[3]));
            g2.fillRect(2, off, w - 4, h - off - 2);
        }
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 120);
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
    public int getVerticalTextPosition() {
        return SwingConstants.BOTTOM;
    }

    public int getHorizontalTextPosition() {
        return SwingConstants.CENTER;
    }

    public Color getForeground() {
        return Color.BLUE;
    }

    public Font getFont() {
        return MyUtil.TitleFont30;
    }

    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }

    public boolean isBorderPainted() {
        return true;
    }
}
