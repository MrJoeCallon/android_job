package maojian.android.walnut;

import android.os.Environment;
import com.android.volley.TimeoutError;

import java.io.File;

/**
 * Created by joe on 2017/3/13.
 */
public class BaseConstant {
    public static final String wxApi = "wx127ef83489e42393";
    public static String shopify = "https://checkout.shopify.com/15873557/policies/36608836.html";
    public static String store = "https://store.walnuttech.co/";

    public static double longitude = 0;
    public static double latitude = 0;
    public static int km = 0;

    public static String baseServiceUrl = "https://apprest.walnuttech.co/index.php/mobile/";
    public static String appid;

    public static String REQUEST_OK = "1";

    public static final String ERROR_NETWORK = "Network Error";
    public static final String ERROR_SERVICE = "Service Error";
    public static final String Timeout_Error = "Timeout Error";
    public static final String AuthFailureError = "Auth Failure Error";
    public static final String ParseError = "Parse Error";
    public static String register = baseServiceUrl + "Login/register";
    public static String otherRegister = baseServiceUrl + "Login/otherRegister";
    public static String forgetPassword = baseServiceUrl + "Login/forgetPassword";
    public static String login = baseServiceUrl + "Login/login";
    public static String otherLogin = baseServiceUrl + "Login/otherLogin";
    public static String index = baseServiceUrl + "index/index";
    public static String postDetails = baseServiceUrl + "index/postDetails";
    public static String discoverIndex = baseServiceUrl + "index/discoverIndex";
    public static String like = baseServiceUrl + "index/like";
    public static String commentList = baseServiceUrl + "index/commentList";
    public static String deleteComment = baseServiceUrl + "index/deleteComment";
    public static String postImgUpload = baseServiceUrl + "user/postImgUpload";
    public static String uoloadUserHeadImg = baseServiceUrl + "user/uoloadUserHeadImg";
//    public static String getLocationList = baseServiceUrl + "index/getLocationList";
    public static String getLocationList ="https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    public static String addPost = baseServiceUrl + "index/addPost";
    public static String commentPost = baseServiceUrl + "index/commentPost";
    public static String releasePost = baseServiceUrl + "user/releasePost";
    public static String likePost = baseServiceUrl + "user/likePost";
    public static String uploadVoideBast64 = baseServiceUrl + "user/uploadVoideBast64";
    public static String messageList = baseServiceUrl + "index/messageList";
    public static String searchList = baseServiceUrl + "user/searchList";
    public static String removeFollow = baseServiceUrl + "user/removeFollow";
    public static String userInfo = baseServiceUrl + "user/userInfo";
    public static String followList = baseServiceUrl + "user/followList";
    public static String fansList = baseServiceUrl + "user/fansList";
    public static String updatePwd = baseServiceUrl + "login/updatePwd";
    public static String editUser = baseServiceUrl + "user/editUser";
    public static String bindingInfo = baseServiceUrl + "user/bindingInfo";
    public static String feedback = baseServiceUrl + "user/feedback";
    public static String editBindingInfo = baseServiceUrl + "user/editBindingInfo";
    public static String sendMileage = baseServiceUrl + "user/sendMileage";
    public static String orderList = baseServiceUrl + "index/orderList";
    public static String deletePost = baseServiceUrl + "index/deletePost";

    /**
     * 获取程序外部目录
     *
     * @return
     */
    private static final String AppPath = Environment.getExternalStorageDirectory() + "/Walnut/";

    /**
     * APP图片目录
     *
     * @return
     */
    public static File getAppPicFile() {
        File file = new File(AppPath + "Pictures/");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
