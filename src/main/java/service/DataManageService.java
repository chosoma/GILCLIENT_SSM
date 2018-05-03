package service;

import java.sql.SQLException;
import java.util.*;

import domain.*;
import protocol.Protocol;
import util.MyDbUtil;

public class DataManageService {


    public static List<Vector<Object>> getTableData(DataSearchPara para, PageBean pageBean) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        stringBuilder.append("case when p.place is NULL then '环境温度' else p.place end AS place,");
        stringBuilder.append("u.xw AS xw,");
        switch (para.getUnitType()) {
            case Protocol.UnitTypeSF6:
                stringBuilder.append("i.den AS den,").append("i.pres AS pres,");
                break;
            case Protocol.UnitTypeSSJ:
                stringBuilder.append("i.vari-us.initvari AS vari,");
                break;
            case Protocol.UnitTypeWD:
                stringBuilder.append("i.temp AS temp,");
                break;
            case Protocol.UnitTypeHV:
                stringBuilder.append("i.hitchvol AS hitchvol,");
                break;
        }
        stringBuilder.append(" i.BatLv AS batlv,").append(" i.date");
        stringBuilder.append(" FROM ").append(DataBaseAttr.UnitTable).append(" u ");
        stringBuilder.append("LEFT JOIN ").append(DataBaseAttr.DataTable).append(" i ON u.type = i.unittype AND u.number = i.unitnumber ");
        stringBuilder.append("LEFT JOIN ").append(DataBaseAttr.PointTable).append(" p ON p.point = u.point ");
        switch (para.getUnitType()) {
            case Protocol.UnitTypeSF6:
                stringBuilder.append("left join ").append(DataBaseAttr.UnitSF6Table).append(" us on u.number = us.number and u.type = ").append(Protocol.UnitTypeSF6);
                break;
            case Protocol.UnitTypeSSJ:
                stringBuilder.append("left join ").append(DataBaseAttr.UnitVariTable).append(" us on u.number = us.number and u.type = ").append(Protocol.UnitTypeSSJ);
                break;
            case Protocol.UnitTypeWD:
                stringBuilder.append("left join ").append(DataBaseAttr.UnitTempTable).append(" us on u.number = us.number and u.type = ").append(Protocol.UnitTypeWD);
                break;
            case Protocol.UnitTypeHV:
                stringBuilder.append("left join ").append(DataBaseAttr.UnitHitchTable).append(" us on u.number = us.number and u.type = ").append(Protocol.UnitTypeHV);
                break;
        }
        ArrayList<Object> p = new ArrayList<Object>();
        stringBuilder.append(" WHERE u.type = ? ");
        p.add(para.getUnitType());
        List<UnitBean> units = para.getUnits();
        if (units != null && units.size() > 0) {
            stringBuilder.append(" AND UNITNUMBER IN ( ");
            for (int i = 0; i < units.size(); i++) {
                UnitBean unit = units.get(i);
                stringBuilder.append("?");
                p.add(unit.getNumber());
                if (i != para.getUnits().size() - 1) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(" ) ");
        }

        stringBuilder.append(" AND date BETWEEN ? AND ? ");
        addpara(p, para);

        stringBuilder.append(" order by date desc ");
        stringBuilder.append(" limit ?,? ");
        p.add(pageBean.getStart());
        p.add(pageBean.getPageCount());
System.out.println(stringBuilder);
        return MyDbUtil.queryTableData(stringBuilder.toString(), p.toArray());
    }

    public static CountBean getTableDataCount(DataSearchPara para) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        stringBuilder.append(" count(*) as count ");
        stringBuilder.append(" FROM ");
        stringBuilder.append(DataBaseAttr.DataTable);
        stringBuilder.append(" d ");
        stringBuilder.append(" WHERE unittype = ? ");

        ArrayList<Object> p = new ArrayList<Object>();
        p.add(para.getUnitType());
        List<UnitBean> units = para.getUnits();
        if (units != null && units.size() > 0) {
            stringBuilder.append(" AND UNITNUMBER IN ( ");
            for (int i = 0; i < units.size(); i++) {
                UnitBean unit = units.get(i);
                stringBuilder.append("?");
                p.add(unit.getNumber());
                if (i != para.getUnits().size() - 1) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(" ) ");
        }
        addpara(p, para);
        stringBuilder.append(" AND date BETWEEN ? AND ? ");
        return MyDbUtil.queryBeanData(stringBuilder.toString(), CountBean.class, p.toArray());
    }

    static void addpara(ArrayList<Object> p, DataSearchPara para) {
        Calendar calendar = Calendar.getInstance();
        if (para.getT1() != null) {
            calendar.setTime(para.getT1());
            p.add(para.getT1());
            if (para.getT2() != null) {
                p.add(para.getT2());
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, 30);
                Date dateend = calendar.getTime();
                p.add(dateend);
            }
        } else {
            if (para.getT2() != null) {
                calendar.setTime(para.getT2());
            }
            Date dateend = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, -30);
            Date datestart = calendar.getTime();
            p.add(datestart);
            p.add(dateend);
        }
    }

    public static Vector<String> getUnitPlaces(String name) {
        return SensorService.getPlaces(name);
    }

    public static Vector<String> getUnitPlaces() {
        return SensorService.getPlaces();
    }

    public static void deleteData(Map<UnitBean, List<Date>> dataBeanListMap) throws SQLException {
        Set<Map.Entry<UnitBean, List<Date>>> entrySet = dataBeanListMap.entrySet();
        for (Map.Entry<UnitBean, List<Date>> entry : entrySet) {
            UnitBean unitBean = entry.getKey();
            List<Date> dates = entry.getValue();
            List<Object> paras = new ArrayList<>();
            paras.add(unitBean.getType());
            paras.add(unitBean.getNumber());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" delete from ");
            stringBuilder.append(DataBaseAttr.DataTable);
            stringBuilder.append(" where unittype = ? and unitnumber = ? and date in (");
            for (int i = 0; i < dates.size(); i++) {
                paras.add(dates.get(i));
                stringBuilder.append("?");
                if (i != dates.size() - 1) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(")");
            MyDbUtil.update(stringBuilder.toString(), paras.toArray());
        }
    }

}
