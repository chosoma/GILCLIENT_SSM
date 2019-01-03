package view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.Configure;
import com.DefaultUI;
import com.LgoInfo;
import com.sun.awt.AWTUtilities;
import mytools.*;
import org.eclipse.swt.internal.win32.OS;
import org.sf.feeling.swt.win32.extension.Win32;
import org.sf.feeling.swt.win32.extension.hook.Keyboard_LLHook;
import org.sf.feeling.swt.win32.extension.hook.data.Keyboard_LLHookData;
import org.sf.feeling.swt.win32.extension.hook.data.Mouse_LLHookData;
import org.sf.feeling.swt.win32.extension.hook.interceptor.InterceptorFlag;
import org.sf.feeling.swt.win32.extension.hook.interceptor.Keyboard_LLHookInterceptor;
import org.sf.feeling.swt.win32.extension.hook.interceptor.Mouse_LLHookInterceptor;
import org.sf.feeling.swt.win32.extension.jna.datatype.win32.LPARAM;
import org.sf.feeling.swt.win32.extension.jna.exception.NativeException;
import org.sf.feeling.swt.win32.extension.jna.win32.structure.HWND;
import org.sf.feeling.swt.win32.extension.registry.RegistryKey;
import org.sf.feeling.swt.win32.extension.registry.RegistryValue;
import org.sf.feeling.swt.win32.extension.registry.RootKey;
import org.sf.feeling.swt.win32.extension.registry.ValueType;
import org.sf.feeling.swt.win32.extension.shell.ApplicationBar;
import org.sf.feeling.swt.win32.internal.extension.APPBARDATA;
import view.dataCollect.datacollect.ChartView;
import view.dataCollect.datacollect.CollectShow;
import view.debugs.Debugs;
import view.icon.CloseIcon;
import view.icon.MyIconFactory;
import view.icon.SetIcon;

import static org.apache.log4j.spi.Configurator.NULL;
import static org.eclipse.swt.internal.win32.OS.EnableWindow;
import static org.sf.feeling.swt.win32.extension.jna.win32.User32.FindWindow;
import static org.sf.feeling.swt.win32.internal.extension.Extension.SHAppBarMessage;


public class Shell extends JFrame implements ActionListener {

    // 虚线框
//	private MyDashedBorder myDashedBorder;
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
        this.initKeyListener();
        this.initWindow();

//        keyHook();
        this.initTop();

        this.initCenter();
    }

    private void initKeyListener() {

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
         AWTUtilities.setWindowOpaque(this, false);// 设置透明
        this.setSize(dimension);
		this.setLocationRelativeTo(null);
        //设置屏幕尺寸
//        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
//        this.setSize(d.width, d.height);
        CardLayout contentCard = new CardLayout();
        JPanel contentPane = new JPanel(contentCard);
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(44, 46, 54)));
        setContentPane(contentPane);

        try {
            // Image image = ImageIO.read(this.getClass().getResource("backGround.jpg"));
            Image image = ImageIO.read(this.getClass().getClassLoader().getResource("icon/indexGroud.png"));
            Image logo = ImageIO.read(this.getClass().getClassLoader().getResource("icon/logo.png"));

            normalpanel = new BackGroundPanel(image, logo);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        contentPane.add(normalpanel, "normal");

//		myDashedBorder = new MyDashedBorder();
//		myDashedBorder.setBounds(this.getBounds());
        // 窗体关闭弹出对话框提示："确定"、"取消"
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Shell.this.exitSys();
            }
        });
        // MouseInputListener listener = new MouseInputHandler(this);
        // addMouseListener(listener);
        // addMouseMotionListener(listener);
    }

    private void initTop() {
        // 顶部面板：放置标题面板和功能面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        normalpanel.add(topPanel, BorderLayout.NORTH);
//		topPanel.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (e.getClickCount() == 2) {
//					btnMax.doClick();
//				}
//			}
//
//			@Override
//			public void mousePressed(MouseEvent arg0) {
//				if (!isMaximized && isShowing()) {
//					lastPoint = arg0.getLocationOnScreen();// 记录鼠标坐标
//					myDashedBorder.setVisible(true);
//				}
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				if (!isMaximized & isDragged) {
//					isDragged = false;
//					Shell.this.setLocation(myDashedBorder.getLocation());
//				}
//				myDashedBorder.setVisible(false);
//			}
//		});
//		topPanel.addMouseMotionListener(new MouseMotionAdapter() {
//			@Override
//			public void mouseDragged(MouseEvent e) {
//				if (!isMaximized && isShowing()) {
//					isDragged = true;
//				Point location = myDashedBorder.getLocation();
//					Point tempPonit = e.getLocationOnScreen();
//					myDashedBorder.setLocation(location.x + tempPonit.x - lastPoint.x,
//							location.y + tempPonit.y - lastPoint.y);
//					lastPoint = tempPonit;
//				}
//			}
//		});

        // 标题面板：放置logo和窗口工具
        JPanel tiltlePanel = new JPanel(new BorderLayout());
        tiltlePanel.setOpaque(false);
        topPanel.add(tiltlePanel, BorderLayout.NORTH);

        JLabel log = new JLabel(" " + LgoInfo.SoftName);
        log.setForeground(Color.BLACK);
        log.setFont(MyUtil.FONT_20);
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

//		JButton btnMin = new JButton(new MinIcon());
//		btnMin.setToolTipText("最小化");
//		btnMin.setFocusable(false);
        // 无边框
//		btnMin.setBorder(null);
        // 取消绘制按钮内容区域
//		btnMin.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
//		btnMin.setFocusPainted(false);
//		right.add(btnMin);
//		btnMin.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
        // Shell.this.setExtendedState(JFrame.ICONIFIED);
        // 此处只能是Frame.setStatr(state),否则在最大化模式下最小化后，
        // 再点击状态栏图标就不能还原最大化,只能显示JFrame.NORMAL状态
//				Shell.this.setState(Frame.ICONIFIED);
//			}
//		});

//		btnMax = new JButton(new MaxIcon());
//		btnMax.setToolTipText("最大化");
//		btnMax.setFocusable(false);
        // 无边框
//		btnMax.setBorder(null);
        // 取消绘制按钮内容区域
//		btnMax.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
//		btnMax.setFocusPainted(false);
//		right.add(btnMax);
//		btnMax.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (isMaximized) {
        // Shell.this.setExtendedState(Frame.NORMAL);
//					Shell.this.setBounds(myDashedBorder.getBounds());
//					ShellState = Frame.NORMAL;
//					btnMax.setToolTipText("最大化");
        // ChartView.getInstance().getPanelGraph().repaint();
//				} else {
        // Shell.this.setExtendedState(Frame.MAXIMIZED_BOTH);
//					Shell.this.setBounds(getMaxBounds());
//					ShellState = Frame.MAXIMIZED_BOTH;
//					btnMax.setToolTipText("向下还原");
//				}
//				isMaximized = !isMaximized;
//				btnMax.setSelected(isMaximized);
//			}
//		});

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
                exitSys();
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

//		Dimension buttonsize = new Dimension(60, 24);

//		JButton jbSF6 = new CollectTitleButton("SF6");
//		jbSF6.setPreferredSize(buttonsize);
//		jbSF6.setSelected(true);
//		jbSF6.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				myButtonGroup(e, "SF6");
//			}
//		});
//		buttonPanel.add(jbSF6);
//
//		JButton jbWd = new CollectTitleButton("温度");
//		jbWd.setPreferredSize(buttonsize);
//		jbWd.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				myButtonGroup(e, "WD");
//			}
//		});
//		buttonPanel.add(jbWd);
//
//		JButton jbGy = new CollectTitleButton("伸缩节");
//		jbGy.setPreferredSize(buttonsize);
//		jbGy.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				myButtonGroup(e, "SSJ");
//			}
//		});
//		buttonPanel.add(jbGy);
//
//		JButton jbSw = new CollectTitleButton("图形");
//		jbSw.setPreferredSize(buttonsize);
//		jbSw.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				myButtonGroup(e, "TX");
//			}
//		});
//		buttonPanel.add(jbSw);
//
//		JButton jbHit = new CollectTitleButton("故障定位");
//		jbHit.setPreferredSize(buttonsize);
//		jbHit.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				myButtonGroup(e, "GZ");
//			}
//		});
//		buttonPanel.add(jbHit);


//		JLabel jLabel = new JLabel(new ImageIcon("images/logo.png"));
//		jLabel.setPreferredSize(new Dimension(20, 20));
//		jLabel.setFont(MyUtil.FONT_36);
        // jLabel.setEnabled(false);
//		jLabel.setForeground(Color.blue);
//jLabel.setVisible(true);
//		right.add(jLabel, FlowLayout.LEFT);

    }

    private JPanel buttonPanel;

    private MyTitleButton debugs;

    private void initSetPop() {
        pop = new JPopupMenu();

        JCheckBoxMenuItem show = new JCheckBoxMenuItem("调试界面", MyIconFactory.getShowDebugIcon());
        show.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean b = ((JCheckBoxMenuItem) e.getSource()).getState();
                if (debugs == null) {
                    debugs = (MyTitleButton) toolBar.getComponent(toolBar.getComponentCount() - 1);
                }
                if (!b && debugs.isSelected()) {
                    ((MyTitleButton) toolBar.getComponent(toolBar.getComponentCount() - 2)).doClick();
                }
                debugs.setVisible(b);
                Debugs.getInstance().setShow(b);
            }
        });
        pop.add(show);

        // JCheckBoxMenuItem voiceAlarm = new JCheckBoxMenuItem("声音报警",
        // MyIconFactory.getVoiceWarnIcon());
        // voiceAlarm.setSelected(Configure.isVioceWarn());
        // voiceAlarm.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // Configure.setVoiceWarn(((JCheckBoxMenuItem) e.getSource())
        // .getState());
        // }
        // });
        // pop.add(voiceAlarm);

        // JMenuItem help = new JMenuItem("帮 助 ", new
        // ImageIcon("images/main/help_16.png"));
        // help.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent e) {
        // try {
        // Runtime.getRuntime().exec("cmd /c start " + "help.chm");
        // } catch (IOException e1) {
        // e1.printStackTrace();
        // }
        // }
        // });
        // pop.add(help);
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
        new CloseDialog();
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
//                Shell.getInstance().setAlwaysOnTop(true);
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
            CollectShow.getInstance().myButtonGroup(temp.getType(), str);
        } else if (e.getSource() instanceof MyOutButton) {
            MyOutButton temp = (MyOutButton) e.getSource();
            String libstring = "";
            switch (temp.getType()) {
                case 1:
                    libstring = Configure.getJfstr();
                    break;
            }
            try {
//                ProcessBuilder pb = new ProcessBuilder("tasklist");
//                Process p = pb.start();
//                BufferedReader out = new BufferedReader(
//                        new InputStreamReader(new BufferedInputStream(p.getInputStream()), Charset.forName("GB2312")));
//                String ostr;
//                while ((ostr = out.readLine()) != null) {
//                    if (ostr.contains(libstring)) {
//                        out.close();
//                        return;
//                    }
//                }
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
        CollectShow.getInstance().myButtonGroup(3, str);
//        CollectTitleButton jb = (CollectTitleButton) buttonPanel.getComponent(3);
//        myButtonGroup(jb, str);
    }

    public void showHitch() {
        String str = "GZ";
        showCollection(str);
        CollectShow.getInstance().myButtonGroup(4, str);
//        CollectTitleButton jb = (CollectTitleButton) buttonPanel.getComponent(4);
//        myButtonGroup(jb, str);
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

    /*
    系统操作
     */
    public void enableWindow(boolean flag) {
        applicationBar1(flag);
        applicationBar2(flag);
    }

    /*
   启用或禁用任务栏 true  禁用  false 启用
    */
    private void applicationBar1(boolean flag) {
        HWND hwnd = null;
        try {
            hwnd = FindWindow("Shell_traywnd", null);
            //AutoHideTaskBar(0);
            //EnableWindow(hwnd, TRUE);
            if (!flag) {
                ApplicationBar.setAppBarState(0,
                        Win32.STATE_AUTOHIDE);
            } else {
                ApplicationBar.setAppBarState(0,
                        Win32.STATE_NONE);
            }
            EnableWindow(hwnd.getValue(), flag);
        } catch (NativeException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    /*
    启用或禁用任务管理器 true  禁用  false 启用
     */
    private void applicationBar2(boolean flag) {
        RootKey currentUser = RootKey.HKEY_CURRENT_USER;
        RegistryKey key = new RegistryKey(currentUser,
                "Software\\Microsoft\\Windows\\CurrentVersion\\Policies\\System");
        if (!key.exists()) {
            key.create();
        }
        RegistryValue value = new RegistryValue();
        value.setType(ValueType.REG_DWORD);
        if (!flag)
            value.setData(1);
        else
            value.setData(0);
        value.setName("DisableTaskmgr");
        key.setValue(value);
    }




    /*
    组合键
     */

    private void keyHook(){
        Keyboard_LLHook.addHookInterceptor(keyboard_LLHookInterceptor);
        if (!Keyboard_LLHook.isInstalled())
            Keyboard_LLHook.installHook();
    }



    private static Keyboard_LLHookInterceptor keyboard_LLHookInterceptor;
    private static Mouse_LLHookInterceptor mouse_LLHookInterceptor;

    static {

        keyboard_LLHookInterceptor = new Keyboard_LLHookInterceptor() {
            @Override
            public InterceptorFlag intercept(Keyboard_LLHookData hookData) {
                int vkCode = hookData.vkCode();
                System.out.println(vkCode);
                boolean isCtrlPressed = OS.GetKeyState(17) < 0 ? true : false;
                boolean isAltPressed = OS.GetKeyState(18) < 0 ? true : false;
                // 屏蔽windows键
                if (vkCode == 91) {
                    return InterceptorFlag.FALSE;
                }
                // 屏蔽ALT+ESC
                if (isAltPressed && vkCode == 27) {
                    return InterceptorFlag.FALSE;
                }
                // 屏蔽CTRL+ESC
                if (isCtrlPressed && vkCode == 27) {
                    return InterceptorFlag.FALSE;
                }
                // 屏蔽ALT+TAB
                if (isAltPressed && vkCode == 9) {
                    return InterceptorFlag.FALSE;
                }
                // 屏蔽ALT+F4
                if (isAltPressed && vkCode == 115) {
                    return InterceptorFlag.FALSE;
                }
                return InterceptorFlag.TRUE;
            }
        };
        mouse_LLHookInterceptor = new Mouse_LLHookInterceptor() {
            @Override
            public InterceptorFlag intercept(Mouse_LLHookData hookData) {
                return InterceptorFlag.FALSE;
            }
        };
    }


}
