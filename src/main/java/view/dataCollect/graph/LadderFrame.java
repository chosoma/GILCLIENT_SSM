package view.dataCollect.graph;

import com.DefaultUI;
import data.FormatTransfer;
import domain.DataBean;
import domain.DataSearchPara;
import domain.PointBean;
import domain.UnitBean;
import mytools.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.urls.StandardXYURLGenerator;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import service.DataService;
import service.UnitService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class LadderFrame extends JDialog {

    private List<UnitBean> unitBeanList;

    public void setUnitBeanList(List<UnitBean> unitBeanList) {
        this.unitBeanList = unitBeanList;
    }

    //    private JComboBox<String> jcbxw;
    private PointBean pointBean;
    private CardLayout card;
    private JPanel centerPanel;

    public LadderFrame(PointBean pointBean) {
        this.setAlwaysOnTop(true);
        this.pointBean = pointBean;
        this.setTitle(pointBean.getPlace());
        this.setIconImages(DefaultUI.icons);
//        setModal(true);// 设置对话框模式
//        setUndecorated(true);// 去除JDialog边框
//         设置JDialog背景透明
//        AWTUtilities.setWindowOpaque(this, false);

        this.card = new CardLayout();
        this.centerPanel = new JPanel();
        this.centerPanel.setLayout(card);
        this.centerPanel.add(new JPanel(), "null");
        this.setMinimumSize(new Dimension(400, 0));

        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.gc();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                System.gc();
            }

            @Override
            public void componentShown(ComponentEvent e) {
                System.gc();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                card.show(centerPanel, "null");
                lineDataset.removeAllSeries();
                System.gc();
            }
        });

//        this.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                if(e.getSource() instanceof LadderFrame){
//                    ((LadderFrame) e.getSource()).setAlwaysOnTop(true);
//                }
//            }
//        });
//        maxrec = Shell.getInstance().getMaxBounds();
        initshow();
        initwait();
        initchart();
    }

    private void initchart() {
        DateAxis timeAxis = new DateAxis();
        timeAxis.setLowerMargin(0.02);  // reduce the default margins
        timeAxis.setUpperMargin(0.02);
        timeAxis.setDateFormatOverride(new SimpleDateFormat("YYYY-MM-dd HH:mm"));
        timeAxis.setLabelFont(MyUtil.FONT_12);
        timeAxis.setTickLabelFont(MyUtil.FONT_12);

        NumberAxis valueAxis = new NumberAxis();
        valueAxis.setAutoRangeIncludesZero(false);  // override default
        valueAxis.setLabelFont(MyUtil.FONT_12);
        valueAxis.setTickLabelFont(MyUtil.FONT_12);
//        valueAxis.setNumberFormatOverride(new DecimalFormat("#0.00"));

        XYPlot plot = new XYPlot(lineDataset, timeAxis, valueAxis, null);
        plot.setAxisOffset(new RectangleInsets(5, 5, 5, 5));//图片区与坐标轴的距离
        plot.setOutlinePaint(null);
        plot.setInsets(new RectangleInsets(0, 0, 0, 0));//坐标轴与最外延的距离
        XYToolTipGenerator toolTipGenerator = StandardXYToolTipGenerator.getTimeSeriesInstance();
        XYURLGenerator urlGenerator = new StandardXYURLGenerator();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setBaseToolTipGenerator(toolTipGenerator);
        renderer.setURLGenerator(urlGenerator);
//        plot.setRenderer(renderer);
        /*设置颜色*/
        renderer.setSeriesPaint(0, new Color(236, 236, 0));//A
        renderer.setSeriesPaint(1, new Color(50, 153, 102));//B
        renderer.setSeriesPaint(2, new Color(236, 0, 0));//C
        renderer.setSeriesPaint(3, new Color(0, 0, 0));//C
        renderer.setBaseShapesVisible(false);
//        xylineandshaperenderer.setBaseShapesVisible(true);
        renderer.setBaseItemLabelsVisible(false);//设置值显示 true 显示 false 不显示
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.INSIDE10, TextAnchor.BASELINE_LEFT));
        renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
//        xyitem.setBaseItemLabelFont(new Font("Dialog", Font.BOLD, 14));
        plot.setRenderer(renderer);
        JFreeChart chart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.setTextAntiAlias(false);
//        chart.setTitle(new TextTitle("", new Font(null, Font.PLAIN, 15)));
        chart.setAntiAlias(true);//设置抗锯齿
//        currentTheme.apply(chart);
        chart.setPadding(new RectangleInsets(5, 5, 5, 5));
        chart.setNotify(true);
        ChartPanel chartPanel = new ChartPanel(chart);
        centerPanel.add(chartPanel, "line");
    }

    private void initwait() {
        JPanel jPanel = new JPanel(new BorderLayout());
        JLabel waitlabel = new JLabel(new ImageIcon("images/progress.gif"));
        jPanel.add(waitlabel, BorderLayout.CENTER);
        JLabel textlabel = new JLabel("正在加载数据请稍后", JLabel.CENTER);
        jPanel.add(textlabel, BorderLayout.SOUTH);
        centerPanel.add(jPanel, "wait");
    }


    private void initshow() {
        JPanel contentPane = new JPanel(new BorderLayout());
        this.setContentPane(contentPane);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        this.setSize(400, 300);
        this.setLocationRelativeTo(null);


        JPanel toolBarL = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 2));
        toolBarL.setOpaque(false);
        contentPane.add(toolBarL, BorderLayout.SOUTH);

        toolBarL.add(Box.createHorizontalStrut(5));
        JTextArea jta4 = new JTextArea("起始\n时间");
        jta4.setOpaque(false);
        jta4.setEditable(false);
        toolBarL.add(jta4);

        Calendar ca = Calendar.getInstance();
        Date date2 = ca.getTime();
        ca.add(Calendar.DAY_OF_MONTH, -1);
        Date date = ca.getTime();
        // 起始时间
        c2s1 = new Check2SPinner(false, date);
//        c2s1.setMaximumSize(new Dimension(165, 17));
        toolBarL.add(c2s1);

        toolBarL.add(Box.createHorizontalStrut(5));

        JTextArea jta5 = new JTextArea("终止\n时间");
        jta5.setOpaque(false);
        jta5.setEditable(false);
        toolBarL.add(jta5);

        // 终止时间
        c2s2 = new Check2SPinner(false, date2);
//        c2s2.setMaximumSize(new Dimension(165, 17));
        toolBarL.add(c2s2);

//        JLabel jlxw = new JLabel("相位", JLabel.CENTER);
//        jlxw.setOpaque(false);
//        toolBarL.add(jlxw);

//        String[] xws = new String[]{"A", "B", "C"};
//        jcbxw = new JComboBox<>(xws);
//        toolBarL.add(jcbxw);
//        toolBarL.add(Box.createHorizontalStrut(5));
        final JButton search = new ClickButton(new ImageIcon(
                "images/search_24.png"));
        search.setFont(MyUtil.FONT_14);
        search.setPreferredSize(new Dimension(24, 24));
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search.setEnabled(false);
                card.show(centerPanel, "wait");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.gc();
                        searchData();
                        card.show(centerPanel, "line");
                        search.setEnabled(true);
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
        });
        toolBarL.add(search);
    }

    private Map<UnitBean, List<DataBean>> unitBeanListMap = new TreeMap<UnitBean, List<DataBean>>(new Comparator<UnitBean>() {
        @Override
        public int compare(UnitBean o1, UnitBean o2) {
            if (o1.getXw() == null || o1.getXw().length() < 1 || o2.getXw() == null || o2.getXw().length() < 1) {
                return 1;
            }
            return o1.getXw().charAt(0) - o2.getXw().charAt(0);
        }
    });
    private DataSearchPara para = new DataSearchPara();

    private void searchData() {
        try {
            getSearchConditon();
            lineDataset.removeAllSeries();
            unitBeanListMap.clear();
            for (UnitBean unitBean : unitBeanList) {
                List<DataBean> dataBeans = DataService.getBetween(unitBean, para);
                unitBeanListMap.put(unitBean, dataBeans);
            }
            if (LadderFrame.this.pointBean.getUnitType() == 3) {
                UnitBean unitBean = UnitService.getInitTempUnit();
                List<DataBean> dataBeans = DataService.getBetween(unitBean, para);
                unitBeanListMap.put(unitBean, dataBeans);
            }
            refreshData();
            unitBeanListMap.clear();
        } catch (SQLException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, "获取超时,请稍后重试", "提示",
                    JOptionPane.ERROR_MESSAGE);
            card.show(centerPanel, "null");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
        } finally {
            System.gc();
        }
    }


    private TimeSeriesCollection lineDataset = new TimeSeriesCollection();

    private void refreshData() {
        lineDataset.removeAllSeries();

        Set<Map.Entry<UnitBean, List<DataBean>>> entries = unitBeanListMap.entrySet();
        for (Map.Entry<UnitBean, List<DataBean>> entry : entries) {
            UnitBean unit = entry.getKey();
            List<DataBean> dataBeans = entry.getValue();
            TimeSeries timeSeries;
            if (unit.isIsinit()) {
                timeSeries = new TimeSeries("环境温度");
            } else {
                if (unit.getType() == 3) {
                    timeSeries = new TimeSeries("温升" + unit.getXw());
                } else {
                    timeSeries = new TimeSeries(unit.getXw());
                }
            }

            Iterator<DataBean> iterator = dataBeans.iterator();
            while (iterator.hasNext()) {
                DataBean data = iterator.next();
                iterator.remove();
                switch (unit.getType()) {
                    case 1:
                        timeSeries.add(new Second(data.getDate()), data.getPres());
                        break;
                    case 2:
                        float vari = data.getVari();
                        if (unit.getInitvari() != 0) {
                            vari = FormatTransfer.newScale(data.getVari(), unit.getInitvari());
                        }
                        timeSeries.add(new Second(data.getDate()), vari);
                        break;
                    case 3:
                        timeSeries.add(new Second(data.getDate()), data.getTemp());
                        break;
                }
            }
            lineDataset.addSeries(timeSeries);
        }
        entries.clear();
        unitBeanListMap.clear();
        System.gc();
    }


    private void getSearchConditon() throws Exception {
        para.setUnitType(pointBean.getUnitType());
        Date startT = c2s1.getTime(), endT = c2s2.getTime();

        para.setT2(endT);
        para.setT1(startT);
        if (startT != null && endT != null && startT.after(endT)) {
            throw new Exception("起始时间应位于终止时间之前");
        }

    }

    private Check2SPinner c2s1, c2s2;


}
