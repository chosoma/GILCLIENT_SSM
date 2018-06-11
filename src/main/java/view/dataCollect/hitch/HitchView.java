package view.dataCollect.hitch;

import com.Configure;
import data.DataFactory;
import domain.DataBean;
import domain.IconConfig;
import domain.UnitBean;
import domain.WarnBean;
import protocol.Protocol;
import service.PointService;
import service.UnitService;
import service.WarningService;
import view.Shell;
import view.dataCollect.datacollect.CollectShow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class HitchView extends JPanel {
    private Image backgroud;
    private java.util.List<UnitBean> hitchUnitBeans;

    public HitchView(Image backgroud) {
        this.setLayout(null);
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.backgroud = backgroud;
        hitchUnitBeans = UnitService.getUnitBeans(Protocol.UnitTypeHV);
        initHitchWarning();
        initHitchLabels();
    }


    private JPanel hitchWarningPanel;

    private void initHitchWarning() {
        hitchWarningPanel = new JPanel(new FlowLayout());
        hitchWarningPanel.add(new JLabel(new ImageIcon("images/main/warn_24.png")));
        hitchWarningPanel.add(new JLabel("发生故障", JLabel.CENTER));
        hitchWarningPanel.setVisible(false);
        this.add(hitchWarningPanel);
        hitchWarningPanel.setBounds(0, 0, 100, 33);
    }



    private java.util.List<HitchIcon> hitchIcons;

    private void initHitchLabels() {
        hitchIcons = new ArrayList<>();

        IconConfig[] iconConfigs = Configure.getIconConfigs();

        for (int i = 0; i < iconConfigs.length; i++) {
            Icon icon = new ImageIcon(iconConfigs[i].getIconname());
            UnitBean unit = getUnit(iconConfigs[i].getPoint(), iconConfigs[i].getXw());
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


    private UnitBean getUnit(int point, String xw) {
        for (UnitBean hitchunit :
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
    }


    private java.util.List<DataBean> hitchings = new ArrayList<DataBean>();

    public void checkHitch(DataBean hitchBean) {
        for (HitchIcon hitchicon : hitchIcons) {
            boolean flag = hitchicon.checkUnit(hitchBean);//true 报警,false 未报警
            if (flag) {
                hitchings.add(hitchBean);
                hitchWarningPanel.setVisible(true);
                hitchicon.setHitchIconLabelVisible(true);
                WarnBean warnBean = new WarnBean();
                warnBean.setPointBean(PointService.getUnitPoint(hitchicon.getPoint()));
                warnBean.setXw(hitchicon.getXw());
                warnBean.setDate(hitchBean.getDate());
                warnBean.setInfo("发生故障--故障值:" + String.valueOf(hitchBean.getHitchvol()));
                hitchicon.setHitchValue(String.valueOf(hitchBean.getHitchvol()));
                CollectShow.getInstance().addWarning(warnBean);
                DataFactory.getInstance().addWarning(warnBean);
//                try {
//					WarningService.saveWarn(warnBean);
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
                Shell.getInstance().showHitch();
            }
        }
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
                Iterator<DataBean> iterator = hitchings.iterator();
                while (iterator.hasNext()) {
                    DataBean hitchBean = iterator.next();
                    if (hitch.getUnittype() == hitchBean.getUnitType() && hitch.getUnitnumber() == hitchBean.getUnitNumber()) {
                        hitch.setHitchValue("无故障");
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
