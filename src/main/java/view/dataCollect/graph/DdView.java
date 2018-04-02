package view.dataCollect.graph;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import domain.DataBean;
import domain.PointBean;
import domain.UnitBean;
import domain.WarnBean;
import service.PointService;
import service.UnitService;

public class DdView extends JPanel {


    private Image backgroud, dwimage;

    public DdView(Image backgroud) {
        this.setLayout(null);
        this.backgroud = backgroud;
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        try {
            dwimage = ImageIO.read(this.getClass().getClassLoader().getResource("icon/danwei.png"));
        } catch (IOException ignored) {

        }
        init2();

    }

    private List<UnitBean> units;


    public void alignZero(UnitBean unit) {
        for (UnitIcon show : showButtons) {
            if (show.getPointBean().getPoint() == unit.getPoint()) {
                show.alignZero(unit.getXw());
                break;
            }
        }
    }

    private void init2() {
        units = UnitService.getUnitList();
        List<PointBean> pointBeans = PointService.getPointUnitList();

        showButtons = new ArrayList<UnitIcon>();
        for (PointBean pointBean : pointBeans) {
            showButtons.add(new UnitIcon(pointBean));
        }


        for (UnitBean unitBean : units) {
            for (UnitIcon show : showButtons) {
                if (show.getPointBean().getPoint() == unitBean.getPoint()) {
                    show.addUnit(unitBean);
                }
            }
        }

        for (UnitIcon show : showButtons) {
            this.add(show.getjPanel(), 0);
            this.add(show);
        }
    }

    public void startWarn(WarnBean warnBean) {
        for (UnitIcon show : showButtons) {
            if (warnBean.getPointBean().equals(show.getPointBean())) {
                show.startWarn(warnBean);
                break;
            }
        }

    }

    public void removeWarning(WarnBean warnBean) {
        for (UnitIcon show : showButtons) {
            if (warnBean.getPointBean().equals(show.getPointBean())) {
                show.removeWarning(warnBean);
                break;
            }
        }

    }

    private List<UnitIcon> showButtons;

    public void addData(DataBean dataBean) {
        UnitBean unitBean = getUnit(dataBean);
        if (unitBean != null) {
            for (UnitIcon show : showButtons) {
                if (show.getPointBean().getPoint() == unitBean.getPoint()) {
                    show.addData(dataBean);
                    break;
                }
            }
        }
    }

    private UnitBean getUnit(DataBean dataBean) {
        UnitBean unit = null;
        for (UnitBean unitBean : units) {
            if (dataBean.getUnitType() == unitBean.getType() && dataBean.getUnitNumber() == unitBean.getNumber()) {
                unit = unitBean;
                break;
            }
        }
        return unit;
    }


    @Override
    protected void paintComponent(Graphics g) {
        if (backgroud != null) {
            g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);
        }
        if (dwimage != null) {
            g.drawImage(dwimage, getWidth() - 260, 10, 250, 30, this);
        }
        setBounds2();
    }

    private void setBounds2() {
        int width = getWidth();
        int height = getHeight();
        int panelwidth = 125;
        int panelheight = 225;

        for (UnitIcon show : showButtons) {
            show.setBounds((int) (width * show.getPointX() - 14), (int) (height * show.getPointY() - 14), show.getWidth(), show.getHeight());
            JPanel jpanel = show.getjPanel();
            int panelx = show.getX() + show.getWidth();
            int panely = show.getY() + show.getHeight();
            if (panelx + panelwidth > width && panely + panelheight > height) {
                panelx = show.getX() - panelwidth;
                panely = height - panelheight;
            } else if (panelx + panelwidth > width) {
                panelx = width - panelwidth;
            } else if (panely + panelheight > height) {
                panely = height - panelheight;
            }
            jpanel.setBounds(panelx, panely, panelwidth, panelheight);


        }
    }
}
