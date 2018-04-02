package model;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import domain.DataBaseAttr;
import service.SensorService;

public abstract class DataManageModel extends DefaultTableModel {

    protected Vector<Vector<Object>> row;

    protected void initDefault() {
        row = new Vector<Vector<Object>>();
    }

    /**
     * 添加数据
     *
     * @param datas
     */
    public void addDatas(List<Vector<Object>> datas) {
        row.clear();
        row.addAll(datas);
        this.fireTableDataChanged();
    }

    ;

    /**
     * 删除数据
     *
     * @param selRows
     */
    public void removeRows(int[] selRows) {
        // 反向循环实现删除
        for (int i = selRows.length - 1; i >= 0; i--) {
            // 上句会出现删除最后一行出错
            removeRow(selRows[i]);
        }
    }

    /**
     * 清空数据
     */
    public void clearData() {
        row.clear();
        this.fireTableDataChanged();
    }

    /**
     * 设置JTable不可修改
     */
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
