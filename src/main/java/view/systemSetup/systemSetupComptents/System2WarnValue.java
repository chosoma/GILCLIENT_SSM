package view.systemSetup.systemSetupComptents;

import domain.UnitBean;
import mytools.ClickButton;
import mytools.ChangeButton;
import mytools.MyUtil;
import service.PointService;
import service.UnitService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Vector;

public class System2WarnValue extends JPanel {
    public System2WarnValue() {
        this.setLayout(null);
//        this.setLayout(new GridLayout(3, 1, 100, 100));
        this.setPreferredSize(new Dimension(500, 360));
        this.setBorder(MyUtil.Component_Border);
        init();
        initToolbar();
    }


    private JComboBox<String> jcbPlace;
    private JComboBox<String> jcbXW;
    //    private JButton jbtSet;
    private JButton cancel, set, edit;

    private void init() {
        JPanel center = new JPanel(null);
        center.setBorder(MyUtil.Component_Border);
        center.setBounds(0, 0, 500, 320);
        this.add(center);

        int x = 40;
        int y = 100; int yheight = 60;
        int widthlabel = 100;
        int widthcombox = 300;
        int height = 30;

        JLabel jltype = new JLabel("测点:", JLabel.LEFT);
        jltype.setBounds(x, y, widthlabel, height);
        jltype.setFont(MyUtil.FONT_20);
        center.add(jltype);
        y += yheight;
        JLabel jlnumber = new JLabel("相位:", JLabel.LEFT);
        jlnumber.setBounds(x, y, widthlabel, height);
        jlnumber.setFont(MyUtil.FONT_20);
        center.add(jlnumber);

        y = 100;

        x = 160;
        Vector<String> vector = new Vector<>();
        vector.addAll(PointService.getPlaces());
        jcbPlace = new JComboBox<>(vector);
//        jcbPlace.setBorder(new TitledBorder(new LineBorder(Color.gray, 2), "监测点", TitledBorder.CENTER, TitledBorder.TOP, MyUtil.FONT_20, Color.BLACK));
        jcbPlace.setFont(MyUtil.FONT_20);
        jcbPlace.setToolTipText((String) jcbPlace.getSelectedItem());
        jcbPlace.setBounds(x, y, widthcombox, height);
        center.add(jcbPlace);
        y += yheight;

        jcbPlace.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                jcbPlace.setToolTipText((String) jcbPlace.getSelectedItem());
            }
        });
        jcbXW = new JComboBox<>(new String[]{"A", "B", "C"});
//        jcbXW.setBorder(new TitledBorder(new LineBorder(Color.gray, 2), "相位", TitledBorder.CENTER, TitledBorder.TOP, MyUtil.FONT_20, Color.BLACK));
        jcbXW.setFont(MyUtil.FONT_20);
        jcbXW.setBounds(x, y, widthcombox, height);
        center.add(jcbXW);


    }


    private void initToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        toolbar.setBounds(0, 319, 500, 41);
        toolbar.setBorder(MyUtil.Component_Border);
        this.add(toolbar);

        Dimension buttonSize = new Dimension(60, 30);

        edit = new ChangeButton("修改", new ImageIcon("images/edit.png"));
        edit.setPreferredSize(buttonSize);
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setEditable(true);
                edit.setEnabled(false);
                cancel.setEnabled(true);
                set.setEnabled(true);
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
                setDialog();
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
                cancel.setEnabled(false);
                set.setEnabled(false);
            }
        });
        toolbar.add(cancel);
        setEditable(false);

    }

    private void setDialog() {
        String place = (String) jcbPlace.getSelectedItem();
        if (place != null && place.equals("")) {
            JOptionPane.showMessageDialog(null, "请选择监测点", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String xw = (String) jcbXW.getSelectedItem();
        List<UnitBean> unitBeans = UnitService.getUnit(place, xw);
        for (UnitBean unit : unitBeans) {
            new SetWarnDialog(unit);
        }
    }

    private void setEditable(boolean flag) {
        jcbPlace.setEnabled(flag);
        jcbXW.setEnabled(flag);
//        jbtSet.setEnabled(flag);
    }

    public void refresh() {
        jcbPlace.removeAllItems();
        for (String place : PointService.getPlaces()) {
            jcbPlace.addItem(place);
        }

    }
}
