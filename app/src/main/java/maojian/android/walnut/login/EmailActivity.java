package maojian.android.walnut.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestPasswordResetCallback;
import maojian.android.walnut.*;
import maojian.android.walnut.data.UserInfos;

public class EmailActivity extends AnyTimeActivity implements LoginView{
    EditText emailText;
    Button findPasswordButton;
    private LoginBean mLoginBean;
    private LoginPresenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        loginPresenter = new LoginPresenter(this,this);
        mLoginBean = (LoginBean)getIntent().getSerializableExtra("mLoginBean");
        emailText = (EditText) findViewById(R.id.editText_forget_password_email);
        findPasswordButton = (Button) findViewById(R.id.button_find_password);
        findPasswordButton.setOnClickListener(findPasswordListener);
        setTitle("Sing Up");
    }

    @Override
    public void onClickEvent(View v) {

    }

    View.OnClickListener findPasswordListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String email = emailText.getText()
                    .toString();
            if (email != null) {
                loginPresenter.otherRegister(mLoginBean.getLogoinType(),mLoginBean.getOpenid(),mLoginBean.getUser_name(),
                        mLoginBean.getHeader_image(),email);
            } else {
                showError(activity.getResources().getString(
                        R.string.error_register_email_address_null));
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent LoginIntent = new Intent(this, LoginActivity.class);
            startActivity(LoginIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setLoginSuccess(LoginBean mLoginBean,String logoinType) {
        UserInfos.setLoginBean(EmailActivity.this, mLoginBean);
        startActivity(new Intent(EmailActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void setLoginFail(String errInfo) {

    }
}
