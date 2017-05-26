package maojian.android.walnut.me.postdetails;


import android.content.Context;
import maojian.android.walnut.BaseConstant;
import maojian.android.walnut.R;
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
public class PostDetailsPresenter extends Presenter<PostDetailsView> {
    /**
     * 构造函数
     *
     * @param view 要绑定的视图对象
     */
    public PostDetailsPresenter(Context context, PostDetailsView view) {
        super(context, view);
    }


    public void getPostDetails(String post_id, IndexBean.IndexListBean indexListBean) {
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("post_id", post_id + "");

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.postDetails, IndexBean.IndexListBean.class, params, context.getResources().getString(R.string.login_tips),
                indexListBean == null ? true : false, new RequestBeanListener<IndexBean.IndexListBean>() {
                    @Override
                    public void requestSuccess(IndexBean.IndexListBean result) {
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
     * @param is_praise #1点赞 2取消点赞
     */
    public void like(int is_praise, String post_id, RequestListener requestListener) {
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("is_praise", is_praise + "");
        params.put("post_id", post_id + "");

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.like, params, requestListener);
    }


}
