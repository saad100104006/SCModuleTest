package jp.co.scmodule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;

import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.CorrectSizeUtil;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;

public class SCMailRegistrationActivity extends SCMyActivity {
    private static final String TAG_LOG = "SCMailRegistrationActivity";
    private Context mContext = null;
    private Activity mActivity = null;

    private EditText mEtEmail = null;
    private EditText mEtPassword = null;
    private EditText mEtPasswordConfirm = null;
    //private Button mBtnLogin = null;
    private Button mBtnRegister = null;
    private ImageButton mLoginPage = null;
    //private TextView mTvDescription = null;
   // private LinearLayout mLlDescription = null;


    private OnClickListener mOnClickListener = null;

    private SCRequestAsyncTask mRequestAsync = null;

    private SCUserObject mUserObj = null;

    private int mCodeType = 0;

    private String mScheme = null;
    private String mEmail = null;
    private String mPassword = null;
    private boolean mIsHash = false;
    private CorrectSizeUtil mCorrectSize = null;

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
        setContentView(R.layout.activity_sclogin);

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

        // get email and password from pref
//        String mail = SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_EMAIL);
//        String pass = SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_PASSWORD);
        String mail = null;
        String pass = null;
        if (mail != null && pass != null) {
            mEtEmail.setText(mail);
            mEtPassword.setText(pass);
        }

        if (mEmail != null && mPassword != null && mScheme != null) {
            mEtEmail.setText(mEmail);
            mEtPassword.setText(mPassword);
            afterClickLogin();
            // mLlDescription.setVisibility(View.GONE);
        } else if (mEmail != null && mPassword != null && mScheme == null) {
            mEtEmail.setText(mEmail);
            mEtPassword.setText(mPassword);

            if (!mEmail.equals("")) {
                String title = null;
               // if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
                    title = getResources().getString(R.string.dialog_tadacopy_new_app_title);
//                } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
//                    title = getResources().getString(R.string.dialog_canpass_new_app_title);
//                }

                String body = getResources().getString(R.string.dialog_new_app_password_require);
                String action = getResources().getString(R.string.common_confirm);
                SCGlobalUtils.showInfoDialog(mContext, title, body, action, null);


                String description = "";
               // if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
                    description = getResources().getString(R.string.login_description_1_tadacopy);
//                } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
//                    description = getResources().getString(R.string.login_description_1_canpass);
//                }
                // mTvDescription.setText(description);

                // mBtnLogin.setVisibility(View.GONE);
                // mBtnRegister.setText(getResources().getString(R.string.transfer));

            } else {
                // mLlDescription.setVisibility(View.GONE);
            }
        }
        //Init variable
//        mCorrectSize = CorrectSizeUtil.getInstance(this);
//        mCorrectSize.correctSize();
    }

    @Override
    protected void initListeners() {
       // mBtnLogin.setContentDescription("login");
        mBtnRegister.setContentDescription("register");
        mLoginPage.setContentDescription("loginPage");
        mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("login")) {
                    afterClickLogin();
                } else if (v.getContentDescription().equals("register")) {
                    afterClickRegister();
                } else if (v.getContentDescription().equals("loginPage")) {
                    afterClickLoginPage();
                }
            }
        };
    }

    @Override
    protected void setListenerForViews() {
        //mBtnLogin.setOnClickListener(mOnClickListener);
        mBtnRegister.setOnClickListener(mOnClickListener);
        mLoginPage.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void findViewById() {
        mEtEmail = (EditText) findViewById(R.id.sc_login_et_email);
        mEtPassword = (EditText) findViewById(R.id.sc_login_et_password);
        mEtPasswordConfirm = (EditText) findViewById(R.id.sc_confirm_pass);
        //mBtnLogin = (Button) findViewById(R.id.sc_login_btn_login);
        mBtnRegister = (Button) findViewById(R.id.sc_login_btn_register);
        mLoginPage = (ImageButton) findViewById(R.id.sc_login_page);
       // mTvDescription = (TextView) findViewById(R.id.sc_login_et_description);
       // mLlDescription = (LinearLayout) findViewById(R.id.sc_login_ll_description);
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

    private void afterClickLogin() {
        String dialogBody = null;
        String email = mEtEmail.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();

        if (email.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_email_empty_label);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        } else if (password.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_password_empty_label);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        } else if (!SCGlobalUtils.isEmailValid(mEtEmail.getText().toString())) {
            dialogBody = getResources().getString(R.string.dialog_body_email_invalid_label);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        }

        requestLoginMail();
    }

    private void afterClickRegister() {
        String dialogBody = null;
        String email = mEtEmail.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        String password_retype = mEtPasswordConfirm.getText().toString().trim();

        if (email.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_email_empty_label);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        } else if (password.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_password_empty_label);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        } else if (password.length() < 6) {
            dialogBody = getResources().getString(R.string.dialog_body_password_less_than_6_label);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        } else if (!SCGlobalUtils.isEmailValid(mEtEmail.getText().toString())) {
            dialogBody = getResources().getString(R.string.dialog_body_email_invalid_label);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        }else if(!password_retype.equals(password)){
            dialogBody = getResources().getString(R.string.dialog_body_password_invalid_match);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        }

        requestRegisterMail();
    }

    private void afterClickLoginPage() {
//        if (mEmail != null && mPassword != null) {
//            Intent intent = new Intent();
//            if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
//                intent.setAction(SCConstants.ACTION_OPEN_LOGIN_TADACOPY);
//            } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
//                intent.setAction(SCConstants.ACTION_OPEN_LOGIN_CANPASS);
//            }
//            startActivity(intent);
//            overridePendingTransition(R.anim.anim_slide_in_left,
//                    R.anim.anim_slide_out_right);
//        }
        Intent intent = new Intent(SCMailRegistrationActivity.this,SCLoginMail.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void requestLoginMail() {
        String email = mEtEmail.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String src = secretKey + email + date;
        String key = SCGlobalUtils.md5Hash(src);
        String applicationId = "";
        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            applicationId = SCConstants.APP_ID_TADACOPY;
        } else if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            applicationId = SCConstants.APP_ID_CANPASS;
        }

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_KEY, key);
        params.put(SCConstants.PARAM_DATE, date);
        params.put(SCConstants.PARAM_EMAIL, email);
        params.put(SCConstants.PARAM_PASSWORD, password);
        params.put(SCConstants.PARAM_APPLICATION_ID, applicationId);
        params.put(SCConstants.PARAM_IS_HASH, (mIsHash) ? "true" : "false");

        mRequestAsync = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_LOGIN_MAIL, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_LOGIN_MAIL, result);
                SCUserObject userObj = null;
                String errCode = null;

                String title = null;
                String body = null;
                if (returnHashMap.containsKey(SCConstants.TAG_USER_DATA)) {
                    userObj = (SCUserObject) returnHashMap.get(SCConstants.TAG_USER_DATA);
                    mUserObj = userObj;
                    mUserObj.setIsGuest("false");

                    // save appid to sharepreference
                    SCSharedPreferencesUtils.putString(mContext, SCConstants.TAG_APP_ID, userObj.getAppId());
                    SCSharedPreferencesUtils.putString(mContext, SCConstants.TAG_LOGIN_TYPE, SCConstants.PROVIDER_EMAIL);

                    SCSharedPreferencesUtils.putString(mContext, SCConstants.TAG_EMAIL, mEtEmail.getText().toString().trim());
                    SCSharedPreferencesUtils.putString(mContext, SCConstants.TAG_PASSWORD, mEtPassword.getText().toString().trim());
                }

                if (returnHashMap.containsKey(SCConstants.TAG_ERROR_CODE)) {
                    errCode = (String) returnHashMap.get(SCConstants.TAG_ERROR_CODE);
                    title = getResources().getString(R.string.dialog_error_title);
                    if (errCode.equals("0")) {
                        body = getResources().getString(R.string.dialog_register_wrong_email_format);
                    } else if (errCode.equals("1")) {
                        body = getResources().getString(R.string.dialog_register_wrong_email_format);
                    } else if (errCode.equals("2")) {
                        body = getResources().getString(R.string.dialog_register_failed_authenticate);
                    } else if (errCode.equals("3")) {
                        body = getResources().getString(R.string.dialog_register_email_has_been_registered);
                    } else if (errCode.equals("4")) {
                        body = getResources().getString(R.string.dialog_register_invalid_password);
                    }

                    SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);

                    return;
                }

                requestRegisterDevice();
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

        mRequestAsync.execute();
    }

    private void requestRegisterDevice() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();
        String uuid = "";
        if( SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_DEVICE_ID, null) == null){
            if(SCGlobalUtils.DEVICEUUID != null)
                uuid = SCGlobalUtils.DEVICEUUID;
            else
            {
                uuid = SCGlobalUtils.getDeviceUUID(this);
                if(uuid.equals("")) {
                    Toast.makeText(this, "UUID Missing", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            uuid = SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_DEVICE_ID, null);
        }

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_APP_ID, appId);
        params.put(SCConstants.PARAM_DATE, date);
        params.put(SCConstants.PARAM_KEY, key);
        params.put(SCConstants.PARAM_DEVICE_ID, uuid);

        SCRequestAsyncTask requestAsync = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_REGISTER_DEVICE, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_REGISTER_DEVICE, result);

                if (returnHashMap.containsKey(SCConstants.TAG_DEVICE_ID)) {
                    String deviceId = (String) returnHashMap.get(SCConstants.TAG_DEVICE_ID);
                    SCSharedPreferencesUtils.putString(mContext, SCConstants.TAG_DEVICE_ID, deviceId);
                    Log.e(TAG_LOG, "Device ID:" + deviceId);

                    if ((mUserObj.getUniversityId().equals("0") || mUserObj.getUniversityId().equals(""))
                            || (mUserObj.getCampusId().equals("0") || mUserObj.getCampusId().equals(""))
                            || (mUserObj.getDepartmentId().equals("0") || mUserObj.getDepartmentId().equals(""))
                            || (mUserObj.getEnrollmentYear().equals("0") || mUserObj.getEnrollmentYear().equals(""))) {

                        Intent intent = new Intent(mContext, SCEditInfoOneActivity.class);
                        intent.putExtra(SCUserObject.class.toString(), mUserObj);
                        if (mCodeType == SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM
                                || mCodeType == SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM
                                || mCodeType == SCConstants.CODE_LOGIN_FOR_FOLLOW_SHOP) {
                            intent.putExtra(Integer.class.toString(), mCodeType);
                        }
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_right,
                                R.anim.anim_slide_out_left);
                    } else {
                        if (mCodeType == SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM
                                || mCodeType == SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM
                                || mCodeType == SCConstants.CODE_LOGIN_FOR_FOLLOW_SHOP) {
                            setResult(RESULT_OK);
                            finish();
                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_right);
                        } else {
                            Intent intent = new Intent();
                           // if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
                                intent.setAction(SCConstants.ACTION_OPEN_CONTENT_TADACOPY);
//                            } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
//                                intent.setAction(SCConstants.ACTION_OPEN_CONTENT_CANPASS);
//                            }
                            intent.putExtra(SCUserObject.class.toString(), mUserObj);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.anim_slide_in_right,
                                    R.anim.anim_slide_out_left);
                        }
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

        requestAsync.execute();
    }

    private void requestRegisterMail() {
        String email = mEtEmail.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String src = secretKey + email + date;
        String key = SCGlobalUtils.md5Hash(src);
        String agent = SCConstants.AGENT;
        String uuid = "";
        if( SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_DEVICE_ID, null) == null){
            if(SCGlobalUtils.DEVICEUUID != null)
                uuid = SCGlobalUtils.DEVICEUUID;
            else
            {
                uuid = SCGlobalUtils.getDeviceUUID(this);
                if(uuid.equals("")) {
                    Toast.makeText(this, "UUID Missing", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            uuid = SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_DEVICE_ID, null);
        }
        String applicationId = "";

        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            applicationId = SCConstants.APP_ID_TADACOPY;
        } else if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            applicationId = SCConstants.APP_ID_CANPASS;
        }


        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_KEY, key);
        params.put(SCConstants.PARAM_DATE, date);
        params.put(SCConstants.PARAM_EMAIL, email);
        params.put(SCConstants.PARAM_PASSWORD, password);
        params.put(SCConstants.PARAM_AGENT, agent);
        params.put(SCConstants.PARAM_DEVICE_ID, uuid);
        params.put(SCConstants.PARAM_APPLICATION_ID, applicationId);

        mRequestAsync = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_REGISTER_MAIL, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_LOGIN_MAIL, result);
                SCUserObject userObj = null;
                String errCode = null;

                String title = null;
                String body = null;

                if (returnHashMap.containsKey(SCConstants.TAG_ERROR_CODE)) {
                    errCode = (String) returnHashMap.get(SCConstants.TAG_ERROR_CODE);
                    body = (String) returnHashMap.get(SCConstants.TAG_ERROR);

                    title = getResources().getString(R.string.dialog_error_title);
                    SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);

//                    if (errCode.equals("0")) {
//                        title = getResources().getString(R.string.dialog_error_title);
//                        body = getResources().getString(R.string.dialog_register_wrong_email_format);
//                        SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
//                    } else if (errCode.equals("1")) {
//                        title = getResources().getString(R.string.dialog_error_title);
//                        body = getResources().getString(R.string.dialog_register_wrong_email_format);
//                        SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
//                    } else if (errCode.equals("2")) {
//                        title = getResources().getString(R.string.dialog_error_title);
//                        body = getResources().getString(R.string.dialog_register_failed_authenticate);
//                        SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
//                    } else if (errCode.equals("3")) {
//                        title = getResources().getString(R.string.dialog_error_title);
//                        body = getResources().getString(R.string.dialog_register_email_has_been_registered);
//                        SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
//                    }else if (errCode.equals("4")) {
//                        title = getResources().getString(R.string.dialog_error_title);
//                        body = getResources().getString(R.string.dialog_register_invalid_password2);
//                        SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
//                    }
                } else {
                    //save email and password for use
                    SCSharedPreferencesUtils.putString(SCMailRegistrationActivity.this, SCConstants.TAG_EMAIL,  mEtEmail.getText().toString().trim());
                    SCSharedPreferencesUtils.putString(SCMailRegistrationActivity.this, SCConstants.TAG_PASSWORD, mEtPassword.getText().toString().trim());
                    title = getResources().getString(R.string.dialog_title_register_success);
                    body = "「" + mEtEmail.getText().toString().trim() + "」" + String.format(getResources().getString(R.string.dialog_content_register_success),
                            mEtEmail.getText().toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle(title);
                    builder.setMessage(body);
                    builder.setNegativeButton(getResources().getString(R.string.dialog_button_register_success), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //requestLoginMail();
                        }
                    });
                    builder.show();

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

        mRequestAsync.execute();
    }


    public void test(View v){
       //go to webpage
    }
}
