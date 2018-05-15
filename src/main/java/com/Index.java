package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.SQLException;

import javax.swing.*;

import mytools.ChartUtil;
import service.NetService;
import service.PointService;
import service.UnitService;
import view.debugs.Debugs;
import view.Login;
import view.Shell;
import view.dataCollect.DataCollect;
import view.dataManage.DataManage;
import view.hitchManage.HitchManage;
import view.homePage.HomePanel;
import view.systemSetup.SystemSetup;
import view.warnManage.WarnManage;

public class Index {

    private Shell shell;
    private FileLock lock;// 必须是全局变量，不然无效

    public Index() {
//         this.common();
//        this.server();
        this.init();
    }

    private void common() {
        try {
            File lockFile = new File(System.getProperty("user.home"),
                    ".dpsLock");
            FileChannel chanel = new FileOutputStream(lockFile).getChannel();
            lock = chanel.tryLock();
            if (lock == null) {
                JOptionPane.showMessageDialog(null, "程序已经启动", "错误",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String processName = getProcessName();
        if (processName != null && !processName.equals("DPS")) {
            System.exit(0);
        }
    }

    private void server() {
        try {
//            File lockFile = new File(System.getProperty("user.home"), ".mylock");
            FileChannel chanel = new FileOutputStream(".dpsLock").getChannel();
            lock = chanel.tryLock();
            if (lock == null) {
                JOptionPane.showMessageDialog(null, "程序已经启动", "错误", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
//        String info = runtime.getUsername();
//        int index = info.indexOf("@");
//        String pid = info.substring(0, index);
//        String processName = getProcessName(pid);
//        LgoInfo.SoftName = processName;
    }

    private void init() {
        ChartUtil.setChartTheme();

        Login login = new Login();// 用户登录界面

        login.loading(0, "starting... ...");


        login.loading(10, "Loading DataCollection... ...");
        try {
            NetService.init();
            PointService.init();
            UnitService.init();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "数据库链接失败!", "错误", JOptionPane.ERROR_MESSAGE);
            login.dispose();
        }
        shell = Shell.getInstance();
        login.loading(20, "Loading Homepage... ...");
        shell.addItem(HomePanel.getInstance(), new ImageIcon("images/main/home_24.png"), "主界面");

        login.loading(30, "Loading Views... ...");
        shell.addItem(DataCollect.getInstance().CreatContentPanel(), new ImageIcon("images/main/collect_24.png"), "数据采集");

        login.loading(40, "Loading DataManagerment... ...");

        shell.addItem(DataManage.getInstance(), new ImageIcon("images/main/manage_24.png"), "数据管理");

        login.loading(50, "Loading WarnManagerment... ...");
        shell.addItem(WarnManage.getInstance(), new ImageIcon("images/main/warn_24.png"), "警报查询");
//        shell.addItem(HitchManage.getInstance(), new ImageIcon("images/main/error.png"), "故障查询");
//        login.loading(60, "Loading DataChart... ...");

//        shell.addItem(SensorMatch.getInstance(), new ImageIcon("images/main/sensor_24.png"), "仪器配置");

        login.loading(70, "Loading SystemSetup... ...");

        shell.addItem(SystemSetup.getInstance(), new ImageIcon("images/main/system_24.png"), "系统设置");

        login.loading(80, "Loading Debug... ...");

        shell.addItem(Debugs.getInstance(), new ImageIcon("images/main/debug_24.png"), "调试信息", false);
        login.loading(90, "Loading Controller... ...");
        // 连接设置、传感器配置、采集界面控制器

        login.loading(100, "Starting... ...");
        login.toLogin();

    }

    private String getProcessName() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String info = runtime.getName();
        int indexPID = info.indexOf("@");

        String pid = info.substring(0, indexPID);
        BufferedReader bufferedReader = null;
        try {
            Process proc = Runtime.getRuntime().exec(
                    "tasklist /FI \"PID eq " + pid + "\"");
            bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(pid + "")) {
                    int indexName = line.indexOf(".exe");
                    return line.substring(0, indexName);
                }
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ignored) {
                }
            }
        }

    }


    public static void main(String[] args) {
        new DefaultUI();
        new Index();
    }


}
