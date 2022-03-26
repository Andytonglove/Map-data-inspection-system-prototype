package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import model.User;

/**
 * 用户Dao类
 * 
 * @author guan
 *
 */
public class UserDao {
    /**
     * 登录验证
     * 
     * @param connection
     * @param user
     * @return
     * @throws Exception
     */
    public User login(Connection connection, User user) throws Exception {
        User resultUser = null;
        String sql = "select * from user where userName=? and password=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, user.getUserName());
        pstmt.setString(2, user.getUserPassWord());
        ResultSet rs = pstmt.executeQuery();

        // TODO 这里直接获取密码不太好，后续可以考虑MD5加密
        if (rs.next()) {
            resultUser = new User();
            resultUser.setId(rs.getInt("id"));
            resultUser.setUserName(rs.getString("userName"));
            resultUser.setUserPassWord(rs.getString("password"));
        }
        return resultUser;
    }

    /**
     * 注册账号
     * 
     * @param connection
     * @param user
     * @return
     * @throws Exception
     */
    public int register(Connection connection, User user) throws Exception {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";

        String sqlselect = "select * from user where userName=?"; // 看看是否有这个用户名重复
        PreparedStatement pstmt_select = connection.prepareStatement(sqlselect);
        pstmt_select.setString(1, user.getUserName());
        ResultSet rs = pstmt_select.executeQuery();
        if (rs.next()) {
            return -1;
        }

        PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, user.getUserName());
        pstmt.setString(2, user.getUserPassWord());
        int cnt = pstmt.executeUpdate();

        return cnt;
    }

    /**
     * 地图错误上报入库
     * 
     * @param connection
     * @param user
     * @param errorString
     * @throws Exception
     */
    public void upload(Connection connection, User user, String errorString) throws Exception {

    }
}
