package maojian.android.walnut;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.me.userdate.UserBean;
import maojian.android.walnut.volley.RequestBeanListener;
import maojian.android.walnut.volley.RequestListener;
import maojian.android.walnut.volley.RequestParams;
import maojian.android.walnut.volley.VolleyRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by android on 1/8/16.
 */
public class ProfileFollowActivity extends AnyTimeActivity {

    private String objectID;

    AVUser currentUser;

    private String type;


    public List<UserListBean.UserBean> postobjectArray;

    public List<AVObject> userfollowobjectArray;

    private ListView lv;
    private userlistAdapter iadapter;
    /**
     * Volley请求对象
     */
    protected VolleyRequest mVolleyRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profilefollow);

        postobjectArray = new ArrayList<UserListBean.UserBean>();
        mVolleyRequest = new VolleyRequest();
//        ImageButton backButton = (ImageButton) findViewById(R.id.profilefollow_backButton);
//
//        backButton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                        ProfileFollowActivity.this.finish();
//                    }
//                }
//        );

        Intent intent = getIntent();
        objectID = intent.getStringExtra("ObjectId");
        type = intent.getStringExtra("type");
        lv = (ListView) findViewById(R.id.profilefollow_listview);
        iadapter = new userlistAdapter(ProfileFollowActivity.this);
        lv.setAdapter(iadapter);
//        final AVQuery commentquery = new AVQuery("_User");
//        commentquery.whereEqualTo("objectId", objectID);
//        commentquery.include("profileImage");
//
//        commentquery.findInBackground(
//                new FindCallback() {
//                    @Override
//                    public void done(List list, AVException e) {
//
//                        if(list!=null){
//                            currentUser = (AVUser) list.get(0);
//
//                        }
//
        getUserlist();
//
//                    }
//                }
//
//        );


        if (type.equals("fans")) {
            setTitle("Fans");

        } else {
            setTitle("Following");

        }

//        getUserlist();


    }

    @Override
    public void onClickEvent(View v) {

    }

    private class userlistAdapter extends BaseAdapter {
        private LayoutInflater inflater;


        private DisplayImageOptions options;
        private DisplayImageOptions postoptions;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        userlistAdapter(Context context) {
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
            if (postobjectArray != null)
                return postobjectArray.size();
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

                final UserListBean.UserBean user = postobjectArray.get(position);



            holder.username.setText(user.getClass_name());

//                holder.messagecontent.setText(post.getNum());

            if (user.getIs_follow() == 0) {//没有关注
                Log.e("message", "followpost" + position);

                holder.messagepost.setImageResource(R.drawable.follow);
                //holder.messagepost.setBackgroundColor(Color.parseColor("#FFFFFF"));

            } else if (user.getIs_follow() == 1) {//已经关注
                holder.messagepost.setImageResource(R.drawable.following);
            }

            ImageLoader.getInstance().displayImage(user.getClass_icon(), holder.imageView, options, animateFirstListener);

            holder.messagepost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user.getIs_follow() == 0) {
                        RequestParams params = new RequestParams();
                        if (UserInfos.getLoginBean() != null)
                            params.put("user_id", UserInfos.getLoginBean().getUser_id());
                        params.put("is_follow", "1");
                        params.put("class_id", user.getClass_id() + "");

                        // 发送请求
                        mVolleyRequest.post(ProfileFollowActivity.this, BaseConstant.removeFollow, params,
                                getResources().getString(R.string.login_tips), new RequestListener() {
                                    @Override
                                    public void requestSuccess(String json) {
                                        user.setIs_follow(1);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void requestError(String e) {

                                    }
                                });
                    } else {
                        Intent intent = new Intent(ProfileFollowActivity.this, customProfileActivity.class);
                        intent.putExtra("ObjectId", user.getClass_id());
                        intent.putExtra("user", UserBean.UserBeanTo(user));
                        startActivity(intent);
                    }
                }
            });
            holder.itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileFollowActivity.this, customProfileActivity.class);
                    intent.putExtra("ObjectId", user.getClass_id());
                    intent.putExtra("user", UserBean.UserBeanTo(user));
                    startActivity(intent);
                }
            });


            return view;
        }
    }

    static class ViewHolder {
        ImageView imageView;
        TextView username;
        LinearLayout itemview;
        ImageView messagepost;
    }


//        private LayoutInflater inflater;
//
//        private DisplayImageOptions options;
//        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
//
//        userlistAdapter(Context context) {
//            inflater = LayoutInflater.from(context);
//
//            options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.ic_stub)
//                    .showImageForEmptyUri(R.drawable.ic_empty)
//                    .showImageOnFail(R.drawable.ic_error)
//                    .cacheInMemory(true)
//                    .cacheOnDisk(true)
//                    .considerExifParams(true)
//                    .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
//                    .build();
//        }
//
//        @Override
//        public int getCount() {
//            return postobjectArray.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            final userListHolder holder;
//            View view = convertView;
//            if (view == null && inflater != null) {
//                view = inflater.inflate(R.layout.discoveruserlist, parent, false);
//                holder = new userListHolder();
//                assert view != null;
//                holder.profileimageView = (ImageView) view.findViewById(R.id.discover_profile);
//                holder.username = (TextView) view.findViewById(R.id.discover_username);
//                holder.followbutton = (TextView) view.findViewById(R.id.discover_button);
//                holder.isfollowing = false;
//
//                Typeface face1 = Typeface.createFromAsset(ProfileFollowActivity.this.getAssets(), "fonts/Brown-Regular.otf");
//
//                holder.followbutton.setTypeface(face1);
//
//                view.setTag(holder);
//            } else {
//                holder = (userListHolder) view.getTag();
//            }
//
//            if (postobjectArray.size() > 0) {
//
//               UserListBean.UserBean user = postobjectArray.get(position);
//
//                if (type.equals("fans")) {
//
//                   if (userfollowobjectArray != null) {
//                        //Log.e("abc","user follow count: "+userfollowobjectArray.size());
//                        for (int i = 0; i < userfollowobjectArray.size(); i++) {
//
//                          if (user.getIs_follow()==1) {
//                                Log.e("abc", "abcfollowing");
//                                holder.followbutton.setText("Following");
//                                holder.isfollowing = true;
//                                break;
//                            } else {
//                                Log.e("abc", "abcfollow");
//                                holder.followbutton.setText("Follow");
//                                holder.isfollowing = false;
//                            }
//                            //holder.followbutton.setText("????");
//
//                        }
//                    }
//                } else {
//                    holder.followbutton.setText("Following");
//                    holder.isfollowing = true;
//                }
//                holder.username.setText(user.getClass_name());
//                ImageLoader.getInstance().displayImage(user.getClass_icon(), holder.profileimageView, options, new SimpleImageLoadingListener());
//
//
//            }
//
////            holder.followbutton.setOnClickListener(
////                    new View.OnClickListener() {
////                        @Override
////                        public void onClick(View view) {
//
////                            if (holder.followbutton.getText().equals("Follow")) {
////
////                                AVObject likeobject = new AVObject("Activity");
////                                likeobject.put("type", "follow");
////                                likeobject.put("fromUser", AVUser.getCurrentUser());
////
////                                final AVQuery pushQuery = AVInstallation.getQuery();
////                                pushQuery.whereEqualTo("owner", currentUser);
////
////                                if (type.equals("fans")) {
////                                    likeobject.put("toUser", (AVUser) (postobjectArray.get(position).get("fromUser")));
////                                    pushQuery.whereEqualTo("owner", (AVUser) (postobjectArray.get(position).get("fromUser")));
////                                } else {
////                                    likeobject.put("toUser", (AVUser) (postobjectArray.get(position).get("toUser")));
////                                    pushQuery.whereEqualTo("owner", (AVUser) (postobjectArray.get(position).get("fromUser")));
////                                }
////
////
////                                AVACL likeACL = new AVACL();
////                                likeACL.setPublicReadAccess(true);// 设置公开的「读」权限，任何人都可阅读
////                                likeACL.setWriteAccess(AVUser.getCurrentUser(), true);//为当前用户赋予「写」权限
////
////                                likeobject.setACL(likeACL);
////
////                                likeobject.saveInBackground(new SaveCallback() {
////                                    @Override
////                                    public void done(AVException e) {
////                                        if (e == null) {
////                                            // 存储成功
//////                                            AVQuery pushQuery = AVInstallation.getQuery();
//////                                            pushQuery.whereEqualTo("owner", currentUser);
////
////                                            AVPush.sendMessageInBackground(AVUser.getCurrentUser().getUsername() + " started following you", pushQuery, new SendCallback() {
////                                                @Override
////                                                public void done(AVException e) {
////                                                    Log.e("abc", "like push done");
////                                                }
////                                            });
////
////                                        } else {
////                                            // 失败的话，请检查网络环境以及 SDK 配置是否正确
////                                        }
////                                    }
////                                });
////
////                                holder.followbutton.setText("Following");
////
////                            } else {
////
////                                AVQuery existlike = new AVQuery("Activity");
////                                existlike.whereEqualTo("type", "follow");
////                                existlike.whereEqualTo("fromUser", AVUser.getCurrentUser());
////                                if (type.equals("fans")) {
////                                    existlike.whereEqualTo("toUser", (AVUser) (postobjectArray.get(position).get("fromUser")));
////                                } else {
////                                    existlike.whereEqualTo("toUser", (AVUser) (postobjectArray.get(position).get("toUser")));
////                                }
////
////                                //existlike.whereEqualTo("toUser", currentUser);
////
////                                existlike.findInBackground(new FindCallback() {
////                                                               @Override
////                                                               public void done(List list, AVException e) {
////
////                                                                   for (int i = 0; i < list.size(); i++) {
////
////                                                                       AVObject a = (AVObject) list.get(i);
////                                                                       a.deleteEventually();
////                                                                   }
////                                                               }
////                                                           }
////                                );
////
////                                holder.followbutton.setText("Follow");
////                            }
////
////                        }
////                    }
////            );
//
//            holder.profileimageView.setOnClickListener(
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                        }
//                    }
//            );
//
//
//            return view;
//        }
//    }
//
//    static class userListHolder {
//        ImageView profileimageView;
//        TextView username;
//
//        TextView followbutton;
//
//        Boolean isfollowing;
//    }

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

    public void getUserlist() {
        RequestParams params = new RequestParams();
        params.put("class_id", objectID);
        if (UserInfos.getLoginBean() != null)
            params.put("user_id", UserInfos.getLoginBean().getUser_id());
        params.put("size", "100");
        params.put("page", "0");
        String url  = BaseConstant.followList;
        if (type.equals("fans")) {
            url = BaseConstant.fansList;
        }
        // 发送请求
        mVolleyRequest.post(this, url, UserListBean.class, params,"", new RequestBeanListener<UserListBean>() {
            @Override
            public void requestSuccess(UserListBean result) {
                if(result!=null) {
                    postobjectArray = result.getData();
                    iadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void requestError(String e) {
                Toast.makeText(ProfileFollowActivity.this,"请求接口出错",Toast.LENGTH_SHORT).show();
            }
        });

//        final AVQuery<AVObject> discoverpostquery = new AVQuery<>("Activity");
//
//        discoverpostquery.whereEqualTo("type", "follow");
//
//        if (type.equals("fans")) {
//            discoverpostquery.whereEqualTo("toUser", currentUser);
//            discoverpostquery.include("fromUser");
//        } else {
//            discoverpostquery.whereEqualTo("fromUser", currentUser);
//            discoverpostquery.include("toUser");
//        }
//
//        discoverpostquery.findInBackground(
//                new FindCallback<AVObject>() {
//                    @Override
//                    public void done(List<AVObject> list, AVException e) {
//
//                        if (list != null) {
//
//                            postobjectArray = list;
//
//
//                            lv = (ListView) findViewById(R.id.profilefollow_listview);
//                            iadapter = new userlistAdapter(ProfileFollowActivity.this);
//                            lv.setAdapter(iadapter);
//
//                            Log.e("abc", "profilefollow: " + postobjectArray.size() + " type: " + type);
//                            for (int i = 0; i < list.size(); i++) {
//
//                                //Log.e("abc","debugging: "+list.get(i));
//
//                                AVUser post = new AVUser();
//                                if (type.equals("fans")) {
//                                    post = (AVUser) (list.get(i).get("fromUser"));
//                                } else {
//                                    post = (AVUser) (list.get(i).get("toUser"));
//                                }
//
//                                final AVQuery<AVObject> discoverfansquery = new AVQuery<>("Activity");
//                                discoverfansquery.whereEqualTo("type", "follow");
//                                discoverfansquery.whereEqualTo("fromUser", AVUser.getCurrentUser());
//                                discoverfansquery.include("toUser");
//
//                                discoverfansquery.findInBackground(
//                                        new FindCallback<AVObject>() {
//                                            @Override
//                                            public void done(List<AVObject> list, AVException e) {
//                                                if (list != null) {
//                                                    userfollowobjectArray = list;
//
//                                                    iadapter.notifyDataSetChanged();
//
//                                                }
//                                            }
//                                        }
//                                );
//
//
//                                AVFile postImage = post.getAVFile("profileImage");
//
//                                //PostImageurl.add(postImage.getUrl());
//
//                                //Log.e("abc", "loglogdede " + i + "  " + postImage.getUrl());
//
//                                profileUrl[i] = postImage.getUrl();
//
//                            }
//                        }
//
//                    }
//                }
//        );


    }

}
