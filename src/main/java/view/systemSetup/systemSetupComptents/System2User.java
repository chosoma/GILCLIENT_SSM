package view.systemSetup.systemSetupComptents;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import mytools.EditButton;
import mytools.MyUtil;
import service.UserService;
import view.ModifiedFlowLayout;
import domain.UserBean;

/**
 * 用户管理
 */
public class System2User extends JPanel {

    private JLabel tempLabel;
    private JPanel center, bottomPane;
    private JButton refreshUser;
    private ImageIcon icon1 = new ImageIcon("images/admin.png");
    private ImageIcon icon2 = new ImageIcon("images/user.png");
    // 底部添加用户组件
    private JTextField jtf;
    private JComboBox jcb;

    public System2User() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(500, 400));
        this.initialize();
    }

    private void initialize() {

        JLabel jlabel = new JLabel("请选择要更改的用户", JLabel.LEFT);
        jlabel.setFont(MyUtil.FONT_18);
        jlabel.setBounds(0, 0, 500, 31);
        jlabel.setBorder(MyUtil.Component_Border);
        this.add(jlabel);

        center = new JPanel(new ModifiedFlowLayout(FlowLayout.LEFT, 20, 20));
        JScrollPane scrollPane = new JScrollPane(center);
        scrollPane.setBounds(0, 30, 500, 296);
        this.add(scrollPane);

        JPanel toolPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        toolPane.setBounds(0, 325, 500, 35);
        toolPane.setBorder(MyUtil.Component_Border);
        this.add(toolPane);

        Dimension buttonSize = new Dimension(60, 30);

        //添加用户:展开"添加用户"面板,执行添加操作
        JButton addUser = new EditButton("添加", new ImageIcon("images/add.png"));
        addUser.setToolTipText("添加用户");
        addUser.setPreferredSize(buttonSize);
        addUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                bottomPane.setVisible(true);
            }
        });
        toolPane.add(addUser);

        //更改用户类型:修改用户权限，对用户进行管理员与普通用户的切换
        JButton changeUser = new EditButton("更改", new ImageIcon("images/change.png"));
        changeUser.setToolTipText("更改用户类型");
        changeUser.setName("更改");
        changeUser.setPreferredSize(buttonSize);
        changeUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                UserBean user = getSelectUser();
                if (user != null) {
                    int flag = JOptionPane.showConfirmDialog(null,
                            "确定要更改该用户类型？", "提示", JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (flag != JOptionPane.OK_OPTION) {
                        return;
                    }
                    try {
                        UserService.changeRole(user);
                        if (tempLabel.getIcon().equals(icon1)) {
                            tempLabel.setIcon(icon2);
                            tempLabel.setToolTipText(UserService.USER);
                        } else {
                            tempLabel.setIcon(icon1);
                            tempLabel.setToolTipText(UserService.ADMIN);
                        }
                        JOptionPane.showMessageDialog(null, "用户类型已成功更改", "成功",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "用户类型更改失败", "失败",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        toolPane.add(changeUser);

        //删除用户:将用户删除
        JButton deleteUser = new EditButton("删除", new ImageIcon("images/delete.png"));
        deleteUser.setName("删除");
        deleteUser.setToolTipText("删除用户");
        deleteUser.setPreferredSize(buttonSize);
        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserBean user = getSelectUser();
                if (user != null) {
                    int flag = JOptionPane.showConfirmDialog(null, "确定要删除该用户？",
                            "提示", JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (flag != JOptionPane.OK_OPTION) {
                        return;
                    }
                    try {
                        UserService.deleteUser(user);
                        center.remove(tempLabel);
                        center.repaint();
                        center.revalidate();
                        tempLabel = null;
                        JOptionPane.showMessageDialog(null, "用户已成功删除", "成功",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "用户删除失败", "失败",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        toolPane.add(deleteUser);

        // 刷新用户列表:刷新系统内所有用户，刷新用户列表
        refreshUser = new EditButton("刷新", new ImageIcon("images/refresh.png"));
        refreshUser.setName("刷新");
        refreshUser.setToolTipText("刷新用户列表");
        refreshUser.setPreferredSize(buttonSize);
        refreshUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<UserBean> users;
                try {
                    users = UserService.query();
                    refreshUsers(users);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        toolPane.add(refreshUser);

        //"添加用户"面板
        bottomPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        bottomPane.setBounds(0, 359, 500, 34);
        bottomPane.setBorder(MyUtil.Component_Border);
        bottomPane.setVisible(false);
        this.add(bottomPane);

        JLabel jl1 = new JLabel("用户名称:", JLabel.RIGHT);
        jl1.setPreferredSize(new Dimension(60, 20));
        bottomPane.add(jl1);

        jtf = new JTextField();
        jtf.setFocusable(true);
        jtf.setPreferredSize(new Dimension(70, 20));
        jtf.setBorder(MyUtil.Component_Border);
        bottomPane.add(jtf);

        JLabel jl2 = new JLabel("用户类型:", JLabel.RIGHT);
        jl2.setPreferredSize(new Dimension(60, 20));
        bottomPane.add(jl2);

        jcb = new JComboBox<>(new String[]{UserService.ADMIN, UserService.USER});
        jcb.setSelectedItem(null);
        jcb.setFocusable(true);
        jcb.setPreferredSize(new Dimension(80, 20));
        bottomPane.add(jcb);

        JButton apply = new EditButton("确认", new ImageIcon("images/apply.png"));
        apply.setName("确认");
        apply.setToolTipText("确认");
        apply.setPreferredSize(buttonSize);
        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                UserBean user = new UserBean();
                String name = jtf.getText().trim().toLowerCase();
                if (isNull(name)) {
                    JOptionPane.showMessageDialog(null, "请输入用户名称", "提示", JOptionPane.ERROR_MESSAGE);
                }
                user.setUsername(name);
                String status = (String) jcb.getSelectedItem();
                if (status == null) {
                    JOptionPane.showMessageDialog(null, "请选择用户类型", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (status.equals(UserService.ADMIN)) {
                    user.setAuthority(UserService.AP);
                } else {
                    user.setAuthority(UserService.UP);
                }
                try {
                    UserBean userBean = UserService.checkUser(user.getUsername());
                    if (userBean != null) {
                        JOptionPane.showMessageDialog(null,
                                "该用户已存在，不能重复添加", "失败",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    UserService.addUser(user);
                    refreshUser.doClick();// 刷新用户列表
                    int flag = JOptionPane.showConfirmDialog(null,
                            "初始密码:123456,是否继续添加用户？", "添加成功",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);
                    if (flag == JOptionPane.YES_OPTION) {
                        clearBottomPane();// 清空"添加用户"面板
                    } else {
                        HideBottomPane();// 隐藏"添加用户"面板
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "该用户已存在，不能重复添加", "添加失败", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        bottomPane.add(apply);

        JButton cancel = new EditButton("取消", new ImageIcon("images/cancel.png"));
        cancel.setToolTipText("取消添加操作");
        cancel.setPreferredSize(buttonSize);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HideBottomPane();// 隐藏"添加用户"面板
            }
        });
        bottomPane.add(cancel);

    }

    // 清空"添加用户"面板
    private void clearBottomPane() {
        jtf.setText(null);
        jcb.setSelectedItem(null);
    }

    // 隐藏"添加用户"面板（移除组件，设置边框为null）
    private void HideBottomPane() {
        this.clearBottomPane();
        bottomPane.setVisible(false);
    }

    /**
     * 获取选中用户
     */
    private UserBean getSelectUser() {
        UserBean user = new UserBean();
        if (tempLabel == null) {
            JOptionPane.showMessageDialog(null, "请选择操作的用户", "提示",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        String UserName = tempLabel.getText();
        user.setUsername(UserName);
        if (UserName.equals("admin")) {
            JOptionPane.showMessageDialog(null,
                    "'admin'为系统默认管理员用户，只能修改密码，不能删除或修改其用户类型", "提示",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (tempLabel.getIcon().equals(icon1)) {
            user.setAuthority(UserService.UP);
        } else {
            user.setAuthority(UserService.AP);
        }

        return user;
    }

    private Dimension labelSize = new Dimension(100, 100);

    /**
     * 加载、刷新用户列表
     */
    private void refreshUsers(List<UserBean> users) {
        tempLabel = null;
        if (center.getComponentCount() > 0) {
            center.removeAll();
        }

        for (UserBean user : users) {
            JLabel temp;
            if (user.getAuthority() == UserService.AP) {
                temp = new JLabel(user.getUsername(), icon1, SwingConstants.CENTER);
                temp.setToolTipText(UserService.ADMIN);
            } else if (user.getAuthority() == UserService.UP) {
                temp = new JLabel(user.getUsername(), icon2, SwingConstants.CENTER);
                temp.setToolTipText(UserService.USER);
            } else {
                continue;
            }
            temp.setPreferredSize(labelSize);
            temp.setVerticalTextPosition(SwingConstants.BOTTOM);
            temp.setHorizontalTextPosition(SwingConstants.CENTER);
            temp.setBackground(new Color(255, 220, 80));
            temp.setFocusable(true);
            temp.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    arg0.getComponent().requestFocus();
                }
            });
            temp.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (tempLabel != null) {
                        tempLabel.setOpaque(false);
                        tempLabel.setBorder(null);
                        tempLabel.repaint();
                        tempLabel = null;
                    }
                    tempLabel = (JLabel) e.getComponent();
                    tempLabel.setOpaque(true);
                    tempLabel.setBorder(BorderFactory.createLineBorder(
                            new Color(170, 210, 255), 1));
                    tempLabel.repaint();
                }
            });
            center.add(temp);
        }// for end
        // 用完清空allUsers
        users.clear();
        center.repaint();
        center.revalidate();
    }

    public void loadUsers() {
        refreshUser.doClick();// 刷新用户列表
    }

    private boolean isNull(String s) {
        return s == null || s.equals("");
    }
}
