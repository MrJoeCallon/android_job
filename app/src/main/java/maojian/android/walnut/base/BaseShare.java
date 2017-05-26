package maojian.android.walnut.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import maojian.android.walnut.AnyTimeActivity;
import maojian.android.walnut.BaseConstant;
import maojian.android.walnut.utils.ShareUtils;

/**
 * Created by joe on 2017/3/30.
 */
public class BaseShare {
    private IWXAPI api;

    public ShareUtils shareUtils;


    public BaseShare(Context context) {
        api = WXAPIFactory.createWXAPI(context, BaseConstant.wxApi, true);
        api.registerApp(BaseConstant.wxApi);
        shareUtils = new ShareUtils(api, context);
    }


}
