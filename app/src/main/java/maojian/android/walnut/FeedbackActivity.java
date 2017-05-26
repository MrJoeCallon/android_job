package maojian.android.walnut;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.me.LinkAccountBean;
import maojian.android.walnut.volley.RequestBeanListener;
import maojian.android.walnut.volley.RequestListener;
import maojian.android.walnut.volley.RequestParams;
import maojian.android.walnut.volley.VolleyRequest;

/**
 * Created by android on 19/9/16.
 */
public class FeedbackActivity extends AnyTimeActivity {
    private VolleyRequest mVolleyRequest;
    EditText searchEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contact);

        mVolleyRequest = new VolleyRequest();
        setTitle("Sending Report");
        final Button postButton = (Button) findViewById(R.id.feedback_sendbutton);
        searchEdit = (EditText) findViewById(R.id.feedback_edittext);
        searchEdit.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.length() > 0) {
                            postButton.setTextColor(Color.parseColor("#00EAAB"));
                            postButton.setEnabled(true);

                        } else {
                            postButton.setTextColor(Color.parseColor("#E4E4E4"));
                            postButton.setEnabled(false);
                        }


                    }
                }
        );

        postButton.setEnabled(false);

        postButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        postButton.setEnabled(false);

                        post();

                    }
                }
        );


    }

    private void post() {
        String content = searchEdit.getText().toString();
        // 发送请求
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("title", content);
        params.put("content", content);

        mVolleyRequest.post(this, BaseConstant.feedback, params, "", new RequestListener() {
            @Override
            public void requestSuccess(String json) {
                if (mVolleyRequest != null) {
                    Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void requestError(String e) {
                if (mVolleyRequest != null)
                    Toast.makeText(FeedbackActivity.this, e, Toast.LENGTH_LONG).show();
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

    }


}
