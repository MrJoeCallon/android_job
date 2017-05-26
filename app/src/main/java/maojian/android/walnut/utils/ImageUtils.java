package maojian.android.walnut.utils;

import android.graphics.*;
import android.util.Log;

import java.io.*;

/**
 * 图像处理工具类：
 * 计算、转换、截取、旋转、压缩等
 * <p/>
 * 长宽高按热带鱼本身的图像处理机制进行处理，保障上传过程保持图片的长宽比例，以及超长图的优化
 * by lsm 2014-10-10
 */
public class ImageUtils {
    final static String TAG = "ImgUtils";
    //    private static final String IMAGE_PATH = Environment.getExternalStorageDirectory().toString() //
    //            + File.separator + "tropic"//
    //            + File.separator + "Images" //
    //            + File.separator;
    private static final int UPLOAD_MAX_SIZE = 300;//上传图片的最大KB数，大于则压缩

    public static Bitmap revitionImageSize(String path) throws IOException {
        return revitionImageSize(path, 1000);
    }

    public static Bitmap revitionImageSize(String path, final int maxSize) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= maxSize) && (options.outHeight >> i <= maxSize)) {
                in = new BufferedInputStream(new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    //XXX:图片处理辅助参数

    /**
     * 计算图片最小适应的宽高值
     *
     * @param w     原图宽
     * @param h     原图高
     * @param minWH 超宽图/长竖图/竖图的最小宽高
     * @param maxW  图片的最大宽度
     * @return
     */
    public static int[] calMinWh(int w, int h, int minWH, int maxW) {
        int outW = w, outH = h;
        float whRare = 1f * w / h;
        if (whRare > (16f / 9)) {
            //超宽图，按高来压缩
            outH = Math.min(minWH, h);
            outW = (int) (outH * whRare);
        } else if (whRare <= (9f / 16)) {
            //长竖图，限制宽度，高度无限
            outW = Math.min(minWH, w);
            outH = (int) (outW / whRare);
        } else if (whRare <= (3f / 4)) {
            //普通竖图，限制宽度，高度无限
            outW = Math.min(minWH, w);
            outH = (int) (outW / whRare);
        } else {
            //其他情况：普通宽图，小型竖图，正图
            //限制最大宽度，其他无限制
            outW = Math.min(maxW, w);
            outH = (int) (outW / whRare);
        }
        return new int[]{outW, outH};
    }

    /**
     * 计算图片最靠近期望宽高的压缩比例
     * <pre>
     *  inSampleSize表示缩略图大小为原始图片大小的几分之一，
     *  即如果这个值为2，则取出的缩略图的宽和高都是原始图片的1/2
     *  图片大小就为原始大小的1/4。
     * </pre>
     *
     * @param options   原图片的bitmap属性值
     * @param reqWidth  期望宽度
     * @param reqHeight 期望高度
     * @return 绽放比例值
     */
    public static int calInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize*2;
    }

    /**
     * 缩放图片，保持原图比例
     *
     * @param bm      源图
     * @param minWh   宽或高的最小值
     * @param maxW    最大的宽度
     * @param rotate  旋转角度
     * @param qualify 图片质量
     * @return 图片字节流
     */
    private static ByteArrayOutputStream thumbToStream(Bitmap bm, int minWh, int maxW, int rotate, int qualify) {
        ByteArrayOutputStream baos = null;
        if (bm != null) {
            //先进行旋转
            if (rotate != 0) {
                bm = ImageUtils.getRotateBitmap(bm, rotate);
            }

            //再进行图片绽放处理
            int w = bm.getWidth();
            int h = bm.getHeight();
            int[] outWH = ImageUtils.calMinWh(w, h, minWh, maxW);
            int outW = outWH[0];
            if (outW < w) {
                float scale = 1f * outW / w;
                bm = ImageUtils.getScaleBitmap(bm, scale);
            }
            baos = new ByteArrayOutputStream();
            if (qualify <= 0) {
                qualify = 100;
            }
            bm.compress(Bitmap.CompressFormat.JPEG, qualify, baos);
        }
        return baos;
    }

    /**
     * 缩放bitmap，使其适应当前的输出屏幕
     *
     * @param bm      位图
     * @param screenW 宽
     * @param screenH 高
     * @param rotate  旋转角度
     * @param qualify 质量
     * @return 图片字节流
     */
    private static ByteArrayOutputStream thumbToScreen(Bitmap bm, int screenW, int screenH, int rotate, int qualify) {
        ByteArrayOutputStream baos = null;
        if (bm != null) {

            //先做旋转
            if (rotate != 0) {
                bm = ImageUtils.getRotateBitmap(bm, rotate);
            }

            int w = bm.getWidth();
            int h = bm.getHeight();

            float scaleW = 1f * screenW / w;
            float scaleH = 1f * screenH / h;
            int startX, startY;

            //以缩放较少的边作为基线，另一个边进行裁剪
            float scale;//= Math.max(scaleH, scaleW);
            int targetW, targetH;
            if (scaleW >= scaleH) {
                //固定宽，裁剪原图的高
                scale = scaleW;
                //保持原宽
                startX = 0;
                targetW = w;
                //裁剪高
                targetH = (int) (screenH / scale);//放大screen的高
                startY = (h - targetH) / 2;//y~targetH是要保留的部分
            } else {
                //固定高，裁剪原图的宽
                scale = scaleH;
                //保持原高
                targetH = h;
                startY = 0;
                //裁剪宽
                targetW = (int) (screenW / scale);
                startX = (w - (int) (screenH / scale)) / 2;//舍弃小数点
            }

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);//缩放比例
            Bitmap scaleBmd = Bitmap.createBitmap(bm, startX, startY, targetW, targetH, matrix, false);

            int newW = scaleBmd.getWidth();
            int newH = scaleBmd.getHeight();
            baos = new ByteArrayOutputStream();
            if (qualify <= 0 || qualify>100) {
                qualify = 100;
            }
            scaleBmd.compress(Bitmap.CompressFormat.JPEG, qualify, baos);
        }
        return baos;
    }
    //XXX:图片处理

    /**
     * 获取压缩图片，用于下一步处理的
     *
     * @param _path   图像路径
     * @param screenW 最小的宽高 如1280的正方形范围的图片
     * @param screenH 长图下最大的宽度，一般长图取960宽就可以了
     * @param rotate  旋转
     * @param qualify 质量
     * @return Bitmap
     */
    public static Bitmap getScreenThumb(String _path, int screenW, int screenH, int rotate, int qualify) {
        Bitmap bm = null;
        if (_path != null) {
            bm = ImageUtils.getLargeBmd(_path, screenW, screenH);
        }
        ByteArrayOutputStream baos = thumbToScreen(bm, screenW, screenH, rotate, qualify);
        if (baos != null) {
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            bm = BitmapFactory.decodeStream(isBm, null, null);
        }
        return bm;
    }

    //    public static Bitmap getScreenThumb(String _path, int minWh, int maxW, boolean autoRotate, int qualify) {
    //        int rotate = autoRotate ? ExifUtils.getExifRotate(_path) : 0;
    //        return getScreenThumb(_path, minWh, maxW, rotate, qualify);
    //    }

    /**
     * 获取压缩图片，用于下一步处理的
     *
     * @param _path   图像路径
     * @param minWh   最小的宽高 如1280的正方形范围的图片
     * @param maxW    长图下最大的宽度，一般长图取960宽就可以了
     * @param rotate  旋转
     * @param qualify 质量
     * @return Bitmap
     */
    public static Bitmap getThumbBitmap(String _path, int minWh, int maxW, int rotate, int qualify) {
        Bitmap bm = null;
        long startT = System.currentTimeMillis();
        if (_path != null) {
            bm = ImageUtils.getLargeBmd(_path, minWh, maxW);
        }
        //Logger.d(TAG, "----loadImageView: getLargeBmd,time" + (System.currentTimeMillis() - startT));
        startT = System.currentTimeMillis();
        if (rotate != 0) {
            bm = ImageUtils.getRotateBitmap(bm, rotate);
           // Logger.d(TAG, "----loadImageView: getThumbBitmap,rotate time" + (System.currentTimeMillis() - startT));
        }
        //        ByteArrayOutputStream baos = thumbToStream(bm, minWh, maxW, rotate, qualify);
        //        if (baos != null) {
        //            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //            bm = BitmapFactory.decodeStream(isBm, null, null);
        //        }

        return bm;

    }

    //    public static Bitmap getThumbBitmap(String _path, int minWh, int maxW, boolean autoRotate, int qualify) {
    //        int rotate = autoRotate ? ExifUtils.getExifRotate(_path) : 0;
    //        return getThumbBitmap(_path, minWh, maxW, rotate, qualify);
    //    }

    /**
     * 获取压缩图片，用于数据传输的
     *
     * @param _path   图像路径
     * @param minWh   最小的宽高 如1280的正方形范围的图片
     * @param maxW    长图下最大的宽度，一般长图取960宽就可以了
     * @param rotate  旋转
     * @param qualify 质量
     * @return 字节流
     */
    public static Thumb getThumbBytes(String _path, int minWh, int maxW, int rotate, int qualify) {
        Bitmap bm = null;
        int w = 0, h = 0;
        byte[] bytes = null;

        if (_path != null) {
            bm = ImageUtils.getLargeBmd(_path, minWh, minWh);
            if(bm == null){
                return null;
            }
            w = bm.getWidth();
            h = bm.getHeight();
        }
        ByteArrayOutputStream baos = thumbToStream(bm, minWh, maxW, rotate, qualify);
        if (baos != null) {
            bytes = baos.toByteArray();
            int byLen = bytes.length;
            ByteArrayInputStream isBm = new ByteArrayInputStream(bytes);
            bm = BitmapFactory.decodeStream(isBm, null, null);
            //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            MaxSize maxSize = MaxSize.get(w, h);
            int decQa = qualify;
            while ((byLen / 1024) > maxSize.getSize() && decQa > 20) {//fixme：压缩到多少以下不再压缩
                baos.reset();//清空baos
                decQa -= 3;//每次都减少5
                if(decQa<0){
                    decQa = 0;
                }
                //这里压缩options%，把压缩后的数据存放到baos中
                bm.compress(Bitmap.CompressFormat.JPEG, decQa, baos);
                bytes = baos.toByteArray();
                byLen = bytes.length;
            }
            //Logger.e(TAG,"decQa ==" + decQa);
            return new Thumb(w, h, bytes);
        }
        return null;
    }
//
//    public static Thumb getThumbBytes(String _path, int minWh, int maxW, boolean autoRotate, int qualify) {
//        int rotate = autoRotate ? ExifUtils.getExifRotate(_path) : 0;
//        return getThumbBytes(_path, minWh, maxW, rotate, qualify);
//    }

    /**
     * 按比例缩放图片
     *
     * @param bmd   位图
     * @param scale 缩放比例
     * @return
     */
    public static Bitmap getScaleBitmap(Bitmap bmd, float scale) {
        int width = bmd.getWidth();
        int height = bmd.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap scaleBmd = Bitmap.createBitmap(bmd, 0, 0, width, height, matrix, true);
        return scaleBmd;
    }

    /**
     * 旋转图片
     *
     * @param bmd    位图
     * @param rotate 旋转角度
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap bmd, int rotate) {
        Matrix matrix = new Matrix();
        matrix.setRotate(rotate, bmd.getWidth() / 2, bmd.getHeight() / 2);
        Bitmap rotateBmd = Bitmap.createBitmap(bmd, 0, 0, bmd.getWidth(), bmd.getHeight(), matrix, true);
        return rotateBmd;
    }

    /**
     * 根据期望尺寸获取得图片
     * <p/>
     * 主要是可以高压缩图片
     *
     * @param _path 文件
     * @param reqW  期望宽度, 为0则输出全图
     * @param reqH  期望高度，为0则输出全图
     * @return
     */
    public static Bitmap getLargeBmd(String _path, int reqW, int reqH) {
        //读图片的属性，不加载图片: inJustDecodeBounds=true
//        Logger.d(TAG,"getLargeBmd path=" + _path + ",reqW=" + reqW + ",reqH=" + reqH);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高  此时返回bm为空
        BitmapFactory.decodeFile(_path, options);

        //计算图片输出的长和宽
        if (reqW > 0 && reqH > 0) {
            options.inSampleSize = calInSampleSize(options, reqW, reqH);
        }

        // inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //return BitmapFactory.decodeStream(new FileInputStream(new File(_path)), null, options);
        return BitmapFactory.decodeFile(_path, options);
    }

    //XXX:图片转换

    /**
     * bmd to byte
     * //分享到微信使用 压缩到32k以下
     *
     * @param bm
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while (baos.toByteArray().length / 1024 >= 32 && options > 0) {    //循环判断如果压缩后图片是否大于maxKb,大于继续压缩
            //重置baos即清空baos
            baos.reset();
            //这里压缩options%，把压缩后的数据存放到baos中
            bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 5;//每次都减少5
        }
        return baos.toByteArray();
    }

    /**
     * byte to bmd
     *
     * @param b
     * @return
     */
    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }





    /**
     * 判断路径是否包含http://
     *
     * @param path
     * @return
     */
    public static boolean isNetImage(String path) {
        return path != null && path.contains("http://");
    }

    /**
     * 去掉路径开头的！
     */
    public static String getLocalImagePath(String path) {
        if (path != null) {
            return path.replaceAll("^!", "");
        }
        return path;
    }

    //XXX:

    //缩略图类型
    public static class Thumb {
        public int width;
        public int height;
        public byte[] bytes;
        public long length;

        public Thumb(int width, int height, byte[] bytes) {
            this.width = width;
            this.height = height;
            this.bytes = bytes;
            this.length = bytes.length;
        }
    }

    //最大尺寸
    public enum MaxSize {
        wLarge(true, 1280, 150),//宽图:超大图 180与200很接近, 比150清晰
        wBig(true, 1024, 120),//宽图:大图
        wMiddle(true, 720, 100),//宽图:中图
        hLong(false, 960, 150),//竖图:长图
        hMid(false, 720, 120),//竖图:长图
        hSmall(false, 600, 100),//竖图:中长图
        normal(true, 0, 100);//其他类型的图片

        private final int width, size;
        private final boolean isWide;//是否宽图

        MaxSize(boolean isWide, int width, int maxSize) {
            this.isWide = isWide;
            this.width = width;
            this.size = maxSize;
        }

        public static MaxSize get(boolean isWide, int w) {
            if (isWide) {
                if (w >= hLong.width) {
                    return hLong;
                } else if (w > hMid.width) {
                    return hLong;
                } else if (w >= hSmall.width) {
                    return hSmall;
                }
            } else {
                if (w >= wLarge.width) {
                    return wLarge;
                } else if (w >= wBig.width) {
                    return wBig;
                } else if (w >= wMiddle.width) {
                    return wMiddle;
                }
            }
            return normal;
        }

        public static MaxSize get(int w, int h) {
            //高大于宽, 并且差距大小1/4高, 则为竖图处理
            return get((h - w) > (h / 4), w);
        }

        public int getSize() {
            return size;
        }
    }

    /**
     * 两张图片叠加，聊天图片截取
     * @param bgd 背景图
     * @param fg  图片
     * @param width
     * @param height
     * @return
     */
    public static Bitmap combineImages(Bitmap bgd, Bitmap fg, int width, int height) {
        if(bgd == null || fg == null){
            return null;
        }
        Bitmap bmp;
//        int width = bgd.getWidth() > fg.getWidth() ?
//                bgd.getWidth() : fg.getWidth();
//        int height = bgd.getHeight() > fg.getHeight() ?
//                bgd.getHeight() : fg.getHeight();
//        Matrix matrix = new Matrix();
//        matrix.postScale(bgd.getWidth()/width, height/bgd.getHeight()); // 长和宽放大缩小的比例
//        bgd = Bitmap.createBitmap(bgd,0,0,width,height,matrix,false);
        Log.i(TAG, "combineImages width=" + bgd.getWidth() + ",height=" + bgd.getHeight());
        Log.i(TAG, "combineImages fg.getWidth()=" + fg.getWidth() + ",fgheight=" + fg.getHeight());
        Bitmap srcBmp = zoomImage(fg,width,height);
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(bgd, 0, 0, null);
        canvas.drawBitmap(srcBmp, 0, 0, paint);

        return bmp;
    }


    /***
     * 图片的缩放方法
     * @param bgimage：源图片资源
     * @param newWidth：缩放后宽度
     * @param newHeight：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        float scale = scaleWidth>scaleHeight?scaleWidth:scaleHeight;
        // 缩放图片动作
        matrix.postScale(scale, scale);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

}
