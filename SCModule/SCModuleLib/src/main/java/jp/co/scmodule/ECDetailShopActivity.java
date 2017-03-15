package jp.co.scmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import jp.co.scmodule.adapters.ECProductAdapter;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.interfaces.SCDialogCallback;
import jp.co.scmodule.interfaces.SCSNSLoginCallback;
import jp.co.scmodule.objects.ECProductObject;
import jp.co.scmodule.objects.ECShopObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.widgets.SCSingleLineTextView;

public class ECDetailShopActivity extends SCMyActivity {
    private static final String TAG_LOG = "ECDetailShopActivity";
    private Context mContext = null;
    private Activity mActivity = null;

    private SCRequestAsyncTask mSCRequestAsyncTask = null;

    private View.OnClickListener mOnClickListener = null;

    private ECProductAdapter mProductAdapter = null;

    private SCRequestAsyncTask mRequestAsync = null;

    private ArrayList<Object> mListData = null;

    private ImageView mImgShop = null;
    private ImageButton mIbtnGoOfficialSite = null;
    private Button mBtnFollowShop = null;
    private Button mBtnNewestSort = null;
    private Button mBtnRankingSort = null;
    private Button mBtnPointSort = null;
    private GridView mGvProduct = null;
    private ImageView mIbtnBack = null;
    private SCSingleLineTextView mTvShopName = null;

    private ECShopObject mShopObj = null;
    private String mShopId = null;

    private String mOrderType = null;

    private TwitterAuthClient mTwitterClient = null;

    private CallbackManager mFacebookCallbackManager = null;

    private WeakReference<Activity> mActivityRef = null;

    private String mToken = null;
    private String mSecret = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SCGlobalUtils.sActivityArr.remove(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        mTwitterClient.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        initGlobalUtils();
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        afterClickBack();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecdetail_shop);

        SCGlobalUtils.sActivityArr.add(this);

        // init twitter sdk
        TwitterAuthConfig authConfig = new TwitterAuthConfig(SCConstants.TWITTER_KEY, SCConstants.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        mActivityRef = new WeakReference<Activity>(this);
        try {
            if(mTwitterClient != null)
                mTwitterClient = new TwitterAuthClient();
        }catch (TwitterApiException e){
            e.printStackTrace();
        }catch (TwitterAuthException e){
            e.printStackTrace();
        }catch (TwitterException e){
            e.printStackTrace();
        }
        // init facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();

        init();
    }

    @Override
    protected void init() {
        super.init();

        mContext = this;
        mActivity = this;

        mOrderType = "1";
        mShopId = getIntent().getStringExtra(String.class.toString());

        initListData();
        requestGetShop();
        requestGetExchangeItems();
    }

    @Override
    protected void findViewById() {
        mImgShop = (ImageView) findViewById(R.id.ecdetail_shop_img_shop);
        mIbtnGoOfficialSite = (ImageButton) findViewById(R.id.ecdetail_shop_ibtn_go_to_offical_site);
        mBtnFollowShop = (Button) findViewById(R.id.ecdetail_shop_btn_follow_shop);
        mBtnNewestSort = (Button) findViewById(R.id.ecdetail_shop_btn_newest_sort);
        mBtnRankingSort = (Button) findViewById(R.id.ecdetail_shop_btn_ranking_sort);
        mBtnPointSort = (Button) findViewById(R.id.ecdetail_shop_btn_point_sort);
        mGvProduct = (GridView) findViewById(R.id.ecdetail_shop_gv_item);
        mIbtnBack = (ImageView) findViewById(R.id.ecdetail_shop_ibtn_back);
        mTvShopName = (SCSingleLineTextView) findViewById(R.id.ecdetail_shop_tv_shop_name);
    }

    @Override
    protected void initListeners() {
        mIbtnGoOfficialSite.setContentDescription("officialSite");
        mBtnFollowShop.setContentDescription("followShop");
        mBtnNewestSort.setContentDescription("newestSort");
        mBtnRankingSort.setContentDescription("rankingSort");
        mBtnPointSort.setContentDescription("pointSort");
        mIbtnBack.setContentDescription("back");

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("officialSite")) {
                    afterClickOfficialSite();
                } else if (v.getContentDescription().equals("followShop")) {
                    afterClickFollowShop();
                } else if (v.getContentDescription().equals("newestSort")) {
                    afterClickNewestSort();
                } else if (v.getContentDescription().equals("rankingSort")) {
                    afterClickRankingSort();
                } else if (v.getContentDescription().equals("pointSort")) {
                    afterClickPointSort();
                } else if (v.getContentDescription().equals("back")) {
                    afterClickBack();
                }
            }
        };
    }

    @Override
    protected void setListenerForViews() {
        mIbtnGoOfficialSite.setOnClickListener(mOnClickListener);
        mBtnFollowShop.setOnClickListener(mOnClickListener);
        mBtnNewestSort.setOnClickListener(mOnClickListener);
        mBtnRankingSort.setOnClickListener(mOnClickListener);
        mBtnPointSort.setOnClickListener(mOnClickListener);
        mIbtnBack.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void resizeScreen() {
        new SCMultipleScreen(this);
        SCMultipleScreen.resizeAllView(this);
    }

    @Override
    protected void initGlobalUtils() {
        new SCGlobalUtils(this);
    }

    private void afterClickOfficialSite() {
        if (mShopObj != null && URLUtil.isValidUrl(mShopObj.getOfficialUrl())) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mShopObj.getOfficialUrl()));
            startActivity(browserIntent);
        }
    }

    private void afterClickFollowShop() {
        if (SCUserObject.getInstance().getIsGuest().equals("true")) {
            SCGlobalUtils.showLoginDialog(this, new SCDialogCallback() {
                @Override
                public void onAction1() {
                    loginWithFacebook(SCConstants.CODE_LOGIN_FOR_FOLLOW_SHOP);
                }

                @Override
                public void onAction2() {
                    loginWithTwitter(SCConstants.CODE_LOGIN_FOR_FOLLOW_SHOP);
                }

                @Override
                public void onAction3() {
                    loginWithEmail(SCConstants.CODE_LOGIN_FOR_FOLLOW_SHOP);
                }

                @Override
                public void onAction4() {

                }
            });
        } else {
            requestFollowShop();
        }
    }

    private void afterClickNewestSort() {
        mBtnNewestSort.setTextColor(getResources().getColor(R.color.common_ec_blue_txt));
        mBtnRankingSort.setTextColor(getResources().getColor(R.color.common_ec_white_blue_txt));
        mBtnPointSort.setTextColor(getResources().getColor(R.color.common_ec_white_blue_txt));

        mBtnNewestSort.setSelected(true);
        mBtnRankingSort.setSelected(false);
        mBtnPointSort.setSelected(false);

        mOrderType = "1";

        requestGetExchangeItems();
    }

    private void afterClickRankingSort() {
        mBtnNewestSort.setTextColor(getResources().getColor(R.color.common_ec_white_blue_txt));
        mBtnRankingSort.setTextColor(getResources().getColor(R.color.common_ec_blue_txt));
        mBtnPointSort.setTextColor(getResources().getColor(R.color.common_ec_white_blue_txt));

        mBtnNewestSort.setSelected(false);
        mBtnRankingSort.setSelected(true);
        mBtnPointSort.setSelected(false);

        mOrderType = "2";

        requestGetExchangeItems();
    }

    private void afterClickPointSort() {
        mBtnNewestSort.setTextColor(getResources().getColor(R.color.common_ec_white_blue_txt));
        mBtnRankingSort.setTextColor(getResources().getColor(R.color.common_ec_white_blue_txt));
        mBtnPointSort.setTextColor(getResources().getColor(R.color.common_ec_blue_txt));

        mBtnNewestSort.setSelected(false);
        mBtnRankingSort.setSelected(false);
        mBtnPointSort.setSelected(true);

        mOrderType = "3";

        requestGetExchangeItems();
    }

    private void afterClickBack() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }


    private void showInfo() {
        if (mShopObj.getFollowed().equals("true")) {
            mBtnFollowShop.setSelected(true);
            mBtnFollowShop.setTextColor(mContext.getResources().getColor(R.color.common_ec_red));
            mBtnFollowShop.setText(getResources().getString(R.string.common_unfollow_shop));
        } else {
            mBtnFollowShop.setSelected(false);
            mBtnFollowShop.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mBtnFollowShop.setText(getResources().getString(R.string.common_follow_shop));
        }
        mTvShopName.setText(mShopObj.getName());
        ImageLoader.getInstance().displayImage(mShopObj.getImage(), mImgShop, SCGlobalUtils.sOptForImgLoader);
    }

    private void initListData() {
        mListData = new ArrayList<Object>();
        mProductAdapter = new ECProductAdapter(mContext, mListData);

        mGvProduct.setHorizontalSpacing(SCMultipleScreen.getValueAfterResize(20));
        mGvProduct.setVerticalSpacing(SCMultipleScreen.getValueAfterResize(20));
        mGvProduct.setAdapter(mProductAdapter);
    }

    private void requestGetShop() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();
        //String appId = "188TZ";

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);
        parameter.put(SCConstants.PARAM_SHOP_ID, mShopId);

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_SHOP_DETAIL, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);
                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_SHOP_DETAIL, result);

                if (returnHash.containsKey(SCConstants.TAG_SHOP)) {
                    mShopObj = (ECShopObject) returnHash.get(SCConstants.TAG_SHOP);
                    showInfo();
                }
            }

            @Override
            public void progress() {

            }

            @Override
            public void onInterrupted(Exception e) {

            }

            @Override
            public void onException(Exception e) {

            }
        });

        mSCRequestAsyncTask.execute();
    }

    private void requestGetExchangeItems() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();
        //String appId = "188TZ";

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);
        parameter.put(SCConstants.PARAM_SHOP_ID, mShopId);
        parameter.put(SCConstants.PARAM_CATEGORY_ID, "");
        parameter.put(SCConstants.PARAM_ORDER_TYPE, mOrderType);

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_EXCHANGE_ITEMS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_EXCHANGE_ITEMS, result);

                if (returnHash.containsKey(SCConstants.TAG_ITEMS)) {
                    mListData.clear();

                    mListData.addAll((ArrayList<ECProductObject>) returnHash.get(SCConstants.TAG_ITEMS));

                    mProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void progress() {
                SCGlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void onInterrupted(Exception e) {
                SCGlobalUtils.dismissLoadingProgress();
            }

            @Override
            public void onException(Exception e) {
                SCGlobalUtils.dismissLoadingProgress();
            }
        });

        mSCRequestAsyncTask.execute();
    }

    private void requestFollowShop() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();
        //String appId = "188TZ";

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);
        parameter.put(SCConstants.PARAM_SHOP_ID, mShopId);

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_FOLLOW_SHOP, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_FOLLOW_SHOP, result);

                if (returnHash.containsKey(SCConstants.TAG_FOLLOWED)) {
                    String followed = (String) returnHash.get(SCConstants.TAG_FOLLOWED);

                    mShopObj.setFollowed(followed);

                    if (mShopObj.getFollowed().equals("true")) {
                        mBtnFollowShop.setSelected(true);
                        mBtnFollowShop.setTextColor(mContext.getResources().getColor(R.color.common_ec_red));
                        mBtnFollowShop.setText(getResources().getString(R.string.common_unfollow_shop));
                    } else {
                        mBtnFollowShop.setSelected(false);
                        mBtnFollowShop.setTextColor(mContext.getResources().getColor(android.R.color.white));
                        mBtnFollowShop.setText(getResources().getString(R.string.common_follow_shop));
                    }
                }
            }

            @Override
            public void progress() {
                SCGlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void onInterrupted(Exception e) {
                SCGlobalUtils.dismissLoadingProgress();
            }

            @Override
            public void onException(Exception e) {
                SCGlobalUtils.dismissLoadingProgress();
            }
        });

        mSCRequestAsyncTask.execute();
    }

    private void loginWithEmail(final int codeType) {
        Intent i = new Intent(mContext, SCMailRegistrationActivity.class);
        i.putExtra(Integer.class.toString(), codeType);
        startActivityForResult(i, codeType);
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void loginWithTwitter(final int codeType) {
        SCGlobalUtils.showLoadingProgress(mContext);
//        try {
//            TwitterSession session = Twitter.getSessionManager().getActiveSession();
//            TwitterAuthToken authToken = session.getAuthToken();
//            if(!authToken.isExpired()) {
//                mToken = authToken.token;
//                mSecret = authToken.secret;
//
//                loginSNS(mToken, mSecret);
//                return;
//            }
//        } catch (Exception e) {
//            GlobalUtils.dismissLoadingProgress();
//            e.printStackTrace();
//        }

        mTwitterClient.authorize(mActivityRef.get(), new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                mToken = result.data.getAuthToken().token;
                mSecret = result.data.getAuthToken().secret;

                loginSNS(codeType, mToken, mSecret);
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
                SCGlobalUtils.dismissLoadingProgress();
            }
        });
    }

    private void loginWithFacebook(final int codeType) {
        SCGlobalUtils.showLoadingProgress(mContext);

        // logout previous user before login new one
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }

        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));

        loginManager.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                mToken = accessToken.getToken();

                loginSNS(codeType, mToken, null);
            }

            @Override
            public void onCancel() {
                SCGlobalUtils.dismissLoadingProgress();
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
                SCGlobalUtils.dismissLoadingProgress();
            }
        });
    }

    private void loginSNS(final int codeType, String token, String secret) {
        SCGlobalUtils.requestLoginSNS(mContext, token, secret, new SCSNSLoginCallback() {
            @Override
            public void onLoginSuccess(SCUserObject userObj) {
                SCGlobalUtils.dismissLoadingProgress();
                if (userObj.getIsNewUser().equals("true")) {
                    Intent intent = new Intent(mContext, SCEditInfoOneActivity.class);
                    intent.putExtra(SCUserObject.class.toString(), userObj);
                    intent.putExtra(Integer.class.toString(), codeType);
                    startActivityForResult(intent, codeType);
                    overridePendingTransition(R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_left);
                } else if ((userObj.getUniversityId().equals("0") || userObj.getUniversityId().equals("null"))
                        || (userObj.getCampusId().equals("0") || userObj.getCampusId().equals("null"))
                        || (userObj.getDepartmentId().equals("0") || userObj.getDepartmentId().equals("null"))
                        || (userObj.getEnrollmentYear().equals("0") || userObj.getEnrollmentYear().equals("null"))) {

                    Intent intent = new Intent(mContext, SCEditInfoOneActivity.class);
                    intent.putExtra(SCUserObject.class.toString(), userObj);
                    intent.putExtra(Integer.class.toString(), codeType);
                    startActivityForResult(intent, codeType);
                    overridePendingTransition(R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_left);

                } else {
                    if (codeType == SCConstants.CODE_LOGIN_FOR_FOLLOW_SHOP) {
                        requestFollowShop();
                    }
                }
            }

            @Override
            public void onLoginFail() {
                SCGlobalUtils.dismissLoadingProgress();
            }
        });
    }
}
