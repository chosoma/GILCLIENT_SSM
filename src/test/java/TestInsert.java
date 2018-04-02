import domain.DataBean;
import service.DataService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestInsert {
    public static void main(String[] args) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2018);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DAY_OF_MONTH, 6);
        c.set(Calendar.HOUR_OF_DAY, 14);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        List<DataBean> datas = new ArrayList<>();
        for (int j = 0; j < 30; j++) {
            c.add(Calendar.HOUR_OF_DAY, 1);
            Date date = c.getTime();
//        Date date = new Date();
            for (byte i = 1; i <= 33; i++) {
                DataBean dataBean = new DataBean();
                dataBean.setUnitType((byte) 1);
                dataBean.setUnitNumber(i);
                float f = (float) Math.random();
                dataBean.setPres(f);
                dataBean.setTemp((float) (Math.random() * 10 + 10));
                dataBean.setDen(f);
                dataBean.setBatlv((float) 3.8);
                dataBean.setDate(date);
                datas.add(dataBean);
            }
            for (byte i = 1; i <= 10; i++) {
                DataBean dataBean = new DataBean();
                dataBean.setUnitType((byte) 3);
                dataBean.setUnitNumber(i);
                dataBean.setTemp((float) (Math.random() * 10 + 10));
                dataBean.setBatlv((float) 3.8);
                dataBean.setDate(date);
                datas.add(dataBean);
            }
            for (byte i = 1; i <= 3; i++) {
                DataBean dataBean = new DataBean();
                dataBean.setUnitType((byte) 2);
                dataBean.setUnitNumber(i);
                dataBean.setVari((float) (Math.random() * 20 + 20));
                dataBean.setBatlv((float) 3.8);
                dataBean.setDate(date);
                datas.add(dataBean);
            }
            System.out.println(j);

            try {
                DataService.saveCollData(datas);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            datas.clear();
        }

    }
}
