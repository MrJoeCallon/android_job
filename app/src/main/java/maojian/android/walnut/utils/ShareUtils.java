package maojian.android.walnut.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;

import cn.sharesdk.wechat.utils.WechatHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import maojian.android.walnut.BaseConstant;
import maojian.android.walnut.FeedbackActivity;
import maojian.android.walnut.Home;
import maojian.android.walnut.R;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.volley.RequestListener;
import maojian.android.walnut.volley.RequestParams;
import maojian.android.walnut.volley.VolleyRequest;

import java.util.HashMap;

/**
 * @author hezuzhi
 * @Description: ()
 * @date 2017/3/29  9:42.
 * @version: 1.0
 */
public class ShareUtils {
    IWXAPI api;
    Context context;
    private String userId = "";
    String username;
    ShareDialog mShareDialog;
    private String post_id = "";
    String title;


    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ShareUtils(IWXAPI api, Context contex) {
        this.api = api;
        this.context = contex;
    }

    public void showShareDialog(final String url, final String username, final String content) {
//        showShare(context);
//        return;
        this.username = username;
        this.title = content;
        mShareDialog = new ShareDialog(context);
        if (!TextUtils.isEmpty(userId) && UserInfos.getLoginBean() != null && userId.equals(UserInfos.getLoginBean().getUser_id()))
            mShareDialog.setRepotrGone();

        mShareDialog.builder();
        mShareDialog.setConfim(new ShareDialog.confirmListener() {
            @Override
            public void onClick(int item) {
                Platform plat;
                switch (item) {
                    case 0:
                        plat = ShareSDK.getPlatform(Facebook.NAME);
                        showShare(plat.getName(), url, content);
                        break;
                    case 1:
//                        plat = ShareSDK.getPlatform(Wechat.NAME);
//                        showShare(context, plat.getName(), url, content);
                        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {
                                TestWXSceneSession(url, content, null);
                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                TestWXSceneSession(url, content, bitmap);
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });

                        break;
                    case 2:
                        plat = ShareSDK.getPlatform(Twitter.NAME);
                        showShare(plat.getName(), url, content);
                        break;
                    case 3:
//                        doSinaWeiboShare(context, url, content);
                        plat = ShareSDK.getPlatform(SinaWeibo.NAME);
                        showShare(plat.getName(), url, content);
                        break;
                    case 4:
//                        doSinaWeiboShare(context, url, content);
                        if (mShareDialog.reportV) {
                            //删除
                            delete();

                        } else {
                            Intent intent = new Intent(context, FeedbackActivity.class);
                            context.startActivity(intent);
                        }


                        break;
                }
            }
        });

        mShareDialog.show();

    }

    private void delete() {
        // 发送请求
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("post_id", post_id);
        new VolleyRequest().post(context, BaseConstant.deletePost, params, "", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                if (context != null) {
                    Toast.makeText(context, "Delete Success", Toast.LENGTH_LONG).show();
                    Home.getWallImages(0, true);
                }
            }

            @Override
            public void requestError(String e) {
                if (context != null)
                    Toast.makeText(context, e, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showShare(String platform, String url, String content) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        if (!TextUtils.isEmpty(title)) {
            oks.setTitle(title);
            oks.setText("Check out my WALNUTT eBoard");
        } else {
            oks.setTitle("Check out my WALNUTT eBoard");
        }
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
//        oks.setText(content);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        if (!TextUtils.isEmpty(url))
            oks.setImageUrl(url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        if (!TextUtils.isEmpty(url))
            oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");

        //启动分享
        oks.show(context);
    }

    private void TestWXSceneSession(String url, final String text, Bitmap shareBitmap) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
//        if (!TextUtils.isEmpty(userId) && UserInfos.getLoginBean() != null && userId.equals(UserInfos.getLoginBean().getUser_id()))
//            msg.title = "我分享了一张照片，快来围观！";
//        else
//            msg.title = "我分享了一张" + username + "的照片，快来围观！";
        if (!TextUtils.isEmpty(title)) {
            msg.title = title;
            msg.description = "Check out my WALNUTT eBoard";
        } else {
            msg.title = "Check out my WALNUTT eBoard";
        }
//        msg.description = text;
        //这里替换一张自己工程里的图片资源
        //msg.setThumbImage(thumb);
        if (shareBitmap != null) {
            msg.thumbData = ImageUtils.Bitmap2Bytes(shareBitmap);//压缩到32k以下
            int imageSize = msg.thumbData.length / 1024;
            if (imageSize >= 32) {
                shareBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
                msg.thumbData = ImageUtils.Bitmap2Bytes(shareBitmap);
            }
        } else {
            shareBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            msg.thumbData = ImageUtils.Bitmap2Bytes(shareBitmap);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);


//        // 初始化一个WXTextObject对象
//        WXTextObject textObj = new WXTextObject();
//        textObj.text = text;
//
//        // 用WXTextObject对象初始化一个WXMediaMessage对象
//        WXMediaMessage msg = new WXMediaMessage();
//        msg.mediaObject = textObj;
//        // 发送文本类型的消息时，title字段不起作用
//        // msg.title = "Will be ignored";
//        msg.description = text;
//
//        // 构造一个Req
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = "text" + System.currentTimeMillis(); // transaction字段用于唯一标识一个请求
//        req.message = msg;
//        req.scene = SendMessageToWX.Req.WXSceneSession;
////        req.openId = getOpenId();
//        // 调用api接口发送数据到微信
//        api.sendReq(req);
    }

//    private static void doWeChatShare(final Context context, String url, final String content) {
//        WechatHelper.ShareParams sp = new Wechat.ShareParams();
//        sp.title = content;// setTitle(content);
////        sp.setText(content);
//        sp.imageUrl = url;//setImageUrl(url);
//        sp.setShareType(Platform.SHARE_IMAGE);
//
////        sp.setImagePath("https://apprest.walnuttech.co/uploads/images/20170321/20170321223633_9xIvFS.jpg");
//        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
////        wechat.shareType(Platform.SHARE_IMAGE);
//        wechat.setPlatformActionListener(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Toast.makeText(context, "分享成功", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//                Toast.makeText(context, "分享失败", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//
//            }
//        }); // 设置分享事件回调
//// 执行图文分享
//        wechat.share(sp);
//    }

    private static void doSinaWeiboShare(final Context context, String url, final String content) {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setText(content);
        sp.setImageUrl(url);
//        sp.setImagePath("https://apprest.walnuttech.co/uploads/images/20170321/20170321223633_9xIvFS.jpg");
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(context, "分享成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(context, "分享失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        }); // 设置分享事件回调
// 执行图文分享
        weibo.share(sp);
    }

    public static void showShare(Context context) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(context);
    }
}
