package service;

import domain.HitchVolLevelBean;
import util.MyDbUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class HitchVolLevelService {

    private static List<HitchVolLevelBean> hitchVolLevels;

    static {
        try {
            query();
        } catch (SQLException e) {
            e.printStackTrace();
            defaultInit();
        }
    }

    public static float getValue(Byte level) {
        for (HitchVolLevelBean volLevel : hitchVolLevels) {
            if (level == volLevel.getLevel()) {
                return volLevel.getVol();
            }
        }
        return 0;
    }

    public static Vector<Byte> getLevels() {
        Vector<Byte> vector = new Vector<>();
        for (HitchVolLevelBean vollevel :
                hitchVolLevels) {
            vector.add(vollevel.getLevel());
        }
        return vector;
    }

    private static void defaultInit() {
        hitchVolLevels = new ArrayList<>();
        for (byte i = 1; i < 10; i++) {
            hitchVolLevels.add(new HitchVolLevelBean(i, (float) (i * 1000 - 0.1)));
        }
    }

    private static void query() throws SQLException {
        String sql = "SELECT vol,level FROM HITCHVOLLEVEL";
        hitchVolLevels = MyDbUtil.queryBeanListData(sql, HitchVolLevelBean.class);
    }

    public static List<HitchVolLevelBean> getHitchVolLevels() {
        return hitchVolLevels;
    }
}
