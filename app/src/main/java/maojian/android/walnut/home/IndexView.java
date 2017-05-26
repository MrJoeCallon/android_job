package maojian.android.walnut.home;


import maojian.android.walnut.home.addpost.UpLoadPicBean;
import maojian.android.walnut.login.LoginBean;
import maojian.android.walnut.utils.eventbus.PostSuccessEvent;

/**
 * @author hezuzhi
 * @Description: (登录信息回调)
 * @date 2016/5/4  10:19.
 * @version: 1.0
 */
public interface IndexView {

    void setLoginSuccess(IndexBean indexBean);


    void setLoginFail(String errInfo);

    void setUpLoadPicSuccess(UpLoadPicBean upLoadPicBean,PostSuccessEvent postSuccessEvent);

    void setUpLoadPicFail(String errInfo);
}
