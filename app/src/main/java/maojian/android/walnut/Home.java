package maojian.android.walnut;

/**
 * Created by android on 19/7/16.
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.home.IndexBean;
import maojian.android.walnut.home.IndexPresenter;
import maojian.android.walnut.home.IndexView;
import maojian.android.walnut.home.addpost.UpLoadPicBean;
import maojian.android.walnut.me.userdate.UserBean;
import maojian.android.walnut.utils.DateUtils;
import maojian.android.walnut.utils.ShareUtils;
import maojian.android.walnut.utils.SharedPreferencesUtils;
import maojian.android.walnut.utils.TimeUtil;
import maojian.android.walnut.utils.eventbus.PostSuccessEvent;
import maojian.android.walnut.volley.RequestListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.tencent.mm.sdk.openapi.IWXAPI;

@SuppressLint("NewApi")
public class Home extends Fragment implements IndexView {
    static IndexPresenter indexPresenter;
    IndexBean indexBean;
    View view;
    public List<IndexBean.IndexListBean> postobjectArray;

    public List<Integer> likecountobjectArray;
    public IWXAPI api;
    //private ListView lv;
    private PullToRefreshListView lv;
    //    public LinearLayout llLoadfinishView;
    //ListViewAdapter adapter;
    SimpleAdapter adapter;
    ImageAdapter iadapter;

    ImageView homeview_camerabutton;


    private ArrayList existinglikeArr;
    ShareUtils shareUtils;
    int firstVisible;
    int visibleItem;
    int totalItem;
    boolean isShowing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //view = inflater.inflate(R.layout.fragment_home, container, false);
        view = inflater.inflate(R.layout.fragment_home, null);
        indexPresenter = new IndexPresenter(getActivity(), this);
        postobjectArray = new ArrayList<IndexBean.IndexListBean>();
        api = WXAPIFactory.createWXAPI(getActivity(), BaseConstant.wxApi, true);
        api.registerApp(BaseConstant.wxApi);
        shareUtils = new ShareUtils(api, getActivity());

        existinglikeArr = new ArrayList();

        likecountobjectArray = new ArrayList<>();


        lv = ((PullToRefreshListView) view.findViewById(R.id.main_listview));
//        llLoadfinishView = (LinearLayout) view.findViewById(R.id.ll_loadfinishview);
        lv.setFocusable(false);

        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                llLoadfinishView.setVisibility(View.GONE);
                getWallImages(0, false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (indexBean == null)
                    getWallImages(0, false);
                else
                    getWallImages(indexBean.getNew_page() + 1, false);
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

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // firstVisibleItem   当前第一个可见的item
                // visibleItemCount   当前可见的item个数
                firstVisible = firstVisibleItem;
                visibleItem = visibleItemCount;
                totalItem = totalItemCount;
                Log.d("onScroll", "firstVisibleItem= " + firstVisibleItem + " visibleItemCount=" + visibleItemCount + " totalItemCount=" + totalItemCount);

            }
        });

        iadapter = new ImageAdapter(getActivity());
        lv.setAdapter(iadapter);
        indexBean = SharedPreferencesUtils.getBean(getActivity(), "IndexBean", IndexBean.class);
        if (UserInfos.getLoginBean() != null && indexBean != null && indexBean.getData() != null) {
            postobjectArray = indexBean.getData();
//            IndexBean indexListBean = new IndexBean();
//            indexListBean.setFristData(indexBean.getAdvUrl());
//            postobjectArray = indexListBean.getData();
//            postobjectArray.addAll(indexBean.getData());

            iadapter.notifyDataSetChanged();
        }


        Log.e("abc", "postobjectArraycreate" + postobjectArray.size());

//        lv = (ListView) view.findViewById(R.id.listView);
//
//        myData = new ArrayList<Map<String, Object>>();
//
//        //myData = getData();
//
//        Log.e("abc", "mydatasize1"+myData.size());
//        adapter = new SimpleAdapter(getActivity(),myData,R.layout.vlist,
//                new String[]{"username","userprofile","img"},
//                new int[]{R.id.userName,R.id.Profile,R.id.Post});
//
//
//
//        lv.setAdapter(adapter);


        homeview_camerabutton = (ImageView) view.findViewById(R.id.mainview_camerabutton);


        Log.e("a123", "  " + homeview_camerabutton);

        homeview_camerabutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("123", "123");
                        if (UserInfos.getLoginBean() == null) {
                            startActivity(new Intent(getActivity(), LoginDialogActivity.class));
                            return;
                        }
                        Intent intent = new Intent(getActivity(), CameraActivity.class);
                        startActivity(intent);


//                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                            //具有拍照权限，直接调用相机
//                            //具体调用代码
//                            Log.e("cameradebug","checkSelfPermission");
//                            Intent intent = new Intent(getActivity(), CameraActivity.class);
//                            startActivity(intent);
//                        } else {
//                            //不具有拍照权限，需要进行权限申请
//                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO}, 1);
//                        }
                    }
                }
        );


        //lv.setAdapter(new ArrayAdapter<String>(getActivity(),
        //        android.R.layout.simple_expandable_list_item_1, strs));
        //new MyThread().start();


        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


//        ImageButton homeview_camerabutton = (ImageButton) getActivity().findViewById(R.id.mainview_camerabutton);
//        Log.e("aa123", "123 "+homeview_camerabutton);
//        homeview_camerabutton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.e("123", "123");
//                        Intent intent = new Intent(getActivity(), CameraActivity.class);
//                        startActivity(intent);
//                    }
//                }
//        );

    }

    @Override
    public void onResume() {

        super.onResume();

        if (getActivity() != null && ((MainActivity) getActivity()).current_tab == 0) {
            getWallImages(0, postobjectArray == null || postobjectArray.size() == 0);
            setIsShowing(true);
        }
//        new Thread(runnable).start();

        Log.e("abc", "home resume");

//        VideoView  videoView = (VideoView) view.findViewById(R.id.videoView);
//        Uri uri = Uri.parse("https://s3.amazonaws.com/avos-cloud-tj3tbek3kfqb/K3XTTmPwLABNaMc474hRDdB.mov");
//        //holder.videoView.setMediaController(new MediaController(getActivity()));
//        videoView.setVideoURI(uri);
//        videoView.start();
    }


    @Override
    public void setLoginSuccess(IndexBean indexBeanNew) {
        indexBean = indexBeanNew;
        if (indexBean.getNew_page() == 0) {
            lv.setMode(PullToRefreshBase.Mode.BOTH);
            postobjectArray = indexBean.getData();
//            IndexBean indexListBean = new IndexBean();
//            indexListBean.setFristData(indexBean.getAdvUrl());
//            postobjectArray = indexListBean.getData();
//            postobjectArray.addAll(indexBean.getData());
            SharedPreferencesUtils.setBean(getActivity(), "IndexBean", indexBean);
        } else {
            postobjectArray.addAll(indexBean.getData());
        }
        iadapter.notifyDataSetChanged();
        lv.onRefreshComplete();

//        if (indexBean.getNew_page() >= indexBean.getTotal_page()-1) {
//            lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
////            llLoadfinishView.setVisibility(View.VISIBLE);
//        } else {
//            lv.setMode(PullToRefreshBase.Mode.BOTH);
//        }


    }

    @Override
    public void setLoginFail(String errInfo, int page) {
        lv.onRefreshComplete();
        if (TextUtils.isEmpty(errInfo))
            errInfo = "Loading Fail";
        if (getActivity() != null)
            Toast.makeText(getActivity(), errInfo, Toast.LENGTH_SHORT).show();

        if (errInfo.equals("No data") && postobjectArray != null && page == 0) {
            indexBean = null;
            SharedPreferencesUtils.removeData(getActivity(), "IndexBean");
            postobjectArray.clear();
            iadapter.notifyDataSetChanged();
        }

    }

    @Override
    public void setUpLoadPicSuccess(UpLoadPicBean upLoadPicBean, final PostSuccessEvent postSuccessEvent) {
        String rurl = "";
        String litpic = "";
        String post_type = "1";
        if (upLoadPicBean != null) {
            String upLoadPicUrl = upLoadPicBean.getImage_url();
            if (!TextUtils.isEmpty(upLoadPicUrl)) {
                rurl = upLoadPicUrl;
            } else {
                rurl = upLoadPicBean.getRurl();
                litpic = upLoadPicBean.getLitpic();
                post_type = "2";
            }

            final String finalRurl = rurl;
            indexPresenter.addPost(rurl, litpic, post_type, postSuccessEvent.getPostBean().getLocation(),
                    postSuccessEvent.getPostBean().getPostCon(), new RequestListener() {
                        @Override
                        public void requestSuccess(String json) {
                            Log.d("post=", json);
                            getWallImages(0, true);
                            Toast.makeText(getActivity(), "posted", Toast.LENGTH_SHORT).show();
                            if (faceBookShare) {
                                String content = postSuccessEvent.getPostBean().getPostCon();
                                shareUtils.setUserId(UserInfos.getLoginBean().getUser_id());
                                Platform plat = ShareSDK.getPlatform(Facebook.NAME);
                                shareUtils.showShare(plat.getName(), finalRurl, content);
                            }
                            if (weChat) {
                                String content = postSuccessEvent.getPostBean().getPostCon();
                                shareUtils.setUserId(UserInfos.getLoginBean().getUser_id());
                                Platform plat = ShareSDK.getPlatform(Wechat.NAME);
                                shareUtils.showShare(plat.getName(), finalRurl, content);
                            }
                            if (WeiBo) {
                                String content = postSuccessEvent.getPostBean().getPostCon();
                                shareUtils.setUserId(UserInfos.getLoginBean().getUser_id());
                                Platform plat = ShareSDK.getPlatform(SinaWeibo.NAME);
                                shareUtils.showShare(plat.getName(), finalRurl, content);
                            }
                            if (Twitter) {
                                String content = postSuccessEvent.getPostBean().getPostCon();
                                shareUtils.setUserId(UserInfos.getLoginBean().getUser_id());
                                Platform plat = ShareSDK.getPlatform(cn.sharesdk.twitter.Twitter.NAME);
                                shareUtils.showShare(plat.getName(), finalRurl, content);
                            }
//                           weChat = false, WeiBo = false, Twitter = false;
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            viewPager.setCurrentItem(2);
//                        }
//                    }, 50);

                        }

                        @Override
                        public void requestError(String e) {
                            Toast.makeText(getActivity(), "Sending Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void setUpLoadPicFail(String errInfo) {
        Toast.makeText(getActivity(), "Sending Fail", Toast.LENGTH_SHORT).show();
    }

    public void onPause() {
        super.onPause();
        Log.d("onPause", "onPause home");
        setIsShowing(false);
    }

    public void setIsShowing(boolean showing) {
        isShowing = showing;
        if (iadapter != null)
            iadapter.notifyDataSetChanged();
    }

    //
    private class ImageAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        private DisplayImageOptions options;
        private DisplayImageOptions profileoptions;


        ImageAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();

            profileoptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                    .build();

            //IMAGE_URLS = new String[0];

            Log.e("abc", "ssbb" + postobjectArray.size());

        }

        @Override
        public int getCount() {
            return postobjectArray.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            View view = convertView;
            final ViewHolder holder;

            if (convertView == null && inflater != null) {
                view = inflater.inflate(R.layout.vlist, parent, false);
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.userName);
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Brown-Regular.otf");
                holder.text.setTypeface(face);
                holder.itemview = (LinearLayout) view.findViewById(R.id.itemview);
                holder.topview = (LinearLayout) view.findViewById(R.id.topview);
                holder.bottomview = (LinearLayout) view.findViewById(R.id.bottomview);
                holder.topline = (View) view.findViewById(R.id.topline);

                holder.image = (ImageView) view.findViewById(R.id.Post);
//                holder.adv = (ImageView) view.findViewById(R.id.adv);

                holder.transferbutton = (ImageView) view.findViewById(R.id.transferbutton);
                ViewGroup.LayoutParams para;
                para = holder.image.getLayoutParams();
                WindowManager wm1 = getActivity().getWindowManager();
                int width1 = wm1.getDefaultDisplay().getWidth();
                int height1 = wm1.getDefaultDisplay().getHeight();

                para.height = width1 / 16 * 10;
                para.width = width1;

                holder.image.setLayoutParams(para);
                holder.profile = (ImageView) view.findViewById(R.id.Profile);

                holder.likecount = (TextView) view.findViewById(R.id.mainlikecount);
                Typeface face1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Brown-Light.otf");
                holder.likecount.setTypeface(face1);
                holder.commentcount = (TextView) view.findViewById(R.id.maincommentcount);
                Typeface face2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Brown-Light.otf");
                holder.commentcount.setTypeface(face1);

                holder.userAddress = (TextView) view.findViewById(R.id.userAddress);
                holder.timelabel = (TextView) view.findViewById(R.id.timelabel);
                Typeface face3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Brown-Light.otf");
                holder.timelabel.setTypeface(face3);
                holder.commentcontent = (TextView) view.findViewById(R.id.main_commentcontent);
                holder.comment_line = (View) view.findViewById(R.id.comment_line);

                Typeface face4 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Brown-Regular.otf");

                holder.commentcount.setTypeface(face4);
                holder.likecount.setTypeface(face4);
                holder.commentcontent.setTypeface(face4);

                holder.likebutton = (ImageButton) view.findViewById(R.id.mainlikebutton);
                holder.commentbutton = (ImageButton) view.findViewById(R.id.maincommentbutton);
                holder.islike = false;

                holder.videoView = (VideoView) view.findViewById(R.id.videoView);
                holder.video_playbutton = (ImageButton) view.findViewById(R.id.video_playbutton);

                ViewGroup.LayoutParams para_video;
                para_video = holder.videoView.getLayoutParams();
                WindowManager wm2 = getActivity().getWindowManager();
                int width2 = wm2.getDefaultDisplay().getWidth();
                int height2 = wm2.getDefaultDisplay().getHeight();

                para_video.height = width2 / 16 * 10;
                para_video.width = width2;

                holder.videoView.setLayoutParams(para_video);

                holder.player = new MediaPlayer();


                view.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }
//            if (position == 0) {
//                holder.topline.setVisibility(View.GONE);
//                holder.topview.setVisibility(View.GONE);
//                holder.bottomview.setVisibility(View.GONE);
//            } else {
//                holder.topline.setVisibility(View.VISIBLE);
//                holder.topview.setVisibility(View.VISIBLE);
//                holder.bottomview.setVisibility(View.VISIBLE);
//            }
//            if (position == 0) {
//                holder.adv.setVisibility(View.VISIBLE);
//
//                ImageLoader.getInstance().displayImage(indexBean.getAdvUrl(), holder.adv,
//                        options, animateFirstListener);
//
//                holder.adv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(getActivity(), StoreWebActivity.class)
//                                .putExtra("url", BaseConstant.store));
//
//                    }
//                });
//            } else {
//                holder.adv.setVisibility(View.GONE);
//            }

            final IndexBean.IndexListBean indexListBean = postobjectArray.get(position);
            if (postobjectArray.size() > 0) {

                if (!TextUtils.isEmpty(indexListBean.getClass_address())) {
                    holder.userAddress.setVisibility(View.VISIBLE);
                    holder.userAddress.setText(indexListBean.getClass_address());
                } else {
                    holder.userAddress.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(indexListBean.getContent())) {
                    holder.comment_line.setVisibility(View.VISIBLE);
                    holder.commentcontent.setVisibility(View.VISIBLE);
                    holder.commentcontent.setText(indexListBean.getContent());

                    ViewTreeObserver observer = holder.commentcontent.getViewTreeObserver(); //textAbstract为TextView控件
                    observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            ViewTreeObserver obs = holder.commentcontent.getViewTreeObserver();
                            obs.removeGlobalOnLayoutListener(this);
                            if (holder.commentcontent.getLineCount() > 3) {//判断行数大于多少时改变
                                int lineEndIndex = holder.commentcontent.getLayout().getLineEnd(2); //设置第六行打省略号
                                String text = holder.commentcontent.getText().subSequence(0, lineEndIndex - 3) + "...More";
                                holder.commentcontent.setText(text);
                            }
                        }
                    });

                    Log.e("abc", "commentcontent " + position);
                } else {
                    //holder.commentcontent.setHeight(0);
                    //holder.commentcontent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    Log.e("abc", "commentcontent gone " + position);
                    holder.commentcontent.setVisibility(View.GONE);
                    holder.comment_line.setVisibility(View.GONE);
//                        holder.commentcontent.setText(indexListBean.getContent());
                }

//                } else {
//                    Log.e("abc", "commentcontent gone " + position);
//                    holder.commentcontent.setVisibility(View.GONE);
//                    //holder.commentcontent.setText(postobjectArray.get(position).get("comment").toString());
//                }

                holder.videoView.setVisibility(View.INVISIBLE);
                holder.image.setVisibility(View.VISIBLE);
                holder.video_playbutton.setVisibility(View.INVISIBLE);

                if (postobjectArray.get(position).getPost_type() == 2) {
                    //视频
//                    holder.videoView.setVisibility(View.VISIBLE);
//                    holder.image.setVisibility(View.INVISIBLE);
                    holder.video_playbutton.setVisibility(View.VISIBLE);

//                    AVFile postVideo = postobjectArray.get(position).getAVFile("video");
//                    if (postVideo != null && !TextUtils.isEmpty(postVideo.getUrl())) {
                    Uri uri = Uri.parse(postobjectArray.get(position).getVideo_url());
                    //holder.videoView.setMediaController(new MediaController(getActivity()));
                    holder.videoView.setVideoURI(uri);
//                    }


//                    if (isShowing && position >= firstVisible && position < firstVisible + visibleItem && !holder.videoView.isPlaying()) {
//                        holder.videoView.start();  // Video playing logic
//                        holder.video_playbutton.setVisibility(View.INVISIBLE);
//                        holder.videoView.setVisibility(View.VISIBLE);
//
//                        holder.image.setVisibility(View.INVISIBLE);
//                    } else
                    if (!isShowing) {
                        //播放结束后的动作
                        if (holder.videoView.isPlaying())
                            holder.videoView.pause();
                        holder.videoView.setVisibility(View.INVISIBLE);
                        holder.video_playbutton.setVisibility(View.VISIBLE);

                        holder.image.setVisibility(View.VISIBLE);
                    }
                    WindowManager wm = getActivity().getWindowManager();

                    int width = wm.getDefaultDisplay().getWidth();
                    //int height = wm.getDefaultDisplay().getHeight();

                    Log.e("video debug", " " + width);

                    //holder.videoView.getHolder().setFixedSize(2160,2160/16*9);

                    holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            //播放结束后的动作
                            holder.videoView.setVisibility(View.INVISIBLE);
                            holder.video_playbutton.setVisibility(View.VISIBLE);

                            holder.image.setVisibility(View.VISIBLE);

                        }
                    });

                    holder.video_playbutton.setAlpha((float) 0.8);
                    holder.video_playbutton.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    holder.videoView.start();
                                    holder.video_playbutton.setVisibility(View.INVISIBLE);
                                    holder.videoView.setVisibility(View.VISIBLE);

                                    holder.image.setVisibility(View.INVISIBLE);
                                }
                            }
                    );


                    //holder.videoView.setVisibility(View.INVISIBLE);
                    //holder.videoView.requestFocus();


                    //holder.videoView

                }


                holder.profile.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (UserInfos.getLoginBean() == null) {
                                    startActivity(new Intent(getActivity(), LoginDialogActivity.class));
                                    return;
                                }
//                                Log.e("abc", "discover: " + position);
//
//                                if (!(x.getUsername().toString().equals(AVUser.getCurrentUser().getUsername().toString()))) {
//
                                Intent intent = new Intent(getActivity(), customProfileActivity.class);
                                //intent.addFlags(Intent.FLAG_ACTIVITY__TO_FRONT);
                                Log.e("abc", "testingt " + position + " objectID " + indexListBean.getClass_id());
                                intent.putExtra("ObjectId", indexListBean.getClass_id());
                                intent.putExtra("user", UserBean.indexListBeanTo(indexListBean));
                                startActivity(intent);
//                                }


                            }
                        }
                );

                holder.text.setText(indexListBean.getClass_name());


//                holder.timelabel.setText(DateUtils.dateProcess1(Long.parseLong(indexListBean.getTime())));
                holder.timelabel.setText(TimeUtil.getTimeLong(indexListBean.getTime()));
                holder.commentcount.setText(indexListBean.getComments_num() + "");
                holder.likecount.setText(indexListBean.getPraise_num() + "");

                if (indexListBean.getIs_praise()) {
                    holder.likebutton.setBackground(getResources().getDrawable(R.drawable.mainlikeicon));
                    holder.islike = true;
                } else {
                    holder.likebutton.setBackground(getResources().getDrawable(R.drawable.maindislikeicon));
                    holder.islike = false;
                }

                holder.likebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UserInfos.getLoginBean() == null) {
                            startActivity(new Intent(getActivity(), LoginDialogActivity.class));
                            return;
                        }
                        holder.islike = !holder.islike;
                        indexListBean.setIs_praise(holder.islike);
                        if (holder.islike) {
                            //點贊
                            indexListBean.setPraise_num(indexListBean.getPraise_num() + 1);
                        } else {
                            indexListBean.setPraise_num(indexListBean.getPraise_num() - 1);
                        }
                        notifyDataSetChanged();
                        indexPresenter.like(holder.islike ? 1 : 2, indexListBean.getPost_id(), new RequestListener() {
                            @Override
                            public void requestSuccess(String json) {
//                                Toast.makeText(getActivity(), holder.islike ? "点赞成功" : "取消点赞成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void requestError(String e) {
                                indexListBean.setIs_praise(!holder.islike);
                                if (holder.islike) {
                                    //點贊
                                    indexListBean.setPraise_num(indexListBean.getPraise_num() - 1);
                                } else {
                                    indexListBean.setPraise_num(indexListBean.getPraise_num() + 1);
                                }
                                holder.islike = !holder.islike;
                                notifyDataSetChanged();
                            }
                        });
                    }
                });

                holder.commentbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UserInfos.getLoginBean() == null) {
                            startActivity(new Intent(getActivity(), LoginDialogActivity.class));
                            return;
                        }
                        Intent intent = new Intent(getActivity(), CommentActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY__TO_FRONT);
                        Log.e("abc", "testingt " + position + " objectID " + postobjectArray.get(position));
                        intent.putExtra("ObjectId", postobjectArray.get(position).getPost_id());
                        intent.putExtra("post", postobjectArray.get(position));
                        startActivity(intent);


                    }
                });
            }

            holder.transferbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "";
                    if (indexListBean.getPost_type() == 1)
                        url = indexListBean.getVideo_url();
                    else
                        url = indexListBean.getCover_icon();
                    String content = "";
                    if (!TextUtils.isEmpty(indexListBean.getContent())) {
                        content = indexListBean.getContent();
                    }
                    shareUtils.setUserId(indexListBean.getClass_id());
                    shareUtils.setPost_id(indexListBean.getPost_id());
                    shareUtils.showShareDialog(url, indexListBean.getClass_name(), content);

                }
            });
            //Log.e("abc", "getView" + (String) x.getUsername());
            if (indexListBean.getPost_type() == 1)
                ImageLoader.getInstance().displayImage(indexListBean.getVideo_url(), holder.image, options, animateFirstListener);
            else
                ImageLoader.getInstance().displayImage(indexListBean.getCover_icon(), holder.image, options, animateFirstListener);

            ImageLoader.getInstance().displayImage(indexListBean.getClass_icon(), holder.profile, profileoptions, animateFirstListener);

            holder.itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    if (position==0){
//                        startActivity(new Intent(getActivity(), StoreWebActivity.class)
//                                .putExtra("url", BaseConstant.store));
//                    }else {
                    if (UserInfos.getLoginBean() == null) {
                        Intent intent = new Intent(getActivity(), LoginDialogActivity.class);
                        startActivity(intent);
                        return;
                    }

                    Intent intent = new Intent(getActivity(), DetailActivity.class);

                    intent.putExtra("ObjectId", postobjectArray.get(position).getPost_id());
                    intent.putExtra("detailObject", postobjectArray.get(position));
                    startActivity(intent);
//                    }
                }
            });

            return view;


        }


    }

    static class ViewHolder {
        TextView text;
        ImageView profile;
        ImageView image, adv;
        ImageView transferbutton;

        TextView likecount;
        TextView commentcount;

        TextView timelabel;
        TextView commentcontent;
        TextView userAddress;
        View comment_line;

        ImageButton likebutton;
        ImageButton commentbutton;

        Boolean islike;

        VideoView videoView;
        ImageButton video_playbutton;
        LinearLayout itemview, topview, bottomview;
        View topline;
        SurfaceView videosurfaceView;
        MediaPlayer player;
        Display currDisplay;
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

    public void getEditCustomDialog() {
        AlertDialog alertDialog;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.include_login1, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        alertDialog = builder.create();
        alertDialog.show();
    }
    //
//    private ArrayList<Map<String, Object>> getData() {
//
//        final ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//
//        Log.e("abc","getdata "+postobjectArray.size());
//        for(int i = 0;i < postobjectArray.size(); i ++){
//
//
//
//            final AVObject post = (AVObject) postobjectArray.get(i);
//            final AVUser user = (AVUser) post.get("user");
//            Log.e("abc", "AVuser user " + post.get("user"));
//
//            final Map<String, Object> map = new HashMap<String, Object>();
//
//
//
//            AVFile userProfile = user.getAVFile("profileImage");
//
//
//            userProfile.getDataInBackground(new GetDataCallback() {
//                @Override
//                public void done(final byte[] bytes, AVException e) {
//
//                    AVFile postImage = post.getAVFile("image");
//
//                    Log.e("aaa","av url"+postImage.getUrl());
//
//                    postImage.getDataInBackground(new GetDataCallback() {
//                        @Override
//                        public void done(byte[] postbytes, AVException e) {
//
//                            adapter = new SimpleAdapter(getActivity(), myData, R.layout.vlist,
//                                    new String[]{"username", "userprofile", "img"},
//                                    new int[]{R.id.userName, R.id.Profile, R.id.Post});
//                            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
//
//                                @Override
//                                public boolean setViewValue(View view, Object data,
//                                                            String textRepresentation) {
//                                    if ((view instanceof ImageView) & (data instanceof Bitmap)) {
//                                        ImageView iv = (ImageView) view;
//                                        Bitmap bm = (Bitmap) data;
//                                        iv.setImageBitmap(bm);
//                                        return true;
//                                    }
//                                    return false;
//
//                                }
//                            });
//
//                            map.put("username", (String) user.get("username"));
//
//                            Bitmap profile = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                            ByteArrayOutputStream postbos = new ByteArrayOutputStream();
//                            profile.compress(Bitmap.CompressFormat.JPEG, 100, postbos);
//                            byte[] pofbytes = postbos.toByteArray();
//                            profile = BitmapFactory.decodeByteArray(pofbytes, 0, pofbytes.length);
//                            map.put("userprofile", profile);
//
////                            Bitmap post = BitmapFactory.decodeByteArray(postbytes, 0, postbytes.length);
////                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
////                            post.compress(Bitmap.CompressFormat.JPEG, 100, bos);
////                            byte[] pobytes = bos.toByteArray();
////                            post = BitmapFactory.decodeByteArray(pobytes, 0, pobytes.length);
////                            map.put("img", post);
//
//
//                            list.add(map);
//                            lv.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//
//                        }
//                    });
//
//
//
//
//                }
//            });

    //}}
    public static void getWallImages(int page, boolean showLoading) {
        indexPresenter.getIndex(page, showLoading);
    }

    public static void addPost(PostSuccessEvent postSuccessEvent) {
        if (postSuccessEvent.getPostBean().isVideo()) {
            indexPresenter.postVideoUpload(postSuccessEvent.getPostBean().getNewPath(), postSuccessEvent);
        } else {
            indexPresenter.postImgUpload(postSuccessEvent.getPostBean().getNewPath(), false, postSuccessEvent);
        }
    }

    private static boolean faceBookShare = false, weChat = false, WeiBo = false, Twitter = false;

    public static void setShare(boolean faceBookShare1, boolean weChat1, boolean WeiBo1, boolean Twitter1) {
        faceBookShare = faceBookShare1;
        weChat = weChat1;
        WeiBo = WeiBo1;
        Twitter = Twitter1;
    }
}


