package maojian.android.walnut.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;
import maojian.android.walnut.BaseConstant;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/30.
 */
public class BitmapUtils {
    /**
     * 图片压缩
     *
     * @param path
     * @return
     */
    public static File scal(String path) {
//        String path = fileUri.getPath();
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        final long fileMaxSize = 100 * 1024;//100K
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = new File(createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.d("outputFile.length()", outputFile.length() + "");
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
                outputFile = new File(createImageFile().getPath());
                copyFileUsingFileChannels(tempFile, outputFile);
            }

        }
        return outputFile;
    }


    private static Uri createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp;
//        File storageDir1 = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
        File storageDir = BaseConstant.getAppPicFile();
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName, ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image);
    }

    private static void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 50;
        long fileSize = baos.toByteArray().length;
        final long fileMaxSize = 30 * 1024;//30K
        if (fileSize >= fileMaxSize) {
            float scale = (float) (1 / Math.sqrt((float) fileSize / fileMaxSize));
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale); //长和宽放大缩小的比例
            Bitmap resizeBmp = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
            return resizeBmp;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
