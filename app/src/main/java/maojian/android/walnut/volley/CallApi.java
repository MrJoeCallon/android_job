package maojian.android.walnut.volley;





import maojian.android.walnut.BaseConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/16.
 */
public class CallApi {
    private final static  String signKey="1234567890123456";


    public static String getOpenApiUrl(String method, String data) {
        try {
            data =  Base64Method.encryptUrlSafeBase64(data);
            String timestamp = String.valueOf(System.currentTimeMillis());
            Map<String,String> maps=new HashMap<String,String>();
//            maps.put("app_id", BaseConstant.appid);
            maps.put("format", "json");
            maps.put("v", "1000");
            maps.put("timestamp", timestamp);
            maps.put("data", data);
            maps.put("method", method);
            maps.put("sign_type", "1");
            String encryptStr = MapUtil.coverMap2SignString(maps);


                String sign= MD5Util.string2MD5(AesEncryption.encrypt(encryptStr,signKey,signKey));

            //String domain = "https://120.76.100.27";
            //		String domain = "https://api.yunyichina.cn";
            String url = BaseConstant.baseServiceUrl + "/openapi/rest?app_id="+BaseConstant.appid+"&v=1000&format=json&sign_type=1&timestamp=" + timestamp
                    + "&method=" + method + "&sign=" + sign + "&data=" + data;
            System.out.println("调用 openapi 接口url： " + url);
            String item = HttpsUtil.doSslGet(url, "utf-8");
            System.out.println("调用 openapi 接口，返回： " + item);
            return item ;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getHttpGetUrl(String method, String data) {
        try {
            data =  Base64Method.encryptUrlSafeBase64(data);
            String timestamp = String.valueOf(System.currentTimeMillis());
            Map<String,String> maps=new HashMap<String,String>();
            maps.put("app_id", BaseConstant.appid);
            maps.put("format", "json");
            maps.put("v", "1000");
            maps.put("timestamp", timestamp);
            maps.put("data", data);
            maps.put("method", method);
            maps.put("sign_type", "1");
            String encryptStr = MapUtil.coverMap2SignString(maps);


            String sign= MD5Util.string2MD5(AesEncryption.encrypt(encryptStr,signKey,signKey));

            String url = BaseConstant.baseServiceUrl + "/openapi/rest?app_id="+BaseConstant.appid+"&v=1000&format=json&sign_type=1&timestamp=" + timestamp
                    + "&method=" + method + "&sign=" + sign + "&data=" + data;
            System.out.println("调用 openapi 接口url： " + url);
//            String item = HttpsUtil.doSslGet(url, "utf-8");
//            System.out.println("调用 openapi 接口，返回： " + item);
            return url ;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void  main(String[] a){

//        String data = "{\"hospital_code\":\"" + "szsdsrmyy" + "\",\"branch_code\":\"" + "45575559X" +
//                "\",\"pat_card_type\":\""  + "1"
//                + "\",\"pat_card_no\":\"" + "0000424777"
////                + "\",\"pat_name\":\"" + "王炜波"
//                + "\"}";
//        String method = "yyt.clinic.mzfee.out.list";
//        String data = "{\"hospital_code\":\"" + BaseConstant.hocode
//                + "\",\"branch_code\":\"" + "45575559X"
//                + "\",\"medicalCardNo\":\"" + "0000424737"
//                + "\"}";
//        String method = "yyt.base.order.list";

        String data = "{\"hospital_code\":\"szsdsrmyy\",\"branch_code\":\"szsdsrmyy\",\"dept_code\":\"0101\",\"doctor_code\":\"000665\",\"dept_name\":\"内科门诊\",\"doctor_name\":\"陈培芬\",\"pat_card_type\":\"1\",\"pat_card_no\":\"0000424777\",\"begin_time\":\"16:30\",\"end_time\":\"17:00\",\"work_id\":\"452289\",\"reg_fee\":\"100\",\"treat_fee\":\"2000\",\"order_mode\":\"2\",\"order_time\":\"2016-12-02 16:29:00\"}";
        String method = "yyt.register.reg.info.order";
        String item = getOpenApiUrl(method,data);

    }
}
