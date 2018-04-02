
package view.systemSetup.systemSetupComptents;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import mytools.ClickButton;
import mytools.MyUtil;
import service.UserService;

/**
 * 修改密码 子面板
 */
public class System2PassWord extends JPanel {

    private JPasswordField jpfR31, jpfR32, jpfR33;

    // 原密码，新密码

    public System2PassWord() {

        this.setLayout(null);
        this.setPreferredSize(new Dimension(500, 360));
        this.setBorder(MyUtil.Component_Border);
        this.initialize();

    }

    private void initialize() {
        JLabel jlR31 = new JLabel("当前密码:", JLabel.RIGHT);
        jlR31.setBounds(50, 50, 150, 30);
        jlR31.setFont(MyUtil.FONT_14);
        this.add(jlR31);
        // 当前密码输入
        jpfR31 = new JPasswordField();
        jpfR31.setBounds(210, 50, 150, 30);
        jpfR31.setFont(MyUtil.FONT_14);
        this.add(jpfR31);

        JLabel jlR32 = new JLabel("新密码:", JLabel.RIGHT);
        jlR32.setBounds(50, 100, 150, 30);
        jlR32.setFont(MyUtil.FONT_14);
        this.add(jlR32);
        // 新密码输入
        jpfR32 = new JPasswordField();
        jpfR32.setBounds(210, 100, 150, 30);
        jpfR32.setFont(MyUtil.FONT_14);
        this.add(jpfR32);

        JLabel jlR33 = new JLabel("确认新密码:", JLabel.RIGHT);
        jlR33.setBounds(50, 150, 150, 30);
        jlR33.setFont(MyUtil.FONT_14);
        this.add(jlR33);
        // 确认新密码输入
        jpfR33 = new JPasswordField();
        jpfR33.setBounds(210, 150, 150, 30);
        jpfR33.setFont(MyUtil.FONT_14);
        this.add(jpfR33);

        JButton jbR31 = new ClickButton("确定", new ImageIcon("images/apply.png"));
        jbR31.setBounds(130, 200, 100, 30);
        jbR31.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password1 = new String(jpfR31.getPassword())
                        .toLowerCase();
                String password2 = new String(jpfR32.getPassword())
                        .toLowerCase();
                String password3 = new String(jpfR33.getPassword())
                        .toLowerCase();
                // 做个密码框是不是都有输入
                if (isNull(password1) || isNull(password2) || isNull(password3)) {
                    JOptionPane.showMessageDialog(null, "密码不能为空", "提示",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // 判断新密码是否与当前密码一致
                if (password2.equals(password1)) {
                    JOptionPane.showMessageDialog(null, "新密码不能与当前密码一致", "错误",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 判断两次新密码是否一致
                if (!password3.equals(password2)) {
                    JOptionPane.showMessageDialog(null, "两次新密码不一致", "错误",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 判断当前密码是否正确
                if (!UserService.checkPassWord(password1)) {
                    JOptionPane.showMessageDialog(null, "当前密码错误", "错误",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    UserService.changePassword(password2);
                    JOptionPane.showMessageDialog(null, "密码修改成功", "成功",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "密码修改失败", "错误",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    jpfR31.setText(null);
                    jpfR32.setText(null);
                    jpfR33.setText(null);
                }
            }
        });
        this.add(jbR31);

        JButton jbR32 = new ClickButton("取消", new ImageIcon("images/cancel.png"));
        jbR32.setBounds(260, 200, 100, 30);
        jbR32.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpfR31.setText(null);
                jpfR32.setText(null);
                jpfR33.setText(null);
            }
        });
        this.add(jbR32);

    }
    private boolean isNull(String s) {
        return s == null || s.equals("");
    }
}
