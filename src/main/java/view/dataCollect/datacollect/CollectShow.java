package view.dataCollect.datacollect;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import domain.HitchBean;
import domain.WarnBean;
import mytools.MyUtil;

import domain.DataBean;
import view.dataCollect.WarnPanel;

public class CollectShow extends JPanel {

    private static CollectShow CS;
    private GridLayout layout;

    private JPanel warnPanel;
    private JScrollPane jspwarn;


    private CollectShow() {
        this.init();
    }

    public static CollectShow getInstance() {
        if (CS == null) {
            synchronized (CollectShow.class) {
                if (CS == null) {
                    CS = new CollectShow();
                }
            }
        }
        return CS;
    }


    public void init() {
        layout = new GridLayout(0, 1);
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.loadView();
    }


    private ChartView chartView;

    /**
     * 初始化数据采集显示界面
     */
    private void loadView() {

        chartView = ChartView.getInstance();
        this.add(chartView, BorderLayout.CENTER);

        warnPanel = new JPanel(new BorderLayout());
        warnPanel.setBorder(new TitledBorder(new EmptyBorder(1, 1, 1, 1), "警报区", TitledBorder.CENTER, TitledBorder.TOP, MyUtil.FONT_20, Color.white));
        warnPanel.setOpaque(true);
        warnPanel.setVisible(true);
        warnPanel.setBackground(new Color(255, 80, 0));
        jspwarn = new JScrollPane(warnPanel);
        jspwarn.setPreferredSize(new Dimension(100, 120));
        this.add(jspwarn, BorderLayout.SOUTH);

    }

    private List<WarnPanel> warnPanels = new ArrayList<>();


    private synchronized WarnPanel getWarn(WarnBean warnBean) {
        for (WarnPanel warn : warnPanels) {
            if (warnBean.equals(warn.getWarnBean())) {
                return warn;
            }
        }
        return null;
    }

    private void removeWarn(WarnPanel warnPanel) {
        this.warnPanel.remove(warnPanel);
        warnPanels.remove(warnPanel);
    }


    public synchronized void addWarning(WarnBean warnBean) {
        WarnPanel warn = getWarn(warnBean);
        if (warn != null) {
            warn.resetWarn(warnBean);
        } else {
            warn = new WarnPanel(warnBean);
            this.warnPanels.add(warn);
            this.layout.setRows(this.layout.getRows() + 1);
            this.warnPanel.setLayout(new GridLayout(layout.getRows(), 1));
            this.warnPanel.add(warn, 0);
        }
        repaintPanel();
    }

    public synchronized void removeWarning(WarnPanel warnPanel) {
        removeWarn(warnPanel);
        this.layout.setRows(this.layout.getRows() - 1);
        this.warnPanel.setLayout(new GridLayout(layout.getRows(), 1));
        repaintPanel();
    }

    private void repaintPanel() {
        this.warnPanel.validate();
        this.jspwarn.validate();
        this.repaint();
    }

    public synchronized void receInitTemp(DataBean data) {
        chartView.receInitTemp(data);
    }

    /**
     * 接受数据
     */
    public synchronized void receData(DataBean data, boolean flag) {
        chartView.receDatas(flag, data);
    }

    public synchronized void checkHitch(HitchBean hitchBean) {
        chartView.checkHitch(hitchBean);
    }

    public synchronized void receDatas(List<DataBean> dataList, boolean flag) {
        chartView.receDatas(dataList, flag);
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth(), h = getHeight();
        Color c1 = new Color(242, 242, 242), c2 = Color.WHITE;
        g2.setPaint(new GradientPaint(0, 0, c1, 0, 250, c2));
        g2.fillRect(0, 0, w, 250);
        g2.setColor(c2);
        g2.fillRect(0, 250, w, h);
        g2.dispose();
        super.paintComponent(g);
    }
}
