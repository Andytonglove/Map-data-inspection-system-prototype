package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
     * @param his
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

    /**
     * 地图错误数据库数据查询
     * 
     * @param connection
     * @param his
     * @return
     * @throws Exception
     */
    public void searchFromName(Connection connection, History his) throws Exception {
        // 数据库数据查询
        String sqlselect = "select * from user where userName=?";
        PreparedStatement pstmt = connection.prepareStatement(sqlselect);
        pstmt.setString(1, his.getUserName());
        ResultSet rs = pstmt.executeQuery();

        String sqlcnt = "SELECT COUNT(*) FROM user where userName=?"; // 统计数量
        PreparedStatement pstmt_cnt = connection.prepareStatement(sqlcnt);
        pstmt_cnt.setString(1, his.getUserName());
        ResultSet rs_cnt = pstmt.executeQuery();
        // TODO
        int cnt = 10;
        History resultHis[] = new History[cnt];

        for (int i = 0; rs.next(); i++) {
            resultHis[i] = new History();
            resultHis[i].setId(rs.getInt("id"));
            resultHis[i].setUserName(rs.getString("userName"));
            resultHis[i].setmapname(rs.getString("mapname"));
            resultHis[i].setposition(rs.getString("position"));
            resultHis[i].settype(rs.getString("type"));
            resultHis[i].setdiscription(rs.getString("discription"));
        }
    }
}
