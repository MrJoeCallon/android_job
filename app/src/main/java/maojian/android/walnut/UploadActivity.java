package maojian.android.walnut;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import maojian.android.walnut.home.addpost.*;
import maojian.android.walnut.utils.BitmapUtils;
import maojian.android.walnut.utils.eventbus.AddLocationEvent;
import maojian.android.walnut.utils.eventbus.FinishActivityEvent;
import maojian.android.walnut.utils.eventbus.PostBean;
import maojian.android.walnut.utils.eventbus.PostSuccessEvent;
import maojian.android.walnut.volley.RequestBeanListener;
import maojian.android.walnut.volley.RequestListener;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
//import com.twitter.sdk.android.core.TwitterAuthConfig;
//import com.twitter.sdk.android.core.TwitterCore;
//import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//import io.fabric.sdk.android.Fabric;

/**
 * Created by android on 5/8/16.
 */
public class UploadActivity extends AnyTimeActivity implements AddPostView {
    private AddPostPresenter addPostPresenter;

    private LayoutInflater inflater;

    private DisplayImageOptions options;

    private String objectID;
    private String objectPath;
    private ImageView upload_imageview;

    private ImageButton upload_share;
    private EditText upload_edittext;

    private Boolean isVideo = false;
    private String videoPath;

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    TextView addlocation_button;
    private String location;
    private Handler handler = new Handler();
    private boolean faceBookShare = false, weChat = false, WeiBo = false, Twitter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload);
        addPostPresenter = new AddPostPresenter(this, this);
        TextView tagpeople_button = (TextView) findViewById(R.id.tagpeople_button);
        addlocation_button = (TextView) findViewById(R.id.addlocation_button);

        final Typeface face1 = Typeface.createFromAsset(UploadActivity.this.getAssets(), "fonts/Brown-Regular.otf");
        tagpeople_button.setTypeface(face1);
        addlocation_button.setTypeface(face1);
        setTitle("Share");
        Intent intent = getIntent();
        addlocation_button.setOnClickListener(this);
//        TwitterAuthConfig authConfig =  new TwitterAuthConfig("consumerKey", "consumerSecret");
//        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

        FacebookSdk.sdkInitialize(this);
        EventBus.getDefault().register(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {


            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        isVideo = intent.getBooleanExtra("isVideo", false);
        videoPath = intent.getStringExtra("VideoPath");

        objectID = "file://" + intent.getStringExtra("filepath");
        objectPath = intent.getStringExtra("filepath");
        if (!isVideo)
            new ScalPicTask(objectPath).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Log.e("abc", "upload debug" + objectID);

        upload_imageview = (ImageView) findViewById(R.id.upload_imageview);

        WindowManager wm = this.getWindowManager();

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        ImageSize mImageSize = new ImageSize(width / 2, width / 2 / 16 * 9);

        ImageLoader.getInstance().loadImage(objectID, mImageSize, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                upload_imageview.setImageBitmap(loadedImage);
            }

        });


        upload_edittext = (EditText) findViewById(R.id.upload_edittext);

        upload_edittext.setTypeface(face1);


        upload_share = (ImageButton) findViewById(R.id.upload_sharebutton);
        upload_share.setEnabled(true);
        upload_share.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newPath = videoPath;
                        if (!isVideo) {
                            newPath = TextUtils.isEmpty(newPath) ? objectPath : newPath;
                        }
                        String conten = "";
                        if (!TextUtils.isEmpty(upload_edittext.getText().toString()))
                            conten = upload_edittext.getText().toString();
                        final PostSuccessEvent postSuccessEvent = new PostSuccessEvent(new PostBean(isVideo, newPath, location, conten));

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Home.setShare(faceBookShare, weChat, WeiBo, Twitter);
                                Home.addPost(postSuccessEvent);

                            }
                        }, 300);
                        EventBus.getDefault().post(postSuccessEvent);

                        finish();
//                        try {
//                            if (isVideo) {
//
//                                addPostPresenter.postVideoUpload(videoPath);
//                            } else {
//                                addPostPresenter.postImgUpload(TextUtils.isEmpty(newPath) ? objectPath : newPath, isVideo);
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        upload_share.setEnabled(false);
//                        try {
//                            final AVFile file = AVFile.withAbsoluteLocalPath("img", objectPath);
//                            file.saveInBackground(
//                                    new SaveCallback() {
//                                        @Override
//                                        public void done(AVException e) {
//                                            final AVObject photo = new AVObject("Photo");
//                                            photo.put("user", AVUser.getCurrentUser());
//                                            photo.put("image", file);
//                                            photo.put("comment", upload_edittext.getText().toString());
//
//                                            if (!isVideo) {
//                                                photo.put("isVideo", false);
//
//
//                                                AVACL likeACL = new AVACL();
//                                                likeACL.setPublicReadAccess(true);// 设置公开的「读」权限，任何人都可阅读
//                                                photo.setACL(likeACL);
//
//                                                photo.saveInBackground(
//                                                        new SaveCallback() {
//                                                            @Override
//                                                            public void done(AVException e) {
//
//                                                                if (upload_edittext.getText().length() > 0) {
//                                                                    AVObject likeobject = new AVObject("Activity");
//                                                                    likeobject.put("type", "comment");
//                                                                    likeobject.put("content", upload_edittext.getText().toString());
//                                                                    likeobject.put("fromUser", AVUser.getCurrentUser());
//                                                                    likeobject.put("toUser", AVUser.getCurrentUser());
//                                                                    likeobject.put("photo", photo);
//
//
//                                                                    AVACL likeACL = new AVACL();
//                                                                    likeACL.setPublicReadAccess(true);// 设置公开的「读」权限，任何人都可阅读
//                                                                    //likeACL.setWriteAccess(AVUser.getCurrentUser(), true);//为当前用户赋予「写」权限
//
//                                                                    likeobject.setACL(likeACL);
//
//                                                                    likeobject.saveInBackground(new SaveCallback() {
//                                                                        @Override
//                                                                        public void done(AVException e) {
//                                                                            if (e == null) {
//
//                                                                                Log.e("abc", "save successfully");
//                                                                                // 存储成功
//                                                                            } else {
//                                                                                Log.e("abc", "error" + e);
//                                                                                // 失败的话，请检查网络环境以及 SDK 配置是否正确
//                                                                            }
//                                                                        }
//                                                                    });
//                                                                }
//
//                                                                Intent intent = new Intent(UploadActivity.this, MainActivity.class);
//                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                                startActivity(intent);
//
//                                                            }
//                                                        }
//                                                );
//
//
//                                            } else {
//                                                photo.put("isVideo", true);
//
//                                                Log.e("abc", "uploading video");
//
//                                                try {
//                                                    Log.e("uploadingvideo", "path: " + videoPath);
//                                                    final AVFile videofile = AVFile.withAbsoluteLocalPath("video.mp4", videoPath);
//                                                    videofile.saveInBackground(
//                                                            new SaveCallback() {
//                                                                @Override
//                                                                public void done(AVException e) {
//
//                                                                    photo.put("video", videofile);
//
//
//                                                                    AVACL likeACL = new AVACL();
//                                                                    likeACL.setPublicReadAccess(true);// 设置公开的「读」权限，任何人都可阅读
//                                                                    photo.setACL(likeACL);
//
//                                                                    photo.saveInBackground(
//                                                                            new SaveCallback() {
//                                                                                @Override
//                                                                                public void done(AVException e) {
//
//                                                                                    if (upload_edittext.getText().length() > 0) {
//                                                                                        AVObject likeobject = new AVObject("Activity");
//                                                                                        likeobject.put("type", "comment");
//                                                                                        likeobject.put("content", upload_edittext.getText().toString());
//                                                                                        likeobject.put("fromUser", AVUser.getCurrentUser());
//                                                                                        likeobject.put("toUser", AVUser.getCurrentUser());
//                                                                                        likeobject.put("photo", photo);
//
//
//                                                                                        AVACL likeACL = new AVACL();
//                                                                                        likeACL.setPublicReadAccess(true);// 设置公开的「读」权限，任何人都可阅读
//                                                                                        //likeACL.setWriteAccess(AVUser.getCurrentUser(), true);//为当前用户赋予「写」权限
//
//                                                                                        likeobject.setACL(likeACL);
//
//                                                                                        likeobject.saveInBackground(new SaveCallback() {
//                                                                                            @Override
//                                                                                            public void done(AVException e) {
//                                                                                                if (e == null) {
//
//                                                                                                    Log.e("abc", "save successfully");
//                                                                                                    // 存储成功
//                                                                                                } else {
//                                                                                                    Log.e("abc", "error" + e);
//                                                                                                    // 失败的话，请检查网络环境以及 SDK 配置是否正确
//                                                                                                }
//                                                                                            }
//                                                                                        });
//                                                                                    }
//
//                                                                                    Intent intent = new Intent(UploadActivity.this, MainActivity.class);
//                                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                                                    startActivity(intent);
//
//                                                                                }
//                                                                            }
//                                                                    );
//
//
//                                                                }
//                                                            }
//                                                    );
//                                                } catch (IOException e1) {
//                                                    e1.printStackTrace();
//                                                }
//
//
//                                            }
//
//
//                                        }
//                                    }
//                            );
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }

                }
        );


        final ImageButton facebookshare = (ImageButton) findViewById(R.id.upload_facebookshare);
        facebookshare.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (faceBookShare) {
                            faceBookShare = false;
                            facebookshare.setBackgroundResource(R.drawable.thirdparty_facebook);
                        } else {
                            faceBookShare = true;
                            facebookshare.setBackgroundResource(R.drawable.xmlid_83_);
                        }


//                        if (!isVideo) {
//                            Log.e("abc", "facebook photo share click");
//                            Bitmap image = BitmapFactory.decodeFile(objectPath);
//                            //Bitmap image = ...
//                            SharePhoto photo = new SharePhoto.Builder()
//                                    .setBitmap(image)
//                                    .build();
//                            SharePhotoContent content = new SharePhotoContent.Builder()
//                                    .addPhoto(photo)
//                                    .build();
//
//                            shareDialog.show(content);
//
//
//                        } else {
//                            Log.e("abc", "facebook video share click");
//                            Uri videoFileUri = Uri.parse(videoPath);
//                            ShareVideo video = new ShareVideo.Builder()
//                                    .setLocalUrl(videoFileUri)
//                                    .build();
//                            ShareVideoContent content = new ShareVideoContent.Builder()
//                                    .setVideo(video)
//                                    .build();
//
//                            shareDialog.show(content);
//                        }
                    }
                }
        );


        final ImageButton twittershare = (ImageButton) findViewById(R.id.upload_twittershare);
        twittershare.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Twitter) {
                            Twitter = false;
                            twittershare.setBackgroundResource(R.drawable.thirdparty_twitter);
                        } else {
                            Twitter = true;
                            twittershare.setBackgroundResource(R.drawable.xmlid_46_);
                        }
                    }
                }
        );

        final ImageButton upload_wechatshare = (ImageButton) findViewById(R.id.upload_wechatshare);
        upload_wechatshare.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (weChat) {
                            weChat = false;
                            upload_wechatshare.setBackgroundResource(R.drawable.thirdparty_wechat);
                        } else {
                            weChat = true;
                            upload_wechatshare.setBackgroundResource(R.drawable.group10578);
                        }
                    }
                }
        );


        final ImageButton upload_weiboshare = (ImageButton) findViewById(R.id.upload_weiboshare);
        upload_weiboshare.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (WeiBo) {
                            WeiBo = false;
                            upload_weiboshare.setBackgroundResource(R.drawable.thirdparty_weibo);
                        } else {
                            WeiBo = true;
                            upload_weiboshare.setBackgroundResource(R.drawable.xmlid_30_);
                        }
                    }
                }
        );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(AddLocationEvent addLocationEvent) {
        location = addLocationEvent.getTitle();
        addlocation_button.setText(location);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.addlocation_button:
                if (BaseConstant.longitude == 0 || BaseConstant.latitude == 0) {
                    Toast.makeText(UploadActivity.this, "获取经纬度失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    return;
                }
                addPostPresenter.addLocation(new RequestBeanListener<LocationBean>() {
                    @Override
                    public void requestSuccess(LocationBean locationBean) {
                        startActivity(new Intent(UploadActivity.this, AddLocationActivity.class)
                                .putExtra("locationBean", locationBean));
                    }

                    @Override
                    public void requestError(String e) {

                    }
                });
                break;
        }
    }

    String newPath = "";

    /**
     * 异步加载数据
     */
    private class ScalPicTask extends AsyncTask<Void, Void, File> {
        private String path;

        /**
         * 构造函数
         */
        public ScalPicTask(String path) {
            this.path = path;
        }

        @Override
        protected File doInBackground(Void... params) {
            //图片压缩
            File picFile = BitmapUtils.scal(path);

            return picFile;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            // 交给处理器处理
            try {
                newPath = file.getPath();//, file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    String upLoadPicUrl = "";

    @Override
    public void setUpLoadPicSuccess(UpLoadPicBean upLoadPicBean) {
        String rurl = "";
        String litpic = "";
        String post_type = "1";
        if (upLoadPicBean != null) {
            upLoadPicUrl = upLoadPicBean.getImage_url();
            if (!TextUtils.isEmpty(upLoadPicUrl)) {
                rurl = upLoadPicUrl;
            } else {
                rurl = upLoadPicBean.getRurl();
                litpic = upLoadPicBean.getLitpic();
                post_type = "2";
            }
            String conten = "";
            if (!TextUtils.isEmpty(upload_edittext.getText().toString()))
                conten = upload_edittext.getText().toString();
            addPostPresenter.addPost(rurl, litpic, post_type, location, conten, new RequestListener() {
                @Override
                public void requestSuccess(String json) {
                    Log.d("post=", json);

                    Toast.makeText(UploadActivity.this, "发布帖子成功", Toast.LENGTH_SHORT).show();
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            viewPager.setCurrentItem(2);
//                        }
//                    }, 50);
                    finish();
                }

                @Override
                public void requestError(String e) {

                }
            });
        }
    }

    @Override
    public void setUpLoadPicFail(String errInfo) {
        Toast.makeText(UploadActivity.this, errInfo, Toast.LENGTH_SHORT).show();

    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
