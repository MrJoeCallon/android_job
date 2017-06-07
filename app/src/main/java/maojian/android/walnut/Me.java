package maojian.android.walnut;

/**
 * Created by android on 19/7/16.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.*;
import android.widget.*;

import com.amap.api.maps.AMap;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.*;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.*;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import maojian.android.walnut.data.UserInfos;
import maojian.android.walnut.me.MePresenter;
import maojian.android.walnut.me.MeView;
import maojian.android.walnut.me.MyPostList;
import maojian.android.walnut.me.userdate.UserBean;
import maojian.android.walnut.utils.Constants;
import maojian.android.walnut.utils.MarkerActivity;
import maojian.android.walnut.utils.ToastUtil;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.*;
import maojian.android.walnut.utils.UiUtils;
import maojian.android.walnut.volley.RequestListener;

@SuppressLint("NewApi")
public class Me extends Fragment implements MeView, OnMarkerClickListener,
        OnInfoWindowClickListener, OnMarkerDragListener, OnMapLoadedListener,
        InfoWindowAdapter {

    private MePresenter mePresenter;
    //    private MapView mapView;
    private com.amap.api.maps.model.MarkerOptions markerOption;
    private TextView markerText;
    private RadioGroup radioOption;
    private AMap aMap;
    private com.amap.api.maps.MapView mapView;
    private Marker marker2;// 有跳动效果的marker对象
    private com.amap.api.maps.model.LatLng latlng = new com.amap.api.maps.model.LatLng(23.124664, 113.322334);
    private MapView map;
    private UserBean userBean;

//    private MapView mapView1;
//    private MapboxMap map1;

    private LocationServices locationServices;

//    AVUser currentUser;

    //ScrollView scrollView;

    ScrollviewCompat scrollView;

    LinearLayout l1;
    LinearLayout l2;

    private ImageView currentprofileimage;

    TextView viewpager1_postlabel;

    private TextView usernameLabel;
    private TextView historyLabel;
    private TextView fansLabel;
    private TextView followingLabel;

    private Button editprofile_button;

    private TextView me_fansbutton;
    private TextView me_followingbutton;
    private TextView profile_bio;

    private GridView listView;

    private List<MyPostList.MyPostBean> myPostList;
    private List<MyPostList.MyPostBean> likePostList;

    private ImageAdapter discoverpostadapter;

    View rootView;

    private ViewPagerCompat viewPager;//页卡内容
    private ImageView imageView;// 动画图片
    private TextView textView1, textView2;
    private List<View> views;// Tab页面列表
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private View view1, view2;//各个页卡

    private ImageButton order_button;
    private ImageButton discount_button;

    //private ImageView viewpager_cursor;

    private ImageButton multi_post;
    private ImageButton single_post;
    private ImageButton like_post;

    private TextView post_text;
    private TextView mark_text;

    private boolean islike_post;

    private ArrayList<PointValue> values;

    private ArrayList<PointValue> existing_usage;


    //图片
    private static final int DEFAULT_DATA = 0;
    private static final int SUBCOLUMNS_DATA = 1;
    private static final int STACKED_DATA = 2;
    private static final int NEGATIVE_SUBCOLUMNS_DATA = 3;
    private static final int NEGATIVE_STACKED_DATA = 4;

    private ColumnChartView chart;
    private ColumnChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;
    private int dataType = DEFAULT_DATA;
    HorizontalScrollView horizontalScrollView;
    MapboxMap mapboxMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MapboxAccountManager.start(getActivity(), "pk.eyJ1IjoibXJqb2UxMjMiLCJhIjoiY2ozMnA4N2xqMDA0MDJxbnE2MmRvOXhhZSJ9.mEJgFmdaUTiMt41ZyJiY2g");
        rootView = inflater.inflate(R.layout.fragment_me, container, false);
        mePresenter = new MePresenter(getActivity(), this);
        myPostList = new ArrayList<MyPostList.MyPostBean>();
        likePostList = new ArrayList<MyPostList.MyPostBean>();
        currentprofileimage = (ImageView) rootView.findViewById(R.id.me_profileimage);

        usernameLabel = (TextView) rootView.findViewById(R.id.me_username);
        profile_bio = (TextView) rootView.findViewById(R.id.profile_bio);
        userBean = UserInfos.getUserBean();
        if (userBean != null && userBean.getUserinfo() != null) {
            profile_bio.setText(userBean.getUserinfo().getInfo());

        }
        //historyLabel = (TextView) rootView.findViewById(R.id.me_historylabel);
        fansLabel = (TextView) rootView.findViewById(R.id.me_fanslabel);
        followingLabel = (TextView) rootView.findViewById(R.id.me_followinglabel);

        Typeface face1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Brown-Regular.otf");
        usernameLabel.setTypeface(face1);
        //historyLabel.setTypeface(face1);
        fansLabel.setTypeface(face1);
        followingLabel.setTypeface(face1);

        islike_post = false;
        //viewpager_cursor = (ImageView) rootView.findViewById(R.id.viewpager_cursor);
//        currentUser = AVUser.getCurrentUser();
//        queryParseMethod();

        InitImageView();

        InitViewPager();
        InitTextView();

        listView = (GridView) view1.findViewById(R.id.viewpager1_gridView);

        viewpager1_postlabel = (TextView) view1.findViewById(R.id.viewpager1_postlabel);

        Typeface postlabelface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Brown-Regular.otf");
        viewpager1_postlabel.setTypeface(postlabelface);

        multi_post = (ImageButton) view1.findViewById(R.id.multi_postbutton);
        single_post = (ImageButton) view1.findViewById(R.id.single_postbutton);
        like_post = (ImageButton) view1.findViewById(R.id.like_postbutton);

        multi_post.setBackground(getResources().getDrawable(R.drawable.multi_post1));
        single_post.setBackground(getResources().getDrawable(R.drawable.single_post));
        like_post.setBackground(getResources().getDrawable(R.drawable.like_post));

        multi_post.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setNumColumns(2);
                islike_post = false;

                multi_post.setBackground(getResources().getDrawable(R.drawable.multi_post1));
                single_post.setBackground(getResources().getDrawable(R.drawable.single_post));
                like_post.setBackground(getResources().getDrawable(R.drawable.like_post));

                viewpager1_postlabel.setText("My Post");
                discoverpostadapter = new ImageAdapter(getActivity(), myPostList);
                ((GridView) listView).setAdapter(discoverpostadapter);
                discoverpostadapter.notifyDataSetChanged();
            }
        });

        single_post.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setNumColumns(1);
                islike_post = false;

                multi_post.setBackground(getResources().getDrawable(R.drawable.multi_post));
                single_post.setBackground(getResources().getDrawable(R.drawable.single_post1));
                like_post.setBackground(getResources().getDrawable(R.drawable.like_post));

                viewpager1_postlabel.setText("My Post");
                discoverpostadapter = new ImageAdapter(getActivity(), myPostList);
                ((GridView) listView).setAdapter(discoverpostadapter);
                discoverpostadapter.notifyDataSetChanged();
            }
        });

        like_post.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                islike_post = true;
                listView.setNumColumns(1);
                mePresenter.likePost(0);
                multi_post.setBackground(getResources().getDrawable(R.drawable.multi_post));
                single_post.setBackground(getResources().getDrawable(R.drawable.single_post));
                like_post.setBackground(getResources().getDrawable(R.drawable.like_post1));

                viewpager1_postlabel.setText("Post You've liked");
                discoverpostadapter = new ImageAdapter(getActivity(), likePostList);
                ((GridView) listView).setAdapter(discoverpostadapter);
                discoverpostadapter.notifyDataSetChanged();
            }
        });

        editprofile_button = (Button) rootView.findViewById(R.id.editprofile_button);

        editprofile_button.setTypeface(face1);

        editprofile_button.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UserInfos.getLoginBean() == null) {
                            Intent intent = new Intent(getActivity(), LoginDialogActivity.class);
                            startActivity(intent);
                            return;
                        }
                        Intent intent = new Intent(getActivity(), EditprofileActivity.class);
//                        Intent intent = new Intent(getActivity(), MarkerActivity.class);
                        startActivity(intent);

                    }
                }
        );


        post_text = (TextView) rootView.findViewById(R.id.post_tab);
        mark_text = (TextView) rootView.findViewById(R.id.mark_tab);
        post_text.setTypeface(face1);
        mark_text.setTypeface(face1);
//        post_text.setTextColor(Color.parseColor("#00EAAB"));
//        mark_text.setTextColor(Color.parseColor("#A2A2A2"));


        //me_historybutton = (TextView) rootView.findViewById(R.id.me_historybutton);

        me_fansbutton = (TextView) rootView.findViewById(R.id.me_fansbutton);
        rootView.findViewById(R.id.me_fans).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UserInfos.getLoginBean() == null) {
                            Intent intent = new Intent(getActivity(), LoginDialogActivity.class);
                            startActivity(intent);
                            return;
                        }
                        Intent intent = new Intent(getActivity(), ProfileFollowActivity.class);
                        intent.putExtra("type", "fans");
                        intent.putExtra("ObjectId", UserInfos.getLoginBean().getUser_id());
                        startActivity(intent);

                    }
                }
        );

        me_followingbutton = (TextView) rootView.findViewById(R.id.me_followingbutton);
        rootView.findViewById(R.id.me_following).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UserInfos.getLoginBean() == null) {
                            Intent intent = new Intent(getActivity(), LoginDialogActivity.class);
                            startActivity(intent);
                            return;
                        }
                        Intent intent = new Intent(getActivity(), ProfileFollowActivity.class);
                        intent.putExtra("type", "following");
                        intent.putExtra("ObjectId", UserInfos.getLoginBean().getUser_id());
                        startActivity(intent);

                    }
                }
        );


        //me_historybutton.setTypeface(face1);
        me_fansbutton.setTypeface(face1);
        me_followingbutton.setTypeface(face1);

//        scrollView = (ScrollView) view2.findViewById(R.id.me_scrollView2);
//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                Log.e("map debug", "onTouch scrollView");
//                if(v.getClass().getName().equals("com.mapbox.mapboxsdk.maps.MapView")) {
//
//                    Log.e("map debug","ViewPagerCompat canScroll");
//
//                    scrollView.requestDisallowInterceptTouchEvent(true);
//
//                    return true;
//                }
//
//                return false;
//            }
//        });

        //scrollView.requestDisallowInterceptTouchEvent(false);

        locationServices = LocationServices.getLocationServices(getActivity());
        enableGps();
//        mapView = (MapView) view2.findViewById(R.id.mapView);
 /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
//        MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
        mapView = (com.amap.api.maps.MapView) view2.findViewById(R.id.map);

        l1 = (LinearLayout) view2.findViewById(R.id.viewpager_lay2_l1);
        l2 = (LinearLayout) view2.findViewById(R.id.viewpager_lay2_l2);

        chart = (ColumnChartView) view2.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        horizontalScrollView = (HorizontalScrollView) view2.findViewById(R.id.horizontalScrollView2);
        if (userBean != null && userBean.getUserinfo() != null) {
            profile_bio.setText(userBean.getUserinfo().getInfo());
            generateDefaultData();


        } else {
            generateDefaultData();
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 200L);

        mapView.onCreate(savedInstanceState);


        init();

        //地图
        MapboxMapOptions options = new MapboxMapOptions()
                .styleUrl(Style.OUTDOORS)
                .camera(new CameraPosition.Builder()
                        .target(new LatLng(43.7383, 7.4094))
                        .zoom(12)
                        .build());


        map = (MapView) view2.findViewById(R.id.mapView);
        map.onCreate(savedInstanceState);
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap1) {
                mapboxMap = mapboxMap1;
                LatLng location = new LatLng();
                location.setLatitude(BaseConstant.latitude);
                location.setLongitude(BaseConstant.longitude);
                mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 12));

//
//                mapboxMap.moveCamera(new CameraUpdate() {
//                    @Override
//                    public CameraPosition getCameraPosition(@NonNull MapboxMap mapboxMap) {
//                        mapboxMap.
//                        return null;
//                    }
//                });CameraUpdate
//               CameraPosition cameraPosition = mapboxMap.getCameraPosition();
//                cameraPosition.zoom
//                new CameraPosition(latLng,10,0,0);
//                .
//               mapboxMap.setCameraPosition(cameraPosition);
//                IconFactory iconFactory = IconFactory.getInstance(getActivity());
//                Drawable iconDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.deviceselected);
//                Icon icon = iconFactory.fromDrawable(iconDrawable);
//
//                // The easiest way to add a marker view
//                mapboxMap.addMarker(new MarkerViewOptions()
//                        .position(new LatLng(-37.821629, 144.978535)));
//
//                // marker view using all the different options available
//                mapboxMap.addMarker(new MarkerViewOptions()
//                        .position(new LatLng(-37.822829, 144.981842))
//                        .icon(icon)
//                        .rotation(90)
//                        .anchor(0.5f, 0.5f)
//                        .alpha(0.5f)
//                        .title("Hisense Arena")
//                        .snippet("Olympic Blvd, Melbourne VIC 3001, Australia")
//                        .infoWindowAnchor(0.5f, 0.5f)
//                        .flat(true));
            }
        });


        return rootView;

    }

    private void generateDefaultData() {
        int numColumns = 30;
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValuesX = new ArrayList<AxisValue>();

        Date dNow = new Date();   //当前时间
//
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历

        calendar.setTime(dNow);//把当前时间赋给日历

        dNow.setTime(dNow.getTime() - dNow.getTime() % (24 * 3600 * 1000) - 8 * 3600 * 1000);
        Log.e("historyusa??", "debuggggg " + ((dNow.getTime() - dBefore.getTime()) / 3600 / 1000));

        SimpleDateFormat formatter = new SimpleDateFormat("d");//M.
        String dateString = formatter.format(dNow);
        calendar.add(Calendar.DAY_OF_MONTH, -(29));  //设置为前30天

        for (int i = 0; i < numColumns; i++) {
            dBefore = calendar.getTime();
            dateString = formatter.format(dBefore);
            axisValuesX.add(new AxisValue(i).setValue(i).setLabel(
                    dateString));// 添加X轴显示的刻度值并设置X轴显示的内容

            values = new ArrayList<SubcolumnValue>();
//            SubcolumnValue subcolumnValue = new SubcolumnValue((float) Math.random() * 50f + 5, i == 29 ? Color.parseColor("#00EAAB") : Color.parseColor("#E8E8E8"));
            int num = numColumns - i - 1;
            float value = 0;
            if (userBean != null && userBean.getInfo() != null)
                for (int j = 0; j < userBean.getInfo().size(); j++) {
                    String infoDay = userBean.getInfo().get(j).getTime().substring(userBean.getInfo().get(j).getTime().length() - 2,
                            userBean.getInfo().get(j).getTime().length());
                    Log.d("infoDay=", infoDay + " dateString=" + dateString);
                    if (dateString.endsWith(infoDay)) {
                        value = userBean.getInfo().get(j).getToday_usage();
                        break;
                    }
                }
            SubcolumnValue subcolumnValue = new SubcolumnValue(value, i == 29 ? Color.parseColor("#00EAAB") : Color.parseColor("#E8E8E8"));
//            subcolumnValue.setLabel("Label");
            values.add(subcolumnValue);

            Column column = new Column(values);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(true);
            columns.add(column);


            calendar.add(Calendar.DAY_OF_MONTH, (1));  //设置为前一天
        }

        data = new ColumnChartData(columns);

        if (hasAxes) {
            Axis axisX = new Axis(axisValuesX);
            axisX.setTextColor(R.color.C3);
            if (getActivity() == null)
                return;
            Typeface face1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Brown-Regular.otf");
            axisX.setTypeface(face1);
//            axisX.setTextSize(UiUtils.dip2px(getActivity(), 10));
//            data.setAxisXBottom(new Axis(axisValues).setHasLines(true));

//            Axis axisY = new Axis().setHasLines(true);
//            if (hasAxesNames) {
//                axisX.setName("");
////                axisY.setName("Axis Y");
//            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(null);
//            data.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        chart.setColumnChartData(data);

    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
//            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
//        markerText = (TextView) findViewById(R.id.mark_listenter_text);
//        radioOption = (RadioGroup) findViewById(R.id.custom_info_window_options);
//        Button clearMap = (Button) findViewById(R.id.clearMap);
//        clearMap.setOnClickListener(this);
//        Button resetMap = (Button) findViewById(R.id.resetMap);
//        resetMap.setOnClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        addMarkersToMap();// 往地图上添加marker
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        map.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        map.onDestroy();
    }


    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
//		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
//		giflist.add(BitmapDescriptorFactory
//				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//		giflist.add(BitmapDescriptorFactory
//				.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//		giflist.add(BitmapDescriptorFactory
//				.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//		aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//				.position(Constants.CHENGDU).title("成都市")
//				.snippet("成都市:30.679879, 104.064855").icons(giflist)
//				.perspective(true).draggable(true).period(50));
//
//		markerOption = new MarkerOptions();
//		markerOption.position(Constants.XIAN);
//		markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
//		markerOption.perspective(true);
//		markerOption.draggable(true);
//		markerOption.icon(
////				BitmapDescriptorFactory
////				.fromResource(R.drawable.location_marker)
//				BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker))
//				);
//		//将Marker设置为贴地显示，可以双指下拉看效果
//		markerOption.setFlat(true);
//		marker2 = aMap.addMarker(markerOption);
//        drawMarkers();// 添加1个带有系统默认icon的marker
        if (myPostList != null) {
            for (final MyPostList.MyPostBean postBean : myPostList)
                if (!TextUtils.isEmpty(postBean.getLat()) && !TextUtils.isEmpty(postBean.getLng())) {
                    String url = "";
                    if (postBean.getType() == 1)
                        url = postBean.getRurl();
                    else
                        url = postBean.getCover_icon();
                    ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                            drawMarkers(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker),
                                    new com.amap.api.maps.model.LatLng(Double.parseDouble(postBean.getLat()), Double.parseDouble(postBean.getLng())));

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            if (bitmap == null)
                                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location_marker);
                            bitmap =  zoomImg(bitmap, 80, 80);
                            IconFactory iconFactory = IconFactory.getInstance(getActivity());
                            Icon icon = iconFactory.fromBitmap(bitmap);
                            if (mapboxMap != null)
                                mapboxMap.addMarker(new MarkerViewOptions()
                                        .icon(icon)
                                        .position(new LatLng(Double.parseDouble(postBean.getLat()),Double.parseDouble(postBean.getLng()))));

                            drawMarkers(bitmap,
                                    new com.amap.api.maps.model.LatLng(Double.parseDouble(postBean.getLat()), Double.parseDouble(postBean.getLng())));

                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
                }
        }

    }

    /**
     * 处理图片
     *
     * @param bm        所要转换的bitmap
     * @param newWidth  新的宽
     * @param newHeight 新的高
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 绘制系统默认的1种marker背景图片
     */
    public void drawMarkers(Bitmap bitmap, com.amap.api.maps.model.LatLng latlng) {
        Log.d("bit", "getHeight=" + bitmap.getHeight() + "getWidth" + bitmap.getWidth());
        Marker marker = aMap.addMarker(new com.amap.api.maps.model.MarkerOptions()
                .position(latlng)
//                .title("哥的位置")
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .setFlat(true)//设置marker平贴地图效果
                .perspective(true));//.draggable(true)
        marker.setRotateAngle(90);// 设置marker旋转90度
        marker.showInfoWindow();// 设置默认显示一个infowinfow

    }

    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.equals(marker2)) {
            if (aMap != null) {
                jumpPoint(marker);
            }
        }
        markerText.setText("你点击的是" + marker.getTitle());
        return false;
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        Point startPoint = proj.toScreenLocation(Constants.XIAN);
        startPoint.offset(0, -100);
        final com.amap.api.maps.model.LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * Constants.XIAN.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * Constants.XIAN.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new com.amap.api.maps.model.LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    /**
     * 监听点击infowindow窗口事件回调
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        ToastUtil.show(getActivity(), "你点击了infoWindow窗口" + marker.getTitle());
        ToastUtil.show(getActivity(), "当前地图可视区域内Marker数量:"
                + aMap.getMapScreenMarkers().size());
    }

    /**
     * 监听拖动marker时事件回调
     */
    @Override
    public void onMarkerDrag(Marker marker) {
        String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n("
                + marker.getPosition().latitude + ","
                + marker.getPosition().longitude + ")";
        markerText.setText(curDes);
    }

    /**
     * 监听拖动marker结束事件回调
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        markerText.setText(marker.getTitle() + "停止拖动");
    }

    /**
     * 监听开始拖动marker事件回调
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
        markerText.setText(marker.getTitle() + "开始拖动");
    }

    /**
     * 监听amap地图加载成功事件回调
     */
    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(Constants.XIAN).include(Constants.CHENGDU)
                .include(latlng).build();
        aMap.moveCamera(com.amap.api.maps.CameraUpdateFactory.newLatLngBounds(bounds, 150));
    }

    /**
     * 监听自定义infowindow窗口的infocontents事件回调
     */
    @Override
    public View getInfoContents(Marker marker) {
        if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_contents) {
            return null;
        }
        View infoContent = getActivity().getLayoutInflater().inflate(
                R.layout.custom_info_contents, null);
        render(marker, infoContent);
        return infoContent;
    }

    /**
     * 监听自定义infowindow窗口的infowindow事件回调
     */
    @Override
    public View getInfoWindow(Marker marker) {
        if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_window) {
            return null;
        }
        View infoWindow = getActivity().getLayoutInflater().inflate(
                R.layout.custom_info_window, null);

        render(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_contents) {
            ((ImageView) view.findViewById(R.id.badge))
                    .setImageResource(R.drawable.badge_sa);
        } else if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_window) {
            ImageView imageView = (ImageView) view.findViewById(R.id.badge);
            imageView.setImageResource(R.drawable.badge_wa);
        }
        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
                    titleText.length(), 0);
            titleUi.setTextSize(15);
            titleUi.setText(titleText);

        } else {
            titleUi.setText("");
        }
        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null) {
            SpannableString snippetText = new SpannableString(snippet);
            snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
                    snippetText.length(), 0);
            snippetUi.setTextSize(20);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("");
        }
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            /**
//             * 清空地图上所有已经标注的marker
//             */
//            case R.id.clearMap:
//                if (aMap != null) {
//                    aMap.clear();
//                }
//                break;
//            /**
//             * 重新标注所有的marker
//             */
//            case R.id.resetMap:
//                if (aMap != null) {
//                    aMap.clear();
//                    addMarkersToMap();
//                }
//                break;
//            default:
//                break;
//        }
//    }

    public void queryParseMethod() {
        if (UserInfos.getLoginBean() == null)
            return;
        mePresenter.userInfo(UserInfos.getLoginBean().getUser_id());
        mePresenter.getMyPost(0);
//        mePresenter.likePost(0);
        mePresenter.sendMileage(new RequestListener() {
            @Override
            public void requestSuccess(String json) {

            }

            @Override
            public void requestError(String e) {

            }
        });
//        Log.e("main bug", "!!! " + currentUser.get("profileImage").toString());
//
//        if(currentUser.get("profileImage").toString().contains("AVObject")){
//
//            AVObject currentobject = (AVObject) currentUser.get("profileImage");
//
//            AVQuery<AVObject> fansquery = new AVQuery<>("_File");
//            fansquery.whereEqualTo("objectId",currentobject.getObjectId());
//
//
//
//        }
//        else {
//
//            AVFile currentProfile = (AVFile) currentUser.get("profileImage");
//            Log.e("main bug", "??? " + currentUser.getUsername());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();

        ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        if (UserInfos.getLoginBean() != null) {
            ImageLoader.getInstance().displayImage(UserInfos.getLoginBean().getHeader_image(), currentprofileimage, options, animateFirstListener);

            usernameLabel.setText(UserInfos.getLoginBean().getUser_name());
            fansLabel.setText(UserInfos.getLoginBean().getFans_num() + "");
            followingLabel.setText(UserInfos.getLoginBean().getFans_num() + "");
        }


    }

    TextView today_text;
    TextView total_text;

    private void InitViewPager() {
//        MapboxAccountManager.start(getActivity(), "pk.eyJ1IjoiZHJhZ21lcGx6IiwiYSI6ImNpaWp6eDEweTAxOTF0cGtwZmwwaDhmcXMifQ.QMz7SFg6hGGmfo48w6eC8Q");
//        MapboxAccountManager.start(getActivity(), "pk.eyJ1IjoibXJqb2UxMjMiLCJhIjoiY2ozMnA4N2xqMDA0MDJxbnE2MmRvOXhhZSJ9.mEJgFmdaUTiMt41ZyJiY2g");

        viewPager = (ViewPagerCompat) rootView.findViewById(R.id.viewpager);
        views = new ArrayList<View>();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view1 = inflater.inflate(R.layout.viewpager_lay1, null);
        view2 = inflater.inflate(R.layout.viewpager_lay2, null);
        views.add(view2);
        views.add(view1);
        viewPager.setAdapter(new MyViewPagerAdapter(views));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                Log.e("map debug", "onTouch viewpager");
//
//                return false;
//            }
//        });


        today_text = (TextView) view2.findViewById(R.id.today_textview);
        total_text = (TextView) view2.findViewById(R.id.total_textview);
        if (userBean != null && userBean.getUserinfo() != null) {
            today_text.setText(userBean.getUserinfo().getToday_usage() + "");
            total_text.setText(userBean.getUserinfo().getAll_usage() + "");
        }

//        LineView lineView = (LineView)view2.findViewById(R.id.line_view);
//        lineView.setDrawDotLine(false); //optional
//        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE); //optional
//
//        ArrayList<String> test = new ArrayList<String>();
//        for (int i=0; i<17; i++){
//            test.add(String.valueOf(i+1));
//        }
//
//        ArrayList<Integer> dataList = new ArrayList<Integer>();
//        int random = (int)(Math.random()*20+1);
//        for (int i=0; i<17; i++){
//            dataList.add((int)(Math.random()*random));
//        }
//
//        lineView.setBottomTextList(test);
//
//
//
//        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
//        dataLists.add(dataList);
//        lineView.setDataList(dataLists);

        int day_range = 7;

        values = new ArrayList<PointValue>();

//        values.add(new PointValue(0, 2));
//        values.add(new PointValue(1, 4));
//        values.add(new PointValue(2, 3));
//        values.add(new PointValue(3, (float) 2.7));
//        values.add(new PointValue(4, (float) 3.4));
//        values.add(new PointValue(5, (float) 1.4));
//        values.add(new PointValue(6, (float) 2.6));
//        values.add(new PointValue(0, (float)0.0));
//        values.add(new PointValue(1, (float)0.0));
//        values.add(new PointValue(2, (float)0.0));
//        values.add(new PointValue(3, (float)0.0));
//        values.add(new PointValue(4, (float)0.0));
//        values.add(new PointValue(5, (float)0.0));
//        values.add(new PointValue(6, (float)0.0));

        //In most cased you can call data model methods in builder-pattern-like manner.
        Line line = new Line(values).setColor(R.color.lightgray).setCubic(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        List<String> date_string_array = new ArrayList<String>();

        Date dNow = new Date();   //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历

        SimpleDateFormat formatter = new SimpleDateFormat("M.d");
        String dateString = formatter.format(dNow);

        List<AxisValue> axisValues = new ArrayList<AxisValue>();

        existing_usage = new ArrayList<PointValue>();

        AVQuery<AVObject> odometerquery = new AVQuery<>("Odometer");

//        Log.e("historyusage","debugging "+currentUser.get("username"));
//        odometerquery.whereEqualTo("fromUser", currentUser);
        odometerquery.whereEqualTo("type", "odometer");
        odometerquery.include("Today");
        odometerquery.include("Total");
        odometerquery.orderByDescending("createdAt");
        odometerquery.limit(7);

//        odometerquery.findInBackground(new FindCallback<AVObject>() {
//            @Override
//            public void done(List<AVObject> list, AVException e) {
//
//                if (list != null) {
//
//                    if (list.size() > 0) {
//
//                        Log.e("historyusage", "debugging");
//                        odometerObjectsArray = list;
//
//                        Date curDate = new Date(System.currentTimeMillis());
//
//                        long difference = odometerObjectsArray.get(0).getCreatedAt().getTime() - curDate.getTime();
//
//                        if (-difference <= 1000 / 60 / 60 / 24) {
//
//                            today_text.setText(((AVObject) list.get(0)).getDouble("Today") + "");
//                        } else {
//                            //total_text.setText("0.0");
//                            today_text.setText("0.0");
//                        }
//                        total_text.setText(((AVObject) list.get(0)).getDouble("Total") + "");
//                        int day_range = 7;
//
//                        Line line = new Line(values).setColor(R.color.lightgray).setCubic(true);
//                        List<Line> lines = new ArrayList<Line>();
//                        lines.add(line);
//
//                        LineChartData data = new LineChartData();
//                        data.setLines(lines);
//
//                        List<String> date_string_array = new ArrayList<String>();
//
//                        Date dNow = new Date();   //当前时间
//
//                        Date dBefore = new Date();
//                        Calendar calendar = Calendar.getInstance(); //得到日历
//
//                        calendar.setTime(dNow);//把当前时间赋给日历
//
//                        dNow.setTime(dNow.getTime() - dNow.getTime() % (24 * 3600 * 1000) - 8 * 3600 * 1000);
//                        Log.e("historyusa??", "debuggggg " + ((dNow.getTime() - dBefore.getTime()) / 3600 / 1000));
//
//                        SimpleDateFormat formatter = new SimpleDateFormat("M.d");
//                        String dateString = formatter.format(dNow);
//
//                        List<AxisValue> axisValues = new ArrayList<AxisValue>();
//
//                        for (int i = 0; i < day_range; i++) {
//
//                            dBefore = calendar.getTime();
//                            dateString = formatter.format(dBefore);
//
//                            date_string_array.add(dateString + "");
//                            calendar.add(Calendar.DAY_OF_MONTH, -(1));  //设置为前一天
//
//                            axisValues.add(new AxisValue(i).setLabel(dateString + ""));
//
//                            Calendar calendar1 = Calendar.getInstance(); //得到日历
//
//                            calendar1.setTime(dNow);
//                            Log.e("historyusa??", "debug!! " + i);
//
//                            for (int j = 0; j < 7; j++) {
//
//
//                                if (odometerObjectsArray != null) {
//
//                                    if (i < odometerObjectsArray.size()) {
//
//                                        Log.e("historyusa@@", "debug " + ((float) odometerObjectsArray.get(i).getCreatedAt().getTime() - calendar1.getTime().getTime()) / 3600 / 1000 / 24);
//
//
//                                        if (odometerObjectsArray.get(i).getCreatedAt().getTime() - calendar1.getTime().getTime() <= 24 * 3600 * 1000
//                                                && odometerObjectsArray.get(i).getCreatedAt().getTime() - calendar1.getTime().getTime() > 0) {
//
////                                            values.set(j,new PointValue(j, (float) odometerObjectsArray.get(i).getNumber("Today")));
//
//
//                                            //values.add(new PointValue(j, (float) odometerObjectsArray.get(i).getNumber("Today")));
//                                            values.add(new PointValue(j, (float) odometerObjectsArray.get(i).getDouble("Today")));
//
//                                            Log.e("historyusa!!", "replace " + odometerObjectsArray.get(i).getDouble("Today"));
//
////                                            values.remove(j);
////                                            values.add(j, new PointValue(j, (float) odometerObjectsArray.get(i).getNumber("Today")));
//
//
//                                        } else {
//                                            values.add(new PointValue(j, (float) 0.0));
//                                            Log.e("historyusa!!", "adding ");
//                                        }
//
//                                    }
//
//                                    calendar1.add(Calendar.DAY_OF_MONTH, -(1));
//
//
//                                }
//                            }
//
//
//                        }

//        data.setAxisXBottom(new Axis(axisValues).setHasLines(true));
//        data.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

//                        List<AxisValue> xaxisValues = new ArrayList<AxisValue>();
//                        List<PointValue> yvalues = new ArrayList<PointValue>();
//
//                        Log.e("date debug", "a???");
//                        for (int i = 0; i < day_range; i++) {
//                            Log.e("date debug", "a" + date_string_array.get(day_range - 1 - i));
//
//                            xaxisValues.add(new AxisValue(i).setLabel(date_string_array.get(day_range - 1 - i)));
//
//                            Log.e("date debug", "b" + values.get(day_range - 1 - i));
//
//
//                            yvalues.add(new PointValue(i, values.get(day_range - 1 - i).getY()));
//
//
//                        }
//
//
//                        line = new Line(yvalues).setColor(R.color.lightgray).setCubic(true);
//                        line.setStrokeWidth(1);
//                        lines = new ArrayList<Line>();
//                        lines.add(line);
//                        data.setLines(lines);
//
//                        Axis axisX = new Axis(xaxisValues).setHasLines(true); //X轴
//
//                        axisX.setHasTiltedLabels(true);
//                        //axisX.setTextSize(9);
//                        //axisX.setMaxLabelChars(10);
//
//                        data.setAxisXBottom(axisX);
//
//
//                        data.setAxisYLeft(new Axis().setName("Range /Km").setHasLines(true).setMaxLabelChars(3));
//
//                        LineChartView chart = (LineChartView) view2.findViewById(R.id.chart);
////
////                        Log.e("history", "chart debug" + chart.toString());
////
//                        chart.setViewportCalculationEnabled(true);
////
//                        Viewport viewport = initViewPort();
//                        chart.setMaximumViewport(viewport);
//                        chart.setCurrentViewport(viewport);
//
//                        chart.setInteractive(false);
//
//                        //chart.setZoomType(ZoomType.HORIZONTAL);
//
//                        chart.setLineChartData(data);
//
//
//                    }
////                    else {
////                        total_text.setText("0.0");
////                        today_text.setText("0.0");
////                    }
//                    else {
//
//                        Log.e("noodometer", " debugging");
//
//                        total_text.setText("0.0");
//                        today_text.setText("0.0");
//
//                        values = new ArrayList<PointValue>();
//                        values.add(new PointValue(0, (float) 0.0));
//                        values.add(new PointValue(1, (float) 0.0));
//                        values.add(new PointValue(2, (float) 0.0));
//                        values.add(new PointValue(3, (float) 0.0));
//                        values.add(new PointValue(4, (float) 0.0));
//                        values.add(new PointValue(5, (float) 0.0));
//                        values.add(new PointValue(6, (float) 0.0));
//
//                        Line line = new Line(values).setColor(R.color.lightgray).setCubic(true);
//                        List<Line> lines = new ArrayList<Line>();
//                        lines.add(line);
//
//                        LineChartData data = new LineChartData();
//                        data.setLines(lines);
//
//                        Date dNow = new Date();
//
//                        Calendar calendar = Calendar.getInstance(); //得到日历
//
//                        calendar.setTime(dNow);//把当前时间赋给日历
//                        List<String> date_string_array = new ArrayList<String>();
//                        SimpleDateFormat formatter = new SimpleDateFormat("M.d");
//
//                        String dateString = formatter.format(dNow);
//
//                        List<AxisValue> axisValues = new ArrayList<AxisValue>();
//                        List<AxisValue> xaxisValues = new ArrayList<AxisValue>();
//                        List<PointValue> yvalues = new ArrayList<PointValue>();
//                        Date dBefore = new Date();
//                        for (int i = 0; i < 7; i++) {
//
//
//                            dBefore = calendar.getTime();
//                            dateString = formatter.format(dBefore);
//
//                            date_string_array.add(dateString + "");
//                            calendar.add(Calendar.DAY_OF_MONTH, -(1));  //设置为前一天
//
//                            axisValues.add(new AxisValue(i).setLabel(dateString + ""));
//
//                        }
//                        for (int i = 0; i < 7; i++) {
//                            Log.e("date debug", "a" + date_string_array.get(7 - 1 - i));
//
//                            xaxisValues.add(new AxisValue(i).setLabel(date_string_array.get(7 - 1 - i)));
//
//                            Log.e("date debug", "b" + values.get(7 - 1 - i));
//
//
//                            yvalues.add(new PointValue(i, values.get(7 - 1 - i).getY()));
//
//
//                        }
//                        line = new Line(yvalues).setColor(R.color.lightgray).setCubic(true);
//                        line.setStrokeWidth(1);
//                        lines = new ArrayList<Line>();
//                        lines.add(line);
//                        data.setLines(lines);
//
//                        Axis axisX = new Axis(xaxisValues).setHasLines(true); //X轴
//
//                        axisX.setHasTiltedLabels(true);
//                        //axisX.setTextSize(9);
//                        //axisX.setMaxLabelChars(10);
//
//                        data.setAxisXBottom(axisX);
//
//
//                        data.setAxisYLeft(new Axis().setName("Range /Km").setHasLines(true).setMaxLabelChars(3));
//
//                        LineChartView chart = (LineChartView) view2.findViewById(R.id.chart);
//
//                        Log.e("history", "chart debug");
//
//                        chart.setViewportCalculationEnabled(true);
////
////                    Viewport viewport = initViewPort();
////                    chart.setMaximumViewport(viewport);
////                    chart.setCurrentViewport(viewport);
//
//                        chart.setInteractive(false);
//
//                        //chart.setZoomType(ZoomType.HORIZONTAL);
//
//                        chart.setLineChartData(data);
//
//
//                    }
//                }
//
//
//            }
//
//        });


//        for (int i = 0; i < day_range; ++i) {
//
//            calendar.add(Calendar.DAY_OF_MONTH, -(1));  //设置为前一天
//            dBefore = calendar.getTime();
//            dateString = formatter.format(dBefore);
//
//            date_string_array.add(dateString + "");
//            axisValues.add(new AxisValue(i).setLabel(dateString + ""));
//
//            Calendar calendar1 = Calendar.getInstance(); //得到日历
//
//
//        }
////        data.setAxisXBottom(new Axis(axisValues).setHasLines(true));
////        data.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));
//
//        List<AxisValue> xaxisValues = new ArrayList<AxisValue>();
//        List<PointValue> yvalues = new ArrayList<PointValue>();
//
//        //Log.e("date debug","a???");
//        for(int i=0;i<day_range;i++){
//            //Log.e("date debug","a"+date_string_array.get(day_range-1-i));
//            xaxisValues.add(new AxisValue(i).setLabel(date_string_array.get(day_range-1 - i)));
//
//            //Log.e("date debug", "b" + values.get(day_range-1 - i));
//            yvalues.add(new PointValue(i, values.get(day_range-1 - i).getY()));
//
//
//        }
//
//
//        line = new Line(yvalues).setColor(R.color.lightgray).setCubic(true);
//        line.setStrokeWidth(1);
//        lines = new ArrayList<Line>();
//        lines.add(line);
//        data.setLines(lines);
//
//        Axis axisX = new Axis(xaxisValues).setHasLines(true); //X轴
//
//        axisX.setHasTiltedLabels(true);
//        //axisX.setTextSize(9);
//        //axisX.setMaxLabelChars(10);
//
//        data.setAxisXBottom(axisX);
//
//
//        data.setAxisYLeft(new Axis().setName("Range /Km").setHasLines(true).setMaxLabelChars(3));
//
//        LineChartView chart = (LineChartView) view2.findViewById(R.id.chart);
//
//        chart.setViewportCalculationEnabled(true);
//
//        Viewport viewport = initViewPort();
//        chart.setMaximumViewport(viewport);
//        chart.setCurrentViewport(viewport);
//
//        chart.setInteractive(false);
//
//        //chart.setZoomType(ZoomType.HORIZONTAL);
//
//        chart.setLineChartData(data);


        order_button = (ImageButton) view2.findViewById(R.id.order_button);
        discount_button = (ImageButton) view2.findViewById(R.id.discount_button);

        order_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(getActivity(), OrderActivity.class);
                startActivity(mainIntent);

            }
        });

        discount_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity(), DiscountActivity.class);
                startActivity(mainIntent);
            }
        });


    }

    private Viewport initViewPort() {
        Viewport viewport = new Viewport();
        viewport.top = 100;
        viewport.bottom = 0;
        viewport.left = 100;
        viewport.right = 100;

        return viewport;
    }

    private void InitTextView() {
        textView1 = (TextView) rootView.findViewById(R.id.mark_tab);
        textView2 = (TextView) rootView.findViewById(R.id.post_tab);


        textView1.setOnClickListener(new MyOnClickListener(0));
        textView2.setOnClickListener(new MyOnClickListener(1));

        //textView2.performClick();
    }

    private void InitImageView() {
        imageView = (ImageView) rootView.findViewById(R.id.viewpager_cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.notloading).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        imageView.setImageMatrix(matrix);// 设置动画初始位置
    }

    @Override
    public void setPostSuccess(MyPostList postList, boolean likePost) {
        if (getActivity() == null) return;
        if (!likePost) {
            myPostList = postList.getData();
            addMarkersToMap();

        } else {
            likePostList = postList.getData();
        }
        if (islike_post) {
            discoverpostadapter = new ImageAdapter(getActivity(), likePostList);
        } else {
            discoverpostadapter = new ImageAdapter(getActivity(), myPostList);
        }

        ((GridView) listView).setAdapter(discoverpostadapter);
        discoverpostadapter.notifyDataSetChanged();

    }

    @Override
    public void setUserBeanSuccess(UserBean userBean1, boolean likePost) {

        UserInfos.setUserBean(getActivity(), userBean1);
        if (userBean1 != null && userBean1.getUserinfo() != null) {
            profile_bio.setText(userBean1.getUserinfo().getInfo());
            generateDefaultData();
            if (userBean == null) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                }, 200L);
            }

        }
        userBean = userBean1;
        if (today_text != null)
            today_text.setText(userBean.getUserinfo().getToday_usage() + "");
        if (total_text != null)
            total_text.setText(userBean.getUserinfo().getAll_usage() + "");

    }

    @Override
    public void setPostFail(String errInfo) {

    }

    private class MyOnClickListener implements OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            Log.e("abc", "text onclick");
            viewPager.setCurrentItem(index);

            if (index == 0) {
                imageView.setBackground(getResources().getDrawable(R.drawable.post_cursor));
                post_text.setTextColor(Color.parseColor("#A2A2A2"));
                mark_text.setTextColor(Color.parseColor("#00EAAB"));


            } else {
                imageView.setBackground(getResources().getDrawable(R.drawable.mark_cursor));

                post_text.setTextColor(Color.parseColor("#00EAAB"));//蓝
                mark_text.setTextColor(Color.parseColor("#A2A2A2"));

            }
        }

    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews == null ? 0 : mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = 0;// 页卡1 -> 页卡2 偏移量   offset  + bmpW

        //one = 0;
        //int two = one * 2;// 页卡1 -> 页卡3 偏移量
        public void onPageScrollStateChanged(int arg0) {


        }


        public void onPageScrolled(int arg0, float arg1, int arg2) {


        }

        public void onPageSelected(int arg0) {
            /*两种方法，这个是一种，下面还有一种，显然这个比较麻烦
            Animation animation = null;
            switch (arg0) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, 0, 0, 0);
                }
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, one, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, one, 0, 0);
                }
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, two, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(one, two, 0, 0);
                }
                break;

            }
            */
            Animation animation = new TranslateAnimation(one * currIndex, one * arg0, 0, 0);//显然这个比较简洁，只有一行代码。
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            imageView.startAnimation(animation);

            if (currIndex == 0) {
                imageView.setBackground(getResources().getDrawable(R.drawable.post_cursor));

                post_text.setTextColor(Color.parseColor("#A2A2A2"));
                mark_text.setTextColor(Color.parseColor("#00EAAB"));

            } else {
                imageView.setBackground(getResources().getDrawable(R.drawable.mark_cursor));
                post_text.setTextColor(Color.parseColor("#00EAAB"));
                mark_text.setTextColor(Color.parseColor("#A2A2A2"));
            }

            //viewPager.setCurrentItem(arg0);
            //Toast.makeText(getActivity(), "您选择了" + viewPager.getCurrentItem() + "页卡", Toast.LENGTH_SHORT).show();
        }

    }


    // Image Adapter

    private class ImageAdapter extends BaseAdapter {

        //        private String[] IMAGE_URLS = postUrl;
        List<MyPostList.MyPostBean> postList;

        private LayoutInflater inflater;

        private DisplayImageOptions options;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        ImageAdapter(Context context, List<MyPostList.MyPostBean> myPostList) {
            this.postList = myPostList;

            inflater = LayoutInflater.from(context);

            options = new DisplayImageOptions.Builder()
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
            return postList == null ? 0 : postList.size();
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

            //Log.e("abclike","  "+IMAGE_URLS.length);

            if (view == null && inflater != null) {
                view = inflater.inflate(R.layout.me_gridview_item, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.me_historyimage);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.imageView.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(getActivity(), DetailActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY__TO_FRONT);


                            //Log.e("abc", "testingt " + position + " objectID " + PostImageCount.get(position));

//                            if (!islike_post)
                            intent.putExtra("ObjectId", postList.get(position).getPost_id());
//                            else
//                                intent.putExtra("ObjectId", likePostImageCount.get(position).getAVObject("photo").getObjectId());

                            startActivity(intent);

                        }
                    }
            );
            if (postList.get(position).getType() == 1) {
                ImageLoader.getInstance().displayImage(postList.get(position).getRurl(), holder.imageView, options, animateFirstListener);

            } else {
                ImageLoader.getInstance().displayImage(postList.get(position).getCover_icon(), holder.imageView, options, animateFirstListener);

            }

            return view;
        }
    }

    static class ViewHolder {
        ImageView imageView;
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

    private void enableGps() {
        // Check if user has granted location permission
        if (!locationServices.areLocationPermissionsGranted()) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            enableLocation();
        }
    }


    private void enableLocation() {
        // If we have the last location of the user, we can move the camera to that position.
//        Location lastLocation = locationServices.getLastLocation();
//        if (lastLocation != null) {
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 12));
//        }
//
//        locationServices.addLocationListener(new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                if (location != null) {
//                    // Move the map camera to where the user location is and then remove the
//                    // listener so the camera isn't constantly updating when the user location
//                    // changes. When the user disables and then enables the location again, this
//                    // listener is registered again and will adjust the camera once again.
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location), 12));
//                    locationServices.removeLocationListener(this);
//                }
//            }
//        });
//        // Enable or disable the location layer on the map
//        Log.e("map debug", "setMyLocationEnabled");
//        map.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {

        super.onResume();
        userBean = UserInfos.getUserBean();

        queryParseMethod();
        Log.e("abc", "home resume");
        mapView.onResume();
        map.onResume();
    }
}