package jp.co.scmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.brickred.socialauth.util.Base64;
import org.json.JSONObject;

import java.util.HashMap;

import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.APICallBack;
import jp.co.scmodule.utils.APIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;
import jp.co.scmodule.utils.SCUrlConstants;

public class  SCUpdateMailActivity extends SCMyActivity {
    private static String TAG_LOG = "SCUpdateMailActivity";
    private Context mContext = null;
    private Activity mActivity = null;

    private EditText mEtEmail = null;
    private Button mBtnUpdate = null;
    private ImageButton mImgBtnBack = null;

    private OnClickListener mOnClickListener = null;

    private SCRequestAsyncTask mRequestAsync = null;

    private SCUserObject mUserObj = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SCGlobalUtils.sActivityArr.remove(this);
    }

    @Override
    public void onBackPressed() {
        afterClickBack();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mail);

        SCGlobalUtils.sActivityArr.add(this);

        init();
    }

    @Override
    protected void init() {
        super.init();

        mContext = this;
        mActivity = this;
        mUserObj = SCUserObject.getInstance();
    }

    @Override
    protected void initListeners() {
        mBtnUpdate.setContentDescription("update");
        mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("update")) {
                    afterClickUpdate();
                } else if (v.getContentDescription().equals("back")) {
                    afterClickBack();
                }
            }
        };
    }

    @Override
    protected void setListenerForViews() {
        mBtnUpdate.setOnClickListener(mOnClickListener);
        mImgBtnBack.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void findViewById() {
        mEtEmail = (EditText) findViewById(R.id.sc_update_email_et_email);
        mBtnUpdate = (Button) findViewById(R.id.sc_update_mail_btn_update);
        mImgBtnBack = (ImageButton) findViewById(R.id.sc_update_email_ibtn_back);
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

    private void afterClickUpdate() {
        String dialogBody = null;
        String title = getResources().getString(R.string.dialog_error_title);
        String email = mEtEmail.getText().toString().trim();

        if (email.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_email_empty_label);
            SCGlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
            return;
        } else if (!SCGlobalUtils.isEmailValid(mEtEmail.getText().toString())) {
            dialogBody = getResources().getString(R.string.dialog_body_email_invalid_label);
            SCGlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
            return;
        } else {
            String url = SCUrlConstants.URL_UPDATE_MAIL;
            String secretKey = SCConstants.SECRET_KEY;
            String date = String.valueOf(System.currentTimeMillis());
            String src = secretKey + mUserObj.getAppId() + date;

            String key = SCGlobalUtils.md5Hash(src);
            String app_id = mUserObj.getAppId();
            String current_email = mUserObj.getEmail();
            String new_email = mEtEmail.getText() + "";

            HashMap<String, String> param = new HashMap<String, String>();
            param.put(SCConstants.PARAM_KEY, key);
            param.put(SCConstants.PARAM_DATE, date);
            param.put(SCConstants.PARAM_APP_ID, app_id);
            param.put(SCConstants.PARAM_EMAIL, current_email);
            param.put(SCConstants.PARAM_UPDATE_EMAIL, new_email);
            SCGlobalUtils.addAditionalHeader = true ;
            SCGlobalUtils.additionalHeaderTag = "Authorization";
            SCGlobalUtils.additionalHeaderValue = "Bearer "+getBase64(SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_ACCESS_TOKEN, null));

            APIUtils.LoadJSON(this, param, url, new APICallBack() {
                @Override
                public void uiStart() {
                    SCGlobalUtils.showLoadingProgress(mContext);
                }

                @Override
                public void success(String successString, int type) {
                    Log.e("successString", successString);
                    try {
                        JSONObject successObj = new JSONObject(successString);
                        String success = null;
                        String errorCode = null;

                        if (successObj.has(SCConstants.TAG_SUCCESS)) {
                            success = successObj.getString(SCConstants.TAG_SUCCESS);
                        }

                        if (successObj.has(SCConstants.TAG_ERROR_CODE)) {
                            errorCode = successObj.getString(SCConstants.TAG_ERROR_CODE);
                        }

                        if (success.equals("true")) {
                            SCSharedPreferencesUtils.clearPreference(SCUpdateMailActivity.this);
                            SCUserObject.resetInstant();
                           // Intent intent = new Intent();
                           // if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
                              //  intent.setAction(SCConstants.ACTION_OPEN_LOGIN_TADACOPY);
//                            } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
//                                intent.setAction(SCConstants.ACTION_OPEN_LOGIN_CANPASS);
//                            }
                          //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            //SCUpdateMailActivity.this.startActivity(intent);
                            Intent intent = new Intent(SCUpdateMailActivity.this,Login_mail.class);
                            intent.putExtra("updated_mail",mEtEmail.getText() + "");
                            startActivity(intent);
                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_right);
                            SCUpdateMailActivity.this.finish();
                            overridePendingTransition(R.anim.anim_slide_in_left,
                                    R.anim.anim_slide_out_right);

                        } else if (errorCode != null) {
                            final String errCode = errorCode;
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String dialogBody = null;
                                    String title = getResources().getString(R.string.dialog_error_title);
                                    if (errCode.equals("1")) {
                                        dialogBody = getResources().getString(R.string.dialog_body_email_invalid_label);
                                    } else if (errCode.equals("2")) {
                                        dialogBody = getResources().getString(R.string.dialog_change_email_fail);
                                    } else if (errCode.equals("3")) {
                                        dialogBody = getResources().getString(R.string.dialog_email_was_registed);
                                    }

                                    if (dialogBody != null) {
                                        SCGlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
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

    }

    private void afterClickBack() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }
    public String getBase64(final String input) {
        return Base64.encodeBytes(input.getBytes(), Base64.DONT_BREAK_LINES);
    }


}
