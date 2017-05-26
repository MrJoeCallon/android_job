package maojian.android.walnut.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joe on 2017/3/14.
 */
public class IndexBean implements Serializable {
    private int total_page;//总页数
    private int new_page;//当前页数
    private String advUrl;

    public String getAdvUrl() {
        return advUrl;
    }

    public void setAdvUrl(String advUrl) {
        this.advUrl = advUrl;
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
    public void setFristData(String advUrl) {
        List<IndexListBean> indexListBeen = new ArrayList<>();
        IndexListBean indexListBean = new IndexListBean();
        indexListBean.setCover_icon(advUrl);
        indexListBeen.add(indexListBean);
        this.data = indexListBeen;
    }

    private List<IndexListBean> data;

    public class IndexListBean implements Serializable {


        public boolean is_praise() {
            return is_praise;
        }

        private String post_id;//帖子ID
        private String class_name;//	用户名字
        private String class_icon;//用户头像链接
        private String class_address;//	地址
        private String time;//发布时间
        private String cover_icon;//	帖子发布图片
        private String class_id;//	用户id
        private int praise_num;//	点赞数
        private int comments_num;//	评论数
        private String content;//	帖子内容
        private boolean is_praise;//	是否赞过 true赞过
        private int post_type;//	帖子类型 1 图片 2视频 3系统帖子
        private String video_url;//	视频链接，没有返回空


        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
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

        public String getClass_address() {
            return class_address;
        }

        public void setClass_address(String class_address) {
            this.class_address = class_address;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCover_icon() {
            return cover_icon;
        }

        public void setCover_icon(String cover_icon) {
            this.cover_icon = cover_icon;
        }

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        public int getPraise_num() {
            return praise_num;
        }

        public void setPraise_num(int praise_num) {
            this.praise_num = praise_num;
        }

        public int getComments_num() {
            return comments_num;
        }

        public void setComments_num(int comments_num) {
            this.comments_num = comments_num;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean getIs_praise() {
            return is_praise;
        }

        public void setIs_praise(boolean is_praise) {
            this.is_praise = is_praise;
        }

        public int getPost_type() {
            return post_type;
        }

        public void setPost_type(int post_type) {
            this.post_type = post_type;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }


        public int getNew_page() {
            return new_page;
        }


    }

}
