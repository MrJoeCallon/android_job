package maojian.android.walnut.utils.eventbus;

/**
 * @author hezuzhi
 * @Description: (EventBus事件，收到后关闭)
 * @date 2016/5/6  11:14.
 * @version: 1.0
 */
public class PostSuccessEvent {
    private PostBean postBean;

    public PostBean getPostBean() {
        return postBean;
    }

    public void setPostBean(PostBean postBean) {
        this.postBean = postBean;
    }

    public PostSuccessEvent(PostBean postBean) {
        this.postBean = postBean;

    }
}
