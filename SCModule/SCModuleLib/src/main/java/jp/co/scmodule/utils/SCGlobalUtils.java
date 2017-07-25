package jp.co.scmodule.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.scmodule.R;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.interfaces.SCDialogCallback;
import jp.co.scmodule.interfaces.SCSNSLoginCallback;
import jp.co.scmodule.interfaces.SCTimerCallback;
import jp.co.scmodule.interfaces.SCTypeUserCallback;
import jp.co.scmodule.objects.SCTypeObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.widgets.SCCustomDialog;
import jp.co.scmodule.widgets.SCProgressDialog;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Administrator on 6/5/2015.
 */
public class SCGlobalUtils {
    private static final String TAG_LOG = "SCGlobalUtils";
    public static String DEVICEUUID = null;
    private static SCProgressDialog sPdLoading = null;
    public static Boolean addAccessTokenWithHeader = false;
    public static Boolean addAditionalHeader = false;
    public static String additionalHeaderTag = null;
    public static String additionalHeaderValue = null;
    public static Boolean ShowTutorial = true;
    public static String ishashpass = "0";
    public static String regId = null;
    public static int from_push = 0;
    public static String campus_work_status = null;
    public static String campus_work_category = null;
    public static boolean showTutInSCPage = true;
    public static boolean first_run = true;
    public static boolean from_slide_menu = false;
    public static boolean ar_notify_checked = false;
    public static String is_point_updated = "false";
    public static boolean showCampusWork = false;
    public static SharedPreferences sharedPreferences = null;
    public static HashMap<String, ArrayList<Object>> mListBanner = new HashMap<>();
    public static boolean tc_device_available_in_campus = false;
    public static String old_point = "0";
    public static String new_point = "0";
    public static int count_no_join_campus_work = 10;
    public static int special_rate = 0;
    public static int discount_rate = 0;
    public static boolean is_all_category_pressed = true;
    //canpass is using it
    public static String gcm_id = "";
    public static String access_token = null;
    public static String refresh_access_token = null;
    public static List<String> count_not_join_campus_work = new ArrayList<String>();
    public static String coupon_item = null;
    public static String is_first_login = "false";
    public static boolean update_profile = false;
    public static boolean is_fav_selected = false;
    public static boolean is_used_selected = false;


    public SCGlobalUtils(Context context) {
        new SCImageUtils(context);
        new SCAPIUtils(context);
    }

    public static String make_url_for_line(SCUserObject mUserObj) {
        String content_text =
                mUserObj.getNickname() + "さんから学生団体 " + mUserObj.getStudent_group_name() + " への招待が来ました。"
                        + "SmartCampusで学生団体を登録して皆んなでポイントを貯めよう！\n\n"

                        + "Smart Campusとは?"
                        + "http://mag.smart-campus.jp/smart-campus%E3%81%A8%E3%81%AF\n\n"

                        + "タダコピのインストールはこちら"
                        + "http://app.tadacopy.jp/app_users/install\n\n"

                        + "CAN>PASSのインストールはこちら"
                        + "http://app.tadacopy.jp/app_users/install_canpass";

        String extra_content = "";
        try {
            extra_content = URLEncoder.encode(content_text, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String open_url = "http://line.me/R/msg/text/?" + extra_content;
        return open_url;
    }

    public static ArrayList<Activity> sActivityArr = new ArrayList<Activity>();

    public static void requestGetUserByDeviceId(final Context context, final SCTypeUserCallback loginCallback) {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());

        String appId = SCSharedPreferencesUtils.getString(context, SCConstants.TAG_APP_ID, "");
        String uuid = SCSharedPreferencesUtils.getString(context, SCConstants.TAG_DEVICE_ID, null);
        ;

//        String appId = SCSharedPreferencesUtils.getString(context, SCConstants.TAG_APP_ID, "");
        //   String uuid = SCGlobalUtils.getDeviceUUID(context);

        String applicationId = "";
        if (context.getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
            applicationId = SCConstants.APP_ID_TADACOPY;
        } else if (context.getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
            applicationId = SCConstants.APP_ID_CANPASS;
        }

        String src = secretKey + uuid + date;
        String key = SCGlobalUtils.md5Hash(src);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_DATE, date);
        params.put(SCConstants.PARAM_KEY, key);
        params.put(SCConstants.PARAM_DEVICE_ID, uuid);
//        params.put(SCConstants.PARAM_APP_ID, appId);
        params.put(SCConstants.PARAM_APPLICATION_ID, applicationId);

        SCRequestAsyncTask requestAsync = new SCRequestAsyncTask(context, SCConstants.REQUEST_GET_USER_BY_DEVICE_ID, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_USER_BY_DEVICE_ID, result);

                if (returnHashMap.containsKey(SCConstants.TAG_APP_USER)) {
                    // do something here
                    loginCallback.onLoginSuccess((SCTypeObject) returnHashMap.get(SCConstants.TAG_APP_USER));
                } else {
                    loginCallback.onLoginFail();
                }
            }

            @Override
            public void progress() {

            }

            @Override
            public void onInterrupted(Exception e) {

                loginCallback.onLoginFail();
            }

            @Override
            public void onException(Exception e) {

                loginCallback.onLoginFail();
            }
        });

        requestAsync.execute();
    }

    public static String getMD5Key(String Secret_Key, String AppId, long TimeMiliSecond) {
        String result = "";
        String inputStr = Secret_Key + AppId + TimeMiliSecond;
        result = md5(inputStr);
        return result;
    }

    public static boolean is_app_first_run(Context context) {
        Boolean value = SCSharedPreferencesUtils.getBoolean(context, SCConstants.TAG_FIRST_RUN, true);
        SCSharedPreferencesUtils.putBoolean(context, SCConstants.TAG_FIRST_RUN, false);
        return value;
    }

    public static String getBannerLastUpdate(Activity mActivity) {
        sharedPreferences =
                mActivity.getSharedPreferences(SCConstants.SHARED_PREFERENCES_GET_BANNER, Context.MODE_PRIVATE);
        String result = "";
        result = sharedPreferences.getString(SCConstants.SHARED_PREFERENCES_GET_BANNER, SCGlobalUtils.getTimeInMilisecond() + "");
        return result;
    }


    public static void requestGetUserByAccessToken(final Context context, final SCSNSLoginCallback loginCallback) {


        HashMap<String, Object> params = new HashMap<String, Object>();


        SCRequestAsyncTask requestAsync = new SCRequestAsyncTask(context, SCConstants.REQUEST_GET_USER_BY_ACCESS_TOKEN, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_USER_BY_ACCESS_TOKEN, result);

                if (returnHashMap.containsKey(SCConstants.TAG_DATA)) {
                    // do something here
                    loginCallback.onLoginSuccess((SCUserObject) returnHashMap.get(SCConstants.TAG_DATA));
                } else {
                    loginCallback.onLoginFail();
                }
            }

            @Override
            public void progress() {

            }

            @Override
            public void onInterrupted(Exception e) {
                loginCallback.onLoginFail();
            }

            @Override
            public void onException(Exception e) {
                loginCallback.onLoginFail();
            }
        });

        requestAsync.execute();
    }

    public static void requestRegisterDevice(final Context context, final SCUserObject userObj, final SCSNSLoginCallback loginCallback) {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();
        String uuid = SCGlobalUtils.DEVICEUUID;

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_APP_ID, appId);
        params.put(SCConstants.PARAM_DATE, date);
        params.put(SCConstants.PARAM_KEY, key);
        params.put(SCConstants.PARAM_DEVICE_ID, uuid);

        SCRequestAsyncTask requestAsync = new SCRequestAsyncTask(context, SCConstants.REQUEST_REGISTER_DEVICE, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_REGISTER_DEVICE, result);

                if (returnHashMap.containsKey(SCConstants.TAG_DEVICE_ID)) {
                    String deviceId = (String) returnHashMap.get(SCConstants.TAG_DEVICE_ID);
                    SCSharedPreferencesUtils.putString(context, SCConstants.TAG_DEVICE_ID, deviceId);
                    Log.e(TAG_LOG, "Device ID:" + deviceId);
                    loginCallback.onLoginSuccess(userObj);
                } else {
                    loginCallback.onLoginFail();
                }

            }

            @Override
            public void progress() {

            }

            @Override
            public void onInterrupted(Exception e) {
                loginCallback.onLoginFail();
            }

            @Override
            public void onException(Exception e) {
                loginCallback.onLoginFail();
            }
        });

        requestAsync.execute();
    }

    public static void saveBannerLastUpdate(Activity mActivity, String time) {
        sharedPreferences = mActivity.getSharedPreferences(SCConstants.SHARED_PREFERENCES_GET_BANNER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SCConstants.SHARED_PREFERENCES_GET_BANNER, time);
        editor.commit();
    }

    public static void requestLoginSNS(final Context context, final String token, final String tokenSecret, final SCSNSLoginCallback loginCallback) {
        String agent = SCConstants.AGENT;
        String applicationId = "";
        if (context.getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
            applicationId = SCConstants.APP_ID_TADACOPY;
        } else if (context.getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
            applicationId = SCConstants.APP_ID_CANPASS;
        }

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_APPLICATION_ID, applicationId);
        params.put(SCConstants.PARAM_AGENT, agent);

        final int type;
        final String provider;
        if (tokenSecret == null) {
            type = SCConstants.REQUEST_LOGIN_FACEBOOK;
            provider = SCConstants.PROVIDER_FACEBOOK;

            params.put(SCConstants.PARAM_FACEBOOK_TOKEN, token);
        } else {
            type = SCConstants.REQUEST_LOGIN_TWITTER;
            provider = SCConstants.PROVIDER_TWITTER;

            params.put(SCConstants.PARAM_TWITTER_ACCESS_TOKEN, token);
            params.put(SCConstants.PARAM_TWITTER_ACCESS_TOKEN_SECRET, tokenSecret);
        }

        if (type == 0) {
            return;
        }

        SCRequestAsyncTask requestAsync = new SCRequestAsyncTask(context, type, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHashMap = null;

                if (type == SCConstants.REQUEST_LOGIN_FACEBOOK) {
                    returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_LOGIN_FACEBOOK, result);
                }

                if (type == SCConstants.REQUEST_LOGIN_TWITTER) {
                    returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_LOGIN_TWITTER, result);
                }

                SCUserObject userObj = null;
                String errCode = null;

                String title = null;
                String body = null;
                if (returnHashMap.containsKey(SCConstants.TAG_DATA)) {
                    userObj = (SCUserObject) returnHashMap.get(SCConstants.TAG_DATA);
                    userObj.setIsGuest("false");

                    //SCUserObject.updateInstance(userObj);

                    // save appid to sharepreference
                    SCSharedPreferencesUtils.putString(context, SCConstants.TAG_APP_ID, userObj.getAppId());
                    SCSharedPreferencesUtils.putString(context, SCConstants.TAG_LOGIN_TYPE, provider);
                }

                if (returnHashMap.containsKey(SCConstants.TAG_ERROR_CODE)) {
                    return;
                }

                requestRegisterDevice(context, userObj, loginCallback);
            }

            @Override
            public void progress() {

            }

            @Override
            public void onInterrupted(Exception e) {
                loginCallback.onLoginFail();
            }

            @Override
            public void onException(Exception e) {

                loginCallback.onLoginFail();
            }
        });

        requestAsync.execute();

    }

    public static void requestGetUser(Context context, final String appId, final String provider, final SCRequestAsyncTask.ObjectCallback callback) {
        if (appId == null) {
            callback.done(null);
            return;
        }
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);

        SCRequestAsyncTask requestAsync = new SCRequestAsyncTask(context, SCConstants.REQUEST_GET_USER, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                SCGlobalUtils.dismissLoadingProgress();

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_UPDATE_USER, result);
                SCUserObject userObj = null;

                if (returnHashMap.containsKey(SCConstants.TAG_APP_USER)) {
                    userObj = (SCUserObject) returnHashMap.get(SCConstants.TAG_APP_USER);
                    userObj.setAppId(appId);
                    if (provider.equals(SCConstants.PROVIDER_GUEST)) {
                        userObj.setIsGuest("true");
                    } else {
                        userObj.setIsGuest("false");
                    }

                    callback.done(userObj);
                } else if (returnHashMap.containsKey(SCConstants.TAG_ERROR_CODE)) {
                    callback.done(null);
                } else {
                    callback.done(null);
                }
            }

            @Override
            public void progress() {
            }

            @Override
            public void onInterrupted(Exception e) {
                callback.done(null);
            }

            @Override
            public void onException(Exception e) {
                callback.done(null);
            }
        });
        requestAsync.execute();
    }


    public static void requestForAccessToken(Context context, final SCRequestAsyncTask.ObjectCallback callback) {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());

        String src = SCConstants.SECRET_KEY + date + SCConstants.TADACOPY_CLIENT_ID_SECRET;
        ;
        String state = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_STATE, state);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_UUID, getDeviceUUID(context));
        parameter.put("client_id", SCConstants.TADACOPY_CLIENT_ID);

        SCRequestAsyncTask requestAsync = new SCRequestAsyncTask(context, SCConstants.REQUEST_GET_ACCESSTOKEN, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                SCGlobalUtils.dismissLoadingProgress();

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_ACCESSTOKEN, result);

                if (returnHashMap.containsKey(SCConstants.TAG_DATA)) {
                    callback.done(returnHashMap.get(SCConstants.TAG_DATA));
                } else if (returnHashMap.containsKey(SCConstants.TAG_ERROR_CODE)) {
                    callback.done(null);
                } else {
                    callback.done(null);
                }
            }

            @Override
            public void progress() {
            }

            @Override
            public void onInterrupted(Exception e) {
                callback.done(null);
            }

            @Override
            public void onException(Exception e) {
                callback.done(null);
            }
        });
        requestAsync.execute();
    }

    // Option of ImageLoader
    public static DisplayImageOptions sOptForImgLoader = new DisplayImageOptions.Builder()
            .showImageOnLoading(android.R.color.white).showImageForEmptyUri(R.color.common_gray)
            .showImageOnFail(R.color.common_gray).bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true).cacheOnDisk(false).considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();
    // .displayer(new FadeInBitmapDisplayer(200))//500 is the fade
    // animation time before image show
    // .displayer(new RoundedBitmapDisplayer(20))//this opt is rounded
    // bitmap
    // .displayer(new FadeInBitmapDisplayer(200))

    public static DisplayImageOptions sOptForUserIcon = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.color.common_gray).showImageForEmptyUri(R.drawable.default_avatar)
            .showImageOnFail(R.drawable.default_avatar).bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(false).cacheOnDisk(false).considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();

    /**
     * TODO Function:<br>
     * To show loading progess
     *
     * @author: Phan Tri
     * @date: Mar 9, 2015
     */
    public static void showLoadingProgress(Context context) {
        if (SCProgressDialog.sPdCount <= 0) {
            SCProgressDialog.sPdCount = 0;
            sPdLoading = null;
            sPdLoading = new SCProgressDialog(context, R.style.CustomDialogTheme);
            sPdLoading.show();
            if (Build.VERSION.SDK_INT > 10) {
                View loadingV = LayoutInflater.from(context).inflate(R.layout.layout_pd_loading, null);
                new SCMultipleScreen(context);
                SCMultipleScreen.resizeAllView((ViewGroup) loadingV);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(SCMultipleScreen.getValueAfterResize(340),
                        SCMultipleScreen.getValueAfterResize(340));
                sPdLoading.addContentView(loadingV, lp);
            } else {
                String message = context.getResources().getString(R.string.common_loading);
                sPdLoading.setMessage(message);
            }
            SCProgressDialog.sPdCount++;
        } else {
            SCProgressDialog.sPdCount++;
        }
    }

    /**
     * TODO Function:<br>
     * To dismiss loading progess
     *
     * @author: Phan Tri
     * @date: Mar 9, 2015
     */
    public static void dismissLoadingProgress() {
        if (SCProgressDialog.sPdCount <= 1) {
            if (sPdLoading != null && sPdLoading.isShowing())
                sPdLoading.dismiss();
            SCProgressDialog.sPdCount--;
        } else {
            SCProgressDialog.sPdCount--;
        }
    }

    public static void showInfoDialog(Context context, String title, String body, String action, final SCDialogCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_show_info_dialog, null);

        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        Button btnOK = (Button) infoDialog.findViewById(R.id.dialog_btn_positive);
        TextView tvTitle = (TextView) infoDialog.findViewById(R.id.dialog_tv_title);
        TextView tvBody = (TextView) infoDialog.findViewById(R.id.dialog_tv_body);

        if (title == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        if (body == null) {
            tvBody.setVisibility(View.GONE);
        } else {
            tvBody.setText(body);
        }

        if (action != null) {
            btnOK.setText(action);
        }
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction1();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }


    public static void showGuestRegisterDialog(Context context, final SCDialogCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.dialog_guest_register, null);


        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        ImageView imageView = (ImageView) v.findViewById(R.id.scmain_img_scicon);
        ImageView close = (ImageView) v.findViewById(R.id.btn_close_copy_code);
        Button webpage = (Button) v.findViewById(R.id.btn_goto_web);

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction1();
                }
                infoDialog.dismiss();
            }
        });

        webpage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction2();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }


    public static void showTermsDialog(Context context, final SCDialogCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.dialog_terms, null);


        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);


        Button webpage = (Button) v.findViewById(R.id.btn_goto_web);
        WebView mWebView = (WebView) v.findViewById(R.id.wb_terms);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/term_text.html");


        webpage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction1();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }

    public static void showLoginDialog(Context mContext, final SCDialogCallback dialogCallback) {
        View v = ((Activity) mContext).getLayoutInflater().inflate(R.layout.layout_ecconfirm_login, null);
        Button btnClose = (Button) v.findViewById(R.id.ec_btn_close);
        Button btnLoginByFacebook = (Button) v.findViewById(R.id.ec_btn_login_by_facebook);
        Button btnLoginByTwitter = (Button) v.findViewById(R.id.ec_btn_login_by_twitter);
        Button btnLoginByEmail = (Button) v.findViewById(R.id.ec_btn_login_by_email);

        SCMultipleScreen.resizeAllView((ViewGroup) v);
        final Dialog dialogLogin = new SCCustomDialog(mContext);
        dialogLogin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogin.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        dialogLogin.setContentView(v);
        dialogLogin.show();

        btnLoginByFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallback.onAction1();
                dialogLogin.dismiss();
            }
        });

        btnLoginByTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallback.onAction2();
                dialogLogin.dismiss();
            }
        });

        btnLoginByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallback.onAction3();
                dialogLogin.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallback.onAction4();
                dialogLogin.dismiss();
            }
        });
    }

    public static void showLoginDialogGoBackToApp(Context mContext, final SCDialogCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(mContext, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.dialog_back_to_app, null);

        new SCMultipleScreen(mContext);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);


        ImageView btnClose = (ImageView) v.findViewById(R.id.btn_close);
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) v.findViewById(R.id.btn_ok);

        if (mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            btn_ok.setBackgroundResource(R.drawable.btn_selector_rounded);
        } else if (mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            btn_ok.setBackgroundResource(R.drawable.btn_selector_rounded_canpass);
        }


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallback.onAction1();
                infoDialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallback.onAction2();
                infoDialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallback.onAction4();
                infoDialog.dismiss();
            }
        });

        infoDialog.show();

    }

    public static void showConfirmDialog(Context context, String title, String body, String action1, String action2, final SCDialogCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_show_confirm_dialog, null);

        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        Button btnOK = (Button) infoDialog.findViewById(R.id.dialog_btn_positive);
        Button btnCancel = (Button) infoDialog.findViewById(R.id.dialog_btn_negative);
        TextView tvTitle = (TextView) infoDialog.findViewById(R.id.dialog_tv_title);
        TextView tvBody = (TextView) infoDialog.findViewById(R.id.dialog_tv_body);

        if (title == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        if (body == null) {
            tvBody.setVisibility(View.GONE);
        } else {
            if (isHtml(body)) {
                tvBody.setText(Html.fromHtml(body));
            } else {
                tvBody.setText(body);
            }
        }

        if (action1 != null) {
            btnOK.setText(action1);
        }
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction1();
                }
                infoDialog.dismiss();
            }
        });

        if (action2 != null) {
            btnCancel.setText(action2);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction2();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }

    public static void showGetLoginCount(Context context, String old_value, String new_value, final SCDialogCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, jp.co.scmodule.R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(jp.co.scmodule.R.layout.dialog_get_login_point, null);

        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        ImageView one = (ImageView) infoDialog.findViewById(jp.co.scmodule.R.id.count_one);
        ImageView two = (ImageView) infoDialog.findViewById(jp.co.scmodule.R.id.count_two);
        ImageView three = (ImageView) infoDialog.findViewById(jp.co.scmodule.R.id.count_three);
        ImageView four = (ImageView) infoDialog.findViewById(jp.co.scmodule.R.id.count_four);
        ImageView five = (ImageView) infoDialog.findViewById(jp.co.scmodule.R.id.count_five);

        ImageView btnCancel = (ImageView) infoDialog.findViewById(jp.co.scmodule.R.id.btn_close);
        final TextView tvOld = (TextView) infoDialog.findViewById(jp.co.scmodule.R.id.tv_old);
        final TextView tvNew = (TextView) infoDialog.findViewById(jp.co.scmodule.R.id.tv_new);
        final TextView tv_last_label = (TextView) infoDialog.findViewById(jp.co.scmodule.R.id.tv_last_label);
        int remainng = 5 - Integer.parseInt(new_value);
        tv_last_label.setText("ポイントGET\n" +
                "まであと" + remainng + "日");
        View[] views = new View[]{};
        int coin_on = R.drawable.get_count_coin_image_on;

        if (context.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || context.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || context.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            coin_on = R.drawable.get_count_coin_image_on;
            tv_last_label.setBackgroundResource(R.drawable.tv_yellow_backgroung);
        } else if (context.getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || context.getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || context.getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            coin_on = R.drawable.get_count_coin_image_on_cp;
            tv_last_label.setBackgroundResource(R.drawable.tv_yellow_backgroung_canpass);
        }
        switch (new_value) {
            case "0":
                break;
            case "1":
                one.setImageResource(coin_on);
                views = new View[]{tvOld, one, tv_last_label};
                break;
            case "2":
                one.setImageResource(coin_on);
                two.setImageResource(coin_on);
                views = new View[]{tvOld, one, two, tv_last_label};
                break;
            case "3":
                one.setImageResource(coin_on);
                two.setImageResource(coin_on);
                three.setImageResource(coin_on);
                views = new View[]{tvOld, one, two, three, tv_last_label};
                break;
            case "4":
                one.setImageResource(coin_on);
                two.setImageResource(coin_on);
                three.setImageResource(coin_on);
                four.setImageResource(coin_on);
                views = new View[]{tvOld, one, two, three, four, tv_last_label};
                break;
            case "5":
                one.setImageResource(coin_on);
                two.setImageResource(coin_on);
                three.setImageResource(coin_on);
                four.setImageResource(coin_on);
                five.setImageResource(coin_on);
                views = new View[]{tvOld, one, two, three, four, five, tv_last_label};
                break;
        }


        // 100ms delay between Animations
        long delayBetweenAnimations = 500l;

        for (int i = 0; i < views.length; i++) {
            final View view = views[i];

            // We calculate the delay for this Animation, each animation starts 100ms
            // after the previous one
            int delay = (int) (i * delayBetweenAnimations);

            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (view.getId() == R.id.tv_old) {
                        Animation animMove = AnimationUtils.loadAnimation(getApplicationContext(),
                                jp.co.scmodule.R.anim.move_animation);
                        animMove.setStartOffset(300);
                        tvOld.startAnimation(animMove);
                        tvNew.startAnimation(animMove);
                    } else if (view.getId() == R.id.tv_last_label) {
                        Animation animblink = AnimationUtils.loadAnimation(getApplicationContext(),
                                jp.co.scmodule.R.anim.blink_animation);
                        tv_last_label.startAnimation(animblink);
                    } else {
                        flipit(view);

                    }
                }
            }, delay);
        }


        tvOld.setText(old_value);
        tvNew.setText(new_value);

        Animation animMove = AnimationUtils.loadAnimation(getApplicationContext(),
                jp.co.scmodule.R.anim.move_animation);
        tvOld.startAnimation(animMove);
        tvNew.startAnimation(animMove);


        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction2();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }

    private static void flipit(final View viewToFlip) {
        ObjectAnimator flip = ObjectAnimator.ofFloat(viewToFlip, "rotationY", 0f, 360f);
        flip.setDuration(500);
        //flip.setRepeatCount(1);
        flip.setStartDelay(200);
        flip.start();

    }

    public static void show_getpoint_dialog(Context context) {

        final SCCustomDialog infoDialog = new SCCustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_login_bonus_dialog, null);

        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);
        FrameLayout total_view = (FrameLayout) v.findViewById(jp.co.scmodule.R.id.frm_container);
        TextView point = (TextView) v.findViewById(jp.co.scmodule.R.id.tv_point);
        point.setText("100");
        total_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog.dismiss();
            }
        });

        infoDialog.setCancelable(true);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        infoDialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        infoDialog.show();

    }

    public static void showThreeActionDialog(Context context, String title, String body, String action1, String action2, String action3, final SCDialogCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_show_3_action_dialog, null);

        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        Button btnOK = (Button) infoDialog.findViewById(R.id.dialog_btn_positive);
        Button btnCancel = (Button) infoDialog.findViewById(R.id.dialog_btn_negative);
        Button btnOther = (Button) infoDialog.findViewById(R.id.dialog_btn_other);
        TextView tvTitle = (TextView) infoDialog.findViewById(R.id.dialog_tv_title);
        TextView tvBody = (TextView) infoDialog.findViewById(R.id.dialog_tv_body);

        if (title == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        if (body == null) {
            tvBody.setVisibility(View.GONE);
        } else {
            if (isHtml(body)) {
                tvBody.setText(Html.fromHtml(body));
            } else {
                tvBody.setText(body);
            }
        }

        if (action1 != null) {
            btnOK.setText(action1);
        }
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction1();
                }
                infoDialog.dismiss();
            }
        });

        if (action2 != null) {
            btnCancel.setText(action2);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction2();
                }
                infoDialog.dismiss();
            }
        });

        btnOther.setText(action3);
        btnOther.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction3();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static final String md5Hash(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }

    public static boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDeviceUUID(Context context) {
        // check for local first
        String localDeviceId = SCSharedPreferencesUtils.getString(context, SCConstants.TAG_DEVICE_ID, null);
        if (localDeviceId != null) {
            return localDeviceId;
        }

        UUID uuid = null;
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            if (!"9774d56d682e549c".equals(androidId)) {
                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
            } else {
                final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return (uuid == null) ? "" : uuid.toString();
    }

    public static boolean isHtml(String src) {
        String tagStart =
                "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
        String tagEnd =
                "\\</\\w+\\>";
        String tagSelfClosing =
                "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)/\\>";
        String htmlEntity =
                "&[a-zA-Z][a-zA-Z0-9]+;";
        Pattern htmlPattern = Pattern.compile(
                "(" + tagStart + ".*" + tagEnd + ")|(" + tagSelfClosing + ")|(" + htmlEntity + ")",
                Pattern.DOTALL
        );

        boolean ret = false;
        if (src != null) {
            ret = htmlPattern.matcher(src).find();
        }
        return ret;
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long getTimeInMilisecond() {
        long result = 0;
        Calendar cal = Calendar.getInstance();
        result = cal.getTimeInMillis();
        return result;
    }


    public static void setTimer(final long timerLength, final SCTimerCallback callback, final Activity activity) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        callback.timeUp();
                    }
                });
            }
        }, timerLength);
    }

    public static void showInformationCouponDialog(Context context, String title, String body, String action1, final SCDialogCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.dialog_coupon_info, null);

        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        TextView btnCancel = (TextView) infoDialog.findViewById(R.id.tv_close);
        TextView tvTitle = (TextView) infoDialog.findViewById(R.id.tv_coupon_name);
        TextView tvBody = (TextView) infoDialog.findViewById(R.id.tv_coupon_info);

        if (title == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        if (body == null) {
            tvBody.setVisibility(View.GONE);
        } else {
            if (isHtml(body)) {
                tvBody.setText(Html.fromHtml(body));
            } else {
                tvBody.setText(body);
            }
        }

        if (action1 != null) {
            btnCancel.setText(action1);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction1();
                }
                infoDialog.dismiss();
            }
        });


        infoDialog.show();
    }


}
