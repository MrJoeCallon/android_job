package maojian.android.walnut;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.*;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
//import com.twitter.sdk.android.core.Result;
//import com.twitter.sdk.android.core.TwitterException;
//import com.twitter.sdk.android.core.TwitterSession;
//import com.twitter.sdk.android.core.identity.TwitterAuthClient;


import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.login.*;
import maojian.android.walnut.utils.eventbus.FinishActivityEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AnyTimeActivity implements LoginView, View.OnLayoutChangeListener {
    LoginPresenter loginPresenter;
    ImageButton loginButton;
    ImageButton registerButton;
    ImageButton forgetPasswordButton;
    EditText userNameEditText;
    EditText userPasswordEditText;
    private ProgressDialog progressDialog;
    ImageView image;
    CallbackManager callbackManager;
    private AccessToken accessToken;


    ImageButton facebook_loginButton;

//	private TwitterAuthClient client;

    private ImageView withoutLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        super.onCreate(savedInstanceState);
//		TwitterAuthConfig authConfig = new TwitterAuthConfig("u9uyYYnlumipcGUU6YJSlPtno", "kc1UL9CPPI3FDVbGMlYN6PVrCeRzw7cISrJzOA2cShV1T2TzFu");
//		Fabric.with(LoginActivity.this, new Twitter(authConfig));

        setContentView(R.layout.activity_login);
        loginPresenter = new LoginPresenter(this, this);
        callbackManager = CallbackManager.Factory.create();
        EventBus.getDefault().register(this);

//		try {
//			PackageInfo info = getPackageManager().getPackageInfo(
//					"maojian.android.walnut",
//					PackageManager.GET_SIGNATURES);
//			for (Signature signature : info.signatures) {
//				MessageDigest md = MessageDigest.getInstance("SHA");
//				md.update(signature.toByteArray());
//				Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//			}
//		} catch (PackageManager.NameNotFoundException e) {
//
//		} catch (NoSuchAlgorithmException e) {
//
//		}


//		client= new TwitterAuthClient();
        ShareSDK.initSDK(this);
//        Platform wechat= ShareSDK.getPlatform(this, Wechat.NAME);
//        wechat.setPlatformActionListener(paListener);
//        wechat.authorize();

        Platform[] Platformlist = ShareSDK.getPlatformList();
        AVAnalytics.trackAppOpened(getIntent());

        AVService.initPushService(this);

        loginButton = (ImageButton) findViewById(R.id.button_login);
        registerButton = (ImageButton) findViewById(R.id.button_register);
        forgetPasswordButton = (ImageButton) findViewById(R.id.button_forget_password);
        userNameEditText = (EditText) findViewById(R.id.editText_userName);
        userPasswordEditText = (EditText) findViewById(R.id.editText_userPassword);


        withoutLogin = (ImageView) findViewById(R.id.withoutLogin);
        withoutLogin.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.bottom_in,
                                R.anim.bottom_out);
                        finish();
                    }
                }
        );

        if (getUserId() != null) {
            Intent mainIntent = new Intent(activity, MainActivity.class);
            startActivity(mainIntent);
            activity.finish();
        }

        loginButton.setOnClickListener(loginListener);
        registerButton.setOnClickListener(registerListener);
        forgetPasswordButton.setOnClickListener(forgetPasswordListener);

        findViewById(R.id.login_wechat).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login("Wechat", "3");
//						LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
                    }
                }
        );

        findViewById(R.id.login_weibo).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("abc", "SinaWeibo onclick");
                        login("SinaWeibo", "4");
//						LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
                    }
                }
        );

        // Facebook login
        facebook_loginButton = (ImageButton) findViewById(R.id.login_facebook);

        facebook_loginButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("abc", "facebook onclick");
                        login("Facebook", "1");
//						LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
                    }
                }
        );


        //幫 LoginManager 增加callback function

        //這邊為了方便 直接寫成inner class

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            //登入成功

            @Override
            public void onSuccess(LoginResult loginResult) {

                //accessToken之後或許還會用到 先存起來

                accessToken = loginResult.getAccessToken();

                Log.d("FB", "access token got.");

                //send request and call graph api

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {

                            //當RESPONSE回來的時候

                            @Override
                            public void onCompleted(final JSONObject object, GraphResponse response) {

                                //讀出姓名 ID FB個人頁面連結

                                Log.e("FB1", "complete");
                                Log.e("FB1", object.optString("name"));
                                Log.e("FB1", object.optString("link"));
                                Log.e("FB1", object.optString("id"));

                                AVQuery<AVObject> followquery = new AVQuery<>("_User");
                                followquery.whereEqualTo("socialID", object.optString("id"));

                                Log.e("FB1", "find user");

                                followquery.findInBackground(
                                        new FindCallback<AVObject>() {
                                            @Override
                                            public void done(List<AVObject> list, AVException e) {

                                                Log.e("FB1", "find done " + list);
                                                if (list != null) {

                                                    if (list.size() > 0) {

                                                        AVUser cUser = (AVUser) list.get(0);
                                                        Log.e("abc", "facebook login debug");
                                                        AVUser.logInInBackground(cUser.getUsername(),
                                                                "WALNUTTT",
                                                                new LogInCallback() {
                                                                    public void done(AVUser user, AVException e) {
                                                                        if (user != null) {
                                                                            progressDialogDismiss();
                                                                            Intent mainIntent = new Intent(activity,
                                                                                    MainActivity.class);
                                                                            startActivity(mainIntent);
                                                                            activity.finish();
                                                                        } else {
                                                                            progressDialogDismiss();
                                                                            showLoginError();
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        final SignUpCallback signUpCallback = new SignUpCallback() {
                                                            public void done(AVException e) {

                                                                final AVFile profileImage = new AVFile("profile", "http://ac-4jow6b01.clouddn.com/e342297307144f23.png", new HashMap<String, Object>());
                                                                profileImage.saveInBackground(
                                                                        new SaveCallback() {
                                                                            @Override
                                                                            public void done(AVException e) {

                                                                                AVUser cUser = AVUser.getCurrentUser();
                                                                                cUser.put("profileImage", profileImage);
                                                                                cUser.saveInBackground(
                                                                                        new SaveCallback() {
                                                                                            @Override
                                                                                            public void done(AVException e) {

                                                                                                progressDialogDismiss();
                                                                                                if (e == null) {
                                                                                                    showRegisterSuccess();
                                                                                                    Intent mainIntent = new Intent(activity, MainActivity.class);
                                                                                                    startActivity(mainIntent);
                                                                                                    activity.finish();
                                                                                                } else {
                                                                                                    switch (e.getCode()) {
                                                                                                        case 202:
                                                                                                            showError(activity
                                                                                                                    .getString(R.string.error_register_user_name_repeat));
                                                                                                            break;
                                                                                                        case 203:
                                                                                                            showError(activity
                                                                                                                    .getString(R.string.error_register_email_repeat));
                                                                                                            break;
                                                                                                        default:
                                                                                                            showError(activity
                                                                                                                    .getString(R.string.network_error));
                                                                                                            break;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                );

                                                                            }
                                                                        }
                                                                );
                                                            }
                                                        };


                                                        //AVFile file = new AVFile("profile", "http://ac-4jow6b01.clouddn.com/e342297307144f23.png", new HashMap<String, Object>());

                                                        AVService.signUp(object.optString("name"), "WALNUTTT", "", object.optString("id"), signUpCallback);


                                                    }

                                                }

                                            }
                                        }
                                );


                            }
                        });

                //包入你想要得到的資料 送出request

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();
            }

            //登入取消

            @Override
            public void onCancel() {
                // App code

                Log.d("FB", "CANCEL");
            }

            //登入失敗

            @Override
            public void onError(FacebookException exception) {
                // App code

                Log.e("abc", exception.toString());
            }
        });


        // Twitter login
//		final TwitterAuthClient mTwitterAuthClient= new TwitterAuthClient();

        ImageButton twitter_custom_button = (ImageButton) findViewById(R.id.login_twitter);
        twitter_custom_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                login("Twitter", "2");


            }
        });

//		showShare();
        listenerRootView();

    }

    @Override
    public void onClickEvent(View v) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(FinishActivityEvent mFinishActivityEvent) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void listenerRootView() {
        //该Activity的最外层Layout
        final View activityRootView = findViewById(R.id.activityRoot);
         image = (ImageView) findViewById(R.id.imageView);
//给该layout设置监听，监听其布局发生变化事件
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                //比较Activity根布局与当前布局的大小
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();

                if (heightDiff > 800) {
                    //大小超过100时，一般为显示虚拟键盘事件
//                    findViewById(R.id.topview).setVisibility(View.GONE);
                    image.setBackgroundResource(R.drawable.welcome);
                } else {
                    //大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
//                    findViewById(R.id.topview).setVisibility(View.VISIBLE);
                    image.setBackgroundResource(R.drawable.loginlogo);
                }
            }
        });

        //设置而已监听器
        activityRootView.addOnLayoutChangeListener(this);

    }


    /*
     * 演示执行第三方登录/注册的方法
     * <p>
     * 这不是一个完整的示例代码，需要根据您项目的业务需求，改写登录/注册回调函数
     *  登录类型（1:facebook 2:Twitter 3:微信 4:微博）
     * @param platformName 执行登录/注册的平台名称，如：SinaWeibo.NAME
     */
    String nickname = "";
    String headimgurl = "";
    String openid = "";

    private void login(String platformName, final String logoinType) {
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(new OnLoginListener() {
            public boolean onLogin(String platform, HashMap<String, Object> res) {
                // 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
                // 此处全部给回需要注册
                if (platform.equals("SinaWeibo")) {
                    nickname = res.get("screen_name").toString();
                    headimgurl = res.get("avatar_hd").toString();//"avatar_hd" -> "http://tva4.sinaimg.cn/crop.0.0.180.180.1024/72f9a9ebjw1e8qgp5bmzyj2050050aa8.jpg"
                    openid = res.get("id").toString();
                } else if (platform.equals("Twitter")) {
                    nickname = res.get("screen_name").toString();
                    headimgurl = res.get("profile_image_url_https").toString();//"profile_image_url_https" -> "https://abs.twimg.com/sticky/default_profile_images/default_profile_0_normal.png".1024/72f9a9ebjw1e8qgp5bmzyj2050050aa8.jpg"
                    openid = res.get("id").toString();
                } else if (platform.equals("Facebook")) {
                    nickname = res.get("name").toString();
                    if (res.get("picture") != null)
                        try {
                            HashMap<String, Object> headimgHashMap = (HashMap<String, Object>) res.get("picture");
                            if (headimgHashMap.get("data") != null) {
                                HashMap<String, Object> data = (HashMap<String, Object>) headimgHashMap.get("data");
                                if (data.get("url") != null) {
                                    headimgurl = data.get("url").toString();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    if (res.get("avatar_hd") != null)
                        headimgurl = res.get("avatar_hd").toString();//"profile_image_url_https" -> "https://abs.twimg.com/sticky/default_profile_images/default_profile_0_normal.png".1024/72f9a9ebjw1e8qgp5bmzyj2050050aa8.jpg"
                    openid = res.get("id").toString();//"third_party_id" -> "dinWr3chU0ejfVPpdZDXtkREXDc"
                } else {
                    Log.d("openid", res.get("openid").toString());
                    nickname = res.get("nickname").toString();
                    headimgurl = res.get("headimgurl").toString();
                    openid = res.get("openid").toString();
                }

                loginPresenter.otherLogin(logoinType, openid);
                return true;
            }

            public boolean onRegister(UserInfo info) {
                // 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
                return true;
            }
        });
        api.login(this);
    }


//    public static Map<String, Object> objectToMap(Object obj) throws Exception {
//        if(obj == null)
//            return null;
//
//        Map<String, Object> map = new HashMap<String, Object>();
//
//        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
//        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//        for (PropertyDescriptor property : propertyDescriptors) {
//            String key = property.getName();
//            if (key.compareToIgnoreCase("class") == 0) {
//                continue;
//            }
//            Method getter = property.getReadMethod();
//            Object value = getter!=null ? getter.invoke(obj) : null;
//            map.put(key, value);
//        }
//
//        return map;
//    }


    /**
     * 返回由对象的属性为key,值为map的value的Map集合
     *
     * @param obj Object
     * @return mapValue Map<String,String>
     * @throws Exception
     */
    public static Map<String, String> getFieldVlaue(Object obj) throws Exception {
        Map<String, String> mapValue = new HashMap<String, String>();
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            String strGet = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
            Method methodGet = cls.getDeclaredMethod(strGet);
            Object object = methodGet.invoke(obj);
            String value = object != null ? object.toString() : "";
            mapValue.put(name, value);
        }
        return mapValue;
    }

    private void showShare() {
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
        oks.show(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
//		client.onActivityResult(requestCode, responseCode, intent);
    }


    OnClickListener loginListener = new OnClickListener() {

        @SuppressLint("NewApi")
        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void onClick(View arg0) {
            String username = userNameEditText.getText().toString();
            if (username.isEmpty()) {
                showUserNameEmptyError();
                return;
            }
            if (password().isEmpty()) {
                showUserPasswordEmptyError();
                return;
            }
            loginPresenter.login(username, password());
//            progressDialogShow();
//            AVUser.logInInBackground(username,
//                    password(),
//                    new LogInCallback() {
//                        public void done(AVUser user, AVException e) {
//                            if (user != null) {
//                                progressDialogDismiss();
//                                Intent mainIntent = new Intent(activity,
//                                        MainActivity.class);
//                                startActivity(mainIntent);
//                                activity.finish();
//                            } else {
//                                progressDialogDismiss();
//                                showLoginError();
//                            }
//                        }
//                    });
        }

        private String password() {
            return userPasswordEditText.getText().toString();
        }
    };


    OnClickListener forgetPasswordListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            Intent forgetPasswordIntent = new Intent(activity, ForgetPasswordActivity.class);
            startActivity(forgetPasswordIntent);
//            activity.finish();
        }
    };

    OnClickListener registerListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent registerIntent = new Intent(activity, RegisterActivity.class);
            startActivity(registerIntent);
//            activity.finish();
        }
    };

    private void progressDialogDismiss() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    private void progressDialogShow() {
        progressDialog = ProgressDialog
                .show(activity,
                        activity.getResources().getText(
                                R.string.dialog_message_title),
                        activity.getResources().getText(
                                R.string.dialog_text_wait), true, false);
    }

    private void showLoginError() {
        new AlertDialog.Builder(activity)
                .setTitle(
                        activity.getResources().getString(
                                R.string.dialog_error_title))
                .setMessage(
                        activity.getResources().getString(
                                R.string.error_login_error))
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    private void showUserPasswordEmptyError() {
        new AlertDialog.Builder(activity)
                .setTitle(
                        activity.getResources().getString(
                                R.string.dialog_error_title))
                .setMessage(
                        activity.getResources().getString(
                                R.string.error_register_password_null))
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    private void showUserNameEmptyError() {
        new AlertDialog.Builder(activity)
                .setTitle(
                        activity.getResources().getString(
                                R.string.dialog_error_title))
                .setMessage(
                        activity.getResources().getString(
                                R.string.error_register_user_name_null))
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }


    private void showRegisterSuccess() {
        new AlertDialog.Builder(activity)
                .setTitle(
                        activity.getResources().getString(
                                R.string.dialog_message_title))
                .setMessage(
                        activity.getResources().getString(
                                R.string.success_register_success))
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    @Override
    public void setLoginSuccess(LoginBean mLoginBean, String logoinType) {
        if (mLoginBean.is_first()) {
            mLoginBean.setHeader_image(headimgurl);
            mLoginBean.setUser_name(nickname);
            mLoginBean.setLogoinType(logoinType);
            mLoginBean.setOpenid(openid);
            startActivity(new Intent(LoginActivity.this, EmailActivity.class).putExtra("mLoginBean", mLoginBean));
        } else {
            UserInfos.setLoginBean(LoginActivity.this, mLoginBean);
            startActivity(new Intent(LoginActivity.this, DeviceActivity.class));
            finish();
        }

    }

    @Override
    public void setLoginFail(String errInfo) {
        if (!TextUtils.isEmpty(errInfo))
            Toast.makeText(LoginActivity.this, errInfo, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (bottom < oldBottom) {
            image.setBackgroundResource(R.drawable.welcome);
        } else if (bottom > oldBottom) {
            image.setBackgroundResource(R.drawable.loginlogo);
        }
    }
}

