package mytools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import view.icon.MyIconFactory;

public class MyFoldPanel extends JPanel {
	private boolean isHiden = false;
	private JButton button;
	private JComponent cpt;
	public static Font LabelFont = new Font("微软雅黑", Font.PLAIN, 15);
	static int Height = 24;
	static Border border = BorderFactory.createEmptyBorder(0, 5, 0, 0);

	public MyFoldPanel(String text, JComponent component) {
		this(text, false, component);
	}

	public MyFoldPanel(String text, boolean isHide, JComponent component) {
		this.cpt = component;
		this.isHiden = isHide;
		this.setLayout(new BorderLayout());
		JComponent toolBar;
		// toolBar = new TitlePanel(new BorderLayout());
		toolBar = new JPanel(new BorderLayout());
		toolBar.setBackground(new Color(40, 138, 221));
		toolBar.setBorder(border);
		toolBar.setPreferredSize(new Dimension(cpt.getPreferredSize().width,
				Height));
		this.add(toolBar, BorderLayout.NORTH);
		this.add(cpt, BorderLayout.CENTER);
		JLabel jlb = new JLabel(text);
		jlb.setForeground(Color.WHITE);
		jlb.setFont(LabelFont);
		toolBar.add(jlb, BorderLayout.WEST);
		button = new JButton(MyIconFactory.getFoldIcon());
		button.setFocusable(false);
		// 无边框
		button.setBorder(null);
		// 取消绘制按钮内容区域
		button.setContentAreaFilled(false);
		// 设置按钮按下后无虚线框
		button.setFocusPainted(false);
		toolBar.add(button, BorderLayout.EAST);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isHiden = !isHiden;
				hideComponent();
			}
		});
		hideComponent();
		this.setBorder(MyUtil.Component_Border);
	}

	private void hideComponent() {
		button.setSelected(isHiden);
		cpt.setVisible(!isHiden);
		this.repaint();
		this.revalidate();
	}
}
