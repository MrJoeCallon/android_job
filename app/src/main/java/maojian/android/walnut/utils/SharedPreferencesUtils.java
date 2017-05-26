package maojian.android.walnut.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author hezuzhi
 * @Description: (SharedPreferences数据缓存)
 * @date 2016/4/13.
 * @version: 1.0
 */
public class SharedPreferencesUtils {

    /**
     * 取Bean
     *
     * @param c   当前上下文
     * @param key 保存字符串值的key
     * @return 返回key对应的值，若获取不到，则返回空字符串
     */
    public static <T> T getBean(Context c, String key, Class<T> classOfT) {
        String json = getString(c, key, "");
        return JsonUtils.object(json, classOfT);
    }

    /**
     * 将一个对象保存起来
     *
     * @param c      当前上下文
     * @param key    保存字符串的key
     * @param object 要保存的对象的值
     */
    public static void setBean(Context c, String key, Object object) {
        SharedPreferences.Editor e = getSharedDataEditor(c);
        if (e != null) e.putString(key, JsonUtils.toJson(object)).commit();
    }

    /**
     * 取一个字符串值
     *
     * @param c   当前上下文
     * @param key 保存字符串值的key
     * @return 返回key对应的值，若获取不到，则返回空字符串
     */
    public static String getString(Context c, String key) {
        return getString(c, key, "");
    }

    /**
     * 取一个字符串值
     *
     * @param c        当前上下文
     * @param key      保存字符串值的key
     * @param defValue 默认值
     * @return 返回key对应的值，若获取不到，则返回默认值
     */
    public static String getString(Context c, String key, String defValue) {
        SharedPreferences sharedata = getSharedData(c);
        return sharedata == null ? defValue : sharedata.getString(key, defValue);
    }

    /**
     * 将一个字符串保存起来
     *
     * @param c    当前上下文
     * @param key  保存字符串的key
     * @param data 要保存的字符串的值
     */
    public static void setString(Context c, String key, String data) {
        SharedPreferences.Editor e = getSharedDataEditor(c);
        if (e != null) e.putString(key, data).commit();
    }

    /**
     * 取一个整数值
     *
     * @param c   当前上下文
     * @param key 保存整数值的key
     * @return 返回key对应的值，若获取不到，则返回0
     */
    public static int getInt(Context c, String key) {
        return getInt(c, key, 0);
    }

    /**
     * 取一个整数值
     *
     * @param c        当前上下文
     * @param key      保存整数值的key
     * @param defValue 默认值
     * @return 返回key对应的值，若获取不到，则返回默认值
     */
    public static int getInt(Context c, String key, int defValue) {
        SharedPreferences sharedata = getSharedData(c);
        return sharedata == null ? defValue : sharedata.getInt(key, defValue);
    }

    /**
     * 将一个整数值保存起来
     *
     * @param c    当前上下文
     * @param key  保存布尔值的key
     * @param data 要保存的值
     */
    public static void setInt(Context c, String key, int data) {
        SharedPreferences.Editor e = getSharedDataEditor(c);
        if (e != null) e.putInt(key, data).commit();
    }

    /**
     * 将一个布尔值保存起来
     *
     * @param c    当前上下文
     * @param key  保存布尔值的key
     * @param data 要保存的布尔值
     */
    public static void setBoolean(Context c, String key, boolean data) {
        SharedPreferences.Editor e = getSharedDataEditor(c);
        if (e != null) e.putBoolean(key, data).commit();
    }

    /**
     * 取一个布尔值
     *
     * @param c   当前上下文
     * @param key 保存布尔值的key
     * @return 返回key对应的布尔值，若获取不到，返回false
     */
    public static boolean getBoolean(Context c, String key) {
        return getBoolean(c, key, false);
    }

    /**
     * 取一个布尔值
     *
     * @param c        当前上下文
     * @param key      保存布尔值的key
     * @param defValue 默认值
     * @return 返回key对应的布尔值，若获取不到，则返回默认值
     */
    public static boolean getBoolean(Context c, String key, boolean defValue) {
        SharedPreferences sharedata = getSharedData(c);
        return sharedata == null ? defValue : sharedata.getBoolean(key, defValue);
    }

    /**
     * 清除数据
     *
     * @param c 当前上下文
     */
    public static void clearData(Context c) {
        SharedPreferences.Editor sharedata = getSharedDataEditor(c);
        if (sharedata != null) sharedata.clear().commit();
    }

    /**
     * 移除数据
     *
     * @param c 当前上下文
     */
    public static void removeData(Context c, String key) {
        SharedPreferences.Editor sharedata = getSharedDataEditor(c);
        if (sharedata != null) sharedata.remove(key).commit();
    }


    /**
     * 获取SharedPreferences对象
     *
     * @param c 当前上下文
     * @return 返回SharedPreferences对象
     */
    private static SharedPreferences getSharedData(Context c) {
        return c == null ? null : c.getSharedPreferences("data", 0);
    }

    /**
     * 获取Editor对象
     *
     * @param c 当前上下文
     * @return 返回Editor对象
     */
    private static SharedPreferences.Editor getSharedDataEditor(Context c) {
        SharedPreferences e = getSharedData(c);
        return e == null ? null : e.edit();
    }
}
