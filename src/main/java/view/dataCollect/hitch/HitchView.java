package view.dataCollect.hitch;

import com.Configure;
import domain.HitchBean;
import domain.HitchUnitBean;
import domain.IconConfig;
import domain.WarnBean;
import service.HitchUnitService;
import service.PointService;
import view.Shell;
import view.dataCollect.datacollect.CollectShow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

public class HitchView extends JPanel {
    private Image backgroud;
    private java.util.List<HitchUnitBean> hitchUnitBeans;

    public HitchView(Image backgroud) {
        this.setLayout(null);
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.backgroud = backgroud;
        hitchUnitBeans = HitchUnitService.getHitchUnitBeans();
        initHitchWarning();
        initHitchLabels();
//        initHitchABCLabels();
    }


    private JPanel hitchWarningPanel;

    private void initHitchWarning() {
        hitchWarningPanel = new JPanel(new FlowLayout());
        hitchWarningPanel.add(new JLabel(new ImageIcon("images/main/warn_24.png")));
        hitchWarningPanel.add(new JLabel("正在报警", JLabel.CENTER));
        hitchWarningPanel.setVisible(false);
//        hitchWarningPanel.setVisible(true);
        this.add(hitchWarningPanel);
        hitchWarningPanel.setBounds(0, 0, 100, 33);


    }


//    private java.util.List<HitchABCLabel> hitchABCLabels;

//    private void initHitchABCLabels() {
//        hitchABCLabels = new ArrayList<>();
//        try {
//            java.util.List<PointBean> pointBeans = PointService.getHitchLabelPoint();
//            for (PointBean pointBean : pointBeans) {
//                HitchABCLabel hitchABCLabel = new HitchABCLabel(pointBean);
//                this.add(hitchABCLabel);
//                hitchABCLabels.add(hitchABCLabel);
//                for (HitchUnitBean unitbean : hitchUnitBeans) {
//                    if (unitbean.getUnitPoint() == pointBean.getUnitPoint()) {
//                        hitchABCLabel.addHitchUnit(unitbean);
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    private java.util.List<HitchIcon> hitchIcons;

    private void initHitchLabels() {
        hitchIcons = new ArrayList<>();

        IconConfig[] iconConfigs = Configure.getIconConfigs();

        for (int i = 0; i < iconConfigs.length; i++) {
            Icon icon = new ImageIcon(iconConfigs[i].getIconname());
            HitchUnitBean unit = getUnit(iconConfigs[i].getPoint(), iconConfigs[i].getXw());
            if (unit == null) {
                System.out.println("未找到对应单元");
                continue;
            }
            hitchIcons.add(new HitchIcon(unit, new HitchIconLabel(icon, iconConfigs[i])));
        }

        for (final HitchIcon hitchLabel : hitchIcons) {
            this.add(hitchLabel);
            this.add(hitchLabel.getHitchInfoPanel());
            this.add(hitchLabel.getHitchIconLabel());
            hitchLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    new HitchTitleDialog(null, hitchLabel.getHitchUnitBean());
                }
            });
        }
        initHitchMouseListener();
    }


    private HitchUnitBean getUnit(int point, String xw) {
        for (HitchUnitBean hitchunit :
                hitchUnitBeans) {
            if (point == hitchunit.getPoint() && xw.equals(hitchunit.getXw())) {
                return hitchunit;
            }
        }
        return null;
    }


    /*
    给图标按钮添加鼠标时间
     */
    private void initHitchMouseListener() {
        for (final HitchIcon hitchLabel : hitchIcons) {
            this.add(hitchLabel);
            hitchLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hitchLabel.setInfoPanelBounds(hitchLabel.getX(), hitchLabel.getY());
                    hitchLabel.setInfoPanelVisible(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hitchLabel.setInfoPanelVisible(false);
                }
            });
        }
    }

    ;


    @Override
    protected void paintComponent(Graphics g) {
        if (backgroud != null) {
            g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);
        }
        for (HitchIcon hitchLabel : hitchIcons) {
            hitchLabel.setBounds((int) (this.getWidth() * hitchLabel.getHitchX()) - 10, (int) (this.getHeight() * hitchLabel.getHitchY()) - 10, hitchLabel.getWidth(), hitchLabel.getHeight());
            hitchLabel.getHitchIconLabel().setIconBounds(getWidth(), getHeight(), getWidth(), getHeight());
        }
//        for (HitchABCLabel hitchLabel : hitchABCLabels) {
//            hitchLabel.setBounds((int) (this.getWidth() * hitchLabel.getPointX()), (int) (this.getHeight() * hitchLabel.getPointY()), (int) (125.0 / 1600 * getWidth()), (int) (225.0 / 600 * getHeight()));
//            hitchLabel.updateUI();
//        }
    }


    private java.util.List<HitchBean> hitchings = new ArrayList<HitchBean>();

    public void checkHitch(HitchBean hitchBean) {
        for (HitchIcon hitchicon : hitchIcons) {
            boolean flag = hitchicon.checkUnit(hitchBean);//true 报警,false 未报警
            if (flag) {
                hitchings.add(hitchBean);
                hitchWarningPanel.setVisible(true);
                hitchicon.setHitchIconLabelVisible(true);
                WarnBean warnBean = new WarnBean();
                warnBean.setPointBean(PointService.getHitchPoint(hitchicon.getPoint()));
                warnBean.setXw(hitchicon.getXw());
                warnBean.setDate(hitchBean.getDate());
                warnBean.setInfo("故障值:" + String.valueOf(hitchBean.getVol()));
                hitchicon.setHitchValue(String.valueOf(hitchBean.getVol()));
                CollectShow.getInstance().addWarning(warnBean);
                Shell.getInstance().showHitch();
            }
        }
//        for (HitchABCLabel hitchLabel : hitchABCLabels) {
//            hitchLabel.checkHitch(hitchBean);
//        }
    }

    public void hiddenUser() {
        for (HitchIcon hitchLabel : hitchIcons) {
            for (MouseListener mouseListener : hitchLabel.getMouseListeners())
                hitchLabel.removeMouseListener(mouseListener);
        }
        initHitchMouseListener();
    }

    public void removeWarning(WarnBean warnBean) {
        for (HitchIcon hitch : hitchIcons) {
            if (hitch.getHitchUnitBean().getPoint() == warnBean.getPointBean().getPoint() && hitch.getHitchUnitBean().getXw().equals(warnBean.getXw())) {
                hitch.setSafe();
                Iterator<HitchBean> iterator = hitchings.iterator();
                while (iterator.hasNext()) {
                    HitchBean hitchBean = iterator.next();
                    if (hitch.getUnittype() == hitchBean.getUnittype() && hitch.getUnitnumber() == hitchBean.getUnitnumber()) {
                        hitch.setHitchValue("无报警");
                        hitch.setHitchIconLabelVisible(false);
                        iterator.remove();
                    }
                }
            }
        }

        if (hitchings.size() == 0) {
            hitchWarningPanel.setVisible(false);
        }


//        for (HitchABCLabel hitchLabel : hitchABCLabels) {
//            if (hitchLabel.getUnitPoint() == warnBean.getPointBean().getUnitPoint()) {
//                hitchLabel.refresh(warnBean.getXw());
//            }
//        }
    }
}
