package maojian.android.walnut.home.addpost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import maojian.android.walnut.AnyTimeActivity;
import maojian.android.walnut.R;
import maojian.android.walnut.discover.search.UserListView;
import maojian.android.walnut.utils.JsonUtils;
import maojian.android.walnut.utils.ToastUtil;
import maojian.android.walnut.utils.eventbus.AddLocationEvent;
import maojian.android.walnut.volley.RequestListener;
import org.greenrobot.eventbus.EventBus;

public class AddLocationActivity extends AnyTimeActivity implements AddPostView, TextWatcher {
    private LocationBean locationBean;
    private LocationAdapter locationAdapter;
    private ListView listView;
    EditText etInput;
    private AddPostPresenter addPostPresenter;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        setTitle("Select the address");
        location = getIntent().getStringExtra("location");
        locationBean = (LocationBean) getIntent().getSerializableExtra("locationBean");
        listView = (ListView) findViewById(R.id.listview);
        etInput = (EditText) findViewById(R.id.et_search);
        locationAdapter = new LocationAdapter(this, locationBean.getResultsList(), location);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new AddLocationEvent(locationBean.getResults().get(position).getName()));
                finish();
            }
        });
        listView.setAdapter(locationAdapter);
        addPostPresenter = new AddPostPresenter(this, this);
        etInput.addTextChangedListener(this);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) etInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    doSearch(etInput.getText().toString().trim());

                    return true;
                }
                return false;
            }


        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先隐藏键盘
                ((InputMethodManager) etInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(
                                getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        doSearch(text);
    }

    private void doSearch(String trim) {
        addPostPresenter.addLocation(trim, new RequestListener() {
            @Override
            public void requestSuccess(String locationString) {
                LocationBean locationBeanNew = JsonUtils.object(locationString, LocationBean.class);
                if (locationBeanNew.getStatus().equals("OK")) {
                    locationBean = locationBeanNew;
                    locationAdapter = new LocationAdapter(AddLocationActivity.this, locationBean.getResultsList(), location);
                    listView.setAdapter(locationAdapter);
                } else
                    ToastUtil.show(AddLocationActivity.this, "Get Location error");
            }

            @Override
            public void requestError(String e) {

            }
        });


    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void setUpLoadPicSuccess(UpLoadPicBean upLoadPicBean) {

    }

    @Override
    public void setUpLoadPicFail(String errInfo) {

    }
}
