package service;

import javax.swing.*;

import com.CollectServer;
import com.Configure;
import com.LogServer;
import data.FormatTransfer;
import domain.NetBean;
import domain.UnitBean;
import domain.WdPeriod;
import protocol.Protocol;
import util.MyDecodeUitl;
import view.dataCollect.graph.GraphView;
import view.systemSetup.SystemSetup;

import java.sql.SQLException;
import java.util.*;
import java.util.Timer;

public class CollectService {

    static {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    setWarnTempPeriod();
                } catch (Exception e) {
                    LogServer.logError(CollectService.class, e);
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

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
    private static List<UnitBean> unitPeriodlist = new ArrayList<>();
    /*
     * 温升报警自动加快周期单元
     */
    private static Map<UnitBean, java.util.Timer> unitWarnTempUplist = new Hashtable<>();

    private static Vector<UnitBean> unitWarnTempUp = new Vector<>();

    public static void cachePeriod(UnitBean unitBean) {
        for (UnitBean unit : unitPeriodlist) {
            if (unit.equals(unitBean)) {
                unit.setPeriod(unitBean.getPeriod());
                return;
            }
        }
        unitPeriodlist.add(unitBean);
    }


    private static void cacheWarnTempPeriod(UnitBean unitBean, java.util.Timer timer) {
        // if (unitWarnTempUplist.containsKey(unitBean)) {
        java.util.Timer timer2 =
                unitWarnTempUplist.put(unitBean, timer);
        if (timer2 != null) {
            timer2.cancel();
            timer2.purge();
        }
        // return;
        // }
        // unitWarnTempUplist.put(unitBean, timer);
    }

    private static void cacheWarnTempPeriod(UnitBean unitBean) {
        if (unitWarnTempUp.contains(unitBean)) {
            return;
        }
        unitWarnTempUp.add(unitBean);
    }

    public static void cacelWarnTempPeriodTimer(byte unittype, byte unitnumber) {
        UnitBean unitBean = UnitService.getUnitBean(unittype, unitnumber);
        if (unitWarnTempUplist.containsKey(unitBean)) {
            java.util.Timer timer = unitWarnTempUplist.remove(unitBean);
            timer.cancel();
            timer.purge();
        }
    }

    public static void cacelWarnTempPeriodTimer2(byte unittype, byte unitnumber) {
        UnitBean unitBean = UnitService.getUnitBean(unittype, unitnumber);
        if (unitWarnTempUp.contains(unitBean)) {
            unitWarnTempUp.remove(unitBean);
        }
    }

    public static void savePeriod(byte type, byte number) {
        for (int i = unitPeriodlist.size() - 1; i >= 0; i--) {
            UnitBean unit = unitPeriodlist.get(i);
            if (unit.getType() == type && unit.getNumber() == number) {
                try {
                    UnitService.setPeriod(unit.getType(), unit.getNumber(), unit.getPeriod());
                    unitPeriodlist.remove(i);
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

    /*
     * 设置温升报警采集周期
     */
    public static void setWarnTempPeriod(final UnitBean unitBean) {
        java.util.Timer timer = new java.util.Timer();
        java.util.TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (unitBean.isTempflag()) {
                    CollectServer.getInstance().applyUnitOffline(unitBean.getType(), unitBean.getNumber(), (byte) 10);
                } else {
                    CollectServer.getInstance().applyUnitOffline(unitBean.getType(), unitBean.getNumber(),
                            unitBean.getPeriod());
                }
            }
        };
        cacheWarnTempPeriod(unitBean, timer);
        timer.schedule(task, 0, 1000);
    }

    public static void setWarnTempPeriod2(final UnitBean unitBean) {
        cacheWarnTempPeriod(unitBean);
    }

    private static void setWarnTempPeriod() {
        if (unitWarnTempUp.size() == 0) {
            return;
        }
        for (UnitBean unitBean : unitWarnTempUp) {
            if (unitBean.isTempflag()) {
                CollectServer.getInstance().applyUnitOffline(unitBean.getType(), unitBean.getNumber(), (byte) 10);
            } else {
                CollectServer.getInstance().applyUnitOffline(unitBean.getType(), unitBean.getNumber(),
                        unitBean.getPeriod());
            }
        }
    }




    ;


}
