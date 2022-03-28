package model;

/**
 * 操作历史记录实体
 * 
 */

public class History {

    private String userName;
    private String mapname;
    private String position;
    private String type;
    private String discription;
    private int id;

    public History() {
        super();
    }

    public History(String userName, String mapname, String position, String type, String discription, int id) {
        super();
        this.userName = userName;
        this.mapname = mapname;
        this.position = position;
        this.type = type;
        this.discription = discription;
        this.id = id;
    }

    public History(String userName, String mapname, String position, String type, String discription) {
        super();
        this.userName = userName;
        this.mapname = mapname;
        this.position = position;
        this.type = type;
        this.discription = discription;
    }

    // 字符串与对象的互相转换方法，稍显多余
    public String historyToString() {
        String stringInfo = String.join(this.userName, "&", this.mapname, "&", this.position, "&", this.type, "&",
                this.discription, "&", Integer.toString(this.id));
        return stringInfo;
    }

    // 用户名
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // 地图名
    public String getmapname() {
        return mapname;
    }

    public void setmapname(String mapname) {
        this.mapname = mapname;
    }

    // 错误位置
    public String getposition() {
        return position;
    }

    public void setposition(String position) {
        this.position = position;
    }

    // 错误类型
    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    // 错误描述
    public String getdiscription() {
        return discription;
    }

    public void setdiscription(String discription) {
        this.discription = discription;
    }

    // 操作序号
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
