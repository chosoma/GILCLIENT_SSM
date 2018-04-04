package service;


import javax.swing.*;

import com.CollectServer;
import domain.UnitBean;
import view.systemSetup.SystemSetup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CollectService {


    /**
     * 关闭服务
     */
    public static void CloseColl1() {
        CollectServer.getInstance().closeConnection();
    }

    /**
     * 启动服务
     */
    public static void OpenColl1() throws Exception {
        CollectServer.getInstance().openConnection();
    }

    /*
     * 设置采集周期存储的单元
     */
    private static List<UnitBean> unitlist = new ArrayList<>();


    public static void cachePeriod(UnitBean unitBean) {
        for (UnitBean unit : unitlist) {
            if (unit.equals(unitBean)) {
                unit.setPeriod(unitBean.getPeriod());
                return;
            }
        }
        unitlist.add(unitBean);
    }

    public static void savePeriod(byte type, byte number) {
        for (int i = unitlist.size() - 1; i >= 0; i--) {
            UnitBean unit = unitlist.get(i);
            if (unit.getType() == type && unit.getNumber() == number) {
                try {
                    UnitService.setPeriod(unit.getType(), unit.getNumber(), unit.getPeriod());
                    unitlist.remove(i);
                    SystemSetup.getInstance().refreshPeriod();
                    JOptionPane.showMessageDialog(null, "设置成功");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    /**
     * 设置采集间隔
     */
    public static void setPeriod(UnitBean unitBean) {
        CollectServer.getInstance().applyUnitOffline(unitBean.getType(), unitBean.getNumber(), unitBean.getPeriod());
        cachePeriod(unitBean);
    }


    private static void saveHitch(byte type, byte number) {

    }


}
