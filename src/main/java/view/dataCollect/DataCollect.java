package view.dataCollect;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.*;

import domain.DataBean;
import service.DataService;
import view.Shell;
import view.dataCollect.datacollect.CollectShow;

public class DataCollect {
    private static DataCollect DC = new DataCollect();
    private JComponent contentPane = null;

    private DataCollect() {

    }

    public static DataCollect getInstance() {
        return DC;
    }

    public JComponent CreatContentPanel() {
        if (contentPane == null) {
            synchronized (DataCollect.class) {
                if (contentPane == null) {
                    this.init2();
                }
            }
        }
        return contentPane;
    }

//    private ScheduledThreadPoolExecutor scheduler = null;
//    RunnableScheduledFuture<?> rsfRoll;


    private void init2() {
        CollectShow collectShow = CollectShow.getInstance();
//        scheduler = new ScheduledThreadPoolExecutor(1);
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(collectShow, BorderLayout.CENTER);
        contentPane.setEnabled(false);
        contentPane.setBorder(null);
        contentPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
//                if (collectShow.isNeedRoll()) {
//                     TODO Auto-generated method stub
                    Shell.getInstance().showButton(true);
//                }
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                // TODO Auto-generated method stub
                Shell.getInstance().showButton(false);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataBean latestAmtemp = DataService.getLatestAmtemp();
                    List<DataBean> datas = DataService.getLatestDatas();
                    if (latestAmtemp != null) {
                        CollectShow.getInstance().receInitTemp(latestAmtemp);
                    }
                    CollectShow.getInstance().receDatas(datas, false);

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();

    }
}
