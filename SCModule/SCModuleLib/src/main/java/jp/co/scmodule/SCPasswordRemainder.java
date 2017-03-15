package jp.co.scmodule;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.utils.CorrectSizeUtil;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;

public class SCPasswordRemainder extends Activity {
    private EditText mEtEmail;
    private CorrectSizeUtil mCorrectSize = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_remainder);
        mEtEmail = (EditText) findViewById(R.id.sc_login_et_email);
        //Init variable
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mCorrectSize.correctSize();
    }

    public void submitEmail(View v) {
        String dialogBody = null;
        String email = mEtEmail.getText().toString().trim();
        if (email.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_email_empty_label);
            SCGlobalUtils.showInfoDialog(this, null, dialogBody, null, null);
            return;
        } else if (!SCGlobalUtils.isEmailValid(mEtEmail.getText().toString())) {
            dialogBody = getResources().getString(R.string.dialog_body_email_invalid_label);
            SCGlobalUtils.showInfoDialog(this, null, dialogBody, null, null);
            return;
        }
        sendForgetPasswordLinkToTHeEmail();

    }

    private void sendForgetPasswordLinkToTHeEmail() {

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_EMAIL, mEtEmail.getText().toString().trim());
        params.put(SCConstants.PARAM_APPLICATION_ID, SCConstants.APPLICATION_ID_TADACOPY);
        SCRequestAsyncTask requestAsync = new SCRequestAsyncTask(SCPasswordRemainder.this, SCConstants.REQUEST_PASSWORD_REMINDER, params, new SCRequestAsyncTask.AsyncCallback() {

            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();
                Log.e("PasswordRM", result);
                try {
                    JSONObject jObj = new JSONObject(result);
                    String dialogBody = "";
                    if (jObj.getString("success").equals("true")) {
                        // Toast.makeText(SCPasswordRemainder.this, jObj.getString("data"), Toast.LENGTH_LONG).show();
                        String title = "パスワードの再設定メールを送信しました";
                        dialogBody = "入力したメールアドレス宛にパスワード再設定メールを送信しました。\n" +
                                "メール本文の再設定URLアクセスし設定を行った後アプリに戻りログインを行ってください。\n" +
                                "※迷惑メール対策などでドメイン指定受信をしている場合「@smart-campus.jp」を指定受信設定してください。";
                        SCGlobalUtils.showInfoDialog(SCPasswordRemainder.this, title, dialogBody, null, null);
                    } else {
                        //Toast.makeText(SCPasswordRemainder.this, jObj.getString("error"), Toast.LENGTH_LONG).show();
                        dialogBody = jObj.getString("error");
                        SCGlobalUtils.showInfoDialog(SCPasswordRemainder.this, null, dialogBody, null, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void progress() {
                SCGlobalUtils.showLoadingProgress(SCPasswordRemainder.this);
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
}
