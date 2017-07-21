package jp.co.scmodule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import jp.co.scmodule.adapters.ECProductAdapter;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.interfaces.SCDialogCallback;
import jp.co.scmodule.interfaces.SCSNSLoginCallback;
import jp.co.scmodule.objects.ECCategoryObject;
import jp.co.scmodule.objects.ECProductObject;
import jp.co.scmodule.objects.ECShopObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.widgets.SCCircleImageView;
import jp.co.scmodule.widgets.SCSingleLineTextView;

public class ECMainActivity extends SCMyActivity {
    private static final String TAG_LOG = "ECMainActivity";
    private Context mContext = null;
    private Activity mActivity = null;

    private static final String TAG_RECENT = "recent";
    private static final String TAG_FAVOR = "favor";
    private static final String TAG_POINT = "point";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_SHOP = "shop";
    private static final String TAG_KEYWORD = "keyword";
    private View DialogView;
    Dialog mDialog_tut1 = null;
    private SCRequestAsyncTask mSCRequestAsyncTask = null;

    private View.OnClickListener mOnClickListener = null;
    private AdapterView.OnItemSelectedListener mOnItemSelectedListener = null;
    private TextView.OnEditorActionListener mOnEditorActionListener = null;

    private TextView mTvTitle = null;
    private ImageButton mIbtnBack = null;
    private ImageButton mIbtnChangeSearchType = null;
    private SCCircleImageView mImgAvatar = null;
    private LinearLayout mLlSlideMenu = null;
    private ImageButton mIbtnMostRecent = null;
    private ImageButton mIbtnMostFavor = null;
    private ImageButton mIbtnMostPoint = null;
    private ImageButton mIbtnMostCategory = null;
    private ImageButton mIbtnMostShop = null;
    private ImageButton mIbtnMostKeyword = null;
    private RelativeLayout mRlFilter = null;
    private GridView mGvProduct = null;
    private SCSingleLineTextView mTvFilter = null;
    private Spinner mSpnFilter = null;
    private EditText mEtSearch = null;
    private TextView mTvUserPoint = null;
    private View mVCover = null;

    private ArrayList<ECShopObject> mShopList = null;
    private ArrayList<ECCategoryObject> mCategoryList = null;
    private List<String> mPointList = null;

    private ECProductAdapter mProductAdapter = null;

    private SCRequestAsyncTask mRequestAsync = null;

    private TextWatcher mTextWatcher = null;

    private ArrayList<Object> mListData = null;

    private String mOrderType = null;
    private String mShopId = null;
    private String mCategoryId = null;

    private TwitterAuthClient mTwitterClient = null;

    private CallbackManager mFacebookCallbackManager = null;

    private WeakReference<Activity> mActivityRef = null;

    private String mToken = null;
    private String mSecret = null;

    private DisplayImageOptions imageOption = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_avatar).showImageForEmptyUri(R.color.common_gray)
            .showImageOnFail(R.drawable.default_avatar).bitmapConfig(Bitmap.Config.RGB_565).build();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SCGlobalUtils.sActivityArr.remove(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SCConstants.CODE_SHOW_PRODUCT_DETAIL
                    || requestCode == SCConstants.CODE_SHOW_POINT_MANAGER) {
                ECProductObject productObj = data.getExtras().getParcelable(ECProductObject.class.toString());

                for (Object obj : mListData) {
                    if (((ECProductObject) obj).getId().equals(productObj.getId())) {
                        ((ECProductObject) obj).setFavorite(productObj.getFavorite());
                    }

                    mProductAdapter.notifyDataSetChanged();
                }
            }

            if (requestCode == SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM) {
                mProductAdapter.afterClickExchangePoint(mProductAdapter.getActivePosition());
                showInfo();
            }

            if (requestCode == SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM) {
                mProductAdapter.requestAddFavorite();
                showInfo();
            }
        }

        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (mTwitterClient != null) {
            mTwitterClient.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        initGlobalUtils();
        showInfo();
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
        setContentView(R.layout.activity_ecmain);

        SCGlobalUtils.sActivityArr.add(this);

        // init twitter sdk
        TwitterAuthConfig authConfig = new TwitterAuthConfig(SCConstants.TWITTER_KEY, SCConstants.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        mActivityRef = new WeakReference<Activity>(this);
        try {
            if (mTwitterClient != null)
                mTwitterClient = new TwitterAuthClient();
        } catch (TwitterApiException e) {
            e.printStackTrace();
        } catch (TwitterAuthException e) {
            e.printStackTrace();
        } catch (TwitterException e) {
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

        mShopList = new ArrayList<ECShopObject>();
        mCategoryList = new ArrayList<ECCategoryObject>();
        mPointList = new ArrayList<String>();

        showInfo();

        initListData();

        // default is favor tab
        mIbtnChangeSearchType.setTag(TAG_FAVOR);
        mOrderType = "2";
        requestGetShops();
        requestGetCategories();

    }

    @Override
    protected void findViewById() {
        mTvTitle = (TextView) findViewById(R.id.ecmain_tv_title);
        mIbtnChangeSearchType = (ImageButton) findViewById(R.id.ecmain_ibtn_change_search_type);
        mIbtnBack = (ImageButton) findViewById(R.id.ecmain_ibtn_back);
        mImgAvatar = (SCCircleImageView) findViewById(R.id.ecmain_img_avatar);
        mLlSlideMenu = (LinearLayout) findViewById(R.id.ecmain_ll_slide_menu);
        mIbtnMostRecent = (ImageButton) findViewById(R.id.ecmain_ibtn_tab_recent);
        mIbtnMostFavor = (ImageButton) findViewById(R.id.ecmain_ibtn_tab_favor);
        mIbtnMostPoint = (ImageButton) findViewById(R.id.ecmain_ibtn_tab_point);
        mIbtnMostCategory = (ImageButton) findViewById(R.id.ecmain_ibtn_tab_category);
        mIbtnMostShop = (ImageButton) findViewById(R.id.ecmain_ibtn_tab_shop);
        mIbtnMostKeyword = (ImageButton) findViewById(R.id.ecmain_ibtn_tab_keyword);
        mRlFilter = (RelativeLayout) findViewById(R.id.ecmain_rl_filter);
        mSpnFilter = (Spinner) findViewById(R.id.ecmain_spn_filter);
        mGvProduct = (GridView) findViewById(R.id.ecmain_gv_item);
        mTvFilter = (SCSingleLineTextView) findViewById(R.id.ecmain_tv_filter);
        mEtSearch = (EditText) findViewById(R.id.ecmain_et_search);
        mVCover = findViewById(R.id.ecmain_v_cover);
        mTvUserPoint = (TextView) findViewById(R.id.ecmain_tv_user_point);
        DialogView = this.getLayoutInflater().inflate(R.layout.dialog_thanks_page, null);
        mDialog_tut1 = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
    }

    public void after_success_show_thanks_page() {
        requestRecommendExchangeItem();
    }


    public void show_thanks_dialog(String image_URL, String is_favourite, String point, String product, String shopname) {
        View v = DialogView;
        ImageView image_header = (ImageView) v.findViewById(R.id.image_header);
        if (is_favourite.equals("true")) {
            image_header.setImageResource(R.drawable.header_with_fav);
        } else {
            image_header.setImageResource(R.drawable.header_without_fav);
        }
        ImageView image = (ImageView) v.findViewById(R.id.image);

        TextView item_ec_tv_owner = (TextView) v.findViewById(R.id.item_ec_tv_owner);
        item_ec_tv_owner.setText(shopname);

        TextView item_ec_tv_name = (TextView) v.findViewById(R.id.item_ec_tv_name);
        item_ec_tv_name.setText(product);

        TextView item_ec_tv_point = (TextView) v.findViewById(R.id.item_ec_tv_point);
        item_ec_tv_point.setText(point + " " + mContext.getResources().getString(R.string.common_point));

        ImageView close = (ImageView) v.findViewById(R.id.btn_close_copy_code);
        ImageView dashboard = (ImageView) v.findViewById(R.id.btn_go_to_dashboard);
        Button campus = (Button) v.findViewById(R.id.btn_goto_campus);
        ImageLoader.getInstance().displayImage(image_URL, image, SCGlobalUtils.sOptForImgLoader);
        campus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
                    intent.setAction(SCConstants.ACTION_OPEN_CONTENT_TADACOPY);
                } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
                    intent.setAction(SCConstants.ACTION_OPEN_CONTENT_CANPASS);
                }
                else if (getPackageName().equals(SCConstants.PACKAGE_TORETAN_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TORETAN_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TORETAN_STAGING)) {
                    intent.setAction(SCConstants.ACTION_OPEN_CONTENT_TORETAN);
                }
                intent.putExtra(SCUserObject.class.toString(), SCUserObject.getInstance());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
                SCGlobalUtils.showCampusWork = true;
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterClickAvatar();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog_tut1.dismiss();
            }
        });
        SCMultipleScreen.resizeAllView((ViewGroup) v);
        mDialog_tut1.setCancelable(false);
        mDialog_tut1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog_tut1.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        mDialog_tut1.setContentView(v);
        mDialog_tut1.show();
    }


    @Override
    protected void initListeners() {
        mOnEditorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    requestGetExchangeItems();
                    //return true;
                }

                return false;
            }
        };

        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mIbtnChangeSearchType.setSelected(false);
        mIbtnChangeSearchType.setContentDescription("searchType");
        mIbtnBack.setContentDescription("back");
        mImgAvatar.setContentDescription("avatar");
        mIbtnMostRecent.setContentDescription("mostRecent");
        mIbtnMostFavor.setContentDescription("mostFavor");
        mIbtnMostPoint.setContentDescription("mostPoint");
        mIbtnMostCategory.setContentDescription("mostCategory");
        mIbtnMostShop.setContentDescription("mostShop");
        mIbtnMostKeyword.setContentDescription("mostKeyword");
        mVCover.setContentDescription("cover");
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("searchType")) {
                    afterClickSearchType();
                } else if (v.getContentDescription().equals("back")) {
                    afterClickBack();
                } else if (v.getContentDescription().equals("avatar")) {
                    afterClickAvatar();
                } else if (v.getContentDescription().equals("mostRecent")) {
                    afterClickMostRecent();
                } else if (v.getContentDescription().equals("mostFavor")) {
                    afterClickMostFavor();
                } else if (v.getContentDescription().equals("mostPoint")) {
                    afterClickMostPoint();
                } else if (v.getContentDescription().equals("mostCategory")) {
                    afterClickMostCategory();
                } else if (v.getContentDescription().equals("mostShop")) {
                    afterClickMostShop();
                } else if (v.getContentDescription().equals("mostKeyword")) {
                    afterClickMostKeyword();
                } else if (v.getContentDescription().equals("cover")) {
                    afterClickCover();
                }
            }
        };

        mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mIbtnChangeSearchType.getTag().equals(TAG_POINT)) {
                    mTvFilter.setText(mPointList.get(position));
                    if (position == 0) {
                        mOrderType = "3";
                    } else if (position == 1) {
                        mOrderType = "4";
                    }

                    requestGetExchangeItems();
                }

                if (mIbtnChangeSearchType.getTag().equals(TAG_SHOP)) {
                    mTvFilter.setText(mShopList.get(position).getName());
                    mShopId = mShopList.get(position).getId();
                    mOrderType = null;
                    requestGetExchangeItems();
                }

                if (mIbtnChangeSearchType.getTag().equals(TAG_CATEGORY)) {
                    mTvFilter.setText(mCategoryList.get(position).getName());
                    mCategoryId = mCategoryList.get(position).getId();
                    mOrderType = null;
                    requestGetExchangeItems();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    @Override
    protected void setListenerForViews() {
        mIbtnChangeSearchType.setOnClickListener(mOnClickListener);
        mImgAvatar.setOnClickListener(mOnClickListener);
        mIbtnMostRecent.setOnClickListener(mOnClickListener);
        mIbtnMostFavor.setOnClickListener(mOnClickListener);
        mIbtnMostPoint.setOnClickListener(mOnClickListener);
        mIbtnMostCategory.setOnClickListener(mOnClickListener);
        mIbtnMostShop.setOnClickListener(mOnClickListener);
        mIbtnMostKeyword.setOnClickListener(mOnClickListener);
        mIbtnBack.setOnClickListener(mOnClickListener);
        mVCover.setOnClickListener(mOnClickListener);

        mSpnFilter.setOnItemSelectedListener(mOnItemSelectedListener);

        mEtSearch.setOnEditorActionListener(mOnEditorActionListener);
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

    public void updateUserPoint() {
        if (SCUserObject.getInstance().getIsGuest().equals("false")) {
            mTvUserPoint.setText(SCUserObject.getInstance().getCampusPoint() + getResources().getString(R.string.common_point));
        }
    }

    private void changeLayoutAfterChooseSearchType(String tag) {
        slideUp(mLlSlideMenu, mIbtnChangeSearchType);
        fadeOut(mVCover);

        mIbtnChangeSearchType.setSelected(false);
        mIbtnChangeSearchType.setTag(tag);

        initSpinner(tag);

        if (tag.equals(TAG_RECENT)) {
            mRlFilter.setVisibility(View.GONE);
            mTvFilter.setVisibility(View.VISIBLE);
            mSpnFilter.setVisibility(View.VISIBLE);
            mEtSearch.setText("");
            mOrderType = "1";

            mCategoryId = null;
            mShopId = null;

            requestGetExchangeItems();
        } else if (tag.equals(TAG_FAVOR)) {
            mRlFilter.setVisibility(View.GONE);
            mTvFilter.setVisibility(View.VISIBLE);
            mSpnFilter.setVisibility(View.VISIBLE);
            mEtSearch.setText("");
            mOrderType = "2";

            mCategoryId = null;
            mShopId = null;

            requestGetExchangeItems();
        } else if (tag.equals(TAG_POINT)) {
            mRlFilter.setVisibility(View.VISIBLE);
            mTvFilter.setVisibility(View.VISIBLE);
            mSpnFilter.setVisibility(View.VISIBLE);
            mTvFilter.setText(getResources().getString(R.string.ecmain_search_type_point_label));
            mEtSearch.setText("");
            mOrderType = "3";

            mCategoryId = null;
            mShopId = null;
        } else if (tag.equals(TAG_CATEGORY)) {
            mRlFilter.setVisibility(View.VISIBLE);
            mTvFilter.setVisibility(View.VISIBLE);
            mSpnFilter.setVisibility(View.VISIBLE);
            mTvFilter.setText(getResources().getString(R.string.ecmain_search_type_category_label));
            mEtSearch.setText("");

            mCategoryId = null;
            mShopId = null;
        } else if (tag.equals(TAG_SHOP)) {
            mRlFilter.setVisibility(View.VISIBLE);
            mTvFilter.setVisibility(View.VISIBLE);
            mSpnFilter.setVisibility(View.VISIBLE);
            mTvFilter.setText(getResources().getString(R.string.ecmain_search_type_shop_label));
            mEtSearch.setText("");

            mCategoryId = null;
            mShopId = null;
        } else if (tag.equals(TAG_KEYWORD)) {
            mRlFilter.setVisibility(View.VISIBLE);
            mTvFilter.setVisibility(View.GONE);
            mSpnFilter.setVisibility(View.GONE);
            mEtSearch.setText("");

            mCategoryId = null;
            mShopId = null;
            requestGetExchangeItems();
        }

        changeSearchTypeLayout();
    }

    private void changeSearchTypeLayout() {
        int drawable = 0;

        if (mIbtnChangeSearchType.getTag().equals(TAG_RECENT)) {
            drawable = R.drawable.btn_most_recent_white_selector;
        } else if (mIbtnChangeSearchType.getTag().equals(TAG_FAVOR)) {
            drawable = R.drawable.btn_most_favor_white_selector;
        } else if (mIbtnChangeSearchType.getTag().equals(TAG_POINT)) {
            drawable = R.drawable.btn_most_point_white_selector;
        } else if (mIbtnChangeSearchType.getTag().equals(TAG_CATEGORY)) {
            drawable = R.drawable.btn_most_category_white_selector;
        } else if (mIbtnChangeSearchType.getTag().equals(TAG_SHOP)) {
            drawable = R.drawable.btn_most_shop_white_selector;
        } else if (mIbtnChangeSearchType.getTag().equals(TAG_KEYWORD)) {
            drawable = R.drawable.btn_most_keyword_white_selector;
        }

        mIbtnChangeSearchType.setImageResource(drawable);
    }

    private void afterClickBack() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }


    public void requestRecommendExchangeItem() {
        String appId = SCUserObject.getInstance().getAppId();
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_APP_ID, appId);

        SCRequestAsyncTask SCRequestAsyncTask = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_GET_RECOMMEND_ITEM_DETAILS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG, result);

                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.getString("success").equals("true")) {
                        JSONObject jsonObject = jObj.getJSONObject("item");
                        String image_URL = jsonObject.getString("image");
                        String is_fav = jsonObject.getString("is_favorite");

                        String point = jsonObject.getString("point");
                        String product = jsonObject.getString("name");
                        String shopname = jsonObject.getString("shop_name");

                        show_thanks_dialog(image_URL, is_fav, point, product, shopname);
                    } else {
                        Toast.makeText(ECMainActivity.this, jObj.getString("error"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

        SCRequestAsyncTask.execute();
    }

    private void afterClickSearchType() {
        changeSearchTypeLayout();

        if (mIbtnChangeSearchType.isSelected()) {
            mIbtnChangeSearchType.setSelected(false);
            slideUp(mLlSlideMenu, mIbtnChangeSearchType);
            fadeOut(mVCover);
        } else {
            mIbtnChangeSearchType.setSelected(true);
            slideDown(mLlSlideMenu, mIbtnChangeSearchType);
            fadeIn(mVCover);
        }
    }

    private void afterClickCover() {
        afterClickSearchType();
    }

    private void afterClickAvatar() {
        Intent i = new Intent(this, ECPointManager.class);
        startActivityForResult(i, SCConstants.CODE_SHOW_POINT_MANAGER);
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void afterClickMostRecent() {
        mTvTitle.setText(getResources().getText(R.string.ecmain_tab_recent));
        changeLayoutAfterChooseSearchType(TAG_RECENT);
    }

    private void afterClickMostFavor() {
        mTvTitle.setText(getResources().getText(R.string.ecmain_tab_favor));
        changeLayoutAfterChooseSearchType(TAG_FAVOR);
    }

    private void afterClickMostPoint() {
        mTvTitle.setText(getResources().getText(R.string.ecmain_tab_point));
        changeLayoutAfterChooseSearchType(TAG_POINT);
    }

    private void afterClickMostCategory() {
        mTvTitle.setText(getResources().getText(R.string.ecmain_tab_category));
        changeLayoutAfterChooseSearchType(TAG_CATEGORY);
    }

    private void afterClickMostShop() {
        mTvTitle.setText(getResources().getText(R.string.ecmain_tab_shop));
        changeLayoutAfterChooseSearchType(TAG_SHOP);
    }

    private void afterClickMostKeyword() {
        mTvTitle.setText(getResources().getText(R.string.ecmain_tab_keyword));
        changeLayoutAfterChooseSearchType(TAG_KEYWORD);
    }

    private void showInfo() {
        if (SCUserObject.getInstance().getIconInstance() == null) {
            ImageLoader.getInstance().displayImage(SCUserObject.getInstance().getIcon(), mImgAvatar, SCGlobalUtils.sOptForUserIcon);
        } else {
            mImgAvatar.setImageBitmap(SCUserObject.getInstance().getIconInstance());
        }

        updateUserPoint();
    }

    private void initListData() {
        mListData = new ArrayList<Object>();
        mProductAdapter = new ECProductAdapter(mContext, mListData);
        mGvProduct.setHorizontalSpacing(SCMultipleScreen.getValueAfterResize(20));
        mGvProduct.setVerticalSpacing(SCMultipleScreen.getValueAfterResize(20));
        mGvProduct.setAdapter(mProductAdapter);
    }

    private void initSpinner(String tag) {
        ArrayAdapter<String> spinnerAdapter = null;
        if (tag.equals(TAG_POINT)) {
            mPointList = Arrays.asList(getResources().getStringArray(R.array.point_array));

            spinnerAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, mPointList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnFilter.setAdapter(spinnerAdapter);
        } else if (tag.equals(TAG_SHOP)) {
            ArrayList<String> shopsName = new ArrayList<String>();
            for (int i = 0; i < mShopList.size(); i++) {
                shopsName.add(mShopList.get(i).getName());
            }

            spinnerAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, shopsName);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnFilter.setAdapter(spinnerAdapter);
        } else if (tag.equals(TAG_CATEGORY)) {
            ArrayList<String> categoriesName = new ArrayList<String>();
            for (int i = 0; i < mCategoryList.size(); i++) {
                categoriesName.add(mCategoryList.get(i).getName());
            }

            spinnerAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, categoriesName);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpnFilter.setAdapter(spinnerAdapter);
        }
    }

    private void fadeIn(final View v) {
        v.setVisibility(View.INVISIBLE);

        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_fade_in);
        anim.setDuration(200);
        anim.setFillAfter(false);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.clearAnimation();
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        v.startAnimation(anim);
    }

    private void fadeOut(final View v) {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_fade_out);
        anim.setDuration(200);
        anim.setFillAfter(false);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.clearAnimation();
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        v.startAnimation(anim);
    }

    private void slideDown(final View v, final View touchedV) {
        v.setVisibility(View.INVISIBLE);

        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_show_down);
        anim.setDuration(200);
        anim.setFillAfter(false);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                touchedV.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                touchedV.setEnabled(true);
                v.clearAnimation();
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        v.startAnimation(anim);
    }

    private void slideUp(final View v, final View touchedV) {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_show_up);
        anim.setDuration(200);
        anim.setFillAfter(false);

        v.setVisibility(View.VISIBLE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                touchedV.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                touchedV.setEnabled(true);
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        v.startAnimation(anim);
    }

    private void requestGetShops() {
        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_SHOPS, new HashMap<String, Object>(), new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {

                Log.e(TAG_LOG + "GET_SHOPS", result);
                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_SHOPS, result);

                if (returnHash.containsKey(SCConstants.TAG_DATA)) {
                    mShopList.addAll((ArrayList<ECShopObject>) returnHash.get(SCConstants.TAG_DATA));
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

    private void requestGetCategories() {
        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_CATEGORIES, new HashMap<String, Object>(), new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {

                Log.e(TAG_LOG + "CATEGORY", result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_CATEGORIES, result);

                if (returnHash.containsKey(SCConstants.TAG_DATA)) {
                    mCategoryList.addAll((ArrayList<ECCategoryObject>) returnHash.get(SCConstants.TAG_DATA));
                }

                requestGetExchangeItems();
            }

            @Override
            public void progress() {
            }

            @Override
            public void onInterrupted(Exception e) {
                requestGetExchangeItems();
            }

            @Override
            public void onException(Exception e) {
                requestGetExchangeItems();
            }
        });

        mSCRequestAsyncTask.execute();
    }

    private void requestGetExchangeItems() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);
        parameter.put(SCConstants.PARAM_KEYWORD, mEtSearch.getText().toString());
        parameter.put(SCConstants.PARAM_ORDER_TYPE, (mOrderType == null) ? "" : mOrderType);
        parameter.put(SCConstants.PARAM_SHOP_ID, (mShopId == null) ? "" : mShopId);
        parameter.put(SCConstants.PARAM_CATEGORY_ID, (mCategoryId == null) ? "" : mCategoryId);

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_EXCHANGE_ITEMS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG + "EXCHANGE", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("discount_rate")) {
                        String discount_rate = jsonObject.getString("discount_rate");
                        if (discount_rate != null) {
                            if (!discount_rate.equals("null")) {
                                SCGlobalUtils.discount_rate = Integer.parseInt(discount_rate);
                            } else {
                                SCGlobalUtils.discount_rate = 0;
                            }

                        } else {
                            SCGlobalUtils.discount_rate = 0;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_EXCHANGE_ITEMS, result);

                if (returnHash.containsKey(SCConstants.TAG_ITEMS)) {
                    mListData.clear();

                    mListData.addAll((ArrayList<ECProductObject>) returnHash.get(SCConstants.TAG_ITEMS));

                    // set category id by mapping category name
                    for (int i = 0; i < mListData.size(); i++) {
                        ECProductObject product = (ECProductObject) mListData.get(i);
                        for (ECCategoryObject category : mCategoryList) {
                            if (product.getCategory().equals(category.getName())) {
                                product.setCategoryId(category.getId());
                            }
                        }
                    }

                    mProductAdapter.notifyDataSetChanged((String) mIbtnChangeSearchType.getTag());
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

    public void showLoginDialog(final int codeType) {
        SCGlobalUtils.showLoginDialog(mContext, new SCDialogCallback() {
            @Override
            public void onAction1() {
                loginWithFacebook(codeType);
            }

            @Override
            public void onAction2() {
                loginWithTwitter(codeType);
            }

            @Override
            public void onAction3() {
                loginWithEmail(codeType);
            }

            @Override
            public void onAction4() {
            }
        });
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
                        || (userObj.getMajorId().equals("0") || userObj.getMajorId().equals("null"))
                        || (userObj.getEnrollmentYear().equals("0") || userObj.getEnrollmentYear().equals("null"))) {

                    Intent intent = new Intent(mContext, SCEditInfoOneActivity.class);
                    intent.putExtra(SCUserObject.class.toString(), userObj);
                    intent.putExtra(Integer.class.toString(), codeType);
                    startActivityForResult(intent, codeType);
                    overridePendingTransition(R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_left);

                } else {
                    showInfo();
                    if (codeType == SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM) {
                        mProductAdapter.requestAddFavorite();
                    }

                    if (codeType == SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM) {
                        mProductAdapter.afterClickExchangePoint(mProductAdapter.getActivePosition());
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
