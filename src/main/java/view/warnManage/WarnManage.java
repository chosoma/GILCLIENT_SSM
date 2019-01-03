package view.warnManage;

import domain.*;
import model.DataManageModel;
import model.DataModel_Warn;
import mytools.*;
import service.DataManageService;
import service.PointService;
import service.WarningService;
import util.MyExcelUtil;
import view.icon.MyIconFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class WarnManage extends JPanel {

    private JComboBox<String> jcbPlace, jcbXW;
    private Check2SPinner c2s1, c2s2;
    private JTable table;
    private static WarnManage DM = null;

    private WarnManage() {

        this.setLayout(new BorderLayout());
        // 加载工具栏
        this.initToolPane();

        this.initCenter();

        this.initWait();
        // 加载表格
        this.initTable();

        this.initPage();
        refreshSJBH();
    }

    private JPanel centerPanel;
    private CardLayout card;

    private void initCenter() {
        centerPanel = new JPanel();
        card = new CardLayout();
        centerPanel.setLayout(card);
        centerPanel.add(new JPanel(), "null");
        this.add(centerPanel, BorderLayout.CENTER);
    }

    private void initWait() {
        JPanel jPanel = new JPanel(new BorderLayout());
        JLabel waitlabel = new JLabel(new ImageIcon("images/progress.gif"));
        jPanel.add(waitlabel, BorderLayout.CENTER);
        JLabel textlabel = new JLabel("正在加载数据请稍后", JLabel.CENTER);
        jPanel.add(textlabel, BorderLayout.SOUTH);
        centerPanel.add(jPanel, "wait");
    }

    private JPanel tablePanel;
    private PageBean pageBean;
    private JTextField jtfthispage;
    private ClickButton startbutton, beforbutton, nextbutton, endbutton;
    private JPanel pageControlPanel;
    private JLabel jlendpage;

    private void initPage() {
        pageBean = new PageBean();
        pageControlPanel = new JPanel();
        pageControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        tablePanel.add(pageControlPanel, BorderLayout.SOUTH);
        startbutton = new ClickButton("首页");
        startbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageBean.toStartPage();
                searchDataThread();
                jtfthispage.setText(String.valueOf(pageBean.getThispage()));
            }
        });
        pageControlPanel.add(startbutton);
        beforbutton = new ClickButton("上一页");
        beforbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageBean.toBeforPage();
                searchDataThread();
                jtfthispage.setText(String.valueOf(pageBean.getThispage()));
            }
        });
        pageControlPanel.add(beforbutton);
        jtfthispage = new JTextField("1");
        jtfthispage.setPreferredSize(new Dimension(30, 16));
        pageControlPanel.add(jtfthispage);

        ClickButton clickButton = new ClickButton("跳转");
        clickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strpage = jtfthispage.getText();
                try {
                    int intpage = Integer.parseInt(strpage);
                    if (intpage <= 0) {
                        throw new NumberFormatException();
                    }
                    pageBean.toPage(intpage);
                    searchDataThread();
                    jtfthispage.setText(String.valueOf(pageBean.getThispage()));

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "请输入正确页码", "提示",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        pageControlPanel.add(clickButton);
        nextbutton = new ClickButton("下一页");
        nextbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageBean.toNextPage();
                searchDataThread();
                jtfthispage.setText(String.valueOf(pageBean.getThispage()));
            }
        });
        pageControlPanel.add(nextbutton);
        endbutton = new ClickButton("末页");
        endbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageBean.toEndPage();
                searchDataThread();
                jtfthispage.setText(String.valueOf(pageBean.getThispage()));
            }
        });
        pageControlPanel.add(endbutton);
        jlendpage = new JLabel("", JLabel.LEFT);
        pageControlPanel.add(jlendpage);
    }

    private void searchDataThread() {
        toolBarR.setVisible(false);
        search.setEnabled(false);
        card.show(centerPanel, "wait");

        new Thread(new Runnable() {
            @Override
            public void run() {
                searchData();
                card.show(centerPanel, "table");
                search.setEnabled(true);
                toolBarR.setVisible(true);
            }
        }).start();
    }

    // 懒汉式
    public static WarnManage getInstance() {
        if (DM == null) {
            synchronized (WarnManage.class) {
                if (DM == null)
                    DM = new WarnManage();
            }
        }
        return DM;
    }

    public void hiddenUser() {
        toolBarR.remove(delete);
        toolBarR.remove(clear);
        toolBarR.validate();
    }

    private JPanel toolBarR;
    private JButton delete;
    private JButton clear;
    private JButton search;

    /**
     * 初始化顶部工具栏面板 jToolPane：顶部工具栏面板有jtb1、jtb2左右两个工具栏，分别加载左边和右边的工具按钮
     */
    private void initToolPane() {

        /*
         * 工具栏面板
         */
        JPanel toolPane = new MyToolPanel(new BorderLayout());
        toolPane.setPreferredSize(new Dimension(toolPane.getWidth(), 36));
        this.add(toolPane, BorderLayout.NORTH);

        /*
         * 工具栏左半边
         */
        JPanel toolBarL = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 2));
        toolBarL.setOpaque(false);
        toolPane.add(toolBarL, BorderLayout.WEST);

        toolBarL.add(Box.createHorizontalStrut(5));

        JTextArea jta3 = new JTextArea("监测\n位置");
        jta3.setOpaque(false);
        jta3.setEditable(false);
        toolBarL.add(jta3);

        jcbPlace = new JComboBox<>();
        jcbPlace.setMaximumSize(new Dimension(100, 20));
        toolBarL.add(jcbPlace);
        JButton refresh = MyUtil.CreateButton(MyIconFactory.getReFreshIcon(),
                "刷新");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                refreshSJBH();

            }
        });
        toolBarL.add(refresh);
        toolBarL.add(Box.createHorizontalStrut(5));

        JTextArea jtaXW = new JTextArea("测点\n相位");
        jtaXW.setOpaque(false);
        jtaXW.setEditable(false);
        toolBarL.add(jtaXW);

        jcbXW = new JComboBox<>(new String[]{"全部", "A", "B", "C"});
        toolBarL.add(jcbXW);
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

        toolBarL.add(Box.createHorizontalStrut(5));
        search = new ClickButton("查询", new ImageIcon(
                "images/search_24.png"));
        search.setFont(MyUtil.FONT_14);
        search.setPreferredSize(new Dimension(65, 32));
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    getSearchConditon();
//                    System.out.println(para);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                search.setEnabled(false);
                card.show(centerPanel, "wait");
                toolBarR.setVisible(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CountBean countBean = WarningService.getTableWarnCount(para);
                            //system.out.println(countBean.getCount());
                            pageBean.setCount(countBean.getCount());
                            jlendpage.setText(String.valueOf(pageBean.getEndpage()));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        searchDataThread();
                    }
                }).start();

                pageBean.toStartPage();
                jtfthispage.setText("1");
            }
        });
        toolBarL.add(search);

        toolBarR = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 2));
        toolBarR.setOpaque(false);
        toolPane.add(toolBarR, BorderLayout.EAST);

        Dimension buttonSize = new Dimension(34, 32);

        delete = new EditButton(new ImageIcon("images/delete.png"));
        delete.setToolTipText("删除选中的警报信息");
        delete.setPreferredSize(buttonSize);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 得到鼠标选中行
                int[] selRows = table.getSelectedRows();
                // 做个判断，如果没有选中行，则弹出提示
                if (selRows.length <= 0) {
                    JOptionPane.showMessageDialog(null, "请选中要删除的警报信息", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 提示"是"、"否"、"取消"删除操作
                int flag = JOptionPane.showConfirmDialog(null, "确定要删除选中警报信息？",
                        "删除", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (flag != JOptionPane.OK_OPTION)
                    return;
//                int[] ids = new int[selRows.length];

                try {
                    Map<WarnBean, List<Date>> warnBeanDateMap = getSelectTable();
                    WarningService.deleteWarn(warnBeanDateMap);
//                    WarningService.deleteData(ids);
                    JOptionPane.showMessageDialog(null, "警报信息已成功删除", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    int[] rows = new int[selRows.length];
                    int j = 0;
                    for (int i : selRows) {
                        rows[j] = table.convertRowIndexToModel(i);
                        j++;
                    }
                    Arrays.sort(rows);// rows升序排序
                    DataManageModel model = (DataManageModel) table.getModel();
                    model.removeRows(rows);
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null,
                            "删除失败", "提示",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        toolBarR.add(delete);

        clear = new EditButton(new ImageIcon("images/clear_24.png"));
        clear.setToolTipText("清空表中警报信息");
        clear.setPreferredSize(buttonSize);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 做个判断，如果没有选中行，则弹出提示
                int rowCount = table.getRowCount();
                if (rowCount == 0) {
                    JOptionPane.showMessageDialog(null, "当前表中无警报信息，或警报信息已清空", "提示",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 提示"是"、"否"、"取消"删除操作
                int flag = JOptionPane.showConfirmDialog(null, "确定要清空表中警报信息？",
                        "清空", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (flag != JOptionPane.OK_OPTION)
                    return;
//                int[] ids = new int[rowCount];
//                for (int i = 0; i < rowCount; i++) {
//                    ids[i] = (Integer) table.getValueAt(i, 0);
//                }

                try {
                    Map<WarnBean, List<Date>> warnBeanDateMap = getAllTable();

                    WarningService.deleteWarn(warnBeanDateMap);

                    JOptionPane.showMessageDialog(null, "警报信息已成功清空", "提示",
                            JOptionPane.INFORMATION_MESSAGE);
                    DataManageModel model = (DataManageModel) table.getModel();
                    model.clearData();
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null,
                            "清空失败", "提示",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        toolBarR.add(clear);

        JButton export = new EditButton(new ImageIcon(
                "images/database_download_24.png"));
        export.setToolTipText("将表中警报信息下载到Excel");
        export.setPreferredSize(buttonSize);
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "当前表中无警报信息，或警报信息已清空", "提示",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        MyExcelUtil.Export2Excel(table, "警报");
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null,
                                "导出失败", "失败",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        toolBarR.add(export);

        JButton print = new EditButton(new ImageIcon("images/printer_24.png"));
        print.setToolTipText("打印表格");
        print.setPreferredSize(buttonSize);
        print.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (table.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "当前表中无警报信息，或警报信息已清空", "提示",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        table.print();
                    } catch (PrinterException e) {
                        JOptionPane.showMessageDialog(null, "打印失败", "提示",
                                JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            }
        });
        toolBarR.add(print);
    }

    private Map<WarnBean, List<Date>> getSelectTable() {
        int[] selRows = table.getSelectedRows();
        Map<WarnBean, List<Date>> warnBeanListMap = new HashMap<>();
        for (int selRow : selRows) {
            String place = (String) table.getValueAt(selRow, 0);
            String xw = (String) table.getValueAt(selRow, 1);
            Date date = (Date) table.getValueAt(selRow, 3);
            PointBean pointBean = PointService.getUnitPoint(place);
            WarnBean warnBean = new WarnBean();
            warnBean.setPointBean(pointBean);
            warnBean.setXw(xw);
            warnBean.setDate(date);
            if (warnBeanListMap.containsKey(warnBean)) {
                warnBeanListMap.get(warnBean).add(date);
            } else {
                List<Date> dates = new ArrayList<>();
                dates.add(date);
                warnBeanListMap.put(warnBean, dates);
            }
        }
        return warnBeanListMap;
    }

    private Map<WarnBean, List<Date>> getAllTable() {
        int rowCount = table.getRowCount();
        Map<WarnBean, List<Date>> warnBeanListMap = new HashMap<>();
        for (int i = 0; i < rowCount; i++) {
            String place = (String) table.getValueAt(i, 0);
            String xw = (String) table.getValueAt(i, 1);
            Date date = (Date) table.getValueAt(i, 3);
            PointBean pointBean = PointService.getUnitPoint(place);
            WarnBean warnBean = new WarnBean();
            warnBean.setPointBean(pointBean);
            warnBean.setXw(xw);
            warnBean.setDate(date);
            if (warnBeanListMap.containsKey(warnBean)) {
                warnBeanListMap.get(warnBean).add(date);
            } else {
                List<Date> dates = new ArrayList<>();
                dates.add(date);
                warnBeanListMap.put(warnBean, dates);
            }
        }
        return warnBeanListMap;
    }

    private DataSearchPara para;

    private void searchData() {
        System.gc();
        try {
            getSearchConditon();
            DataManageModel model = DataModel_Warn.getInstance();
            table.setModel(model);
            List<Vector<Object>> datas = WarningService.getTableWarn(para, pageBean);
            model.addDatas(datas);
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询失败,请稍后重启", "提示",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 初始化中间表格部分
     */
    private void initTable() {
        // 中部JTable
        tablePanel = new JPanel(new BorderLayout());
        table = new JTable();
        this.initializeTable();// 初始化表格

        // 将JTable添加到滚动面板中
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(tablePanel, "table");
    }

    // 初始化表格
    private void initializeTable() {
        MyTCR tcr = new MyTCR();
        table.setDefaultRenderer(String.class, tcr);
        table.setDefaultRenderer(Number.class, tcr);
        table.setDefaultRenderer(Float.class, tcr);
        table.setDefaultRenderer(Double.class, tcr);
        table.setDefaultRenderer(Date.class, tcr);

        // 表头设置
        JTableHeader tableHeader = table.getTableHeader();
        DefaultTableCellRenderer dtcr = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);// 表头居中
        Dimension dimension = dtcr.getSize();
        dimension.height = MyUtil.HeadHeight;
        dtcr.setPreferredSize(dimension);// 设置表头高度
        tableHeader.setDefaultRenderer(dtcr);
        // 表头不可拖动
        tableHeader.setReorderingAllowed(false);
        // 列宽不可修改
        tableHeader.setResizingAllowed(false);

        // 自动排序
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(MyUtil.RowHeight);// 设置行高

    }

    /**
     * 获取查询条件
     */
    private void getSearchConditon() throws Exception {
        para = new DataSearchPara();
        para.setType("警报");
        int sjbhIdenx = jcbPlace.getSelectedIndex();
        if (sjbhIdenx < 0) {
            throw new Exception("监测位置不存在");
        } else if (sjbhIdenx > 0) {// 全部
            String sjbh = (String) jcbPlace.getSelectedItem();
            para.setPlace(sjbh);
        }
        sjbhIdenx = jcbXW.getSelectedIndex();
        if (sjbhIdenx < 0) {
            throw new Exception("相位不存在");
        } else if (sjbhIdenx > 0) {// 全部
            String xw = (String) jcbXW.getSelectedItem();
            para.setXw(xw);
        }
        Date startT = c2s1.getTime(), endT = c2s2.getTime();
        if (startT == null) {// 起始时间未选中
            if (endT != null) {// 终止时间未选中
                para.setT2(endT);
            }
        } else {// 起始时间选中
            para.setT1(startT);
            if (endT != null) {// 终止时间未选中
                if (startT.after(endT)) {
                    throw new Exception("起始时间应位于终止时间之前");
                }
                para.setT2(endT);
            }
        }
    }

    private void refreshSJBH() {
        Vector<String> sjbhs = DataManageService.getUnitPlaces();
        jcbPlace.removeAllItems();
        if (sjbhs.size() > 0) {
            jcbPlace.addItem("全部");
            for (String sjbh : sjbhs) {
                jcbPlace.addItem(sjbh);
            }
        }
        jcbXW.setSelectedIndex(0);
    }

}
