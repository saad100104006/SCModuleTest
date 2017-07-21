package jp.co.scmodule;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devsmart.android.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.interfaces.SCDialogCallback;
import jp.co.scmodule.objects.SCCampusObject;
import jp.co.scmodule.objects.SCDepartmentObject;
import jp.co.scmodule.objects.SCEnrollmentObject;
import jp.co.scmodule.objects.SCGenderObject;
import jp.co.scmodule.objects.SCMajorObject;
import jp.co.scmodule.objects.SCPrefectureObject;
import jp.co.scmodule.objects.SCUniversityObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCImageUtils;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;
import jp.co.scmodule.widgets.SCCircleImageView;


public class SCProfileInfoActivity extends SCMyActivity {
    private static final String TAG_LOG = "SCProfileInfoActivity";
    private Context mContext = null;
    private Activity mActivity = null;
    private View DialogView1;
    Dialog mDialog_tut1 = null;
    private DatePickerDialog mBirthdayPickerDialog = null;

    private View.OnClickListener mOnClickListener = null;
    private TextWatcher mTextWatcher = null;

    private SCRequestAsyncTask mRequestAsync = null;

    private Button mBtnCancel = null;
    private Button mBtnSave = null;
    private Button mBtnEdit = null;
    private ImageButton mIbtnBack = null;
    private SCCircleImageView mImgAvatar = null;
    private TextView mTvName = null;
    private TextView mTvEmail = null;
    private EditText mEtNickname = null;
    private TextView mTvUniversity = null;
    private TextView mTvDepartment = null;
    private TextView mTvMajor = null;
    private TextView mTvEnrollment = null;
    private TextView mTvGender = null;
    private TextView mTvBirthday = null;
    private EditText mEtPostcode = null;
    private TextView mTvPrefecture = null;
    private EditText mEtAddress = null;
    private EditText mEtPhone = null;
    private LinearLayout mLlEmail = null;

    private SCUserObject mUserObj = null;

    private SCUniversityObject mUniversityObj = null;
    private SCDepartmentObject mDepartmentObj = null;
    private SCMajorObject mMajorObj = null;
    private SCEnrollmentObject mEnrollmentObj = null;
    private SCGenderObject mGenderObj = null;
    private SCPrefectureObject mPrefectureObj = null;

    private boolean mIsRemovingText = false;

    private Bitmap mBmAvatar = null;

    private String mBirthDay = null;

    private LinearLayout mLlnotguest = null;
    private FrameLayout mFmguest = null;
    private Button mBtnregister = null;

    private View DialogView;
    Dialog mDialog = null;
    private String old_point = null;
    private String current_point = null;
    private String get_point_for_update = null;
    private TextView header_label = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SCGlobalUtils.sActivityArr.remove(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == SCConstants.TYPE_AVATAR) {
            InputStream stream = null;
            Bitmap bitmap = null;

            try {
                Uri uri = data.getData();
                Bundle bundle = data.getExtras();
                if (uri == null) {
                    // case nexus device
                    Bitmap imageBitmap = (Bitmap) bundle.get("data");
                    // mImgProfile.setImageBitmap(imageBitmap);
                    bitmap = imageBitmap;
                } else {
                    stream = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);

                    String absPath = SCImageUtils.getPath(mContext, uri);
                    bitmap = SCImageUtils.rotateBitmap(bitmap, Uri.parse(absPath));
                }

                // thumb bitmap
                int bmHeight = bitmap.getHeight();
                int bmWith = bitmap.getWidth();

                float ratio = bmWith * 1.0f / bmHeight;

                Bitmap thumbBitmap = SCImageUtils.getBitmapThumb(bitmap, 200, Math.round(200 / ratio));
                // set image to views
                mBmAvatar = thumbBitmap;
                mImgAvatar.setImageBitmap(thumbBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == SCConstants.TYPE_UNIVERSITY) {
            mUniversityObj = data.getParcelableExtra(SCUniversityObject.class.toString());
            mTvUniversity.setText(mUniversityObj.getName());

            mDepartmentObj = null;
            mTvDepartment.setText("");
            mMajorObj = null;
            mTvMajor.setText("");
        } else if (requestCode == SCConstants.TYPE_DEPARTMENT) {
            mDepartmentObj = data.getParcelableExtra(SCDepartmentObject.class.toString());
            mTvDepartment.setText(mDepartmentObj.getName());

            mMajorObj = null;
            mTvMajor.setText("");
        } else if (requestCode == SCConstants.TYPE_MAJOR) {
            mMajorObj = data.getParcelableExtra(SCMajorObject.class.toString());
            mTvMajor.setText(mMajorObj.getName());
        } else if (requestCode == SCConstants.TYPE_ENROLLMENT) {
            mEnrollmentObj = data.getParcelableExtra(SCEnrollmentObject.class.toString());
            mTvEnrollment.setText(mEnrollmentObj.getName() + getResources().getString(R.string.common_year));
        } else if (requestCode == SCConstants.TYPE_GENDER) {
            mGenderObj = data.getParcelableExtra(SCGenderObject.class.toString());
            mTvGender.setText(mGenderObj.getName());
        } else if (requestCode == SCConstants.TYPE_PREFECTURE) {
            mPrefectureObj = data.getParcelableExtra(SCPrefectureObject.class.toString());
            mTvPrefecture.setText(mPrefectureObj.getName());
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
        setContentView(R.layout.activity_profile_info);

        SCGlobalUtils.sActivityArr.add(this);

        try {
            mUserObj = getIntent().getParcelableExtra(SCUserObject.class.toString());

            old_point = mUserObj.getCampusPoint();
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
        mIbtnBack = (ImageButton) findViewById(R.id.profile_ibtn_back);
        mImgAvatar = (SCCircleImageView) findViewById(R.id.profile_img_avatar);
        mTvName = (TextView) findViewById(R.id.profile_tv_name);
        mTvEmail = (TextView) findViewById(R.id.profile_tv_email);
        mEtNickname = (EditText) findViewById(R.id.profile_et_nickname);
        //disable emojis
//        InputFilter[] filterArray = new InputFilter[] {getEditTextFilterEmoji()};
//        mEtNickname.setFilters(filterArray);

        mTvUniversity = (TextView) findViewById(R.id.profile_tv_university);
        mTvMajor = (TextView) findViewById(R.id.profile_tv_major);
        mTvDepartment = (TextView) findViewById(R.id.profile_tv_department);
        mTvEnrollment = (TextView) findViewById(R.id.profile_tv_enrollment);
        mTvGender = (TextView) findViewById(R.id.profile_tv_gender);
        mTvBirthday = (TextView) findViewById(R.id.profile_tv_birthday);
        mEtPostcode = (EditText) findViewById(R.id.profile_et_postcode);
        mTvPrefecture = (TextView) findViewById(R.id.profile_tv_prefectures);
        mEtAddress = (EditText) findViewById(R.id.profile_et_address);
        mEtPhone = (EditText) findViewById(R.id.profile_et_phone);
        mBtnCancel = (Button) findViewById(R.id.profile_btn_cancel);
        mBtnSave = (Button) findViewById(R.id.profile_btn_save);
        mBtnEdit = (Button) findViewById(R.id.profile_btn_edit);
        mLlEmail = (LinearLayout) findViewById(R.id.profile_ll_email);
        mLlnotguest = (LinearLayout) findViewById(R.id.ln_not_guest);
        mFmguest = (FrameLayout) findViewById(R.id.fm_guest);
        mBtnregister = (Button) findViewById(R.id.btn_register);
        DialogView = this.getLayoutInflater().inflate(R.layout.dialog_guest_register, null);
        mDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);

        DialogView1 = this.getLayoutInflater().inflate(R.layout.layout_point_dialog, null);
        mDialog_tut1 = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);

        header_label = (TextView) findViewById(R.id.header_label);

        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            setUpViewsForTadacopy();
        } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            setUpViewsForCanpass();
        }
        else if (getPackageName().equals(SCConstants.PACKAGE_TORETAN_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TORETAN_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TORETAN_STAGING)) {
            setUpViewsForToretan();
        }
    }

    private void setUpViewsForTadacopy() {
    }

    private void setUpViewsForCanpass() {

        header_label.setTextColor(getResources().getColor(R.color.canpass_main));
    }
    private void setUpViewsForToretan() {

        header_label.setTextColor(getResources().getColor(R.color.toretan_main));
    }

    @Override
    protected void initListeners() {
        mIbtnBack.setContentDescription("back");
        mBtnCancel.setContentDescription("cancel");
        mBtnSave.setContentDescription("save");
        mBtnEdit.setContentDescription("edit");

        mImgAvatar.setContentDescription("avatar");

        mTvEmail.setContentDescription("email");
        mTvUniversity.setContentDescription("university");
        mTvDepartment.setContentDescription("department");
        mTvMajor.setContentDescription("major");
        mTvEnrollment.setContentDescription("enrollment");
        mTvGender.setContentDescription("gender");
        mTvBirthday.setContentDescription("birthday");
        mTvPrefecture.setContentDescription("prefecture");

        mBtnregister.setContentDescription("register");

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("back")) {
                    afterClickBack();
                } else if (v.getContentDescription().equals("cancel")) {
                    afterClickCancel();
                } else if (v.getContentDescription().equals("save")) {
                    afterClickSave();
                } else if (v.getContentDescription().equals("edit")) {
                    afterClickEdit();
                } else if (v.getContentDescription().equals("avatar")) {
                    afterClickAvatar();
                } else if (v.getContentDescription().equals("email")) {
                    afterClickEmail();
                } else if (v.getContentDescription().equals("university")) {
                    afterClickUniversity();
                } else if (v.getContentDescription().equals("department")) {
                    afterClickDepartment();
                } else if (v.getContentDescription().equals("major")) {
                    afterClickMajor();
                } else if (v.getContentDescription().equals("enrollment")) {
                    afterClickEnrollment();
                } else if (v.getContentDescription().equals("birthday")) {
                    afterClickBirthday();
                } else if (v.getContentDescription().equals("prefecture")) {
                    afterClickPrefecture();
                } else if (v.getContentDescription().equals("gender")) {
                    afterClickGender();
                } else if (v.getContentDescription().equals("register")) {
                    afterClickRegister();
                }
            }
        };

        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before < count) {
                    mIsRemovingText = false;
                } else {
                    mIsRemovingText = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (getCurrentFocus() == null) {
                    return;
                }

                if (mEtPostcode.getId() == getCurrentFocus().getId()) {
                    if (s.length() == 3) {
                        if (mIsRemovingText == false) {
                            mEtPostcode.setText(s.toString() + "-");
                            mEtPostcode.setSelection(mEtPostcode.getText().length());
                        }
                    }
                }

                if (!s.toString().equals(mEtPostcode.getText().toString())
                        && mEtNickname.getId() == getCurrentFocus().getId()) {
                    mTvName.setText(s.toString());
                }
            }
        };
    }

    private void afterClickRegister() {
        show_dialog_guest_register();
    }

    private void show_dialog_guest_register() {

        SCGlobalUtils.showGuestRegisterDialog(this, new SCDialogCallback() {
            @Override
            public void onAction1() {

            }

            @Override
            public void onAction2() {
                logoutANDgotomultiloginpage();
            }

            @Override
            public void onAction3() {

            }

            @Override
            public void onAction4() {

            }
        });


//        View v = DialogView;
//        ImageView imageView = (ImageView) v.findViewById(R.id.scmain_img_scicon);
//        ImageView close = (ImageView) v.findViewById(R.id.btn_close_copy_code);
//        Button webpage = (Button) v.findViewById(R.id.btn_goto_web);
//        webpage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//                logoutANDgotomultiloginpage();
//            }
//        });
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//            }
//        });
//
//        SCMultipleScreen.resizeAllView((ViewGroup) v);
//        mDialog.setCancelable(false);
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
//        mDialog.setContentView(v);
//
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//                mDialog.show();
//            }
//        });

    }

    private void logoutANDgotomultiloginpage() {

        HashMap<String, Object> params = new HashMap<String, Object>();

        SCRequestAsyncTask requestAsync = new SCRequestAsyncTask(SCProfileInfoActivity.this, SCConstants.REQUEST_LOGOUT, params, new SCRequestAsyncTask.AsyncCallback() {

            @Override
            public void done(String result) {
                Log.e("SCProfileInfoActivity", result);
                SCGlobalUtils.dismissLoadingProgress();
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.getString("success").equals("true")) {
                        SCSharedPreferencesUtils.removeComponent(SCProfileInfoActivity.this, SCConstants.TAG_APP_ID);
                        SCSharedPreferencesUtils.removeComponent(SCProfileInfoActivity.this, SCConstants.TAG_LOGIN_TYPE);
                        SCSharedPreferencesUtils.removeComponent(SCProfileInfoActivity.this, SCConstants.TAG_ACCESS_TOKEN);
                        SCSharedPreferencesUtils.removeComponent(SCProfileInfoActivity.this, SCConstants.TAG_REFRESH_TOKEN);
                        SCSharedPreferencesUtils.removeComponent(SCProfileInfoActivity.this, SCConstants.TWITTER_USER_NAME);
                        SCSharedPreferencesUtils.removeComponent(SCProfileInfoActivity.this, SCConstants.CODE);
                        SCSharedPreferencesUtils.removeComponent(SCProfileInfoActivity.this, SCConstants.TIME);
                        SCSharedPreferencesUtils.removeComponent(SCProfileInfoActivity.this, SCConstants.TAG_NOTIFICATION);
                        SCGlobalUtils.campus_work_status = null;
                        SCGlobalUtils.campus_work_category = null;
                        SCUserObject.resetInstant();
                        if (SCGlobalUtils.ishashpass.equals("1")) {
                            SCGlobalUtils.ishashpass = "0";
                            SCSharedPreferencesUtils.putString(SCProfileInfoActivity.this, SCConstants.TAG_PASSWORD, "");
                        }

//                            Intent myIntent = new Intent(SCProfileInfoActivity.this,Class.forName("tcapp.com.LoginActivity"));
                        Intent myIntent = new Intent(SCProfileInfoActivity.this, SCMultiLoginPage.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(myIntent);
//
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_right,
                                R.anim.anim_slide_out_left);
                    } else {
                        Toast.makeText(SCProfileInfoActivity.this, jObj.getString("error"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
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

        requestAsync.execute();

    }

    @Override
    protected void setListenerForViews() {
        mIbtnBack.setOnClickListener(mOnClickListener);
        mBtnCancel.setOnClickListener(mOnClickListener);
        mBtnEdit.setOnClickListener(mOnClickListener);
        mBtnSave.setOnClickListener(mOnClickListener);
        mImgAvatar.setOnClickListener(mOnClickListener);

        mTvEmail.setOnClickListener(mOnClickListener);
        mTvUniversity.setOnClickListener(mOnClickListener);
        mTvDepartment.setOnClickListener(mOnClickListener);
        mTvMajor.setOnClickListener(mOnClickListener);
        mTvEnrollment.setOnClickListener(mOnClickListener);
        mTvGender.setOnClickListener(mOnClickListener);
        mTvBirthday.setOnClickListener(mOnClickListener);
        mTvPrefecture.setOnClickListener(mOnClickListener);

        mEtNickname.addTextChangedListener(mTextWatcher);
        mEtPostcode.addTextChangedListener(mTextWatcher);
        mBtnregister.setOnClickListener(mOnClickListener);
    }

    //filtering emojis
    public static InputFilter getEditTextFilterEmoji() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                CharSequence sourceOriginal = source;
                source = replaceEmoji(source);
                end = source.toString().length();

                if (end == 0)
                    return ""; //Return empty string if the input character is already removed

                if (!sourceOriginal.toString().equals(source.toString())) {
                    char[] v = new char[end - start];
                    TextUtils.getChars(source, start, end, v, 0);

                    String s = new String(v);

                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(s);
                        TextUtils.copySpansFrom((Spanned) source, start, end, null, sp, 0);
                        return sp;
                    } else {
                        return s;
                    }
                } else {
                    return null; // keep original
                }
            }

            private String replaceEmoji(CharSequence source) {

                String notAllowedCharactersRegex = "[^a-zA-Z0-9@#\\$%\\&\\-\\+\\(\\)\\*;:!\\?\\~`£\\{\\}\\[\\]=\\.,_/\\\\\\s'\\\"<>\\^\\|÷×]";
                return source.toString()
                        .replaceAll(notAllowedCharactersRegex, "");
            }

        };
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

    private void initBirthdayDialog() {
        Calendar newCalendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.JAPAN);
        mBirthdayPickerDialog = new DatePickerDialog(this, R.style.CustomDialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mTvBirthday.setText(dateFormatter.format(newDate.getTime()));

                String birthdayStr = String.format(getResources().getString(R.string.common_date), dayOfMonth, String.valueOf(monthOfYear + 1), year);
                mTvBirthday.setText(birthdayStr);
                mBirthDay = birthdayStr;
            }

        }, (newCalendar.get(Calendar.YEAR) - 18), 0, 1);

        String title = getResources().getString(R.string.edit_info_two_birthday_dialog_title);
        mBirthdayPickerDialog.setTitle(title);
    }

    private void afterClickEmail() {
        String loginType = SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_LOGIN_TYPE);
        if (loginType != null
                && (loginType.equals(SCConstants.PROVIDER_FACEBOOK)
                || loginType.equals(SCConstants.PROVIDER_TWITTER))) {
            return;
        }

        String title = getResources().getString(R.string.dialog_update_email_title);
        String body = getResources().getString(R.string.dialog_update_email_body);
        String action1 = getResources().getString(R.string.common_email_update);
        String action2 = getResources().getString(R.string.common_cancel);

        SCGlobalUtils.showConfirmDialog(mContext, title, body, action1, action2, new SCDialogCallback() {
            @Override
            public void onAction1() {
                Intent intent = new Intent(mContext, SCUpdateMailActivity.class);
                startActivityForResult(intent, SCConstants.TYPE_UPDATE_MAIL);
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
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

    private void afterClickBack() {
        finish();
//        overridePendingTransition(R.anim.anim_slide_in_left,
//                R.anim.anim_slide_out_right);
    }

    private void afterClickCancel() {
        resetInfo();
        showInfo();
        mIbtnBack.setVisibility(View.VISIBLE);
        mBtnEdit.setVisibility(View.VISIBLE);
        mBtnCancel.setVisibility(View.GONE);
        mBtnSave.setVisibility(View.GONE);

        mImgAvatar.setClickable(false);

        mTvEmail.setEnabled(false);
        mEtNickname.setEnabled(false);
        mTvUniversity.setEnabled(false);
        mTvMajor.setEnabled(false);
        mTvDepartment.setEnabled(false);
        mTvEnrollment.setEnabled(false);
        mTvGender.setEnabled(false);
        mTvBirthday.setEnabled(false);
        mEtPostcode.setEnabled(false);
        mTvPrefecture.setEnabled(false);
        mEtAddress.setEnabled(false);
        mEtPhone.setEnabled(false);
    }

    private void afterClickSave() {
        String dialogBody = null;
        if (mEtNickname.getText().toString().trim().equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_empty_nickname);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        } else if (mUniversityObj == null) {
            dialogBody = getResources().getString(R.string.dialog_body_empty_university);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        } else if (mDepartmentObj == null) {
            dialogBody = getResources().getString(R.string.dialog_body_empty_department);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        } else if (mEnrollmentObj == null) {
            dialogBody = getResources().getString(R.string.dialog_body_empty_enrollment);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        } else if (!SCGlobalUtils.isEmailValid(mTvEmail.getText().toString().trim())) {
            String loginType = SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_LOGIN_TYPE);
            if (!loginType.equals(SCConstants.PROVIDER_FACEBOOK)
                    && !loginType.equals(SCConstants.PROVIDER_TWITTER)
                    && !loginType.equals(SCConstants.PROVIDER_GUEST)) {
                dialogBody = getResources().getString(R.string.dialog_body_email_invalid_label);
                SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
                return;
            }
        } else if ((mEtPostcode.getText().toString().trim().length() != 8)
                && (mEtPostcode.getText().toString().trim().length() != 0)) {
            String title = getResources().getString(R.string.dialog_error_title);
            String body = getResources().getString(R.string.dialog_body_wrong_zipcode);
            SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
            return;
        }

        requestUpdateUser();

        mIbtnBack.setVisibility(View.VISIBLE);
        mBtnEdit.setVisibility(View.VISIBLE);
        mBtnCancel.setVisibility(View.GONE);
        mBtnSave.setVisibility(View.GONE);

        mImgAvatar.setClickable(false);

        mTvEmail.setEnabled(false);
        mEtNickname.setEnabled(false);
        mTvUniversity.setEnabled(false);
        mTvMajor.setEnabled(false);
        mTvDepartment.setEnabled(false);
        mTvEnrollment.setEnabled(false);
        mTvGender.setEnabled(false);
        mTvBirthday.setEnabled(false);
        mEtPostcode.setEnabled(false);
        mTvPrefecture.setEnabled(false);
        mEtAddress.setEnabled(false);
        mEtPhone.setEnabled(false);
    }

    private void afterClickEdit() {
        mIbtnBack.setVisibility(View.GONE);
        mBtnEdit.setVisibility(View.GONE);
        mBtnCancel.setVisibility(View.VISIBLE);
        mBtnSave.setVisibility(View.VISIBLE);

        mImgAvatar.setClickable(true);

        mTvEmail.setEnabled(true);
        mEtNickname.setEnabled(true);
        mTvUniversity.setEnabled(true);
        mTvMajor.setEnabled(true);
        mTvDepartment.setEnabled(true);
        mTvEnrollment.setEnabled(true);

        if (mUserObj.getIsGuest() != null && mUserObj.getIsGuest().equals("false")) {
            mTvGender.setEnabled(true);
            mTvBirthday.setEnabled(true);
            mEtPostcode.setEnabled(true);
            mTvPrefecture.setEnabled(true);
            mEtAddress.setEnabled(true);
            mEtPhone.setEnabled(true);
        } else {
            mTvGender.setEnabled(false);
            mTvBirthday.setEnabled(false);
            mEtPostcode.setEnabled(false);
            mTvPrefecture.setEnabled(false);
            mEtAddress.setEnabled(false);
            mEtPhone.setEnabled(false);
        }
    }

    private void afterClickAvatar() {
        Intent intentChooseImage = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setType("image/*");

        if (Build.VERSION.SDK_INT < 19) {
            intentChooseImage = new Intent();
            intentChooseImage.setAction(Intent.ACTION_GET_CONTENT);
            intentChooseImage.setType("image/*");

        } else {
            intentChooseImage = new Intent(Intent.ACTION_GET_CONTENT);
            intentChooseImage.addCategory(Intent.CATEGORY_OPENABLE);
            intentChooseImage.setType("image/*");
        }
        Intent chooserIntent = Intent.createChooser(takePictureIntent, getResources().getString(R.string.dialog_choose_image_title));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, SCConstants.TYPE_AVATAR);
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

    private void afterClickGender() {
        moveToChoosenActivity(SCConstants.TYPE_GENDER);
    }

    private void afterClickBirthday() {
        mBirthdayPickerDialog.show();
    }

    private void afterClickPrefecture() {
        moveToChoosenActivity(SCConstants.TYPE_PREFECTURE);
    }

    private void moveToChoosenActivity(int type) {
        String dialogBody = null;

        Intent intent = new Intent(this, SCChoosenActivity.class);
        switch (type) {
            case SCConstants.TYPE_DEPARTMENT:
                if (mUniversityObj == null) {
                    dialogBody = getResources().getString(R.string.dialog_body_empty_university);
                    SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
                    return;
                } else {
                    intent.putExtra(SCUniversityObject.class.toString(), mUniversityObj);
                }
                break;

            case SCConstants.TYPE_MAJOR:
                if (mDepartmentObj == null) {
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

    private void resetInfo() {
        mTvEmail.setText("");
        mEtNickname.setText("");
        mTvUniversity.setText("");
        mTvMajor.setText("");
        mTvDepartment.setText("");
        mTvEnrollment.setText("");
        mTvGender.setText("");
        mTvBirthday.setText("");
        mEtPostcode.setText("");
        mTvPrefecture.setText("");
        mEtAddress.setText("");
        mEtPhone.setText("");
    }


    private void showInfo() {
        mImgAvatar.setClickable(false);

        String loginType = SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_LOGIN_TYPE);
        if (loginType != null
                && (loginType.equals(SCConstants.PROVIDER_FACEBOOK)
                || loginType.equals(SCConstants.PROVIDER_TWITTER))
                || loginType.equals(SCConstants.PROVIDER_GUEST)) {
            mLlEmail.setVisibility(View.GONE);
        } else {
            mLlEmail.setVisibility(View.VISIBLE);
        }

        if (mUserObj.getIcon() != null && !mUserObj.getIcon().equals("")) {
            if (mUserObj.getIconInstance() == null) {
                ImageLoader.getInstance().displayImage(mUserObj.getIcon(), mImgAvatar, SCGlobalUtils.sOptForUserIcon);
            } else {
                mImgAvatar.setImageBitmap(mUserObj.getIconInstance());
            }
        }

        if (mUserObj.getIsGuest() != null && mUserObj.getIsGuest().equals("false")) {
            mBtnEdit.setVisibility(View.VISIBLE);
        } else {
            mBtnEdit.setVisibility(View.GONE);
        }


        if (mUserObj.getIsGuest() != null && mUserObj.getIsGuest().equals("false")) {
            mFmguest.setVisibility(View.GONE);
            mLlnotguest.setVisibility(View.VISIBLE);
        } else {
            mLlnotguest.setVisibility(View.GONE);
            mFmguest.setVisibility(View.VISIBLE);
        }

        if (mUserObj.getNickname() != null && !mUserObj.getNickname().equals("")) {
            mTvName.setText(mUserObj.getNickname());
            mEtNickname.setText(mUserObj.getNickname());
        }

        if (mUserObj.getEmail() != null && !mUserObj.getEmail().equals("")) {
            mTvEmail.setText(mUserObj.getEmail());
        }

        if (mUserObj.getUniversityName() != null && !mUserObj.getUniversityName().equals("")) {
            mTvUniversity.setText(mUserObj.getUniversityName());

            SCCampusObject campusObj = new SCCampusObject();
            campusObj.setId(mUserObj.getCampusId());

            mUniversityObj = new SCUniversityObject();
            mUniversityObj.setId(mUserObj.getUniversityId());
            mUniversityObj.setName(mUserObj.getUniversityName());
            mUniversityObj.setmCampusObj(campusObj);
        }

        if (mUserObj.getDepartmentName() != null && !mUserObj.getDepartmentName().equals("")) {
            mTvDepartment.setText(mUserObj.getDepartmentName());

            mDepartmentObj = new SCDepartmentObject();
            mDepartmentObj.setId(mUserObj.getDepartmentId());
            mDepartmentObj.setName(mUserObj.getDepartmentName());
        }

        if (mUserObj.getMajorName() != null && !mUserObj.getMajorName().equals("")) {
            mTvMajor.setText(mUserObj.getMajorName());

            mMajorObj = new SCMajorObject();
            mMajorObj.setId(mUserObj.getMajorId());
            mMajorObj.setName(mUserObj.getMajorName());
        }

        if (mUserObj.getEnrollmentYear() != null && !mUserObj.getEnrollmentYear().equals("0")) {
            mTvEnrollment.setText(mUserObj.getEnrollmentYear() + getResources().getString(R.string.common_year));

            mEnrollmentObj = new SCEnrollmentObject();
            mEnrollmentObj.setName(mUserObj.getEnrollmentYear());
        }

        if (mUserObj.getSex() != null && !mUserObj.getSex().equals("")) {
            mGenderObj = new SCGenderObject();
            if (mUserObj.getSex().equals(SCConstants.MALE)) {
                mTvGender.setText(Arrays.asList(
                        getResources().getStringArray(R.array.gender_array)).get(0));
                mGenderObj.setName(Arrays.asList(
                        getResources().getStringArray(R.array.gender_array)).get(0));
            } else if (mUserObj.getSex().equals(SCConstants.FEMALE)) {
                mTvGender.setText(Arrays.asList(
                        getResources().getStringArray(R.array.gender_array)).get(1));
                mGenderObj.setName(Arrays.asList(
                        getResources().getStringArray(R.array.gender_array)).get(1));
            }

        }

        if (mUserObj.getBirthday() != null && !mUserObj.getBirthday().equals("")) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//            try {
            //Date birthday = format.parse(mUserObj.getBirthday());
            //String birthdayStr = String.format(getResources().getString(R.string.common_date), DateFormat.format("dd", birthday), DateFormat.format("MM", birthday), DateFormat.format("yyyy", birthday));
            mTvBirthday.setText(mUserObj.getBirthday());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        } else {
            mTvBirthday.setText(getResources().getString(R.string.edit_info_two_birthday));
        }

        if (mUserObj.getPostCode() != null && !mUserObj.getPostCode().equals("")) {
            mEtPostcode.setText(mUserObj.getPostCode());
        }

        if (mUserObj.getPrefecture() != null && !mUserObj.getPrefecture().equals("")) {
            mTvPrefecture.setText(mUserObj.getPrefecture());

            mPrefectureObj = new SCPrefectureObject();
            mPrefectureObj.setId(mUserObj.getPrefectureId());
            mPrefectureObj.setName(mUserObj.getPrefecture());
        }

        if (mUserObj.getAddress() != null && !mUserObj.getAddress().equals("")) {
            mEtAddress.setText(mUserObj.getAddress());
        }

        if (mUserObj.getPhoneNumber() != null && !mUserObj.getPhoneNumber().equals("")) {
            mEtPhone.setText(mUserObj.getPhoneNumber());
        }
    }

    private void show_getpoint_dialog() {
        get_point_for_update = String.valueOf(Integer.parseInt(current_point) - Integer.parseInt(old_point));

        View v = DialogView1;
        FrameLayout total_view = (FrameLayout) v.findViewById(R.id.frm_container);
        TextView point = (TextView) v.findViewById(R.id.tv_point);
        point.setText(get_point_for_update);
        total_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog_tut1.dismiss();
            }
        });
        mDialog_tut1.setCancelable(true);
        mDialog_tut1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog_tut1.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        mDialog_tut1.setContentView(v);
        mDialog_tut1.show();

//        Handler handler = new Handler();
//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                mDialog_tut1.show();
//            }
//        };
//        handler.postDelayed(run, 1000);

    }


    private void requestUpdateUser() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();
        String agent = SCConstants.AGENT;
        String uuid = "";
        if (SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_DEVICE_ID, null) == null) {
            if (SCGlobalUtils.DEVICEUUID != null)
                uuid = SCGlobalUtils.DEVICEUUID;
            else {
                uuid = SCGlobalUtils.getDeviceUUID(this);
                if (uuid.equals("")) {
                    Toast.makeText(this, "UUID Missing", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            uuid = SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_DEVICE_ID, null);
        }

        String src = secretKey + appId + date;
        //String src = secretKey + "134Dh" + "1435726129";
        String key = SCGlobalUtils.md5Hash(src);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_KEY, key);
        params.put(SCConstants.PARAM_APP_ID, mUserObj.getAppId());
        params.put(SCConstants.PARAM_EMAIL, mTvEmail.getText().toString().trim());
        params.put(SCConstants.PARAM_NICKNAME, mEtNickname.getText().toString());
        params.put(SCConstants.PARAM_UNIV_ID, mUniversityObj.getId());
        params.put(SCConstants.PARAM_DEPARTMENT_ID, mDepartmentObj.getId());
        params.put(SCConstants.PARAM_CAMPUS_ID, mUniversityObj.getmCampusObj().getId());
        params.put(SCConstants.PARAM_ENROLLMENT_YEAR, mEnrollmentObj.getName());
        params.put(SCConstants.PARAM_MAJOR_ID, (mMajorObj != null) ? mMajorObj.getId() : "0");
        params.put(SCConstants.PARAM_AGENT, agent);
        params.put(SCConstants.PARAM_DEVICE_ID, uuid);

        params.put(SCConstants.PARAM_DATE, date);
        if (!mTvGender.getText().toString().equals(getResources().getString(R.string.edit_info_two_gender))) {
            params.put(SCConstants.PARAM_SEX, (mTvGender.getText().equals(
                    Arrays.asList(getResources().getStringArray(R.array.gender_array)).get(0))) ? SCConstants.MALE : SCConstants.FEMALE);
        }

        if (!mTvBirthday.getText().toString().equals(getResources().getString(R.string.edit_info_two_birthday))) {
            if (mBirthDay != null) {
                params.put(SCConstants.PARAM_BIRTHDAY, mBirthDay);
            }
        }

        if (!mEtPostcode.getText().toString().equals("")) {
            params.put(SCConstants.PARAM_POST_CODE, mEtPostcode.getText().toString());
        }

        if (mPrefectureObj != null) {
            params.put(SCConstants.PARAM_PREFECTURE_ID, mPrefectureObj.getId());
        }

        if (!mEtPhone.getText().toString().equals("")) {
            params.put(SCConstants.PARAM_PHONE_NUMBER, mEtPhone.getText().toString());
        }

        if (!mEtAddress.getText().toString().equals("")) {
            params.put(SCConstants.PARAM_ADDRESS, mEtAddress.getText().toString());
        }

        mRequestAsync = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_UPDATE_USER, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_UPDATE_USER, result);
                SCUserObject userObj = null;
                String errCode = null;

                if (returnHashMap.containsKey(SCConstants.TAG_APP_USER)) {
                    userObj = (SCUserObject) returnHashMap.get(SCConstants.TAG_APP_USER);
                    current_point = userObj.getCampusPoint();
                    userObj.setAppId(mUserObj.getAppId());
                    SCUserObject.updateInstance(userObj);

                    mUserObj = SCUserObject.getInstance();
                }

                if (SCGlobalUtils.is_point_updated.equals("true")) {
                    show_getpoint_dialog();
                }

                if (returnHashMap.containsKey(SCConstants.TAG_ERROR_CODE)) {
                    return;
                }

                requestIconUpdate();
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

    private void requestIconUpdate() {
        if (mBmAvatar == null) {
            showInfo();
            SCGlobalUtils.dismissLoadingProgress();
            return;
        }
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();

        String src = secretKey + appId + date;
        //String src = secretKey + "134Dh" + "1435726129";
        String key = SCGlobalUtils.md5Hash(src);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_KEY, key);
        params.put(SCConstants.PARAM_APP_ID, mUserObj.getAppId());
        params.put(SCConstants.PARAM_DATE, date);
        params.put(SCConstants.PARAM_ICON, mBmAvatar);

        mRequestAsync = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_ICON_UPDATE, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                SCGlobalUtils.dismissLoadingProgress();

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_ICON_UPDATE, result);

                if (returnHashMap.containsKey(SCConstants.TAG_URL)) {
                    String url = (String) returnHashMap.get(SCConstants.TAG_URL);
                    mUserObj.setIconInstance(null);
                    mUserObj.setIcon(url);
                }

                if (returnHashMap.containsKey(SCConstants.TAG_ERROR)) {
                    return;
                }

                showInfo();
            }

            @Override
            public void progress() {
                showInfo();
            }

            @Override
            public void onInterrupted(Exception e) {
                SCGlobalUtils.dismissLoadingProgress();
                showInfo();
            }

            @Override
            public void onException(Exception e) {
                SCGlobalUtils.dismissLoadingProgress();
                showInfo();
            }
        });

        mRequestAsync.execute();
    }

}
