package model;

import java.util.Vector;

public class DataModel_SSJ extends DataManageModel {

    private static DataModel_SSJ DM = null;

    public DataModel_SSJ() {
        initDefault();
    }

    @Override
    protected void initDefault() {
        super.initDefault();
        Vector<String> column = new Vector<String>();
//        column.add("ID");
//        column.add("网关类型");
//        column.add("网关编号");
//        column.add("单元类型");
//        column.add("单元编号");
//        column.add("单元编号");
        column.add("监测点");
        column.add("测点相位");
        column.add("偏移量(mm)");
        column.add("电压(Ｖ)");
        column.add("时间");
        this.setDataVector(row, column);
    }

    public static DataModel_SSJ getInstance() {
        if (DM == null) {
            synchronized (DataModel_SSJ.class) {
                if (DM == null)
                    DM = new DataModel_SSJ();
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
        // id,gt,gn,ut,un,vari,batlv
        if (column == 1 || column == 0) {
            return String.class;
        } else if (column == 4) {
            return java.util.Date.class;
        } else {
            return Float.class;
        }
    }

}

