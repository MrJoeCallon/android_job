package maojian.android.walnut.message;


import maojian.android.walnut.discover.DiscoverIndexBean;

/**
 * @author hezuzhi
 * @Description: (登录信息回调)
 * @date 2016/5/4  10:19.
 * @version: 1.0
 */
public interface MessageView {

    void setSuccess(MessageListBean messageListBean);


    void setFail(String errInfo);
}
