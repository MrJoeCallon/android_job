package maojian.android.walnut.me.postdetails;


import maojian.android.walnut.home.IndexBean;

/**
 * @author hezuzhi
 * @Description: (登录信息回调)
 * @date 2016/5/4  10:19.
 * @version: 1.0
 */
public interface PostDetailsView {

    void setSuccess(IndexBean.IndexListBean postDetailsBean);


    void setFail(String errInfo);
}
