//package maojian.android.walnut;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.provider.MediaStore;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import com.avos.avoscloud.AVObject;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
//import maojian.android.walnut.ImagePicker.imageselector_view.ClipImageLayout;
//import maojian.android.walnut.home.ImageAdapter;
//import maojian.android.walnut.utils.eventbus.PostSuccessEvent;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.io.*;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//public class VideoPickerActivity extends AppCompatActivity {
//    private ArrayList<Floder> mDirPaths = new ArrayList<Floder>();
//
//    private Floder imageAll, currentImageFolder;
//
//    private GridView listView;
//
//    private List<AVObject> PostImageCount;
//
//    private final String[] postUrl = new String[500];
//
//    private ImageAdapter discoverpostadapter;
//
//    private ImageView imagepicker_imageview;
//
//    private ImageButton imagepicker_next;
//    private ImageButton imagepicker_return;
//
//    private ClipImageLayout mClipImageLayout;
//    private View contview;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_video_picker);
//        initViews();
//        EventBus.getDefault().register(this);
//        listView = (GridView) findViewById(R.id.imagepicker_gridView);
//        discoverpostadapter  = new ImageAdapter(ImagePickerActivity.this);
//        ((GridView) listView).setAdapter(discoverpostadapter);
//        setTitle("library");
////        imagepicker_return= (ImageButton) findViewById(R.id.imagepicker_returnbutton);
////        imagepicker_return.setOnClickListener(
////                new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        ImagePickerActivity.this.finish();
////                    }
////                }
////        );
//
//        imagepicker_next = (ImageButton) findViewById(R.id.imagepicker_nextbutton);
//        imagepicker_next.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //mClipImageLayout.clip();
//                        Log.e("abc","click debug");
//
//                        imagepicker_next.setEnabled(false);
//
//                        File pictureFile = getOutputMediaFile();
//                        if (pictureFile == null){
//                            Log.d("abc", "Error creating media file, check storage permissions: ");
//                            return;
//                        }
//
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        mClipImageLayout.clip().compress(Bitmap.CompressFormat.PNG, 50, baos);
//
//                        try {
//                            FileOutputStream fos = new FileOutputStream(pictureFile);
//                            fos.write(baos.toByteArray());
//                            fos.close();
//                        } catch (FileNotFoundException e) {
//                            Log.d("abc", "File not found: " + e.getMessage());
//                        } catch (IOException e) {
//                            Log.d("abc", "Error accessing file: " + e.getMessage());
//                        }
//
//                        Intent intent = new Intent(ImagePickerActivity.this, UploadActivity.class);
//                        intent.putExtra("filepath", pictureFile.toString());
//                        startActivity(intent);
////                        finish();
//                    }
//                }
//        );
//
//
//    }
//
//    @Override
//    public void onClickEvent(View v) {
//
//    }
//
//    private File getOutputMediaFile(){
//        //get the mobile Pictures directory
////        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//
//        //get the current time
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//
//        return new File(BaseConstant.getAppPicFile() + "/IMAGE_"+ timeStamp + ".jpg");
//    }
//
//    private void initViews() {
//
//        imageAll = new Floder();
//        imageAll.setDir("/所有图片");
//        currentImageFolder = imageAll;
//        mDirPaths.add(imageAll);
//
//        getThumbnail();
//    }
//
//    /**
//     * 得到缩略图
//     */
//    private void getThumbnail() {
//        /**
//         * 临时的辅助类，用于防止同一个文件夹的多次扫描
//         */
//        HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();
//
//        Cursor mCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                new String[]{MediaStore.Images.ImageColumns.DATA}, "", null,
//                MediaStore.MediaColumns.DATE_ADDED + " DESC");
//        Log.e("TAG0", mCursor.getCount() + "");
//        if (mCursor.moveToFirst()) {
//            do {
//                // 获取图片的路径
//
//                String path = mCursor.getString(0);
//
//                if (path != null) {
//                    Log.e("TAG1", path);
//
//                    path = "file://"+path;
//
//                    imageAll.images.add(new ImageItem(path));
//                    // 获取该图片的父路径名
//                    File parentFile = new File(path).getParentFile();
//                    if (parentFile == null) {
//                        continue;
//                    }
//                    Floder imageFloder = null;
//                    String dirPath = parentFile.getAbsolutePath();
//                    if (!tmpDir.containsKey(dirPath)) {
//                        // 初始化imageFloder
//                        imageFloder = new Floder();
//                        imageFloder.setDir(dirPath);
//                        imageFloder.setFirstImagePath(path);
//                        mDirPaths.add(imageFloder);
//                        Log.d("zyh", dirPath + "," + path);
//                        tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
//                    } else {
//                        imageFloder = mDirPaths.get(tmpDir.get(dirPath));
//                    }
//                    imageFloder.images.add(new ImageItem(path));
//
//                }
//            }
//            while (mCursor.moveToNext());
//        }
//
//
//        mCursor.close();
//
//        for (int i = 0; i < mDirPaths.size(); i++) {
//            Floder f = mDirPaths.get(i);
//            Log.d("zyh", i + "-----" + f.getName() + "---" + f.images.size());
//        }
//        tmpDir = null;
//    }
//
//    class Floder {
//
//
//        /**
//         * 图片的文件夹路径
//         */
//        private String dir;
//        /**
//         * 第一张图片的路径
//         */
//        private String firstImagePath;
//
//        /**
//         * 文件夹的名称
//         */
//        private String name;
//        public List<ImageItem> images = new ArrayList<ImageItem>();
//
//        public String getDir() {
//            return dir;
//        }
//
//        public void setDir(String dir) {
//            this.dir = dir;
//            int lastIndexOf = this.dir.lastIndexOf("/");
//            this.name = this.dir.substring(lastIndexOf);
//        }
//
//        public String getFirstImagePath() {
//            return firstImagePath;
//        }
//
//        public void setFirstImagePath(String firstImagePath) {
//            this.firstImagePath = firstImagePath;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//    }
//
//    class ImageItem {
//
//        String path;
//
//        public ImageItem(String p) {
//            this.path = p;
//        }
//
//    }
//
////    public void chip(View view) {
////        Bitmap bitmap = mClipImageLayout.clip();
////
////        ByteArrayOutputStream baos = new ByteArrayOutputStream();
////        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
////        byte[] datas = baos.toByteArray();
////
////        imageView.setImageBitmap(BitmapFactory.decodeByteArray(datas, 0, datas.length));
////        dialog.show();
////    }
//
//    private class ImageAdapter extends BaseAdapter {
//
//        private  String[] IMAGE_URLS = postUrl;
//
//
//
//        private LayoutInflater inflater;
//
//        private DisplayImageOptions options;
//        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
//
//        ImageAdapter(Context context) {
//            inflater = LayoutInflater.from(context);
//
//            options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.ic_stub)
//                    .showImageForEmptyUri(R.drawable.ic_empty)
//                    .showImageOnFail(R.drawable.ic_error)
//                    .cacheInMemory(true)
//                    .cacheOnDisk(true)
//                    .considerExifParams(true)
//                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .build();
//
////            imagepicker_imageview = (ImageView) findViewById(R.id.imagepicker_imageview);
////            ImageLoader.getInstance().displayImage(imageAll.images.get(0).path, imagepicker_imageview, options, animateFirstListener);
//
//            String path[] = imageAll.images.get(0).path.split("://");
//
//            Log.e("abc","debugdebugimage "+path[1]);
//
//
//            mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
//            contview = findViewById(R.id.contive);
////            mClipImageLayout.getZoomImageView()
////                    .setImageBitmap(BitmapFactory.decodeFile(path[1]));
//            ImageLoader.getInstance().displayImage(imageAll.images.get(0).path, mClipImageLayout.getZoomImageView(), options, animateFirstListener);
//        }
//
//        @Override
//        public int getCount() {
//            return imageAll.images.size();
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
//            final ViewHolder holder;
//            View view = convertView;
//            if (view == null&& inflater != null) {
//                view = inflater.inflate(R.layout.me_gridview_item, parent, false);
//                holder = new ViewHolder();
//                assert view != null;
//                holder.imageView = (ImageView) view.findViewById(R.id.me_historyimage);
//                view.setTag(holder);
//            } else {
//                holder = (ViewHolder) view.getTag();
//            }
//
//
//
//            holder.imageView.setOnClickListener(
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            if(null == imageAll.images.get(position).path)return;
//
//                            //ImageLoader.getInstance().displayImage(imageAll.images.get(position).path, imagepicker_imageview, options, animateFirstListener);
//
//                            String path[] = imageAll.images.get(position).path.split("://");
////                            mClipImageLayout.getZoomImageView()
////                                    .setImageBitmap(BitmapFactory.decodeFile(path[1]));
//                            ImageLoader.getInstance().displayImage(imageAll.images.get(position).path, mClipImageLayout.getZoomImageView(), options, animateFirstListener);
//
//
////                            Intent intent = new Intent();
////
////                            Uri mUri =  Uri.parse(imageAll.images.get(position).path);
////
////                            intent.setAction("com.android.camera.action.CROP");
////                            intent.setDataAndType(mUri, "image/*");// mUri是已经选择的图片Uri
////                            intent.putExtra("crop", "true");
////                            intent.putExtra("aspectX", 1);// 裁剪框比例
////                            intent.putExtra("aspectY", 1);
////                            intent.putExtra("outputX", 150);// 输出图片大小
////                            intent.putExtra("outputY", 150);
////                            intent.putExtra("return-data", true);
////
////                            ImagePickerActivity.this.startActivityForResult(intent, 200);
//
//
//                        }
//                    }
//            );
//
//            ImageLoader.getInstance().displayImage(imageAll.images.get(position).path, holder.imageView, options, animateFirstListener);
//
//            return view;
//        }
//    }
//
//    static class ViewHolder {
//        ImageView imageView;
//    }
//
//
//    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
//
//        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
//
//        @Override
//        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//            if (loadedImage != null) {
//                ImageView imageView = (ImageView) view;
//                boolean firstDisplay = !displayedImages.contains(imageUri);
//                if (firstDisplay) {
//                    FadeInBitmapDisplayer.animate(imageView, 500);
//                    displayedImages.add(imageUri);
//                }
//            }
//        }
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventBus(PostSuccessEvent mFinishActivityEvent) {
//        finish();
//    }
//
//}
//
//
//    public void getVideoPath() {
//        Uri originalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        //若为视频则为MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        ContentResolver cr = this.getContentResolver();
//        Cursor cursor = cr.query(originalUri, null, null, null, null);
//        if (cursor == null) {
//            return;
//        }
//        for (cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()) {
//            long thumbNailsId = cursor.getLong(cursor.getColumnIndex("_ID"));
//            //filePath为该图片文件的全路径
//            String filePath = cursor.getString(cursor.getColumnIndex("_data"));
//            //folderPath为该图片所在文件夹的文件夹全路径
//            String folderPath= filePath.substring(0, filePath.lastIndexOf("/"));
//            //拿到该文件夹下的所有图片文件, DESC表示降序
//            Cursor imageCursor = cr.query(originalUri, null, "_data like '%"
//                            + folderPath.replaceAll("'", "''") + "%'", null,
//                    "_id DESC");
//            //通过对imageCursor使用下面方法遍历，获取每张图片的路径
//            //String imageFullPath = imageCursor
//            //          .getString(imageCursor.getColumnIndex("_data"));
//            Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr,thumbNailsId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
//            //若为视频则为
////                  Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr,
////                          thumbNailsId, Video.Thumbnails.MICRO_KIND, null);
////            return bitmap;
//        }
//
//    }
//}
