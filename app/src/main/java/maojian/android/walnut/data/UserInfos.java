package maojian.android.walnut.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import maojian.android.walnut.BaseConstant;
import maojian.android.walnut.login.LoginBean;
import maojian.android.walnut.me.userdate.UserBean;
import maojian.android.walnut.utils.JsonUtils;
import maojian.android.walnut.utils.SharedPreferencesUtils;


/**
 * @author hezuzhi
 * @Description: (用户资料信息保存)
 * @date 2016/5/4  9:22.
 * @version: 1.0
 */
public class UserInfos {

    /**
     * 用户唯一ID
     */
    private static String KEY_USER_BEAN = "mLoginBean";
    private static LoginBean mLoginBean;

    private static String KEY_userBean = "userBean";
    private static UserBean userBean;

    private static int savephoto = 0;
    private static String KEY_savephoto = "savephoto";

    public static int getSavephoto(Context c) {
        savephoto = SharedPreferencesUtils.getInt(c, KEY_savephoto, savephoto);
        return savephoto;
    }

    public static void setSavephoto(Context c, int savephoto) {
        UserInfos.savephoto = savephoto;
        SharedPreferencesUtils.setInt(c, KEY_savephoto, savephoto);
    }

    public static UserBean getUserBean() {
        return userBean;
    }

    public static void setUserBean(Context c, UserBean userBean) {
        UserInfos.userBean = userBean;
        SharedPreferencesUtils.setString(c, KEY_userBean, JsonUtils.toJson(userBean));
    }

    public static LoginBean getLoginBean() {
        return mLoginBean;
    }

    public static void setLoginBean(Context c, LoginBean mLoginBean) {
        UserInfos.mLoginBean = mLoginBean;
        SharedPreferencesUtils.setString(c, KEY_USER_BEAN, JsonUtils.toJson(mLoginBean));
    }


    /**
     * initGlobalInfo 初始化用户信息
     *
     * @param c
     */
    public static void initUserInfo(Context c) {
        mLoginBean = JsonUtils.object(SharedPreferencesUtils.getString(c, KEY_USER_BEAN), LoginBean.class);
        userBean = JsonUtils.object(SharedPreferencesUtils.getString(c, KEY_userBean), UserBean.class);
    }

    /**
     * 更新用户mUserId
     *
     * @param c         上下文对象
     * @param loginBean 用户mLoginBean
     */
    public static void updateLoginBean(Context c, LoginBean loginBean) {
        mLoginBean = loginBean;
        SharedPreferencesUtils.setString(c, KEY_USER_BEAN, JsonUtils.toJson(loginBean));
//        BaseConstant.initUrl();
    }


    /**
     * 清理缓存
     *
     * @param c
     */
    public static void clearData(Activity c) {
//        UserInfo.personCardBean = null;
        SharedPreferencesUtils.clearData(c);
        updateLoginBean(c, null);
        setUserBean(c, null);
//        setServicePortUrl(c);
    }


}
