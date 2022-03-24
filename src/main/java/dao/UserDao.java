package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        if (rs.next()) {
            resultUser = new User();
            resultUser.setId(rs.getInt("id"));
            resultUser.setUserName(rs.getString("userName"));
            resultUser.setUserPassWord(rs.getString("password"));
        }
        return resultUser;
    }
}
