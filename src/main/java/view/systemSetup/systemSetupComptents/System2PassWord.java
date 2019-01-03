
package view.systemSetup.systemSetupComptents;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import mytools.EditButton;
import mytools.MyUtil;
import service.UserService;

/**
 * 修改密码 子面板
 */
public class System2PassWord extends JPanel {

    private JPasswordField jpfR31, jpfR32, jpfR33;
    private JButton cancel, edit;
    // 原密码，新密码

    public System2PassWord() {

        this.setLayout(null);
        this.setPreferredSize(new Dimension(500, 360));
        this.setBorder(MyUtil.Component_Border);
        this.initialize();
        this.initToolbar();
    }

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
                checkPassword();
            }
        });
        toolbar.add(edit);


        cancel = new EditButton("退出", new ImageIcon("images/cancel.png"));
        cancel.setPreferredSize(buttonSize);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelEdit();
            }
        });
        toolbar.add(cancel);

    }


    private void initialize() {

        int x = 40;
        int y = 80;
        int yheight = 60;
        int widthlabel = 100;
        int widthcombox = 300;
        int height = 30;

        JLabel jlR31 = new JLabel("当前密码:", JLabel.LEFT);
        jlR31.setBounds(x, y, widthlabel, height);
        jlR31.setFont(MyUtil.FONT_20);
        this.add(jlR31);

        y += yheight;
        JLabel jlR32 = new JLabel("新密码:", JLabel.LEFT);
        jlR32.setBounds(x, y, widthlabel, height);
        jlR32.setFont(MyUtil.FONT_20);
        this.add(jlR32);

        y += yheight;
        JLabel jlR33 = new JLabel("确认密码:", JLabel.LEFT);
        jlR33.setBounds(x, y, widthlabel, height);
        jlR33.setFont(MyUtil.FONT_20);
        this.add(jlR33);

        x = 160;
        y = 80;
        // 当前密码输入
        jpfR31 = new JPasswordField();
        jpfR31.setBounds(x, y, widthcombox, height);
        jpfR31.setFont(MyUtil.FONT_20);
        this.add(jpfR31);
        y += yheight;

        // 新密码输入
        jpfR32 = new JPasswordField();
        jpfR32.setBounds(x, y, widthcombox, height);
        jpfR32.setFont(MyUtil.FONT_20);
        this.add(jpfR32);
        y += yheight;

        // 新密码输入
        jpfR33 = new JPasswordField();
        jpfR33.setBounds(x, y, widthcombox, height);
        jpfR33.setFont(MyUtil.FONT_20);
        this.add(jpfR33);

    }

    private void cancelEdit() {
        jpfR31.setText(null);
        jpfR32.setText(null);
        jpfR33.setText(null);
    }

    private void checkPassword() {
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

    private boolean isNull(String s) {
        return s == null || s.equals("");
    }
}
