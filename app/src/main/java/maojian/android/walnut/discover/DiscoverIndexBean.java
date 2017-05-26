package maojian.android.walnut.discover;

import java.io.Serializable;
import java.util.List;

/**
 * Created by joe on 2017/3/14.
 */
public class DiscoverIndexBean implements Serializable {
    private int total_page;//总页数
    private int new_page;//当前页数
    private List<BannerBean> banner;

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

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

    public List<IndexListBean> getData() {
        return data;
    }

    public void setData(List<IndexListBean> data) {
        this.data = data;
    }

    private List<IndexListBean> data;
    public class BannerBean implements Serializable {
        private String banner_icon;
        private String post_id;
        private String title;
        private String type;
        private String rurl;

        public String getRurl() {
            return rurl;
        }

        public void setRurl(String rurl) {
            this.rurl = rurl;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        private String num;
        public String getBanner_icon() {
            return banner_icon;
        }

        public void setBanner_icon(String banner_icon) {
            this.banner_icon = banner_icon;
        }

        public String getBanner_address() {
            return banner_address;
        }

        public void setBanner_address(String banner_address) {
            this.banner_address = banner_address;
        }

        private String banner_address;
    }
    public class IndexListBean implements Serializable {
        private String rurl;//帖子ID

        public String getRurl() {
            return rurl;
        }

        public void setRurl(String rurl) {
            this.rurl = rurl;
        }

        private String post_id;//帖子ID
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

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

        public String getCover_icon() {
            return cover_icon;
        }

        public void setCover_icon(String cover_icon) {
            this.cover_icon = cover_icon;
        }

        private String cover_icon;//	帖子发布图片



    }

}
