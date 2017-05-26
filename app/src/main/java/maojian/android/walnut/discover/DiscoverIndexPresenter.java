package maojian.android.walnut.discover;


import android.content.Context;
import maojian.android.walnut.BaseConstant;
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
public class DiscoverIndexPresenter extends Presenter<DiscoverIndexView> {
    /**
     * 构造函数
     *
     * @param view 要绑定的视图对象
     */
    public DiscoverIndexPresenter(Context context, DiscoverIndexView view) {
        super(context, view);
    }


    public void getIndex(int page) {
        RequestParams params = new RequestParams();
        params.put("size", "10");
        params.put("page", page + "");

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.discoverIndex, DiscoverIndexBean.class, params, new RequestBeanListener<DiscoverIndexBean>() {
            @Override
            public void requestSuccess(DiscoverIndexBean result) {
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
