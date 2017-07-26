package jp.co.scmodule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.brickred.socialauth.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import jp.co.scmodule.interfaces.SCDialogCallback;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.CorrectSizeUtil;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;
import jp.co.scmodule.utils.SCUrlConstants;


public class WebviewActivity extends Activity implements View.OnClickListener {
    Bundle bundle;
    WebView wvContent;
    ImageView mImgButtonBack;
    ProgressBar mpbProgressBar;
    String request_type = null;
    TextView header = null;
    //Variables:
    CorrectSizeUtil mCorrectSize;
    String url = null;

    HttpResponse response = null;
    HttpClient httpclient = null;
    HttpPost httpPost = null;
    ImageView history_back = null;
    ImageView history_forward = null;
    LinearLayout history_ln = null;

    int left_arrow = 0;
    int right_arrow = 0;

    private View header_divider = null;
    private boolean isAutologin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_sc);
        String url = getIntent().getExtras().getString("url");
        header_divider = (View) findViewById(R.id.header_divider);
        wvContent = (WebView) findViewById(R.id.wv_campus_work_content);
        mpbProgressBar = (ProgressBar) findViewById(R.id.prgb_campus_work_webview);
        mImgButtonBack = (ImageView) findViewById(R.id.img_left_header);
        history_ln = (LinearLayout) findViewById(R.id.history_ln);
        history_back = (ImageView) findViewById(R.id.history_back);
        history_forward = (ImageView) findViewById(R.id.history_forward);

        header = (TextView) findViewById(R.id.header);
        header.setText("");
        mCorrectSize = CorrectSizeUtil.getInstance(this);

        wvContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, final int newProgress) {
                super.onProgressChanged(view, newProgress);
                mpbProgressBar.setVisibility(View.VISIBLE);
                if (newProgress < 100) {
                    mpbProgressBar.setProgress(newProgress);
                } else {
                    mpbProgressBar.setVisibility(View.GONE);
                }
            }


        });
        //actions:
        if (url.equals("autoLoginWebview")) {
            isAutologin = true;
            history_ln.setVisibility(View.VISIBLE);
            String collectionUrl = getIntent().getExtras().getString("collectionUrl");
            String title = getIntent().getExtras().getString("title");
            rediect(collectionUrl);
            if (title != null) {
                header.setText(title);
            }
            //anotherway(collectionUrl);
        } else {
            history_ln.setVisibility(View.GONE);
            wvContent.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    Bundle bundle = SslCertificate.saveState(error.getCertificate());
                    X509Certificate x509Certificate;
                    byte[] bytes = bundle.getByteArray("x509-certificate");
                    if (bytes == null) {
                        x509Certificate = null;
                    } else {
                        try {
                            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                            Certificate cert = certFactory.generateCertificate(new ByteArrayInputStream(bytes));
                            x509Certificate = (X509Certificate) cert;
                        } catch (CertificateException e) {
                            x509Certificate = null;
                        }
                    }

                    // Now I have an X509Certificate I can pass to an X509TrustManager for validation.
                }

            });
            wvContent.getSettings().setJavaScriptEnabled(true);

            wvContent.loadUrl(url);
        }
        mImgButtonBack.setOnClickListener(this);
        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            setUpViewsForTadacopy();
        } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            setUpViewsForCanpass();
        }
        else if (getPackageName().equals(SCConstants.PACKAGE_TORETAN_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TORETAN_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TORETAN_STAGING)) {
            setUpViewsForToretan();
        }


        history_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wvContent.canGoBack()) {
                    /*
                        public void goBack ()
                            Goes back in the history of this WebView.
                    */
                    wvContent.goBack();

                }
            }
        });


        history_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wvContent.canGoForward()) {
                    /*
                        public void goForward ()
                            Goes forward in the history of this WebView.
                    */
                    wvContent.goForward();

                }
            }
        });

        mCorrectSize.correctSize();
    }



    private void setUpViewsForTadacopy() {
        left_arrow = R.drawable.yellow_left_arrow;
        right_arrow = R.drawable.yellow_right_arrow;
    }

    private void setUpViewsForCanpass() {
        mImgButtonBack.setImageResource(R.drawable.yellow_left_arrow_canpass);
        header.setTextColor(getResources().getColor(R.color.canpass_main));
        mpbProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_horizontal_canpass));
        header_divider.setBackgroundColor(getResources().getColor(R.color.canpass_main));
        left_arrow = R.drawable.yellow_left_arrow_canpass;
        right_arrow = R.drawable.yellow_right_arrow_canpass;
    }
    private void setUpViewsForToretan() {
        mImgButtonBack.setImageResource(R.drawable.blue_left_arrow_toretan);
        header.setTextColor(getResources().getColor(R.color.toretan_main));
        mpbProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_horizontal_toretan));
        header_divider.setBackgroundColor(getResources().getColor(R.color.toretan_main));
        left_arrow = R.drawable.blue_left_arrow_toretan;
        right_arrow = R.drawable.blue_right_arrow_toretan;
    }



    private void anotherway(String collectionUrl) {

        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return super.shouldOverrideUrlLoading(view, url);
            }

        });

        SCUserObject mUserObj = SCUserObject.getInstance();
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();
        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);


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
        httpclient = new DefaultHttpClient();
        httpPost = new HttpPost(getPostUrl());

        SCGlobalUtils.additionalHeaderTag = "Authorization";
        SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(this, SCConstants.TAG_ACCESS_TOKEN, null));

        httpPost.addHeader(SCGlobalUtils.additionalHeaderTag, SCGlobalUtils.additionalHeaderValue);// example of adding extra header referer

        ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();


        postParameters.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID, appId)); // add post parameters in array list
        postParameters.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID, SCConstants.TADACOPY_DEFAULT_APP_ID_SECRET));
        postParameters.add(new BasicNameValuePair(SCConstants.PARAM_DATE, date + ""));
        postParameters.add(new BasicNameValuePair(SCConstants.PARAM_KEY, key));
        postParameters.add(new BasicNameValuePair("client_id", SCConstants.TADACOPY_CLIENT_ID));
        postParameters.add(new BasicNameValuePair("redirect_uri", collectionUrl));
        postParameters.add(new BasicNameValuePair("uuid", UUID));


        try {
            wvContent.getSettings().setJavaScriptEnabled(true);
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            new Retrievedata().execute();

        } catch (Exception e) {
            // handle errors
            e.printStackTrace();
        }
    }

    private void rediect(String collectionUrl) {
        SCUserObject mUserObj = SCUserObject.getInstance();
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();
        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);


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
        String postData = "";
        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            postData = "app_id=" + appId
                    + "&client_id=" + SCConstants.TADACOPY_CLIENT_ID
                    + "&redirect_uri=" + collectionUrl
                    + "&key=" + key
                    + "&date=" + date
                    + "&uuid=" + UUID
                    + "&application_id=" + SCConstants.TADACOPY_DEFAULT_APP_ID_SECRET;
        } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            postData = "app_id=" + appId
                    + "&client_id=" + SCConstants.CANPASS_CLIENT_ID
                    + "&redirect_uri=" + collectionUrl
                    + "&key=" + key
                    + "&date=" + date
                    + "&uuid=" + UUID
                    + "&application_id=" + SCConstants.CANPASS_DEFAULT_APP_ID_SECRET;
        }
        else if (getPackageName().equals(SCConstants.PACKAGE_TORETAN_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TORETAN_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TORETAN_STAGING)) {
            postData = "app_id=" + appId
                    + "&client_id=" + SCConstants.TORETAN_CLIENT_ID
                    + "&redirect_uri=" + collectionUrl
                    + "&key=" + key
                    + "&date=" + date
                    + "&uuid=" + UUID
                    + "&application_id=" + SCConstants.TORETAN_DEFAULT_APP_ID_SECRET;
        }


        wvContent.getSettings().setJavaScriptEnabled(true);
//        wvContent.clearCache(true);
//        wvContent.clearHistory();
//        clearCookies(this);

        wvContent.postUrl(getPostUrl(), EncodingUtils.getBytes(postData, "BASE64"));


        //Listen the redirection and get the code
        wvContent.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("url", url);
                if (wvContent.canGoBack()) {
                    history_back.setImageResource(left_arrow);
                } else {
                    history_back.setImageResource(R.drawable.gray_left_arrow);
                }

                // Check web view forward history availability
                if (wvContent.canGoForward()) {
                    history_forward.setImageResource(right_arrow);
                } else {
                    history_forward.setImageResource(R.drawable.gray_right_arrow);
                }

            }
        });


    }

//    @SuppressWarnings("deprecation")
//    public static void clearCookies(Context context)
//    {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Log.e("ClearCookies", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP));
//            CookieManager.getInstance().removeAllCookies(null);
//            CookieManager.getInstance().flush();
//        } else
//        {
//            Log.e("ClearCookies", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP));
//            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
//            cookieSyncMngr.startSync();
//            CookieManager cookieManager=CookieManager.getInstance();
//            cookieManager.removeAllCookie();
//            cookieManager.removeSessionCookie();
//            cookieSyncMngr.stopSync();
//            cookieSyncMngr.sync();
//        }
//    }

    private String getPostUrl() {
        return SCUrlConstants.URL_AUTO_LOGIN;
    }

    public class Retrievedata extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                response = httpclient.execute(httpPost);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            BasicResponseHandler responseHandler = new BasicResponseHandler();
            String htmlString = null;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null; ) {
                    builder.append(line).append("\n");
                }
                String html = builder.toString();
                wvContent.loadData(html, "text/html", "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.img_left_header) {

            if (isAutologin) {

                SCGlobalUtils.showLoginDialogGoBackToApp(WebviewActivity.this, new SCDialogCallback() {
                    @Override
                    public void onAction1() {
                        //cancel btn to left

                    }

                    @Override
                    public void onAction2() {
                        //ok button to right
                        isAutologin = false;
                        finish();
                        overridePendingTransition(R.anim.anim_nothing,
                                R.anim.anim_slide_out_bottom);
                    }

                    @Override
                    public void onAction3() {
                        //close btn at the bottom
                    }

                    @Override
                    public void onAction4() {

                    }
                });

            } else {
                finish();
                overridePendingTransition(R.anim.anim_nothing,
                        R.anim.anim_slide_out_bottom);

            }


        }
    }

    public String getBase64(final String input) {
        return Base64.encodeBytes(input.getBytes(), Base64.DONT_BREAK_LINES);
    }

}
