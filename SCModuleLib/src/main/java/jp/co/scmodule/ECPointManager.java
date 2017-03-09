package jp.co.scmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.brickred.socialauth.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.scmodule.adapters.ECPointHistoryAdapter;
import jp.co.scmodule.adapters.ECPointProductAdapter;
import jp.co.scmodule.adapters.ECPointShopAdapter;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.ECChartItemObject;
import jp.co.scmodule.objects.ECProductObject;
import jp.co.scmodule.objects.ECShopObject;
import jp.co.scmodule.objects.SCCampusObject;
import jp.co.scmodule.objects.SCHistoryObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.APICallBack;
import jp.co.scmodule.utils.APIUtils;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;
import jp.co.scmodule.utils.SCUrlConstants;
import jp.co.scmodule.widgets.ECChart;
import jp.co.scmodule.widgets.ECExpandableGridView;
import jp.co.scmodule.widgets.SCCircleImageView;
import jp.co.scmodule.widgets.SCSingleLineTextView;


public class ECPointManager extends SCMyActivity implements View.OnClickListener {
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

    private static final String TAG_LOG = "ECPointManager";
    //Define
    private final String BTN_SHOW_MORE_HISTORY = "ShowMoreHistory";
    private final String BTN_SHOW_MORE_HISTORY_POINT = "ShowMoreHistoryPoint";
    private final String BTN_SHOW_MORE_FEATURE = "ShowMoreFeature";
    private final String BTN_SHOW_MORE_SHOP = "ShowMoreShop";
    private final String BTN_BACK = "Back";

    //    Views:
    private ECExpandableGridView grvHistory;
    private ECExpandableGridView grvFavorite;
    private ECExpandableGridView grvShop;
    private ECExpandableGridView pointHisory;
    private ImageView imgShowMoreHistory;
    private ImageView imgShowMoreHistoryPoint;
    private ImageView imgShowMoreFavorite;
    private ImageView imgShowMoreShop;
    private ImageView imgBack;
    private SCSingleLineTextView tvUserName;
    private TextView tvUserPoint;
    private SCCircleImageView imgAvatar;
    private ECChart ecChart;

    private LinearLayout header = null;
    private View bottom_line = null;

    //variable:
    private SCUserObject mUserObject;
    ArrayList<ECChartItemObject> listChartItem;
    private ArrayList<ECProductObject> mListHistory;
    private ArrayList<SCHistoryObject> mListPointHistory;
    private ArrayList<ECProductObject> mListFavorite;
    private ArrayList<ECShopObject> mListShop;
    private ECPointProductAdapter mHistoryAdapter;
    private ECPointHistoryAdapter mHistoryPointAdapter;
    private ECPointProductAdapter mFavoriteAdapter;
    private ECPointShopAdapter mShopAdapter;

    private SCRequestAsyncTask mSCRequestAsyncTask;

    private boolean mIsFirstLoad = true;

    private Activity mActivity;
    private Context mContext;

    private ECProductObject mUpdatedProductObj = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SCConstants.CODE_SHOW_PRODUCT_DETAIL) {
                mUpdatedProductObj = data.getExtras().getParcelable(ECProductObject.class.toString());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SCGlobalUtils.sActivityArr.remove(this);
    }

    @Override
    protected void onRestart() {
        new SCGlobalUtils(this);

        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.e(TAG_LOG, "onResume");
        if (!mIsFirstLoad) {
            requestGetFavoriteItems();
            requestGetFollowShop();
            requestGetExchangeLogs();
            requestForHistory();
        }

        requestCheckPoint();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecpoint_manager);

        SCGlobalUtils.sActivityArr.add(this);

        mActivity = this;
        mContext = this;

        new SCGlobalUtils(this);

        mIsFirstLoad = false;

        //find views:
        header = (LinearLayout) findViewById(R.id.header);
        bottom_line = (View) findViewById(R.id.bottom_white);
        //
        grvHistory = (ECExpandableGridView) findViewById(R.id.ec_point_grv_history);
        grvHistory.setExpanded(true);
        grvFavorite = (ECExpandableGridView) findViewById(R.id.ec_point_grv_feature);
        grvFavorite.setExpanded(true);
        grvShop = (ECExpandableGridView) findViewById(R.id.ec_point_grv_shop);
        grvShop.setExpanded(true);
        pointHisory = (ECExpandableGridView) findViewById(R.id.ec_point_history);
        pointHisory.setExpanded(true);

        ecChart = (ECChart) findViewById(R.id.ec_chart_view);

        imgBack = (ImageView) findViewById(R.id.ec_point_btn_back);
        imgBack.setContentDescription(BTN_BACK);

        imgShowMoreHistory = (ImageView) findViewById(R.id.ec_point_show_more_history);
        imgShowMoreHistory.setContentDescription(BTN_SHOW_MORE_HISTORY);
        imgShowMoreHistoryPoint = (ImageView) findViewById(R.id.show_more_point_history);
        imgShowMoreHistoryPoint.setContentDescription(BTN_SHOW_MORE_HISTORY_POINT);
        imgShowMoreFavorite = (ImageView) findViewById(R.id.ec_point_show_more_feature);
        imgShowMoreFavorite.setContentDescription(BTN_SHOW_MORE_FEATURE);
        imgShowMoreShop = (ImageView) findViewById(R.id.ec_point_show_more_shop);
        imgShowMoreShop.setContentDescription(BTN_SHOW_MORE_SHOP);

        tvUserName = (SCSingleLineTextView) findViewById(R.id.ec_point_tv_user_name);
        tvUserPoint = (TextView) findViewById(R.id.ec_point_point);
        imgAvatar = (SCCircleImageView) findViewById(R.id.ec_point_avatar);

        //init variable
        mUserObject = SCUserObject.getInstance();
        listChartItem = new ArrayList<ECChartItemObject>();

        mListFavorite = new ArrayList<ECProductObject>();
        mListHistory = new ArrayList<ECProductObject>();
        mListShop = new ArrayList<ECShopObject>();
        mListPointHistory = new ArrayList<SCHistoryObject>();

        mHistoryAdapter = new ECPointProductAdapter(mActivity, mListHistory, ECPointProductAdapter.TYPE_EXCHANGE_HISTORY);
        mHistoryPointAdapter  = new ECPointHistoryAdapter(mActivity,mListPointHistory);
        mFavoriteAdapter = new ECPointProductAdapter(mActivity, mListFavorite, ECPointProductAdapter.TYPE_FAVORITE);
        mShopAdapter = new ECPointShopAdapter(mActivity, mListShop);

        new SCMultipleScreen(this);
        SCMultipleScreen.resizeAllView(this);


        //actions:
        fillInforUser(mUserObject);
        getPointLogs();

        imgShowMoreHistory.setOnClickListener(this);
        imgShowMoreHistoryPoint.setOnClickListener(this);
        imgShowMoreFavorite.setOnClickListener(this);
        imgShowMoreShop.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        requestGetFavoriteItems();
        requestGetFollowShop();
        requestGetExchangeLogs();
        requestForHistory();
    }

    private void fillInforUser(SCUserObject userObject) {
        tvUserName.setText(userObject.getNickname());
        if (userObject.getCampusPoint() != null && !userObject.getCampusPoint().equals("")) {
            tvUserPoint.setText(userObject.getCampusPoint());
        } else {
            tvUserPoint.setText("0");
        }

        if (SCUserObject.getInstance().getIconInstance() == null) {
            ImageLoader.getInstance().displayImage(userObject.getIcon(), imgAvatar, SCGlobalUtils.sOptForUserIcon);
        } else {
            imgAvatar.setImageBitmap(userObject.getIconInstance());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getContentDescription().equals(BTN_SHOW_MORE_HISTORY)) {
            mHistoryAdapter.expand();
            mHistoryAdapter.notifyDataSetChanged();
            imgShowMoreHistory.setVisibility(View.GONE);
        } else if (view.getContentDescription().equals(BTN_SHOW_MORE_HISTORY_POINT)) {
            mHistoryPointAdapter.expand();
            mHistoryPointAdapter.notifyDataSetChanged();
            imgShowMoreHistoryPoint.setVisibility(View.GONE);
            header.setVisibility(View.VISIBLE);
            bottom_line.setVisibility(View.VISIBLE);
            pointHisory.setVisibility(View.VISIBLE);
        } else if (view.getContentDescription().equals(BTN_SHOW_MORE_FEATURE)) {
            mFavoriteAdapter.expand();
            mFavoriteAdapter.notifyDataSetChanged();
            imgShowMoreFavorite.setVisibility(View.GONE);
        } else if (view.getContentDescription().equals(BTN_SHOW_MORE_SHOP)) {
            mShopAdapter.expand();
            mShopAdapter.notifyDataSetChanged();
            imgShowMoreShop.setVisibility(View.GONE);
        } else if (view.getContentDescription().equals(BTN_BACK)) {
            if(mUpdatedProductObj != null) {
                Intent intent = new Intent();
                intent.putExtra(ECProductObject.class.toString(), mUpdatedProductObj);
                setResult(RESULT_OK, intent);
            }
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_right);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }

    private void requestCheckPoint() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObject.getAppId();
        //String appId = "188TZ";

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);

        SCRequestAsyncTask mSCRequestAsyncTask = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_CHECK_POINT, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);
                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_CHECK_POINT, result);
                if (returnHash.containsKey(SCConstants.TAG_CAMPUS_POINT)) {
                    String campusPoint = (String)returnHash.get(SCConstants.TAG_CAMPUS_POINT);
                    mUserObject.setCampusPoint(campusPoint);
                    tvUserPoint.setText(campusPoint);
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

    private void requestGetFavoriteItems() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_FAVORITE_ITEMS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_FAVORITE_ITEMS, result);

                if (returnHash.containsKey(SCConstants.TAG_ITEMS)) {
                    mListFavorite.clear();

                    mListFavorite.addAll((ArrayList<ECProductObject>) returnHash.get(SCConstants.TAG_ITEMS));

                    grvFavorite.setAdapter(mFavoriteAdapter);
                    mFavoriteAdapter.notifyDataSetChanged();

                }

                if (mListHistory.size() <= 3) {
                    imgShowMoreFavorite.setVisibility(View.GONE);
                } else {
                    imgShowMoreFavorite.setVisibility(View.VISIBLE);
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


    private void requestForHistory() {

        String appId = SCUserObject.getInstance().getAppId();;
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_APP_ID, appId);

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_HISTORY_ITEMS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_HISTORY_ITEMS, result);

                if (returnHash.containsKey(SCConstants.TAG_POINT_HISTORY_LOGS)) {
                    mListPointHistory.clear();

                   mListPointHistory.addAll((ArrayList<SCHistoryObject>) returnHash.get(SCConstants.TAG_POINT_HISTORY_LOGS));

                    pointHisory.setAdapter(mHistoryPointAdapter);
                    mHistoryPointAdapter.notifyDataSetChanged();
                }

                if (mListPointHistory.size() == 0) {
                    imgShowMoreHistoryPoint.setVisibility(View.GONE);
                } else {
                    imgShowMoreHistoryPoint.setVisibility(View.VISIBLE);
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



    private void requestGetFollowShop() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_FOLLOW_SHOPS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_FOLLOW_SHOPS, result);

                if (returnHash.containsKey(SCConstants.TAG_SHOPS)) {
                    mListShop.clear();

                    mListShop.addAll((ArrayList<ECShopObject>) returnHash.get(SCConstants.TAG_SHOPS));

                    grvShop.setAdapter(mShopAdapter);
                    mShopAdapter.notifyDataSetChanged();
                }

                if (mListShop.size() <= 3) {
                    imgShowMoreShop.setVisibility(View.GONE);
                } else {
                    imgShowMoreShop.setVisibility(View.VISIBLE);
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

    private void requestGetExchangeLogs() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();
        // just for test
        //appId = "17AS";

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_EXCHANGE_LOGS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_EXCHANGE_LOGS, result);

                if (returnHash.containsKey(SCConstants.TAG_APP_EXCHANGE_LOGS)) {
                    mListHistory.clear();

                    mListHistory.addAll((ArrayList<ECProductObject>) returnHash.get(SCConstants.TAG_APP_EXCHANGE_LOGS));
                    grvHistory.setAdapter(mHistoryAdapter);

                    mHistoryAdapter.notifyDataSetChanged();
                }

                if (mListHistory.size() <= 3) {
                    imgShowMoreHistory.setVisibility(View.GONE);
                } else {
                    imgShowMoreHistory.setVisibility(View.VISIBLE);
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

    public void getPointLogs() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObject.getAppId();
        String email = mUserObject.getEmail();

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put(SCConstants.PARAM_APP_ID, appId);
        param.put(SCConstants.PARAM_DATE, date);
        param.put(SCConstants.PARAM_KEY, key);
        param.put(SCConstants.PARAM_EMAIL, email);

        String url = SCUrlConstants.URL_GET_POINT_LOGS;
//        SCGlobalUtils.addAditionalHeader = true ;
//        SCGlobalUtils.additionalHeaderTag = "Authorization";
//        SCGlobalUtils.additionalHeaderValue = "Bearer "+getBase64(SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_ACCESS_TOKEN, null));
        SCGlobalUtils.addAditionalHeader = true;
        APIUtils.LoadJSON(this, param, url, new APICallBack() {
            @Override
            public void uiStart() {
                SCGlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void success(String successString, int type) {
                try {
                    Log.e(TAG_LOG, successString);

                    JSONObject jsonObject = new JSONObject(successString);
                    if (jsonObject.getString(SCConstants.TAG_SUCCESS).equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray(SCConstants.TAG_LOGS);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            ECChartItemObject chartItemObject = new ECChartItemObject();
                            chartItemObject.parseJSON(object);
                            listChartItem.add(chartItemObject);
                        }
                        setUpChart(listChartItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String failString) {

            }

            @Override
            public void uiEnd() {
                SCGlobalUtils.dismissLoadingProgress();
            }
        });
    }

    private void setUpChart(final ArrayList<ECChartItemObject> listItem) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ecChart.setUpChart(listItem);
            }
        });
    }
    public String getBase64(final String input) {
        return Base64.encodeBytes(input.getBytes(), Base64.DONT_BREAK_LINES);
    }
}
