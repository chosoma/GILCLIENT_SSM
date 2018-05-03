package model;

import java.util.Vector;

public class DataModel_Hitch extends DataManageModel {

    private static DataModel_Hitch DM = null;

    private DataModel_Hitch() {
        initDefault();
    }
    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
        column.add("监测点");
        column.add("相位");
        column.add("警报值");
        column.add("电池电压(V)");
        column.add("时间");
        this.setDataVector(row, column);
    }

    public static DataModel_Hitch getInstance() {
        if (DM == null) {
            synchronized (DataModel_Hitch.class) {
                if (DM == null)
                    DM = new DataModel_Hitch();
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
