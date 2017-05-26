package maojian.android.walnut.home.comment;


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
public class CommentPresenter extends Presenter<CommentView> {
    /**
     * 构造函数
     *
     * @param view 要绑定的视图对象
     */
    public CommentPresenter(Context context, CommentView view) {
        super(context, view);
    }


    public void getComment(int page, String post_id) {
        RequestParams params = new RequestParams();
//        if (UserInfos.getLoginBean() != null)
        params.put("post_id", post_id);
        params.put("size", "100");
        params.put("page", page + "");

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.commentList, CommentList.class, params, context.getResources().getString(R.string.login_tips), new RequestBeanListener<CommentList>() {
            @Override
            public void requestSuccess(CommentList result) {
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
     */
    public void addComment(String comments_content, String post_id, RequestListener requestListener) {
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("comments_content", comments_content + "");
        params.put("post_id", post_id + "");

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.commentPost, params, context.getResources().getString(R.string.login_tips), requestListener);
    }

}
