package view.dataCollect.datacollect;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import domain.PointBean;
import domain.UnitBean;
import service.UnitService;
import view.ModifiedFlowLayout;
import domain.DataBean;

public class AbcView extends JPanel {

    private List<AbcUnitView> units;
    private JComponent content;

    public List<AbcUnitView> getUnits() {
        return units;
    }

    public AbcView() {
        units = new ArrayList<AbcUnitView>();

        content = new JPanel(new ModifiedFlowLayout(FlowLayout.LEFT, 6, 6));
//        content = new JPanel(gbl);
//        content.setBackground(Color.black);
        this.setLayout(new BorderLayout());
//        this.setLayout(gbl);
        this.add(content, BorderLayout.CENTER);
    }

    public void hidenUser() {
        for (AbcUnitView abc : units) {
            abc.hiddenUser();
        }
    }


    public void addAbcUnit(AbcUnitView unit) {
        units.add(unit);
        content.add(unit);
    }

    public void clearAbcUnits() {
        units.clear();
        content.removeAll();
    }

    public void setInitTemp(DataBean data) {
        for (AbcUnitView abcUnitView : units) {
            abcUnitView.setInitTemp(data.getTemp());
        }
    }

    public void addData(DataBean data,boolean flag) {
        UnitBean unit = UnitService.getUnitBean(data.getUnitType(), data.getUnitNumber());
        if (unit == null) {
            return;
        }
        for (AbcUnitView aunit : units) {
            if (unit.getPoint() == aunit.getPointBean().getPoint()) {
                aunit.addData(data,flag);
                break;
            }
        }
    }
    public void setTitle(PointBean pointBean) {
        for (AbcUnitView auv : units) {
            if (auv.getPointBean().getPoint() == pointBean.getPoint()) {
                auv.setTitle(pointBean.getPlace());
            }
        }
    }

}
