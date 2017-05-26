package maojian.android.walnut.me;

import java.io.Serializable;
import java.util.List;

/**
 * @author hezuzhi
 * @Description: ()
 * @date 2017/3/16  10:05.
 * @version: 1.0
 */
public class MyPostList implements Serializable {


    public List<MyPostBean> getData() {
        return data;
    }

    public void setData(List<MyPostBean> data) {
        this.data = data;
    }

    private List<MyPostBean> data;

    public class MyPostBean implements Serializable {
        private String post_id;//":398,
        private String cover_icon;//":398,
        private int type;
        private String lng;

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

        private String lat;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getRurl() {
            return rurl;
        }

        public void setRurl(String rurl) {
            this.rurl = rurl;
        }

        private String rurl;


        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getCover_icon() {
            return cover_icon;
        }

        public void setCover_icon(String cover_icon) {
            this.cover_icon = cover_icon;
        }
    }
}
