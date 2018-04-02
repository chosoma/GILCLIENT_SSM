package view.systemSetup.systemSetupComptents;

import com.CollectServer;
import domain.UnitBean;
import mytools.ClickButton;
import mytools.ChangeButton;
import mytools.MyUtil;
import service.CollectService;
import service.PointService;
import service.UnitService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class System2Period extends JPanel {

    public System2Period() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(500, 360));
        this.setBorder(MyUtil.Component_Border);
        init();
        initToolbar();
    }

    private boolean lock;

    public void openlock(boolean flag) {
        lock = flag;
        if (!flag) {
            setEditable(false);
            edit.setEnabled(true);
            cancel.setEnabled(false);
        }
    }

    public void setEditable(boolean isEditable) {
        jcbPlace.setEnabled(isEditable);
        if (isEditable && "环境温度".equals(jcbPlace.getSelectedItem())) {
            jcbXW.setEnabled(false);
        } else {
            jcbXW.setEnabled(isEditable);
        }
        jcbPeriod.setEnabled(isEditable);
        jbtSet.setEnabled(isEditable);
    }

    private JComboBox<String> jcbPlace;
    private JComboBox<String> jcbXW;
    private JComboBox<Integer> jcbPeriod;
    private ClickButton jbtSet;
    private JLabel jlPrimaryPeriod;
    private JButton cancel, edit;

    private void init() {
        JPanel center = new JPanel(new GridLayout(2, 1));
        center.setBorder(MyUtil.Component_Border);
        center.setBounds(0, 0, 500, 320);
        this.add(center);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.gridy = 1;
        gbc.gridx = 1;

        JPanel jp1 = new JPanel(gbl);
        jp1.setOpaque(false);
        jp1.setBorder(new TitledBorder(new LineBorder(Color.gray, 2), "测点选择", TitledBorder.CENTER, TitledBorder.TOP, MyUtil.FONT_15, Color.BLACK));
        center.add(jp1);
        gbl.setConstraints(jp1, gbc);

//
//        JLabel jlbPlace = new JLabel("监测点:", JLabel.CENTER);
//        jlbPlace.setFont(MyUtil.FONT_20);
//        jp1.add(jlbPlace);
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbl.setConstraints(jlbPlace, gbc);
        Vector<String> places = PointService.getPlaces();
        places.add("环境温度");
        jcbPlace = new JComboBox<String>(places);
        jcbPlace.setFont(MyUtil.FONT_20);
        jcbPlace.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "测点", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, MyUtil.FONT_10, Color.BLACK));
        jcbPlace.setEnabled(false);
        jcbPlace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshPeriod();
            }
        });
        jp1.add(jcbPlace);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbl.setConstraints(jcbPlace, gbc);
//
//        JLabel jlbXW = new JLabel("相位:", JLabel.CENTER);
//        jlbXW.setFont(MyUtil.FONT_20);
//        jp1.add(jlbXW);
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbl.setConstraints(jlbXW, gbc);
        jcbXW = new JComboBox<>(new String[]{"A", "B", "C"});
        jcbXW.setFont(MyUtil.FONT_20);
        jcbXW.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "相位", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, MyUtil.FONT_10, Color.BLACK));
        jcbXW.setEnabled(false);
        jcbXW.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshPeriod();
            }
        });
        jp1.add(jcbXW);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbl.setConstraints(jcbXW, gbc);

        JPanel jp2 = new JPanel(gbl);
        jp2.setOpaque(false);
        jp2.setBorder(new TitledBorder(new LineBorder(Color.gray, 2), "采样周期", TitledBorder.CENTER, TitledBorder.TOP, MyUtil.FONT_15, Color.BLACK));
        center.add(jp2);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbl.setConstraints(jp2, gbc);

        jlPrimaryPeriod = new JLabel();
        jlPrimaryPeriod.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "当前周期", TitledBorder.LEFT, TitledBorder.TOP, MyUtil.FONT_15, Color.BLACK));
        jp2.add(jlPrimaryPeriod);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbl.setConstraints(jlPrimaryPeriod, gbc);

        Integer[] integer = new Integer[255];
        for (int i = 0; i < integer.length; i++) {
            integer[i] = i + 1;
        }

        jcbPeriod = new JComboBox<Integer>(integer);
        jcbPeriod.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "周期选择", TitledBorder.LEFT, TitledBorder.TOP, MyUtil.FONT_15, Color.BLACK));
        jcbPeriod.setFont(MyUtil.FONT_20);
        jcbPeriod.setEnabled(false);
        jp2.add(jcbPeriod);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbl.setConstraints(jcbPeriod, gbc);

        jbtSet = new ClickButton("设置");
        jbtSet.setName("set1");
        jbtSet.setEnabled(false);
        jbtSet.setFont(MyUtil.FONT_20);
        jbtSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CollectServer.getInstance().isNullLinked()) {
                    JOptionPane.showMessageDialog(null, "当前无任何链接");
                    return;
                }
                setPeriod();
            }
        });
        jp2.add(jbtSet);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbl.setConstraints(jbtSet, gbc);
        center.setLayout(gbl);
    }

    private void initToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        toolbar.setBounds(0, 319, 500, 41);
        toolbar.setBorder(MyUtil.Component_Border);
        this.add(toolbar);

        Dimension buttonSize = new Dimension(60, 30);

        edit = new ChangeButton("修改", new ImageIcon("images/edit.png"));
        edit.setToolTipText("修改连接设置");
        edit.setPreferredSize(buttonSize);
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!lock) {
                    JOptionPane.showMessageDialog(null, "请先开启服务器", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                refreshPeriod();
                setEditable(true);
                edit.setEnabled(false);
                cancel.setEnabled(true);
            }
        });
        toolbar.add(edit);


        cancel = new ChangeButton("退出", new ImageIcon("images/cancel.png"));
        cancel.setToolTipText("退出修改");
        cancel.setEnabled(false);
        cancel.setPreferredSize(buttonSize);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEditable(false);
                edit.setEnabled(true);
                cancel.setEnabled(false);
            }
        });
        toolbar.add(cancel);

    }

    public void refreshPeriod() {
        String place = (String) jcbPlace.getSelectedItem();
        String xw = (String) jcbXW.getSelectedItem();
        if ("环境温度".equals(place)) {
            jcbXW.setEnabled(false);
            UnitBean amtempUnit = UnitService.getInitTempUnit();
            if (amtempUnit == null) {
                return;
            }
            jlPrimaryPeriod.setText(String.valueOf(amtempUnit.getPeriod()&0xff));
        } else {
            if (lock) {
                jcbXW.setEnabled(true);
            }
            List<UnitBean> unitBeans = UnitService.getUnit(place, xw);
            if (unitBeans.size() == 0) {
                return;
            }
            jlPrimaryPeriod.setText(String.valueOf(unitBeans.get(0).getPeriod()&0xff));
        }
    }

    private void setPeriod() {
        String place = (String) jcbPlace.getSelectedItem();
        if ("环境温度".equals(place)) {
            UnitBean amtempUnit = UnitService.getInitTempUnit();
            if (amtempUnit == null) {
                return;
            }
            byte jg = (byte) (int) jcbPeriod.getSelectedItem();
            amtempUnit.setPeriod(jg);
            CollectService.setPeriod(amtempUnit);
        } else {
            String xw = (String) jcbXW.getSelectedItem();
            List<UnitBean> unitBeans = UnitService.getUnit(place, xw);
            byte jg = (byte) (int) jcbPeriod.getSelectedItem();
            for (UnitBean unit : unitBeans) {
                unit.setPeriod(jg);
                CollectService.setPeriod(unit);
            }
        }
    }

    public void refresh() {
        jcbPlace.removeAllItems();
        for (String place : PointService.getPlaces()) {
            jcbPlace.addItem(place);
        }
        jcbPlace.addItem("环境温度");
    }
}
