package jp.co.scmodule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.CorrectSizeUtil;
import jp.co.scmodule.utils.GetAccessToken;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;
import jp.co.scmodule.utils.SCUrlConstants;

public class SCLoginForOld extends SCMyActivity {
    private static final String TAG_LOG = "SCLoginForOld";
    private Context mContext = null;
    private Activity mActivity = null;

    private EditText mEtEmail = null;
    private EditText mEtPassword = null;

    private Button mBtnRegister = null;
    private Button mLoginPage = null;
    private boolean getCode = false;

    private String authCode;

    private View.OnClickListener mOnClickListener = null;

    private SCRequestAsyncTask mRequestAsync = null;

    private SCUserObject mUserObj = null;

    private int mCodeType = 0;

    private String mScheme = null;
    private String mEmail = null;
    private String mPassword = null;
    private boolean mIsHash = false;
    private CorrectSizeUtil mCorrectSize = null;

    Dialog auth_dialog = null;
    WebView web = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SCGlobalUtils.sActivityArr.remove(this);
    }

    @Override
    public void onBackPressed() {
        // afterClickBack();
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sclogin_for_old);
        SCGlobalUtils.sActivityArr.add(this);

        mContext = this;
        mActivity = this;

        mEmail = getIntent().getStringExtra(SCConstants.TAG_EMAIL);
        mPassword = getIntent().getStringExtra(SCConstants.TAG_PASSWORD);
        mScheme = getIntent().getStringExtra(SCConstants.TAG_SCHEME);
        mCodeType = getIntent().getIntExtra(Integer.class.toString(), 0);
        mIsHash = getIntent().getBooleanExtra(SCConstants.TAG_IS_HASH, false);

        init();
    }

    @Override
    protected void init() {
        super.init();
        if (mEmail != null && mPassword != null) {
            mEtEmail.setText(mEmail);
            mEtPassword.setText(mPassword);
        }

        //Init variable
//        mCorrectSize = CorrectSizeUtil.getInstance(this);
//        mCorrectSize.correctSize();
    }

    @Override
    protected void findViewById() {
        mEtEmail = (EditText) findViewById(R.id.sc_login_et_email);
        mEtPassword = (EditText) findViewById(R.id.sc_login_et_password);
        mLoginPage = (Button) findViewById(R.id.sc_login_btn_login);
    }

    @Override
    protected void initListeners() {
        mLoginPage.setContentDescription("login");
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("login")) {
                    afterClickLogin();
                } 
            }
        };
    }

    private void afterClickLogin() {

        requestlogin();
    }

    private void requestlogin() {
        long date = SCGlobalUtils.getTimeInMilisecond();
        String key = SCConstants.SECRET_KEY + date + SCConstants.TADACOPY_CLIENT_ID_SECRET;
        String state = SCGlobalUtils.md5(key);
        String UUID = "";
        if (SCSharedPreferencesUtils.getString(this, SCConstants.TAG_DEVICE_ID, null) == null) {
            if (SCGlobalUtils.DEVICEUUID != null)
                UUID = SCGlobalUtils.DEVICEUUID;
            else {
                UUID = SCGlobalUtils.getDeviceUUID(this);
                if (UUID.equals("")) {
                    Toast.makeText(this, "UUID Missing", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            UUID = SCSharedPreferencesUtils.getString(this, SCConstants.TAG_DEVICE_ID, null);
        }
        //create the posdata for webview to load
        String postData = "client_id=" + SCConstants.TADACOPY_CLIENT_ID
                + "&redirect_uri=" + SCConstants.TADACOPY_REDIRECT_URI
                + "&response_type=" + "code"
                + "&email=" + mEtEmail.getText().toString().trim()
                + "&password=" + mEtPassword.getText().toString().trim()
                + "&password_is_hash=" + SCGlobalUtils.ishashpass + ""
                + "&state=" + state
                + "&date=" + date
                + "&uuid=" + UUID
                + "&application_id=" + SCConstants.TADACOPY_DEFAULT_APP_ID_SECRET;
        //set the invisible webview
        Log.e("postdata", postData);
        String url = "";
        //post in webview

        url = SCUrlConstants.URL_BASE_LOGIN + "login_mail_by_update_password";

        //post in webview
        loadTheUrlAndGetAccessToken(url, postData);
    }

    private void loadTheUrlAndGetAccessToken(String url, String postdata) {
        //showing a default default dialog
        auth_dialog = new Dialog(SCLoginForOld.this);
        auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        auth_dialog.setContentView(R.layout.auth_dialog);
        web = (WebView) auth_dialog.findViewById(R.id.webv);
        web.getSettings().setJavaScriptEnabled(true);

        web.postUrl(url, EncodingUtils.getBytes(postdata, "BASE64"));


        //Listen the redirection and get the code
        web.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("url", url);
                Uri uri = Uri.parse(url);
                if (url.contains("code=") && !url.contains("success=0")) {
                    if(!getCode) {
                        getCode = true;
                        authCode = uri.getQueryParameter("code");
                        Log.i("", "CODE : " + authCode);
                        //auth_dialog.dismiss();
                        // Toast.makeText(getApplicationContext(), "Authorization Code is: " + authCode, Toast.LENGTH_SHORT).show();
                        //clean the cache
                        clearTheResponseCache();
                        //get the access token by the auth code
                        new TokenGet().execute();
                    }
                } else if (url.contains("error")) {
                    SCGlobalUtils.dismissLoadingProgress();
                    Log.i("", "Failed to Redirect");
                    String error = uri.getQueryParameter("error");
                    try {
                        error = URLDecoder.decode(error, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                    //auth_dialog.dismiss();
                    //delete is hash
                    if (SCGlobalUtils.ishashpass.equals("1")) {
                        SCGlobalUtils.ishashpass = "0";
                    }

                }
                auth_dialog.dismiss();
            }
        });
        auth_dialog.show();
        auth_dialog.setCancelable(true);
        SCGlobalUtils.showLoadingProgress(mContext);


    }

    private void clearTheResponseCache() {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }


    private class TokenGet extends AsyncTask<String, String, JSONObject> {
        // private ProgressDialog pDialog;
        String Code;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(Login_mail.this);
//            pDialog.setMessage("Connecting ...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            GetAccessToken jParser = new GetAccessToken();
            JSONObject json = jParser.gettoken(mContext, authCode, SCConstants.TADACOPY_CLIENT_ID, SCConstants.TADACOPY_CLIENT_ID_SECRET, SCConstants.TADACOPY_REDIRECT_URI);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            // pDialog.dismiss();
            SCGlobalUtils.dismissLoadingProgress();
            if (json != null) {

                try {

                    String success = json.getString("success");
                    if (success.equals("true")) {
                        JSONObject jObj = json.getJSONObject("data");
                        String access_token = jObj.getString("access_token");
                        String refresh_access_token = jObj.getString("refresh_token");
                        //save email and password for use
                        SCSharedPreferencesUtils.putString(SCLoginForOld.this, SCConstants.TAG_EMAIL, mEtEmail.getText().toString());
                        SCSharedPreferencesUtils.putString(SCLoginForOld.this, SCConstants.TAG_PASSWORD, mEtPassword.getText().toString());
                        //save the access and refresh token
                        SCSharedPreferencesUtils.putString(SCLoginForOld.this, SCConstants.TAG_ACCESS_TOKEN, access_token);
                        SCSharedPreferencesUtils.putString(SCLoginForOld.this, SCConstants.TAG_REFRESH_TOKEN, refresh_access_token);

                        if (jObj.has("user_data")) {
                            String result = jObj.toString();
                            HashMap<String, Object> returnHashMap = null;

                            returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_LOGIN_USER_DATA, result);

                            SCUserObject userObj = null;
                            String errCode = null;

                            String title = null;
                            String body = null;
                            if (returnHashMap.containsKey("user_data")) {
                                userObj = (SCUserObject) returnHashMap.get("user_data");
                                userObj.setIsGuest("false");
                                // save appid to sharepreference
                                SCSharedPreferencesUtils.putString(SCLoginForOld.this, SCConstants.TAG_APP_ID, userObj.getAppId());
                                SCSharedPreferencesUtils.putString(SCLoginForOld.this, SCConstants.TAG_LOGIN_TYPE, SCConstants.PROVIDER_EMAIL);

                                if ((userObj.getUniversityId() == null || (userObj.getUniversityId().equals("0") || userObj.getUniversityId().equals("")))
                                        || (userObj.getCampusId() == null || (userObj.getCampusId().equals("0") || userObj.getCampusId().equals("")))
                                        || (userObj.getDepartmentId() == null || (userObj.getDepartmentId().equals("0") || userObj.getDepartmentId().equals("")))
                                       // || (userObj.getMajorId() == null || (userObj.getMajorId().equals("0") || userObj.getMajorId().equals("")))
                                        || (userObj.getEnrollmentYear() == null || (userObj.getEnrollmentYear().equals("0") || userObj.getEnrollmentYear().equals("")))) {

                                    Intent intent = new Intent(getApplicationContext(), SCEditInfoOneActivity.class);
                                    intent.putExtra(SCUserObject.class.toString(), userObj);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.anim_slide_in_right,
                                            R.anim.anim_slide_out_left);

                                } else {
                                    Intent intent = new Intent();
                                    //if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
                                        intent.setAction(SCConstants.ACTION_OPEN_CONTENT_TADACOPY);
//                                    } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
//                                        intent.setAction(SCConstants.ACTION_OPEN_CONTENT_CANPASS);
//                                    }
                                    intent.putExtra(SCUserObject.class.toString(), mUserObj);
                                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.anim_slide_in_right,
                                            R.anim.anim_slide_out_left);
                                }


                            }

                            if (returnHashMap.containsKey(SCConstants.TAG_ERROR_CODE)) {
                                return;
                            }

                            ;

                        }
                        // writeSSOInfo(Utils.access_token, Utils.refresh_access_token);
                    } else if (success.equals("false")) {
                        SCGlobalUtils.dismissLoadingProgress();
                        Toast.makeText(getApplicationContext(), json.getString("error") + "", Toast.LENGTH_LONG).show();
                        if (SCGlobalUtils.ishashpass.equals("1")) {
                            SCGlobalUtils.ishashpass = "0";
                        }
                    } else {
                        SCGlobalUtils.dismissLoadingProgress();
                        Toast.makeText(getApplicationContext(), "Something went wrong, please try again", Toast.LENGTH_LONG).show();
                        if (SCGlobalUtils.ishashpass.equals("1")) {
                            SCGlobalUtils.ishashpass = "0";
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
//                pDialog.dismiss();
                SCGlobalUtils.dismissLoadingProgress();
                if (SCGlobalUtils.ishashpass.equals("1")) {
                    SCGlobalUtils.ishashpass = "0";
                    SCSharedPreferencesUtils.removeComponent(SCLoginForOld.this, SCConstants.TAG_PASSWORD);
                }
            }
        }
    }

    @Override
    protected void setListenerForViews() {
        mLoginPage.setOnClickListener(mOnClickListener);
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
}
