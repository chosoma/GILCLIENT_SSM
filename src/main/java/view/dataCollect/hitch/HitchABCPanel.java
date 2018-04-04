package view.dataCollect.hitch;


import domain.DataBean;
import domain.UnitBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 鼠标悬停在故障按钮上时显示的页面
 */
public class HitchABCPanel extends JPanel {

    private UnitBean unit;


    private JLabel showLabel;

    public HitchABCPanel(UnitBean unit) {
        this.unit = unit;
        this.setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        showLabel = new JLabel("", JLabel.CENTER);
        this.add(showLabel, BorderLayout.CENTER);
        this.setVisible(false);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setVisible(false);
            }
        });
        setInfo("无报警");
    }

    public void setInfo(DataBean hitchBean) {
        setInfo(String.valueOf(hitchBean.getHitchvol()));
    }

    public void setInfo(String string) {
        showLabel.setText("相位:" + unit.getXw() + ",报警值:" + string);
    }


    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 30;
    }
}
