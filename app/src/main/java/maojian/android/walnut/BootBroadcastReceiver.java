package maojian.android.walnut;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by joe on 2017/6/4.
 */
public class BootBroadcastReceiver  extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        Log.i("TAG", "广播被接收了");

        Intent serviceIntent = new Intent(context, PhoneListenerService.class);

        context.startService(serviceIntent);

    }

}