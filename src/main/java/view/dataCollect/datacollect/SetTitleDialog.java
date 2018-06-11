package view.dataCollect.datacollect;

import domain.PointBean;
import mytools.ClickButton;
import service.SensorService;
import service.PointService;
import view.dataCollect.datacollect.ChartView;
import view.dataCollect.graph.GraphView;
import view.icon.CloseIcon;
import view.systemSetup.SystemSetup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class SetTitleDialog extends JDialog {
    private JTextField jtftitle;
    private Point lastPoint;

    public SetTitleDialog(Window owner, final PointBean pointBean) {
        jtftitle = new JTextField();
        jtftitle.setText(pointBean.getPlace());
//        PointBean pointBean1 = pointBean;
        setModal(true);// 设置对话框模式
        setUndecorated(true);// 去除JDialog边框
        // 设置JDialog背景透明
        // AWTUtilities.setWindowOpaque(this, false);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(44, 46,
                54)));
        this.setContentPane(contentPane);

        JPanel headPane = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Color HeadC1 = new Color(90, 163, 146),
                        HeadC2 = new Color(22, 106, 90);
                g2.setPaint(new GradientPaint(0, 0, HeadC1, 0, getHeight() - 1, HeadC2));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }

            public boolean isOpaque() {
                return false;
            }
        };

        headPane.setPreferredSize(new Dimension(headPane.getWidth(), 32));
        contentPane.add(headPane, BorderLayout.NORTH);
        headPane.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent arg0) {
                lastPoint = arg0.getLocationOnScreen();// 记录鼠标坐标
            }
        });
        headPane.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point tempPonit = e.getLocationOnScreen();
                Point location = getLocation();
                setLocation(tempPonit.x - lastPoint.x + location.x, tempPonit.y - lastPoint.y + location.y);
                lastPoint = tempPonit;
            }
        });

        JLabel jltitle = new JLabel("监测点", new ImageIcon("images/main/sensor_24.png"), JLabel.LEFT);
        jltitle.setForeground(Color.WHITE);
        jltitle.setFont(new Font("微软雅黑", Font.BOLD, 14));

        headPane.add(jltitle, BorderLayout.WEST);

        JPanel headRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        headRight.setOpaque(false);
        headPane.add(headRight, BorderLayout.EAST);

        JButton close = new JButton(new CloseIcon());
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
            }
        });
        headRight.add(close);

        JPanel centerPane = initContent();
        Dimension size = centerPane.getSize();
        contentPane.add(centerPane, BorderLayout.CENTER);

        JPanel bottomPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 2));
        bottomPane.setBackground(new Color(240, 240, 240));
        contentPane.add(bottomPane, BorderLayout.SOUTH);

        JButton buttonSave = new ClickButton("保存", new ImageIcon(
                "images/apply.png"));
        buttonSave.setToolTipText("保存修改");
        buttonSave.setPreferredSize(new Dimension(75, 28));
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jtftitle.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "请输入监测位置名称", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                switch (pointBean.getUnitType()) {
                    case 1:
                        pointBean.setPlace("六氟化硫:" + jtftitle.getText());
                        break;
                    case 2:
                        pointBean.setPlace("伸缩节:" + jtftitle.getText());
                        break;
                    case 3:
                        pointBean.setPlace("温升:" + jtftitle.getText());
                        break;
                }
                try {
                    PointService.updatePlace(pointBean);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "修改失败", "失败", JOptionPane.ERROR_MESSAGE);
                    dispose();
                    return;
                }
                ChartView.getInstance().setTitle(pointBean);
                SensorService.setPlace(pointBean);
                SystemSetup.getInstance().refresh();
                JOptionPane.showMessageDialog(null, "修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
        bottomPane.add(buttonSave);

        bottomPane.add(Box.createVerticalStrut(36));

        JButton buttonCancel = new ClickButton("取消", new ImageIcon("images/cancel.png"));
        buttonCancel.setToolTipText("取消添加，并退出该窗口");
        buttonCancel.setPreferredSize(new Dimension(75, 28));
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bottomPane.add(buttonCancel);

        this.setSize(size.width + 10, size.height + headPane.getPreferredSize().height + bottomPane.getPreferredSize().height + 10);
        setLocationRelativeTo(owner);// 居中

        this.setVisible(true);
    }

    private JPanel initContent() {
        JPanel jPanel = new JPanel();
        jPanel.setSize(200, 40);
        jPanel.setLayout(null);
        JLabel jLabel = new JLabel("监测点:", JLabel.CENTER);
        jPanel.add(jLabel);
        jLabel.setBounds(10, 10, 80, 20);
        this.jtftitle.setBounds(80, 10, 100, 20);
        jPanel.add(jtftitle);
        return jPanel;
    }
}
