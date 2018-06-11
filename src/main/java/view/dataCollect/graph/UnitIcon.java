package view.dataCollect.graph;


import data.FormatTransfer;
import domain.DataBean;
import domain.PointBean;
import domain.UnitBean;
import domain.WarnBean;
import view.icon.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class UnitIcon extends JLabel {

    private PointBean pointBean;
    private Color colorWarn = new Color(255, 80, 0);

    private UnitABCPanel panel;


    public PointBean getPointBean() {
        return pointBean;
    }


    private Color buttonColor = new Color(240, 240, 240);
    private LadderFrame ladderFrame;

    UnitABCPanel getjPanel() {
        return panel;
    }



    public UnitIcon(final PointBean pointBean) {
//        initTimer();
        panel = new UnitABCPanel(pointBean);
        this.setBackground(buttonColor);
        this.pointBean = pointBean;
        switch (pointBean.getUnitType()) {
            case 1:
                this.setIcon(new MinIcon(MyIconFactory.SF6_28));
                break;
            case 2:
                this.setIcon(new MinIcon(MyIconFactory.vari_28));
                break;
            case 3:
                this.setIcon(new MinIcon(MyIconFactory.temp_28));
                break;
        }

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                ladderFrame.setTitle(pointBean.getPlace());
                ladderFrame.setVisible(true);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setVisible(true);
                cancelTimer();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setVisible(false);
            }
        });

        ladderFrame = new LadderFrame(pointBean);
        ladderFrame.setUnitBeanList(panel.getUnitList());
    }


    void addUnit(UnitBean unitBean) {
        panel.addUnit(unitBean);
    }

    void alignZero(String xw) {
        panel.alignZero(xw);
    }


    void addData(DataBean dataBean) {
        panel.addData(dataBean);
    }


    public void removeWarning(WarnBean warnBean) {
        cancelTimer();
        panel.removeWarning(warnBean);
    }

    public void startWarn(WarnBean warnBean) {
        startTimer();
        panel.startWarn(warnBean);
    }

    private int index;

    private boolean flag = false;

    private void initTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (flag) {
                    index++;
                    if (index % 2 == 0) {
                        UnitIcon.this.setBackground(buttonColor);
                    } else {
                        UnitIcon.this.setBackground(colorWarn);
                    }
                } else {
                    UnitIcon.this.setBackground(buttonColor);
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 500);
    }

    private void startTimer() {
        setOpaque(true);
        flag = true;
    }

    private void cancelTimer() {
        setOpaque(false);
        flag = false;
    }


    public float getPointX() {
        return pointBean.getX();
    }

    public float getPointY() {
        return pointBean.getY();
    }

    @Override
    public int getWidth() {
        return 28;
    }

    @Override
    public int getHeight() {
        return 28;
    }
}
