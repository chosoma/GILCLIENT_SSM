package com;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import domain.IconConfig;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import domain.WdPeriod;

/**
 * 数据库链接参数
 */
public class Configure {

    private static String fileName = "propertyInfo/properties.xml";

    private static String SQLurl;// 数据库连接字符串
    private static String LocalPort;// 本地端口
    private static int Preheat;// 预热时间（毫秒）
    private static boolean VioceWarn;// 报警
    private static boolean RunningVoiceWarn;//正在报警
    private static WdPeriod wdPeriod;
    private static String jfstr;

    private static IconConfig[] iconConfigs;// 故障定位所有图标的位置属性

    static {
        File file = new File(fileName);
        if (!file.exists()) {
            intiDefault();
            createConfigXML();
        } else {
            try {
                SAXReader reader = new SAXReader();
                Document document = reader.read(file);

                Element root = document.getRootElement();

                // ***********采集***********
                Element collection = root.element("collection");
//                Element CommDelay = (Element) collection.selectSingleNode("commDelay");
//                String wait = CommDelay.attributeValue("wait");
//                String space = CommDelay.attributeValue("space");
//                String reply = CommDelay.attributeValue("reply");
//                String timeout = CommDelay.attributeValue("timeout");
//                DelayGPRS = new DelayBean(wait, space, reply, timeout);


                Element wdP = (Element) collection.selectSingleNode("wdPeriod");
                byte wd1 = Byte.valueOf(wdP.attributeValue("wd1").trim());
                byte wd2 = Byte.valueOf(wdP.attributeValue("wd2").trim());
                byte jg1 = Byte.valueOf(wdP.attributeValue("jg1").trim());
                byte jg2 = Byte.valueOf(wdP.attributeValue("jg2").trim());
                wdPeriod = new WdPeriod(wd1, wd2, jg1, jg2);

                // ***********通讯***********
                Element communication = root.element("communication");
                Element localport = (Element) communication
                        .selectSingleNode("GPRS");
                LocalPort = localport.attributeValue("localport").trim();

                // ***********数据库***********
                Element database = root.element("database");
                String ip = database.attributeValue("ip");
                String port = database.attributeValue("port");
                String dbname = database.attributeValue("dbname");
                ip = ip == null ? "localhost" : ip;
                port = port == null ? "3306" : port;
                dbname = dbname == null ? "gil" : dbname;
                SQLurl = "jdbc:mysql://" + ip + ":" + port + "/" + dbname + "?characterEncoding=UTF-8";

                Element lib = root.element("start");
                jfstr = lib.attributeValue("jflib");

                iconConfigs = new IconConfig[6];
                for (int i = 0; i < iconConfigs.length; i++) {
                    Element iconconfig = root.element("icon" + i);
                    int point = Integer.parseInt(iconconfig.attributeValue("point"));
                    String xw = iconconfig.attributeValue("xw");
                    float x = Float.parseFloat(iconconfig.attributeValue("x"));
                    float y = Float.parseFloat(iconconfig.attributeValue("y"));
                    float width = Float.parseFloat(iconconfig.attributeValue("width"));
                    float height = Float.parseFloat(iconconfig.attributeValue("height"));
                    String iconname = iconconfig.attributeValue("iconname");
                    iconConfigs[i] = new IconConfig(point, xw, x, y, width, height,iconname);
                }
                System.out.println(Arrays.toString(iconConfigs));


            } catch (DocumentException e1) {
                e1.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
                file.delete();
                intiDefault();
                createConfigXML();
            }
        }
    }


    /**
     * 获取gateway的本地端口
     *
     * @return
     */
    public static String getLocalPort() {
        return LocalPort;
    }

    /**
     * 缺省配置
     */
    private static void intiDefault() {
        VioceWarn = false;
        LocalPort = "8088";
        String ip = "localhost", port = "3306", dbname = "gil";
        SQLurl = "jdbc:mysql://" + ip + ":" + port + "/" + dbname;
        wdPeriod = new WdPeriod((byte) 100, (byte) 100, (byte) 240, (byte) 240);
        Preheat = 80000;
//        DelayGPRS = new DelayBean("30000", "20000", "10000", "300000");
        jfstr = "jf.exe";
    }

    private static void createConfigXML() {
        // 建立document对象
        Document document = DocumentHelper.createDocument();
        Element property = document.addElement("property");// 添加文档根
        // ***********采集***********
        property.addComment("采集");// 加入一行注释
        Element collection = property.addElement("collection"); // 添加property的子节点

        collection.addComment(" 温度频率");// 加入一行注释
        Element wd = collection.addElement("wdPeriod"); // 添加collection的子节点
        wd.addAttribute("wd1", wdPeriod.getWd1() + "");
        wd.addAttribute("wd2", wdPeriod.getWd2() + "");
        wd.addAttribute("jg1", wdPeriod.getJg1() + "");
        wd.addAttribute("jg2", wdPeriod.getJg2() + "");

        // ***********通讯***********
        property.addComment("通讯");// 加入一行注释
        Element communication = property.addElement("communication"); // 添加property的子节点
        communication.addComment("GPRS/CDMA");// 加入一行注释
        Element GPRS = communication.addElement("GPRS"); // 添加communication的子节点
        GPRS.addAttribute("localport", LocalPort);

        // ***********数据库***********
        property.addComment("数据库");// 加入一行注释
        Element database = property.addElement("database"); // 添加property的子节点
        database.addAttribute("ip", "localhost");
        database.addAttribute("port", "3306");
        database.addAttribute("dbname", "gil");

        Element lib = property.addElement("start");
        lib.addAttribute("jflib", "jf.exe");

        for (int i = 0; i < 6; i++) {
            Element iconi = property.addElement("icon" + i);
            switch (i / 3) {
                case 0:
                    iconi.addAttribute("point", String.valueOf(401));
                    break;
                case 1:
                    iconi.addAttribute("point", String.valueOf(402));
                    break;
            }
            switch (i % 3) {
                case 0:
                    iconi.addAttribute("xw", "A");
                    break;
                case 1:
                    iconi.addAttribute("xw", "B");
                    break;
                case 2:
                    iconi.addAttribute("xw", "C");
                    break;
            }

            iconi.addAttribute("x", String.valueOf(i % 3 * 0.33));
            iconi.addAttribute("y", String.valueOf(i / 3 * 0.5));
            iconi.addAttribute("width", String.valueOf(0.33));
            iconi.addAttribute("height", String.valueOf(0.5));
            iconi.addAttribute("iconname", "images/conduit" + i+".png");
        }

        try {
            push2file(document);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void push2file(Document document) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(new FileOutputStream(fileName), format);
        writer.write(document);
        writer.close();
    }

    /**
     * 获取sql连接URL
     */
    public static String getSQLurl() {
        return SQLurl;
    }


    /**
     * 获取温度频率
     *
     * @return
     */
    public static WdPeriod getWdPeriod() {
        return wdPeriod;
    }

    /**
     * 设置温度频率
     *
     * @param wdPeriod2
     */
    public static void setWdPeriod(WdPeriod wdPeriod2) {
        wdPeriod = wdPeriod2;
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(fileName));
            Element choose = (Element) document.selectSingleNode("//collection/wdPeriod");
            if (choose != null) {
                choose.addAttribute("wd1", wdPeriod2.getWd1() + "");
                choose.addAttribute("wd2", wdPeriod2.getWd2() + "");
                choose.addAttribute("jg1", wdPeriod2.getJg1() + "");
                choose.addAttribute("jg2", wdPeriod2.getJg2() + "");
            }
            push2file(document);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getJfstr() {
        return jfstr;
    }

    public static IconConfig getIconConfig(int point) {
        for (IconConfig iconconfig :
                iconConfigs) {
            if (point == iconconfig.getPoint()) {
                return iconconfig;
            }
        }
        return null;
    }

    public static IconConfig[] getIconConfigs() {
        return iconConfigs;
    }
}
