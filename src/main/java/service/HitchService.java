package service;

import domain.*;
import util.MyDbUtil;

import java.sql.SQLException;
import java.util.*;

public class HitchService {


    private static String sqlInsert;
    private static String hitchtable;

    static {
        hitchtable = DataBaseAttr.HitchTable;
        sqlInsert = " insert into " + hitchtable
                + " (unittype,unitnumber,vol,batlv,date)"
                + " values ( ?,?,?,?,? )";
    }

    /**
     * C
     */
    public static void save(List<HitchBean> hitchBeans) throws SQLException {
        Vector<Vector<Object>> vectors = new Vector<>();
        for (HitchBean hitchBean : hitchBeans) {
            vectors.add(hitchBean.getSqlData());
        }
        MyDbUtil.batchData(sqlInsert, vectors);
    }

    public static void save(HitchBean hitchBeans) throws SQLException {
        MyDbUtil.update(sqlInsert, hitchBeans.getSqlData().toArray());
    }


    /**
     * R
     */
    public static CountBean getTableDataCount(DataSearchPara para) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" SELECT COUNT(*) AS COUNT \n");
        ArrayList<Object> p = new ArrayList<>();
        stringBuilder.append(" FROM\n");
        stringBuilder.append(hitchtable);
        stringBuilder.append(" WHERE ");
        if (para.getHitchunits().size() > 0) {
            stringBuilder.append(" unitnumber in (");
            for (int i = 0; i < para.getHitchunits().size(); i++) {
                HitchUnitBean unit = para.getHitchunits().get(i);
                p.add(unit.getNumber());
                stringBuilder.append("?");
                if (i != para.getHitchunits().size() - 1) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(") and");
        }
        stringBuilder.append(" date BETWEEN ? AND ? ");
        DataManageService.addpara(p, para);
        return MyDbUtil.queryBeanData(stringBuilder.toString(), CountBean.class, p.toArray());
    }

    public static List<Vector<Object>> getTableData(DataSearchPara para, PageBean pageBean) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Object> p = new ArrayList<>();
        stringBuilder.append(" SELECT p.place,h.xw,d.vol,d.batlv,d.date ");
        stringBuilder.append(" FROM\n");
        stringBuilder.append(DataBaseAttr.HitchUnitTable);
        stringBuilder.append(" h left join ");
        stringBuilder.append(hitchtable);
        stringBuilder.append(" d on h.number = d.unitnumber ");
        stringBuilder.append(" left join ");
        stringBuilder.append(DataBaseAttr.PointTable);
        stringBuilder.append(" p on h.point = p.point ");
        stringBuilder.append(" WHERE ");
        if (para.getHitchunits().size() > 0) {
            stringBuilder.append(" unitnumber in (");
            for (int i = 0; i < para.getHitchunits().size(); i++) {
                HitchUnitBean unit = para.getHitchunits().get(i);
                p.add(unit.getNumber());
                stringBuilder.append("?");
                if (i != para.getHitchunits().size() - 1) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(") and");
        }
//        if (para.getUnitNumber() != null) {
//            stringBuilder.append(" unitnumber = ? and ");
//            p.add(para.getUnitNumber());
//        }
        stringBuilder.append(" date BETWEEN ? AND ? ");
        DataManageService.addpara(p, para);
        stringBuilder.append(" order by date desc ");
        stringBuilder.append(" limit ?,? ");
        p.add(pageBean.getStart());
        p.add(pageBean.getPageCount());

        return MyDbUtil.queryTableData(stringBuilder.toString(), p.toArray());
    }

    /**
     * D
     */
    public static void deleteData(java.util.Map<HitchUnitBean, List<Date>> dataBeanListMap) throws SQLException {
        Set<Map.Entry<HitchUnitBean, List<Date>>> entrySet = dataBeanListMap.entrySet();
        for (Map.Entry<HitchUnitBean, List<Date>> entry : entrySet) {
            HitchUnitBean unitBean = entry.getKey();
            List<Date> dates = entry.getValue();
            List<Object> paras = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" delete from ");
            stringBuilder.append(hitchtable);
            stringBuilder.append(" where unitnumber = ? and date in (");
            paras.add(unitBean.getNumber());
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
