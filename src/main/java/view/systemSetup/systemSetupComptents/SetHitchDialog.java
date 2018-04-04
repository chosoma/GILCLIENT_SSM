package view.systemSetup.systemSetupComptents;

import data.FormatTransfer;
import domain.HitchVolLevelBean;
import domain.UnitBean;
import mytools.ClickButton;
import service.HitchVolLevelService;
import service.UnitService;
import view.icon.CloseIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class SetHitchDialog extends JDialog {
    private UnitBean hitchUnitBean;

    private Point lastPoint;


    public SetHitchDialog(UnitBean hitchUnitBean) {
        this.hitchUnitBean = hitchUnitBean;
        this.level = hitchUnitBean.getVollevel();
        this.vol = hitchUnitBean.getVolwarn();
        initDefault();
    }

    private void initDefault() {
        setModal(true);// 设置对话框模式

        setUndecorated(true);// 去除JDialog边框

        JPanel contentPane = new JPanel(new BorderLayout());

        contentPane.setBorder(BorderFactory.createLineBorder(new Color(44, 46, 54)));

        this.setContentPane(contentPane);

        initlevel();

        int headheight = this.initHead();

        Dimension size = this.initContent();

        int bottomheight = this.initBottom();

        this.setSize(size.width + 10, size.height + headheight + bottomheight + 10);
        setLocationRelativeTo(null);// 居中
        this.setVisible(true);


    }


    private int initBottom() {
        JPanel bottomPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        bottomPane.setBackground(new Color(240, 240, 240));
        this.getContentPane().add(bottomPane, BorderLayout.SOUTH);

        JButton buttonSave = new ClickButton("保存", new ImageIcon(
                "images/apply.png"));
        buttonSave.setToolTipText("保存修改");
        buttonSave.setPreferredSize(new Dimension(75, 28));
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setPerameter();
                    JOptionPane.showMessageDialog(null, "设置成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    JOptionPane.showMessageDialog(null, "设置失败!", "失败", JOptionPane.ERROR_MESSAGE);
//                } catch (NumberFormatException nfe) {
//                    JOptionPane.showMessageDialog(null, "输入有误!", "错误", JOptionPane.ERROR_MESSAGE);
//                    return;
                }
                dispose();
                close();

            }
        });
        bottomPane.add(buttonSave);

        bottomPane.add(Box.createVerticalStrut(36));

        JButton buttonCancel = new ClickButton("取消", new ImageIcon(
                "images/cancel.png"));
        buttonCancel.setToolTipText("取消添加，并退出该窗口");
        buttonCancel.setPreferredSize(new Dimension(75, 28));
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                close();
            }
        });
        bottomPane.add(buttonCancel);
        return bottomPane.getPreferredSize().height;
    }

    private int initHead() {
        JPanel headPane = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color HeadC1 = new Color(115, 168, 240),
                        HeadC2 = new Color(136, 186, 205);
                g2.setPaint(new GradientPaint(0, 0, HeadC1, 0, getHeight() - 1,
                        HeadC2));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }

            public boolean isOpaque() {
                return false;
            }
        };

        headPane.setPreferredSize(new Dimension(headPane.getWidth(), 32));
        this.getContentPane().add(headPane, BorderLayout.NORTH);
        headPane.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent arg0) {
                lastPoint = arg0.getLocationOnScreen();// 记录鼠标坐标
            }
        });
        headPane.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point tempPonit = e.getLocationOnScreen();
                Point location = getLocation();
                setLocation(tempPonit.x - lastPoint.x + location.x, tempPonit.y
                        - lastPoint.y + location.y);
                lastPoint = tempPonit;
            }
        });

        JLabel title = new JLabel("报警值设置", new ImageIcon(
                "images/main/sensor_24.png"), JLabel.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("微软雅黑", Font.BOLD, 14));

        headPane.add(title, BorderLayout.WEST);

        JPanel headRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        headRight.setOpaque(false);
        headPane.add(headRight, BorderLayout.EAST);

        final JButton close = new JButton(new CloseIcon());
        close.setToolTipText("关闭");
        close.setFocusable(false);
        // 无边框
        close.setBorder(null);
        // 取消绘制按钮内容区域
        close.setContentAreaFilled(false);
        // 设置按钮按下后无虚线框
        close.setFocusPainted(false);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                close();
            }
        });
        headRight.add(close);
        return headPane.getPreferredSize().height;
    }

    private void setPerameter() throws SQLException {
        hitchUnitBean.setVollevel(level);
        hitchUnitBean.setVolwarn(vol);
        UnitService.updateWarning(hitchUnitBean);
    }

//    private static Insets textFieldInsets = new Insets(0, 0, 0, 5);


    private Dimension initContent() {
        JPanel centerPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridheight = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weighty = 1;
        gbc.weightx = 1;

        gbc.gridy = 0;
        JLabel jlbType = new JLabel("监测点:", JLabel.CENTER);
        centerPanel.add(jlbType);
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbl.setConstraints(jlbType, gbc);
        JLabel jlbplace = new JLabel(hitchUnitBean.getPlace(), JLabel.CENTER);
        centerPanel.add(jlbplace);
        gbc.gridx++;
        gbc.gridwidth = 2;
        gbl.setConstraints(jlbplace, gbc);


        gbc.gridy = 1;
        JLabel jlbType2 = new JLabel("相位:", JLabel.CENTER);
        centerPanel.add(jlbType2);
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbl.setConstraints(jlbType2, gbc);
        JLabel jlbxw = new JLabel(hitchUnitBean.getXw(), JLabel.CENTER);
        centerPanel.add(jlbxw);
        gbc.gridx++;
        gbc.gridwidth = 2;
        gbl.setConstraints(jlbxw, gbc);


        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JLabel warnlevel = new JLabel("报警等级:", JLabel.CENTER);
        centerPanel.add(warnlevel);
        gbl.setConstraints(warnlevel, gbc);
        gbc.gridy++;
        final JLabel warnvalue = new JLabel("报警值:", JLabel.CENTER);
        centerPanel.add(warnvalue);
        gbl.setConstraints(warnvalue, gbc);

        gbc.gridx++;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        final JComboBox<Byte> warnlevels = new JComboBox<>(HitchVolLevelService.getLevels());
        warnlevels.setSelectedItem(level);
        centerPanel.add(warnlevels);
        gbl.setConstraints(warnlevels, gbc);
        gbc.gridy++;
        final JLabel warnvalues = new JLabel(String.valueOf(vol), JLabel.CENTER);
        gbl.setConstraints(warnvalues, gbc);
        centerPanel.add(warnvalues);

        final ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                vol = HitchVolLevelService.getValue((Byte) warnlevels.getSelectedItem());
                level = (Byte) warnlevels.getSelectedItem();
                warnvalues.setText(String.valueOf(vol));
            }
        };
        warnlevels.addItemListener(itemListener);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        Thread tsub = new Thread(new Runnable() {
            @Override
            public void run() {
                while (tflag) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ItemListener[] itemListeners = warnlevels.getItemListeners();
                    boolean itemflag = true;
                    for (ItemListener item : itemListeners) {
                        if (item == itemListener) {
                            itemflag = false;
                        }
                    }
                    if (subflag) {
                        if (!itemflag) {
                            warnlevels.removeItemListener(itemListener);
                        }
                        sub();
                        warnvalues.setText(String.valueOf(vol));
                        checkPlusLevel();
                        warnlevels.setSelectedItem(level);
                    } else if (plusflag) {
                        if (!itemflag) {
                            warnlevels.removeItemListener(itemListener);
                        }
                        plus();
                        warnvalues.setText(String.valueOf(vol));
                        checkPlusLevel();
                        warnlevels.setSelectedItem(level);
                    } else {
                        if (itemflag) {
                            warnlevels.addItemListener(itemListener);
                        }
                    }
                }
            }
        });
        tsub.start();
        JButton jbsub = new JButton("-");
        jbsub.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                subflag = true;

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                subflag = false;
            }
        });
        gbl.setConstraints(jbsub, gbc);
        centerPanel.add(jbsub);

        gbc.gridx++;

        JButton jbplus = new JButton("+");
        jbplus.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                plusflag = true;
//                hitchUnitBean.plus();
//                warnvalues.setText(String.valueOf(hitchUnitBean.getVolwarn()));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                plusflag = false;
            }
        });
        gbl.setConstraints(jbplus, gbc);
        centerPanel.add(jbplus);

        centerPanel.setLayout(gbl);
        centerPanel.setSize(200, 150);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        return centerPanel.getSize();
    }

    private void setPeriod() {


    }

    private boolean plusflag, subflag, tflag = true;

    private void close() {
        tflag = false;
//        tplus.interrupt();
//        tsub.interrupt();
    }

    private float[] hitchVolLevelBeans;

    private void initlevel() {
        java.util.List<HitchVolLevelBean> hitchVolLevelBeanList = HitchVolLevelService.getHitchVolLevels();
        hitchVolLevelBeans = new float[10];
        for (int i = 0; i < hitchVolLevelBeans.length; i++) {
            for (HitchVolLevelBean vollevel : hitchVolLevelBeanList) {
                if (vollevel.getLevel() == i + 1) {
                    hitchVolLevelBeans[i] = vollevel.getVol();
                    break;
                }
            }
        }
    }

    private void checkPlusLevel() {
        for (byte i = 0; i < hitchVolLevelBeans.length; i++) {
            if (this.vol < hitchVolLevelBeans[i]) {
                level = i;
                return;
            }
        }
    }

    private byte level;
    private float vol;

    private void sub() {
        this.vol = FormatTransfer.newScale(vol, 0.1f);
        if (vol < 0) {
            vol = 0;
        }
    }

    private void plus() {
        this.vol = FormatTransfer.newScale(vol, -0.1f);
    }

}
