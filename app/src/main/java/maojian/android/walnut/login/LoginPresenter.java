package maojian.android.walnut.login;


import android.content.Context;
import maojian.android.walnut.BaseConstant;
import maojian.android.walnut.R;
import maojian.android.walnut.utils.presenter.Presenter;
import maojian.android.walnut.volley.RequestBeanListener;
import maojian.android.walnut.volley.RequestParams;


/**
 * @author hezuzhi
 * @Description: (登录信息Bean)
 * @date 2016/5/4  10:19.
 * @version: 1.0
 */
public class LoginPresenter extends Presenter<LoginView> {
    /**
     * 构造函数
     *
     * @param view 要绑定的视图对象
     */
    public LoginPresenter(Context context, LoginView view) {
        super(context, view);
    }

    /**
     * 实现register
     *
     * @param account  用户名
     * @param passWord 用户名
     */
    public void register(String account, String passWord) {
        RequestParams params = new RequestParams();
        params.put("email", account);
        params.put("password", passWord);

        // 发送请求
        mVolleyRequest.post(context, BaseConstant.register, LoginBean.class, params, context.getResources().getString(R.string.login_tips), new RequestBeanListener<LoginBean>() {
            @Override
            public void requestSuccess(LoginBean result) {
                if (mView != null) {
                    mView.setLoginSuccess(result, "");
                }
            }

            @Override
            public void requestError(String e) {
                if (mView != null) {
                    mView.setLoginFail(e);
                }
            }
        });
    }


    /**
     * 实现登录
     *
     * @param account  用户名
     * @param passWord 用户名
     */
    public void login(String account, String passWord) {
        RequestParams params = new RequestParams();
        params.put("email", account);
        params.put("password", passWord);
        // 发送请求
        mVolleyRequest.post(context, BaseConstant.login, LoginBean.class, params, context.getResources().getString(R.string.login_tips), new RequestBeanListener<LoginBean>() {
            @Override
            public void requestSuccess(LoginBean result) {
                if (mView != null) {
                    mView.setLoginSuccess(result, "");
                }
            }

            @Override
            public void requestError(String e) {
                if (mView != null) {
                    mView.setLoginFail(e);
                }
            }
        });
    }

    /**
     * 实现第三方登录
     *
     * @param logoinType 登录类型（1:facebook 2:Twitter 3:微信 4:微博）
     * @param openid     第三方唯一标识
     */
    public void otherLogin(final String logoinType, String openid) {
        RequestParams params = new RequestParams();
        params.put("logoinType", logoinType);
        params.put("openid", openid);
        // 发送请求
        mVolleyRequest.post(context, BaseConstant.otherLogin, LoginBean.class, params, context.getResources().getString(R.string.login_tips), new RequestBeanListener<LoginBean>() {
            @Override
            public void requestSuccess(LoginBean result) {
                if (mView != null) {
                    mView.setLoginSuccess(result, logoinType);
                }
            }

            @Override
            public void requestError(String e) {
                if (mView != null) {
                    mView.setLoginFail(e);
                }
            }
        });
    }

    /**
     * 实现第三方注册
     *
     * @param logoinType 登录类型（1:facebook 2:Twitter 3:微信 4:微博）
     * @param openid     第三方唯一标识
     */
    public void otherRegister(final String logoinType, String openid, String user_name, String header_image, String email) {
        RequestParams params = new RequestParams();
        params.put("logoinType", logoinType);
        params.put("openid", openid);
        params.put("user_name", user_name);
        params.put("header_image", header_image);
        params.put("email", email);
        // 发送请求
        mVolleyRequest.post(context, BaseConstant.otherRegister, LoginBean.class, params, context.getResources().getString(R.string.login_tips), new RequestBeanListener<LoginBean>() {
            @Override
            public void requestSuccess(LoginBean result) {
                if (mView != null) {
                    mView.setLoginSuccess(result, logoinType);
                }
            }

            @Override
            public void requestError(String e) {
                if (mView != null) {
                    mView.setLoginFail(e);
                }
            }
        });
    }

    public void forgetPassword(final String email) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        // 发送请求
        mVolleyRequest.post(context, BaseConstant.forgetPassword, LoginBean.class, params, context.getResources().getString(R.string.login_tips), new RequestBeanListener<LoginBean>() {
            @Override
            public void requestSuccess(LoginBean result) {
                if (mView != null) {
                    mView.setLoginSuccess(result, email);
                }
            }

            @Override
            public void requestError(String e) {
                if (mView != null) {
                    mView.setLoginFail(e);
                }
            }
        });
    }

}
