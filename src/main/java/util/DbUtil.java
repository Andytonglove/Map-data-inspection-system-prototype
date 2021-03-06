package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据库工具类
 * 连接MySQL数据库
 * 
 * @author guan
 */
public class DbUtil {

    private String dbUrl = "jdbc:mysql://localhost:3306/map-data-inspection?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8"; // 数据库连接地址
    private String dbUserName = "root"; // 数据库用户名
    private String dbPassWord = "root"; // 密码
    private String jdbcName = "com.mysql.cj.jdbc.Driver"; // 驱动名称

    // 获取数据库连接
    public Connection getConnection() throws Exception {
        Class.forName(jdbcName);
        Connection connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
        return connection;
    }

    // 关闭数据库连接
    public void closeConnection(Connection connection) throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        DbUtil dbUtil = new DbUtil();
        try {
            dbUtil.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
