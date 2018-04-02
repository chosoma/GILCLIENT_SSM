package model;

import java.util.Vector;

/**
 * SF6表模型
 */
public class DataModel_SF6 extends DataManageModel {

    private static DataModel_SF6 DM = null;

    private DataModel_SF6() {
        initDefault();
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("监测点");
        column.add("测点相位");
        column.add("密度(MPa)");
        column.add("压力(MPa)");
        column.add("电压(Ｖ)");
        column.add("时间");
        this.setDataVector(row, column);
    }

    public static DataModel_SF6 getInstance() {
        if (DM == null) {
            synchronized (DataModel_SF6.class) {
                if (DM == null)
                    DM = new DataModel_SF6();
            }
        }
        return DM;
    }

    /**
     * 重写getColumnClass方法，实现排序对列类型的区分
     * 这里根据数据库表中各个列类型，自定义返回每列的类型(用于解决数据库中NULL处理抛出异常)
     */
    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 1 || column == 0) {
            return String.class;
        } else if (column == 5) {
            return java.util.Date.class;
        } else {
            return Float.class;
        }
    }

}
