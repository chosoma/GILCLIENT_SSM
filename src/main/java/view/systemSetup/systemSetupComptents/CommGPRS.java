package view.systemSetup.systemSetupComptents;

import javax.swing.*;

import mytools.ClickButton;
import mytools.MyUtil;

import service.CollectService;
import view.systemSetup.SystemSetup;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommGPRS extends JPanel {

    public CommGPRS() {
        this.setPreferredSize(new Dimension(500, 360));
        this.setBorder(MyUtil.Component_Border);
        this.init();
    }

    private ClickButton jbtOpen1;
    private JLabel jlbOpen1, jlbWait1;

    private void init() {


        jbtOpen1 = new ClickButton("启动服务");
        jbtOpen1.setPreferredSize(new Dimension(100,  30));
        jbtOpen1.setSize(new Dimension(100, 30));
        jbtOpen1.setFont(MyUtil.FONT_20);
        this.add(jbtOpen1);

        jlbOpen1 = new JLabel("服务未启动", JLabel.CENTER);
        jlbOpen1.setPreferredSize(new Dimension(80, 20));
        jlbOpen1.setSize(new Dimension(80, 20));
        jlbOpen1.setFont(MyUtil.FONT_20);
        jbtOpen1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isOpen = jbtOpen1.isSelected();
                if (isOpen) {
                    CollectService.CloseColl1();
                    jbtOpen1.setSelected(false);
                    jlbOpen1.setText("服务已关闭");
                    jbtOpen1.setText("启动服务");
                    SystemSetup.getInstance().setEditable(false);
                } else {
                    jbtOpen1.setEnabled(false);
                    jlbOpen1.setEnabled(false);
                    jlbWait1.setVisible(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                CollectService.OpenColl1();
                                jbtOpen1.setSelected(true);
                                jbtOpen1.setText("关闭服务");
                                jlbOpen1.setText("服务已启动");
                                SystemSetup.getInstance().setEditable(true);
                            } catch (java.net.BindException be) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "端口被占用或程序已启动",
                                        "失败",
                                        JOptionPane.ERROR_MESSAGE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(
                                        null,
                                        e.getMessage(),
                                        "服务启动失败",
                                        JOptionPane.ERROR_MESSAGE);
                            } finally {
                                jlbOpen1.setEnabled(true);
                                jbtOpen1.setEnabled(true);
                                jlbWait1.setVisible(false);
                            }
                        }
                    }).start();
                }
            }
        });
        this.add(jlbOpen1);

        jlbWait1 = new JLabel(new ImageIcon("images/progress.gif"));
        jlbWait1.setPreferredSize(new Dimension(20, 20));
        jlbWait1.setVisible(false);
        this.add(jlbWait1);


        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbs = new GridBagConstraints();
        gbs.fill = GridBagConstraints.BOTH;
        gbs.gridwidth = 1;
        gbs.gridheight = 1;
        gbs.insets = new Insets(50, 50, 0, 50);
        gbs.weightx = 1;
        gbs.weighty = 1;
        gbs.gridx = 1;
        gbs.gridy = 1;
        gbl.setConstraints(jbtOpen1, gbs);

        gbs.gridy++;
        gbl.setConstraints(jlbOpen1, gbs);

        gbs.gridy++;
        gbl.setConstraints(jlbWait1, gbs);

        this.setLayout(gbl);

    }

    public void closeResource() {
        if (jbtOpen1.isSelected()) {
            jbtOpen1.doClick();
        }
    }


}
