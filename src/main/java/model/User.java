package model;

/**
 * 用户实体
 * 
 * @author guan
 *
 */
public class User {
    private String userName;
    private String userPassWord;
    private String id;

    public User() {
        super();
    }

    public User(String userName, String userPassWord) {
        super();
        this.userName = userName;
        this.userPassWord = userPassWord;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassWord() {
        return userPassWord;
    }

    public void setUserPassWord(String userPassWord) {
        this.userPassWord = userPassWord;
    }

}
