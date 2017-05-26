package maojian.android.walnut;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestPasswordResetCallback;
import maojian.android.walnut.login.LoginBean;
import maojian.android.walnut.login.LoginPresenter;
import maojian.android.walnut.login.LoginView;

public class ForgetPasswordActivity extends AnyTimeActivity implements LoginView {
    LoginPresenter loginPresenter;
    EditText emailText;
    Button findPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
//		this.getActionBar().setDisplayHomeAsUpEnabled(true);
        loginPresenter = new LoginPresenter(this, this);
        emailText = (EditText) findViewById(R.id.editText_forget_password_email);
        findPasswordButton = (Button) findViewById(R.id.button_find_password);
        findPasswordButton.setOnClickListener(findPasswordListener);
        setTitle("Forgot Password");
    }

    @Override
    public void onClickEvent(View v) {

    }

    OnClickListener findPasswordListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String email = emailText.getText()
                    .toString();
            if (email != null) {
                loginPresenter.forgetPassword(email);
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
    public void setLoginSuccess(LoginBean mLoginBean, String logoinType) {
        finish();
    }

    @Override
    public void setLoginFail(String errInfo) {

    }
}
