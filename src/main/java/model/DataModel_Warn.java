package model;

import java.util.Vector;

public class DataModel_Warn extends DataManageModel {
    private static DataModel_Warn DM = null;

    public DataModel_Warn() {
        initDefault();
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
//        column.add("ID");
        column.add("监测点");
        column.add("测点相位");
        column.add("警报信息");
        column.add("时间");
        column.add("是否处理");
        this.setDataVector(row, column);
    }

    public static DataModel_Warn getInstance() {
        if (DM == null) {
            synchronized (DataModel_SSJ.class) {
                if (DM == null)
                    DM = new DataModel_Warn();
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
        if (column == 3) {
            return java.util.Date.class;
        } else {
            return String.class;
        }
    }
}
