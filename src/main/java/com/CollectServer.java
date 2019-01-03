package com;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import domain.NetBean;
import service.UnitService;
import domain.UnitBean;


/**
 * 服务相关
 */

public class CollectServer {

    private ServerSocket ss;
    private List<CollectSocket> listST = new ArrayList<CollectSocket>();


    private static CollectServer Collect = new CollectServer();

    private CollectServer() {
    }

    public static CollectServer getInstance() {
        return Collect;
    }

    /**
     * 启动服务
     */
    public void openConnection()  {

        int localPort = Integer.valueOf(Configure.getLocalPort());
        try {
			ss = new ServerSocket(localPort);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "程序已经启动");
			System.exit(0);
			e1.printStackTrace();
			return;
		}
        listST.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Socket s = ss.accept();
                        new Thread(new CollectSocket(s)).start();
                    }
                } catch (IOException e) {
                    // e.printStackTrace();
                } finally {
                    // 关闭serverSocket
                    if (ss!=null&&!ss.isClosed()) {
                        try {
                            ss.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    //
//    /**
//     * 关闭服务
//     */
    public void closeConnection() {
        try {
        	if(ss!=null) {        		
        		ss.close();
        	}
        } catch (IOException e1) {
        	JOptionPane.showMessageDialog(null, "程序已启动");
        	System.exit(0);
            e1.printStackTrace();
        }
        // 关闭socket
        for (int i = listST.size() - 1; i >= 0; i--) {
            CollectSocket st = listST.get(i);
            try {
                st.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        listST.clear();
    }

    // 移除Socket
    synchronized void removeSocket(CollectSocket st) {
        listST.remove(st);
        checkUseful();
    }

    // 添加Socket，并移除关闭和未连接的
    synchronized void addSocket(CollectSocket st) {
        listST.add(st);
        checkUseful();
    }

    private void checkUseful() {
        for (int i = 0; i < listST.size(); i++) {
            CollectSocket s = listST.get(i);
            if (s.isClosed() || !s.isConnected()) {
                listST.remove(i);
                i--;
            }
        }
    }

    /**
     * 设置报警
     */
    public void setAlarm(NetBean net, byte time) {
        for (int i = 0; i < listST.size(); i++) {
            CollectSocket s = listST.get(i);
            if (s.isUseful()) {
                if (net.getType() == s.getNetType() && net.getNumber() == s.getNetId()) {
                    try {
                        s.setAlarm(time);
                    } catch (IOException e) {
                        // e.printStackTrace();
                        listST.remove(i);
                        i--;
                    }
                }
            } else {
                listST.remove(i);
                i--;
            }
        }

    }

    public boolean isNullLinked() {
        return listST.size() == 0;
    }


    /*
        server-client设置采集周期
     */
    public void applyUnitOffline(byte unitType, Byte unitNumber, byte jg) {
        if (unitNumber == null) {
            Vector<UnitBean> beans = UnitService.getUnitBeans(unitType);
            if (beans.size() <= 0) {
                return;
            }
            for (UnitBean unitBean : beans) {
                applyUnitOffline(unitType, unitBean.getNumber(), jg);
            }
        } else {
            UnitBean unitBean = UnitService.getUnitBean(unitType, unitNumber);
            for (int i = 0; i < listST.size(); i++) {
//                System.out.println("\n"+i);
                CollectSocket s = listST.get(i);
                if (s.isUseful()) {
//                    if (unitBean != null && unitBean.getGatewaytype() == s.getNetType() && unitBean.getGatewaynumber() == s.getNetId()) {
                    try {
                        s.setJg(unitBean, jg);
                        // break;
                    } catch (IOException e) {
                        // e.printStackTrace();
                        listST.remove(i);
                        i--;
                    }
//                    }
                } else {
                    listST.remove(i);
                    i--;
                }
            }
        }
    }
    public void applyUnitOffline(byte[] bytes) {

    }



}
