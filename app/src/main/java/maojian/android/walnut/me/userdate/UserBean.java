package maojian.android.walnut.me.userdate;

import maojian.android.walnut.UserListBean;
import maojian.android.walnut.home.IndexBean;
import maojian.android.walnut.home.comment.CommentList;
import maojian.android.walnut.login.LoginBean;
import maojian.android.walnut.message.MessageListBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author hezuzhi
 * @Description: ()
 * @date 2017/3/16  10:05.
 * @version: 1.0
 */
public class UserBean implements Serializable {

    private Userinfo userinfo;

    public Userinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Userinfo userinfo) {
        this.userinfo = userinfo;
    }

    private List<Info> info;

    private List<Lats> lats;

    public List<Lats> getLats() {
        return lats;
    }

    public void setLats(List<Lats> lats) {
        this.lats = lats;
    }

    public List<Info> getInfo() {
        return info;
    }

    public void setInfo(List<Info> info) {
        this.info = info;
    }

    public class Lats implements Serializable {
        private String lng;//":"2017-03-25",
        private String lat;//":"2017-03-25",

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }
    }
    public class Info implements Serializable {
        private String time;//":"2017-03-25",

        //        private int usage;
        private float today_usage;

        public float getToday_usage() {
            return today_usage;
        }

        public void setToday_usage(float today_usage) {
            this.today_usage = today_usage;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

//        public int getUsage() {
//            return usage;
//        }
//
//        public void setUsage(int usage) {
//            this.usage = usage;
//        }
    }

    public static class Userinfo implements Serializable {
        private String class_id;//": 398,
        private String class_name;//": 1,
        private String class_icon;//": "https://www.baidu.com",
        private String fans_num = "0";//":10,
        private String following_num = "0";//":20,
        private int is_push;//":1#是否发推送0：不发
        private int is_follllow;//":1

        private String gender;//" :“男 新增字段”，
        private String spectra_name;//":"ddd新增字段"
        private String info;//”:"杀死 新增字段"
        private int height;//”:"180 新增字段"
        private int weight;//”:"180 新增字段"
        private String email;//”:"新增字段 222@qq.com"
        private int today_usage;//:15,

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getSpectra_name() {
            return spectra_name;
        }

        public void setSpectra_name(String spectra_name) {
            this.spectra_name = spectra_name;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getToday_usage() {
            return today_usage;
        }

        public void setToday_usage(int today_usage) {
            this.today_usage = today_usage;
        }

        public int getAll_usage() {
            return all_usage;
        }

        public void setAll_usage(int all_usage) {
            this.all_usage = all_usage;
        }

        private int all_usage;//:200

        public int getIs_push() {
            return is_push;
        }

        public void setIs_push(int is_push) {
            this.is_push = is_push;
        }

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getClass_icon() {
            return class_icon;
        }

        public void setClass_icon(String class_icon) {
            this.class_icon = class_icon;
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

        public int getIs_follllow() {
            return is_follllow;
        }

        public void setIs_follllow(int is_follllow) {
            this.is_follllow = is_follllow;
        }


    }
    public static UserBean indexListBeanTo(IndexBean.IndexListBean indexListBean) {
        UserBean userBean = new UserBean();
        UserBean.Userinfo userinfo = new  UserBean.Userinfo();
        userinfo.setClass_icon(indexListBean.getClass_icon());
        userinfo.setClass_id(indexListBean.getClass_id());
        userinfo.setClass_name(indexListBean.getClass_name());
        userBean.setUserinfo(userinfo);
        return userBean;
    }
    public static UserBean messageBeanTo(MessageListBean.MessageBean messageBean) {
        UserBean userBean = new UserBean();
        UserBean.Userinfo userinfo = new  UserBean.Userinfo();
        userinfo.setClass_icon(messageBean.getClass_icon());
        userinfo.setClass_id(messageBean.getClass_id());
        userinfo.setClass_name(messageBean.getClass_name());
        userBean.setUserinfo(userinfo);
        return userBean;
    }


    public static UserBean CommentBeanTo(CommentList.CommentBean post) {
        UserBean userBean = new UserBean();
        UserBean.Userinfo userinfo = new  UserBean.Userinfo();
        userinfo.setClass_icon(post.getClass_icon());
        userinfo.setClass_id(post.getClass_id());
        userinfo.setClass_name(post.getClass_name());
        userBean.setUserinfo(userinfo);
        return userBean;
    }

    public static UserBean UserBeanTo(UserListBean.UserBean post) {
        UserBean userBean = new UserBean();
        UserBean.Userinfo userinfo = new  UserBean.Userinfo();
        userinfo.setClass_icon(post.getClass_icon());
        userinfo.setClass_id(post.getClass_id());
        userinfo.setClass_name(post.getClass_name());
        userinfo.setIs_follllow(post.getIs_follow());
        userBean.setUserinfo(userinfo);

        return userBean;
    }

    public static UserBean detailObjectTo(IndexBean.IndexListBean detailObject) {
        UserBean userBean = new UserBean();
        UserBean.Userinfo userinfo = new  UserBean.Userinfo();
        userinfo.setClass_icon(detailObject.getClass_icon());
        userinfo.setClass_id(detailObject.getClass_id());
        userinfo.setClass_name(detailObject.getClass_name());
        userBean.setUserinfo(userinfo);
        return userBean;
    }
}
