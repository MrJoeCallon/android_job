package maojian.android.walnut.volley;

import org.apache.commons.codec.binary.Base64;

/**
 * UrlSafeBase64 用'-'替换'+' 用'_'替换'/' 
 * 编码采用:UTF-8
 * @author zhangkewen
 *
 */
public class Base64Method {

	/**
	 * 加密字符串为base64
	 * @param a_strString
	 * @return
	 * @throws Exception
	 */
	public static String encryptUrlSafeBase64(String a_strString) throws Exception {
		Base64 base64 = new Base64();
		String base64str = new String(base64.encode(a_strString
				.getBytes("utf-8")), "utf-8");
		base64str = base64str.replace("\n", "").replace("\r", "")
				.replace('+', '-').replace('/', '_');
		return base64str;
	}
	
	/**
	 * 加密字符串为base64
	 * @param a_strString
	 * @return
	 * @throws Exception
	 */
	public static String encryptNormalBase64(String a_strString) throws Exception {
		Base64 base64 = new Base64();
		String base64str = new String(base64.encode(a_strString
				.getBytes("utf-8")), "utf-8").replace("\n", "").replace("\r", "");
		return base64str;
	}

	/**
	 * 加密byte为base64
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String encryptUrlSafeBase64(byte[] bytes) throws Exception {
		Base64 base64 = new Base64();
		String base64str = new String(base64.encode(bytes), "utf-8");
		base64str = base64str.replace("\n", "").replace("\r", "")
				.replace('+', '-').replace('/', '_');
		return base64str;
	}
	
	/**
	 * 加密byte为base64
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String encryptNormalBase64(byte[] bytes) throws Exception {
		Base64 base64 = new Base64();
		String base64str = new String(base64.encode(bytes), "utf-8").replace("\n", "").replace("\r", "");
		return base64str;
	}

	/**
	 * 解密base64字符串
	 * @param a_strString
	 * @return
	 * @throws Exception
	 */
	public static String decryptUrlSafeBase64(String a_strString) throws Exception {
		Base64 base64 = new Base64();
		byte[] bytes = base64.decode(a_strString.replace('-', '+')
				.replace('_', '/').getBytes("utf-8"));
		String str = new String(bytes, "utf-8");
		return str;
	}

	/**
	 * 解密base64为byte[]
	 * @param a_strString
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptUrlSafeBase64ForByte(String a_strString)
			throws Exception {
		Base64 base64 = new Base64();
		byte[] bytes = base64.decode(a_strString.replace('-', '+')
				.replace('_', '/').getBytes("utf-8"));
		return bytes;
	}
	
	/**
	 * 解密base64为byte[]
	 * @param a_strString
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptNormalBase64ForByte(String a_strString)
			throws Exception {
		Base64 base64 = new Base64();
		byte[] bytes = base64.decode(a_strString.getBytes("utf-8"));
		return bytes;
	}
}
