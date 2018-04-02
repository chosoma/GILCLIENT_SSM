package service;

import java.sql.SQLException;
import java.util.List;

import util.MyDbUtil;
import domain.DataBaseAttr;
import domain.UserBean;

public class UserService {

    public static String ADMIN = "管理员";
    public static String USER = "普通用户";
    public static int AP = 1, UP = 2;
    private static UserBean UserLogin;

    private static UserService MODEL;

    private UserService() {

    }

    public static UserService getInstance() {
        if (MODEL == null) {
            synchronized (UserService.class) {
                if (MODEL == null) {
                    MODEL = new UserService();
                }
            }
        }
        return MODEL;
    }

    /**
     * 登录校验
     *
     */
    public static UserBean checkUser(String[] paras) throws SQLException {
        String sql = "select * from " + DataBaseAttr.UserTable
                + " where username = ? and password = ? ";
        UserLogin = MyDbUtil.queryBeanData(sql, UserBean.class, paras[0],
                paras[1]);
        return UserLogin;
    }


    /**
     * 校验用户
     */
    public static UserBean checkUser(String paras) throws SQLException {
        String sql = "select * from " + DataBaseAttr.UserTable
                + " where username = ? ";
        return  MyDbUtil.queryBeanData(sql, UserBean.class, paras);
    }
    /**
     * 检验密码
     */
    public static boolean checkPassWord(String p) {
        return UserLogin.getPassword().equals(p);
    }

    /**
     * 修改密码
     */
    public static void changePassword(String password) throws SQLException {
        String sql = "update " + DataBaseAttr.UserTable
                + "  set password=? where username=?";
        MyDbUtil.update(sql, password, UserLogin.getUsername());
        UserLogin.setPassword(password);
    }

    /**
     * 获取全部用户
     */
    public static List<UserBean> query() throws SQLException {
        String sql = "select username , authority from " + DataBaseAttr.UserTable
                + " where authority >= ? order by authority , username";
        return MyDbUtil.queryBeanListData(sql, UserBean.class, 1);
    }


    /**
     * 更改用户权限
     *
     */
    public static void changeRole(UserBean user) throws SQLException {
        String sql = "update " + DataBaseAttr.UserTable
                + "  set authority = ? where username = ? ";
        MyDbUtil.update(sql, user.getAuthority(), user.getUsername());
    }

    /**
     * 删除用户
     *
     */
    public static void deleteUser(UserBean user) throws SQLException {
        String sql = "delete from " + DataBaseAttr.UserTable + "  where username = ? ";
        MyDbUtil.update(sql, user.getUsername());
    }

    /**
     * 添加用户
     *
     */
    public static void addUser(UserBean user) throws SQLException {
        String sql = "insert into " + DataBaseAttr.UserTable
                + " (username,password,authority) values (?,123456,?)";
        MyDbUtil.update(sql, user.getUsername(), user.getAuthority());
    }

    /**
     * 判断当前登录用户是否为管理员
     *
     */
    public boolean isAdmin() {
        return UserLogin.getAuthority() <= 1;
    }

}
