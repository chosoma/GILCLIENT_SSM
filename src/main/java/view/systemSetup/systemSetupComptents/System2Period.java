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
//        jbtSet.setEnabled(isEditable);
    }

    private JComboBox<String> jcbPlace;
    private JComboBox<String> jcbXW;
    private JComboBox<Integer> jcbPeriod;
    //    private ClickButton jbtSet;
    private JLabel jlPrimaryPeriod;
    private JButton cancel, set, edit;

    private void init() {
//        JPanel center = new JPanel(new GridLayout(2, 1));
        JPanel center = new JPanel(null);
        center.setBorder(MyUtil.Component_Border);
        center.setBounds(0, 0, 500, 320);
        this.add(center);

        int x = 40;
        int y = 60;
        int yheight = 60;
        int height = 30;
        int widthlabel = 100;
        int widthcombox = 300;


        Vector<String> places = PointService.getPlaces();
        places.add("环境温度");
        JLabel jlPlace = new JLabel("测点:", JLabel.LEFT);
        jlPlace.setBounds(x, y, widthlabel, height);
        jlPlace.setFont(MyUtil.FONT_20);
        center.add(jlPlace);
        y += yheight;

        JLabel jlbXW = new JLabel("相位:", JLabel.LEFT);
        jlbXW.setFont(MyUtil.FONT_20);
        jlbXW.setBounds(x, y, widthlabel, height);
        center.add(jlbXW);
        y += yheight;

        JLabel jlPrimaryPeriod = new JLabel("当前周期:", JLabel.LEFT);
        jlPrimaryPeriod.setFont(MyUtil.FONT_20);
        jlPrimaryPeriod.setBounds(x, y, widthlabel, height);
        center.add(jlPrimaryPeriod);
        y += yheight;

        JLabel jlPeriod = new JLabel("周期选择:", JLabel.LEFT);
        jlPeriod.setFont(MyUtil.FONT_20);
        jlPeriod.setBounds(x, y, widthlabel, height);
        center.add(jlPeriod);


        x = 160;
        y = 60;
        jcbPlace = new JComboBox<String>(places);
        jcbPlace.setFont(MyUtil.FONT_20);
//        jcbPlace.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "测点", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, MyUtil.FONT_10, Color.BLACK));
        jcbPlace.setBounds(x, y, widthcombox, height);
        jcbPlace.setToolTipText((String) jcbPlace.getSelectedItem());
        jcbPlace.setEnabled(false);
        jcbPlace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jcbPlace.setToolTipText((String) jcbPlace.getSelectedItem());
                refreshPeriod();
            }
        });
        center.add(jcbPlace);
        y += yheight;
        jcbXW = new JComboBox<>(new String[]{"A", "B", "C"});
        jcbXW.setFont(MyUtil.FONT_20);
//        jcbXW.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "相位", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, MyUtil.FONT_10, Color.BLACK));
        jcbXW.setBounds(x, y, widthcombox, height);
        jcbXW.setEnabled(false);
        jcbXW.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshPeriod();
            }
        });
        center.add(jcbXW);
        y += yheight;

        x = 160;
        this.jlPrimaryPeriod = new JLabel();
//        jlPrimaryPeriod.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "当前周期", TitledBorder.LEFT, TitledBorder.TOP, MyUtil.FONT_15, Color.BLACK));
        this.jlPrimaryPeriod.setBounds(x, y, widthcombox - 40, height);
        this.jlPrimaryPeriod.setFont(MyUtil.FONT_20);
        center.add(this.jlPrimaryPeriod);
        x += widthcombox - 40;
        JLabel jldwthen = new JLabel("分", JLabel.CENTER);
        jldwthen.setFont(MyUtil.FONT_20);
        jldwthen.setBounds(x, y, 40, height);
        center.add(jldwthen);
        x = 160;
        y += yheight;
        Integer[] integer = new Integer[] {
        		10,20,30,45,60,120,240
        };

        jcbPeriod = new JComboBox<Integer>(integer);
//        jcbPeriod.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "周期选择", TitledBorder.LEFT, TitledBorder.TOP, MyUtil.FONT_15, Color.BLACK));
        jcbPeriod.setFont(MyUtil.FONT_20);
        jcbPeriod.setBounds(x, y, widthcombox - 40, height);
        jcbPeriod.setEnabled(false);
        center.add(jcbPeriod);

        x += widthcombox - 40;
        JLabel jldwnew = new JLabel("分", JLabel.CENTER);
        jldwnew.setFont(MyUtil.FONT_20);
        jldwnew.setBounds(x, y, 40, height);
        center.add(jldwnew);

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
//                if (!lock) {
//                    JOptionPane.showMessageDialog(null, "请先开启服务器", "提示", JOptionPane.INFORMATION_MESSAGE);
//                    return;
//                }
                refreshPeriod();
                setEditable(true);
                edit.setEnabled(false);
                set.setEnabled(true);
                cancel.setEnabled(true);
            }
        });
        toolbar.add(edit);

        set = new ChangeButton("设置", new ImageIcon("images/set.png"));
        set.setToolTipText("设置");
        set.setEnabled(false);
        set.setPreferredSize(buttonSize);
        set.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPeriod();
            }
        });
        toolbar.add(set);

        cancel = new ChangeButton("退出", new ImageIcon("images/cancel.png"));
        cancel.setToolTipText("退出修改");
        cancel.setEnabled(false);
        cancel.setPreferredSize(buttonSize);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEditable(false);
                edit.setEnabled(true);
                set.setEnabled(false);
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
            jlPrimaryPeriod.setText(String.valueOf(amtempUnit.getPeriod() & 0xff));
        } else {
        	jcbXW.setEnabled(true);
            if (edit.isEnabled()) {
                jcbPlace.setEnabled(false);
                jcbXW.setEnabled(false);
            }
//            else if (lock) {
//                jcbXW.setEnabled(true);
//            }
            List<UnitBean> unitBeans = UnitService.getUnit(place, xw);
            if (unitBeans.size() == 0) {
                return;
            }
            jlPrimaryPeriod.setText(String.valueOf(unitBeans.get(0).getPeriod() & 0xff));
        }
    }

    private void setPeriod() {
        if (CollectServer.getInstance().isNullLinked()) {
            JOptionPane.showMessageDialog(null, "当前无任何链接");
            return;
        }
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
