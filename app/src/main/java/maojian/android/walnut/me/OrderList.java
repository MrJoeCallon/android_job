package maojian.android.walnut.me;

import java.io.Serializable;
import java.util.List;

/**
 * @author hezuzhi
 * @Description: ()
 * @date 2017/4/13  10:48.
 * @version: 1.0
 */
public class OrderList implements Serializable {


    private List<OrderBean> myorder;

    public List<OrderBean> getMyorder() {
        return myorder;
    }

    public void setMyorder(List<OrderBean> myorder) {
        this.myorder = myorder;
    }

    public class OrderBean implements Serializable {
        private String post_id;//":398,
        private String order_id;//" : "33333";
        private String order_name;//" : "name";

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOrder_name() {
            return order_name;
        }

        public void setOrder_name(String order_name) {
            this.order_name = order_name;
        }

        public String getOrder_time() {
            return order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        public String getOrder_image() {
            return order_image;
        }

        public void setOrder_image(String order_image) {
            this.order_image = order_image;
        }

        public String getOrder_details_url() {
            return order_details_url;
        }

        public void setOrder_details_url(String order_details_url) {
            this.order_details_url = order_details_url;
        }

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        private String order_time;//" : "2016-11-11";
        private String order_image;//" : "https://www.baidu.co"
        private String order_details_url;//" : "https://www.baidu";
        private String order_type;//
    }
}
