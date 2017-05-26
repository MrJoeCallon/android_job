package maojian.android.walnut;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.twitter.Twitter;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

import com.tencent.mm.sdk.openapi.WXAPIFactory;
import maojian.android.walnut.ImagePicker.ProfilePickerActivity;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.demo.login.UserInfo;
import maojian.android.walnut.home.addpost.UpLoadPicBean;
import maojian.android.walnut.login.LoginBean;
import maojian.android.walnut.me.*;
import maojian.android.walnut.me.userdate.UserBean;
import maojian.android.walnut.message.MessagePresenter;
import maojian.android.walnut.utils.ActionSheetDialog;
import maojian.android.walnut.utils.Base64;
import maojian.android.walnut.utils.LastInputEditText;
import maojian.android.walnut.utils.ShareUtils;
import maojian.android.walnut.utils.eventbus.FinishActivityEvent;
import maojian.android.walnut.volley.RequestBeanListener;
import maojian.android.walnut.volley.RequestListener;
import maojian.android.walnut.volley.RequestParams;
import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * Created by jie on 2016/8/1.
 */
public class EditprofileActivity extends AnyTimeActivity implements MeView {
    private MePresenter mePresenter;
    private ImageView avatar;
    DisplayImageOptions avatarstyle;
    private LastInputEditText username;
    private EditText useremail;
    private Button epsave;

    private TextView usergender;
    private LastInputEditText userskatename;
    private LastInputEditText info;
    private LastInputEditText epheight, epweight;

    private TextView editprofile_button;
    //新选的图片路径
    public static String profileImage = "";

    private UserBean userBean;
    private LinearLayout ll_gender;

    ShareUtils shareUtils;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_editprofile);
        mePresenter = new MePresenter(this, this);
        avatarstyle = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(Color.WHITE, 1))
                .build();

        final View epview = getWindow().getDecorView();
        epview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                epview.setFocusable(true);
                epview.setFocusableInTouchMode(true);
                epview.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(username.getWindowToken(), 0);
                return false;
            }
        });
        ll_gender = (LinearLayout) findViewById(R.id.ll_gender);
        avatar = (ImageView) findViewById(R.id.imageView2);
        username = (LastInputEditText) findViewById(R.id.epusername);
        useremail = (LastInputEditText) findViewById(R.id.epmail);
        epheight = (LastInputEditText) findViewById(R.id.epheight);
        epweight = (LastInputEditText) findViewById(R.id.epweight);
        usergender = (TextView) findViewById(R.id.epusergender);
        userskatename = (LastInputEditText) findViewById(R.id.epskatebotname);
        info = (LastInputEditText) findViewById(R.id.info);

//        setHasFocus();


        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.terms).setOnClickListener(this);
        epsave = (Button) findViewById(R.id.epsave);


        shareUtils = new ShareUtils(null, this);

        userBean = UserInfos.getUserBean();
        if (userBean != null) {
            setView(userBean);
        } else {
            username.setText(UserInfos.getLoginBean().getUser_name());
            useremail.setText(UserInfos.getLoginBean().getUser_email());
            ImageLoader.getInstance().displayImage(UserInfos.getLoginBean().getHeader_image(), avatar, avatarstyle);
        }
        FacebookSdk.sdkInitialize(this);
        epsave.setOnClickListener(this);
        findViewById(R.id.epbackButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        EditprofileActivity.this.finish();
                    }
                }
        );

        editprofile_button = (TextView) findViewById(R.id.editprofile_editbutton);
        editprofile_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showChoiceDialog();


                    }
                }
        );

        Button logout_button = (Button) findViewById(R.id.logout_button);
        logout_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AVService.logout();
                        UserInfos.clearData(EditprofileActivity.this);
                        EventBus.getDefault().post(new FinishActivityEvent());
                        Intent loginIntent = new Intent(EditprofileActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                        EditprofileActivity.this.finish();
                    }
                }
        );


        findViewById(R.id.report_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditprofileActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ll_gender).setOnClickListener(this);
        findViewById(R.id.facebookfriends).setOnClickListener(this);
        findViewById(R.id.changepassword).setOnClickListener(this);
        findViewById(R.id.pushsetting).setOnClickListener(this);
        findViewById(R.id.savephoto).setOnClickListener(this);
        findViewById(R.id.privacy).setOnClickListener(this);
        findViewById(R.id.linkedaccount).setOnClickListener(this);
        avatar.setOnClickListener(this);
//        BaseConstant.km = 6;
    }

    private void setHasFocus() {
        username.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String username1 = username.getText().toString();
                if (hasFocus && !TextUtils.isEmpty(username1)) {
                    // 获得焦点
                    username.setText(username1);
                    username.setSelection(username1.length());
                    username.requestFocus();
                }
            }
        });

        useremail.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus && !TextUtils.isEmpty(useremail.getText())) {
                    // 获得焦点
                    useremail.setSelection(useremail.getText().length());
                }
            }
        });

        epheight.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus && !TextUtils.isEmpty(epheight.getText())) {
                    // 获得焦点
                    epheight.setSelection(epheight.getText().length());
                }
            }
        });

        epweight.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus && !TextUtils.isEmpty(epweight.getText())) {
                    // 获得焦点
                    epweight.setSelection(epweight.getText().length());
                }
            }
        });

        userskatename.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus && !TextUtils.isEmpty(userskatename.getText())) {
                    // 获得焦点
                    userskatename.setSelection(userskatename.getText().length());
                }
            }
        });

        info.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus && !TextUtils.isEmpty(info.getText())) {
                    // 获得焦点
                    info.setSelection(info.getText().length());
                }
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (TextUtils.isEmpty(profileImage)) {
            getdata();
        } else {
            try {
                postImgUpload(profileImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void postImgUpload(String path) throws Exception {
        RequestParams params = new RequestParams();
//        if (UserInfos.getLoginBean() != null)
        String Baseurl = "";//
        Baseurl = Base64.imgPath2Base64(path);//imgToBase64(path, null);// URLEncoder.encode(Base64.imgToBase64(path, null), "utf-8");
        params.put("image", "data:image/jpg;base64," + Baseurl);

        // 发送请求
        mePresenter.getVolleyRequest().post(this, BaseConstant.uoloadUserHeadImg, UpLoadPicBean.class, params, getResources().getString(R.string.login_tips),
                new RequestBeanListener<UpLoadPicBean>() {
                    @Override
                    public void requestSuccess(UpLoadPicBean result) {
                        profileImage = "";
                        if (result != null && !TextUtils.isEmpty(result.getImage_url())) {
                            ImageLoader.getInstance().displayImage(result.getImage_url(), avatar, avatarstyle);
                            userBean.getUserinfo().setClass_icon(result.getImage_url());
                            editUser(userBean);
                        }
                    }

                    @Override
                    public void requestError(String e) {
                        profileImage = "";
                    }
                });
    }

    /**
     * 仿IOS底部弹出  拍照 相册  取消的弹框
     */
    public void showChoiceDialog() {
        ActionSheetDialog dialog = new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("Import from Facebook", ActionSheetDialog.SheetItemColor.Green,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                authorize(Facebook.NAME);
                            }
                        });
        dialog.addSheetItem("Import from Twitter", ActionSheetDialog.SheetItemColor.Green,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        authorize(Twitter.NAME);
                    }
                });
        dialog.addSheetItem("Import from Camera Library", ActionSheetDialog.SheetItemColor.Green,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        Intent intent = new Intent(EditprofileActivity.this, ProfilePickerActivity.class);
                        startActivity(intent);
                    }
                });
        //    按钮
        dialog.addSheetItem("Take photo", ActionSheetDialog.SheetItemColor.Green,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        Intent intent = new Intent(EditprofileActivity.this, ProFileCameraActivity.class);
                        startActivity(intent);
                    }
                });
        dialog.show();
    }


    private void authorize(final String name) {
        Platform weibo = ShareSDK.getPlatform(name);
//回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        weibo.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                arg2.printStackTrace();
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> platform) {
                // TODO Auto-generated method stub
                //输出所有授权信息
                arg0.getDb().exportData();
                String headimgurl = "";
                String type;

                if (name.equals("Twitter")) {
                    headimgurl = platform.get("profile_image_url_https").toString();//"profile_image_url_https" -> "https://abs.twimg.com/sticky/default_profile_images/default_profile_0_normal.png".1024/72f9a9ebjw1e8qgp5bmzyj2050050aa8.jpg"

                } else if (name.equals("Facebook")) {
                    if (platform.get("picture") != null)
                        try {
                            HashMap<String, Object> headimgHashMap = (HashMap<String, Object>) platform.get("picture");
                            if (headimgHashMap.get("data") != null) {
                                HashMap<String, Object> data = (HashMap<String, Object>) headimgHashMap.get("data");
                                if (data.get("url") != null) {
                                    headimgurl = data.get("url").toString();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                }
                if (!TextUtils.isEmpty(headimgurl)) {
                    ImageLoader.getInstance().displayImage(headimgurl, avatar, avatarstyle);
                    userBean.getUserinfo().setClass_icon(headimgurl);
                    editUser(userBean);
                }


            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
//authorize与showUser单独调用一个即可
        weibo.authorize();//单独授权,OnComplete返回的hashmap是空的
        weibo.showUser(null);//授权并获取用户信息
//移除授权
//weibo.removeAccount(true);
    }

    public void setView(UserBean userBean) {
        username.setText(userBean.getUserinfo().getClass_name());
        useremail.setText(userBean.getUserinfo().getEmail());
        usergender.setText(userBean.getUserinfo().getGender());
        userskatename.setText(userBean.getUserinfo().getSpectra_name());
        info.setText(userBean.getUserinfo().getInfo());
        epheight.setText(userBean.getUserinfo().getHeight() + "");
        epweight.setText(userBean.getUserinfo().getWeight() + "");
        findViewById(R.id.pushsetting).setSelected(userBean.getUserinfo().getIs_push() == 0 ? false : true);
        findViewById(R.id.savephoto).setSelected(UserInfos.getSavephoto(this) == 0 ? false : true);

        ImageLoader.getInstance().displayImage(userBean.getUserinfo().getClass_icon(), avatar, avatarstyle);
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {

            case R.id.ll_gender:
                showGenderChoiceDialog();
                break;
            case R.id.facebookfriends:
//                showShareDialog();

                String content = "Your friend " + UserInfos.getLoginBean().getUser_name() + " invites you to play WALNUTTGO!!!"
                        + "https://itunes.apple.com/hk/app/walnutt-go/id1127657933?l=zh&mt=8";
                shareUtils.setUserId(UserInfos.getLoginBean().getUser_id());
                Platform plat = ShareSDK.getPlatform(Facebook.NAME);
                shareUtils.showShare(plat.getName(), "", content);
                break;
            case R.id.changepassword:
                startActivity(new Intent(EditprofileActivity.this, ChangePasswordActivity.class));
                break;
            case R.id.pushsetting:

                v.setSelected(!v.isSelected());
                break;
            case R.id.savephoto:
                v.setSelected(!v.isSelected());
                UserInfos.setSavephoto(EditprofileActivity.this, v.isSelected() ? 1 : 0);
                break;
            case R.id.imageView2:
                showChoiceDialog();
                break;
            case R.id.privacy:
                startActivity(new Intent(EditprofileActivity.this, StoreWebActivity.class).putExtra("url", BaseConstant.shopify));
                break;
            case R.id.terms:
                startActivity(new Intent(EditprofileActivity.this, StoreWebActivity.class).putExtra("url", BaseConstant.shopify));
                break;

            case R.id.epsave:

                userBean.getUserinfo().setClass_name(username.getText().toString());//  username.setText(.getClass_name());
//                useremail.setText(userBean.getUserinfo().getEmail());
                userBean.getUserinfo().setGender(usergender.getText().toString());//usergender.setText(userBean.getUserinfo().getGender());
                userBean.getUserinfo().setSpectra_name(userskatename.getText().toString());//.setText(userBean.getUserinfo().getSpectra_name());
                userBean.getUserinfo().setInfo(info.getText().toString());//info.setText(.getInfo());
                userBean.getUserinfo().setHeight(Integer.parseInt(epheight.getText().toString()));//epheight.setText(userBean.getUserinfo().getHeight() + "");
                userBean.getUserinfo().setWeight(Integer.parseInt(epweight.getText().toString()));//.setText(userBean.getUserinfo().getWeight() + "");
                userBean.getUserinfo().setIs_push(findViewById(R.id.pushsetting).isSelected() ? 1 : 0);//setSelected(.getIs_push() == 0 ? false : true);
                editUser(userBean);

                break;
            case R.id.linkedaccount:
                startActivity(new Intent(EditprofileActivity.this, LinkAccountActivity.class));
                break;
            case R.id.clear:
                try {
                    ImageLoader.getInstance().clearDiskCache();
                    ImageLoader.getInstance().clearMemoryCache();//清理volley缓存数据
                    Toast.makeText(EditprofileActivity.this, "Clear Success", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    String appLinkUrl, previewImageUrl;

    private void showShareDialog() {
        appLinkUrl = "https://www.mydomain.com/myapplink";
        previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show(this, content);
        }
    }

    /**
     * 仿IOS底部弹出  拍照 相册  取消的弹框
     */
    public void showGenderChoiceDialog() {
        ActionSheetDialog dialog = new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("Male", ActionSheetDialog.SheetItemColor.Green,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                usergender.setText("male");
                            }
                        });
        //    按钮
        dialog.addSheetItem("Female", ActionSheetDialog.SheetItemColor.Green,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        usergender.setText("female");
                    }
                });
        dialog.show();
    }

    private void editUser(final UserBean userBean) {
        mePresenter.editUser(userBean, new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                UserInfos.setUserBean(EditprofileActivity.this, userBean);
                LoginBean mLoginBean = UserInfos.getLoginBean();
                mLoginBean.setUser_name(userBean.getUserinfo().getClass_name());
                mLoginBean.setHeader_image(userBean.getUserinfo().getClass_icon());
                UserInfos.setLoginBean(EditprofileActivity.this, mLoginBean);
                Toast.makeText(EditprofileActivity.this, "Edit Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void requestError(String e) {
                Toast.makeText(EditprofileActivity.this, e, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getdata() {
        mePresenter.userInfo(UserInfos.getLoginBean().getUser_id());
    }


    @Override
    public void setPostSuccess(MyPostList myPostList, boolean likePost) {

    }

    @Override
    public void setUserBeanSuccess(UserBean userBean1, boolean likePost) {
        if (userBean1 != null) {
            userBean = userBean1;
            UserInfos.setUserBean(EditprofileActivity.this, userBean1);
            setView(userBean);
        }
    }

    @Override
    public void setPostFail(String errInfo) {

    }
}
