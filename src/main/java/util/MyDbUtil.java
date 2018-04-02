package util;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import com.Configure;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class MyDbUtil {
    // --数据源,整个程序中都只有这一个数据源
    private static ComboPooledDataSource dataSource;

    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("propertyInfo\\database.properties"));
            dataSource = new ComboPooledDataSource();
            // String driver = PropUtil.getDBdriver();
            String driver = "com.mysql.jdbc.Driver";
            dataSource.setDriverClass(driver);
            String dbUrl = Configure.getSQLurl();
            if (isNull(dbUrl)) {
                dbUrl = properties.getProperty("dbUrl");
            }
            dataSource.setJdbcUrl(dbUrl);
            String user = properties.getProperty("username"), password = properties.getProperty("password");
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setInitialPoolSize(10);
            dataSource.setMinPoolSize(2);
            dataSource.setMaxPoolSize(50);
            dataSource.setMaxStatements(50);
            dataSource.setMaxIdleTime(10);
            dataSource.setAcquireIncrement(5);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    /**
     * javaBean的数据查询（多条数据）
     *
     * @param sql    查询语句
     * @param clazz  查询结果对象的类
     * @param params 查询条件
     * @return 查询结果集合
     * @throws SQLException
     */
    public static <T> List<T> queryBeanListData(String sql, Class<T> clazz,
                                                Object... params) throws SQLException {
        QueryRunner runner = new QueryRunner(dataSource);
        return runner.query(sql, new BeanListHandler<T>(clazz), params);
    }

    /**
     * javaBean单条记录查询
     *
     * @param sql    查询语句
     * @param clazz  查询结果对象的类
     * @param params 查询条件
     * @return 返回查询结果
     * @throws SQLException
     */
    public static <T> T queryBeanData(String sql, Class<T> clazz,
                                      Object... params) throws SQLException {
        QueryRunner runner = new QueryRunner(dataSource);
        return runner.query(sql, new BeanHandler<T>(clazz), params);
    }


    public static List<Vector<Object>> queryTableData(String sql, Object... params) throws SQLException {
        QueryRunner runner = new QueryRunner(dataSource);
        return runner.query(sql, new TableListHandler(), params);
    }


    /**
     * 增删改查
     *
     * @param sql    sql操作语句
     * @param params 操作条件
     * @return 操作成功数据条数
     * @throws SQLException
     */
    public static int update(String sql, Object... params) throws SQLException {
        QueryRunner runner = new QueryRunner(dataSource);
        return runner.update(sql, params);

    }


    /**
     * 批量操作数据
     *
     * @param sql   sql操作语句
     * @param datas 数据集合
     */
    public static int batchData(String sql, Vector<Vector<Object>> datas) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        int count = datas.size();
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            for (Vector<Object> data : datas) {
                try {
                    for (int i = 0; i < data.size(); i++) {
                        ps.setObject(i + 1, data.get(i));
                    }
                    ps.executeUpdate();
                } catch (SQLException e) {
                    count--;
                    // e.printStackTrace();
                    continue;
                }
            }
            return count;
        } finally {
            DbUtils.closeQuietly(conn, ps, null);
        }
    }


    public static boolean isNull(String s) {
        return s == null || s.equals("");
    }
}
