package maojian.android.walnut.me;


import android.content.Context;
import maojian.android.walnut.BaseConstant;
import maojian.android.walnut.R;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.home.comment.CommentList;
import maojian.android.walnut.home.comment.CommentView;
import maojian.android.walnut.me.userdate.UserBean;
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
public class MePresenter extends Presenter<MeView> {
    /**
     * 构造函数
     *
     * @param view 要绑定的视图对象
     */
    public MePresenter(Context context, MeView view) {
        super(context, view);
    }


    public void userInfo(String class_id) {
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("class_id", class_id + "");

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.userInfo, UserBean.class, params, new RequestBeanListener<UserBean>() {
            @Override
            public void requestSuccess(UserBean result) {
                if (mView != null) {
                    mView.setUserBeanSuccess(result, false);
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

    public void getMyPost(int page) {
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("class_id", UserInfos.getLoginBean().getUser_id());
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
    public void likePost(int page) {
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("class_id", UserInfos.getLoginBean().getUser_id());
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

    public void editUser(UserBean userBean, final RequestListener requestListener) {
        RequestParams params = new RequestParams();
        params.put("user_id", userBean.getUserinfo().getClass_id());
        params.put("header_image", userBean.getUserinfo().getClass_icon());
        params.put("info", userBean.getUserinfo().getInfo());
        params.put("user_name", userBean.getUserinfo().getClass_name());
        params.put("gender", userBean.getUserinfo().getGender());
        params.put("spectra_name", userBean.getUserinfo().getSpectra_name());
        params.put("height", userBean.getUserinfo().getHeight() + "");
        params.put("weight", userBean.getUserinfo().getWeight() + "");
        params.put("is_push", userBean.getUserinfo().getIs_push() + "");
        // 发送请求
        mVolleyRequest.post(context, BaseConstant.editUser, params, "", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                if (mView != null) {
                    requestListener.requestSuccess(json);
                }
            }

            @Override
            public void requestError(String e) {
                if (mView != null) {
                    requestListener.requestError(e);
                }
            }
        });
    }

    public void sendMileage(final RequestListener requestListener) {
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        else
            return;
        if (BaseConstant.km == 0)
            return;
        params.put("today_usage", BaseConstant.km + "");
        params.put("lng", BaseConstant.longitude + "");
        params.put("lat", BaseConstant.latitude + "");
        // 发送请求
        mVolleyRequest.post(context, BaseConstant.sendMileage, params, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                BaseConstant.km = 0;
                if (mView != null) {
                    requestListener.requestSuccess(json);
                }
            }

            @Override
            public void requestError(String e) {
                if (mView != null) {
                    requestListener.requestError(e);
                }
            }
        });
    }

}
