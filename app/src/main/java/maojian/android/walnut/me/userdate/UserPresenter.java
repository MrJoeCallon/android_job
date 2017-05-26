package maojian.android.walnut.me.userdate;


import android.content.Context;
import maojian.android.walnut.BaseConstant;
import maojian.android.walnut.R;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.me.MeView;
import maojian.android.walnut.me.MyPostList;
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
public class UserPresenter extends Presenter<UserView> {
    /**
     * 构造函数
     *
     * @param view 要绑定的视图对象
     */
    public UserPresenter(Context context, UserView view) {
        super(context, view);
    }


    public void userInfo(String class_id) {
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("class_id", class_id + "");

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.userInfo, UserBean.class, params,"", new RequestBeanListener<UserBean>() {
            @Override
            public void requestSuccess(UserBean result) {
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

    public void follow(String class_id, RequestListener requestListener) {

        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("is_follow", "1");
        params.put("class_id", class_id);

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.removeFollow, params,
                context.getResources().getString(R.string.login_tips), requestListener);
    }

    public void getMyPost(int page, String class_id) {
        RequestParams params = new RequestParams();
        params.put("class_id", class_id);
        params.put("size", "100");
        params.put("page", page + "");

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.releasePost, MyPostList.class, params, new RequestBeanListener<MyPostList>() {
            @Override
            public void requestSuccess(MyPostList result) {
                if (mView != null) {
                    mView.setPostSuccess(result, false);
                }
            }

            @Override
            public void requestError(String e) {
                if (mView != null) {
                    mView.setPostFail(e);
                }
            }
        });
    }

    /**
     */
    public void likePost(int page, String class_id) {
        RequestParams params = new RequestParams();
        params.put("class_id", class_id);
        params.put("size", "100");
        params.put("page", page + "");

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.likePost, MyPostList.class, params, new RequestBeanListener<MyPostList>() {
            @Override
            public void requestSuccess(MyPostList result) {
                if (mView != null) {
                    mView.setPostSuccess(result, true);
                }
            }

            @Override
            public void requestError(String e) {
                if (mView != null) {
                    mView.setPostFail(e);
                }
            }
        });
    }

}
