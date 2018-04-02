package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import util.MyDbUtil;
import domain.DataBaseAttr;
import domain.NetBean;

public class NetService {
    private static List<NetBean> netList;

    public static List<NetBean> getNetList() {
        return netList;
    }

    /**
     * 获取采集单元信息
     *
     * @return
     * @throws SQLException
     */
    public static void init() throws SQLException {
        String sql = " select * from " + DataBaseAttr.NetTable + " group by type,number order by type ,number; ";
        netList = MyDbUtil.queryBeanListData(sql, NetBean.class);
        Collections.sort(netList);
    }


    /**
     * 根据测值类型和netid获取采集单元
     *
     * @param type
     * @param netid
     * @return
     */
    public static NetBean getNetBean(byte type, byte netid) {
        for (NetBean bean : netList) {
            if (bean.getType() == type && bean.getNumber() == netid) {
                return bean;
            }
        }
        return null;
    }


}
