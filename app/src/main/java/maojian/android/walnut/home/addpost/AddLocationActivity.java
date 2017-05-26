package maojian.android.walnut.home.addpost;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import maojian.android.walnut.AnyTimeActivity;
import maojian.android.walnut.R;

public class AddLocationActivity extends AnyTimeActivity {
    private LocationBean locationBean;
    private LocationAdapter locationAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        setTitle("选择地址");
        locationBean = (LocationBean) getIntent().getSerializableExtra("locationBean");
        listView = (ListView) findViewById(R.id.listview);
        locationAdapter = new LocationAdapter(this, locationBean.getResult().getPois());
        listView.setAdapter(locationAdapter);
    }

    @Override
    public void onClickEvent(View v) {

    }
}
