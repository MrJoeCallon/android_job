package maojian.android.walnut.home.comment;

import java.io.Serializable;
import java.util.List;

/**
 * @author hezuzhi
 * @Description: ()
 * @date 2017/3/16  10:05.
 * @version: 1.0
 */
public class CommentList implements Serializable {
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

    public List<CommentBean> getData() {
        return data;
    }

    public void setData(List<CommentBean> data) {
        this.data = data;
    }

    private List<CommentBean> data;

    public class CommentBean implements Serializable {
        private String class_id;//":398,

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

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getComments_content() {
            return comments_content;
        }

        public void setComments_content(String comments_content) {
            this.comments_content = comments_content;
        }

        private String class_name;//":1,
        private String class_icon;//":"https://www.baidu.com",
        private String post_id;//":"帖子ID",
        private String time;//":"2017-02-06 14:48:21",
        private String comments_content;//":"个性签名",
    }
}
