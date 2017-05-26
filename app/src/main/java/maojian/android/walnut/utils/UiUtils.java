package maojian.android.walnut.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author hezuzhi
 * @Description: (尺寸转换，获取屏幕信息)
 * @date 2016/4/13.
 * @version: 1.0
 */
public class UiUtils {
    //public static void showToast(Context c, String info) {
    //    Toast.makeText(c, info, Toast.LENGTH_LONG).show();
    //}
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 将dip转换为px
     * @param c 上下文相关对象
     * @param dip 待转换的数值
     * @return 返回转换的px数值
     */
    public static int dip2px(Context c, int dip) {
        final float scale = c.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 将sp转换为px
     * @param c 上下文相关对象
     * @param sp 待转换的数值
     * @return 返回转换的px数值
     */
    public static int sp2px(Context c, int sp) {
        final float scale = c.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    /**
     * 获取手机屏幕宽度
     * @param c 上下文相关对象
     * @return 返回手机屏幕宽度
     */
    public static int getScreenWidth(Context c) {
        DisplayMetrics display = c.getResources().getDisplayMetrics();
        return display.widthPixels;
    }

    /**
     * 获取手机屏幕高度
     * @param c 上下文相关对象
     * @return 返回手机屏幕高度
     */
    public static int getScreenHeight(Context c) {
        DisplayMetrics display = c.getResources().getDisplayMetrics();
        return display.heightPixels;
    }

}
