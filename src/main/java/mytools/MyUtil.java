package mytools;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

import sun.swing.SwingUtilities2;

public class MyUtil {

    // 左面板颜色(159,181,210)(238,238,238)
    public static Color[] LEFT_PANE_COLOR = new Color[]{
            new Color(225, 235, 250), new Color(159, 181, 210)};

    // 按钮选中时色数组
    public static Color[] SelectColors = new Color[]{Color.WHITE,
            Color.WHITE, Color.WHITE, Color.WHITE};
    // 按钮按下时色数组
    public static Color[] PressColors = new Color[]{new Color(230, 230, 230),
            new Color(210, 210, 210), new Color(210, 210, 210),
            new Color(230, 230, 230)};
    // 按钮悬浮色数组
    public static Color[] HoverColors = new Color[]{Color.WHITE,
            new Color(240, 240, 240), new Color(220, 220, 220), Color.WHITE};
    // 按钮外边框颜色
    public static Color BUTTON_OUTER_BORDER = new Color(150, 150, 150);
    // 按钮内边框颜色
    public static Color BUTTON_INNER_BORDER = new Color(200, 200, 200);

    // 按钮边框颜色
    public static Color Component_Border_Color = new Color(127, 157, 185);
    // 组件边框
    public static Border Component_Border = BorderFactory.createLineBorder(
            Component_Border_Color, 1);


    private static String Font_Name = "微软雅黑";
    private static int Font_Style = Font.PLAIN;
    public static Font FONT_36 = new Font(Font_Name, Font_Style, 36);
    public static Font FONT_20 = new Font(Font_Name, Font_Style, 20);
    public static Font FONT_18 = new Font(Font_Name, Font_Style, 18);
    public static Font FONT_16 = new Font(Font_Name, Font_Style, 16);
    public static Font FONT_15 = new Font(Font_Name, Font_Style, 15);
    public static Font FONT_14 = new Font(Font_Name, Font_Style, 14);
    public static Font FONT_13 = new Font(Font_Name, Font_Style, 13);
    public static Font FONT_12 = new Font(Font_Name, Font_Style, 12);
    // public static Font FONT_11 = new Font(Font_Name, Font_Style, 11);
    public static Font FONT_10 = new Font(Font_Name, Font_Style, 10);
    public static Font FONT_9 = new Font(Font_Name, Font_Style, 9);

    public static Font TitleFont = new Font(Font_Name, Font_Style, 12);
    public static Font TitleFont30 = new Font(Font_Name, Font_Style, 30);

    /**
     * 表格颜色
     */
    // 选中行背景色
    public static Color[] tableSel_B = new Color[]{new Color(255, 225, 60),
            new Color(255, 200, 30)};
    // 奇数行前景色、背景色
    public static Color tableOdd_F = Color.DARK_GRAY;
    public static Color tableOdd_B = Color.WHITE;
    // 偶数行前景色、背景色
    public static Color tableEven_F = Color.DARK_GRAY;
    public static Color tableEven_B = new Color(240, 248, 253);
    public static int RowHeight = 22;// 表行高
    public static int HeadHeight = 24;// 表头高

    /**
     *
     */
    public static Color InactiveControlTextColor = new Color(153, 153, 153);// 组件不可用时的颜色

    public static String DATA_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String DATA_FORMAT_PATTERN_2 = "yyyy-MM-dd HH:mm";

    public static SimpleDateFormat getDateFormat() {
        return getDateFormat(DATA_FORMAT_PATTERN);
    }

    public static SimpleDateFormat getDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    public static Color Shadow = new Color(120, 120, 120);

    public static void DrawBorder(Graphics2D g2, Color c, int width, int height) {
        g2.setColor(c);
        g2.drawRect(0, 0, width - 3, height - 3);
        DrawShadow(g2, null, 0, 0, width, height);
    }

    public static AlphaComposite AlphaComposite_100 = AlphaComposite.SrcOver;
    public static AlphaComposite AlphaComposite_50F = AlphaComposite.SrcOver.derive(0.5f);
    public static AlphaComposite AlphaComposite_30F = AlphaComposite.SrcOver.derive(0.3f);
    public static AlphaComposite AlphaComposite_20F = AlphaComposite.SrcOver.derive(0.2f);
    public static AlphaComposite AlphaComposite_8F = AlphaComposite.SrcOver.derive(0.08f);

    // 绘制阴影
    public static void DrawShadow(Graphics2D g2, Color c, int x, int y, int w,
                                  int h) {
        Composite composite = g2.getComposite();
        if (c == null) {
            g2.setColor(Shadow);
        } else
            g2.setColor(c);
        // line
        g2.setComposite(AlphaComposite_50F);// dark
        g2.drawLine(2 + x, h - 2, w - 3, h - 2);// HORIZONTAL
        g2.drawLine(w - 2, 2 + y, w - 2, h - 3);// VERTICAL
        g2.setComposite(AlphaComposite_30F);// light
        g2.drawLine(2 + x, h - 1, w - 3, h - 1);// HORIZONTAL
        g2.drawLine(w - 1, 2 + y, w - 1, h - 3);// VERTICAL
        // conner-dark
        g2.drawLine(w - 2, 1 + y, w - 2, 1 + y);// rightTop
        g2.drawLine(w - 2, h - 2, w - 2, h - 2);// rightBottom
        g2.drawLine(1 + x, h - 2, 1 + x, h - 2);// leftBottom
        // conner-light
        g2.setComposite(AlphaComposite_20F);
        g2.drawLine(w - 2, y, w - 1, 1 + y);// rightTop
        g2.drawLine(w - 1, h - 2, w - 2, h - 1);// rightBottom
        g2.drawLine(1 + x, h - 1, x, h - 2);// leftBottom
        // conner
        // g2.setComposite(AC.derive(0.08f));
        // g2.drawLine(w - 1, y, w - 1, y);// rightTop
        // g2.drawLine(w - 1, h - 1, w - 1, h - 1);// rightBottom
        // g2.drawLine(x, h - 1, x, h - 1);// leftBottom
        g2.setComposite(composite);
    }

    public static JTextField CreateTextField() {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        return tf;
    }

    /**
     * 获取图标按钮
     *
     * @param icon
     * @param toolTip
     * @return
     */
    public static JButton CreateButton(Icon icon, String toolTip) {
        JButton bt = new JButton(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                if (model.isPressed()) {
                    g.translate(1, 1);
                }
                super.paintComponent(g);
                if (model.isPressed()) {
                    g.translate(-1, -1);
                }
            }
        };
        bt.setFocusable(false);
        // 无边框
        bt.setBorder(null);
        // 取消绘制按钮内容区域
        bt.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        bt.setFocusPainted(false);
        bt.setToolTipText(toolTip);
        return bt;

    }

    static Date startDate = Timestamp.valueOf("1970-01-01 00:00:00");
    static Date endDate = Timestamp.valueOf("2099-01-01 00:00:00");

    public static JSpinner createSpinner(Date date) {
        return createSpinner(date, MyUtil.DATA_FORMAT_PATTERN_2);
    }

    public static JSpinner createSpinner(Date date, String pattern) {
        SpinnerDateModel datemodel = new SpinnerDateModel(date, startDate,
                endDate, Calendar.SECOND);
        JSpinner jsp = new JSpinner(datemodel);
        DateEditor dateEditor = new DateEditor(jsp, pattern);
        JFormattedTextField dateJTF = dateEditor.getTextField();
        dateJTF.setEditable(false);
        jsp.setEditor(dateEditor);
        return jsp;
    }

    public static JSpinner createNumSpinner(int value, int minimum,
                                            int maximum, int stepSize) {
        JSpinner jsp = new JSpinner(new SpinnerNumberModel(value, minimum,
                maximum, stepSize));
        JFormattedTextField dayJTF = ((JSpinner.DefaultEditor) jsp.getEditor())
                .getTextField();
        dayJTF.setEditable(false);
        return jsp;
    }

    // 获取软件名 默认艺术字体
    public static BufferedImage getArtTitle(String title, Font font) {
        return getArtTitle(title, font, Color.YELLOW, Color.RED, 1,
                Color.DARK_GRAY);
    }

    // 获取元素名 默认艺术字体
    public static BufferedImage getArtElement(String title, Font font) {
        return getArtTitle(title, font, Color.RED, null, 1, Color.BLACK);
    }

    public static BufferedImage getArtTitle(String title, Font font, Color c1,
                                            Color c2, int strokeWidth, Color strokeC) {
        GlyphVector v = font.createGlyphVector(
                SwingUtilities2.getFontMetrics(null, font)
                        .getFontRenderContext(), title);
        Shape shape = v.getOutline();
        Rectangle bounds = shape.getBounds();
        int w = bounds.x + bounds.width + strokeWidth * 2 + 2;
        int h = bounds.height + strokeWidth * 2 + 2;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.translate(-bounds.x + 2, -bounds.y + 2);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if (c1 == null) {
            c1 = Color.YELLOW;
        }
        if (c2 != null) {
            g2d.setPaint(new GradientPaint(0, 0, c1, bounds.width,
                    bounds.height, c2));
        } else {
            g2d.setColor(c1);
        }
        g2d.fill(shape);
        if (strokeWidth > 0) {
            g2d.setColor(strokeC);
            g2d.setStroke(new BasicStroke(strokeWidth));
            g2d.draw(shape);
        }
        g2d.dispose();
        return img;
    }
}