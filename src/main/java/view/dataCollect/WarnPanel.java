package view.dataCollect;

import domain.WarnBean;
import mytools.ChangeButton;
import service.WarningService;
import view.dataCollect.datacollect.ChartView;
import view.dataCollect.datacollect.CollectShow;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class WarnPanel extends JPanel {
    private WarnBean warnBean;

    private Color colorWarn = new Color(255, 80, 0);

    public WarnBean getWarnBean() {
        return warnBean;
    }

    public WarnPanel(WarnBean warnBean) {
        this.warnBean = warnBean;

        init();
    }

    private JLabel warnlabel;

    private void init() {
        this.setLayout(new BorderLayout());
        this.setBorder(new LineBorder(Color.WHITE, 1));
//        this.setOpaque(false);

        warnlabel = new JLabel("", JLabel.CENTER);
        warnlabel.setBackground(colorWarn);

        warnlabel.setText(getWarnInfo());
        this.add(warnlabel);


        JButton jb = new ChangeButton("解除警报", new ImageIcon("images/delete.png"));
        jb.setOpaque(true);
        this.add(jb, BorderLayout.EAST);

        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int isHandle = JOptionPane.showConfirmDialog(null, "是否解除报警?", "解除报警", JOptionPane.OK_CANCEL_OPTION);
                if (isHandle != JOptionPane.OK_OPTION) {
                    return;
                }
                ChartView.getInstance().removeWarning(getWarnBean());
                CollectShow.getInstance().removeWarning(WarnPanel.this);
                try {
                    WarningService.updateHandle(warnBean);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                cancelTimer();
                WarnPanel.this.setBackground(colorWarn);
            }
        });

//        CollectShow.getInstance().addWarning(this);
//        ChartView.getInstance().startWarn(warnBean);
        initTimer();
    }

    private String getWarnInfo() {
        StringBuilder sb = new StringBuilder();
//        switch (warnBean.getPointBean().getUnitType()) {
//            case 1:
//                sb.append("监测类型:SF6,");
//                break;
//            case 2:
//                sb.append("监测类型:伸缩节,");
//                break;
//            case 3:
//                sb.append("监测类型:温度,");
//                break;
//
//        }
        sb.append("监测点:");
        sb.append(warnBean.getPointBean().getPlace());
        sb.append(",相位:");
        sb.append(warnBean.getXw());
        sb.append(",");
        sb.append(warnBean.getInfo());
        sb.append(" 时间:");
        sb.append(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(warnBean.getDate()));
        return sb.toString();
    }

    public void resetWarn(WarnBean warnBean) {
        this.warnBean = warnBean;
        warnlabel.setText(getWarnInfo());
        flag = true;
    }

    private int index;
    private boolean flag = true;

    private void initTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (flag) {
                    index++;
                    if (index % 2 == 0) {
                        WarnPanel.this.setBackground(new Color(255, 255, 255));
                    } else {
                        WarnPanel.this.setBackground(colorWarn);
                    }
                } else {
                    WarnPanel.this.setBackground(colorWarn);
                }

            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 500);
    }

    private void cancelTimer() {
        flag = false;
    }

}
