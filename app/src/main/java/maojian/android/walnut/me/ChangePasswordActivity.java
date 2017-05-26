package maojian.android.walnut.me;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.avos.avoscloud.AVAnalytics;
import maojian.android.walnut.AnyTimeActivity;
import maojian.android.walnut.BaseConstant;
import maojian.android.walnut.R;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.volley.RequestListener;
import maojian.android.walnut.volley.RequestParams;
import maojian.android.walnut.volley.VolleyRequest;

public class ChangePasswordActivity extends AnyTimeActivity {
    /**
     * Volley请求对象
     */
    protected VolleyRequest mVolleyRequest;
    protected EditText currentpassword, newpassword, newpassword1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle("ChangePassword");
        mVolleyRequest = new VolleyRequest();
        findViewById(R.id.epsave).setOnClickListener(this);
        currentpassword = (EditText) findViewById(R.id.currentpassword);
        newpassword = (EditText) findViewById(R.id.newpassword);
        newpassword1 = (EditText) findViewById(R.id.newpassword1);
    }

    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.epsave:
                String current_password = currentpassword.getText().toString();

                if (TextUtils.isEmpty(current_password)) {
                    Toast.makeText(ChangePasswordActivity.this, "请输入您的原始密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                String new_password = newpassword.getText().toString();
                if (TextUtils.isEmpty(new_password)) {
                    Toast.makeText(ChangePasswordActivity.this, "请输入您的新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String newpassword = newpassword1.getText().toString();
                if (TextUtils.isEmpty(newpassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "请再次输入您的新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newpassword.equals(new_password)) {
                    Toast.makeText(ChangePasswordActivity.this, "二次新密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestParams params = new RequestParams();
                if (UserInfos.getLoginBean() != null)
                    params.put("user_id", UserInfos.getLoginBean().getUser_id());
                params.put("current_password", current_password + "");
                params.put("new_password", new_password + "");

                // 发送请求
                mVolleyRequest.post(ChangePasswordActivity.this, BaseConstant.updatePwd, params, "", new RequestListener() {
                    @Override
                    public void requestSuccess(String json) {
                        Toast.makeText(ChangePasswordActivity.this, "修改密码成功", Toast.LENGTH_LONG).show();
                        ChangePasswordActivity.this.finish();
                    }

                    @Override
                    public void requestError(String e) {
                        Toast.makeText(ChangePasswordActivity.this, e, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }


    }
}
