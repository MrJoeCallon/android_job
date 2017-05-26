package maojian.android.walnut;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.VideoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import maojian.android.walnut.base.BaseShare;
import maojian.android.walnut.home.IndexBean;
import maojian.android.walnut.me.postdetails.PostDetailsPresenter;
import maojian.android.walnut.me.postdetails.PostDetailsView;
import maojian.android.walnut.me.userdate.UserBean;
import maojian.android.walnut.utils.TimeUtil;
import maojian.android.walnut.volley.RequestListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by android on 29/7/16.
 */
public class DetailActivity extends AnyTimeActivity implements PostDetailsView {

    PostDetailsPresenter postDetailsPresenter;
    private String objectID;
    ImageButton likebutton;
    private IndexBean.IndexListBean detailObject;

    private boolean islike;

    ImageView detailPost;
    ImageView detailProfile;
    BaseShare baseShare;

    TextView likecount;
    VideoView videoView;
    ImageButton video_playbutton;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);
        postDetailsPresenter = new PostDetailsPresenter(this, this);
        islike = false;
        setTitle("Details");
        baseShare = new BaseShare(this);

        Intent intent = getIntent();
        objectID = intent.getStringExtra("ObjectId");
        detailObject = (IndexBean.IndexListBean) intent.getSerializableExtra("detailObject");
        postDetailsPresenter.getPostDetails(objectID, detailObject);

        baseShare.shareUtils.setPost_id(objectID);
        Log.e("abc", "object: " + objectID);
        //AVObject.createWithoutData("Photo", objectID);
//        final DisplayImageOptions postoptions;
//        postoptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_stub)
//                .showImageForEmptyUri(R.drawable.ic_empty)
//                .showImageOnFail(R.drawable.ic_error)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .build();
//
//        final DisplayImageOptions profileptions;
//        profileptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_stub)
//                .showImageForEmptyUri(R.drawable.ic_empty)
//                .showImageOnFail(R.drawable.ic_error)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
//                .build();
//
//        final ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
//
//        final TextView detailusername = (TextView) findViewById(R.id.detailuserName);
//
//        Typeface face1 = Typeface.createFromAsset(this.getAssets(), "fonts/Brown-Regular.otf");
//        detailusername.setTypeface(face1);
//
//        final AVQuery commentquery = new AVQuery("Photo");
//        commentquery.whereEqualTo("objectId", objectID);
//        commentquery.include("user");
//
//        Log.e("detaildebug", "  " + objectID);
//
//        detailPost = (ImageView) findViewById(R.id.detailPost);
//        detailProfile = (ImageView) findViewById(R.id.detailProfile);

//        commentquery.findInBackground(
//                new FindCallback() {
//                    @Override
//                    public void done(List list, AVException e) {
//                        if (list != null) {
//                            detailObject = (AVObject) list.get(0);
//
//                            detailusername.setText(((AVUser) detailObject.get("user")).getUsername());
//
//                            AVFile userProfile = ((AVUser) detailObject.get("user")).getAVFile("profileImage");
//
//                            ImageLoader.getInstance().displayImage(userProfile.getUrl(), detailProfile, profileptions, animateFirstListener);
//
//                            Log.e("detaildebug", "?? " + detailObject.getAVFile("image").getUrl());
//
//
//
//
//                            ViewGroup.LayoutParams para;
//                            para = detailPost.getLayoutParams();
//                            WindowManager wm1 = getWindowManager();
//                            int width1 = wm1.getDefaultDisplay().getWidth();
//                            int height1 = wm1.getDefaultDisplay().getHeight();
//
//                            para.height = width1/16*10;
//                            para.width = width1;
//
//                            detailPost.setLayoutParams(para);
//
//
//
//                            ImageLoader.getInstance().displayImage(detailObject.getAVFile("image").getUrl(), detailPost, postoptions, animateFirstListener);
//
//                            final VideoView detailVideo = (VideoView) findViewById(R.id.detail_videoView);
//                            final ImageButton detailPlay = (ImageButton) findViewById(R.id.detail_videoplay);
//
//                            ViewGroup.LayoutParams para_video;
//                            para_video = detailVideo.getLayoutParams();
//                            WindowManager wm2 = getWindowManager();
//                            int width2 = wm2.getDefaultDisplay().getWidth();
//                            int height2 = wm2.getDefaultDisplay().getHeight();
//
//                            para_video.height = width2/16*10;
//                            para_video.width = width2;
//
//                            detailVideo.setLayoutParams(para_video);
//
//                            detailPost.setVisibility(View.VISIBLE);
//                            detailVideo.setVisibility(View.INVISIBLE);
//                            detailPlay.setVisibility(View.INVISIBLE);
//
//                            getWallImages();
//
//
//                            if(detailObject.get("isVideo")!=null){
//                                //detailPost.setVisibility(View.INVISIBLE);
//                                //detailVideo.setVisibility(View.VISIBLE);
//                                detailPlay.setVisibility(View.VISIBLE);
//
//                                AVFile postVideo = detailObject.getAVFile("video");
//                                Uri uri = Uri.parse(postVideo.getUrl());
//                                //holder.videoView.setMediaController(new MediaController(getActivity()));
//                                detailVideo.setVideoURI(uri);
//
//                                detailVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                                    @Override
//                                    public void onCompletion(MediaPlayer mp) {
//                                        //播放结束后的动作
//
//                                        detailPlay.setVisibility(View.VISIBLE);
//                                        detailVideo.setVisibility(View.INVISIBLE);
//
//                                        detailPost.setVisibility(View.VISIBLE);
//
//                                    }
//                                });
//
//                                detailPlay.setAlpha((float) 0.8);
//                                detailPlay.setOnClickListener(
//                                        new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                detailVideo.start();
//                                                detailPlay.setVisibility(View.INVISIBLE);
//                                                detailVideo.setVisibility(View.VISIBLE);
//
//                                                detailPost.setVisibility(View.INVISIBLE);
//                                            }
//                                        }
//                                );
//
//                            }
//
//                        }
//                    }
//                }
//        );


//        ImageButton backButton = (ImageButton) findViewById(R.id.detailbackButton);
//
//        backButton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                        DetailActivity.this.finish();
//                    }
//                }
//        );
//
//
//        detailProfile.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Intent intent = new Intent(DetailActivity.this, customProfileActivity.class);
//                        //intent.addFlags(Intent.FLAG_ACTIVITY__TO_FRONT);
//
////                        Log.e("jump debug", "" + ((AVUser) detailObject.get("user")).getObjectId());
//
//                        intent.putExtra("ObjectId", detailObject.getClass_id());
//                        intent.putExtra("user", UserBean.detailObjectTo(detailObject));
//                        startActivity(intent);
//
//                    }
//                }
//        );
        findViewById(R.id.transferbutton).setOnClickListener(this);
        if (detailObject != null)
            setDetails();
    }

    protected void onPause() {
        super.onPause();
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
            videoView.setVisibility(View.INVISIBLE);
            video_playbutton.setVisibility(View.VISIBLE);

            image.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickEvent(View v) {
        switch (v.getId()) {
            case R.id.transferbutton:
                if (detailObject != null) {
                    String url = "";
                    if (detailObject.getPost_type() == 1)
                        url = detailObject.getVideo_url();
                    else
                        url = detailObject.getCover_icon();
                    String content = "";
                    if (!TextUtils.isEmpty(detailObject.getContent())) {
                        content = detailObject.getContent();
                    }
                    baseShare.shareUtils.setUserId(detailObject.getClass_id());
                    baseShare.shareUtils.showShareDialog(url, detailObject.getClass_name(), content);
                }
                break;
        }
    }

    private void setDetails() {
        TextView text = (TextView) findViewById(R.id.userName);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Brown-Regular.otf");
        text.setTypeface(face);

        image = (ImageView) findViewById(R.id.Post);
        ViewGroup.LayoutParams para;
        para = image.getLayoutParams();
        WindowManager wm1 = getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        int height1 = wm1.getDefaultDisplay().getHeight();

        para.height = width1 / 16 * 10;
        para.width = width1;

        image.setLayoutParams(para);
        ImageView profile = (ImageView) findViewById(R.id.Profile);

        likecount = (TextView) findViewById(R.id.mainlikecount);
        Typeface face1 = Typeface.createFromAsset(getAssets(), "fonts/Brown-Light.otf");
        likecount.setTypeface(face1);
        TextView commentcount = (TextView) findViewById(R.id.maincommentcount);
        Typeface face2 = Typeface.createFromAsset(getAssets(), "fonts/Brown-Light.otf");
        commentcount.setTypeface(face1);

        TextView userAddress = (TextView) findViewById(R.id.userAddress);
        TextView timelabel = (TextView) findViewById(R.id.timelabel);
        Typeface face3 = Typeface.createFromAsset(getAssets(), "fonts/Brown-Light.otf");
        timelabel.setTypeface(face3);
        TextView commentcontent = (TextView) findViewById(R.id.main_commentcontent);
        View comment_line = (View) findViewById(R.id.comment_line);

        Typeface face4 = Typeface.createFromAsset(getAssets(), "fonts/Brown-Regular.otf");

        commentcount.setTypeface(face4);
        likecount.setTypeface(face4);
        commentcontent.setTypeface(face4);

        likebutton = (ImageButton) findViewById(R.id.mainlikebutton);
        ImageButton commentbutton = (ImageButton) findViewById(R.id.maincommentbutton);
        islike = false;

        videoView = (VideoView) findViewById(R.id.videoView);
        video_playbutton = (ImageButton) findViewById(R.id.video_playbutton);

        ViewGroup.LayoutParams para_video;
        para_video = videoView.getLayoutParams();
        WindowManager wm2 = getWindowManager();
        int width2 = wm2.getDefaultDisplay().getWidth();
        int height2 = wm2.getDefaultDisplay().getHeight();

        para_video.height = width2 / 16 * 10;
        para_video.width = width2;

        videoView.setLayoutParams(para_video);

        MediaPlayer player = new MediaPlayer();

        if (!TextUtils.isEmpty(detailObject.getClass_address())) {
            userAddress.setVisibility(View.VISIBLE);
            userAddress.setText(detailObject.getClass_address());
        } else {
            userAddress.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(detailObject.getContent())) {
            comment_line.setVisibility(View.VISIBLE);
            commentcontent.setVisibility(View.VISIBLE);
            commentcontent.setText(detailObject.getContent());

        } else {
            //holder.commentcontent.setHeight(0);
            //holder.commentcontent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            commentcontent.setVisibility(View.GONE);
            comment_line.setVisibility(View.GONE);
//                        holder.commentcontent.setText(indexListBean.getContent());
        }

//                } else {
//                    Log.e("abc", "commentcontent gone " + position);
//                    holder.commentcontent.setVisibility(View.GONE);
//                    //holder.commentcontent.setText(postobjectArray.get(position).get("comment").toString());
//                }

        videoView.setVisibility(View.INVISIBLE);
        image.setVisibility(View.VISIBLE);
        video_playbutton.setVisibility(View.INVISIBLE);

        if (detailObject.getPost_type() == 2) {

            //holder.videoView.setVisibility(View.VISIBLE);
            //holder.image.setVisibility(View.INVISIBLE);
//            video_playbutton.setVisibility(View.VISIBLE);

//                    AVFile postVideo = postobjectArray.get(position).getAVFile("video");
//                    if (postVideo != null && !TextUtils.isEmpty(postVideo.getUrl())) {
            Uri uri = Uri.parse(detailObject.getVideo_url());
            //holder.videoView.setMediaController(new MediaController(getActivity()));
            videoView.setVideoURI(uri);
//                    }
//            videoView.start();  // Video playing logic
//                    video_playbutton.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);
            image.setVisibility(View.VISIBLE);
            video_playbutton.setVisibility(View.VISIBLE);

            WindowManager wm = getWindowManager();

            int width = wm.getDefaultDisplay().getWidth();
            //int height = wm.getDefaultDisplay().getHeight();

            Log.e("video debug", " " + width);

            //holder.videoView.getHolder().setFixedSize(2160,2160/16*9);

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //播放结束后的动作
                    videoView.setVisibility(View.INVISIBLE);
                    video_playbutton.setVisibility(View.VISIBLE);

                    image.setVisibility(View.VISIBLE);

                }
            });

            video_playbutton.setAlpha((float) 0.8);
            video_playbutton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            videoView.start();
                            video_playbutton.setVisibility(View.INVISIBLE);
                            videoView.setVisibility(View.VISIBLE);

                            image.setVisibility(View.INVISIBLE);
                        }
                    }
            );


            //holder.videoView.setVisibility(View.INVISIBLE);
            //holder.videoView.requestFocus();


            //holder.videoView

        }


        profile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(DetailActivity.this, customProfileActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY__TO_FRONT);

//                        Log.e("jump debug", "" + ((AVUser) detailObject.get("user")).getObjectId());

                        intent.putExtra("ObjectId", detailObject.getClass_id());
                        intent.putExtra("user", UserBean.detailObjectTo(detailObject));
                        startActivity(intent);


                    }
                }
        );

        text.setText(detailObject.getClass_name());

        //Log.e("abc", "likiecount " + likecountArray);

//                Date curDate = new Date(System.currentTimeMillis());

//                long difference = postobjectArray.get(position).getCreatedAt().getTime() - curDate.getTime();
//                Log.v("Time difference:", String.valueOf(difference));
//
//                String timediff = "today";
//
//                if ((-difference) < 60 * 1000) {
//                    timediff = (-difference / 1000) + " Seconds ago";
//                    Log.e("abc", "timedebug " + timediff + "  " + difference);
//                } else if ((-difference) < 60 * 60 * 1000) {
//                    timediff = (-difference / 1000 / 60) + " Minutes ago";
//
//                } else if ((-difference) < 60 * 60 * 24 * 1000) {
//                    timediff = (-difference / 1000 / 60 / 60) + " Hours ago";
//                } else {
//                    //Log.e("timedebugging","??? "+(int)(-difference/1000/60/60/24)+"difff "+difference);
//                    timediff = (-difference / 1000 / 60 / 60 / 24) + " Days ago";
//                }
//
//                holder.timelabel.setText(timediff);

//                holder.timelabel.setText(DateUtils.dateProcess1(Long.parseLong(indexListBean.getTime())));
        timelabel.setText(TimeUtil.getTimeLong(detailObject.getTime()));
        commentcount.setText(detailObject.getComments_num() + "");
        likecount.setText(detailObject.getPraise_num() + "");

        if (detailObject.getIs_praise()) {
            likebutton.setBackground(getResources().getDrawable(R.drawable.mainlikeicon));
            islike = true;
        } else {
            likebutton.setBackground(getResources().getDrawable(R.drawable.maindislikeicon));
            islike = false;
        }

        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                islike = !islike;
                detailObject.setIs_praise(islike);
                if (islike) {
                    //點贊
                    detailObject.setPraise_num(detailObject.getPraise_num() + 1);
                    likebutton.setBackground(getResources().getDrawable(R.drawable.mainlikeicon));
                } else {
                    detailObject.setPraise_num(detailObject.getPraise_num() - 1);
                    likebutton.setBackground(getResources().getDrawable(R.drawable.maindislikeicon));
                }
                likecount.setText(detailObject.getPraise_num() + "");
                postDetailsPresenter.like(islike ? 1 : 2, detailObject.getPost_id(), new RequestListener() {
                    @Override
                    public void requestSuccess(String json) {
//                                Toast.makeText(getActivity(), holder.islike ? "点赞成功" : "取消点赞成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void requestError(String e) {
                        islike = !islike;
                        detailObject.setIs_praise(islike);
                        if (islike) {
                            //點贊
                            likebutton.setBackground(getResources().getDrawable(R.drawable.mainlikeicon));
                            detailObject.setPraise_num(detailObject.getPraise_num() + 1);
                        } else {
                            detailObject.setPraise_num(detailObject.getPraise_num() - 1);
                            likebutton.setBackground(getResources().getDrawable(R.drawable.maindislikeicon));
                        }
                        likecount.setText(detailObject.getPraise_num() + "");
                    }
                });
//                        if (!holder.islike) {
//
//                            holder.islike = true;
//                            Log.e("abc", "dislike" + position);
//                            holder.likebutton.setBackground(getResources().getDrawable(R.drawable.mainlikeicon));
//                            Integer a = likecountArray[position] + 1;
//                            holder.likecount.setText(a.toString());
//
//                            existinglikeArr.add(position);
//                            likecountArray[position] = likecountArray[position] + 1;
//
//                            AVObject likeobject = new AVObject("Activity");
//                            likeobject.put("type", "like");
//                            likeobject.put("fromUser", AVUser.getCurrentUser());
//                            likeobject.put("toUser", (AVUser) postobjectArray.get(position).get("user"));
//                            likeobject.put("photo", postobjectArray.get(position));
//
//                            AVACL likeACL = new AVACL();
//                            likeACL.setPublicReadAccess(true);// 设置公开的「读」权限，任何人都可阅读
//                            likeACL.setWriteAccess(AVUser.getCurrentUser(), true);//为当前用户赋予「写」权限
//
//                            likeobject.setACL(likeACL);
//
//                            likeobject.saveInBackground(new SaveCallback() {
//                                @Override
//                                public void done(AVException e) {
//                                    if (e == null) {
//                                        // 存储成功
//
//                                        AVQuery pushQuery = AVInstallation.getQuery();
//                                        pushQuery.whereEqualTo("owner", (AVUser) postobjectArray.get(position).get("user"));
//
//                                        AVPush.sendMessageInBackground(AVUser.getCurrentUser().getUsername() + " liked your post", pushQuery, new SendCallback() {
//                                            @Override
//                                            public void done(AVException e) {
//                                                Log.e("abc", "like push done");
//                                            }
//                                        });
//
//
//                                    } else {
//                                        // 失败的话，请检查网络环境以及 SDK 配置是否正确
//                                    }
//                                }
//                            });
//
//
//                        } else {
//                            holder.islike = false;
//                            Log.e("abc", "like!" + position);
//                            holder.likebutton.setBackground(getResources().getDrawable(R.drawable.maindislikeicon));
//                            Integer a = likecountArray[position] - 1;
//                            holder.likecount.setText(a.toString());
//
//                            boolean b = existinglikeArr.remove((Integer) position);
//                            likecountArray[position] = likecountArray[position] - 1;
//
////                        AVObject likeobject = new AVObject("Activity");
////                        likeobject.put("type", "like");
////                        likeobject.put("fromUser", AVUser.getCurrentUser());
////                        likeobject.put("toUser", (AVUser) postobjectArray.get(position).get("user"));
////                        likeobject.put("photo", postobjectArray.get(position));
////
////                        likeobject.deleteEventually();
//
//                            AVQuery existlike = new AVQuery("Activity");
//                            existlike.whereEqualTo("photo", postobjectArray.get(position));
//                            existlike.whereEqualTo("type", "like");
//                            existlike.whereEqualTo("fromUser", AVUser.getCurrentUser());
//
//                            existlike.findInBackground(new FindCallback() {
//                                                           @Override
//                                                           public void done(List list, AVException e) {
//
//                                                               for (int i = 0; i < list.size(); i++) {
//
//                                                                   AVObject a = (AVObject) list.get(i);
//                                                                   a.deleteEventually();
//                                                               }
//                                                           }
//                                                       }
//                            );
//
//
//                        }

            }
        });

        commentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetailActivity.this, CommentActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY__TO_FRONT);
                intent.putExtra("ObjectId", detailObject.getPost_id());
                intent.putExtra("post", detailObject);
                startActivity(intent);


            }
        });

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();

        DisplayImageOptions profileoptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();

        //Log.e("abc", "getView" + (String) x.getUsername());
        ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        if (detailObject.getPost_type() == 1)
            ImageLoader.getInstance().
                    displayImage(detailObject.getVideo_url(), image, options, animateFirstListener);
        else
            ImageLoader.getInstance().

                    displayImage(detailObject.getCover_icon(), image, options, animateFirstListener);

        ImageLoader.getInstance().

                displayImage(detailObject.getClass_icon(), profile, profileoptions, animateFirstListener);

    }


    public void getWallImages() {


//        final AVQuery<AVObject> likequery = new AVQuery<>("Activity");
//        likequery.whereEqualTo("type", "like");
//        likequery.whereEqualTo("photo", detailObject);
//        likequery.findInBackground(
//                new FindCallback<AVObject>() {
//                    @Override
//                    public void done(List<AVObject> list, AVException e) {
//
//                        Integer x = list.size();
//
//                        likecount = list.size();
//
//                        detaillikeCount.setText(x.toString());
//
//                        likequery.whereEqualTo("fromUser", AVUser.getCurrentUser());
//
//                        likequery.findInBackground(
//                                new FindCallback<AVObject>() {
//                                    @Override
//                                    public void done(List<AVObject> list, AVException e) {
//                                        if (list.size() > 0) {
//                                            detail_likebutton.setBackground(getResources().getDrawable(R.drawable.mainlikeicon));
//                                            islike = true;
//                                        }
//                                    }
//                                }
//                        );
//
//                    }
//                }
//        );
//
//        AVQuery<AVObject> commentquery = new AVQuery<>("Activity");
//        commentquery.whereEqualTo("type", "comment");
//        commentquery.whereEqualTo("photo", detailObject);
//        commentquery.findInBackground(
//                new FindCallback<AVObject>() {
//                    @Override
//                    public void done(List<AVObject> list, AVException e) {
//
//                        Integer x = list.size();
//
//                        commentcount = list.size();
//
//                        detailcommentCount.setText(x.toString());
//
//                    }
//                }
//        );
//        Date curDate = new Date(System.currentTimeMillis());
//
//        long difference = detailObject.getCreatedAt().getTime() - curDate.getTime();
//        Log.v("Time difference:", String.valueOf(difference));
//
//        String timediff = "today";
//        if (-difference<60*1000)
//            timediff = (int)(-difference/1000)+" Seconds ago";
//        else if (-difference<60*60*1000){
//            timediff = (int)(-difference/1000/60)+" Minutes ago";
//
//        }
//        else if (-difference<60*60*24*1000){
//            timediff = (int)(-difference/1000/60/60)+" Hours ago";
//        }
//        else {
//            //Log.e("timedebugging","??? "+(int)(-difference/1000/60/60/24)+"difff "+difference);
//            timediff = (int)(-difference/1000/60/60/24)+" Days ago";
//        }


    }

    @Override
    public void setSuccess(IndexBean.IndexListBean postDetailsBean) {
        detailObject = postDetailsBean;
        if (detailObject != null)
            setDetails();
    }

    @Override
    public void setFail(String errInfo) {

    }

//    @Override
//    public void onResume() {
//
//        super.onResume();
//        getWallImages();
//        Log.e("abc","detail resume");
//
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
}
