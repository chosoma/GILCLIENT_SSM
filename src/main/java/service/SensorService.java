package service;

import java.sql.SQLException;
import java.util.*;

import domain.*;
import model.*;

import view.dataCollect.datacollect.AbcUnitView;


public class SensorService {


    public static final String Sensor_WD = "温度";
    public static final String Sensor_SF6 = "SF6";
    public static final String Sensor_SSJ = "伸缩节";
    public static final String Sensor_WARN = "警报";
    public static final String Sensor_HITCH = "故障";
    public static final String[] Sensor_Type = new String[]{Sensor_SF6, Sensor_WD, Sensor_SSJ};
    private static List<UnitBean> units;
    private static List<UnitPacket> unitPackets;

    static {
        unitPackets = new ArrayList<>();
        try {
            refreshSensor();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化单元资料
     *
     * @throws SQLException
     */
    public static void refreshSensor() throws SQLException {
        units = UnitService.getUnitList();
        List<PointBean> points = PointService.getPointUnitList();
        Collections.sort(units);
        refreshUnitPackts();//获取gateway
        for (PointBean point : points) {
            UnitPacket unitPacket = getUnitPacket(point.getGatewayType(), point.getGatewayNumber());
            if (unitPacket == null) {
                unitPacket = new UnitPacket(point.getGatewayType(), point.getGatewayNumber());
                unitPackets.add(unitPacket);
            }
            unitPacket.addPoint(point);

            for (UnitBean unitBean : units) {
                if (unitBean.getPoint() == point.getPoint()) {
                    unitPacket.addUnit(unitBean);
                }
            }
        }
    }


    public static UnitPacket getUnitPacket(byte netType, byte netID) {
        for (UnitPacket packet : unitPackets) {
            if (packet.match(netType, netID)) {
                return packet;
            }
        }
        return null;
    }

    public static List<AbcUnitView> getAbcUnitViews() {
        List<AbcUnitView> views = new ArrayList<AbcUnitView>();
        for (UnitPacket packet : unitPackets) {
            views.addAll(packet.getAbcUnitViews());
        }
        return views;
    }


    public static void setPlace(PointBean pointBean) {
        for (UnitBean unit : units) {
            if (unit.getPoint() == pointBean.getPoint()) {
                unit.setPlace(pointBean.getPlace());
            }
        }
    }

    public static Vector<String> getPlaces() {
        Vector<String> places = new Vector<>();
        for (UnitBean unitBean : units) {
            String place = unitBean.getPlace();
            if (place == null) {
                continue;
            }
            if (!places.contains(place)) {
                places.add(place);
            }
        }
        return places;
    }

    public static Vector<String> getPlaces(String name) {
        byte type = 0;
        switch (name) {
            case "SF6":
                type = 1;
                break;
            case "伸缩节":
                type = 2;
                break;
            case "温度":
                type = 3;
                break;
        }
        Vector<String> places = new Vector<>();
        for (UnitBean unitBean : units) {
            if (type == unitBean.getType()) {
                String place = unitBean.getPlace();
                if (place == null) {
                    continue;
                }
                if (!places.contains(place)) {
                    places.add(place);
                }
            }
        }
        return places;
    }

    /**
     * 根据物理量获取表格数据模型
     *
     * @param type
     * @return
     */
    public static DataManageModel getDataModel(String type) {
        switch (type) {
            case Sensor_WD:
                return DataModel_WD.getInstance();
            case Sensor_SF6:
                return DataModel_SF6.getInstance();
            case Sensor_SSJ:
                return DataModel_SSJ.getInstance();
            case Sensor_WARN:
                return DataModel_Warn.getInstance();
            case Sensor_HITCH:
                return DataModel_Warn.getInstance();
            default:
                return null;
        }
    }

    private static void refreshUnitPackts() {
        int size = unitPackets.size();
        for (int i = size - 1; i >= 0; i--) {
            unitPackets.remove(i).clear();
        }
        for (NetBean netbean : NetService.getNetList()) {
            unitPackets.add(new UnitPacket(netbean));
        }
    }

}
