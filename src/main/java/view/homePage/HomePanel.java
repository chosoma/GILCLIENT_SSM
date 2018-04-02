package view.homePage;

import java.awt.*;

import javax.swing.*;

import com.LgoInfo;
import mytools.MyOutButton;
import mytools.MySkipButton;
import view.icon.*;

public class HomePanel extends GlassPanel {

    private static HomePanel homePanel;

    public static HomePanel getInstance() {
        if (homePanel == null) {
            homePanel = new HomePanel();
        }
        return homePanel;
    }

    private HomePanel() {
        this.init();
    }


    private void init() {
//        this.setOpaque(false);
        this.setLayout(new BorderLayout());

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbs = new GridBagConstraints();
        gbs.fill = GridBagConstraints.BOTH;
        gbs.gridwidth = 1;
        gbs.gridheight = 1;
        gbs.insets = new Insets(15, 10, 10, 10);
        gbs.weightx = 1;
        gbs.weighty = 1;


        JPanel center = new JPanel();
        center.setLayout(gbl);
        center.setOpaque(false);
        MySkipButton mtbSF6 = new MySkipButton("SF6", new SF6Icon(), 0);
        MySkipButton mtbTemp = new MySkipButton("温度", new TempIcon(), 1);
        MySkipButton mtbVari = new MySkipButton("伸缩节", new VariIcon(), 2);
        MySkipButton mobGZ = new MySkipButton("故障定位", new WarnIcon(), 4);
        MyOutButton mobJF = new MyOutButton("局放检测", new UnKnownIcon(), 1);
        MySkipButton mtbLadder = new MySkipButton("图形", new LadderIcon(), 3);
        center.add(mtbSF6);
        gbs.gridx = 0;
        gbs.gridy = 0;
        gbl.setConstraints(mtbSF6, gbs);
        center.add(mtbTemp);
        gbs.gridx = 1;
        gbs.gridy = 0;
        gbl.setConstraints(mtbTemp, gbs);
        center.add(mtbVari);
        gbs.gridx = 2;
        gbs.gridy = 0;
        gbl.setConstraints(mtbVari, gbs);
        center.add(mobGZ);
        gbs.gridx = 0;
        gbs.gridy = 1;
        gbl.setConstraints(mobGZ, gbs);
        center.add(mobJF);
        gbs.gridx = 1;
        gbs.gridy = 1;
        gbl.setConstraints(mobJF, gbs);
        center.add(mtbLadder);
        gbs.gridx = 2;
        gbs.gridy = 1;
        gbl.setConstraints(mtbLadder, gbs);
        this.add(center, BorderLayout.CENTER);

        JPanel bottom = new GlassPanel(new BorderLayout(), 0.3f);

        final JLabel companyName = new JLabel("© CopyRight " + LgoInfo.CopyrightName, JLabel.CENTER);
        companyName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        bottom.add(companyName, BorderLayout.NORTH);


        this.add(bottom, BorderLayout.SOUTH);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}