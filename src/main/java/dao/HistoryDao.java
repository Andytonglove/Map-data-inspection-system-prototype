package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
     * @return ArrayList<History>
     * @throws Exception
     */
    public ArrayList<History> searchFromName(Connection connection, History his) throws Exception {
        // 数据库数据查询
        String sqlselect = "select * from user where userName=?";
        PreparedStatement pstmt = connection.prepareStatement(sqlselect);
        pstmt.setString(1, his.getUserName());
        ResultSet rs = pstmt.executeQuery();

        String sqlcnt = "SELECT COUNT(*) AS count1 FROM user where userName=?"; // 统计数量
        PreparedStatement pstmt_cnt = connection.prepareStatement(sqlcnt);
        pstmt_cnt.setString(1, his.getUserName());
        ResultSet rs_cnt = pstmt.executeQuery();
        int cntString = 0;
        while (rs.next()) {
            cntString = rs_cnt.getInt("count1");
        }
        // 接下来循环存入查询结果
        ArrayList<History> resultList = new ArrayList<History>();
        History resultHis[] = new History[cntString];
        for (int i = 0; i < cntString; i++) {
            resultHis[i] = new History();
            resultHis[i].setId(rs.getInt("id"));
            resultHis[i].setUserName(rs.getString("userName"));
            resultHis[i].setmapname(rs.getString("mapname"));
            resultHis[i].setposition(rs.getString("position"));
            resultHis[i].settype(rs.getString("type"));
            resultHis[i].setdiscription(rs.getString("discription"));

            resultList.add(resultHis[i]); // 存入array
        }
        return resultList;
    }
}
