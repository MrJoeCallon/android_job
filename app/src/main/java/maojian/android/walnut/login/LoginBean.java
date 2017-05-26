package maojian.android.walnut.login;

import java.io.Serializable;

/**
 * @author hezuzhi
 * @Description: (登录信息Bean)
 * @date 2016/5/4  10:19.
 * @version: 1.0
 */
public class LoginBean implements Serializable {
    /**
     * 用户名
     */
    private String user_name;
    private String user_email;
    private String user_id;
    private String logoinType;
    private String openid;
    private int is_follllow;

    public int getIs_follllow() {
        return is_follllow;
    }

    public void setIs_follllow(int is_follllow) {
        this.is_follllow = is_follllow;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getLogoinType() {
        return logoinType;
    }

    public void setLogoinType(String logoinType) {
        this.logoinType = logoinType;
    }

    private boolean is_first;

    public boolean is_first() {
        return is_first;
    }

    public void setIs_first(boolean is_first) {
        this.is_first = is_first;
    }

    public String getHeader_image() {
        return header_image;
    }

    public void setHeader_image(String header_image) {
        this.header_image = header_image;
    }

    private String header_image;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFans_num() {
        return fans_num;
    }

    public void setFans_num(String fans_num) {
        this.fans_num = fans_num;
    }

    public String getFollowing_num() {
        return following_num;
    }

    public void setFollowing_num(String following_num) {
        this.following_num = following_num;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    private String fans_num;
    private String following_num;
    private String objectID;

}
