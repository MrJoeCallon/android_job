package maojian.android.walnut.login;


/**
 * @author hezuzhi
 * @Description: (登录信息回调)
 * @date 2016/5/4  10:19.
 * @version: 1.0
 */
public interface LoginView {
    /**
     * 设置用户登录成功
     * @param mLoginBean 登录成功的响应
     */
    void setLoginSuccess(LoginBean mLoginBean,String logoinType);

    /**
     * 设置登录失败
     */
    void setLoginFail(String errInfo);
}
