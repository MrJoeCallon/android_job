package maojian.android.walnut.home.addpost;


import android.content.Context;
import maojian.android.walnut.BaseConstant;
import maojian.android.walnut.R;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.home.comment.CommentList;
import maojian.android.walnut.home.comment.CommentView;
import maojian.android.walnut.utils.Base64;
import maojian.android.walnut.utils.presenter.Presenter;
import maojian.android.walnut.volley.RequestBeanListener;
import maojian.android.walnut.volley.RequestListener;
import maojian.android.walnut.volley.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * @author hezuzhi
 * @Description:
 * @date 2016/5/4  10:19.
 * @version: 1.0
 */
public class AddPostPresenter extends Presenter<AddPostView> {
    /**
     * 构造函数
     *
     * @param view 要绑定的视图对象
     */
    public AddPostPresenter(Context context, AddPostView view) {
        super(context, view);
    }


    public void postImgUpload(String path, boolean isVideo) throws Exception {
        RequestParams params = new RequestParams();
//        if (UserInfos.getLoginBean() != null)
        String Baseurl = "";//
        if (isVideo) {
            Baseurl = Base64.encodeBase64File(path);//imgToBase64(path, null);// URLEncoder.encode(Base64.imgToBase64(path, null), "utf-8");
            params.put("file", "data:file/mp4;base64," + Baseurl);
        } else {
            Baseurl = Base64.imgPath2Base64(path);//imgToBase64(path, null);// URLEncoder.encode(Base64.imgToBase64(path, null), "utf-8");
            params.put("image", "data:image/jpg;base64," + Baseurl);
        }

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.postImgUpload, UpLoadPicBean.class, params, context.getResources().getString(R.string.login_tips),
                new RequestBeanListener<UpLoadPicBean>() {
                    @Override
                    public void requestSuccess(UpLoadPicBean result) {
                        if (mView != null) {
                            mView.setUpLoadPicSuccess(result);
                        }
                    }

                    @Override
                    public void requestError(String e) {
                        if (mView != null) {
                            mView.setUpLoadPicFail(e);
                        }
                    }
                });
    }

    public void postVideoUpload(String path) throws Exception {
        RequestParams params = new RequestParams();
//        if (UserInfos.getLoginBean() != null)
        String Baseurl = "";//
        Baseurl = Base64.encodeBase64File(path);//imgToBase64(path, null);// URLEncoder.encode(Base64.imgToBase64(path, null), "utf-8");
        params.put("video", "data:file/mp4;base64," + Baseurl);

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.uploadVoideBast64, UpLoadPicBean.class, params, context.getResources().getString(R.string.login_tips),
                new RequestBeanListener<UpLoadPicBean>() {
                    @Override
                    public void requestSuccess(UpLoadPicBean result) {
                        if (mView != null) {
                            mView.setUpLoadPicSuccess(result);
                        }
                    }

                    @Override
                    public void requestError(String e) {
                        if (mView != null) {
                            mView.setUpLoadPicFail(e);
                        }
                    }
                });
    }

    /**
     */
    public void addPost(String rurl, String litpic, String post_type, String address, String content, RequestListener requestListener) {
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("post_type", post_type);//#1 图片 2视频
        params.put("litpic", litpic);// #当post_type==1，“”，==2时 视频缩列图连接

        params.put("rurl", rurl);//连接
        params.put("address", address);
        params.put("content", content);
        if (BaseConstant.longitude != 0)
            params.put("lng", BaseConstant.longitude + "");
        if (BaseConstant.latitude != 0)
            params.put("lat", BaseConstant.latitude + "");

//                *friends=“  2，222，444”#提醒好友查看者ID

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.addPost, params, requestListener);
    }

    /**
     */

    public void addLocation(RequestBeanListener requestListener) {
        RequestParams params = new RequestParams();
//        params.put("lng", BaseConstant.longitude + "");
//        params.put("lat", BaseConstant.latitude + "");
        params.put("lng", "-102.254.674");
        params.put("lat", "50.563313");
        // 发送请求
        mVolleyRequest.post(context, BaseConstant.getLocationList, LocationBean.class, params,
                context.getResources().getString(R.string.login_tips), requestListener);
    }

}
