package maojian.android.walnut.me;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import maojian.android.walnut.AnyTimeActivity;
import maojian.android.walnut.BaseConstant;
import maojian.android.walnut.R;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.home.addpost.LocationBean;
import maojian.android.walnut.me.userdate.UserBean;
import maojian.android.walnut.volley.RequestBeanListener;
import maojian.android.walnut.volley.RequestListener;
import maojian.android.walnut.volley.RequestParams;
import maojian.android.walnut.volley.VolleyRequest;

import java.util.HashMap;

public class LinkAccountActivity extends AnyTimeActivity {
    private VolleyRequest mVolleyRequest;
    private TextView tv_weibo, tv_facebook, tv_twitter, tv_wechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_account);
        setTitle("Linked Account");
        mVolleyRequest = new VolleyRequest();
        init();
        getLinkAccount();
    }

    private void init() {
        findViewById(R.id.facebook).setOnClickListener(this);
        findViewById(R.id.twitter).setOnClickListener(this);
        findViewById(R.id.wechat).setOnClickListener(this);
        findViewById(R.id.weibo).setOnClickListener(this);
        tv_weibo = (TextView) findViewById(R.id.tv_weibo);
        tv_facebook = (TextView) findViewById(R.id.tv_facebook);
        tv_twitter = (TextView) findViewById(R.id.tv_twitter);
        tv_wechat = (TextView) findViewById(R.id.tv_wechat);

    }

    private void getLinkAccount() {
        // 发送请求
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        mVolleyRequest.post(this, BaseConstant.bindingInfo, LinkAccountBean.class, params, "", new RequestBeanListener<LinkAccountBean>() {
            @Override
            public void requestSuccess(LinkAccountBean result) {
                if (mVolleyRequest != null && result != null) {
                    if (result.getFacebook() == 0)
                        tv_facebook.setTextColor(getResources().getColor(R.color.C5));
                    if (result.getTwitter() == 0)
                        tv_twitter.setTextColor(getResources().getColor(R.color.C5));
                    if (result.getWechat() == 0)
                        tv_wechat.setTextColor(getResources().getColor(R.color.C5));
                    if (result.getWeibo() == 0)
                        tv_weibo.setTextColor(getResources().getColor(R.color.C5));
                }
            }

            @Override
            public void requestError(String e) {
                if (mVolleyRequest != null) {
                    Toast.makeText(LinkAccountActivity.this, e, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVolleyRequest = null;
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.facebook:
                authorize(Facebook.NAME);
                break;
            case R.id.twitter:
                authorize(Twitter.NAME);
                break;
            case R.id.wechat:
                authorize(Wechat.NAME);
                break;
            case R.id.weibo:
                authorize(SinaWeibo.NAME);
                break;

        }
    }

    private void authorize(final String name) {
        Platform weibo = ShareSDK.getPlatform(name);
//回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        weibo.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub
                arg2.printStackTrace();
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> platform) {
                // TODO Auto-generated method stub
                //输出所有授权信息
                arg0.getDb().exportData();
                String openid = "";
                String type ;
                if (name.equals("SinaWeibo")) {
                    type  = "4";
                    openid = platform.get("id").toString();
                } else if (name.equals("Twitter")) {
                    type  = "2";
                    openid = platform.get("id").toString();
                } else if (name.equals("Facebook")) {
                    type  = "1";
                    openid = platform.get("id").toString();//"third_party_id" -> "dinWr3chU0ejfVPpdZDXtkREXDc"
                } else {
                    type  = "3";
                    openid = platform.get("openid").toString();
                }
                editBindingInfo(type,openid);

            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });
//authorize与showUser单独调用一个即可
        weibo.authorize();//单独授权,OnComplete返回的hashmap是空的
        weibo.showUser(null);//授权并获取用户信息
//移除授权
//weibo.removeAccount(true);
    }

    private void editBindingInfo(final String logoinType, String openid) {
// 发送请求
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("openid", openid);
        params.put("logoinType", logoinType);
        mVolleyRequest.post(this, BaseConstant.editBindingInfo, params, "", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                if (mVolleyRequest != null) {
                    Toast.makeText(LinkAccountActivity.this, "绑定成功", Toast.LENGTH_LONG).show();
                    if (logoinType.equals("1"))
                        tv_facebook.setTextColor(getResources().getColor(R.color.C2));
                    if (logoinType.equals("2"))
                        tv_twitter.setTextColor(getResources().getColor(R.color.C2));
                    if (logoinType.equals("3"))
                        tv_wechat.setTextColor(getResources().getColor(R.color.C2));
                    if (logoinType.equals("4"))
                        tv_weibo.setTextColor(getResources().getColor(R.color.C2));
                }
            }

            @Override
            public void requestError(String e) {
                if (mVolleyRequest != null)
                    Toast.makeText(LinkAccountActivity.this, e, Toast.LENGTH_LONG).show();
            }
        });
    }
}
