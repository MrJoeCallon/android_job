package maojian.android.walnut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.demo.LoginPage;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        doSplashTask();
    }

    private void doSplashTask() {
        Intent serviceIntent = new Intent(this, PhoneListenerService.class);

        startService(serviceIntent);
        // 延时800ms执行
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                if (UserInfos.getLoginBean() != null) {
                    intent.setClass(getApplicationContext(), DeviceActivity.class);
                } else {
                    intent.setClass(getApplicationContext(), LoginActivity.class);
                }
//                intent.setClass(getApplicationContext(), TestActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();


            }
        }, 1000);
    }
}
