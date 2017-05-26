package maojian.android.walnut;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import maojian.android.walnut.home.comment.CommentList;
import maojian.android.walnut.home.comment.CommentPresenter;
import maojian.android.walnut.home.comment.CommentView;
import maojian.android.walnut.me.userdate.UserBean;
import maojian.android.walnut.utils.TimeUtil;
import maojian.android.walnut.volley.RequestListener;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by android on 29/7/16.
 */
public class CommentActivity extends AnyTimeActivity implements CommentView {

    private final String[] profileUrl = new String[100];

    public List<CommentList.CommentBean> postobjectArray;

    private iMessageAdapter messageadapter;
    private ListView lv;
    private CommentPresenter commentPresenter;

    //    private AVObject commentObject;
    private String post_id;
    Button postButton;
    EditText searchEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comment);
        commentPresenter = new CommentPresenter(this, this);


        setTitle("Comment");
        findViewById(R.id.comment_img).setOnClickListener(this);
        postButton = (Button) findViewById(R.id.comment_send);
        postButton.setOnClickListener(this);
        searchEdit = (EditText) findViewById(R.id.comment_edit);
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

//        postButton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                        AVObject likeobject = new AVObject("Activity");
//                        likeobject.put("type", "comment");
//                        likeobject.put("content", searchEdit.getText().toString());
//                        likeobject.put("fromUser", AVUser.getCurrentUser());
//                        likeobject.put("toUser", commentObject.get("user"));
//                        likeobject.put("photo", commentObject);
//
//                        Log.e("abc", "post send click " + commentObject.get("") + " content: " + searchEdit.getText());
//
//                        Log.e("abc", "ddd??? " + commentObject.get("user"));
//
//                        AVACL likeACL = new AVACL();
//                        likeACL.setPublicReadAccess(true);// 设置公开的「读」权限，任何人都可阅读
//                        //likeACL.setWriteAccess(AVUser.getCurrentUser(), true);//为当前用户赋予「写」权限
//
//                        likeobject.setACL(likeACL);
//
//                        likeobject.saveInBackground(new SaveCallback() {
//                            @Override
//                            public void done(AVException e) {
//                                if (e == null) {
//                                    // 存储成功
//                                    Log.e("abc", "commentObject success");
//
//                                    AVQuery pushQuery = AVInstallation.getQuery();
//                                    pushQuery.whereEqualTo("owner", commentObject.get("user"));
//
//                                    AVPush.sendMessageInBackground(AVUser.getCurrentUser().getUsername() + " commented your post", pushQuery, new SendCallback() {
//                                        @Override
//                                        public void done(AVException e) {
//                                            Log.e("abc", "comment push done");
//                                        }
//                                    });
//
//                                    getWallImages(0);
//
//
//                                } else {
//                                    Log.e("abc", "error" + e);
//                                    // 失败的话，请检查网络环境以及 SDK 配置是否正确
//                                }
//                            }
//                        });
//
//                    }
//                }
//        );

        Intent intent = getIntent();

        post_id = intent.getStringExtra("ObjectId");

//        commentObject = AVObject.createWithoutData("Photo", objectID);
//
//        final AVQuery commentquery = new AVQuery("Photo");
//        commentquery.whereEqualTo("objectId",objectID);
//        commentquery.include("user");
//
//        commentquery.findInBackground(
//                new FindCallback() {
//                    @Override
//                    public void done(List list, AVException e) {
//                        if(list!=null){
//                            commentObject = (AVObject)list.get(0);
//                        }
//                    }
//                }
//        );
//
//        Log.e("abc", "00ddd??? " + commentObject.get("user"));


        lv = (ListView) findViewById(R.id.comment_listview);

        getWallImages(0);

    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.comment_img:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                if (isOpen) {
                    imm.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);//从控件所在的窗口中隐藏
                }else {
                    finish();
                }
                break;
            case R.id.comment_send:
                final String content = searchEdit.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                commentPresenter.addComment(content, post_id, new RequestListener() {
                    @Override
                    public void requestSuccess(String json) {
                        Log.d("addComment", json);
                        Toast.makeText(CommentActivity.this, "Comment success", Toast.LENGTH_SHORT).show();
                        searchEdit.setText("");
                        getWallImages(0);
                    }

                    @Override
                    public void requestError(String e) {
                        if (TextUtils.isEmpty(e))
                            e = "Comment fail";
                        Toast.makeText(CommentActivity.this, e, Toast.LENGTH_SHORT).show();
                        searchEdit.setText(content);
                    }
                });
                break;
        }
    }

    @Override
    public void setSuccess(CommentList commentList) {
        if (commentList != null && commentList.getData() != null && commentList.getData().size() > 0) {
            postobjectArray = commentList.getData();
            messageadapter = new iMessageAdapter(CommentActivity.this);
            // lv.setAdapter(messageadapter);
            //   messageadapter.notifyDataSetChanged();

            lv.setAdapter(messageadapter);
        }
    }

    @Override
    public void setFail(String errInfo) {
        if (!TextUtils.isEmpty(errInfo))
            Toast.makeText(CommentActivity.this, errInfo, Toast.LENGTH_SHORT).show();
    }

    private class iMessageAdapter extends BaseAdapter {

        private String[] IMAGE_URLS = profileUrl;


        private LayoutInflater inflater;


        private DisplayImageOptions options;
        private DisplayImageOptions postoptions;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        iMessageAdapter(Context context) {
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
            return postobjectArray.size();
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
            //         if (view == null&& inflater != null) {
            view = inflater.inflate(R.layout.comment_listitem, parent, false);
            holder = new ViewHolder();
            assert view != null;
            holder.imageView = (ImageView) view.findViewById(R.id.comment_userprofile);
            holder.username = (TextView) view.findViewById(R.id.comment_username);
            holder.messagecontent = (TextView) view.findViewById(R.id.comment_content);

            holder.messagepost = (ImageView) view.findViewById(R.id.message_post);
            view.setTag(holder);

//            } else {
//                holder = (ViewHolder) view.getTag();
//            }

            holder.comment_timelabel = (TextView) view.findViewById(R.id.comment_timelabel);

            if (postobjectArray.size() > 0) {

                final CommentList.CommentBean post = postobjectArray.get(position);

//                Date curDate = new Date(System.currentTimeMillis());
//
//                long difference = post.getCreatedAt().getTime() - curDate.getTime();
//                Log.v("Time difference:", String.valueOf(difference));
//
//                String timediff = "today";
//
//                if ((-difference) < 60 * 1000) {
//                    timediff = (-difference / 1000) + " s";
//                    Log.e("abc", "timedebug " + timediff + "  " + difference);
//                } else if ((-difference) < 60 * 60 * 1000) {
//                    timediff = (-difference / 1000 / 60) + " m";
//
//                } else if ((-difference) < 60 * 60 * 24 * 1000) {
//                    timediff = (-difference / 1000 / 60 / 60) + " h";
//                } else {
//                    //Log.e("timedebugging","??? "+(int)(-difference/1000/60/60/24)+"difff "+difference);
//                    timediff = (-difference / 1000 / 60 / 60 / 24) + " d";
//                }

//                final AVUser fromuser = (AVUser) post.get("fromUser");

                holder.username.setText(post.getClass_name());

                holder.messagecontent.setText(post.getComments_content());

                holder.comment_timelabel.setText(TimeUtil.getTimeLong(post.getTime()));

                holder.imageView.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Log.e("abc", "discover: " + position);

                                Intent intent = new Intent(CommentActivity.this, customProfileActivity.class);
                                //intent.addFlags(Intent.FLAG_ACTIVITY__TO_FRONT);
//                                Log.e("abc", "testingt " + position + " objectID " + fromuser.getObjectId());
                                intent.putExtra("ObjectId", post.getClass_id());
                                intent.putExtra("user", UserBean.CommentBeanTo(post));
                                startActivity(intent);

                            }
                        }
                );

                ImageLoader.getInstance().displayImage(post.getClass_icon(), holder.imageView, options, animateFirstListener);
            }


            return view;
        }
    }

    static class ViewHolder {
        ImageView imageView;
        TextView username;
        TextView messagecontent;

        TextView comment_timelabel;

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

    public void getWallImages(int page) {

        // Log.e("abc", "ddd " + commentObject.getObjectId());
        commentPresenter.getComment(page, post_id);


//        AVQuery<AVObject> discoverpostquery = new AVQuery<>("Activity");
//        discoverpostquery.whereEqualTo("type","comment");
//        discoverpostquery.whereEqualTo("photo", commentObject);
//        discoverpostquery.include("fromUser");
//        //discoverpostquery.whereEqualTo("toUser", AVUser.getCurrentUser());
//        //discoverpostquery.whereNotEqualTo("fromUser", AVUser.getCurrentUser());
//
//        discoverpostquery.orderByDescending("createdAt");
//
//        discoverpostquery.findInBackground(new FindCallback<AVObject>() {
//            @Override
//            public void done(List<AVObject> list, AVException e) {
//
//
//
//                if (list != null) {
//                    postobjectArray = list;
//                    for (int i = 0; i < list.size(); i++) {
//
//                        AVObject post = (AVObject) list.get(i);
//
//                        AVUser fromuser = (AVUser) post.get("fromUser");
//
//                        AVFile profuleImage = fromuser.getAVFile("profileImage");
//
//
//
//                        profileUrl[i] = profuleImage.getUrl();
//
//                    }
//                }
//                messageadapter = new iMessageAdapter(CommentActivity.this);
//                // lv.setAdapter(messageadapter);
//                //   messageadapter.notifyDataSetChanged();
//
//                lv.setAdapter(messageadapter);
//            }
//        });
    }


}
