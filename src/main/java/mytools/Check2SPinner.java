
package mytools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.JSpinner.DateEditor;
import javax.swing.text.DateFormatter;

/**
 * 绘制JPanel边框,视觉上像是一个组件
 * 注意：JPanel布局方式，FlowLyout会有边界，这里用了BorderLyout布局
 */
public class Check2SPinner extends JPanel {

    private static final long serialVersionUID = 1L;
    private JCheckBox checkBox;
    // 复选框默认选中设置
    private Boolean choose;
    private JSpinner spinner;

    // private String dataFormat = MyUtil.DATA_FORMAT_PATTERN;
    // private SimpleDateFormat timeFormat = MyUtil.DataFormat_8;

    // 传递设定时间的构造函数
    public Check2SPinner(Boolean choose, String date) {
        this.choose = choose;
        Date setDate = Timestamp.valueOf(date);
        init(setDate);
    }

    // 传递当前系统时间的构造函数
    public Check2SPinner(Boolean choose, Date date) {
        this.choose = choose;
        init(date);
    }

    private void init(Date value) {
        // 复选框
        checkBox = new JCheckBox();
        checkBox.setBorder(null);
        checkBox.setOpaque(false);
        checkBox.setSelected(choose);
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (checkBox.isSelected())
                    spinner.setEnabled(true);
                else
                    spinner.setEnabled(false);
            }

        });

        spinner = MyUtil.createSpinner(value);
        spinner.setBorder(null);
        spinner.setEnabled(false);
        spinner.setToolTipText("提示：勾选后，双击选择相应的时间段进行更改");

        this.setLayout(new BorderLayout());
        this.add(checkBox, BorderLayout.WEST);
        this.add(spinner, BorderLayout.CENTER);
        // 背景色，视具体情况而定
        this.setBackground(Color.WHITE);
        this.setBorder(MyUtil.Component_Border);
    }

    public Date getTime() {
        if (checkBox.isSelected()) {
            return (Date) spinner.getValue();
        }
        return null;
    }

    public void setChoose(Boolean b) {
        checkBox.setSelected(b);
    }

}
