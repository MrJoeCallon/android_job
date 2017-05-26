package maojian.android.walnut;

/**
 * Created by android on 19/7/16.
 */

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.me.MePresenter;
import maojian.android.walnut.me.userdate.UserBean;
import maojian.android.walnut.message.MessageListBean;
import maojian.android.walnut.message.MessagePresenter;
import maojian.android.walnut.message.MessageView;
import maojian.android.walnut.utils.SharedPreferencesUtils;
import maojian.android.walnut.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@SuppressLint("NewApi")
public class iMessage extends Fragment implements MessageView {
    private MessagePresenter messagePresenter;
    private iMessageAdapter messageadapter;
    public List<MessageListBean.MessageBean> postobjectArray;

//    private ListView lv;

    private PullToRefreshListView mlistview;
    MessageListBean messageListBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        messagePresenter = new MessagePresenter(getActivity(), this);
        postobjectArray = new ArrayList<MessageListBean.MessageBean>();

        getWallImages(0);

//        lv = (ListView) rootView.findViewById(R.id.message_listview);

        mlistview = ((PullToRefreshListView) rootView.findViewById(R.id.message_listview));
//        mlistview.setInterface(this);

        mlistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                llLoadfinishView.setVisibility(View.GONE);
                getWallImages(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getWallImages(messageListBean.getNew_page() + 1);
//                if (hasNext.equals("true")) {
//                    isRefresh = 2;
//                    hostPage++;
//                    isResume = false;
//                    mMessagePresenter.getMessage(UserInfo.mLoginBean.getUserId(), hostPage + "", "20");
//                } else {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            hospitalList.onRefreshComplete();
//                            ToastUtils.showShort(getActivity(), "已经没有更多的数据了");
//                        }
//                    }, 500);
//                }

            }
        });

        messageadapter = new iMessageAdapter(getActivity());
        mlistview.setAdapter(messageadapter);
//        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //startImagePagerActivity(position);
//                Intent intent = new Intent(getActivity(), DetailActivity.class);
//
//                intent.putExtra("ObjectId", postobjectArray.get(position).getPost_id());
////                intent.putExtra("detailObject", postobjectArray.get(position));
//                startActivity(intent);
//            }
//        });

        return rootView;

    }


    @Override
    public void setSuccess(MessageListBean messageListBeanNew) {
        messageListBean = messageListBeanNew;
        if (messageListBean.getNew_page() == 0) {
            mlistview.setMode(PullToRefreshBase.Mode.BOTH);
            postobjectArray = messageListBean.getData();
        } else {
            postobjectArray.addAll(messageListBean.getData());
        }
        messageadapter.notifyDataSetChanged();
        mlistview.onRefreshComplete();
//        messageadapter = new iMessageAdapter(getActivity());
//                // lv.setAdapter(messageadapter);
//                //   messageadapter.notifyDataSetChanged();
//
//                mlistview.setAdapter(messageadapter);
    }

    @Override
    public void setFail(String errInfo) {
        mlistview.onRefreshComplete();
        if (TextUtils.isEmpty(errInfo))
            errInfo = "数据加载失败";
        if (getActivity() != null)
            Toast.makeText(getActivity(), errInfo, Toast.LENGTH_SHORT).show();
    }

//    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
//
//        @Override
//        protected String[] doInBackground(Void... params) {
//            // Simulates a background job.
//
//            AVQuery<AVObject> discoverpostquery = new AVQuery<>("Activity");
//            discoverpostquery.include("fromUser");
//            discoverpostquery.include("photo");
//            discoverpostquery.whereEqualTo("toUser", AVUser.getCurrentUser());
//            discoverpostquery.whereNotEqualTo("fromUser", AVUser.getCurrentUser());
//
//            discoverpostquery.orderByDescending("createdAt");
//
//            discoverpostquery.findInBackground(new FindCallback<AVObject>() {
//                @Override
//                public void done(List<AVObject> list, AVException e) {
//
//                    postobjectArray = list;
//
//                    if (list != null) {
//                        for (int i = 0; i < list.size(); i++) {
//
//                            AVObject post = (AVObject) list.get(i);
//
//                            AVUser fromuser = (AVUser) post.get("fromUser");
//
//                            AVFile profuleImage = fromuser.getAVFile("profileImage");
//
//                            if (post.get("photo") != null) {
//                                AVObject a = (AVObject) post.get("photo");
//                                postUrl[i] = a.getAVFile("image").getUrl();
//                            }
//
//                            profileUrl[i] = profuleImage.getUrl();
//
//                        }
//                    }
//                    //messageadapter = new iMessageAdapter(getActivity());
//                    // lv.setAdapter(messageadapter);
//                    //mlistview.setAdapter(messageadapter);
//
//
//                    messageadapter.notifyDataSetChanged();
//
//
//                }
//            });
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                ;
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String[] result) {
//            //mListItems.addFirst("Added after refresh...");
//            // Call onRefreshComplete when the list has been refreshed.
//            //   mlistview.onRefreshComplete();
//            super.onPostExecute(result);
//        }
//    }

    private class iMessageAdapter extends BaseAdapter {


        private LayoutInflater inflater;


        private DisplayImageOptions options;
        private DisplayImageOptions postoptions,followoptions;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        iMessageAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_empty_plus)
                    .showImageForEmptyUri(R.drawable.ic_empty_plus)
                    .showImageOnFail(R.drawable.ic_empty_plus)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                    .build();
            followoptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.meaasge_user)
                    .showImageForEmptyUri(R.drawable.meaasge_user)
                    .showImageOnFail(R.drawable.meaasge_user)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
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
                view = inflater.inflate(R.layout.message_listitem, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.message_userprofile);
                holder.username = (TextView) view.findViewById(R.id.message_username);

                Typeface face1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Brown-Light.otf");
                holder.username.setTypeface(face1);
                holder.messagecontent = (TextView) view.findViewById(R.id.message_content);
                holder.messagecontent.setTypeface(face1);
                holder.itemview = (LinearLayout) view.findViewById(R.id.itemview);
                holder.messagepost = (ImageView) view.findViewById(R.id.message_post);
                view.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }
            Typeface face2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Brown-Regular.otf");

            if (postobjectArray.size() > 0) {
                final MessageListBean.MessageBean post = (MessageListBean.MessageBean) postobjectArray.get(position);


                holder.username.setText(post.getClass_name());

                holder.messagecontent.setText(post.getNum());
                post.setTime(TimeUtil.getTimeLong(post.getTime()));
                if (post.getType() == 4) {//官方消息

                    String w = "System message.  " + post.getTime();
                    holder.messagecontent.setText(w);
                    holder.username.setText(UserInfos.getLoginBean().getUser_name());
                    holder.messagepost.setImageResource(R.drawable.device_msg);

                } else if (post.getType() == 3) {//@
                    Log.e("message", "followpost" + position);

                    String w = "@" + post.getClass_name() + "    you.  " + post.getTime();
//                    int start = 0;
//                    int end = w.indexOf('u');
//                    Spannable word = new SpannableString(w);
//                    word.setSpan(new StyleSpan(Typeface.BOLD), start, end,
//                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                    holder.messagecontent.setText(w);
//                    holder.messagepost.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    holder.messagepost.setImageResource(R.drawable.meaasge_user);
                    String Inbox_icon = postobjectArray.get(position).getInbox_icon();
                    ImageLoader.getInstance().displayImage(Inbox_icon, holder.messagepost, followoptions, animateFirstListener);
                } else if (post.getType() == 2) {//关注
                    Log.e("message", "followpost" + position);

                    String w = "Started following you." + post.getTime();
//                    int start = 0;
//                    int end = w.indexOf('u');
//                    Spannable word = new SpannableString(w);
//                    word.setSpan(new StyleSpan(Typeface.BOLD), start, end,
//                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                    holder.messagecontent.setText("Started following you.  " + post.getTime());
//                    holder.messagepost.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    holder.messagepost.setImageResource(R.drawable.meaasge_user);

                } else if (post.getType() == 1) {
                    Log.e("message", "commentpost" + position);
                    holder.messagecontent.setText("comment your post.  " + post.getTime());
                    String Inbox_icon = postobjectArray.get(position).getInbox_icon();
                    if (post.getPost_type() == 2) {
                        if (!TextUtils.isEmpty(postobjectArray.get(position).getLitpic()))
                            Inbox_icon = postobjectArray.get(position).getLitpic();
                    }
                    ImageLoader.getInstance().displayImage(Inbox_icon, holder.messagepost, postoptions, animateFirstListener);

                } else if (post.getType() == 0) {
                    String Inbox_icon = postobjectArray.get(position).getInbox_icon();
                    Log.e("message", "likepost" + position);
                    String tips = "like your post.  ";
                    if (post.getPost_type() == 2) {
//                        tips = "Liked your video .  ";
                        if (!TextUtils.isEmpty(postobjectArray.get(position).getLitpic()))
                            Inbox_icon = postobjectArray.get(position).getLitpic();
                    }
                    holder.messagecontent.setText(tips + post.getTime());
                    ImageLoader.getInstance().displayImage(Inbox_icon, holder.messagepost, postoptions, animateFirstListener);
                }

                if (post.getType() == 4) {//官方消息
                    holder.imageView.setImageResource(R.drawable.default_msg);
                } else {
                    ImageLoader.getInstance().displayImage(postobjectArray.get(position).getClass_icon(), holder.imageView, options, animateFirstListener);

                }
                holder.itemview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if (UserInfos.getLoginBean() == null) {
                            startActivity(new Intent(getActivity(), LoginDialogActivity.class));
                            return;
                        }
                        if (post.getType() == 0) {
                            intent = new Intent(getActivity(), DetailActivity.class);

                            intent.putExtra("ObjectId", postobjectArray.get(position).getPost_id());
//                intent.putExtra("detailObject", postobjectArray.get(position));
                            startActivity(intent);
                        } else if (post.getType() == 1) {
                            intent = new Intent(getActivity(), CommentActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY__TO_FRONT);
                            Log.e("abc", "testingt " + position + " objectID " + postobjectArray.get(position));
                            intent.putExtra("ObjectId", postobjectArray.get(position).getPost_id());
                            intent.putExtra("post", postobjectArray.get(position));
                            startActivity(intent);
                        } else if (post.getType() == 2) {
                            intent = new Intent(getActivity(), ProfileFollowActivity.class);
                            intent.putExtra("type", "following");
                            intent.putExtra("ObjectId", UserInfos.getLoginBean().getUser_id());
                            startActivity(intent);
                        } else if (post.getType() == 3) {
                            intent = new Intent(getActivity(), customProfileActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY__TO_FRONT);
                            Log.e("abc", "testingt " + position + " objectID " + postobjectArray.get(position).getClass_id());
                            intent.putExtra("ObjectId", postobjectArray.get(position).getClass_id());
                            intent.putExtra("user", UserBean.messageBeanTo(postobjectArray.get(position)));
                            startActivity(intent);
                        } else if (post.getType() == 4) {
                            intent = new Intent(getActivity(), VideoPlayerActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
            return view;
        }
    }

    static class ViewHolder {
        ImageView imageView;
        TextView username;
        TextView messagecontent;
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

    public void getWallImages(int page) {
        messagePresenter.getMessageList(page);

    }

    @Override
    public void onResume() {

        super.onResume();

        getWallImages(0);
        Log.e("abc", "home resume");

    }


}