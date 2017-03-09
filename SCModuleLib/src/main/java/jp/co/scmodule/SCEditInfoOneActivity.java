package jp.co.scmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.SCDepartmentObject;
import jp.co.scmodule.objects.SCEnrollmentObject;
import jp.co.scmodule.objects.SCGenderObject;
import jp.co.scmodule.objects.SCMajorObject;
import jp.co.scmodule.objects.SCUniversityObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;


public class SCEditInfoOneActivity extends SCMyActivity {
    private static final String TAG_LOG = "SCEditInfoOneActivity";
    private Context mContext = null;
    private Activity mActivity = null;

    private OnClickListener mOnClickListener = null;
    private SCRequestAsyncTask mRequestAsync = null;
    private Button mBtnGender = null;
    private EditText mEtName = null;
    private Button mBtnUniversity = null;
    private Button mBtnDepartment = null;
    private Button mBtnMajor = null;
    private Button mBtnEnrollment = null;
    private Button mBtnNext = null;

    private SCUserObject mUserObj = null;

    private SCUniversityObject mUniversityObj = null;
    private SCDepartmentObject mDepartmentObj = null;
    private SCMajorObject mMajorObj = null;
    private SCEnrollmentObject mEnrollmentObj = null;
    private SCGenderObject mGenderObj = null;
    private int mCodeType = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SCGlobalUtils.sActivityArr.remove(this);
    }

    @Override
    protected void onRestart() {
        initGlobalUtils();
        super.onRestart();
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

        if(requestCode == SCConstants.TYPE_UNIVERSITY) {
            mUniversityObj = data.getParcelableExtra(SCUniversityObject.class.toString());
            mBtnUniversity.setText(mUniversityObj.getName());

            mDepartmentObj = null;
            mBtnDepartment.setText(getResources().getText(R.string.edit_info_one_department));

            mMajorObj = null;
            mBtnMajor.setText(getResources().getText(R.string.edit_info_one_major));

        } else if (requestCode == SCConstants.TYPE_DEPARTMENT) {
            mDepartmentObj = data.getParcelableExtra(SCDepartmentObject.class.toString());
            mBtnDepartment.setText(mDepartmentObj.getName());
            if(mDepartmentObj.getHaveMajor().equals("false")) {
                mBtnMajor.setTextColor(getResources().getColor(android.R.color.darker_gray));
                mBtnMajor.setClickable(false);
                mBtnMajor.setSelected(true);
            } else {
                mBtnMajor.setTextColor(getResources().getColor(android.R.color.white));
                mBtnMajor.setClickable(true);
                mBtnMajor.setSelected(false);
            }
            mMajorObj = null;
            mBtnMajor.setText(getResources().getText(R.string.edit_info_one_major));
        } else if (requestCode == SCConstants.TYPE_MAJOR) {
            mMajorObj = data.getParcelableExtra(SCMajorObject.class.toString());
            mBtnMajor.setText(mMajorObj.getName());
        } else if (requestCode == SCConstants.TYPE_ENROLLMENT) {
            mEnrollmentObj = data.getParcelableExtra(SCEnrollmentObject.class.toString());
            mBtnEnrollment.setText(mEnrollmentObj.getName() + getResources().getString(R.string.common_year));
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
       afterClickBack();
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info_one);

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

        if(mUserObj != null) {
            showInfo();
        }
    }
    //filtering emojis
    public static InputFilter getEditTextFilterEmoji()
    {
        return new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                CharSequence sourceOriginal = source;
                source = replaceEmoji(source);
                end = source.toString().length();

                if (end == 0) return ""; //Return empty string if the input character is already removed

                if (! sourceOriginal.toString().equals(source.toString()))
                {
                    char[] v = new char[end - start];
                    TextUtils.getChars(source, start, end, v, 0);

                    String s = new String(v);

                    if (source instanceof Spanned)
                    {
                        SpannableString sp = new SpannableString(s);
                        TextUtils.copySpansFrom((Spanned) source, start, end, null, sp, 0);
                        return sp;
                    }
                    else
                    {
                        return s;
                    }
                }
                else
                {
                    return null; // keep original
                }
            }

            private String replaceEmoji(CharSequence source)
            {

                String notAllowedCharactersRegex = "[^a-zA-Z0-9@#\\$%\\&\\-\\+\\(\\)\\*;:!\\?\\~`£\\{\\}\\[\\]=\\.,_/\\\\\\s'\\\"<>\\^\\|÷×]";
                return source.toString()
                        .replaceAll(notAllowedCharactersRegex, "");
            }

        };
    }

    @Override
    protected void findViewById() {
        mEtName = (EditText) findViewById(R.id.edit_info_one_et_name);
        //disable emojis
//        InputFilter[] filterArray = new InputFilter[] {getEditTextFilterEmoji()};
//        mEtName.setFilters(filterArray);

        mBtnUniversity = (Button) findViewById(R.id.edit_info_one_btn_university);
        mBtnDepartment = (Button) findViewById(R.id.edit_info_one_btn_department);
        mBtnMajor = (Button) findViewById(R.id.edit_info_one_btn_major);
        mBtnEnrollment = (Button) findViewById(R.id.edit_info_one_btn_enrollment);
        mBtnNext = (Button) findViewById(R.id.edit_info_one_btn_next);

        mBtnGender = (Button) findViewById(R.id.edit_info_two_btn_gender);
    }

    @Override
    protected void initListeners() {
        mBtnGender.setContentDescription("gender");
        mBtnUniversity.setContentDescription("university");
        mBtnMajor.setContentDescription("major");
        mBtnDepartment.setContentDescription("department");
        mBtnEnrollment.setContentDescription("enrollment");
        mBtnNext.setContentDescription("next");

        mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getContentDescription() == null) {
                    return;
                } else if(v.getContentDescription().equals("university")) {
                    afterClickUniversity();
                } else if(v.getContentDescription().equals("department")) {
                    afterClickDepartment();
                } else if(v.getContentDescription().equals("major")) {
                    afterClickMajor();
                } else if(v.getContentDescription().equals("enrollment")) {
                    afterClickEnrollment();
                } else if(v.getContentDescription().equals("next")) {
                    afterClickNext();
                }else if(v.getContentDescription().equals("gender")) {
                    afterClickGender();
                }
            }
        };
    }

    private void afterClickGender() {
        moveToChoosenActivity(SCConstants.TYPE_GENDER);
    }

    @Override
    protected void setListenerForViews() {
        mBtnUniversity.setOnClickListener(mOnClickListener);
        mBtnMajor.setOnClickListener(mOnClickListener);
        mBtnDepartment.setOnClickListener(mOnClickListener);
        mBtnEnrollment.setOnClickListener(mOnClickListener);
        mBtnNext.setOnClickListener(mOnClickListener);
        mBtnGender.setOnClickListener(mOnClickListener);

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

    private void afterClickUniversity() {
        moveToChoosenActivity(SCConstants.TYPE_UNIVERSITY);
    }

    private void afterClickMajor() {
        moveToChoosenActivity(SCConstants.TYPE_MAJOR);
    }

    private void afterClickDepartment() {
        moveToChoosenActivity(SCConstants.TYPE_DEPARTMENT);
    }

    private void afterClickEnrollment() {
        moveToChoosenActivity(SCConstants.TYPE_ENROLLMENT);
    }

    private void afterClickNext() {
        String dialogBody = null;
        if(mEtName.getText().toString().trim().equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_empty_nickname);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        } else if(mUniversityObj == null) {
            dialogBody = getResources().getString(R.string.dialog_body_empty_university);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        } else if(mDepartmentObj == null) {
            dialogBody = getResources().getString(R.string.dialog_body_empty_department);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        }
//        else if(mMajorObj == null) {
//            dialogBody = getResources().getString(R.string.dialog_body_empty_major);
//            SCGlobalUtils.showInfoDialog(null, dialogBody, null);
//            return;
//        }
        else if(mEnrollmentObj == null) {
            dialogBody = getResources().getString(R.string.dialog_body_empty_enrollment);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        }

        if(mUserObj == null) {
            requestRegisterUserStageOne();
        } else {
            requestUpdateUser();
        }
    }

    private void moveToChoosenActivity(int type) {
        String dialogBody = null;

        Intent intent = new Intent(this, SCChoosenActivity.class);
        switch (type) {
            case SCConstants.TYPE_DEPARTMENT:
                if(mUniversityObj == null) {
                    dialogBody = getResources().getString(R.string.dialog_body_empty_university);
                    SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
                    return;
                } else {
                    intent.putExtra(SCUniversityObject.class.toString(), mUniversityObj);
                }
                break;

            case SCConstants.TYPE_MAJOR:
                if(mDepartmentObj == null) {
                    dialogBody = getResources().getString(R.string.dialog_body_empty_department);
                    SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
                    return;
                } else {
                    intent.putExtra(SCDepartmentObject.class.toString(), mDepartmentObj);
                }
                break;
        }

        intent.putExtra(Integer.class.toString(), type);
        startActivityForResult(intent, type);
        overridePendingTransition(R.anim.anim_slide_in_bottom,
                R.anim.anim_slide_out_bottom);
    }

    private void afterClickBack() {

        if(mUserObj.getIsGuest().equals("true")){
            callLogoutAPI();
            Log.e("Guest","This is a guest user,So Logout");
        }else {
//            finish();
//            overridePendingTransition(R.anim.anim_slide_in_left,
//                    R.anim.anim_slide_out_right);
        }
    }

    private void callLogoutAPI() {

        HashMap<String, Object> params = new HashMap<String, Object>();
        SCGlobalUtils.showLoadingProgress(SCEditInfoOneActivity.this);
        SCRequestAsyncTask requestAsync = new SCRequestAsyncTask(SCEditInfoOneActivity.this,SCConstants.REQUEST_LOGOUT,params,new SCRequestAsyncTask.AsyncCallback() {

            @Override
            public void done(String result) {
                Log.e("SplashActivity", result);

                SCGlobalUtils.dismissLoadingProgress();
                try {
                    JSONObject jObj = new JSONObject(result);
                    if(jObj.getString("success").equals("true")){
                        if (SCSharedPreferencesUtils.getString(SCEditInfoOneActivity.this, SCConstants.TAG_LOGIN_TYPE, null).equals(SCConstants.PROVIDER_GUEST)) {
                            SCSharedPreferencesUtils.putBoolean(SCEditInfoOneActivity.this, SCConstants.TAG_USED_GUEST_ONCE, true);
                        }
                        SCSharedPreferencesUtils.removeComponent(SCEditInfoOneActivity.this, SCConstants.TAG_APP_ID);
                        SCSharedPreferencesUtils.removeComponent(SCEditInfoOneActivity.this, SCConstants.TAG_LOGIN_TYPE);
                        SCSharedPreferencesUtils.removeComponent(SCEditInfoOneActivity.this, SCConstants.TAG_ACCESS_TOKEN);
                        SCSharedPreferencesUtils.removeComponent(SCEditInfoOneActivity.this, SCConstants.TAG_REFRESH_TOKEN);
                        SCSharedPreferencesUtils.removeComponent(SCEditInfoOneActivity.this, SCConstants.CODE);
                        SCSharedPreferencesUtils.removeComponent(SCEditInfoOneActivity.this, SCConstants.TIME);
                        SCSharedPreferencesUtils.removeComponent(SCEditInfoOneActivity.this, SCConstants.TAG_NOTIFICATION);
                        SCGlobalUtils.tc_device_available_in_campus =false;

                        SCUserObject.resetInstant();
                        if(SCGlobalUtils.ishashpass.equals("1")){
                            SCGlobalUtils.ishashpass = "0";
                            SCSharedPreferencesUtils.putString(SCEditInfoOneActivity.this, SCConstants.TAG_PASSWORD, "");
                        }
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left,
                                R.anim.anim_slide_out_right);
                    }else{
                        Toast.makeText(SCEditInfoOneActivity.this, jObj.getString("error"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void progress() {

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

    private void requestUpdateUser() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();
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

        String src = secretKey + appId + date;
        //String src = secretKey + "134Dh" + "1435726129";
        String key = SCGlobalUtils.md5Hash(src);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_KEY, key);
        params.put(SCConstants.PARAM_APP_ID, appId);
        params.put(SCConstants.PARAM_NICKNAME, mEtName.getText().toString().trim());
        params.put(SCConstants.PARAM_UNIV_ID, mUniversityObj.getId());
        params.put(SCConstants.PARAM_DEPARTMENT_ID, mDepartmentObj.getId());
        params.put(SCConstants.PARAM_CAMPUS_ID, mUniversityObj.getmCampusObj().getId());
        params.put(SCConstants.PARAM_MAJOR_ID, (mMajorObj == null) ? "0" : mMajorObj.getId());
        params.put(SCConstants.PARAM_ENROLLMENT_YEAR, mEnrollmentObj.getName());
        params.put(SCConstants.PARAM_DATE, date);
        params.put(SCConstants.PARAM_AGENT, agent);
        params.put(SCConstants.PARAM_DEVICE_ID, uuid);

        if(!mBtnGender.getText().toString().equals(getResources().getString(R.string.edit_info_two_gender))) {
            params.put(SCConstants.PARAM_SEX, (mBtnGender.getText().equals(
                    Arrays.asList(getResources().getStringArray(R.array.gender_array)).get(0))) ? SCConstants.MALE : SCConstants.FEMALE);
        }else{
            params.put(SCConstants.PARAM_SEX,"0");
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

//                Intent intent = new Intent(mContext, SCEditInfoTwoActivity.class);
//                intent.putExtra(SCUserObject.class.toString(), mUserObj);
//                if(mCodeType == SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM
//                        || mCodeType == SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM
//                        || mCodeType == SCConstants.CODE_LOGIN_FOR_FOLLOW_SHOP) {
//                    intent.putExtra(Integer.class.toString(), mCodeType);
//                }
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.anim_slide_in_right,
//                        R.anim.anim_slide_out_left);

                if(mCodeType == SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM
                        || mCodeType == SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM
                        || mCodeType == SCConstants.CODE_LOGIN_FOR_FOLLOW_SHOP) {
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_left);
                } else {
                    Intent intent = new Intent();
                   // if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
                        intent.setAction(SCConstants.ACTION_OPEN_CONTENT_TADACOPY);
//                    } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
//                        intent.setAction(SCConstants.ACTION_OPEN_CONTENT_CANPASS);
//                    }
                    //intent.putExtra(SCUserObject.class.toString(), mUserObj);
                    SCUserObject.updateInstance(mUserObj);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_left);
                    finish();
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

    private void requestRegisterUserStageOne() {
        String secretKey = SCConstants.SECRET_KEY;
        String nickname = mEtName.getText().toString();
        String universityId = mUniversityObj.getId();
        String campusId = mUniversityObj.getmCampusObj().getId(); //temp
        String departmentId = mDepartmentObj.getId();
        String majorId = (mMajorObj == null) ? "0" : mMajorObj.getId();
        String enrollmentYear = mEnrollmentObj.getName();
        String date = String.valueOf(System.currentTimeMillis());
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
        String agent = SCConstants.AGENT;
        String applicationId = "";
       // if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
            applicationId = SCConstants.APP_ID_TADACOPY;
//        } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
//            applicationId = SCConstants.APP_ID_CANPASS;
//        }

        String src = secretKey + universityId + campusId + departmentId + majorId + enrollmentYear + date;
        String key = SCGlobalUtils.md5Hash(src);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_KEY, key);
        params.put(SCConstants.PARAM_NICKNAME, nickname);
        params.put(SCConstants.PARAM_UNIV_ID, universityId);
        params.put(SCConstants.PARAM_DEPARTMENT_ID, departmentId);
        params.put(SCConstants.PARAM_CAMPUS_ID, campusId);
        params.put(SCConstants.PARAM_MAJOR_ID, majorId);
        params.put(SCConstants.PARAM_ENROLLMENT_YEAR, enrollmentYear);
        params.put(SCConstants.PARAM_DATE, date);
        params.put(SCConstants.PARAM_APPLICATION_ID, applicationId);
        params.put(SCConstants.PARAM_DEVICE_ID, uuid);
        params.put(SCConstants.PARAM_AGENT, agent);

        if(!mBtnGender.getText().toString().equals(getResources().getString(R.string.edit_info_two_gender))) {
            params.put(SCConstants.PARAM_SEX, (mBtnGender.getText().equals(
                    Arrays.asList(getResources().getStringArray(R.array.gender_array)).get(0))) ? SCConstants.MALE : SCConstants.FEMALE);
        }else{
            params.put(SCConstants.PARAM_SEX,"0");
        }

        mRequestAsync = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_REGISTER_USER, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);
                SCGlobalUtils.dismissLoadingProgress();

                try {
                    String appId = (String) SCAPIUtils.parseJSON(SCConstants.REQUEST_REGISTER_USER, result).get(SCConstants.TAG_APP_ID);
                    if(appId != null) {
                        //Toast.makeText(mContext, appId, Toast.LENGTH_SHORT).show();
                        mUserObj = SCUserObject.getInstance();
                        mUserObj.setNickname(mEtName.getText().toString());
                        mUserObj.setUniversityName(mUniversityObj.getName());
                        mUserObj.setUniversityId(mUniversityObj.getId());
                        mUserObj.setDepartmentName(mDepartmentObj.getName());
                        mUserObj.setDepartmentId(mDepartmentObj.getId());
                        mUserObj.setMajorId((mMajorObj != null) ? mMajorObj.getId() : "0");
                        mUserObj.setMajorName((mMajorObj != null) ? mMajorObj.getName() : "");
                        mUserObj.setCampusId(mUniversityObj.getmCampusObj().getId());
                        mUserObj.setEnrollmentYear(mEnrollmentObj.getName());
                        mUserObj.setAppId(appId);
                        mUserObj.setIsGuest("true");



                        // save app_id into shared preference
                        SCSharedPreferencesUtils.putString(mContext, SCConstants.TAG_APP_ID, mUserObj.getAppId());
                        SCSharedPreferencesUtils.putString(mContext, SCConstants.TAG_LOGIN_TYPE, SCConstants.PROVIDER_GUEST);

                        Intent intent = new Intent();
                       // if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
                            intent.setAction(SCConstants.ACTION_OPEN_CONTENT_TADACOPY);
//                        } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
//                            intent.setAction(SCConstants.ACTION_OPEN_CONTENT_CANPASS);
//                        }
                        intent.putExtra(SCUserObject.class.toString(), mUserObj);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_right,
                                R.anim.anim_slide_out_left);
                    }
                }catch (Exception e) {
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

        mRequestAsync.execute();
    }

    private void showInfo() {
        if(!mUserObj.getSex().equals("")) {
            if(mUserObj.getSex().equals(SCConstants.MALE)) {
                mBtnGender.setText(
                        Arrays.asList(getResources().getStringArray(R.array.gender_array)).get(0));
            } else if(mUserObj.getSex().equals(SCConstants.FEMALE)) {
                mBtnGender.setText(
                        Arrays.asList(getResources().getStringArray(R.array.gender_array)).get(1));
            }else {
                mBtnGender.setText(R.string.edit_info_two_gender);
            }
        }
        mEtName.setText(mUserObj.getNickname());
    }
}
