package maojian.android.walnut;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.me.OrderList;
import maojian.android.walnut.volley.RequestBeanListener;
import maojian.android.walnut.volley.RequestParams;
import maojian.android.walnut.volley.VolleyRequest;

import java.util.List;

/**
 * Created by android on 25/10/16.
 */
public class OrderActivity extends Activity {

    ImageButton order_backbutton;

    LinearLayout order_view;
    LinearLayout repair_view;

    TextView order_devicename;
    TextView order_devicecolor;
    TextView order_number;
    TextView order_date;
    TextView order_status;

    TextView repair_devicename;
    TextView repair_devicecolor;
    TextView repair_number;
    TextView repair_date;
    TextView repair_status;
    /**
     * Volley请求对象
     */
    protected VolleyRequest mVolleyRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_order);

        mVolleyRequest = new VolleyRequest();
        order_backbutton = (ImageButton) findViewById(R.id.order_backbutton);
        order_backbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderActivity.this.finish();
                    }
                }
        );

        order_view = (LinearLayout) findViewById(R.id.order_view);
        repair_view = (LinearLayout) findViewById(R.id.repair_view);

//        order_view.setVisibility(View.GONE);
//        repair_view.setVisibility(View.GONE);

        order_devicename = (TextView) findViewById(R.id.order_devicename);
        order_devicecolor = (TextView) findViewById(R.id.order_devicecolor);
        order_number = (TextView) findViewById(R.id.order_number);
        order_date = (TextView) findViewById(R.id.order_date);
        order_status = (TextView) findViewById(R.id.order_status);

        repair_devicename = (TextView) findViewById(R.id.repair_devicename);
        repair_devicecolor = (TextView) findViewById(R.id.repair_devicecolor);
        repair_number = (TextView) findViewById(R.id.repair_number);
        repair_date = (TextView) findViewById(R.id.repair_date);
        repair_status = (TextView) findViewById(R.id.repair_status);


//        order_view.setVisibility(View.GONE);
        repair_view.setVisibility(View.GONE);

//        getWallImages();
        getOrderlist();
    }


    public void getOrderlist() {
        RequestParams params = new RequestParams();
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        String url = BaseConstant.orderList;
        // 发送请求
        mVolleyRequest.post(this, url, OrderList.class, params, "", new RequestBeanListener<OrderList>() {
            @Override
            public void requestSuccess(OrderList result) {
                if (result != null) {

                }
            }

            @Override
            public void requestError(String e) {
                Toast.makeText(OrderActivity.this, "请求接口出错", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWallImages() {

        AVQuery<AVObject> discoverpostquery = new AVQuery<>("Order_repair");
        discoverpostquery.whereEqualTo("User", AVUser.getCurrentUser());
        discoverpostquery.orderByDescending("createdAt");


        discoverpostquery.findInBackground(new FindCallback<AVObject>() {
                                               @Override
                                               public void done(List<AVObject> list, AVException e) {
                                                   int coupon_count = 0;
                                                   if (list != null) {
                                                       for (int i = 0; i < list.size(); i++) {
                                                           final AVObject post = (AVObject) list.get(i);

                                                            if(post.get("type").equals("order")){

                                                                order_view.setVisibility(View.VISIBLE);

                                                                order_devicename.setText(post.get("Device").toString());
                                                                order_devicecolor.setText(post.get("Color").toString());
                                                                order_number.setText("Order Number: "+post.get("ordernumber"));
                                                                order_status.setText(post.get("Status").toString());
                                                                order_date.setText(post.get("OrderTime").toString());
                                                            }

                                                           if(post.get("type").equals("repair")){

                                                               repair_view.setVisibility(View.VISIBLE);

                                                               repair_devicename.setText(post.get("Device").toString());
                                                               repair_devicecolor.setText(post.get("Color").toString());
                                                               repair_number.setText("Order Number: "+post.get("ordernumber"));
                                                               repair_status.setText(post.get("Status").toString());
                                                               repair_date.setText(post.get("OrderTime").toString());
                                                           }


                                                       }

                                                   }
                                                   else{
                                                       order_view.setVisibility(View.GONE);
                                                       repair_view.setVisibility(View.GONE);

                                                   }
                                               }
                                           });

    }


}
