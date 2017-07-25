package jp.co.scmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
import jp.co.scmodule.adapters.ECPointProductAdapter;
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


public class ECDetailHistoryProductActivity extends SCMyActivity implements View.OnClickListener {
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

    private final String TAG_LOG = "ECDetailHistoryExchange";
    //ID for Views:
    private final String EC_BTN_TRADE = "ec_btn_trade";
    private final String EC_BTN_BACK = "ec_btn_back";
    private final String EC_BTN_COPY_CODE = "ec_btn_copy";
    private final String EC_IMG_PRODUCT_IMAGE = "ec_img_product_image";
    private final String EC_TV_SHOP_NAME = "ec_tv_shop_name";
    private final String EC_TV_PRODUCT_NAME = "ec_tv_product_name";
    private final String EC_TV_DETAIL_PRODUCT = "ec_tv_detail_product";
    private final String EC_BTN_GO_TO_SHOP = "ec_btn_go_to_shop";
    private final String EC_IMG_DETAIL_PRODUCT_LIKE = "ec_img_detail_product_like";
    private final String EC_BTN_OFFICIAL_URL = "ec_btn_official_url";

    //Views:
    private ImageView btnBack;
    private ImageView imgProductImage;
    private TextView tvShopName;
    private TextView tvProductName;
    private Button btnGoToShop;
    private Button btnOfficialUrl;
    private TextView tvExchangeDate;
    private TextView tvShipDate;
    private TextView tvExpiredDate;
    private TextView tvProductCode;

    private SCRequestAsyncTask mSCRequestAsyncTask;

    //Variable
    private SCUserObject mUserObject;

    private ECProductObject mProductObj;

    private int mPosition;

    private Context mContext = null;
    private Activity mActivity = null;

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
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        new SCGlobalUtils(this);
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecdetail_history_product);

        SCGlobalUtils.sActivityArr.add(this);

        mContext = this;

        new SCGlobalUtils(this);

        //Init Views:
        btnBack = (ImageView) findViewById(R.id.ec_btn_back);
        btnBack.setContentDescription(EC_BTN_BACK);
        imgProductImage = (ImageView) findViewById(R.id.ec_img_detail_product_image);
        tvShopName = (TextView) findViewById(R.id.ec_tv_shop_name);
        tvProductName = (TextView) findViewById(R.id.ec_tv_product_name);
        btnGoToShop = (Button) findViewById(R.id.ec_btn_go_to_shop);
        btnGoToShop.setContentDescription(EC_BTN_GO_TO_SHOP);
        btnOfficialUrl = (Button) findViewById(R.id.ec_btn_official_url);
        btnOfficialUrl.setContentDescription(EC_BTN_OFFICIAL_URL);

        tvExchangeDate = (TextView) findViewById(R.id.ec_tv_exchange_date);
        tvShipDate = (TextView) findViewById(R.id.ec_tv_ship_date);
        tvExpiredDate = (TextView) findViewById(R.id.ec_tv_expired_date);
        tvProductCode = (TextView) findViewById(R.id.ec_tv_product_code);
        tvProductCode.setContentDescription(EC_BTN_COPY_CODE);
        //Variables:
        mUserObject = SCUserObject.getInstance();

        //Action:
        btnBack.setOnClickListener(this);
        btnGoToShop.setOnClickListener(this);
        btnOfficialUrl.setOnClickListener(this);
        tvProductCode.setOnClickListener(this);

        mPosition = getIntent().getIntExtra(Integer.class.toString(), -1);

        mProductObj = getIntent().getParcelableExtra(ECProductObject.class.toString());

        tvShopName.setMaxWidth(SCMultipleScreen.getValueAfterResize(639));
        tvProductName.setMaxWidth(SCMultipleScreen.getValueAfterResize(639));

        requestGetExchangeLogDetail();

        //resize
        new SCMultipleScreen(this);
        SCMultipleScreen.resizeAllView(this);
    }

    private void showInfo(ECProductObject product) {
        ImageLoader.getInstance().displayImage(product.getImage(), imgProductImage, SCGlobalUtils.sOptForImgLoader);
        tvShopName.setText(product.getShop());
        tvProductName.setText(product.getName());

        tvExchangeDate.setText(formatDate(product.getExchangeDate()));
        tvShipDate.setText(formatDate(product.getSendDate()));
        if(product.getLimitDate().equals("") || product.getLimitDate().equals("null")) {
            tvExpiredDate.setText(getResources().getString(R.string.ec_point_unlimited));
        } else {
            tvExpiredDate.setText(formatDate(product.getLimitDate()));
        }

        tvProductCode.setText(product.getCode());
    }

    @Override
    public void onClick(View view) {
        if (view.getContentDescription().equals(EC_BTN_BACK)) {
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
        } else if (view.getContentDescription().equals(EC_BTN_OFFICIAL_URL)) {
            if(mProductObj == null || (mProductObj != null && mProductObj.getOfficialUrl().equals(""))) {
                return;
            }

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mProductObj.getOfficialUrl()));
            startActivity(browserIntent);
        }else if (view.getContentDescription().equals(EC_BTN_COPY_CODE)) {

            if (!tvProductCode.getText().toString().equals("発行中")) {
                String body = "コードをコピーしますか？";
                String title = mContext.getResources().getString(R.string.dialog_confirm_title);
                String action1 = "コピー";
                String action2 = "キャンセル";
                SCGlobalUtils.showConfirmDialog(mContext, title, body, action1, action2, new SCDialogCallback() {
                    @Override
                    public void onAction1() {
                        setClipboard(mContext, tvProductCode.getText().toString());
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
        }

    }

    // copy text to clipboard
    private void setClipboard(Context context,String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    private String formatDate(String date) {
        // remove second string from data string and replace "-" to "."
        date = (date.substring(0, date.length() - 9)).replaceAll("-", "/");
        return date;
    }

    private void requestGetExchangeLogDetail() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);

//        appId = "17AS";
//        date = "1443583727025";
//        key = "5adce5d328254fc11dcc3ce55e972a83";

        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);
        parameter.put(SCConstants.PARAM_APP_EXCHANGE_LOG_ID, mProductObj.getId());

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_EXCHANGE_LOG_DETAIL, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_EXCHANGE_LOG_DETAIL, result);

                if (returnHash.containsKey(SCConstants.TAG_APP_EXCHANGE_LOG)) {
                    mProductObj = (ECProductObject)returnHash.get(SCConstants.TAG_APP_EXCHANGE_LOG);
                    showInfo(mProductObj);
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
