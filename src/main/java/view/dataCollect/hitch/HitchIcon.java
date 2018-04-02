package view.dataCollect.hitch;

import domain.HitchBean;
import domain.HitchUnitBean;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * 故障图标
 */
public class HitchIcon extends JLabel {
    private HitchUnitBean hitchUnitBean;
    private HitchABCPanel hitchInfoPanel;//鼠标悬停时的显示页面
    private HitchIconLabel hitchIconLabel;


    public HitchIcon(HitchUnitBean hitchUnitBean, HitchIconLabel hitchIconLabel) {
        this.hitchInfoPanel = new HitchABCPanel(hitchUnitBean);
        this.hitchUnitBean = hitchUnitBean;
        this.hitchIconLabel = hitchIconLabel;
        setSafe();
    }

    public HitchIconLabel getHitchIconLabel() {
        return hitchIconLabel;
    }

    public HitchABCPanel getHitchInfoPanel() {
        return hitchInfoPanel;
    }

    public HitchUnitBean getHitchUnitBean() {
        return hitchUnitBean;
    }

    public float getHitchX() {
        return hitchUnitBean.getX();
    }

    public float getHitchY() {
        return hitchUnitBean.getY();
    }

    public void setSafe() {
        this.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("icon/hitchsafe.png")));
    }

    private void setWarn() {
        this.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("icon/hitchwarn.png")));
    }

    private void setError() {
        this.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("icon/hitcherror.png")));
    }

    boolean checkUnit(HitchBean hitchBean) { //true 报警, false 未报警
        if (hitchUnitBean.getNumber() == hitchBean.getUnitnumber()) {
            if (hitchBean.getVol() >= hitchUnitBean.getVolwarn()) {
                setError();
                return true;
            } else if (hitchBean.getVol() >= hitchUnitBean.getVolwarn() * 0.9) {
                setWarn();
                return false;
            } else {
                setSafe();
                return false;
            }
        }
        return false;
    }

    public void setHitchValue(String value) {
        hitchInfoPanel.setInfo(value);
    }


    public void setInfoPanelBounds(int x, int y) {
        hitchInfoPanel.setBounds(x + getWidth(), y + getHeight(), hitchInfoPanel.getWidth(), hitchInfoPanel.getHeight());
    }

    public void setInfoPanelVisible(boolean flag) {
        hitchInfoPanel.setVisible(flag);
    }

    public void setHitchIconLabelVisible(boolean flag) {
        hitchIconLabel.setVisible(flag);
    }

    public byte getUnittype() {
        return hitchUnitBean.getType();
    }

    public byte getUnitnumber() {
        return hitchUnitBean.getNumber();
    }

    public int getPoint() {
        return hitchUnitBean.getPoint();
    }

    public String getXw() {
        return hitchUnitBean.getXw();
    }

    @Override
    public int getWidth() {
        return 20;
    }

    @Override
    public int getHeight() {
        return 20;
    }
}
