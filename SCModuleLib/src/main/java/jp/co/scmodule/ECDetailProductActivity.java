package jp.co.scmodule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.interfaces.SCDialogCallback;
import jp.co.scmodule.interfaces.SCSNSLoginCallback;
import jp.co.scmodule.objects.ECProductObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;


public class ECDetailProductActivity extends SCMyActivity implements View.OnClickListener {
    @Override
    protected void findViewById() {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void setListenerForViews() {

    }

    @Override
    protected void resizeScreen() {

    }

    @Override
    protected void initGlobalUtils() {

    }

    private final String TAG_LOG = "ECDetailProductActivity";
    //ID for Views:
    private final String EC_BTN_TRADE = "ec_btn_trade";
    private final String EC_BTN_BACK = "ec_btn_back";
    private final String EC_IMG_PRODUCT_IMAGE = "ec_img_product_image";
    private final String EC_TV_SHOP_NAME = "ec_tv_shop_name";
    private final String EC_TV_PRODUCT_NAME = "ec_tv_product_name";
    private final String EC_TV_DETAIL_PRODUCT = "ec_tv_detail_product";
    private final String EC_BTN_GO_TO_SHOP = "ec_btn_go_to_shop";
    private final String EC_IMG_DETAIL_PRODUCT_LIKE = "ec_img_detail_product_like";
    private final String EC_BTN_OFFICIAL_URL = "ec_btn_official_url";

    //Views:
    private Button btnTrade;
    private ImageView btnBack;
    private ImageView imgProductImage;
    private TextView tvShopName;
    private TextView tvProductName;
    private TextView tvDescriptionProduct;
    private Button btnLike;
    private TextView tvPoint;
    private Button btnGoToShop;
    private Button btnOfficialUrl;
    private TextView tvUserPoint;
    private LinearLayout llMain;
    private View DialogView;
    Dialog mDialog_tut1 = null;
    private TextView tv_special_discount = null;

    private SCRequestAsyncTask mSCRequestAsyncTask;

    //Variable
    private SCUserObject mUserObject;

    private ECProductObject mProductObj;

    private int mPosition;

    private TwitterAuthClient mTwitterClient = null;

    private CallbackManager mFacebookCallbackManager = null;

    private WeakReference<Activity> mActivityRef = null;

    private String mToken = null;
    private String mSecret = null;

    private Context mContext = null;
    private Activity mActivity = null;

    private String mId = null;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ECProductObject.class.toString(), mProductObj);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SCGlobalUtils.sActivityArr.remove(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM) {
                if (SCUserObject.getInstance().getIsGuest().equals("false")) {
                    tvUserPoint.setText(SCUserObject.getInstance().getCampusPoint() + getResources().getString(R.string.common_point));
                }

                onClick(btnTrade);
            }

            if (requestCode == SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM) {
                if (SCUserObject.getInstance().getIsGuest().equals("false")) {
                    tvUserPoint.setText(SCUserObject.getInstance().getCampusPoint() + getResources().getString(R.string.common_point));
                }
                requestAddFavorite();
            }
        }

        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        mTwitterClient.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        new SCGlobalUtils(this);
        super.onRestart();
    }


    public void show_thanks_dialog() {

        View v = DialogView;

        ImageView image_header = (ImageView) v.findViewById(R.id.image_header);
        ImageView image = (ImageView) v.findViewById(R.id.image);
        ImageView close = (ImageView) v.findViewById(R.id.btn_close_copy_code);
        Button campus = (Button) v.findViewById(R.id.btn_goto_campus);
        campus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecdetail_product);

        SCGlobalUtils.sActivityArr.add(this);

        mContext = this;
        mActivity = this;

        new SCGlobalUtils(this);

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

        //Init Views:
        llMain = (LinearLayout) findViewById(R.id.ec_product_detail_ll_main);
        btnTrade = (Button) findViewById(R.id.ec_btn_trade);
        btnTrade.setContentDescription(EC_BTN_TRADE);
        btnBack = (ImageView) findViewById(R.id.ec_btn_back);
        btnBack.setContentDescription(EC_BTN_BACK);
        imgProductImage = (ImageView) findViewById(R.id.ec_img_detail_product_image);
        tvShopName = (TextView) findViewById(R.id.ec_tv_shop_name);
        tvProductName = (TextView) findViewById(R.id.ec_tv_product_name);
        tvDescriptionProduct = (TextView) findViewById(R.id.ec_tv_description_product);
        btnLike = (Button) findViewById(R.id.ec_btn_detail_product_like);
        btnLike.setContentDescription(EC_IMG_DETAIL_PRODUCT_LIKE);
        tvPoint = (TextView) findViewById(R.id.ec_tv_point);
        tvUserPoint = (TextView) findViewById(R.id.ec_tv_user_point);
        btnGoToShop = (Button) findViewById(R.id.ec_btn_go_to_shop);
        tv_special_discount = (TextView) findViewById(R.id.tv_discount_badge);

        btnGoToShop.setContentDescription(EC_BTN_GO_TO_SHOP);
        btnOfficialUrl = (Button) findViewById(R.id.ec_btn_official_url);
        btnOfficialUrl.setContentDescription(EC_BTN_OFFICIAL_URL);

        DialogView = this.getLayoutInflater().inflate(R.layout.dialog_thanks_page, null);
        mDialog_tut1 = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);

        //Variables:
        mUserObject = SCUserObject.getInstance();

        //Action:
        btnTrade.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnGoToShop.setOnClickListener(this);
        btnLike.setOnClickListener(this);
        btnOfficialUrl.setOnClickListener(this);

        mPosition = getIntent().getIntExtra(Integer.class.toString(), -1);

        tvShopName.setMaxWidth(SCMultipleScreen.getValueAfterResize(639));
        tvProductName.setMaxWidth(SCMultipleScreen.getValueAfterResize(639));

        mProductObj = getIntent().getParcelableExtra(ECProductObject.class.toString());
        mId = getIntent().getStringExtra(String.class.toString());

        // check if user show exchange from outside scheme
        if (mId != null) {
            requestGetExchangeItemDetail(mId);
        } else {
            showInfo();
        }

        //resize
        new SCMultipleScreen(this);
        SCMultipleScreen.resizeAllView(this);
    }

    private void showInfo() {
        llMain.setVisibility(View.VISIBLE);
        ImageLoader.getInstance().displayImage(mProductObj.getImage(), imgProductImage, SCGlobalUtils.sOptForImgLoader);
        if (SCUserObject.getInstance().getIsGuest().equals("false")) {
            tvUserPoint.setText(SCUserObject.getInstance().getCampusPoint() + getResources().getString(R.string.common_point));
        }

        if (SCGlobalUtils.discount_rate == 0) {
            tv_special_discount.setVisibility(View.GONE);
            tvPoint.setText(mProductObj.getPoint() + " " + getResources().getString(R.string.common_point));

        } else {
            tv_special_discount.setVisibility(View.VISIBLE);
            tv_special_discount.setText(SCGlobalUtils.discount_rate + "%\nOFF");

            int discount_point = (Integer.parseInt(mProductObj.getPoint()) * SCGlobalUtils.discount_rate) / 100;
            int updated_point = Integer.parseInt(mProductObj.getPoint()) - discount_point;
            tvPoint.setText(updated_point
                    + " " + mContext.getResources().getString(R.string.common_point));
        }
        tvShopName.setMaxWidth(SCMultipleScreen.getValueAfterResize(387));
        tvShopName.setText(mProductObj.getShop());
        tvProductName.setMaxWidth(SCMultipleScreen.getValueAfterResize(387));
        tvProductName.setText(mProductObj.getName());
        tvDescriptionProduct.setText(mProductObj.getDescription());
        if (this.mProductObj.getFavorite().equals("true")) {
            btnLike.setSelected(true);
            // btnLike.setTextColor(mContext.getResources().getColor(R.color.common_ec_red));
            // btnLike.setText(mContext.getResources().getString(R.string.common_unlike_product));
        } else {
            btnLike.setSelected(false);
            // btnLike.setTextColor(mContext.getResources().getColor(android.R.color.white));
            // btnLike.setText(mContext.getResources().getString(R.string.common_like_product));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getContentDescription().equals(EC_BTN_TRADE)) {
            if (mUserObject.getIsGuest().equals("true")) {
                SCGlobalUtils.showLoginDialog(this, new SCDialogCallback() {
                    @Override
                    public void onAction1() {
                        loginWithFacebook(SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM);
                    }

                    @Override
                    public void onAction2() {
                        loginWithTwitter(SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM);
                    }

                    @Override
                    public void onAction3() {
                        loginWithEmail(SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM);
                    }

                    @Override
                    public void onAction4() {

                    }
                });
            } else {
                int userPoint = Integer.parseInt(SCUserObject.getInstance().getCampusPoint());
                int productPoint = Integer.parseInt(mProductObj.getPoint());
                if (userPoint < productPoint) {
                    showDialogNotEnoughPoint();
                    return;
                }

                if (!mProductObj.getCategoryId().equals("2")) {
                    if ((SCUserObject.getInstance().getNickname().equals("")
                            || SCUserObject.getInstance().getPostCode().equals("")
                            || SCUserObject.getInstance().getAddress().equals("")
                            || SCUserObject.getInstance().getPhoneNumber().equals(""))) {
                        showDialogNotEnoughInfoField();
                    } else if (!SCUserObject.getInstance().getNickname().equals("")
                            && !SCUserObject.getInstance().getPostCode().equals("")
                            && !SCUserObject.getInstance().getAddress().equals("")
                            && !SCUserObject.getInstance().getPhoneNumber().equals("")) {
                        showDialogEnoughInfoField();
                    }
                } else {
                    showDialogConfirmExchangeStage1();
                }
            }
        } else if (view.getContentDescription().equals(EC_BTN_BACK)) {
            Intent intent = new Intent();
            intent.putExtra(ECProductObject.class.toString(), mProductObj);
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_right);
        } else if (view.getContentDescription().equals(EC_BTN_GO_TO_SHOP)) {
            Intent intent = new Intent(this, ECDetailShopActivity.class);
            intent.putExtra(String.class.toString(), mProductObj.getShopId());
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
        } else if (view.getContentDescription().equals(EC_IMG_DETAIL_PRODUCT_LIKE)) {
            if (mUserObject.getIsGuest().equals("true")) {
                SCGlobalUtils.showLoginDialog(this, new SCDialogCallback() {
                    @Override
                    public void onAction1() {
                        loginWithFacebook(SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM);
                    }

                    @Override
                    public void onAction2() {
                        loginWithTwitter(SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM);
                    }

                    @Override
                    public void onAction3() {
                        loginWithEmail(SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM);
                    }

                    @Override
                    public void onAction4() {

                    }
                });
            } else {
                requestAddFavorite();
            }
        } else if (view.getContentDescription().equals(EC_BTN_OFFICIAL_URL)) {
            if (mProductObj != null && URLUtil.isValidUrl(mProductObj.getOfficialUrl())) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mProductObj.getOfficialUrl()));
                mActivity.startActivity(browserIntent);
            }
        }
    }

    private void showDialogConfirmExchangeStage1() {
        String title = mContext.getResources().getString(R.string.dialog_exchange_stage_1_title);
        String body = mContext.getResources().getString(R.string.dialog_body_exchange_point_stage_1);
        body = String.format(
                body,
                SCUserObject.getInstance().getNickname(),
                SCUserObject.getInstance().getEmail(),
                mProductObj.getName(),
                mProductObj.getPoint(),
                SCUserObject.getInstance().getCampusPoint(),
                String.valueOf(Integer.parseInt(SCUserObject.getInstance().getCampusPoint()) - Integer.parseInt(mProductObj.getPoint())));
        String action1 = mContext.getResources().getString(R.string.common_ok_label);
        String action2 = mContext.getResources().getString(R.string.common_cancel_label);
        String action3 = mContext.getResources().getString(R.string.common_edit_label);
        SCGlobalUtils.showThreeActionDialog(mContext, title, body, action1, action2, action3, new SCDialogCallback() {
            @Override
            public void onAction1() {
                showDialogConfirmExchangeStage2();
            }

            @Override
            public void onAction2() {

            }

            @Override
            public void onAction3() {
                Intent intent = new Intent(mContext, SCProfileInfoActivity.class);
                intent.putExtra(SCUserObject.class.toString(), SCUserObject.getInstance());
                ((Activity) mContext).startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
            }

            @Override
            public void onAction4() {

            }
        });
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
                goto_point_manager();
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

    private void goto_point_manager() {
        Intent i = new Intent(this, ECPointManager.class);
        startActivityForResult(i, SCConstants.CODE_SHOW_POINT_MANAGER);
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
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
                        Toast.makeText(ECDetailProductActivity.this, jObj.getString("error"), Toast.LENGTH_LONG).show();
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

    private void showDialogConfirmExchangeStage2() {
        requestPayExchangeItem();
        requestRecommendExchangeItem();
//        String title = mContext.getResources().getString(R.string.dialog_exchange_stage_2_title);
//        String body = mContext.getResources().getString(R.string.dialog_body_exchange_point_stage_2);
//        SCGlobalUtils.showInfoDialog(mContext, title, body, null, new SCDialogCallback() {
//            @Override
//            public void onAction1() {
//                requestPayExchangeItem();
//            }
//
//            @Override
//            public void onAction2() {
//
//            }
//
//            @Override
//            public void onAction3() {
//
//            }
//
//            @Override
//            public void onAction4() {
//
//            }
//        });
    }

    private void showDialogNotEnoughPoint() {
        String title = mContext.getResources().getString(R.string.dialog_error_title);
        String body = mContext.getResources().getString(R.string.dialog_pay_exchange_not_enough_point);
        SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
    }

    private void showDialogEnoughInfoField() {
        String title = mContext.getResources().getString(R.string.dialog_confirm_title);
        int color = mContext.getResources().getColor(R.color.red);
        String body = mContext.getResources().getString(R.string.dialog_confirm_info_before_exchange, color);
        String action1 = mContext.getResources().getString(R.string.common_ok_label);
        String action2 = mContext.getResources().getString(R.string.common_cancel_label);
        String action3 = mContext.getResources().getString(R.string.common_edit_label);
        SCGlobalUtils.showThreeActionDialog(mContext, title, body, action1, action2, action3, new SCDialogCallback() {
            @Override
            public void onAction1() {
                showDialogConfirmExchangeStage1();
            }

            @Override
            public void onAction2() {

            }

            @Override
            public void onAction3() {
                Intent intent = new Intent(mContext, SCProfileInfoActivity.class);
                intent.putExtra(SCUserObject.class.toString(), SCUserObject.getInstance());
                ((Activity) mContext).startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
            }

            @Override
            public void onAction4() {

            }
        });
    }

    private void showDialogNotEnoughInfoField() {
        String title = mContext.getResources().getString(R.string.dialog_confirm_title);
        int color = mContext.getResources().getColor(R.color.red);
        String body = mContext.getResources().getString(R.string.dialog_confirm_info_before_exchange, color);
        String action1 = mContext.getResources().getString(R.string.common_edit_label);
        String action2 = mContext.getResources().getString(R.string.common_cancel_label);
        SCGlobalUtils.showConfirmDialog(mContext, title, body, action1, action2, new SCDialogCallback() {
            @Override
            public void onAction1() {
                Intent intent = new Intent(mContext, SCProfileInfoActivity.class);
                intent.putExtra(SCUserObject.class.toString(), SCUserObject.getInstance());
                ((Activity) mContext).startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
            }

            @Override
            public void onAction2() {

            }

            @Override
            public void onAction3() {

            }

            @Override
            public void onAction4() {

            }
        });
    }

    private void requestAddFavorite() {
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
        parameter.put(SCConstants.PARAM_APP_EXCHANGE_ITEM_ID, mProductObj.getId());

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_ADD_FAVORITE_ITEM, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_ADD_FAVORITE_ITEM, result);

                if (returnHash.containsKey(SCConstants.TAG_FAVORITE)) {
                    String favorite = (String) returnHash.get(SCConstants.TAG_FAVORITE);

                    mProductObj.setFavorite(favorite);

                    if (mProductObj.getFavorite().equals("true")) {
                        btnLike.setSelected(true);
                        //  btnLike.setTextColor(mContext.getResources().getColor(R.color.common_ec_red));
                        //  btnLike.setText(mContext.getResources().getString(R.string.common_unlike_product));
                    } else {
                        btnLike.setSelected(false);
                        // btnLike.setTextColor(mContext.getResources().getColor(android.R.color.white));
                        //  btnLike.setText(mContext.getResources().getString(R.string.common_like_product));
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

    private void requestPayExchangeItem() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();
        String applicationId = "";
        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
            applicationId = SCConstants.APP_ID_TADACOPY;
        } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
            applicationId = SCConstants.APP_ID_CANPASS;
        }
        else if (getPackageName().equals(SCConstants.PACKAGE_TORETAN)) {
            applicationId = SCConstants.APPLICATION_ID_TORETAN;
        }
        //String appId = "188TZ";

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);
        parameter.put(SCConstants.PARAM_APP_EXCHANGE_ITEM_ID, mProductObj.getId());
        parameter.put(SCConstants.PARAM_APPLICATION_ID, applicationId);

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_PAY_EXCHANGE_ITEM, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_PAY_EXCHANGE_ITEM, result);

                if (returnHash.containsKey(SCConstants.TAG_AFTER_POINT)) {
                    String afterPoint = (String) returnHash.get(SCConstants.TAG_AFTER_POINT);

                    // update point
                    SCUserObject.getInstance().setCampusPoint(afterPoint);
                    if (SCUserObject.getInstance().getIsGuest().equals("false")) {
                        tvUserPoint.setText(SCUserObject.getInstance().getCampusPoint() + getResources().getString(R.string.common_point));
                    }
                }

                if (returnHash.containsKey(SCConstants.TAG_ERROR_CODE)) {
                    int errCode = Integer.parseInt((String) returnHash.get(SCConstants.TAG_ERROR_CODE));
                    String title = getResources().getString(R.string.dialog_error_title);
                    String body = null;
                    if (errCode == ECProductObject.ERROR_CONNECTION) {
                        body = getResources().getString(R.string.dialog_pay_exchange_connection_fail);
                    } else if (errCode == ECProductObject.ERROR_LOGIN_FAIL) {
                        body = getResources().getString(R.string.dialog_pay_exchange_login_fail);
                    } else if (errCode == ECProductObject.ERROR_NOT_ENOUGH_POINT) {
                        body = getResources().getString(R.string.dialog_pay_exchange_not_enough_point);
                    }

                    if (body != null) {
                        SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
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
                    if (SCUserObject.getInstance().getIsGuest().equals("false")) {
                        tvUserPoint.setText(SCUserObject.getInstance().getCampusPoint() + getResources().getString(R.string.common_point));
                    }

                    if (codeType == SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM) {
                        requestAddFavorite();
                    }

                    if (codeType == SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM) {
                        onClick(btnTrade);
                    }
                }
            }

            @Override
            public void onLoginFail() {
                SCGlobalUtils.dismissLoadingProgress();
            }
        });
    }

    private void requestGetExchangeItemDetail(String id) {
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
        parameter.put(SCConstants.PARAM_APP_EXCHANGE_ITEM_ID, id);

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_EXCHANGE_ITEM_DETAIL, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_EXCHANGE_ITEM_DETAIL, result);

                if (returnHash.containsKey(SCConstants.TAG_ITEM)) {
                    mProductObj = (ECProductObject) returnHash.get(SCConstants.TAG_ITEM);

                    showInfo();
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
}
