package service;

import domain.*;
import util.MyDbUtil;

import java.sql.SQLException;
import java.util.*;

public class WarningService {

    private static String sqlInsert;
    private static String sqlHandle;

    static {
        sqlInsert = "INSERT INTO " + DataBaseAttr.WarnTable
                + "( info , handle , date , point , xw )"
                + "VALUES"
                + "(?,?,?,?,?)";
        sqlHandle = "UPDATE " + DataBaseAttr.WarnTable
                + " SET HANDLE = 1 " +
                " WHERE DATE = ? AND POINT = ? AND XW = ?";
    }

    public static List<Vector<Object>> getTableWarn(DataSearchPara para, PageBean pageBean) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder("SELECT\n");
        stringBuilder
                .append("p.place AS place,\n")
                .append("w.xw AS xw,\n")
                .append("w.info,\n")
                .append("w.date,\n")
                .append("CASE w.handle WHEN 1 THEN '已处理' ELSE '未处理' END AS handle \n")
                .append("FROM\n")
                .append("point p\n")
                .append("JOIN warn w\n")
                .append("WHERE\n")
                .append("p.point = w.point\n");
        ArrayList<Object> p = new ArrayList<Object>();
        if (para.getPlace() != null) {
            p.add(para.getPlace());
            stringBuilder.append(" AND p.place = ? ");
        }
        if (para.getXw() != null) {
            p.add(para.getXw());
            stringBuilder.append(" AND w.xw = ? ");
        }
        DataManageService.addpara(p, para);
        stringBuilder.append(" AND date BETWEEN ? AND ? ");
        stringBuilder.append(" order by date desc ");
        stringBuilder.append(" limit ?,? ");
        p.add(pageBean.getStart());
        p.add(pageBean.getPageCount());
        return MyDbUtil.queryTableData(stringBuilder.toString(), p.toArray());
    }

    public static CountBean getTableWarnCount(DataSearchPara para) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("SELECT count(*) as count from\n")
                .append("point p\n")
                .append("JOIN warn w\n")
                .append("WHERE\n")
                .append("p.point = w.point\n");
        ArrayList<Object> p = new ArrayList<Object>();
        if (para.getPlace() != null) {
            p.add(para.getPlace());
            stringBuilder.append(" AND p.place = ? ");
        }
        if (para.getXw() != null) {
            p.add(para.getXw());
            stringBuilder.append(" AND w.xw = ? ");
        }
        DataManageService.addpara(p, para);
        stringBuilder.append(" AND date BETWEEN ? AND ? ");
//        stringBuilder.append("group by w.point, w.xw, w.date\n");
//        stringBuilder.append(") c");
        return MyDbUtil.queryBeanData(stringBuilder.toString(), CountBean.class, p.toArray());
    }

    public static void saveWarn(WarnBean warnBean) throws SQLException {
        MyDbUtil.update(sqlInsert, warnBean.getSqlData().toArray());
    }

    public static void saveCollWarn(List<WarnBean> warnBeans) throws SQLException {
        Vector<Vector<Object>> datasV = new Vector<Vector<Object>>();
        for (WarnBean data : warnBeans) {
            datasV.add(data.getSqlData());
        }
        MyDbUtil.batchData(sqlInsert, datasV);
    }

    public static void deleteWarn(Map<WarnBean, List<Date>> dataBeanListMap) throws SQLException {
        Set<Map.Entry<WarnBean, List<Date>>> entrySet = dataBeanListMap.entrySet();
        for (Map.Entry<WarnBean, List<Date>> entry : entrySet) {
            WarnBean warnBean = entry.getKey();
            List<Date> dates = entry.getValue();
            List<Object> paras = new ArrayList<>();
            paras.add(warnBean.getPointBean().getPoint());
            paras.add(warnBean.getXw());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" delete from ");
            stringBuilder.append(DataBaseAttr.WarnTable);
            stringBuilder.append(" where point = ? and xw = ? and date in (");
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

    public static void updateHandle(WarnBean warnBean) throws SQLException {
        MyDbUtil.update(sqlHandle, warnBean.getSqlUpdate().toArray());
    }

}
