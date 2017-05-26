package maojian.android.walnut;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;

public abstract class AnyTimeActivity extends FragmentActivity implements
        View.OnClickListener {

    public AnyTimeActivity activity;
    private String userId;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        userId = null;
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getObjectId();
        }

    }

    public void setTitle(String title) {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        if (tvTitle != null) {
            Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/Brown-Regular.otf");
            tvTitle.setTypeface(face1);
            tvTitle.setText(title);
        }
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.iv_back && findViewById(R.id.iv_back) != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
            if (isOpen) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//从控件所在的窗口中隐藏
            }
            finish();
        }
        onClickEvent(v);
    }


    public void getEditCustomDialog() {
        AlertDialog alertDialog;
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.include_login1, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("A New Version is Available");

        alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * onclick事件
     */
    public abstract void onClickEvent(View v);

    public String getUserId() {
        return userId;
    }

    protected void showError(String errorMessage) {
        showError(errorMessage, activity);
    }

    public void showError(String errorMessage, Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(
                        activity.getResources().getString(
                                R.string.dialog_message_title))
                .setMessage(errorMessage)
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }
}
