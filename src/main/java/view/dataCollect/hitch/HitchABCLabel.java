package view.dataCollect.hitch;

import domain.DataBean;
import domain.PointBean;
import domain.UnitBean;
import domain.WarnBean;
import view.Shell;
import view.dataCollect.WarnPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * 带有相位的显示框
 */
public class HitchABCLabel extends JPanel {

    private PointBean pointBean;

    private java.util.List<UnitBean> hitchUnitBeanList;

    public HitchABCLabel(PointBean pointBean) {
        this.pointBean = pointBean;
        this.setLayout(new BorderLayout());
        hitchUnitBeanList = new ArrayList<>();
        init();
    }

    private JLabel jlA;
    private JLabel jlB;
    private JLabel jlC;

    private void init() {
        Font font = new Font(null, Font.PLAIN, 15);
        jlA = new JLabel("A:无故障", JLabel.CENTER);
        jlA.setFont(font);
        Color colora = new Color(236, 236, 0);
        jlA.setForeground(colora);
        jlB = new JLabel("B:无故障", JLabel.CENTER);
        jlB.setFont(font);
        Color colorb = new Color(50, 153, 102);
        jlB.setForeground(colorb);
        jlC = new JLabel("C:无故障", JLabel.CENTER);
        jlC.setFont(font);
        Color colorc = new Color(236, 0, 0);
        jlC.setForeground(colorc);

        JPanel center = new JPanel();
        center.setLayout(new GridLayout(3, 1));
        center.add(jlA);
        center.add(jlB);
        center.add(jlC);
        this.add(center, BorderLayout.CENTER);
    }

    public void checkHitch(DataBean hitchBean) {
        for (UnitBean unitbean :
                hitchUnitBeanList) {
            if (hitchBean.getUnitNumber() == unitbean.getNumber()) {
                String str;
                if (hitchBean.getHitchvol() >= unitbean.getVolwarn()) {
                    str = "故障";
                    WarnBean warnBean = new WarnBean();
                    warnBean.setPointBean(pointBean);
                    warnBean.setXw(unitbean.getXw());
                    warnBean.setDate(hitchBean.getDate());
                    warnBean.setInfo(str);
                    new WarnPanel(warnBean);
                    Shell.getInstance().showHitch();
                } else {
                    str = "无故障";
                }
                switch (unitbean.getXw()) {
                    case "A":
                        jlA.setText("A:" + str);
                        break;
                    case "B":
                        jlB.setText("B:" + str);
                        break;
                    case "C":
                        jlC.setText("C:" + str);
                        break;
                }
                break;
            }
        }
    }

    public void refresh(String xw) {
        switch (xw) {
            case "A":
                jlA.setText("A:无故障");
                break;
            case "B":
                jlB.setText("B:无故障");
                break;
            case "C":
                jlC.setText("C:无故障");
                break;
        }
    }

    public void addHitchUnit(UnitBean unitBean) {
        this.hitchUnitBeanList.add(unitBean);
    }

    public float getPointX() {
        return this.pointBean.getX();
    }

    public float getPointY() {
        return this.pointBean.getY();
    }


    public int getPoint() {
        return pointBean.getPoint();
    }
//
//    @Override
//    public int getWidth() {
//        return 125;
//    }
//
//    @Override
//    public int getHeight() {
//        return 225;
//    }
}
