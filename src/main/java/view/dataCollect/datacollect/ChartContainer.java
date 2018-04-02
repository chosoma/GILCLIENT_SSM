
package view.dataCollect.datacollect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import mytools.ChartUtil;
import mytools.MyToolPanel;
import mytools.MyUtil;
import domain.DataBean;

/**
 * 实时采集数据图的集合，雨量柱状图，传感器采集折线图继承该类
 */
public abstract class ChartContainer extends JPanel {
    // protected JPanel toolBar;
    private JLabel titleLabel;
    // (237, 240, 249)(146, 157, 200)
    private static Color c1 = new Color(182, 216, 245);
    private static Dimension DEFAULT_Dimension = new Dimension(270, 180);
    private static Border ChartBoder = MyUtil.Component_Border;
    private static Dimension LabelDimension = new Dimension(5, 26);

    void initialize(JPanel chartPanel) {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setBorder(ChartBoder);
        // 实现双击全屏
        // chartPanel.setBackground(new Color(239, 243, 247));
        chartPanel.setPreferredSize(DEFAULT_Dimension);
        this.add(chartPanel, BorderLayout.CENTER);
        JPanel toolBar = new MyToolPanel(new Color(127, 175, 247), new Color(77, 136, 230), new BorderLayout());
        this.add(toolBar, BorderLayout.NORTH);
        titleLabel = new JLabel();
        titleLabel.setFont(MyUtil.FONT_16);
        titleLabel.setForeground(ChartUtil.FontColor);
        // titleLabel.setForeground(Color.WHITE);
        // titleLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        toolBar.add(titleLabel, BorderLayout.CENTER);
        toolBar.add(Box.createRigidArea(LabelDimension), BorderLayout.WEST);
    }

    // 添加数据
    public abstract boolean addData(DataBean data);

    // 清空数据
    public abstract void clearData(String date);

    // 设置折点可见
    public abstract void setSeriesShapesVisible(int series, boolean visible);

    public void setTitle(String title) {
        setTitle(null, title);
    }

    private void setTitle(Icon icon, String title) {
        if (icon != null) {
            titleLabel.setIcon(icon);
        }
        titleLabel.setText(title);
    }

    public abstract boolean matchData(DataBean data);

}
