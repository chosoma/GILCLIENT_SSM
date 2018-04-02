package service;

import domain.DataBaseAttr;
import domain.PointBean;
import util.MyDbUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class PointService {
    private static List<PointBean> pointUnitList;
    private static List<PointBean> pointHitchList;

    public static List<PointBean> getPointUnitList() {
        return pointUnitList;
    }

    public static PointBean getHitchPoint(int point) {
        for (PointBean pointBean : pointHitchList) {
            if (pointBean.getPoint() == point) {
                return pointBean;
            }
        }
        return null;
    }

    public static PointBean getUnitPoint(String place) {
        for (PointBean pointBean : pointUnitList) {
            if (place.equals(pointBean.getPlace())) {
                return pointBean;
            }
        }
        return null;
    }

    public static void init() throws SQLException {
        pointUnitList = new ArrayList<>();
        pointHitchList = new ArrayList<>();
        pointUnitList.addAll(getUnitPoint());
        pointHitchList.addAll(getHitchLabelPoint());
        Collections.sort(pointUnitList);
    }

    public static void updatePlace(PointBean pointBean) throws SQLException {
        String sql = " update " + DataBaseAttr.PointTable + " set \n" + " place = ? where point = ? ";
        MyDbUtil.update(sql, pointBean.getPlace(), pointBean.getPoint());
        init();
    }

    public static List<PointBean> getUnitPoint() throws SQLException {
        String sql = " select p.point , place , x , y , gatewaytype , gatewaynumber , u.type as unittype\n" +
                " from " + DataBaseAttr.PointTable + " p , " + DataBaseAttr.UnitTable + " u\n" +
                " where u.point = p.point group by p.point";
        return MyDbUtil.queryBeanListData(sql, PointBean.class);
    }


    public static List<PointBean> getHitchLabelPoint() throws SQLException {
        String sql = "select point.place,point.point,point.x, point.y from point right join hitchunit on point.point = hitchunit.point group by point.point";
        return MyDbUtil.queryBeanListData(sql, PointBean.class);
    }


    public static Vector<String> getPlaces() {
        Vector<String> v = new Vector<>();
        for (PointBean point : pointUnitList) {
            if (!v.contains(point.getPlace())) {
                v.add(point.getPlace());
            }
        }
        return v;
    }

}
