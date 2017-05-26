package maojian.android.walnut.discover.search;


import maojian.android.walnut.UserListBean;

/**
 * @author hezuzhi
 * @Description: (登录信息回调)
 * @date 2016/5/4  10:19.
 * @version: 1.0
 */
public interface UserListView {

    void setSuccess(UserListBean userListBean);


    void setFail(String errInfo);
}
