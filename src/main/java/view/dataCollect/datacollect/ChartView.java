package view.dataCollect.datacollect;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import domain.*;
import service.SensorService;
import view.dataCollect.graph.GraphView;
import view.dataCollect.hitch.HitchView;

public class ChartView extends JPanel {

    private CardLayout centerCard;// 卡片布局
    private static ChartView CV;
    private AbcView panelSF6, panelWd, panelSSJ;
    private GraphView panelGraph;
    private HitchView panelHitch;

    private ChartView() {
        init();
        loadSensor();
    }

    public void loadSensor() {
        List<AbcUnitView> views = SensorService.getAbcUnitViews();
        panelSF6.clearAbcUnits();
        panelSSJ.clearAbcUnits();
        panelWd.clearAbcUnits();
        for (AbcUnitView view : views) {
            byte type = view.getPointBean().getUnitType();
            switch (type) {
                case 1:
                    panelSF6.addAbcUnit(view);
                    break;
                case 2:
                    panelSSJ.addAbcUnit(view);
                    break;
                case 3:
                    panelWd.addAbcUnit(view);
                    break;
            }
        }
    }

    public static ChartView getInstance() {
        if (CV == null) {
            synchronized (ChartView.class) {
                CV = new ChartView();
            }
        }
        return CV;
    }

    private void init() {
        centerCard = new CardLayout();
        this.setLayout(centerCard);
        panelSF6 = new AbcView();
        this.add(panelSF6, "SF6");
        panelWd = new AbcView();
        this.add(panelWd, "WD");
        panelSSJ = new AbcView();
        this.add(panelSSJ, "SSJ");
        Image backgroud;
        try {
            backgroud = ImageIO.read(this.getClass().getClassLoader().getResource("icon/indexGroud.png"));
            panelGraph = new GraphView(backgroud);
            this.add(panelGraph, "TX");
            panelHitch = new HitchView(backgroud);
            this.add(panelHitch, "GZ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hiddenUser() {
        panelSSJ.hidenUser();
        panelSF6.hidenUser();
        panelWd.hidenUser();
        panelHitch.hiddenUser();
    }

    public void startWarn(WarnBean warnBean) {
        panelGraph.startWarn(warnBean);

    }

    public void removeWarning(WarnBean warnBean) {
        panelGraph.removeWarning(warnBean);
        panelHitch.removeWarning(warnBean);
    }


    public void alignZero(UnitBean unit) {
        panelGraph.alignZero(unit);
    }


    public void receDatas(boolean flag, DataBean... datas) {
        List<DataBean> dataBeans = new ArrayList<DataBean>();
        dataBeans.addAll(Arrays.asList(datas));
        receDatas(dataBeans, flag);
    }

    public void receInitTemp(DataBean data) {
        panelWd.setInitTemp(data);
    }

    public void receDatas(List<DataBean> dataList, boolean flag) {
        for (DataBean data : dataList) {
            byte type = data.getUnitType();
            switch (type) {
                case 1:
                    panelSF6.addData(data, flag);
                    break;
                case 2:
                    panelSSJ.addData(data, flag);
                    break;
                case 3:
                    panelWd.addData(data, flag);
                    break;
                case 4:
                    panelHitch.checkHitch(data);
                    break;
            }
            panelGraph.addData(data);
        }
    }


    public void setTitle(PointBean pointBean) {
        switch (pointBean.getUnitType()) {
            case 1:
                panelSF6.setTitle(pointBean);
                break;
            case 2:
                panelSSJ.setTitle(pointBean);
                break;
            case 3:
                panelWd.setTitle(pointBean);
                break;
        }
    }


    public void showPane(String name) {
        centerCard.show(this, name);
    }
}
