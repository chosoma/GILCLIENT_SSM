package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import domain.DataSearchPara;
import util.MyDbUtil;

import domain.DataBaseAttr;
import domain.UnitBean;

public class UnitService {
    private static List<UnitBean> unitList;


    public static List<UnitBean> getUnitList() {
        return unitList;
    }

    public static UnitBean getUnitBean(byte type, byte number) {
        for (UnitBean unitBean : unitList) {
            if (type == unitBean.getType() && number == unitBean.getNumber()) {
                return unitBean;
            }
        }
        return null;
    }


    public static Vector<UnitBean> getUnitBeans(byte type) {
        Vector<UnitBean> beans = new Vector<UnitBean>();
        for (UnitBean unitBean : unitList) {
            if (type == unitBean.getType()) {
                beans.add(unitBean);
            }
        }
        return beans;
    }

    public static UnitBean getInitTempUnit() {
        for (UnitBean unit : unitList) {
            if (unit.isInittemp()) {
                return unit;
            }
        }
        return null;
    }

    public static List<UnitBean> getUnit(String place, String xw) {
        System.out.println(place);
        System.out.println(xw);
        List<UnitBean> units = new ArrayList<>();
        for (UnitBean unit : unitList) {
            if (xw != null && "".equals(xw)) {
                if (unit.getPlace().equals(place)) {
                    units.add(unit);
                }
            } else if (unit.getPlace() != null && unit.getPlace().equals(place) && unit.getXw().equals(xw)) {
                units.add(unit);
            }
        }
        return units;
    }

    public static UnitBean getUnit(int point, String xw) {
        for (UnitBean unit : unitList) {
            if (point == unit.getPoint() && xw.equals(unit.getXw())) {
                return unit;
            }
        }
        return null;
    }

    public static List<UnitBean> getUnits(DataSearchPara para) {
        List<UnitBean> units = new ArrayList<>();
        if (para.getPlace() != null && para.getPlace().equals("环境温度")) {
            units.add(UnitService.getInitTempUnit());
        } else
            for (UnitBean unit : unitList) {
                if (para.getPlace() != null && para.getXw() != null) {
                    if (para.getPlace().equals(unit.getPlace()) && para.getXw().equals(unit.getXw())) {
                        units.add(unit);
                    }
                } else if (para.getXw() == null && para.getPlace() != null) {
                    if (para.getPlace().equals(unit.getPlace())) {
                        units.add(unit);
                    }
                } else if (para.getPlace() == null && para.getXw() != null) {
                    if (para.getXw().equals(unit.getXw())) {
                        units.add(unit);
                    }
                } else {
                    if (para.getUnitType() == unit.getType()) {
                        units.add(unit);
                    }
                }
            }
        return units;
    }

    /**
     * 获取采集单元信息
     *
     * @return
     * @throws SQLException
     */
    public static void init() throws SQLException {
        String sql = "select " +
                "type , number , gatewaytype, gatewaynumber, " +
                "period, inittemp, initvari, minvari, maxvari, maxden, minden, maxper, minper, warntemp, xw, u.point, p.place " +
                "from " + DataBaseAttr.UnitTable + " u , " + DataBaseAttr.PointTable + " p " +
                "where u.point = p.point  ";
        unitList = MyDbUtil.queryBeanListData(sql, UnitBean.class);
        String sql2 = "select * " +
                "from " + DataBaseAttr.UnitTable + " u " +
                "where u.inittemp = 1 ";
        unitList.addAll(MyDbUtil.queryBeanListData(sql2, UnitBean.class));
//        for (UnitBean unit :
//                unitList) {
//            System.out.println(unit);
//        }
//        System.out.println(unitList);
    }


    public static void updateInitvari(UnitBean bean) throws SQLException {
        String sql = "update "
                + DataBaseAttr.UnitTable
                + " set initvari = ? "
                + " where type = ? and number = ? ; ";
        MyDbUtil.update(sql, bean.getInitvari(), bean.getType(), bean.getNumber());
    }

    public static void updateWarning(UnitBean bean) throws SQLException {
        String sql = "update "
                + DataBaseAttr.UnitTable
                + " set maxden = ? ,"
                + "  minden = ? ,"
                + "  maxper = ? ,"
                + "  minper = ? ,"
                + "  maxvari = ? ,"
                + "  minvari = ? ,"
                + "  warntemp = ? "
                + " where type = ? and number = ? ; ";
        MyDbUtil.update(sql, bean.getMaxden(), bean.getMinden(), bean.getMaxper(), bean.getMinper(), bean.getMaxvari(), bean.getMinvari(), bean.getWarnTemp(), bean.getType(), bean.getNumber());
    }


    /**
     * 根据类型设置采集间隔
     *
     * @param type
     * @param jg
     * @throws SQLException
     */
    public static void setPeriods(byte type, byte jg) throws SQLException {
        String sql = "update " + DataBaseAttr.UnitTable + " set period = ? where type = ?";
        MyDbUtil.update(sql, jg, type);
    }

    /**
     * 根据类型和unitid设置采集单元的间隔
     *
     * @param type
     * @param unitid
     * @param jg
     * @throws SQLException
     */
    public static void setPeriod(byte type, Byte unitid, byte jg) throws SQLException {
        System.out.println(type + ":" + unitid + ":" + jg);
        if (unitid == null) {
            setPeriods(type, jg);
        } else {
            String sql = "update " + DataBaseAttr.UnitTable
                    + " set period = ? where type= ? and number = ?";
            for (UnitBean bean : unitList) {
                if (bean.getType() == type && bean.getNumber() == unitid) {
                    MyDbUtil.update(sql, jg, bean.getType(), bean.getNumber());
                    break;
                }
            }
        }
    }


}
