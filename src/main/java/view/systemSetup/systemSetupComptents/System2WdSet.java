package view.systemSetup.systemSetupComptents;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.Configure;
import mytools.EditButton;
import mytools.MyUtil;

import domain.WdPeriod;

public class System2WdSet extends JPanel {

	private JButton apply, cancel, edit;
	private Vector<Byte> wds, jgs;
	private JComboBox<Byte> jcbWd1, jcbWd2, jcbJg1, jcbJg2;

	public System2WdSet() {
		wds = new Vector<Byte>();
		jgs = new Vector<Byte>();
		for (byte i = 30; i <= 120; i++) {
			wds.add(i);
		}
		for (byte i = 1; i <= 120; i++) {
			jgs.add(i);
		}
		this.setLayout(null);
		this.setPreferredSize(new Dimension(500, 360));
		this.initContent();
		this.initToolbar();
		this.loadInfo();// 加载配置信息
	}

	private void initContent() {

		JPanel content = new JPanel(null);
		content.setBounds(0, 0, 500, 320);
		content.setBorder(MyUtil.Component_Border);
		this.add(content);

		int x = 40;
		int y = 60;
		int yheight = 60;
		int widthlabel = 100;
		int widthcombox = 240;
		int widthdw = 60;
		int height = 30;

		JLabel jlb1 = new JLabel("高温：", JLabel.LEFT);
		jlb1.setBounds(x, y, widthlabel, height);
		jlb1.setFont(MyUtil.FONT_20);
		content.add(jlb1);
		y += yheight;
		JLabel jlb2 = new JLabel("周期：", JLabel.LEFT);
		jlb2.setFont(MyUtil.FONT_20);
		jlb2.setBounds(x, y, widthlabel, height);
		content.add(jlb2);
		y += yheight;
		JLabel jlb3 = new JLabel("过热：", JLabel.LEFT);
		jlb3.setFont(MyUtil.FONT_20);
		jlb3.setBounds(x, y, widthlabel, height);
		content.add(jlb3);
		y += yheight;
		JLabel jlb4 = new JLabel("周期：", JLabel.LEFT);
		jlb4.setFont(MyUtil.FONT_20);
		jlb4.setBounds(x, y, widthlabel, height);
		content.add(jlb4);
		x = 160;
		y = 60;
		jcbWd1 = new JComboBox<Byte>(wds);
		jcbWd1.setBounds(x, y, widthcombox, height);
		jcbWd1.setFont(MyUtil.FONT_20);
		jcbWd1.setEnabled(false);
		content.add(jcbWd1);
		y += yheight;

		jcbJg1 = new JComboBox<Byte>(jgs);
		jcbJg1.setFont(MyUtil.FONT_20);
		jcbJg1.setBounds(x, y, widthcombox, height);
		jcbJg1.setEnabled(false);
		content.add(jcbJg1);
		y += yheight;

		jcbWd2 = new JComboBox<Byte>(wds);
		jcbWd2.setFont(MyUtil.FONT_20);
		jcbWd2.setBounds(x, y, widthcombox, height);
		jcbWd2.setEnabled(false);
		content.add(jcbWd2);
		y += yheight;

		jcbJg2 = new JComboBox<Byte>(jgs);
		jcbJg2.setFont(MyUtil.FONT_20);
		jcbJg2.setBounds(x, y, widthcombox, height);
		jcbJg2.setEnabled(false);
		content.add(jcbJg2);

		x = 420;
		y = 60;
		JLabel jlwddw1 = new JLabel("℃", JLabel.LEFT);
		jlwddw1.setFont(MyUtil.FONT_20);
		jlwddw1.setBounds(x, y, widthdw, height);
		content.add(jlwddw1);
		y += yheight;
		JLabel jlzqdw1 = new JLabel("分", JLabel.LEFT);
		jlzqdw1.setFont(MyUtil.FONT_20);
		jlzqdw1.setBounds(x, y, widthdw, height);
		content.add(jlzqdw1);
		y += yheight;
		JLabel jlwddw2 = new JLabel("℃", JLabel.LEFT);
		jlwddw2.setFont(MyUtil.FONT_20);
		jlwddw2.setBounds(x, y, widthdw, height);
		content.add(jlwddw2);
		y += yheight;
		JLabel jlzqdw2 = new JLabel("分", JLabel.LEFT);
		jlzqdw2.setFont(MyUtil.FONT_20);
		jlzqdw2.setBounds(x, y, widthdw, height);
		content.add(jlzqdw2);

	}

	// 初始化工具栏
	private void initToolbar() {
		JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
		toolbar.setBounds(0, 319, 500, 41);
		toolbar.setBorder(MyUtil.Component_Border);
		this.add(toolbar);

		Dimension buttonSize = new Dimension(60, 30);

		edit = new EditButton("修改", new ImageIcon("images/edit.png"));
		edit.setPreferredSize(buttonSize);
		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jcbWd1.setEnabled(true);
				jcbWd2.setEnabled(true);
				jcbJg2.setEnabled(true);
				jcbJg1.setEnabled(true);
				edit.setEnabled(false);
				apply.setEnabled(true);
				cancel.setEnabled(true);
			}
		});
		toolbar.add(edit);

		apply = new EditButton("应用", new ImageIcon("images/apply.png"));
		apply.setToolTipText("保存修改，并应用");
		apply.setName("apply");
		apply.setPreferredSize(buttonSize);
		apply.setEnabled(false);
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				byte wd1 = (Byte) jcbWd1.getSelectedItem();
				byte wd2 = (Byte) jcbWd2.getSelectedItem();
				byte jg1 = (Byte) jcbJg1.getSelectedItem();
				byte jg2 = (Byte) jcbJg2.getSelectedItem();
				if (wd2 > wd1 && jg2 < jg1) {
					WdPeriod wdPeriod = new WdPeriod(wd1, wd2, jg1, jg2);
					Configure.setWdPeriod(wdPeriod);
					jcbWd1.setEnabled(false);
					jcbWd2.setEnabled(false);
					jcbJg2.setEnabled(false);
					jcbJg1.setEnabled(false);
					edit.setEnabled(true);
					cancel.setEnabled(false);
					apply.setEnabled(false);
				} else {
					String msg = "";
					if (wd1 >= wd2) {
						msg += "第二温度低于(等于)第一温度\n";
					}
					if (jg1 <= jg2) {
						msg += "第二温度测量周期高于(等于)第一温度测量周期\n";
					}
					JOptionPane.showMessageDialog(null, msg, "错误", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		toolbar.add(apply);

		cancel = new EditButton("取消", new ImageIcon("images/cancel.png"));
		cancel.setEnabled(false);
		cancel.setPreferredSize(buttonSize);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadInfo();
				jcbWd1.setEnabled(false);
				jcbWd2.setEnabled(false);
				jcbJg1.setEnabled(false);
				jcbJg2.setEnabled(false);
				edit.setEnabled(true);
				cancel.setEnabled(false);
				apply.setEnabled(false);
			}
		});
		toolbar.add(cancel);

	}

	private void loadInfo() {
		WdPeriod wdPeriod = Configure.getWdPeriod();
		jcbWd1.setSelectedItem(wdPeriod.getWd1());
		jcbWd2.setSelectedItem(wdPeriod.getWd2());
		jcbJg1.setSelectedItem(wdPeriod.getJg1());
		jcbJg2.setSelectedItem(wdPeriod.getJg2());
	}

	public void setEditable(boolean b) {
		edit.setEnabled(b);
	}

}
