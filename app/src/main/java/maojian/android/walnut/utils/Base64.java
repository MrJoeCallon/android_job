package maojian.android.walnut.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import twitter4j.internal.http.BASE64Encoder;
import java.io.*;

/**
 * @author hezuzhi
 * @Description: ()
 * @date 2017/3/16  11:29.
 * @version: 1.0
 */
public class Base64 {

    /**
     * 将文件转成base64 字符串
     * @return  *
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);;
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return BASE64Encoder.encode(buffer);
    }

//    public static String encodeBase64File(String path) throws Exception {
//        File file = new File(path);;
//        String  result = "";
//        FileInputStream inputFile = new FileInputStream(file);
//        byte[] buffer = new byte[(int) file.length()];
//        inputFile.read(buffer);
//        inputFile.close();
////        return BASE64Encoder.encode(buffer);
//        try {
//            result = new String(buffer, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        Log.d("tsy", "result: ------" + result);
//        return result;
//    }

    public static String imgPath2Base64(String imgPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);

        if (bitmap == null) {
            Log.d("tsy", "图片不存在");
        }
        String result = null;
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            try {
                baos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] bitmapBytes = baos.toByteArray();

            byte[] encode = android.util.Base64.encode(bitmapBytes, android.util.Base64.NO_WRAP);
            try {
                result = new String(encode, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("tsy", "result: ------" + result);
        }
        return result;
    }

    /**
     *
     * @param imgPath
     * @param bitmap
     * @return
     */
    public static String imgToBase64(String imgPath, Bitmap bitmap) {
        if (imgPath !=null && imgPath.length() > 0) {
            bitmap = readBitmap(imgPath);
        }
        if(bitmap == null){
            //bitmap not found!!
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return android.util.Base64.encodeToString(imgBytes, android.util.Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static Bitmap readBitmap(String imgPath) {
        try {
            return BitmapFactory.decodeFile(imgPath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }

    }


}
