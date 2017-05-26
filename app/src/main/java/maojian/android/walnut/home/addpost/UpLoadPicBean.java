package maojian.android.walnut.home.addpost;

import java.io.Serializable;

/**
 * @author hezuzhi
 * @Description: ()
 * @date 2017/3/16  11:21.
 * @version: 1.0
 */
public class UpLoadPicBean implements Serializable {
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    private String image_url;

    private String rurl;
    private String litpic;

    public String getRurl() {
        return rurl;
    }

    public void setRurl(String rurl) {
        this.rurl = rurl;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }
}
