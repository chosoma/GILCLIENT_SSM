package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class MyExcelUtil {
    static JFileChooser fileChooser;

    static {
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("导出到Excel");
        // 是否允许选择多个文件
        fileChooser.setMultiSelectionEnabled(false);
        // 从可用文件过滤器的列表中移除 AcceptAll 文件过滤器
        fileChooser.setAcceptAllFileFilterUsed(false);
        // 添加Excel过滤器
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
                "Excel表格(*.xls)", "xls"));
    }

    public static void Export2Excel(JTable table, String sheetName)
            throws IOException {
        String filename = null;
        fileChooser.setDialogTitle("保存");
        int returnVal = fileChooser.showSaveDialog(table.getParent());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filename = fileChooser.getSelectedFile().getPath();
            if (!filename.endsWith(".xls")) {
                filename = filename + ".xls";
            }
        } else {
            return;
        }

        WritableWorkbook wwb = null;
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            wwb = Workbook.createWorkbook(file);
            WritableSheet ws = wwb.createSheet(sheetName, 0);

            for (int i = 0; i < table.getColumnCount(); i++) {
                try {
                    Label label = new Label(i, 0, table.getColumnName(i));
                    ws.addCell(label);
                } catch (RowsExceededException e) {
                    e.printStackTrace();
                    continue;
                } catch (WriteException e) {
                    e.printStackTrace();
                    continue;
                }
            }

            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    try {
                        if (table.getValueAt(i, j) == null) {
                            ws.addCell(new Label(j, i + 1, null));
                        } else if (table.getValueAt(i, j).getClass().equals(Byte.class)) {
                            ws.addCell(new Number(j, i + 1, (Byte) table.getValueAt(i, j)));
                        } else if (table.getValueAt(i, j).getClass().equals(Integer.class)) {
                            ws.addCell(new Number(j, i + 1, (Integer) table.getValueAt(i, j)));
                        } else if (table.getValueAt(i, j).getClass().equals(Float.class)) {
                            ws.addCell(new Number(j, i + 1, (Float) table.getValueAt(i, j)));
                        } else if (table.getValueAt(i, j).getClass().equals(Long.class)) {
                            ws.addCell(new Number(j, i + 1, (Long) table.getValueAt(i, j)));
                        } else if (table.getValueAt(i, j).getClass().equals(Double.class)) {
                            ws.addCell(new Number(j, i + 1, (Double) table.getValueAt(i, j)));
                        } else if (table.getValueAt(i, j).getClass().equals(BigDecimal.class)) {
                            // 要将BigDecimal转换成Double类型
                            ws.addCell(new Number(j, i + 1, ((BigDecimal) table.getValueAt(i, j)).doubleValue()));
                        } else if (table.getValueAt(i, j).getClass().equals(String.class)) {
                            ws.addCell(new Label(j, i + 1, (String) table.getValueAt(i, j)));
                        } else if (table.getValueAt(i, j).getClass().equals(Timestamp.class)) {
                            ws.addCell(new Label(j, i + 1, String.valueOf(table.getValueAt(i, j)).substring(0, 19)));
                        }
                    } catch (RowsExceededException e) {
                        e.printStackTrace();
                        continue;
                    } catch (WriteException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
            // 将内容写到文件中
            wwb.write();
            JOptionPane.showMessageDialog(null, "导出成功", "成功",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e1) {
            throw new IOException("另一个程序正在使用此Excel文件，进程无法访问");
        } finally {
            // 将wwb关闭
            if (wwb != null) {
                try {
                    wwb.close();
                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从Excel中导入数据
     *
     * @return
     * @throws IOException
     */
    public static ArrayList<String[]> Import4Excel() throws IOException {
        String filename = null;
        fileChooser.setDialogTitle("加载");
        int returnVal = fileChooser.showOpenDialog(null);// TODO
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filename = fileChooser.getSelectedFile().getPath();
        } else {
            return null;
        }
        ArrayList<String[]> paras = null;
        InputStream stream = null;
        Workbook rwb = null;
        try {
            stream = new FileInputStream(filename);
            // 创建一个workbook类读取excel文件
            rwb = Workbook.getWorkbook(stream);
            // 得到第i个工作薄
            Sheet st = rwb.getSheet(0);// 这里有两种方法获取sheet表,1为名字，下标，从0开始

            // 得到st的行数
            int rowNum = st.getRows();
            // 得到st的列数
            int colNum = st.getColumns();

            paras = new ArrayList<String[]>();
            // 行循环
            for (int i = 1; i < rowNum; i++) {
                String[] para = new String[colNum];
                // 列循环
                for (int j = 0; j < colNum; j++) {
                    // 得到第j列第i行的数据
                    Cell cell = st.getCell(j, i);
                    if (cell.getContents().equals("") || cell.getContents() == null) {
                        para[j] = null;
                    } else {
                        para[j] = cell.getContents();
                    }
                }
                paras.add(para);
            }
        } catch (FileNotFoundException e) {
            throw new IOException("文件不存在");
        } catch (BiffException e) {
            throw new IOException("文件格式不正确");
        } catch (IOException e) {
            throw new IOException("文件读取失败");
        } finally {
            if (rwb != null) {
                rwb.close();
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return paras;
    }
}
