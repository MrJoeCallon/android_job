package maojian.android.walnut.volley;

/**
 * @author hezuzhi
 * @Description: (返回json的请求)
 * @date 2016/5/3  17:35.
 * @version: 1.0
 */
public interface RequestListener  {

    /** 成功 */
    void requestSuccess(String json);

    /** 错误 */
    void requestError(String e);
}
