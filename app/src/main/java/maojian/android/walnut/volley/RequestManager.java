package maojian.android.walnut.volley;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.Volley;


import maojian.android.walnut.AnyTimeApplication;
import maojian.android.walnut.BaseConstant;
import maojian.android.walnut.utils.JsonUtils;
import maojian.android.walnut.utils.LoadingFragment;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * @author hezuzhi
 * @Description: (网络请求管理)
 * @date 2016/5/3  17:35.
 * @version: 1.0
 */
@SuppressLint("NewApi")
public class RequestManager {
    public static RequestQueue mRequestQueue = Volley.newRequestQueue(AnyTimeApplication
            .getApplicationInstance());

    public RequestManager() {

    }

    /**
     * 返回String
     *
     * @param url      连接
     * @param tag      上下文
     * @param listener 回调
     */
    public void get(String url, Object tag, RequestListener listener) {
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,
                url, null, responseListener(listener, false, null),
                responseError(listener, false, null));
        addRequest(request, tag);
    }

    /**
     * 返回String 带进度条
     *
     * @param url           连接
     * @param tag           上下文
     * @param progressTitle 进度条文字
     * @param listener      回调
     */
    public void get(String url, Object tag, String progressTitle,
                    RequestListener listener) {
        LoadingFragment dialog = new LoadingFragment();
        dialog.show(((FragmentActivity) tag).getSupportFragmentManager(),
                "Loading");
        if (TextUtils.isEmpty(progressTitle)) {
            progressTitle = "Loading...";
        }
        dialog.setMsg(progressTitle);
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,
                url, null, responseListener(listener, true, dialog),
                responseError(listener, true, dialog));
        addRequest(request, tag);
    }

    /**
     * 返回对象
     *
     * @param url      连接
     * @param tag      上下文
     * @param classOfT 类对象
     * @param listener 回调
     */
    public <T> void get(String url, Object tag, Class<T> classOfT,
                        RequestBeanListener<T> listener) {
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,
                url, null, responseListener(listener, classOfT, false, null),
                responseError(listener, false, null));
        addRequest(request, tag);
    }

    /**
     * 返回对象
     *
     * @param url           连接
     * @param tag           上下文
     * @param classOfT      类对象
     * @param progressTitle 进度条文字
     * @param listener      回调
     */
    public <T> void get(String url, Object tag, Class<T> classOfT,
                        String progressTitle, boolean LoadingShow, RequestBeanListener<T> listener) {
        LoadingFragment dialog = new LoadingFragment();
        if (LoadingShow) {
            dialog.show(((FragmentActivity) tag).getSupportFragmentManager(),
                    "Loading");
            if (TextUtils.isEmpty(progressTitle)) {
                progressTitle = "正在加载中...";
            }
            dialog.setMsg(progressTitle);
        }
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.GET,
                url, null, responseListener(listener, classOfT, LoadingShow, dialog),
                responseError(listener, LoadingShow, dialog));
        addRequest(request, tag);
    }

    /**
     * 返回String
     *
     * @param url      接口
     * @param tag      上下文
     * @param params   post需要传的参数
     * @param listener 回调
     */
    public void post(String url, Object tag, RequestParams params,
                     RequestListener listener) {
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.POST,
                url, params, responseListener(listener, false, null),
                responseError(listener, false, null));
        addRequest(request, tag);
    }

    /**
     * 返回String 带进度条
     *
     * @param url           接口
     * @param tag           上下文
     * @param params        post需要传的参数
     * @param progressTitle 进度条文字
     * @param listener      回调
     */
    public void post(String url, Object tag, RequestParams params,
                     String progressTitle, RequestListener listener) {
        LoadingFragment dialog = new LoadingFragment();
        dialog.show(((FragmentActivity) tag).getSupportFragmentManager(),
                "Loading");
        if (TextUtils.isEmpty(progressTitle)) {
            progressTitle = "Loading...";
        }
        dialog.setMsg(progressTitle);
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.POST,
                url, params, responseListener(listener, true, dialog),
                responseError(listener, true, dialog));
        addRequest(request, tag);
    }

    /**
     * 返回对象 带进度条
     *
     * @param url           接口
     * @param tag           上下文
     * @param classOfT      类对象
     * @param params        post需要传的参数
     * @param progressTitle 进度条文字
     * @param LoadingShow   true (显示进度) false (不显示进度)
     * @param listener      回调
     */
    public <T> void post(String url, Object tag, Class<T> classOfT,
                         RequestParams params, String progressTitle, boolean LoadingShow,
                         RequestBeanListener<T> listener) {
        LoadingFragment dialog = new LoadingFragment();
        if (LoadingShow) {
            dialog.show(((FragmentActivity) tag).getSupportFragmentManager(),
                    "Loading");
            if (TextUtils.isEmpty(progressTitle)) {
                progressTitle = "Loading...";
            }
            dialog.setMsg(progressTitle);
        }
        ByteArrayRequest request = new ByteArrayRequest(Request.Method.POST,
                url, params,
                responseListener(listener, classOfT, LoadingShow, dialog),
                responseError(listener, LoadingShow, dialog));
        addRequest(request, tag);
    }

    /**
     * 成功消息监听 返回对象
     *
     * @param l
     * @return
     */
    protected <T> Response.Listener<byte[]> responseListener(
            final RequestBeanListener<T> l, final Class<T> classOfT,
            final boolean flag, final LoadingFragment p) {
        return new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] arg0) {
                String data = null;
                try {
                    data = new String(arg0, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d("服务器返回", "data= " + data);
                    JSONObject mJsonObject = new JSONObject(data);
                    if (mJsonObject != null && !mJsonObject.isNull("errNum")) {
                        String status = mJsonObject.getString("errNum");
                        String errMsg = mJsonObject.getString("retMsg");
                        String message = mJsonObject.getString("retData");
                        if (status.equals(BaseConstant.REQUEST_OK)) {
                            if (getJSONType(message) == JSON_TYPE.JSON_TYPE_OBJECT) {
                                l.requestSuccess(JsonUtils.object(message, classOfT));
                            } else if (getJSONType(message) == JSON_TYPE.JSON_TYPE_ARRAY) {
                                l.requestSuccess(JsonUtils.object(data, classOfT));
                            } else if (getJSONType(data) == JSON_TYPE.JSON_TYPE_OBJECT) {
                                l.requestSuccess(JsonUtils.object(data, classOfT));
                            } else {
                                l.requestSuccess(null);
                            }
                        } else {
                            l.requestError(errMsg);
                        }
                    }  else if (mJsonObject != null && !mJsonObject.isNull("result_code")) {
                        String resultCode = mJsonObject.getString("result_code");
                        String message = mJsonObject.getString("result");
                        String resultMessage = mJsonObject.getString("result_message");
                        if (resultCode.equals("0")) {
                            if (getJSONType(message) == JSON_TYPE.JSON_TYPE_OBJECT) {
                                l.requestSuccess(JsonUtils.object(message, classOfT));
                            } else if (getJSONType(message) == JSON_TYPE.JSON_TYPE_ARRAY) {
                                l.requestSuccess(JsonUtils.object(data, classOfT));
                            } else {
                                l.requestSuccess(null);
                            }
                        } else {
                            l.requestError(resultMessage);
                        }
                    } else {
                        l.requestSuccess(JsonUtils.object(data, classOfT));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    l.requestError("服务器数据异常");
                }
                if (flag) {
                    if (p.getShowsDialog()) {
                        p.dismissAllowingStateLoss();
                    }
                }
            }
        };
    }

    /**
     * 对象返回错误监听
     *
     * @param l    回调
     * @param flag flag true 带进度条 flase不带进度条
     * @param p    进度条的对象
     * @return
     */
    protected <T> Response.ErrorListener responseError(
            final RequestBeanListener<T> l, final boolean flag,
            final LoadingFragment p) {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                    // For AuthFailure, you can re login with user credentials.
                    // For ClientError, 400 & 401, Errors happening on client side when sending api request.
                    // In this case you can check how client is forming the api and debug accordingly.
                    // For ServerError 5xx, you can do retry or handle accordingly.
                    if (error instanceof NetworkError) {
                        l.requestError(BaseConstant.ERROR_NETWORK);
                    } else if (error instanceof ServerError) {
                        l.requestError(BaseConstant.ERROR_SERVICE);
                    } else if (error instanceof AuthFailureError) {
                        l.requestError(BaseConstant.AuthFailureError);
                    } else if (error instanceof ParseError) {
                        l.requestError(BaseConstant.ParseError);
                    } else if (error instanceof NoConnectionError) {
                        l.requestError(BaseConstant.ERROR_NETWORK);
                    } else if (error instanceof TimeoutError) {
                        l.requestError(BaseConstant.Timeout_Error);
                    } else {
                        l.requestError(BaseConstant.ERROR_NETWORK);
                    }
                } else {
                    l.requestError(BaseConstant.ERROR_NETWORK);
                }

                if (flag) {
                    if (p.getShowsDialog()) {
                        p.dismissAllowingStateLoss();
                    }
                }
            }
        };
    }

    /**
     * 成功消息监听 返回String
     *
     * @param l    String 接口
     * @param flag true 带进度条 flase不带进度条
     * @param p    进度条的对象
     * @return
     */
    protected Response.Listener<byte[]> responseListener(
            final RequestListener l, final boolean flag, final LoadingFragment p) {
        return new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] arg0) {
                if (flag) {
                    if (p.getShowsDialog()) {
                        p.dismissAllowingStateLoss();
                    }
                }
                String data = null;
                try {
                    data = new String(arg0, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject mJsonObject = new JSONObject(data);
                    if (mJsonObject != null && !mJsonObject.isNull("errNum")) {
                        String status = mJsonObject.getString("errNum");
                        String errMsg = mJsonObject.getString("retMsg");
                        String message = mJsonObject.getString("retData");
                        if (status.equals(BaseConstant.REQUEST_OK)) {
                            l.requestSuccess(message);
                        } else {
                            l.requestError(errMsg);
                        }
                    } else {
                        l.requestSuccess(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (getJSONType(data) == JSON_TYPE.JSON_TYPE_ARRAY) {
                        l.requestSuccess(data);
                    } else {
                        l.requestError(BaseConstant.ERROR_SERVICE);
                    }
                }
            }
        };
    }

    /**
     * String 返回错误监听
     *
     * @param l    String 接口
     * @param flag true 带进度条 flase不带进度条
     * @param p    进度条的对象
     * @return
     */
    protected Response.ErrorListener responseError(
            final RequestListener l, final boolean flag, final LoadingFragment p) {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (flag) {
                    if (p.getShowsDialog()) {
//                        p.dismiss();
                        p.dismissAllowingStateLoss();
                    }
                }
                if (error != null) {
                    // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                    // For AuthFailure, you can re login with user credentials.
                    // For ClientError, 400 & 401, Errors happening on client side when sending api request.
                    // In this case you can check how client is forming the api and debug accordingly.
                    // For ServerError 5xx, you can do retry or handle accordingly.
                    if (error instanceof NetworkError) {
                        l.requestError(BaseConstant.ERROR_NETWORK);
                    } else if (error instanceof ServerError) {
                        l.requestError(BaseConstant.ERROR_SERVICE);
                    } else if (error instanceof AuthFailureError) {
                        l.requestError(BaseConstant.AuthFailureError);
                    } else if (error instanceof ParseError) {
                        l.requestError(BaseConstant.ParseError);
                    } else if (error instanceof NoConnectionError) {
                        l.requestError(BaseConstant.ERROR_NETWORK);
                    } else if (error instanceof TimeoutError) {
                        l.requestError(BaseConstant.ERROR_NETWORK);
                    } else {
                        l.requestError(BaseConstant.ERROR_NETWORK);
                    }
                } else {
                    l.requestError(BaseConstant.ERROR_NETWORK);
                }
            }
        };
    }

    public void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        30000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
//        if (!NetWorkUtil.isNetworkAvailable(BaseApplication.getApplicationInstance())) {
//            NetworkError mVolleyError = new NetworkError();
//            request.getErrorListener().onErrorResponse(mVolleyError);
//        } else {
        mRequestQueue.add(request);
//        }
    }

    /**
     * 当主页面调用协议 在结束该页面调用此方法
     *
     * @param tag
     */
    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public enum JSON_TYPE {
        /**
         * JSONObject
         */
        JSON_TYPE_OBJECT,
        /**
         * JSONArray
         */
        JSON_TYPE_ARRAY,
        /**
         * 不是JSON格式的字符串
         */
        JSON_TYPE_ERROR
    }

    /***
     * 获取JSON类型
     * 判断规则
     * 判断第一个字母是否为{或[ 如果都不是则不是一个JSON格式的文本
     *
     * @param str
     * @return
     */
    public static JSON_TYPE getJSONType(String str) {
        if (TextUtils.isEmpty(str)) {
            return JSON_TYPE.JSON_TYPE_ERROR;
        }

        final char[] strChar = str.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];

//        LogUtils.d(JSONUtil.class, "getJSONType", " firstChar = "+firstChar);

        if (firstChar == '{') {
            return JSON_TYPE.JSON_TYPE_OBJECT;
        } else if (firstChar == '[') {
            return JSON_TYPE.JSON_TYPE_ARRAY;
        } else {
            return JSON_TYPE.JSON_TYPE_ERROR;
        }
    }
}
