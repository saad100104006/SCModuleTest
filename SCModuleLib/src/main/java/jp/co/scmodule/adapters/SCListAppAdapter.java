package jp.co.scmodule.adapters;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.brickred.socialauth.util.Base64;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.co.scmodule.R;
import jp.co.scmodule.adapters.holders.SCListAppHolder;
import jp.co.scmodule.objects.SCAppObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;
import jp.co.scmodule.utils.SCUrlConstants;

/**
 * Created by VNCCO on 6/30/2015.
 */
public class SCListAppAdapter extends BaseAdapter {
    private SCListAppHolder mListAppHolder = null;
    private Activity mActivity = null;
    private ArrayList<Object> mListApp = null;

    private DisplayImageOptions mImageLoaderOpts = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.common_loading_app_icon)
            .showImageForEmptyUri(R.drawable.common_loading_app_icon)
            .showImageOnFail(R.drawable.common_loading_app_icon)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true).cacheOnDisk(false).considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();

    public SCListAppAdapter(Activity activity, ArrayList<Object> listApp) {
        this.mActivity = activity;
        this.mListApp = listApp;
    }

    @Override
    public int getCount() {
        return mListApp.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_app, null);
            mListAppHolder = new SCListAppHolder();
            mListAppHolder.imgApp = (ImageView) convertView.findViewById(R.id.item_app_img);
            mListAppHolder.tvName = (TextView) convertView.findViewById(R.id.item_app_name);
            mListAppHolder.vPaddingRight = convertView.findViewById(R.id.v_padding_right);
            mListAppHolder.vPaddingLeft = convertView.findViewById(R.id.v_padding_left);

            new SCMultipleScreen(mActivity);
            SCMultipleScreen.resizeAllView((ViewGroup) convertView);

            convertView.setTag(mListAppHolder);
        } else {
            mListAppHolder = (SCListAppHolder) convertView.getTag();
        }

        mListAppHolder.resetView();



        if (position == 0) {
            mListAppHolder.vPaddingLeft.setVisibility(View.VISIBLE);
        }

        SCAppObject appObject = new SCAppObject();
        appObject = (SCAppObject) mListApp.get(position);
        ImageLoader.getInstance().displayImage(appObject.getIcon(), mListAppHolder.imgApp, mImageLoaderOpts);

        mListAppHolder.tvName.setText(appObject.getName());
        mListAppHolder.imgApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem(position);
            }
        });
        return convertView;
    }

    private void onClickItem(int position) {
//        String currentPkgName = mActivity.getPackageName();
        String currentPkgName = "tcapp.com";
        SCAppObject scAppObject = (SCAppObject) mListApp.get(position);
        boolean isAppIntall = false;
        Intent intent = null;
        PackageManager pm = mActivity.getPackageManager();
        try {
            pm.getPackageInfo(scAppObject.getApp_id(), PackageManager.GET_ACTIVITIES);

            isAppIntall = true;
        } catch (PackageManager.NameNotFoundException e) {
            isAppIntall = false;
        }
        if (isAppIntall) {
            String packageName = scAppObject.getApp_id();
            if (currentPkgName.equals(packageName)) {
                return;
            }

            try {
                intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(new ComponentName(packageName, packageName + ".SplashActivity"));
                String appId = SCSharedPreferencesUtils.getString(mActivity, SCConstants.TAG_APP_ID, "");
                String loginType = SCSharedPreferencesUtils.getString(mActivity, SCConstants.TAG_LOGIN_TYPE, "");
                intent.putExtra(SCConstants.TAG_APP_ID, appId);
                intent.putExtra(SCConstants.TAG_LOGIN_TYPE, loginType);
                mActivity.startActivity(intent);
            } catch (Exception e) {
                intent = mActivity.getPackageManager().getLaunchIntentForPackage(packageName);
                mActivity.startActivity(intent);
            }
        } else {
            if (!scAppObject.getApp_id().equals("null")) {
                Intent intentMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + scAppObject.getApp_id()));
                mActivity.startActivity(intentMarket);
            } else {
                if (scAppObject.getIsAutiLogin()!=null && scAppObject.getIsAutiLogin().equals("true")) {
                    openBrowserWithAutoLogin(mActivity.getBaseContext(),scAppObject.getUrl());
                } else {
                    String url = scAppObject.getUrl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mActivity.startActivity(i);
                }
            }

        }
    }

    public String getBase64(final String input) {
        return Base64.encodeBytes(input.getBytes(), Base64.DONT_BREAK_LINES);
    }

    private void openBrowserWithAutoLogin(Context context, String URL) {
        //opening other apps or other URLS
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
//        mContext.startActivity(browserIntent);

        SCUserObject mUserObj = SCUserObject.getInstance();
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();
        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);


        String UUID = "";
        if (SCSharedPreferencesUtils.getString(context, SCConstants.TAG_DEVICE_ID, null) == null) {
            if (SCGlobalUtils.DEVICEUUID != null)
                UUID = SCGlobalUtils.DEVICEUUID;
            else {
                UUID = SCGlobalUtils.getDeviceUUID(context);
                if (UUID.equals("")) {
                    Toast.makeText(context, "UUID Missing", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            UUID = SCSharedPreferencesUtils.getString(context, SCConstants.TAG_DEVICE_ID, null);
        }


        String url = SCUrlConstants.URL_DOMAIN + "/api/auto_login?"
                + "app_id=" + appId
                + "&client_id=" + SCConstants.TADACOPY_CLIENT_ID
                + "&redirect_uri=" + URL
                + "&key=" + key
                + "&date=" + date
                + "&uuid=" + UUID
                + "&application_id=" + SCConstants.TADACOPY_DEFAULT_APP_ID_SECRET;

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Bundle bundle = new Bundle();
        Map<String, String> headers = new HashMap<>();
        SCGlobalUtils.additionalHeaderTag = "Authorization";
        SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(context, SCConstants.TAG_ACCESS_TOKEN, null));
        bundle.putString(SCGlobalUtils.additionalHeaderTag, SCGlobalUtils.additionalHeaderValue);
        i.putExtra(Browser.EXTRA_HEADERS, bundle);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }
}
