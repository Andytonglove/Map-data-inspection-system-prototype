package model;

/**
 * 用户实体
 *
 */
public class User {

    private String userName;
    private String userPassWord;
    private int id;

    public User() {
        super();
    }

    public User(String userName, String userPassWord) {
        super();
        this.userName = userName;
        this.userPassWord = userPassWord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
