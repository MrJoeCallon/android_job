package maojian.android.walnut.message;

import java.io.Serializable;
import java.util.List;

/**
 * Created by joe on 2017/3/14.
 */
public class MessageListBean implements Serializable {
    private int total_page;//总页数
    private int new_page;//当前页数

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public int getNew_page() {
        return new_page;
    }

    public void setNew_page(int new_page) {
        this.new_page = new_page;
    }

    public List<MessageBean> getData() {
        return data;
    }

    public void setData(List<MessageBean> data) {
        this.data = data;
    }

    private List<MessageBean> data;
    public class MessageBean implements Serializable {
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        private String class_id;//": 398,
        private String class_name;//": 1,
        private String class_icon;//": "https://www.baidu.com",
        private String time;//": "2017-02-06 14:48:21",
        private String inbox_icon;//": "https://www.baidu.com",
        private String inbox_id;//": 1,
        private String litpic;

        public String getLitpic() {
            return litpic;
        }

        public void setLitpic(String litpic) {
            this.litpic = litpic;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getInbox_icon() {
            return inbox_icon;
        }

        public void setInbox_icon(String inbox_icon) {
            this.inbox_icon = inbox_icon;
        }

        public String getInbox_id() {
            return inbox_id;
        }

        public void setInbox_id(String inbox_id) {
            this.inbox_id = inbox_id;
        }

        public int getPost_type() {
            return post_type;
        }

        public void setPost_type(int post_type) {
            this.post_type = post_type;
        }

        private int post_type;//": 1,

        private String post_id;//帖子ID


        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        private String num;

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }




    }

}
