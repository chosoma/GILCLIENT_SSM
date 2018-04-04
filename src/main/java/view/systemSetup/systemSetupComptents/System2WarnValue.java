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
    private JButton jbtSet;
    private JButton cancel, edit;

    private void init() {
        JPanel center = new JPanel();
        center.setBorder(MyUtil.Component_Border);
        center.setBounds(0, 0, 500, 320);
        this.add(center);
//        JLabel jltype = new JLabel("监测点:", JLabel.RIGHT);
//        jltype.setBounds(25, 10, 50, 20);
//        this.add(jltype);

        Vector<String> vector = new Vector<>();
        vector.addAll(PointService.getPlaces());


        jcbPlace = new JComboBox<>(vector);
//        jcbPlace = new JComboBox<>(PointService.getHitchPlaces());
        jcbPlace.setBorder(new TitledBorder(new LineBorder(Color.gray, 2), "监测点", TitledBorder.CENTER, TitledBorder.TOP, MyUtil.FONT_20, Color.BLACK));
        jcbPlace.setFont(MyUtil.FONT_20);
//        jcbPlace.setBounds(75, 10, 100, 20);
        center.add(jcbPlace);
//        jcbPlace.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if(HitchUnitService.getHitchPlaces().contains(jcbPlace.getSelectedItem())){
//                    jcbXW.setEnabled(false);
//                }else {
//                    jcbXW.setEnabled(true);
//                }
//            }
//        });

//        JLabel jlnumber = new JLabel("相位:", JLabel.RIGHT);
//        jlnumber.setBounds(25, 40, 50, 20);
//        this.add(jlnumber);

        jcbXW = new JComboBox<>(new String[]{"A", "B", "C"});
        jcbXW.setBorder(new TitledBorder(new LineBorder(Color.gray, 2), "相位", TitledBorder.CENTER, TitledBorder.TOP, MyUtil.FONT_20, Color.BLACK));
        jcbXW.setFont(MyUtil.FONT_20);
//        jcbXW.setBounds(75, 40, 100, 20);
        center.add(jcbXW);

        jbtSet = new ClickButton("修改");
        jbtSet.setFont(MyUtil.FONT_20);
//        jbtSet.setBounds(84, 80, 80, 26);
        jbtSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });
        jcbPlace.setSelectedIndex(0);
        center.add(jbtSet);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbs = new GridBagConstraints();
        gbs.fill = GridBagConstraints.BOTH;
        gbs.gridwidth = 1;
        gbs.gridheight = 1;
        gbs.insets = new Insets(10, 10, 10, 10);
        gbs.gridx = 0;
        gbs.gridy = 0;
        gbl.setConstraints(jcbPlace, gbs);

        gbs.gridy++;
        gbl.setConstraints(jcbXW, gbs);

        gbs.gridy++;
        gbl.setConstraints(jbtSet, gbs);

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
        setEditable(false);

    }

    private void setEditable(boolean flag) {
        jcbPlace.setEnabled(flag);
        jcbXW.setEnabled(flag);
        jbtSet.setEnabled(flag);
    }

    public void refresh() {
        jcbPlace.removeAllItems();
        for (String place : PointService.getPlaces()) {
            jcbPlace.addItem(place);
        }

    }
}
