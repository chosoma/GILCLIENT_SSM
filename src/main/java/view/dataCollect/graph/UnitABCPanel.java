package view.dataCollect.graph;

import data.FormatTransfer;
import domain.DataBean;
import domain.PointBean;
import domain.UnitBean;
import domain.WarnBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UnitABCPanel extends JPanel {
    private List<UnitBean> unitList;
    private PointBean point;
    private JLabel title, jlA, jlB, jlC;
    private Color colorWarn = new Color(255, 80, 0), colorB = new Color(255, 255, 255), buttonColor = new Color(240, 240, 240);

    public int getTitleWidth() {

        int width = point.getPlace().length() * 25;
        return width < 125 ? 125 : width;
    }

    public UnitABCPanel(PointBean point) {
        this.point = point;

        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setVisible(false);
        unitList = new ArrayList<>();

        initABC();

    }

    public void setTitle() {
        title.setText(point.getPlace());
    }

    private void initABC() {

        Font font = new Font(null, Font.PLAIN, 25);

        title = new JLabel(point.getPlace(), JLabel.CENTER);
        title.setFont(font);
        jlA = new JLabel("A相", JLabel.CENTER);
        jlA.setFont(font);
        Color colora = new Color(236, 236, 0);
        jlA.setForeground(colora);
        jlB = new JLabel("B相", JLabel.CENTER);
        jlB.setFont(font);
        Color colorb = new Color(50, 153, 102);
        jlB.setForeground(colorb);
        jlC = new JLabel("C相", JLabel.CENTER);
        jlC.setFont(font);
        Color colorc = new Color(236, 0, 0);
        jlC.setForeground(colorc);

        setLayout(new GridLayout(4, 1));
        add(title);
        add(jlA);
        add(jlB);
        add(jlC);
    }

    public List<UnitBean> getUnitList() {
        return unitList;
    }

    void addUnit(UnitBean unitBean) {
        unitList.add(unitBean);
    }

    void alignZero(String xw) {
        switch (xw) {
            case "A":
                jlA.setText("A相:0.0");
                break;
            case "B":
                jlB.setText("B相:0.0");
                break;
            case "C":
                jlC.setText("C相:0.0");
                break;
        }
    }

    void addData(DataBean dataBean) {
        String str = "××";
        switch (dataBean.getUnitType()) {
            case 1:
//                if (dataBean.isLowPres() || dataBean.isLowLock()) {
//                    break;
//                }
//                if (dataBean.getPres() >= 0) {
                str = String.valueOf(dataBean.getPres());
//                }
                break;
            case 2:
                float vari = dataBean.getVari();
                if (vari < 0) {
                    break;
                }
                UnitBean unit = getUnit(dataBean);
                if (unit != null) {
                    vari = FormatTransfer.newScale(vari, unit.getInitvari());
                }
                str = String.valueOf(vari);
                break;
            case 3:
                str = String.valueOf(dataBean.getTemp());
                break;
        }

        String xw = getXw(dataBean);
        switch (xw) {
            case "A":
                jlA.setText("A相:" + str);
                break;
            case "B":
                jlB.setText("B相:" + str);
                break;
            case "C":
                jlC.setText("C相:" + str);
                break;
        }
    }

    private String getXw(DataBean dataBean) {
        String xw = "";
        UnitBean unit = getUnit(dataBean);
        if (unit != null) {
            xw = unit.getXw();
        }
        return xw;
    }

    private UnitBean getUnit(DataBean dataBean) {
        for (UnitBean unit : unitList) {
            if (dataBean.getUnitType() == unit.getType() && dataBean.getUnitNumber() == unit.getNumber()) {
                return unit;
            }
        }
        return null;
    }

    public void removeWarning(WarnBean warnBean) {
        switch (warnBean.getXw()) {
            case "A":
                jlA.setBackground(colorB);
                break;
            case "B":
                jlB.setBackground(colorB);
                break;
            case "C":
                jlC.setBackground(colorB);
                break;
        }
    }

    public void startWarn(WarnBean warnBean) {
        switch (warnBean.getXw()) {
            case "A":
                jlA.setOpaque(true);
                jlA.setBackground(colorWarn);
                break;
            case "B":
                jlB.setOpaque(true);
                jlB.setBackground(colorWarn);
                break;
            case "C":
                jlC.setOpaque(true);
                jlC.setBackground(colorWarn);
                break;
        }
    }


}
