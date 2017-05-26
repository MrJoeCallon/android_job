package maojian.android.walnut.discover.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import maojian.android.walnut.*;
import maojian.android.walnut.me.userdate.UserBean;
import maojian.android.walnut.message.MessageListBean;
import maojian.android.walnut.utils.SharedPreferencesUtils;
import maojian.android.walnut.volley.RequestListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SearchActivity extends AnyTimeActivity implements UserListView, TextWatcher {
    private List<UserListBean.UserBean> userBean;
    private SearchPresenter searchPresenter;
    private ReFlashListView mlistview;
    private UserListAdapter userListAdapter;
    EditText etInput;
    //用户历史点击
    private UserListBean userHisBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchPresenter = new SearchPresenter(this, this);
        etInput = (EditText) findViewById(R.id.et_search);
        mlistview = ((ReFlashListView) findViewById(R.id.listview));
        userListAdapter = new UserListAdapter(this);
        mlistview.setAdapter(userListAdapter);
        userHisBean = SharedPreferencesUtils.getBean(this, "UserListBean", UserListBean.class);
        if (userHisBean != null && userHisBean.getData() != null) {
            userBean = userHisBean.getData();
            userListAdapter.notifyDataSetChanged();
        }
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
        searchPresenter.search(0, trim);
    }

    @Override
    public void onClickEvent(View v) {

    }

    @Override
    public void setSuccess(UserListBean userListBean) {
        userBean = userListBean.getData();
        userListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setFail(String errInfo) {

    }

    private class UserListAdapter extends BaseAdapter {


        private LayoutInflater inflater;


        private DisplayImageOptions options;
        private DisplayImageOptions postoptions;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        UserListAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                    .build();
            postoptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            if (userBean != null)
                return userBean.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (view == null && inflater != null) {
                view = inflater.inflate(R.layout.search_listitem, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.message_userprofile);
                holder.username = (TextView) view.findViewById(R.id.message_username);

                Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/Brown-Light.otf");
                holder.username.setTypeface(face1);

                holder.itemview = (LinearLayout) view.findViewById(R.id.itemview);
                holder.messagepost = (ImageView) view.findViewById(R.id.message_post);
                view.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }
            Typeface face2 = Typeface.createFromAsset(getAssets(), "fonts/Brown-Regular.otf");

            final UserListBean.UserBean post = (UserListBean.UserBean) userBean.get(position);


            holder.username.setText(post.getClass_name());

//                holder.messagecontent.setText(post.getNum());

            if (post.getIs_follow() == 0) {//没有关注
                Log.e("message", "followpost" + position);

                holder.messagepost.setImageResource(R.drawable.follow);
                //holder.messagepost.setBackgroundColor(Color.parseColor("#FFFFFF"));

            } else if (post.getIs_follow() == 1) {//已经关注
                holder.messagepost.setImageResource(R.drawable.following);
            }

            ImageLoader.getInstance().displayImage(userBean.get(position).getClass_icon(), holder.imageView, options, animateFirstListener);

            holder.messagepost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (post.getIs_follow() == 0) {
                        searchPresenter.removeFollow(1, post.getClass_id(), new RequestListener() {
                            @Override
                            public void requestSuccess(String json) {
                                userBean.get(position).setIs_follow(1);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void requestError(String e) {

                            }
                        });
                    } else {
                        addPostHis(post);

                        Intent intent = new Intent(SearchActivity.this, customProfileActivity.class);
                        intent.putExtra("ObjectId", post.getClass_id());
                        intent.putExtra("user", UserBean.UserBeanTo(post));
                        startActivity(intent);
                    }
                }
            });
            holder.itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPostHis(post);

                    Intent intent = new Intent(SearchActivity.this, customProfileActivity.class);
                    intent.putExtra("ObjectId", post.getClass_id());
                    intent.putExtra("user", UserBean.UserBeanTo(post));
                    startActivity(intent);
                }
            });


            return view;
        }
    }

    private void addPostHis(UserListBean.UserBean post) {
        if (userHisBean == null)
            userHisBean = new UserListBean();
        if (userHisBean.getData() != null) {
            for (UserListBean.UserBean userBean : userHisBean.getData()) {
                if (userBean.getClass_id().equals(post.getClass_id())) {
                    userHisBean.getData().remove(userBean);
                    break;
                }
            }
        } else {
            userHisBean.setData(new ArrayList<UserListBean.UserBean>());
        }
        userHisBean.getData().add(post);
        SharedPreferencesUtils.setBean(SearchActivity.this, "UserListBean", userHisBean);
    }

    static class ViewHolder {
        ImageView imageView;
        TextView username;
        LinearLayout itemview;
        ImageView messagepost;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
