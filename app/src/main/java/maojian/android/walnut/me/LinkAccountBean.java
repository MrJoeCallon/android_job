package maojian.android.walnut.me;

import java.io.Serializable;

/**
 * Created by joe on 2017/3/29.
 */
public class LinkAccountBean implements Serializable {
    private int facebook;//":1;
    private int twitter;//":1;
    private int weibo;//":0;

    public int getFacebook() {
        return facebook;
    }

    public void setFacebook(int facebook) {
        this.facebook = facebook;
    }

    public int getTwitter() {
        return twitter;
    }

    public void setTwitter(int twitter) {
        this.twitter = twitter;
    }

    public int getWeibo() {
        return weibo;
    }

    public void setWeibo(int weibo) {
        this.weibo = weibo;
    }

    public int getWechat() {
        return wechat;
    }

    public void setWechat(int wechat) {
        this.wechat = wechat;
    }

    private int wechat;//":1
}
