package maojian.android.walnut;

import android.app.Application;

import android.content.Context;
import com.avos.avoscloud.AVOSCloud;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import maojian.android.walnut.data.UserInfos;
//import com.crashlytics.android.Crashlytics;
//import com.twitter.sdk.android.Twitter;
//import com.twitter.sdk.android.core.TwitterAuthConfig;
//
//import io.fabric.sdk.android.Fabric;

public class AnyTimeApplication extends Application {
	private static AnyTimeApplication instance;
	// Note: Your consumer key and secret should be obfuscated in your source code before shipping.
	private static final String TWITTER_KEY = "u9uyYYnlumipcGUU6YJSlPtno";
	private static final String TWITTER_SECRET = "kc1UL9CPPI3FDVbGMlYN6PVrCeRzw7cISrJzOA2cShV1T2TzFu";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		UserInfos.initUserInfo(this);
//		TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//		Fabric.with(this, new Twitter(authConfig));
//
//		Fabric.with(this, new Crashlytics());
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);

		//Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);

		//如果使用美国节点，请加上这行代码
		AVOSCloud.useAVCloudUS();
		AVOSCloud.initialize(this,
				"tJ3tBEK3KfQBNItfiXR4l5qU-MdYXbMMI",
				"UikVLEp3EkJ73L3KBkw8R0eq");

		// U need your AVOS key and so on to run the code.
//		AVOSCloud.useAVCloudCN();
//		AVOSCloud.initialize(this,
//				"4jOw6B01AaKMTOXrW87ql6UH-gzGzoHsz",
//				"VUJizmNtiSVp0lthQqH0FmSN");



	}
	public static Context getApplicationInstance() {
		return instance;
	}
}
