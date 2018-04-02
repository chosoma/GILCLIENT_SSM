package domain;

import service.UnitService;
import view.dataCollect.datacollect.AbcUnitView;

import java.util.ArrayList;
import java.util.List;

/**
 * 单元包
 */
public class UnitPacket {
    private List<UnitBean> units;
    private List<PointBean> points;
    private byte gatewaytype;
    private byte gatewaynumber;

    public UnitPacket() {
        units = new ArrayList<>();
        points = new ArrayList<>();
    }

    public UnitPacket(byte gt, byte gn) {
        this();
        this.gatewaytype = gt;
        this.gatewaynumber = gn;
    }

    public UnitPacket(NetBean netBean) {
        this();
        this.gatewaytype = netBean.getType();
        this.gatewaynumber = netBean.getNumber();
    }

    /**
     * 添加单元  成功返回true 失败返回false
     */
    public boolean addUnit(UnitBean unit) {
        if (units.contains(unit)) {
            units.remove(unit);
        }
        return units.add(unit);
    }

    public boolean addPoint(PointBean point) {
        if (points.contains(point)) {
            points.remove(point);
        }
        return points.add(point);
    }

    /**
     * 判断网关类型和编号 完全匹配返回true 否则返回false
     *
     * @param netType
     * @param netID
     * @return
     */
    public boolean match(byte netType, byte netID) {
        return this.gatewaytype == netType && this.gatewaynumber == netID;
    }


    public UnitBean getUnit(byte type, byte number) {
        for (UnitBean unitBean : units) {
            if (unitBean.getType() == type && unitBean.getNumber() == number) {
                return unitBean;
            }
        }
        return null;
    }

    public void clear() {
        units.clear();
    }

    public List<AbcUnitView> getAbcUnitViews() {
        List<AbcUnitView> views = new ArrayList<AbcUnitView>();

        for (PointBean point : points) {
            List<UnitBean> units = new ArrayList<>();
            for (UnitBean unit : UnitService.getUnitList()) {
                if (unit.getPoint() == point.getPoint()) {
                    units.add(unit);
                }
            }
            views.add(new AbcUnitView(point, units));
        }

        return views;
    }

    @Override
    public String toString() {
        return "UnitPacket{" +
                "gatewaytype=" + gatewaytype +
                ", gatewaynumber=" + gatewaynumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitPacket that = (UnitPacket) o;

        return gatewaytype == that.gatewaytype && gatewaynumber == that.gatewaynumber;
    }

    @Override
    public int hashCode() {
        int result = (int) gatewaytype;
        result = 31 * result + (int) gatewaynumber;
        return result;
    }
}
