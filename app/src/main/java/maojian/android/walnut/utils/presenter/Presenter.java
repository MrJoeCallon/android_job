package maojian.android.walnut.utils.presenter;

import android.content.Context;
import maojian.android.walnut.volley.RequestManager;
import maojian.android.walnut.volley.VolleyRequest;


/**
 * @author hezuzhi
 * @Description: (MVP中的P)
 * @date 2016/4/13.
 * @version: 1.0
 */
public class Presenter<E> {
    /**
     * MVP中P负责更新的视图
     */
    protected E mView = null;
    /**
     * activity 中用this
     */
    protected Context context;

    /**
     * Volley请求对象
     */
    protected VolleyRequest mVolleyRequest;

    public VolleyRequest getVolleyRequest() {
        return mVolleyRequest;
    }


    /**
     * 构造函数
     *
     * @param view 要绑定的视图
     */
    public Presenter(Context context, E view) {
        this.context = context;
        this.mView = view;
        mVolleyRequest = new VolleyRequest();
    }

    /**
     * 绑定视图
     *
     * @param view 要绑定的视图
     */
    public void attachView(E view) {
        this.mView = view;
    }

    /**
     * 解绑视图，取消网络请求
     */
    public void detachView() {
        RequestManager.cancelAll(context);
        this.mView = null;
    }
}
