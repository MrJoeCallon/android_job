package maojian.android.walnut;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
import maojian.android.walnut.utils.ToastUtil;
import maojian.android.walnut.utils.eventbus.FinishActivityEvent;
import maojian.android.walnut.utils.eventbus.OutEvent;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AnyTimeActivity implements LoginView {
    LoginPresenter loginPresenter;
    Button registerButton;
    EditText userName;
    //	EditText userEmail;
    EditText userPassword;
    EditText userPasswordAgain;
    private ProgressDialog progressDialog;

    private AccessToken accessToken;
    CallbackManager callbackManager;

//    private TwitterAuthClient client;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
//		this.getActionBar().setDisplayHomeAsUpEnabled(true);
        loginPresenter = new LoginPresenter(this, this);
        callbackManager = CallbackManager.Factory.create();

//        client = new TwitterAuthClient();

        registerButton = (Button) findViewById(R.id.button_i_need_register);
        userName = (EditText) findViewById(R.id.editText_register_userName);
//		userEmail = (EditText) findViewById(R.id.editText_register_email);
        userPassword = (EditText) findViewById(R.id.editText_register_userPassword);
        userPasswordAgain = (EditText) findViewById(R.id.editText_register_userPassword_again);

        registerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (userPassword.getText().toString()
                        .equals(userPasswordAgain.getText().toString())) {
                    if (!userPassword.getText().toString().isEmpty()) {
                        if (!userName.getText().toString().isEmpty()) {
//							if (!userEmail.getText().toString().isEmpty()) {
//								progressDialogShow();
                            register(userName.getText().toString(), userPassword.getText().toString());
//							} else {
//								showError(activity
//										.getString(R.string.error_register_email_address_null));
//							}
                        } else {
                            showError(activity
                                    .getString(R.string.error_register_user_name_null));
                        }
                    } else {
                        showError(activity
                                .getString(R.string.error_register_password_null));
                    }
                } else {
                    showError(activity
                            .getString(R.string.error_register_password_not_equals));
                }
            }
        });


        findViewById(R.id.register_wechat).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login("Wechat", "3");
//						LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
                    }
                }
        );

        findViewById(R.id.register_weibo).setOnClickListener(
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
        ImageButton facebook_loginButton = (ImageButton) findViewById(R.id.register_facebook);

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
        ImageButton twitter_custom_button = (ImageButton) findViewById(R.id.register_twitter);
        twitter_custom_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                login("Twitter", "2");


            }
        });

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
                Log.d("openid", res.get("openid").toString());
                nickname = res.get("nickname").toString();
                headimgurl = res.get("headimgurl").toString();
                openid = res.get("openid").toString();
                loginPresenter.otherLogin(logoinType, res.get("openid").toString());
                return true;
            }

            public boolean onRegister(UserInfo info) {
                // 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
                return true;
            }
        });
        api.login(this);
    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent LoginIntent = new Intent(this, LoginActivity.class);
            startActivity(LoginIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void register(String email, String passward) {
        loginPresenter.register(email, passward);
    }

    public void register() {
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
                                                    Intent mainIntent = new Intent(activity, DeviceActivity.class);
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

//        progressDialogDismiss();
//        if (e == null) {
//          showRegisterSuccess();
//          Intent mainIntent = new Intent(activity, MainActivity.class);
//          startActivity(mainIntent);
//          activity.finish();
//        } else {
//          switch (e.getCode()) {
//            case 202:
//              showError(activity
//                  .getString(R.string.error_register_user_name_repeat));
//              break;
//            case 203:
//              showError(activity
//                  .getString(R.string.error_register_email_repeat));
//              break;
//            default:
//              showError(activity
//                  .getString(R.string.network_error));
//              break;
//          }
//        }
            }
        };
        final String username = userName.getText().toString();
        final String password = userPassword.getText().toString();
//    final String email = userEmail.getText().toString();

//	Resources res=getResources();
//	Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.notloading);
//
//    Bitmap.CompressFormat format= Bitmap.CompressFormat.JPEG;
//    int quality = 100;
//    OutputStream stream = null;
//    try {
//        stream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/walnutprofile.png");
//        bmp.compress(format, quality, stream);
//    } catch (FileNotFoundException e) {
//// TODO Auto-generated catch block
//        e.printStackTrace();
//    }

        AVService.signUp(username, password, "email", Environment.getExternalStorageDirectory() + "/walnutprofile.png", signUpCallback);
    }

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

    @Override
    public void setLoginSuccess(LoginBean mLoginBean, String type) {
        EventBus.getDefault().post(new FinishActivityEvent());
        UserInfos.setLoginBean(RegisterActivity.this, mLoginBean);
        startActivity(new Intent(RegisterActivity.this, DeviceActivity.class));
        finish();
    }

    @Override
    public void setLoginFail(String errInfo) {
        ToastUtil.show(RegisterActivity.this, errInfo);
    }
}
