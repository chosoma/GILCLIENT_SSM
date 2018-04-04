package service;

import java.sql.SQLException;
import java.util.*;

import domain.*;
import util.MyDbUtil;

public class DataService {

    private static String sqlInsert;
    private static String sqlValidInsert;

    private DataService() {
    }

    static {
        String datatable = DataBaseAttr.DataTable;
        String validtable = DataBaseAttr.DataValidTable;
        sqlInsert = " insert into " + datatable
                + " (unittype,unitnumber,den,pres,temp,vari,hitchvol,batlv,date)"
                + " values ( ?,?,?,?,?,?,?,?,?)";
        sqlValidInsert = " insert into " + validtable
                + " (unittype,unitnumber,den,pres,temp,vari,hitchvol,batlv,date)"
                + " values ( ?,?,?,?,?,?,?,?,?)";

    }


    public static List<DataBean> getBetween(UnitBean unitBean, DataSearchPara para) throws SQLException {
        StringBuilder sbgetBetween = new StringBuilder();
        sbgetBetween.delete(0, sbgetBetween.length());
        sbgetBetween.append(" select date, pres, temp, den, vari, batlv \n");
        sbgetBetween.append(" from data\n");
        ArrayList<Object> p = new ArrayList<>();
        p.add(unitBean.getType());
        p.add(unitBean.getNumber());
        sbgetBetween.append(" where unittype = ? and unitnumber = ? \n");
        DataManageService.addpara(p, para);
        sbgetBetween.append(" AND date BETWEEN ? AND ? ");
        List<DataBean> list = MyDbUtil.queryBeanListData(sbgetBetween.toString(), DataBean.class, p.toArray());
        sbgetBetween.delete(0, sbgetBetween.length());
        sbgetBetween = null;
        System.gc();
        return list;
    }


    public static void saveCollData(List<DataBean> datas) throws SQLException {
        Vector<Vector<Object>> datasV = new Vector<Vector<Object>>();
        for (DataBean data : datas) {
            datasV.add(data.getSqlData());
        }
        MyDbUtil.batchData(sqlInsert, datasV);
    }


    public static void saveCollData(DataBean data) throws SQLException {
        MyDbUtil.update(sqlInsert, data.getSqlData().toArray());
    }

    public static void saveValidCollData(DataBean data) throws SQLException {
        MyDbUtil.update(sqlValidInsert, data.getSqlData().toArray());
    }


    public static DataBean getLatestAmtemp() throws SQLException {
        String sql = "select d.temp " +
                "from data d , unit u " +
                "where d.unittype = u.type " +
                "and d.unitnumber = u.number " +
                "and u.number = (select number from unit_temp where isinit = 1) order by d.date desc limit 1 ";
        return MyDbUtil.queryBeanData(sql, DataBean.class);
    }

    /**
     * 各点最后一条数据
     *
     * @return List
     * @throws SQLException
     */
    public static List<DataBean> getLatestDatas() throws SQLException {
        String sql = "SELECT\n" +
                "d.gatewaynumber,\n" +
                "d.unittype,\n" +
                "d.unitnumber,\n" +
                "d.period,\n" +
                "d.channel,\n" +
                "d.pres,\n" +
                "d.temp,\n" +
                "d.den,\n" +
                "   d. vari ,\n" +
                "   d . batlv ,\n" +
                "   d . date ,\n" +
                "   d . xw ,\n" +
                "   d . point ,\n" +
                "   d . place \n" +
                "FROM\n" +
                "  (\n" +
                "    (SELECT\n" +
                "       g . number  AS 'gatewaynumber',\n" +
                "       u . type  AS 'unittype',\n" +
                "       u . number  AS 'unitnumber',\n" +
                "       u . period ,\n" +
                "       g . channel ,\n" +
                "       d . Pres  AS 'pres',\n" +
                "       d . Temp  AS 'temp',\n" +
                "       d . Den  AS 'den',\n" +
                "       d . Vari  AS 'vari',\n" +
                "       d . BatLv  AS 'batlv',\n" +
                "       d . date ,\n" +
                "       u . xw ,\n" +
                "       p . point ,\n" +
                "       p . place \n" +
                "    FROM\n" +
                "      ((( data  d\n" +
                "        JOIN  " + DataBaseAttr.UnitTable + "  u)\n" +
                "        JOIN  " + DataBaseAttr.GATEWAYTable + "  g)\n" +
                "        JOIN  " + DataBaseAttr.PointTable + "  p)\n" +
                "    WHERE\n" +
                "      (( d . unittype  =  u . type )\n" +
                "        AND (d.unitnumber=u.number )\n" +
                "        AND (u. point=p.point )\n" +
                "        AND ( u . gatewaytype=g.type )\n" +
                "        AND ( u . gatewaynumber=g.number )" +
                "         )) d\n" +
                "      JOIN (SELECT\n" +
                "        unittype ,unitnumber, MAX(date) AS 'date'\n" +
                "      FROM\n" +
                DataBaseAttr.DataTable +
                " GROUP BY" +
                " unittype,unitnumber ) da" +
                "  )\n" +
                "WHERE " +
                "(( d.unittype  =  da.unittype ) AND (d.unitnumber=da.unitnumber) AND (d.date=da.date))";

        return MyDbUtil.queryBeanListData(sql, DataBean.class);
    }
}
