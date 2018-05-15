package view.systemSetup;

import domain.PointBean;
import mytools.MyPanel;
import mytools.MyUtil;
import service.UserService;
import view.dataManage.DataManage;
import view.systemSetup.systemSetupComptents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SystemSetup extends JPanel {
    private static SystemSetup S2S = null;
    //    private CommGPRS sys2comm;
    private System2Period sys2per;//采集周期
//    private System2WdSet sys2Wd;//温度频率
    private System2WarnValue sys2warn;//报警值设置
    private System2PassWord sys2pw;//修改密码
    private System2User sys2u;//用户管理
    private JPanel leftPanel, rightPanel;
    private JLabel[] leftLabels = new JLabel[4];    //0通讯 1采集周期 2温度频率 3报警设置 4用户管理 5修改密码
    private String[] labelString = new String[]{
//            "通讯设置",
            "采集周期", //            "温度频率", 
            "报警设置", "用户管理", "修改密码"};
    private String[] rightString = new String[]{
//            "通 讯 设 置",
            "采 集 周 期", //            "温 度 频 率", 
            "报 警 设 置", "用 户 管 理", "修 改 密 码"};
    private JLabel titleLabel;
    private boolean isInitialize = false;// 初始化标志，用于首次打开显示时用
    private int dividerLocation = 250;// 分割条位置

    public static SystemSetup getInstance() {
        if (S2S == null) {
            synchronized (SystemSetup.class) {
                if (S2S == null)
                    S2S = new SystemSetup();
            }
        }
        return S2S;
    }

    public SystemSetup() {
        this.setLayout(new BorderLayout());
        initLeft();
        initRight();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPanel, rightPanel);
        // 指定左边的面板占多大的像素
        splitPane.setDividerLocation(dividerLocation);
        // 把拆分边界线设为0，即不显示
        splitPane.setDividerSize(0);
        // 设置jSplitPane不可拖动
        splitPane.setEnabled(false);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);
        this.add(splitPane, BorderLayout.CENTER);
        this.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                if (!isInitialize) {
                    isInitialize = true;
                    boolean admin = UserService.getInstance().isAdmin();
                    if (admin) {
                        sys2u.loadUsers();
                    }
                }
            }
        });

    }

    private void initRight() {
        titleLabel = new JLabel(labelString[0], JLabel.CENTER);
        titleLabel.setFont(MyUtil.FONT_36);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));// 绘制空白边框
//        sys2comm = new CommGPRS();
        sys2per = new System2Period();
        sys2pw = new System2PassWord();
        sys2warn = new System2WarnValue();
        sys2u = new System2User();
//        sys2Wd = new System2WdSet();
        rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5000, 10));
        rightPanel.setOpaque(false);
        rightPanel.add(titleLabel, 0);
        rightPanel.add(sys2per, 1);
    }


    private void initLeft() {
        // 左面板
        leftPanel = new MyPanel(MyUtil.LEFT_PANE_COLOR, 1.0f);
        leftPanel.setLayout(new FlowLayout(FlowLayout.CENTER, dividerLocation, 30));
        for (int i = 0; i < leftLabels.length; i++) {
            leftLabels[i] = new JLabel(labelString[i], JLabel.CENTER);
            leftPanel.add(leftLabels[i]);
            leftLabels[i].setFont(MyUtil.FONT_16);
            leftLabels[i].setPreferredSize(new Dimension(140, 50));
            final int index = i;
            leftLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    JPanel jPanel = null;
                    switch (index) {
//                        case 0:
//                            jPanel = sys2comm;
//                            break;
                        case 0:
                            jPanel = sys2per;
                            break;
                        case 1:
//                            jPanel = sys2Wd;
//                            break;
//                        case 2:
                            jPanel = sys2warn;
                            break;
                        case 2:
                            jPanel = sys2u;
                            break;
                        case 3:
                            jPanel = sys2pw;
                            break;
                    }
                    if (rightPanel.getComponent(1) == jPanel) {
                        return;
                    }
                    titleLabel.setText(rightString[index]);
                    rightPanel.remove(1);
                    rightPanel.add(jPanel, 1);
                    rightPanel.repaint();
                    rightPanel.revalidate();
                }

                @Override
                public void mouseEntered(MouseEvent arg0) {
                    leftLabels[index].setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(MouseEvent arg0) {
                    leftLabels[index].setForeground(Color.BLACK);
                }
            });
        }
    }

    public void hideUserMan() {
        for (int i = 0; i < 4; i++) {
            leftLabels[i].setVisible(false);
        }
        titleLabel.setText(rightString[4]);
        rightPanel.remove(1);
        rightPanel.add(sys2pw, 1);
    }

//    public void closeResource() {
//        sys2comm.closeResource();
//    }

    public boolean isEditable() {
//        return sys2comm.isEditable();
        return true;
    }

    public void setEditable(boolean isEditable) {
//        sys2comm.setEditable(isEditable);
        sys2per.openlock(isEditable);
    }

    public void refresh() {
        sys2per.refresh();
        sys2warn.refresh();
    }

    public void refreshPeriod() {
        sys2per.refreshPeriod();
    }

}
