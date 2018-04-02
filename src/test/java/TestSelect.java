import domain.CountBean;
import util.MyDbUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class TestSelect {
    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT count(*) as count\n");
        stringBuilder.append("FROM\n");
        stringBuilder.append("(SELECT ").append(" d.id ").append(" FROM ");
//        stringBuilder.append("(select u.type,u.number,u.point,u.xw\n");
//        if (para.getUnitType() == 2) {
//            stringBuilder.append(",u.initvari\n");
//        }
//        stringBuilder.append("FROM gateway g RIGHT JOIN unit u ON g.type = u.gatewaytype AND g.number = u.gatewaynumber) u\n");
        stringBuilder.append(" unit u\n");
        stringBuilder.append("LEFT JOIN data d ON u.type = d.unittype AND u.number = d.unitnumber\n");
        stringBuilder.append("LEFT JOIN point p ON p.point = u.point\n");
        stringBuilder.append("WHERE d.valid = 1\n").append("AND u.type = ? ");

        ArrayList<Object> p = new ArrayList<Object>();
        p.add(1);

        stringBuilder.append(" group by u.type, u.number, date ");
        stringBuilder.append(") c");
        try {
            long start = System.currentTimeMillis();
            CountBean countBean = null;
            countBean = MyDbUtil.queryBeanData(stringBuilder.toString(), CountBean.class, p.toArray());
            System.out.println(countBean.getCount());
            long end = System.currentTimeMillis();
            System.out.println(end - start + "ms");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
