package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.Charset;


import javax.imageio.ImageIO;
import javax.swing.*;

import com.Configure;
import com.DefaultUI;
import com.LgoInfo;
import mytools.*;
import view.dataCollect.datacollect.ChartView;
import view.debugs.Debugs;
import view.icon.CloseIcon;
import view.icon.MaxIcon;
import view.icon.MinIcon;
import view.icon.MyIconFactory;
import view.icon.SetIcon;

import view.systemSetup.SystemSetup;

public class Shell extends JFrame implements ActionListener {

    // 虚线框
    private MyDashedBorder myDashedBorder;
    private Point lastPoint;
    private boolean isMaximized = false;
    private JPopupMenu pop;
    private JButton btnMax;
    private boolean isDragged = false;
    private JPanel normalpanel;
    private JPanel toolBar;
    private JPanel centerPanel;
    private CardLayout centerCard;// 卡片布局
    public static int ShellState = Frame.NORMAL;

    public static Dimension dimension = new Dimension(1000, 600);

    private static Shell SHELL = null;

    private Shell() {

        this.initWindow();

        this.initTop();

        this.initCenter();

    }

    public static Shell getInstance() {
        if (SHELL == null) {
            synchronized (Shell.class) {
                if (SHELL == null)
                    SHELL = new Shell();
            }
        }
        return SHELL;
    }

    private void initWindow() {

        this.setIconImages(DefaultUI.icons);
        this.setTitle(LgoInfo.SoftName);// 标题

        this.setUndecorated(true);// 去除边框修饰
        // AWTUtilities.setWindowOpaque(this, false);// 设置透明
        this.setSize(dimension);
        this.setLocationRelativeTo(null);

        CardLayout contentCard = new CardLayout();
        JPanel contentPane = new JPanel(contentCard);
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(44, 46, 54)));
        setContentPane(contentPane);

        try {
//            Image image = ImageIO.read(this.getClass().getResource("backGround.jpg"));
            Image image = ImageIO.read(this.getClass().getClassLoader().getResource("icon/backGround.jpg"));
            normalpanel = new BackGroundPanel(image);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        contentPane.add(normalpanel, "normal");

        myDashedBorder = new MyDashedBorder();
        myDashedBorder.setBounds(this.getBounds());
        // 窗体关闭弹出对话框提示："确定"、"取消"
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Shell.this.exitSys();
            }
        });
//        MouseInputListener listener = new MouseInputHandler(this);
//        addMouseListener(listener);
//        addMouseMotionListener(listener);
    }

    private void initTop() {
        // 顶部面板：放置标题面板和功能面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        normalpanel.add(topPanel, BorderLayout.NORTH);
        topPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    btnMax.doClick();
                }
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                if (!isMaximized && isShowing()) {
                    lastPoint = arg0.getLocationOnScreen();// 记录鼠标坐标
                    myDashedBorder.setVisible(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isMaximized & isDragged) {
                    isDragged = false;
                    Shell.this.setLocation(myDashedBorder.getLocation());
                }
                myDashedBorder.setVisible(false);
            }
        });
        topPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isMaximized&&isShowing()) {
                    isDragged = true;
                    Point location = myDashedBorder.getLocation();
                    Point tempPonit = e.getLocationOnScreen();
                    myDashedBorder.setLocation(location.x + tempPonit.x - lastPoint.x, location.y + tempPonit.y - lastPoint.y);
                    lastPoint = tempPonit;
                }
            }
        });

        // 标题面板：放置logo和窗口工具
        JPanel tiltlePanel = new JPanel(new BorderLayout());
        tiltlePanel.setOpaque(false);
        topPanel.add(tiltlePanel, BorderLayout.NORTH);

        JLabel log = new JLabel(" " + LgoInfo.SoftName);
        log.setForeground(Color.BLACK);
        log.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        tiltlePanel.add(log, BorderLayout.WEST);

        // 窗口操作面板，“最小化”、“最大化”、关闭
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        tiltlePanel.add(right, BorderLayout.EAST);

        initSetPop();

        JButton btnSet = new JButton(new SetIcon());
        btnSet.setToolTipText("主菜单");
        btnSet.setFocusable(false);
        // 无边框
        btnSet.setBorder(null);
        // 取消绘制按钮内容区域
        btnSet.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        btnSet.setFocusPainted(false);
        btnSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton jb = (JButton) e.getSource();
                pop.show(jb, 0, jb.getHeight());
            }
        });
        right.add(btnSet);

        JButton btnMin = new JButton(new MinIcon());
        btnMin.setToolTipText("最小化");
        btnMin.setFocusable(false);
        // 无边框
        btnMin.setBorder(null);
        // 取消绘制按钮内容区域
        btnMin.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        btnMin.setFocusPainted(false);
        right.add(btnMin);
        btnMin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Shell.this.setExtendedState(JFrame.ICONIFIED);
                // 此处只能是Frame.setStatr(state),否则在最大化模式下最小化后，
                // 再点击状态栏图标就不能还原最大化,只能显示JFrame.NORMAL状态
                Shell.this.setState(Frame.ICONIFIED);
            }
        });

        btnMax = new JButton(new MaxIcon());
        btnMax.setToolTipText("最大化");
        btnMax.setFocusable(false);
        // 无边框
        btnMax.setBorder(null);
        // 取消绘制按钮内容区域
        btnMax.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        btnMax.setFocusPainted(false);
        right.add(btnMax);
        btnMax.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMaximized) {
                    // Shell.this.setExtendedState(Frame.NORMAL);
                    Shell.this.setBounds(myDashedBorder.getBounds());
                    ShellState = Frame.NORMAL;
                    btnMax.setToolTipText("最大化");
//                    ChartView.getInstance().getPanelGraph().repaint();
                } else {
                    // Shell.this.setExtendedState(Frame.MAXIMIZED_BOTH);
                    Shell.this.setBounds(getMaxBounds());
                    ShellState = Frame.MAXIMIZED_BOTH;
                    btnMax.setToolTipText("向下还原");
                }
                isMaximized = !isMaximized;
                btnMax.setSelected(isMaximized);
            }
        });

        JButton btnClose = new JButton(new CloseIcon());
        btnClose.setToolTipText("关闭");
        btnClose.setFocusable(false);
        // 无边框
        btnClose.setBorder(null);
        // 取消绘制按钮内容区域
        btnClose.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        btnClose.setFocusPainted(false);
        right.add(btnClose);
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Shell.this.exitSys();
            }
        });

        // 功能面板，放置“主界面”、“数据采集”······
        JPanel funcionPanel = new JPanel(new BorderLayout());
        funcionPanel.setOpaque(false);
        topPanel.add(funcionPanel, BorderLayout.CENTER);

        toolBar = new JPanel();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 2));
        toolBar.setOpaque(false);
        funcionPanel.add(toolBar, BorderLayout.WEST);

        JPanel toolBarRight = new JPanel(new BorderLayout());
        toolBarRight.setOpaque(false);
        funcionPanel.add(toolBarRight, BorderLayout.EAST);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
        buttonPanel.setOpaque(false);
        toolBarRight.add(buttonPanel, BorderLayout.SOUTH);

        Dimension buttonsize = new Dimension(60, 24);

        JButton jbSF6 = new CollectTitleButton("SF6");
        jbSF6.setPreferredSize(buttonsize);
        jbSF6.setSelected(true);
        jbSF6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myButtonGroup(e, "SF6");
            }
        });
        buttonPanel.add(jbSF6);

        JButton jbWd = new CollectTitleButton("温度");
        jbWd.setPreferredSize(buttonsize);
        jbWd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myButtonGroup(e, "WD");
            }
        });
        buttonPanel.add(jbWd);

        JButton jbGy = new CollectTitleButton("伸缩节");
        jbGy.setPreferredSize(buttonsize);
        jbGy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myButtonGroup(e, "SSJ");
            }
        });
        buttonPanel.add(jbGy);

        JButton jbSw = new CollectTitleButton("图形");
        jbSw.setPreferredSize(buttonsize);
        jbSw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myButtonGroup(e, "TX");
            }
        });
        buttonPanel.add(jbSw);

        JButton jbHit = new CollectTitleButton("故障定位");
        jbHit.setPreferredSize(buttonsize);
        jbHit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myButtonGroup(e, "GZ");
            }
        });
        buttonPanel.add(jbHit);
        final JLabel jLabel = new JLabel("南瑞恒驰", JLabel.RIGHT);
        jLabel.setFont(MyUtil.FONT_20);
//        jLabel.setEnabled(false);
        right.add(jLabel, FlowLayout.LEFT);

        java.util.Timer timer = new java.util.Timer();
        java.util.TimerTask task = new java.util.TimerTask() {
            @Override
            public void run() {
                rgbplus();
                jLabel.setForeground(new Color(rgb));
            }
        };
        timer.schedule(task, 0, 20);

    }

    private void rgbplus() {
        switch (type % 6) {
            case 0:
                rgb += 0x100;
                if (rgb == 0x00ffff) {
                    type++;
                }
                break;
            case 1:
                rgb--;
                if (rgb == 0x00ff00) {
                    type++;
                }
                break;
            case 2:
                rgb += 0x10000;
                if (rgb == 0xffff00) {
                    type++;
                }
                break;
            case 3:
                rgb -= 0x100;
                if (rgb == 0xff0000) {
                    type++;
                }
                break;
            case 4:
                rgb++;
                if (rgb == 0xff00ff) {
                    type++;
                }
                break;
            case 5:
                rgb -= 0x10000;
                if (rgb == 0x0000ff) {
                    type = 0;
                }
                break;
        }
    }

    private int type = 0;
    private int rgb = 0xff;

    private JPanel buttonPanel;

    private MyTitleButton debugs;

    private void initSetPop() {
        pop = new JPopupMenu();

        JCheckBoxMenuItem show = new JCheckBoxMenuItem("调试界面",
                MyIconFactory.getShowDebugIcon());
        show.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean b = ((JCheckBoxMenuItem) e.getSource()).getState();
                if (debugs == null) {
                    debugs = (MyTitleButton) toolBar.getComponent(toolBar
                            .getComponentCount() - 1);
                }
                if (!b && debugs.isSelected()) {
                    ((MyTitleButton) toolBar.getComponent(toolBar
                            .getComponentCount() - 2)).doClick();
                }
                debugs.setVisible(b);
                Debugs.getInstance().setShow(b);
            }
        });
        pop.add(show);

//        JCheckBoxMenuItem voiceAlarm = new JCheckBoxMenuItem("声音报警",
//                MyIconFactory.getVoiceWarnIcon());
//        voiceAlarm.setSelected(Configure.isVioceWarn());
//        voiceAlarm.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                Configure.setVoiceWarn(((JCheckBoxMenuItem) e.getSource())
//                        .getState());
//            }
//        });
//        pop.add(voiceAlarm);

//        JMenuItem help = new JMenuItem("帮 助 ", new ImageIcon("images/main/help_16.png"));
//        help.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    Runtime.getRuntime().exec("cmd /c start " + "help.chm");
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
//        pop.add(help);
    }

    public Rectangle getMaxBounds() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle bounds = new Rectangle(screenSize);
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
        bounds.x += insets.left;
        bounds.y += insets.top;
        bounds.width -= insets.left + insets.right;
        bounds.height -= insets.top + insets.bottom;
        return bounds;
    }


    // 初始化中间面板
    private void initCenter() {
        centerCard = new CardLayout();
        centerPanel = new JPanel(centerCard);
        centerPanel.setOpaque(false);
        normalpanel.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * 退出程序
     */
    private void exitSys() {
        int flag = JOptionPane.showConfirmDialog(null, "您确定要退出系统？", "关闭",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (flag == JOptionPane.OK_OPTION) {
            SystemSetup.getInstance().closeResource();
            Shell.this.setVisible(false);
            Shell.this.dispose();
            try {
                Runtime.getRuntime().exec("taskkill /f /im jf.exe");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }

    public void addItem(JComponent component, Icon icon, String text) {
        this.addItem(component, icon, text, true);
    }

    public void addItem(JComponent component, Icon icon, String text, boolean isVisible) {
        // 按钮
        MyTitleButton mtb = new MyTitleButton(text, icon);
        mtb.setVisible(isVisible);
        mtb.addActionListener(this);
        toolBar.add(mtb);
        // 内容
        centerPanel.add(component, text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof MyTitleButton) {
            MyTitleButton temp = (MyTitleButton) e.getSource();
            if (!temp.isSelected()) {
                temp.setSelected(true);
                for (Component b : toolBar.getComponents()) {
                    if (b != temp && ((MyTitleButton) b).isSelected()) {
                        ((MyTitleButton) b).setSelected(false);
                    }
                }
                centerCard.show(centerPanel, temp.getText());
            }
        } else if (e.getSource() instanceof MySkipButton) {
            MySkipButton temp = (MySkipButton) e.getSource();

            String str = "";
            switch (temp.getType()) {
                case 0:
                    str = "SF6";
                    break;
                case 1:
                    str = "WD";
                    break;
                case 2:
                    str = "SSJ";
                    break;
                case 3:
                    str = "TX";
                    break;
                case 4:
                    str = "GZ";
                    break;
            }

            showCollection(str);
            myButtonGroup(temp.getType(), str);
        } else if (e.getSource() instanceof MyOutButton) {
            MyOutButton temp = (MyOutButton) e.getSource();
            String libstring = "";
            switch (temp.getType()) {
                case 1:
                    libstring = Configure.getJfstr();
                    break;
            }
            try {
                ProcessBuilder pb = new ProcessBuilder("tasklist");
                Process p = pb.start();
                BufferedReader out = new BufferedReader(new InputStreamReader(new BufferedInputStream(p.getInputStream()), Charset.forName("GB2312")));
                String ostr;
                while ((ostr = out.readLine()) != null) {
                    if (ostr.contains(libstring)) {
                        out.close();
                        return;
                    }
                }
                File file = new File(libstring);
                if (file.exists()) {
                    if (file.canWrite()) {
                        Runtime.getRuntime().exec(libstring);
                    }
                } else {
                    throw new FileNotFoundException();
                }
            } catch (FileNotFoundException e1) {
                JOptionPane.showMessageDialog(null, "文件不存在", "错误", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "文件无法打开", "错误", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        }
    }

    public void showButton(boolean istable) {
        buttonPanel.setVisible(istable);
    }

    private void myButtonGroup(ActionEvent e, String name) {
        CollectTitleButton jb = (CollectTitleButton) e.getSource();
        myButtonGroup(jb, name);
    }

    private void showCollection(String str) {
        centerCard.show(centerPanel, "数据采集");
        for (Component b : toolBar.getComponents()) {
            if (b instanceof MyTitleButton) {
                MyTitleButton myTitleButton = (MyTitleButton) b;
                if (myTitleButton.getText().equals("数据采集")) {
                    myTitleButton.setSelected(true);
                } else {
                    myTitleButton.setSelected(false);
                }
            }
        }
        ChartView.getInstance().showPane(str);
    }

    public void showPanel() {
        String str = "TX";
        showCollection(str);
        CollectTitleButton jb = (CollectTitleButton) buttonPanel.getComponent(3);
        myButtonGroup(jb, str);
    }

    public void showHitch(){
        String str = "GZ";
        showCollection(str);
        CollectTitleButton jb = (CollectTitleButton) buttonPanel.getComponent(4);
        myButtonGroup(jb, str);
    }

    private void myButtonGroup(CollectTitleButton jb, String name) {
        if (!jb.isSelected()) {
            jb.setSelected(true);
            for (Component b : buttonPanel.getComponents()) {
                if (b != jb && ((CollectTitleButton) b).isSelected()) {
                    ((CollectTitleButton) b).setSelected(false);
                }
            }
            ChartView.getInstance().showPane(name);
        }
    }

    private void myButtonGroup(int type, String name) {
        CollectTitleButton jb = (CollectTitleButton) buttonPanel.getComponent(type);
        myButtonGroup(jb, name);
    }

}
