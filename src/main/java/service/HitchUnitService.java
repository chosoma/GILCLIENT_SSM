package service;

import domain.DataBaseAttr;
import domain.HitchUnitBean;
import util.MyDbUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class HitchUnitService {
    private static List<HitchUnitBean> hitchUnitBeans;
    private static final String HitchUnitTable = "hitchunit";

    static {
        try {
            selectHitchUnit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<HitchUnitBean> getUnits(String place, String xw) {
        List<HitchUnitBean> hitchUnitBeans = new ArrayList<>();
        for (HitchUnitBean hitchUnitBean : HitchUnitService.hitchUnitBeans) {
            if (place == null || xw == null) {
                if (place == null && xw != null) {
                    if (hitchUnitBean.getXw().equals(xw)) {
                        hitchUnitBeans.add(hitchUnitBean);
                    }
                } else if (place != null) {
                    if (hitchUnitBean.getPlace().equals(place)) {
                        hitchUnitBeans.add(hitchUnitBean);
                    }
                } else {
                    hitchUnitBeans.add(hitchUnitBean);
                }
            } else if (hitchUnitBean.getPlace().equals(place) && hitchUnitBean.getXw().equals(xw)) {
                hitchUnitBeans.add(hitchUnitBean);
            }
        }
        return hitchUnitBeans;
    }

    public static Vector<String> getPlaces() {
        Vector<String> vector = new Vector<>();
        for (HitchUnitBean hitchUnitBean :
                hitchUnitBeans) {
            if (!vector.contains(hitchUnitBean.getPlace())) {
                vector.add(hitchUnitBean.getPlace());
            }
        }
        return vector;
    }

    /*
     *  R
     */
    private static void selectHitchUnit() throws SQLException {
        String stringBuilder = " select " +
                " p.point,p.place,u.xw,u.x,u.y,u.number,u.gatewaytype,u.gatewaynumber,u.vollevel,u.volwarn " +
                " from " + DataBaseAttr.PointTable + " p right join " + HitchUnitTable + " u on p.point = u.point";
        hitchUnitBeans = MyDbUtil.queryBeanListData(stringBuilder, HitchUnitBean.class);
    }

    /*
     *   U
     */
    public static void updateHitchUnit(HitchUnitBean hitchUnitBean) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" UPDATE \n");
        stringBuilder.append(HitchUnitTable);
        stringBuilder.append(" SET \n");
        List<Object> p = new ArrayList<>();
        stringBuilder.append(" vollevel = ? , volwarn = ? ");
        p.add(hitchUnitBean.getVollevel());
        p.add(hitchUnitBean.getVolwarn());
        stringBuilder.append(" WHERE number = ?");
        p.add(hitchUnitBean.getNumber());
        MyDbUtil.update(stringBuilder.toString(), p.toArray());
    }

    public static void updatePlace(HitchUnitBean hitchUnitBean) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" UPDATE \n");
        stringBuilder.append(DataBaseAttr.PointTable);
        stringBuilder.append(" SET \n");
        List<Object> p = new ArrayList<>();
        stringBuilder.append(" place = ? ");
        p.add(hitchUnitBean.getPlace());
        stringBuilder.append(" WHERE point = ?");
        p.add(hitchUnitBean.getPoint());
        MyDbUtil.update(stringBuilder.toString(), p.toArray());
    }

    public static void refresh(HitchUnitBean hitchUnitBean) {
        for (HitchUnitBean unit :
                hitchUnitBeans) {
            if (hitchUnitBean.getPoint() == unit.getPoint()) {
                unit.setPlace(hitchUnitBean.getPlace());
            }
        }
    }

    public static List<HitchUnitBean> getHitchUnitBeans() {
        return hitchUnitBeans;
    }

    public static HitchUnitBean getUnit(String place, String xw) {
        for (HitchUnitBean hitchUnitBean : HitchUnitService.hitchUnitBeans) {
            if (hitchUnitBean.getPlace().equals(place) && hitchUnitBean.getXw().equals(xw)) {
                return hitchUnitBean;
            }
        }
        return null;
    }

}
