package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import domain.DataSearchPara;
import protocol.Protocol;
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

    public static Vector<String> getHitchPlaces() {
        Vector<String> vector = new Vector<>();
        for (UnitBean unit : unitList) {
            if (unit.getType() != Protocol.UnitTypeHV) {
                continue;
            }
            if (!vector.contains(unit.getPlace())) {
                vector.add(unit.getPlace());
            }
        }
        return vector;
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

    public static void refresh(UnitBean hitchUnitBean) {
        for (UnitBean unit : unitList) {
            if (hitchUnitBean.getPoint() == unit.getPoint()) {
                unit.setPlace(hitchUnitBean.getPlace());
            }
        }
    }

    public static UnitBean getInitTempUnit() {
        for (UnitBean unit : unitList) {
            if (unit.isIsinit()) {
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
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("select u.type,u.number,u.gatewaytype,u.gatewaynumber,u.period," +
                " ut.isinit,ut.warntemp," +
                " ur.initvari,ur.minvari,ur.maxvari," +
                " us.maxden,us.minden,us.maxper,us.minper,us.warntemp as sf6temp," +
                " uh.vollevel,uh.volwarn,uh.x,uh.y," +
                " u.xw,u.point,p.place");
        sqlBuilder.append(" from " + DataBaseAttr.UnitTable + " u" +
                " left join " + DataBaseAttr.PointTable + " p on u.point = p.point" +
                " left join " + DataBaseAttr.GATEWAYTable + " g on u.gatewaytype = g.type and u.gatewaynumber = g.number" +
                " left join " + DataBaseAttr.UnitSF6Table + " us on u.number = us.number and u.type = " + Protocol.UnitTypeSF6 +
                " left join " + DataBaseAttr.UnitVariTable + " ur on u.number = ur.number and u.type = " + Protocol.UnitTypeSSJ +
                " left join " + DataBaseAttr.UnitTempTable + " ut on u.number = ut.number and u.type = " + Protocol.UnitTypeWD +
                " left join " + DataBaseAttr.UnitHitchTable + " uh on u.number = uh.number and u.type = " + Protocol.UnitTypeHV);


        unitList = MyDbUtil.queryBeanListData(sqlBuilder.toString(), UnitBean.class);

//        String sql = "select " +
//                "type , number , gatewaytype, gatewaynumber, " +
//                "period, inittemp, initvari, minvari, maxvari, maxden, minden, maxper, minper, warntemp, xw, u.point, p.place " +
//                "from " + DataBaseAttr.UnitTable + " u , " + DataBaseAttr.PointTable + " p " +
//                "where u.point = p.point  ";
//        unitList = MyDbUtil.queryBeanListData(sql, UnitBean.class);
//        String sql2 = "select * " +
//                "from " + DataBaseAttr.UnitTable + " u " +
//                "where u.inittemp = 1 ";
//        unitList.addAll(MyDbUtil.queryBeanListData(sql2, UnitBean.class));
//        for (UnitBean unit :
//                unitList) {
//            System.out.println(unit);
//        }
//        System.out.println(unitList);
    }


    public static void updateInitvari(UnitBean bean) throws SQLException {
        String sql = "update "
                + DataBaseAttr.UnitVariTable
                + " set initvari = ? "
                + " where number = ? ; ";
        MyDbUtil.update(sql, bean.getInitvari(), bean.getNumber());
    }

    public static void updateWarning(UnitBean bean) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("update ");
        switch (bean.getType()) {
            case Protocol.UnitTypeSF6:
                sqlBuilder.append(DataBaseAttr.UnitSF6Table);
                sqlBuilder.append(" set maxden = ? , minden = ? , maxper = ? , minper = ? , warntemp = ? ");
                break;
            case Protocol.UnitTypeSSJ:
                sqlBuilder.append(DataBaseAttr.UnitVariTable);
                sqlBuilder.append(" set maxvari = ? , minvari = ? ");
                break;
            case Protocol.UnitTypeWD:
                sqlBuilder.append(DataBaseAttr.UnitTempTable);
                sqlBuilder.append(" set warntemp = ? ");
                break;
            case Protocol.UnitTypeHV:
                sqlBuilder.append(DataBaseAttr.UnitHitchTable);
                sqlBuilder.append(" set vollevel = ? , volwarn = ? ");
                break;
        }
        sqlBuilder.append(" where number = ? ");
        MyDbUtil.update(sqlBuilder.toString(), bean.getProper().toArray());
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
