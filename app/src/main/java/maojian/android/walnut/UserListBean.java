package maojian.android.walnut;

import java.io.Serializable;
import java.util.List;

/**
 * @author hezuzhi
 * @Description: ()
 * @date 2017/3/23  14:50.
 * @version: 1.0
 */
public class UserListBean implements Serializable {
    private List<UserBean> data;

    public List<UserBean> getData() {
        return data;
    }

    public void setData(List<UserBean> data) {
        this.data = data;
    }

    public class UserBean implements Serializable {
        private String class_id;//": 398,
        private String class_name;//": 1,
        private String class_icon;//": "https://www.baidu.com",
        private int is_follow;//":1

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        public String getClass_icon() {
            return class_icon;
        }

        public void setClass_icon(String class_icon) {
            this.class_icon = class_icon;
        }

        public int getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(int is_follow) {
            this.is_follow = is_follow;
        }
    }
}
