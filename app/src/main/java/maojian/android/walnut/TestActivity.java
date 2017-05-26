package maojian.android.walnut;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import maojian.android.walnut.utils.ListenedScrollView;
import maojian.android.walnut.utils.OnScrollListener;
import maojian.android.walnut.utils.SlideBar;

public class TestActivity extends Activity {

    private ImageView speed, speed_up_icon, speed_back_icon;
    ListenedScrollView scrollView;
    boolean first = true;
    FrameLayout scrollFrame;
    int scrollInit = 540;
    private int forward_progress = 0, current_mode = 1;
    private int backward_progress = 0;
    private boolean forward_longClick = false;
    private boolean back_longClick = false;
    private TextView mph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test);

        speed = (ImageView) findViewById(R.id.speed);
        speed_up_icon = (ImageView) findViewById(R.id.speed_up_icon);
        speed_back_icon = (ImageView) findViewById(R.id.speed_back_icon);
        mph = (TextView) findViewById(R.id.mph);
        scrollView = (ListenedScrollView) findViewById(R.id.ScrollView2);
        //设置监听。
        scrollView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onBottomArrived() {
                //滑倒底部了
            }

            @Override
            public void onScrollStateChanged(ListenedScrollView view, int scrollState) {
                //滑动状态改变
                if (first) {
                    first = false;
                    return;
                }
                Log.d("滑动状态改变", "scrollState=" + scrollState);
                if (scrollState == 0) {
                    mph.setText("0");
                    speed_up_icon.setVisibility(View.VISIBLE);
                    speed.setImageResource(R.drawable.zyfspeedup);
                    speed_back_icon.setVisibility(View.GONE);
                    mph.setTextColor(getResources().getColor(R.color.C1));
                    scrollView.scrollTo(0, scrollInit);//725   6
                    if (forward_longClick) {
                        //停止向前
                        stopforword();
                    }
                    if (back_longClick) {
                        //停止向后
                        stopback();
                    }
                }
            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                //滑动位置改变
                Log.d("滑动位置改变", "oldl=" + oldl + "  oldt=" + oldt + "first=" + first);
                if (first || (oldl == 0 && oldt == 0 && forward_longClick == false && back_longClick == false)) {
                    return;
                }

                if (oldt >= scrollInit) {//在前进
                    if (back_longClick) {
                        //停止向后
                        stopback();
                    }
                    speed_up_icon.setVisibility(View.VISIBLE);
                    speed.setImageResource(R.drawable.zyfspeedup);
                    speed_back_icon.setVisibility(View.GONE);

                    forward_longClick = true;
                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    backward_progress = 1;
                    forward_progress = (int) ((oldt - scrollInit) / 5.2);
                    setMph(forward_progress / 10);
//                    mph.setText(forward_progress / 10 + "");
                    mph.setTextColor(getResources().getColor(R.color.C1));
                    Log.d("滑动位置改变", "forward_progress=" + forward_progress);
//                    new Thread(forward_runnable).start();

                    Log.e("videodebug", "longpressing");
                } else {
                    if (forward_longClick) {
                        //停止向前
                        stopforword();
                    }
                    mph.setTextColor(getResources().getColor(R.color.C8));
                    speed_up_icon.setVisibility(View.GONE);
                    speed_back_icon.setVisibility(View.VISIBLE);
                    speed.setImageResource(R.drawable.zyfspeeddown);
//                    startmode = 3;
                    back_longClick = true;
                    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    forward_progress = 1;
                    backward_progress = (int) ((scrollInit - oldt) / 5.2);
                    setMph(backward_progress / 10);
//                    mph.setText(backward_progress / 10 + "");
//                    new Thread(back_runnable).start();

                    Log.e("videodebug", "longpressing");
                }
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                first = true;
                scrollView.scrollTo(0, scrollInit);
            }
        }, 200);
    }

    private void stopback() {
        back_longClick = false;
//        new Thread(back_runnable).start();
    }


    private void stopforword() {
        forward_longClick = false;
//        new Thread(forward_runnable).start();
    }

    public void setMph(int mphw) {
        switch (current_mode) {
            case 01:
                mph.setText(mphw + "");
                break;
            case 02:
                int kmh = (int) (mphw * 1.7);
                mph.setText(kmh + "");
                break;
            case 03:
                int kmh1 = (int) (mphw * 2.4);
                mph.setText(kmh1 + "");
                break;
        }
    }


    int count = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        int action = event.getAction();

        if (action ==KeyEvent.ACTION_DOWN) {

            Log.e("volume key debug","ACTION_DOWN "+count);

//            return true;
        }


        if (action == KeyEvent.ACTION_UP) {
            scrollView.setInTouch(true);
            Log.e("volume key debug", "ACTION_UP " + count);
            count = 0;


//            return true;
        }

        return super.dispatchKeyEvent(event);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {

            case KeyEvent.KEYCODE_VOLUME_DOWN:

                scrollView.setInTouch(first);
                count--;
                Log.e("volume key debug", "KEYCODE_VOLUME_DOWN " + count);
                return true;

            case KeyEvent.KEYCODE_VOLUME_UP:

                scrollView.setInTouch(true);
                count++;
                Log.e("volume key debug", "KEYCODE_VOLUME_UP " + count);
                return true;
            case KeyEvent.KEYCODE_VOLUME_MUTE:
                mph.setText("MUTE");

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//
//
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//
//                scrollView.setInTouch(first);
//                count--;
//                Log.e("volume key debug", "KEYCODE_VOLUME_DOWN " + count);
//                return true;
//
//            case KeyEvent.KEYCODE_VOLUME_UP:
//
//                scrollView.setInTouch(true);
//                count++;
//                Log.e("volume key debug", "KEYCODE_VOLUME_UP " + count);
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_MUTE:
//                return true;
//        }
//
//
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//
//        int action = event.getAction();
//
////        if (action ==KeyEvent.ACTION_DOWN) {
////            count++;
////            Log.e("volume key debug","ACTION_DOWN "+count);
////
////            return true;
////        }
//
//
//        if (action == KeyEvent.ACTION_UP) {
//            scrollView.setInTouch(true);
//            Log.e("volume key debug", "ACTION_UP " + count);
//
//            return true;
//        }
//
//        return super.dispatchKeyEvent(event);
//    }
}
