package jp.co.scmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import jp.co.scmodule.utils.CorrectSizeUtil;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;

public class SCMultiLoginPage extends Activity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private Button btnFacebookLogin = null;
    private Button btnTwitterLogin = null;
    private Button btnLineLogin = null;
    private CorrectSizeUtil mCorrectSize = null;
    private ImageButton gotoWebPage = null;
    private TextView mTvEmailLogin = null;
    private Context mActivity;


    @Override
    protected void onDestroy() {
        // Utils.sActivityArr.remove(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        afterClickBack();
        super.onBackPressed();
    }

    private void afterClickBack() {
        SCGlobalUtils.ShowTutorial = false;
        Intent intent = new Intent();
        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            intent.setAction(SCConstants.ACTION_OPEN_TUTORIAL_TADACOPY);
        } else if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            intent.setAction(SCConstants.ACTION_OPEN_TUTORIAL_CANPASS);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scmulti_login_page);

        //Init variable
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        mActivity = this;

        //Init views
        btnFacebookLogin = (Button) findViewById(R.id.btn_facebook_login);
        btnTwitterLogin = (Button) findViewById(R.id.btn_twitter_login);
        btnLineLogin = (Button) findViewById(R.id.btn_line_login);
        gotoWebPage = (ImageButton) findViewById(R.id.imgbtn_gotowebpage);
        mTvEmailLogin = (TextView) findViewById(R.id.tv_email_login);

        //action
        btnTwitterLogin.setOnClickListener(this);
        btnLineLogin.setOnClickListener(this);
        btnFacebookLogin.setOnClickListener(this);
        gotoWebPage.setOnClickListener(this);
        mTvEmailLogin.setOnClickListener(this);
        mCorrectSize.correctSize();
    }


    public void go_register_email() {
        Intent i = new Intent(this, SCMailRegistrationActivity.class);
        this.startActivity(i);
        // overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

    }


    public void go_quest_mode() {
        Intent i = new Intent(this, SCEditInfoOneActivity.class);
        this.startActivity(i);
        overridePendingTransition(jp.co.scmodule.R.anim.anim_slide_in_right,
                jp.co.scmodule.R.anim.anim_slide_out_left);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_facebook_login) {
            login_with_facebook_from_app();
        } else if (i == R.id.btn_twitter_login) {
            login_with_twitter_from_app();
        } else if (i == R.id.tv_email_login) {
            go_register_email();
        } else if (i == R.id.btn_line_login) {
            login_with_line_from_app();
        }

    }

    private void login_with_line_from_app() {
        Intent intent = new Intent();
        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            intent.setAction(SCConstants.ACTION_OPEN_SNS_TADACOPY);
        } else if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            intent.setAction(SCConstants.ACTION_OPEN_SNS_CANPASS);
        }
        intent.putExtra("login_type", SCConstants.PROVIDER_LINE);
        startActivity(intent);
    }


    private void login_with_twitter_from_app() {
        Intent intent = new Intent();
        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            intent.setAction(SCConstants.ACTION_OPEN_SNS_TADACOPY);
        } else if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            intent.setAction(SCConstants.ACTION_OPEN_SNS_CANPASS);
        }
        intent.putExtra("login_type", SCConstants.PROVIDER_TWITTER);
        startActivity(intent);
    }


    private void login_with_facebook_from_app() {
        Intent intent = new Intent();
        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            intent.setAction(SCConstants.ACTION_OPEN_SNS_TADACOPY);
        } else if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            intent.setAction(SCConstants.ACTION_OPEN_SNS_CANPASS);
        }
        intent.putExtra("login_type", SCConstants.PROVIDER_FACEBOOK);
        startActivity(intent);
    }

    public void back(View v) {
        afterClickBack();

    }

}
