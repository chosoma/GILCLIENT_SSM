package view.dataCollect.datacollect;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;

import data.FormatTransfer;
import domain.PointBean;
import domain.UnitBean;
import domain.WarnBean;
import mytools.ClickButton;
import mytools.MyUtil;
import service.UnitService;
import domain.DataBean;
import service.WarningService;
import view.Shell;
import view.dataCollect.WarnPanel;

/**
 * 单元视图
 */
public class AbcUnitView extends JPanel {

    private List<UnitBean> units;
    private static float amtemp;

    public static float getAmtemp() {
        return amtemp;
    }

    public static void setAmtemp(float amtemp) {
        AbcUnitView.amtemp = amtemp;
    }

    private JLabel[] jlas = new JLabel[6]; //0 温度 1 密度 2 压力 3 电压 4 位移 5 校准
    private JLabel[] jlbs = new JLabel[6]; //0 温度 1 密度 2 压力 3 电压 4 位移 5 校准
    private JLabel[] jlcs = new JLabel[6]; //0 温度 1 密度 2 压力 3 电压 4 位移 5 校准
    private ClickButton[] jbsetinits = new ClickButton[3];
    private ClickButton jbreset;

    public void hiddenUser() {
        for (ClickButton button : jbsetinits) {
            if (button != null) {
                button.setEnabled(false);
            }
        }
        jbreset.setEnabled(false);
    }


    private PointBean pointBean;

    public PointBean getPointBean() {
        return pointBean;
    }


    public AbcUnitView(PointBean pointBean, List<UnitBean> units) {
        this.pointBean = pointBean;
        this.units = units;
        flags = new ArrayList<Boolean>();
        this.setLayout(null);
        this.setPreferredSize(abcsize);

        init2();
    }


    private DataBean[] dataBeans = new DataBean[3];
    private Color colorWarn = new Color(255, 80, 0);
    private Color colorB = new Color(255, 255, 255);


    private static Border border = MyUtil.Component_Border;


    private static Dimension abcsize = new Dimension(210, 110);
    private JLabel place;

    public void setTitle(String title) {
        place.setText(title);
    }

    private static Color colorTitle = new Color(182, 216, 245);// 138, 191, 237
    private static Color colorSubTitle = new Color(232, 242, 254);// 182, 216, 245


    private List<Boolean> flags;

    public void addData(DataBean data, boolean flag) {

        flags.clear();
        byte unitnumber = data.getUnitNumber();
        UnitBean unit = getUnitBean(unitnumber);
        if (unit == null) {
            return;
        }
        addData(unit, data);
        WarnBean warnBean = new WarnBean();
        initWarnBean(warnBean, unit, pointBean);

        if (flag) {
            checkWarning(warnBean, unit, data);
        }
        switch (unit.getXw()) {
            case "A":
                dataBeans[0] = data;
                break;
            case "B":
                dataBeans[1] = data;
                break;
            case "C":
                dataBeans[2] = data;
                break;
        }
        if (flags.size() > 0) {
            Shell.getInstance().showPanel();
            warnBean.setDate(data.getDate());
            CollectShow.getInstance().addWarning(warnBean);
            ChartView.getInstance().startWarn(warnBean);
            try {
                WarningService.saveWarn(warnBean);
            } catch (SQLException ignored) {

            }
        }
        repaint();
    }

    private void initWarnBean(WarnBean warnBean, UnitBean unit, PointBean pointBean) {
        warnBean.setPointBean(pointBean);
        warnBean.setXw(unit.getXw());
    }


    private void init2() {
        initTitle();
        int y = 40;
        for (int i = 0; i < jlas.length; i++) {
            jlas[i] = new JLabel("", JLabel.CENTER);
            jlas[i].setBorder(border);
            jlas[i].setOpaque(true);
            this.add(jlas[i]);
        }
        initValueLabel(jlas, y);
        y += 20;
        for (int i = 0; i < jlbs.length; i++) {
            jlbs[i] = new JLabel("", JLabel.CENTER);
            jlbs[i].setBorder(border);
            jlbs[i].setOpaque(true);
            this.add(jlbs[i]);
        }
        initValueLabel(jlbs, y);
        y += 20;
        for (int i = 0; i < jlcs.length; i++) {
            jlcs[i] = new JLabel("", JLabel.CENTER);
            jlcs[i].setBorder(border);
            jlcs[i].setOpaque(true);
            this.add(jlcs[i]);
        }
        initValueLabel(jlcs, y);
        if (pointBean.getUnitType() == 2) {
            setInitVari();
            y = 40;
            for (int i = 0, x = 160; i < jbsetinits.length; i++, y += 20) {
                jbsetinits[i] = new ClickButton("校零");
                jbsetinits[i].setBounds(x, y, 42, 23);
                this.add(jbsetinits[i]);
                final int index = i;
                jbsetinits[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        if (dataBeans[index] == null) {
                            JOptionPane.showMessageDialog(null, "请先获得初始值", "错误", JOptionPane.WARNING_MESSAGE);
                        } else if (dataBeans[index].getVari() > 125 || dataBeans[index].getVari() < 0) {
                            JOptionPane.showMessageDialog(null, "该数据无效,请重新获取", "错误", JOptionPane.WARNING_MESSAGE);
                        } else {
                            UnitBean unitBean = getUnitBean(index);
                            if (unitBean == null) {
                                return;
                            }
                            updateUnit(unitBean, index);
                        }
                    }
                });
            }
        }
    }


    private void initTitle() {
        place = new JLabel("", JLabel.CENTER);
        setTitle(pointBean.getPlace());
        place.setBounds(0, 0, 161, 21);
        place.setBorder(border);
        place.setBackground(colorTitle);
        place.setOpaque(true);
        this.add(place);

        jbreset = new ClickButton("修改");
        jbreset.setBounds(160, 0, 42, 23);
        jbreset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SetTitleDialog(null, pointBean);
            }
        });
        this.add(jbreset);
        JLabel[] jltitles = new JLabel[6];//0 温度 1 密度 2 压力 3 电压 4 位移 5 校准
        for (int i = 0; i < jltitles.length; i++) {
            String str = "";
            switch (i) {
                case 0:
                    switch (pointBean.getUnitType()) {
                        case 1:
                            str = "温度";
                            break;
                        case 3:
                            str = "温升";
                            break;
                    }
                    break;
                case 1:
                    str = "密度";
                    break;
                case 2:
                    str = "压力";
                    break;
                case 3:
                    str = "电压";
                    break;
                case 4:
                    str = "偏移量";
                    break;
                case 5:
                    switch (pointBean.getUnitType()) {
                        case 3:
                            str = "环境温度";
                            break;
                        case 2:
                            str = "基准";
                            break;
                    }
                    break;
            }
            jltitles[i] = new JLabel(str, JLabel.CENTER);
            jltitles[i].setBorder(border);
            jltitles[i].setBackground(colorSubTitle);
            jltitles[i].setOpaque(true);
        }
        int x = 0;
        int y = 20;
        int width = 41;
        int height = 21;
        JLabel jlbxw = new JLabel("相位", JLabel.CENTER);
        jlbxw.setBorder(border);
        jlbxw.setBackground(colorSubTitle);
        jlbxw.setOpaque(true);
        jlbxw.setBounds(x, y, width, height);
        x += 40;
        this.add(jlbxw);
        switch (this.pointBean.getUnitType()) {
            case 1:
                width = 61;
                for (int i = 1; i <= 3; i++, x += 60) {
                    if (i == 3) {
                        width = 41;
                    }
                    jltitles[i].setBounds(x, y, width, height);
                    this.add(jltitles[i]);
                }
                break;
            case 2:
                for (int i = 5; i >= 3; i--, x += 40) {
                    jltitles[i].setBounds(x, y, width, height);
                    this.add(jltitles[i]);
                }
                JLabel jLabel = new JLabel("");
                jLabel.setBorder(border);
                jLabel.setOpaque(true);
                jLabel.setBackground(colorSubTitle);
                jLabel.setBounds(x, y, width, height);
                this.add(jLabel);
                break;
            case 3:
                width = 61;
                jltitles[5].setBounds(x, y, width, height);
                x += width - 1;
                jltitles[0].setBounds(x, y, width, height);
                x += width - 1;
                jltitles[3].setBounds(x, y, 41, height);
                this.add(jltitles[0]);
                this.add(jltitles[3]);
                this.add(jltitles[5]);
                break;
        }

        JLabel[] jlxws = new JLabel[3];
        int xwx = 0;
        int xwy = 40;
        for (int i = 0; i < jlxws.length; i++, xwy += 20) {
            String str = "";
            switch (i) {
                case 0:
                    str = "A";
                    break;
                case 1:
                    str = "B";
                    break;
                case 2:
                    str = "C";
                    break;
            }
            jlxws[i] = new JLabel(str, JLabel.CENTER);
            jlxws[i].setBorder(border);
            jlxws[i].setOpaque(true);
            jlxws[i].setBackground(colorSubTitle);
            jlxws[i].setBounds(xwx, xwy, 41, 21);
            this.add(jlxws[i]);
        }
    }

    private void initValueLabel(JLabel[] jLabels, int y) {
        int x = 40;
        int width = 41;
        int height = 21;
        switch (this.pointBean.getUnitType()) {
            case 1:
                width = 61;
                for (int j = 1; j <= 3; j++, x += 60) {
                    if (j == 3) {
                        width = 41;
                    }
                    jLabels[j].setBounds(x, y, width, height);
                    this.add(jLabels[j]);
                }
                break;
            case 2:
                for (int j = 5; j >= 3; j--, x += 40) {
                    jLabels[j].setBounds(x, y, width, height);
                    this.add(jLabels[j]);
                }
                break;
            case 3:
                width = 61;
                jLabels[5].setBounds(x, y, width, height);
                x += width - 1;
                jLabels[0].setBounds(x, y, width, height);
                x += width - 1;
                jLabels[3].setBounds(x, y, 41, height);
                this.add(jLabels[0]);
                this.add(jLabels[3]);
                this.add(jLabels[5]);
                break;
        }
    }

    private void setInitVari() {
        if (pointBean.getUnitType() != 2) {
            return;
        }
        for (UnitBean unit : units) {
            float initvari = unit.getInitvari();
            getInitLabel(unit).setText(String.valueOf(initvari));
        }
    }

    public void setInitTemp(float temp) {
        setAmtemp(temp);
        jlas[5].setText(String.valueOf(temp));
        jlbs[5].setText(String.valueOf(temp));
        jlcs[5].setText(String.valueOf(temp));
    }

    private UnitBean getUnitBean(int index) {
        switch (index) {
            case 0:
                return getUnitBean("A");
            case 1:
                return getUnitBean("B");
            case 2:
                return getUnitBean("C");
        }
        return null;
    }

    private UnitBean getUnitBean(String xw) {
        for (UnitBean unit : units) {
            if (unit.getXw().equals(xw)) {
                return unit;
            }
        }
        return null;
    }

    private UnitBean getUnitBean(byte number) {
        for (UnitBean unit : units) {
            if (unit.getNumber() == number) {
                return unit;
            }
        }
        return null;
    }

    private void updateUnit(UnitBean unitBean, int index) {
        if (unitBean.getInitvari() != 0.0f) {
            int flag = JOptionPane.showConfirmDialog(null, "初始值已存在是否覆盖", "提示", JOptionPane.OK_CANCEL_OPTION);
            if (flag != JOptionPane.OK_OPTION) {
                return;
            }
        }
        try {
            UnitBean unit = UnitService.getUnitBean(unitBean.getType(), unitBean.getNumber());
            if (unit == null) {
                JOptionPane.showMessageDialog(null, "单元不存在,请先添加单元!", "设置失败", JOptionPane.WARNING_MESSAGE);
                return;
            }
            unit.setInitvari(dataBeans[index].getVari());
            UnitService.updateInitvari(unit);
            ChartView.getInstance().alignZero(unitBean);
            JOptionPane.showMessageDialog(null, "设置成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "设置失败,请稍后重试", "失败", JOptionPane.WARNING_MESSAGE);
            return;
        }
        getInitLabel(unitBean).setText(String.valueOf(unitBean.getInitvari()));
        getVariLabel(unitBean).setText("0.0");
        getVariLabel(unitBean).setBackground(colorB);
    }

    private void checkWarning(WarnBean warnBean, UnitBean unit, DataBean data) {
        switch (unit.getType()) {
            case 1:
                SF6CheckWarning(warnBean, unit, data);
                break;
            case 2:
                VariCheckWarning(warnBean, unit, data);
                break;
            case 3:
                TempCheckWarning(warnBean, unit, data);
                break;
        }
    }

    private void SF6CheckWarning(WarnBean warnBean, UnitBean unit, DataBean data) {
        if (data.isLowPres()) {
            warnBean.setInfo(" 低压报警");
        }
        if (data.isLowLock()) {
            warnBean.setInfo(" 低压闭锁");
        }

        boolean[] flags = new boolean[3];

        if (unit.getWarnTemp() != null && data.getTemp() > unit.getWarnTemp()) {
            flags[0] = true;
            warnBean.setInfo(" 温度过高");
        }

        if (unit.getMaxden() != null && unit.getMinden() != null) {
            if (data.getDen() > unit.getMaxden() || data.getDen() < unit.getMinden()) {
                flags[1] = true;
                if (unit.getMaxden() != null && unit.getMinden() != null) {
                    if (data.getDen() > unit.getMaxden()) {
                        warnBean.setInfo(" 密度过高");
                    } else if (data.getDen() < unit.getMinden()) {
                        warnBean.setInfo(" 密度过低");
                    }
                }
            }
        }
        if (unit.getMaxper() != null && unit.getMinper() != null) {
            if (data.getPres() > unit.getMaxper() || data.getPres() < unit.getMinper()) {
                flags[2] = true;
                if (unit.getMaxper() != null && unit.getMinper() != null) {
                    if (data.getPres() > unit.getMaxper()) {
                        warnBean.setInfo(" 压力过高");
                    } else if (data.getPres() < unit.getMinper()) {
                        warnBean.setInfo(" 压力过低");
                    }
                }
            }
        }
        for (int i = 0; i < flags.length; i++) { // 0 temp 1 den 2 per
            if (flags[i]) {
                this.flags.add(true);
                switch (unit.getXw()) {
                    case "A":
                        jlas[i].setBackground(colorWarn);
                        break;
                    case "B":
                        jlbs[i].setBackground(colorWarn);
                        break;
                    case "C":
                        jlcs[i].setBackground(colorWarn);
                        break;
                }
            } else {
                switch (unit.getXw()) {
                    case "A":
                        jlas[i].setBackground(colorB);
                        break;
                    case "B":
                        jlbs[i].setBackground(colorB);
                        break;
                    case "C":
                        jlcs[i].setBackground(colorB);
                        break;
                }
            }
        }
    }

    private void TempCheckWarning(WarnBean warnBean, UnitBean unit, DataBean data) {
        boolean flag = false;
//        if (data.isDisconnect() || data.getTemp() < -273) {
//            flag = true;
//            warningstr.append("\n--温度值无效");
//        } else
        if (unit.getWarnTemp() != null && data.getTemp() > unit.getWarnTemp()) {
            flag = true;
            warnBean.setInfo(" 温度过高");
        }
        if (flag) {
            flags.add(true);
            switch (unit.getXw()) {
                case "A":
                    jlas[0].setBackground(colorWarn);
                    break;
                case "B":
                    jlbs[0].setBackground(colorWarn);
                    break;
                case "C":
                    jlcs[0].setBackground(colorWarn);
                    break;
            }
        } else {
            switch (unit.getXw()) {
                case "A":
                    jlas[0].setBackground(colorB);
                    break;
                case "B":
                    jlbs[0].setBackground(colorB);
                    break;
                case "C":
                    jlcs[0].setBackground(colorB);
                    break;
            }
        }
    }

    private void VariCheckWarning(WarnBean warnBean, UnitBean unit, DataBean data) {
        boolean flag = false;
        float vari = FormatTransfer.newScale(data.getVari(), unit.getInitvari());
        if (unit.getMaxvari() != null && unit.getMinvari() != null) {
            if (vari > unit.getMaxvari()) {
                flag = true;
                warnBean.setInfo(" 超出最大范围");
            }
            if (vari < unit.getMinvari()) {
                flag = true;
                warnBean.setInfo(" 超出最小范围");
            }
        }
        JLabel jLabel = getVariLabel(unit);
        if (jLabel == null) {
            return;
        }
        if (flag) {
            flags.add(true);
            jLabel.setBackground(colorWarn);
        } else {
            jLabel.setBackground(colorB);
        }
    }

    private JLabel getTempLabel(UnitBean unit) {
        return getLabel(unit, 0);
    }

    private JLabel getDenLabel(UnitBean unit) {
        return getLabel(unit, 1);
    }

    private JLabel getPresLabel(UnitBean unit) {
        return getLabel(unit, 2);
    }

    private JLabel getBatlvLabel(UnitBean unit) {
        return getLabel(unit, 3);
    }

    private JLabel getVariLabel(UnitBean unit) {
        return getLabel(unit, 4);
    }

    private JLabel getInitLabel(UnitBean unit) {
        return getLabel(unit, 5);
    }

    private JLabel getLabel(UnitBean unit, int index) {
        switch (unit.getXw()) {
            case "A":
                return jlas[index];
            case "B":
                return jlbs[index];
            case "C":
                return jlcs[index];
        }
        return null;
    }

    private void addData(UnitBean unit, DataBean data) {

        switch (data.getUnitType()) {
            case 1:

                getTempLabel(unit).setText(String.valueOf(data.getTemp()));

                getPresLabel(unit).setText(String.valueOf(data.getPres()));
                if (data.isLowPres()) {
                    flags.add(true);
                }

                getDenLabel(unit).setText(String.valueOf(data.getDen()));
                if (data.isLowLock()) {
                    flags.add(true);
                }
                break;
            case 2:

                float vari = data.getVari();
                if (unit.getInitvari() != 0) {
                    vari = FormatTransfer.newScale(data.getVari(), unit.getInitvari());
                }
                getVariLabel(unit).setText(String.valueOf(vari));
                break;
            case 3:
                float temp = data.getTemp();
//                if (amtemp != 0) {
//                    temp = FormatTransfer.newScale(data.getTemp(), amtemp);
//                }
//                if (temp < 0) {
//                    temp = 0;
//                }
                getTempLabel(unit).setText(String.valueOf(temp));
                break;
            default:
                return;
        }
        getBatlvLabel(unit).setText(String.valueOf(data.getBatlv()));
        if (data.getBatlv() <= 5) {
            getBatlvLabel(unit).setBackground(new Color(255, 97, 95));
        } else if (data.getBatlv() <= 6) {
            getBatlvLabel(unit).setBackground(new Color(255, 194, 71));
        } else {
            getBatlvLabel(unit).setBackground(colorB);
        }
    }
}
