package jp.co.scmodule;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.SCGenderObject;
import jp.co.scmodule.objects.SCPrefectureObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;


public class SCEditInfoTwoActivity extends SCMyActivity {
    private static final String TAG_LOG = "SCEditInfoTwoActivity";
    private Context mContext = null;
    private Activity mActivity = null;

    private SCRequestAsyncTask mRequestAsync = null;

    private View.OnClickListener mOnClickListener = null;
    private DatePickerDialog mBirthdayPickerDialog = null;

    private Button mBtnGender = null;
    private Button mBtnBirthday = null;
    private Button mBtnPrefecture = null;
    private Button mBtnNext = null;
    private Button mBtnSkip = null;
    private EditText mEtZip1 = null;
    private EditText mEtZip2 = null;
    private EditText mEtAddress = null;
    private EditText mEtTel = null;

    private SCGenderObject mGenderObj = null;
    private SCPrefectureObject mPrefectureObj = null;

    private SCUserObject mUserObj = null;

    private String mBirthday = null;

    private int mCodeType = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SCGlobalUtils.sActivityArr.remove(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED) {
            return;
        }

        if(requestCode == SCConstants.TYPE_GENDER) {
            mGenderObj = data.getParcelableExtra(SCGenderObject.class.toString());
            mBtnGender.setText(mGenderObj.getName());
        }

        if(requestCode == SCConstants.TYPE_PREFECTURE) {
            mPrefectureObj = data.getParcelableExtra(SCPrefectureObject.class.toString());
            mBtnPrefecture.setText(mPrefectureObj.getName());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        initGlobalUtils();
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
        setContentView(R.layout.activity_edit_info_two);

        SCGlobalUtils.sActivityArr.add(this);

        try {
            mUserObj = getIntent().getParcelableExtra(SCUserObject.class.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mCodeType = getIntent().getIntExtra(Integer.class.toString(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
    }

    @Override
    protected void init() {
        super.init();

        mContext = this;
        mActivity = this;

        showInfo();

        initBirthdayDialog();
    }

    @Override
    protected void findViewById() {
        mBtnGender = (Button) findViewById(R.id.edit_info_two_btn_gender);
        mBtnBirthday = (Button) findViewById(R.id.edit_info_two_btn_birthday);
        mBtnPrefecture = (Button) findViewById(R.id.edit_info_two_btn_prefecture);
        mBtnNext = (Button) findViewById(R.id.edit_info_two_btn_next);
        mBtnSkip = (Button) findViewById(R.id.edit_info_two_btn_skip);
        mEtZip1 = (EditText) findViewById(R.id.edit_info_two_et_zip_1);
        mEtZip2 = (EditText) findViewById(R.id.edit_info_two_et_zip_2);
        mEtAddress = (EditText) findViewById(R.id.edit_info_two_et_address);
        mEtTel = (EditText) findViewById(R.id.edit_info_two_et_tel);
    }

    @Override
    protected void initListeners() {
        mBtnGender.setContentDescription("gender");
        mBtnBirthday.setContentDescription("birthday");
        mBtnPrefecture.setContentDescription("prefecture");
        mBtnNext.setContentDescription("next");
        mBtnSkip.setContentDescription("skip");

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getContentDescription() == null) {
                    return;
                } else if(v.getContentDescription().equals("gender")) {
                    afterClickGender();
                } else if(v.getContentDescription().equals("birthday")) {
                    afterClickBirthday();
                } else if(v.getContentDescription().equals("prefecture")) {
                    afterClickPrefecture();
                } else if(v.getContentDescription().equals("next")) {
                    afterClickNext();
                } else if(v.getContentDescription().equals("skip")) {
                    afterClickSkip();
                }
            }
        };
    }

    @Override
    protected void setListenerForViews() {
        mBtnGender.setOnClickListener(mOnClickListener);
        mBtnBirthday.setOnClickListener(mOnClickListener);
        mBtnPrefecture.setOnClickListener(mOnClickListener);
        mBtnNext.setOnClickListener(mOnClickListener);
        mBtnSkip.setOnClickListener(mOnClickListener);
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


    private void afterClickGender() {
        moveToChoosenActivity(SCConstants.TYPE_GENDER);
    }

    private void afterClickBirthday() {
        mBirthdayPickerDialog.show();
    }

    private void afterClickPrefecture() {
        moveToChoosenActivity(SCConstants.TYPE_PREFECTURE);
    }

    private void afterClickNext() {
        if((mEtZip1.getText().toString().length() + mEtZip2.getText().toString().length()) != 7
                && (mEtZip1.getText().toString().length() + mEtZip2.getText().toString().length()) != 0 ) {
            String title = getResources().getString(R.string.dialog_error_title);
            String body = getResources().getString(R.string.dialog_body_wrong_zipcode);
            SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
            return;
        }
        requestUpdateUser();
    }

    private void afterClickSkip() {
        if(mCodeType == SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM
                || mCodeType == SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM
                || mCodeType == SCConstants.CODE_LOGIN_FOR_FOLLOW_SHOP) {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
        } else {
            Intent intent = new Intent();
            if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
                intent.setAction(SCConstants.ACTION_OPEN_CONTENT_TADACOPY);
            } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
                intent.setAction(SCConstants.ACTION_OPEN_CONTENT_CANPASS);
            }
            intent.putExtra(SCUserObject.class.toString(), mUserObj);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
        }
    }

    private void moveToChoosenActivity(int type) {
        Intent intent = new Intent(this, SCChoosenActivity.class);
        intent.putExtra(Integer.class.toString(), type);
        startActivityForResult(intent, type);
        overridePendingTransition(R.anim.anim_slide_in_bottom,
                R.anim.anim_slide_out_bottom);
    }

    private void initBirthdayDialog() {
        Calendar newCalendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.JAPAN);
        mBirthdayPickerDialog = new DatePickerDialog(this, R.style.CustomDialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mBirthday = dateFormatter.format(newDate.getTime());

                String birthdayStr = String.format(getResources().getString(R.string.common_date), dayOfMonth, String.valueOf(monthOfYear + 1), year);
                mBtnBirthday.setText(birthdayStr);
            }

        },  (newCalendar.get(Calendar.YEAR) - 18), 0, 1);

        String title = getResources().getString(R.string.edit_info_two_birthday_dialog_title);
        mBirthdayPickerDialog.setTitle(title);
    }

    private void afterClickBack() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }

    private void requestUpdateUser() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();
        String agent = SCConstants.AGENT;

        String src = secretKey + appId + date;
        //String src = secretKey + "134Dh" + "1435726129";
        String key = SCGlobalUtils.md5Hash(src);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_KEY, key);
        params.put(SCConstants.PARAM_APP_ID, mUserObj.getAppId());
        params.put(SCConstants.PARAM_EMAIL, mUserObj.getEmail());
        params.put(SCConstants.PARAM_NICKNAME, mUserObj.getNickname());
        params.put(SCConstants.PARAM_UNIV_ID, mUserObj.getUniversityId());
        params.put(SCConstants.PARAM_DEPARTMENT_ID, mUserObj.getDepartmentId());
        params.put(SCConstants.PARAM_CAMPUS_ID, mUserObj.getCampusId());
        params.put(SCConstants.PARAM_MAJOR_ID, mUserObj.getMajorId());
        params.put(SCConstants.PARAM_ENROLLMENT_YEAR, mUserObj.getEnrollmentYear());
        params.put(SCConstants.PARAM_DATE, date);
        params.put(SCConstants.PARAM_AGENT, agent);
        if(!mBtnGender.getText().toString().equals(getResources().getString(R.string.edit_info_two_gender))) {
            params.put(SCConstants.PARAM_SEX, (mBtnGender.getText().equals(
                    Arrays.asList(getResources().getStringArray(R.array.gender_array)).get(0))) ? SCConstants.MALE : SCConstants.FEMALE);
        }

        if(!mBtnBirthday.getText().toString().equals(getResources().getString(R.string.edit_info_two_birthday))) {
            if(mBirthday != null)
            params.put(SCConstants.PARAM_BIRTHDAY, mBirthday);
        }

        if(!mEtZip1.getText().toString().equals("") && !mEtZip2.getText().toString().equals("")) {
            params.put(SCConstants.PARAM_POST_CODE, mEtZip1.getText().toString() + "-" + mEtZip2.getText().toString());
        }

        if(mPrefectureObj != null) {
            params.put(SCConstants.PARAM_PREFECTURE_ID, mPrefectureObj.getId());
        }

        if(!mEtTel.getText().toString().equals("")) {
            params.put(SCConstants.PARAM_PHONE_NUMBER, mEtTel.getText().toString());
        }

        if(!mEtAddress.getText().toString().equals("")) {
            params.put(SCConstants.PARAM_ADDRESS, mEtAddress.getText().toString());
        }

        mRequestAsync = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_UPDATE_USER, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);
                SCGlobalUtils.dismissLoadingProgress();

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_UPDATE_USER, result);
                SCUserObject userObj = null;
                String errCode = null;

                String title = null;
                String body = null;
                if(returnHashMap.containsKey(SCConstants.TAG_APP_USER)) {
                    userObj = (SCUserObject)returnHashMap.get(SCConstants.TAG_APP_USER);
                    userObj.setAppId(mUserObj.getAppId());
                    mUserObj = userObj;
                }

                if(returnHashMap.containsKey(SCConstants.TAG_ERROR_CODE)) {
                    return;
                }

                if(mCodeType == SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM
                        || mCodeType == SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM
                        || mCodeType == SCConstants.CODE_LOGIN_FOR_FOLLOW_SHOP) {
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_left);
                } else {
                    Intent intent = new Intent();
                    if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
                        intent.setAction(SCConstants.ACTION_OPEN_CONTENT_TADACOPY);
                    } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
                        intent.setAction(SCConstants.ACTION_OPEN_CONTENT_CANPASS);
                    }
                    intent.putExtra(SCUserObject.class.toString(), mUserObj);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_left);
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

    private void showInfo() {
        if(!mUserObj.getSex().equals("")) {
            if(mUserObj.getSex().equals(SCConstants.MALE)) {
                mBtnGender.setText(
                        Arrays.asList(getResources().getStringArray(R.array.gender_array)).get(0));
            } else {
                mBtnGender.setText(
                        Arrays.asList(getResources().getStringArray(R.array.gender_array)).get(1));
            }
        }

        if(!mUserObj.getBirthday().equals("")) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date birthday = format.parse(mUserObj.getBirthday());
                String birthdayStr = String.format(getResources().getString(R.string.common_date), DateFormat.format("dd", birthday), DateFormat.format("MM", birthday), DateFormat.format("yyyy", birthday));
                mBtnBirthday.setText(birthdayStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if(!mUserObj.getPostCode().equals("")) {
            String[] zipcode = mUserObj.getPostCode().split("-");
            String zip1 = zipcode[0];
            String zip2 = zipcode[1];
            mEtZip1.setText(zip1);
            mEtZip2.setText(zip2);
        }

        if(!mUserObj.getPrefecture().equals("")) {
            mBtnPrefecture.setText(mUserObj.getPrefecture());
        }

        if(!mUserObj.getPhoneNumber().equals("")) {
            mEtTel.setText(mUserObj.getPhoneNumber());
        }

        if(!mUserObj.getAddress().equals("")) {
            mEtAddress.setText(mUserObj.getAddress());
        }

    }
}
