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
        String sql = "INSERT INTO history (username, mapname, position, type, discription) VALUES (?, ?, ?, ?, ?)";

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
     * 地图错误数据库数据查询：通过用户名查询
     * 
     * @param connection
     * @param his
     * @return ArrayList<History>
     * @throws Exception
     */
    public ArrayList<History> searchFromName(Connection connection, History his) throws Exception {
        // 数据库数据查询
        String sqlcnt = "SELECT COUNT(*) AS 'count1' FROM history where username=?"; // 统计数量
        PreparedStatement pstmt_cnt = connection.prepareStatement(sqlcnt);
        pstmt_cnt.setString(1, his.getUserName());
        ResultSet rs_cnt = pstmt_cnt.executeQuery();
        int cntString = 0;
        if (rs_cnt.next()) {
            cntString = rs_cnt.getInt("count1");
        }

        String sqlselect = "select * from history where username=?";
        PreparedStatement pstmt = connection.prepareStatement(sqlselect);
        pstmt.setString(1, his.getUserName());
        ResultSet rs = pstmt.executeQuery();
        // 接下来循环存入查询结果
        ArrayList<History> resultList = new ArrayList<History>();
        History resultHis[] = new History[cntString];
        int i = 0;
        while (rs.next()) {
            // 注意这里不能用for或者if循环而是用while，next指针向后遍历！
            resultHis[i] = new History();
            resultHis[i].setId(rs.getInt("id"));
            resultHis[i].setUserName(rs.getString("userName"));
            resultHis[i].setmapname(rs.getString("mapname"));
            resultHis[i].setposition(rs.getString("position"));
            resultHis[i].settype(rs.getString("type"));
            resultHis[i].setdiscription(rs.getString("discription"));

            resultList.add(resultHis[i]); // 存入array
            i++;
        }
        return resultList;
    }

    /**
     * 地图错误数据库数据查询：关键词模糊查询
     * 
     * @param connection
     * @param keyword
     * @return ArrayList<History>
     * @throws Exception
     */
    public ArrayList<History> searchByKeyword(Connection connection, String keyword) throws Exception {
        // 数据库模糊数据查询：%str%

        String sqlselect = "select * from history where (mapname like ?) UNION " +
                "select * from history where (position like ?) UNION " +
                "select * from history where (type like ?) UNION " +
                "select * from history where (discription like ?)";
        PreparedStatement pstmt = connection.prepareStatement(sqlselect, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE); // 这里允许游标移动，多两个参数，至关重要！！！！
        pstmt.setString(1, "%" + keyword + "%");
        pstmt.setString(2, "%" + keyword + "%");
        pstmt.setString(3, "%" + keyword + "%");
        pstmt.setString(4, "%" + keyword + "%");
        ResultSet rs = pstmt.executeQuery();
        rs.last(); // 将光标移动到最后一行再统计行数
        int cntString = rs.getRow(); // 获取总行数，即得到的结果统计数量，省去再有一个统计的sql语句
        // 接下来循环存入查询结果
        ArrayList<History> resultList = new ArrayList<History>();
        History resultHis[] = new History[cntString];
        int i = 0;
        rs.beforeFirst(); // 将游标再移动回开始
        while (rs.next()) {
            resultHis[i] = new History();
            resultHis[i].setId(rs.getInt("id"));
            resultHis[i].setUserName(rs.getString("userName"));
            resultHis[i].setmapname(rs.getString("mapname"));
            resultHis[i].setposition(rs.getString("position"));
            resultHis[i].settype(rs.getString("type"));
            resultHis[i].setdiscription(rs.getString("discription"));

            resultList.add(resultHis[i]); // 存入array
            i++;
        }
        return resultList;
    }
}
