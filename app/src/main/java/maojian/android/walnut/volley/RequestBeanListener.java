package maojian.android.walnut.volley;


/**
 * @author hezuzhi
 * @Description: (返回对象的请求)
 * @date 2016/5/3  17:35.
 * @version: 1.0
 */
public interface RequestBeanListener<T> {
    /**
     * 成功
     *
     * @param result
     */
    void requestSuccess(T result);

    /**
     * 错误
     */
    void requestError(String e);
}

