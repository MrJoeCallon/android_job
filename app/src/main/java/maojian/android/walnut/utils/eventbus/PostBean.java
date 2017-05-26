package maojian.android.walnut.utils.eventbus;

import java.io.Serializable;

/**
 * Created by joe on 2017/4/15.
 */
public class PostBean implements Serializable {
    private boolean isVideo;
    private String location;
    public PostBean(boolean isVideo, String newPath, String location, String postCon) {
        this.isVideo = isVideo;
        this.newPath = newPath;
        this.location = location;
        this.postCon = postCon;
    }
    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostCon() {
        return postCon;
    }

    public void setPostCon(String postCon) {
        this.postCon = postCon;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    private String postCon;
    private String newPath;
}
