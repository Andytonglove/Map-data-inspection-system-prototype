package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import model.History;

/**
 * 历史操作记录Dao类
 * 
 * @author guan
 *
 */
public class HistoryDao {
    /**
     * 地图错误上报入库
     * 
     * @param connection
     * @param history
     * @return
     * @throws Exception
     */
    public int upload(Connection connection, History his) throws Exception {
        // 上传数据
        String sql = "INSERT INTO user (username, mapname, position, type, discription) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, his.getUserName());
        pstmt.setString(2, his.getmapname());
        pstmt.setString(3, his.getposition());
        pstmt.setString(4, his.gettype());
        pstmt.setString(5, his.getdiscription());
        int cnt = pstmt.executeUpdate();

        return cnt;
    }
}
