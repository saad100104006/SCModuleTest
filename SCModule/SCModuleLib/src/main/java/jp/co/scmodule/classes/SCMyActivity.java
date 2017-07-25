package jp.co.scmodule.classes;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import io.fabric.sdk.android.Fabric;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCGlobalUtils;

/**
 * The abstract class for all activity
 * @author PhanTri
 *
 */
public abstract class SCMyActivity extends FragmentActivity{
	private SCUserObject mUserObj = null;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(SCUserObject.class.toString(), mUserObj);//1 is saved. 0 is never saved
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Fabric.with(this, new Crashlytics());

		initImageLoader(this);
		if (savedInstanceState != null) {
			if (savedInstanceState.getParcelable(SCUserObject.class.toString()) != null) {
				mUserObj = SCUserObject.getInstance();
				mUserObj.updateInstance((SCUserObject) savedInstanceState.getParcelable(SCUserObject.class.toString()));
			}
		} else {
			mUserObj = SCUserObject.getInstance();
		}
	}

	private void initImageLoader(Context context) {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
				context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}

	protected void init() {
		findViewById();
		initListeners();
		setListenerForViews();
		resizeScreen();
		initGlobalUtils();
	}
	
	protected abstract void findViewById();
	protected abstract void initListeners();
	protected abstract void setListenerForViews();
	protected abstract void resizeScreen();
	protected abstract void initGlobalUtils();
}
