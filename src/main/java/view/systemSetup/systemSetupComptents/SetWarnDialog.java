package view.systemSetup.systemSetupComptents;

import domain.UnitBean;
import mytools.ClickButton;
import service.UnitService;
import view.icon.CloseIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class SetWarnDialog extends JDialog {

    private UnitBean unitBean;
    private Point lastPoint;



    public SetWarnDialog(UnitBean unit) {
        unitBean = unit;
        initDefault();
    }

    private void initDefault() {

        setModal(true);// 设置对话框模式
        setUndecorated(true);// 去除JDialog边框
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createLineBorder(new Color(44, 46,
                54)));
        this.setContentPane(contentPane);

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
                try {
                    setPerameter();
                    JOptionPane.showMessageDialog(null, "设置成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                    JOptionPane.showMessageDialog(null, "设置失败!", "失败", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "输入有误!", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dispose();
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
            }
        });
        bottomPane.add(buttonCancel);

        this.setSize(size.width + 10, size.height + headPane.getPreferredSize().height + bottomPane.getPreferredSize().height + 10);
        setLocationRelativeTo(null);// 居中
        this.setVisible(true);

    }

    private JTextField jtftemp, jtfdenmax, jtfdenmin, jtfpermax, jtfpermin, jtfvarimax, jtfvarimin;
    private static Insets textFieldInsets = new Insets(0, 0, 0, 5);


    private JPanel initContent() {
        int indexx = 0;
        switch (unitBean.getType()) {
            case 1:
                indexx = 6;
                break;
            case 2:
                indexx = 4;
                break;
            case 3:
                indexx = 3;
                break;
        }

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
        JLabel jlbTypes = new JLabel(unitBean.getPlace(), JLabel.CENTER);
        centerPanel.add(jlbTypes);
        gbc.gridx++;
        gbc.gridwidth = 2;
        gbl.setConstraints(jlbTypes, gbc);
        if (unitBean != null) {
            gbc.gridy = 1;
            JLabel jlbNumber = new JLabel("相位:", JLabel.CENTER);
            centerPanel.add(jlbNumber);
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            gbl.setConstraints(jlbNumber, gbc);
            JLabel jlbNumbers = new JLabel(unitBean.getXw(), JLabel.CENTER);
            centerPanel.add(jlbNumbers);
            gbc.gridx++;
            gbc.gridwidth = 2;
            gbl.setConstraints(jlbNumbers, gbc);
        }
        if (unitBean.getType() == 1) {
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            JLabel jlbdenmax = new JLabel("密度最大值:", JLabel.CENTER);
            centerPanel.add(jlbdenmax);
            gbl.setConstraints(jlbdenmax, gbc);
            gbc.gridy++;
            JLabel jlbdenmin = new JLabel("密度最小值:", JLabel.CENTER);
            centerPanel.add(jlbdenmin);
            gbl.setConstraints(jlbdenmin, gbc);
            gbc.gridy++;
            JLabel jlbpermax = new JLabel("压力最大值:", JLabel.CENTER);
            centerPanel.add(jlbpermax);
            gbl.setConstraints(jlbpermax, gbc);
            gbc.gridy++;
            JLabel jlbpermin = new JLabel("压力最小值:", JLabel.CENTER);
            centerPanel.add(jlbpermin);
            gbl.setConstraints(jlbpermin, gbc);
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            jtfdenmax = new JTextField();
            jtfdenmax.setMargin(textFieldInsets);
            centerPanel.add(jtfdenmax);
            gbl.setConstraints(jtfdenmax, gbc);
            gbc.gridy++;
            jtfdenmin = new JTextField();
            jtfdenmin.setMargin(textFieldInsets);
            centerPanel.add(jtfdenmin);
            gbl.setConstraints(jtfdenmin, gbc);
            gbc.gridy++;
            jtfpermax = new JTextField();
            jtfpermax.setMargin(textFieldInsets);
            centerPanel.add(jtfpermax);
            gbl.setConstraints(jtfpermax, gbc);
            gbc.gridy++;
            jtfpermin = new JTextField();
            centerPanel.add(jtfpermin);
            gbl.setConstraints(jtfpermin, gbc);
            if (unitBean != null && unitBean.getMaxden() != null) {
                jtfdenmax.setText(String.valueOf(unitBean.getMaxden()));
            }
            if (unitBean != null && unitBean.getMinden() != null) {
                jtfdenmin.setText(String.valueOf(unitBean.getMinden()));
            }
            if (unitBean != null && unitBean.getMaxper() != null) {
                jtfpermax.setText(String.valueOf(unitBean.getMaxper()));
            }
            if (unitBean != null && unitBean.getMinper() != null) {
                jtfpermin.setText(String.valueOf(unitBean.getMinper()));
            }
        } else if (unitBean.getType() == 3) {
            gbc.gridy = 2;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            JLabel jlbtemp = new JLabel("温升:", JLabel.CENTER);
            centerPanel.add(jlbtemp);
            gbl.setConstraints(jlbtemp, gbc);
            gbc.gridx++;
            gbc.gridwidth = 2;
            jtftemp = new JTextField();
            jtftemp.setMargin(textFieldInsets);
            centerPanel.add(jtftemp);
            gbl.setConstraints(jtftemp, gbc);
            if (unitBean != null && unitBean.getWarnTemp() != null) {
                jtftemp.setText(String.valueOf(unitBean.getWarnTemp()));
            }
        } else {
            gbc.gridy = 2;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            JLabel jlbvarimax = new JLabel("移动最大值:", JLabel.CENTER);
            centerPanel.add(jlbvarimax);
            gbl.setConstraints(jlbvarimax, gbc);
            gbc.gridy++;
            JLabel jlbvarimin = new JLabel("移动最小值:", JLabel.CENTER);
            centerPanel.add(jlbvarimin);
            gbl.setConstraints(jlbvarimin, gbc);

            gbc.gridx++;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            jtfvarimax = new JTextField();
            jtfvarimax.setMargin(textFieldInsets);
            centerPanel.add(jtfvarimax);
            gbl.setConstraints(jtfvarimax, gbc);
            gbc.gridy++;
            jtfvarimin = new JTextField();
            jtfvarimin.setMargin(textFieldInsets);
            gbl.setConstraints(jtfvarimin, gbc);
            centerPanel.add(jtfvarimin);
            if (unitBean != null && unitBean.getMaxvari() != null) {
                jtfvarimax.setText(String.valueOf(unitBean.getMaxvari()));
            }
            if (unitBean != null && unitBean.getMinvari() != null) {
                jtfvarimin.setText(String.valueOf(unitBean.getMinvari()));
            }
        }
        centerPanel.setLayout(gbl);

        centerPanel.setSize(200, indexx * 30);
        return centerPanel;
    }

    private void setPerameter() throws NumberFormatException, SQLException {
//        UnitBean unitBean = new UnitBean();
//        String name = jlbTypes.getText();
//        unitBean.setUsername(name);
        for (UnitBean unit : UnitService.getUnitList()) {
//            if (this.unitBean != null) {
            if (!unit.equals(this.unitBean)) {
                continue;
            }


            switch (unit.getType()) {
                case 1:
                    if (jtfdenmax != null) {
                        float denmax = Float.parseFloat(jtfdenmax.getText());
                        unit.setMaxden(denmax);
                    }
                    if (jtfdenmin != null) {
                        float denmin = Float.parseFloat(jtfdenmin.getText());
                        unit.setMinden(denmin);
                    }
                    if (jtfpermax != null) {
                        float permax = Float.parseFloat(jtfpermax.getText());
                        unit.setMaxper(permax);
                    }
                    if (jtfpermin != null) {
                        float permin = Float.parseFloat(jtfpermin.getText());
                        unit.setMinper(permin);
                    }
                    if (jtftemp != null) {
                        float temp = Float.parseFloat(jtftemp.getText());
                        unit.setWarnTemp(temp);
                    }
                    if (unit.getMaxden() <= unit.getMinden()) {
                        throw new NumberFormatException();
                    }
                    if (unit.getMaxper() <= unit.getMinper()) {
                        throw new NumberFormatException();
                    }
                    break;
                case 3:
                    if (jtftemp != null) {
                        float temp = Float.parseFloat(jtftemp.getText());
                        unit.setWarnTemp(temp);
                    }
                    break;
                case 2:
                    if (jtfvarimax != null) {
                        float varimax = Float.parseFloat(jtfvarimax.getText());
                        unit.setMaxvari(varimax);
                    }
                    if (jtfvarimin != null) {
                        float varimin = Float.parseFloat(jtfvarimin.getText());
                        unit.setMinvari(varimin);
                    }
                    if (unit.getMaxvari() <= unit.getMinvari() || unit.getMaxvari() > 125 || unit.getMaxvari() < -125 || unit.getMinvari() > 125 || unit.getMinvari() < -125) {
                        throw new NumberFormatException();
                    }
                    break;
            }
            UnitService.updateWarning(unit);

        }
    }


}
