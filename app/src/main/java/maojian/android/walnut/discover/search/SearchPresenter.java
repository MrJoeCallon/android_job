package maojian.android.walnut.discover.search;


import android.content.Context;
import maojian.android.walnut.BaseConstant;
import maojian.android.walnut.R;
import maojian.android.walnut.UserListBean;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.home.IndexBean;
import maojian.android.walnut.home.IndexView;
import maojian.android.walnut.utils.presenter.Presenter;
import maojian.android.walnut.volley.RequestBeanListener;
import maojian.android.walnut.volley.RequestListener;
import maojian.android.walnut.volley.RequestParams;


/**
 * @author hezuzhi
 * @Description:
 * @date 2016/5/4  10:19.
 * @version: 1.0
 */
public class SearchPresenter extends Presenter<UserListView> {
    /**
     * 构造函数
     *
     * @param view 要绑定的视图对象
     */
    public SearchPresenter(Context context, UserListView view) {
        super(context, view);
    }


    public void search(int page, String search_string) {
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("size", "100");
        params.put("search_string", search_string);
        params.put("page", page + "");
        // 发送请求
        mVolleyRequest.post(context, BaseConstant.searchList, UserListBean.class, params, new RequestBeanListener<UserListBean>() {
            @Override
            public void requestSuccess(UserListBean result) {
                if (mView != null) {
                    mView.setSuccess(result);
                }
            }

            @Override
            public void requestError(String e) {
                if (mView != null) {
                    mView.setFail(e);
                }
            }
        });
    }

    /**
     * @param removeFollow 1关注 2取消关注
     */
    public void removeFollow(int removeFollow, String class_id, RequestListener requestListener) {
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("is_follow", removeFollow + "");
        params.put("class_id", class_id + "");

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.removeFollow, params, context.getResources().getString(R.string.login_tips), requestListener);
    }


}
