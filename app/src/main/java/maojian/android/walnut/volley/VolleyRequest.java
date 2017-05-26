package maojian.android.walnut.volley;

import android.content.Context;

/**
 * @author hezuzhi
 * @Description: (网络请求对象)
 * @date 2016/4/29  11:24.
 * @version: 1.0
 */
public class VolleyRequest {

    public RequestManager mRequestManager;

    public VolleyRequest() {
        mRequestManager = new RequestManager();
    }


    /**
     * 返回String 带进度条 get
     *
     * @param context
     * @param method
     * @param progressTitle
     * @param l
     */
    public void get(Context context, String method, String data, String progressTitle,
                    RequestListener l) {
        String url = CallApi.getHttpGetUrl(method, data);
        mRequestManager.get(url, context, progressTitle, l);
    }

    /**
     * 返回对象 get
     *
     * @param context
     * @param method
     * @param classOfT
     * @param l
     * @param <T>
     */
    public <T> void get(Context context, String method, String data, Class<T> classOfT,
                        RequestBeanListener<T> l) {
        String url = CallApi.getHttpGetUrl(method, data);
        mRequestManager.get(url, context, classOfT, null, false, l);
    }

    /**
     * 返回对象 带进度条 get
     *
     * @param context
     * @param method
     * @param classOfT
     * @param progressTitle
     * @param <T>
     */
    public <T> void get(Context context, String method, String data, Class<T> classOfT,
                        String progressTitle, RequestBeanListener<T> l) {
        String url = CallApi.getHttpGetUrl(method, data);
        mRequestManager.get(url, context, classOfT, progressTitle, true, l);

    }

    /**
     * 返回对象 带进度条 get 可选择显示进度 适合带分页
     *
     * @param context
     * @param method
     * @param classOfT
     * @param progressTitle
     * @param LoadingShow   true (显示进度) false (不显示进度)
     * @param <T>
     */
    public <T> void get(Context context, String data, String method, Class<T> classOfT,
                        String progressTitle, boolean LoadingShow, RequestBeanListener<T> l) {
        String url = CallApi.getHttpGetUrl(method, data);
        mRequestManager.get(url, context, classOfT, progressTitle, LoadingShow,
                l);

    }
    /**
     * 返回String get
     *
     * @param context
     * @param url
     * @param l
     */
    public void get(Context context, String url, RequestListener l) {
        mRequestManager.get(url, context, l);
    }

    /**
     * 返回String 带进度条 get
     *
     * @param context
     * @param url
     * @param progressTitle
     * @param l
     */
    public void get(Context context, String url, String progressTitle,
                    RequestListener l) {
        mRequestManager.get(url, context, progressTitle, l);
    }

    /**
     * 返回对象 get
     *
     * @param context
     * @param url
     * @param classOfT
     * @param l
     * @param <T>
     */
    public <T> void get(Context context, String url, Class<T> classOfT,
                        RequestBeanListener<T> l) {
        mRequestManager.get(url, context, classOfT, null, false, l);
    }

    /**
     * 返回对象 带进度条 get
     *
     * @param context
     * @param url
     * @param classOfT
     * @param progressTitle
     * @param <T>
     */
    public <T> void get(Context context, String url, Class<T> classOfT,
                        String progressTitle, RequestBeanListener<T> l) {
        mRequestManager.get(url, context, classOfT, progressTitle, true, l);

    }

    /**
     * 返回对象 带进度条 get 可选择显示进度 适合带分页
     *
     * @param context
     * @param url
     * @param classOfT
     * @param progressTitle
     * @param LoadingShow   true (显示进度) false (不显示进度)
     * @param <T>
     */
    public <T> void get(Context context, String url, Class<T> classOfT,
                        String progressTitle, boolean LoadingShow, RequestBeanListener<T> l) {
        mRequestManager.get(url, context, classOfT, progressTitle, LoadingShow,
                l);

    }

    /**
     * 返回String post
     *
     * @param context
     * @param url
     * @param params
     * @param l
     */
    public void post(Context context, String url, RequestParams params,
                     RequestListener l) {
        mRequestManager.post(url, context, params, l);
    }

    /**
     * 返回对象 post
     *
     * @param context
     * @param url
     * @param classOfT
     * @param params
     * @param l
     */
    public <T> void post(Context context, String url, Class<T> classOfT,
                         RequestParams params, RequestBeanListener<T> l) {
        mRequestManager.post(url, context, classOfT, params, null, false, l);
    }

    /**
     * 返回String 带进度条 post
     *
     * @param context
     * @param url
     * @param params
     * @param progressTitle
     * @param l
     */
    public void post(Context context, String url, RequestParams params,
                     String progressTitle, RequestListener l) {
        mRequestManager.post(url, context, params, progressTitle, l);
    }

    /**
     * 返回对象 带进度条 post
     *
     * @param context
     * @param url
     * @param classOfT
     * @param params
     * @param l
     */
    public <T> void post(Context context, String url, Class<T> classOfT,
                         RequestParams params, String progressTitle, RequestBeanListener<T> l) {
//        String url = CallApi.getHttpGetUrl(method, data);
        mRequestManager.post(url, context, classOfT, params, progressTitle,
                true, l);

    }

    /**
     * 返回对象 带进度条 post 可选择显示进度 适合带分页
     *
     * @param context
     * @param url
     * @param classOfT
     * @param params
     * @param progressTitle
     * @param LoadingShow   true (显示进度) false (不显示进度)
     * @param l
     */
    public <T> void post(Context context, String url, Class<T> classOfT,
                         RequestParams params, String progressTitle, boolean LoadingShow,
                         RequestBeanListener<T> l) {
        mRequestManager.post(url, context, classOfT, params, progressTitle,
                LoadingShow, l);
    }

}
