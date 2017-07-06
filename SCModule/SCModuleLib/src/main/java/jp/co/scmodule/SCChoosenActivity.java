package jp.co.scmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import jp.co.scmodule.adapters.SCChoosenAdapter;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.SCDepartmentObject;
import jp.co.scmodule.objects.SCEnrollmentObject;
import jp.co.scmodule.objects.SCGenderObject;
import jp.co.scmodule.objects.SCMajorObject;
import jp.co.scmodule.objects.SCPrefectureObject;
import jp.co.scmodule.objects.SCUniversityObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCConstants;

public class SCChoosenActivity extends SCMyActivity {
    private String TAG_LOG = "SCChoosenActivity";
    private Context mContext = null;
    private Activity mActivity = null;

    private SCRequestAsyncTask mRequestAsync = null;
    private View.OnClickListener mOnClickListener = null;
    private TextWatcher mTextWatcher = null;

    private ListView mLvItem = null;
    private TextView mTvTitle = null;
    private RelativeLayout mRlSearch = null;
    private EditText mEtSearch = null;
    private SCChoosenAdapter mAdapter = null;
    private ArrayList<Object> mListData = null;

    private int mType = 0;
    private SCUniversityObject mUniversityObj = null;
    private SCDepartmentObject mDepartmentObj = null;

    private RelativeLayout rl_main = null;
    private FrameLayout fake_main = null;
    private EditText et_fake = null;

    public SCUniversityObject getmUniversityObj() {
        return mUniversityObj;
    }

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
    public void onBackPressed() {
        afterClickBack();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED) {
            return;
        }

        if(requestCode == SCConstants.TYPE_CAMPUS) {
            mUniversityObj = data.getParcelableExtra(SCUniversityObject.class.toString());
            Intent i = new Intent();
            i.putExtra(SCUniversityObject.class.toString(),
                    mUniversityObj);
            setResult(Activity.RESULT_OK, i);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_bottom,
                    R.anim.anim_slide_out_bottom);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void back_button_pressed(View v){
        afterClickBack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosen);

        SCGlobalUtils.sActivityArr.add(this);

        mType = getIntent().getIntExtra(Integer.class.toString(), 0);

        try {
            mUniversityObj = getIntent().getParcelableExtra(SCUniversityObject.class.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mDepartmentObj = getIntent().getParcelableExtra(SCDepartmentObject.class.toString());
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

        initListItem();

        showInfo();
    }

    @Override
    protected void findViewById() {
        mLvItem = (ListView) findViewById(R.id.choosen_lv);
        mTvTitle = (TextView) findViewById(R.id.choosen_tv_title);
        mRlSearch = (RelativeLayout) findViewById(R.id.choosen_rl_search);
        mEtSearch = (EditText) findViewById(R.id.choosen_et_search);


        rl_main = (RelativeLayout) findViewById(R.id.rl_main);
        fake_main = (FrameLayout) findViewById(R.id.fake_edit);
        et_fake = (EditText) findViewById(R.id.et_fake);

        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            setUpViewsForTadacopy();
        } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            setUpViewsForCanpass();
        }
    }

    private void setUpViewsForTadacopy() {
    }

    private void setUpViewsForCanpass() {
        mTvTitle.setTextColor(getResources().getColor(R.color.canpass_main));

    }


    @Override
    protected void initListeners() {
        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mListData == null) {
                    return;
                }

                ArrayList<Object> listSearched = new ArrayList<Object>();
                for(Object obj : mListData) {
                    if(obj instanceof SCUniversityObject) {
                        if(((SCUniversityObject) obj).getName().toLowerCase()
                                .contains(s.toString().toLowerCase())
                                || ((SCUniversityObject) obj).getKana().toLowerCase()
                                    .contains(s.toString().toLowerCase())) {
                            listSearched.add(obj);
                        }
                    }

                    if(obj instanceof SCDepartmentObject) {
                        if(((SCDepartmentObject) obj).getName().toLowerCase()
                                .contains(s.toString().toLowerCase())) {
                            listSearched.add(obj);
                        }
                    }

                    if(obj instanceof SCMajorObject) {
                        if(((SCMajorObject) obj).getName().toLowerCase()
                                .contains(s.toString().toLowerCase())) {
                            listSearched.add(obj);
                        }
                    }

                    if(obj instanceof SCPrefectureObject) {
                        if(((SCPrefectureObject) obj).getName().toLowerCase()
                                .contains(s.toString().toLowerCase())) {
                            listSearched.add(obj);
                        }
                    }
                }

                SCChoosenAdapter adapter = new SCChoosenAdapter(mContext, listSearched);
                mLvItem.setAdapter(adapter);
            }
        };

        et_fake.setContentDescription("et_fake");

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getContentDescription() == null) {
                    return;
                } else if(v.getContentDescription().equals("et_fake")) {
                    afterClickFake();
                }
            }
        };

    }

    private void afterClickFake() {
        rl_main.setVisibility(View.VISIBLE);
        fake_main.setVisibility(View.GONE);
    }

    @Override
    protected void setListenerForViews() {
        mEtSearch.addTextChangedListener(mTextWatcher);
        et_fake.setOnClickListener(mOnClickListener);
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

    private void initListItem() {
        mListData = new ArrayList<Object>();
        mAdapter = new SCChoosenAdapter(mContext, mListData);
        mLvItem.setAdapter(mAdapter);
    }

    private void afterClickBack() {
        finish();
        if(mType != SCConstants.TYPE_CAMPUS) {
            overridePendingTransition(R.anim.anim_slide_in_bottom,
                    R.anim.anim_slide_out_bottom);
        } else {
            overridePendingTransition(R.anim.anim_slide_in_left,
                    R.anim.anim_slide_out_right);
        }
    }

    private void showInfo() {
        String tilte = "";
        switch (mType) {
            case SCConstants.TYPE_UNIVERSITY:
                rl_main.setVisibility(View.INVISIBLE);
                fake_main.setVisibility(View.VISIBLE);
                tilte = getResources().getString(R.string.edit_info_one_university);
                mTvTitle.setText(tilte);
                mEtSearch.setHint("大学名を検索しよう！");
                et_fake.setHint("大学名を検索しよう！");
                requestGetData(SCConstants.REQUEST_GET_UNIVERSITY);
                break;
            case SCConstants.TYPE_DEPARTMENT:
                fake_main.setVisibility(View.GONE);
                tilte = getResources().getString(R.string.edit_info_one_department);
                mTvTitle.setText(tilte);
                mEtSearch.setHint("学部名を検索しよう！");
                requestGetData(SCConstants.REQUEST_GET_DEPARTMENT);
                break;
            case SCConstants.TYPE_MAJOR:
                fake_main.setVisibility(View.GONE);
                tilte = getResources().getString(R.string.edit_info_one_major);
                mTvTitle.setText(tilte);
                mEtSearch.setHint("学科名を検索しよう！");
                requestGetData(SCConstants.REQUEST_GET_MAJOR);
                break;
            case SCConstants.TYPE_ENROLLMENT:
                fake_main.setVisibility(View.GONE);
                tilte = getResources().getString(R.string.edit_info_one_enrollment);
                mTvTitle.setText(tilte);
                mEtSearch.setHint("入学年度を検索しよう！");
                getListEnrollment();
                break;

            case SCConstants.TYPE_CAMPUS:
                fake_main.setVisibility(View.GONE);
                tilte = getResources().getString(R.string.edit_info_one_campus);
                mTvTitle.setText(tilte);
                requestGetData(SCConstants.REQUEST_GET_CAMPUS);
                break;

            case SCConstants.TYPE_GENDER:
                fake_main.setVisibility(View.GONE);
                tilte = getResources().getString(R.string.edit_info_two_gender);
                mTvTitle.setText(tilte);
                getListGender();
                break;

            case SCConstants.TYPE_PREFECTURE:
                fake_main.setVisibility(View.GONE);
                tilte = getResources().getString(R.string.edit_info_two_prefecture);
                mTvTitle.setText(tilte);
                requestGetData(SCConstants.REQUEST_GET_PREFECTURE);
                break;
        }

        // just show search field when in university, department and major scene
        if(mType == SCConstants.TYPE_UNIVERSITY
                || mType == SCConstants.TYPE_DEPARTMENT
                || mType == SCConstants.TYPE_MAJOR
                || mType == SCConstants.TYPE_PREFECTURE) {
            mRlSearch.setVisibility(View.VISIBLE);
        } else {
            mRlSearch.setVisibility(View.GONE);
        }
    }

    private void getListEnrollment() {
        mListData.clear();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        if (currentMonth >= 10)
            currentYear += 1;
        for(int i = currentYear; i >= 2005 ; i--) {
            SCEnrollmentObject enrollmentObj = new SCEnrollmentObject();
            enrollmentObj.setName(String.valueOf(i));
            mListData.add(enrollmentObj);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void getListGender() {
        mListData.clear();
        for(int i = 0; i < Arrays.asList(getResources().getStringArray(R.array.gender_array)).size(); i++) {
            SCGenderObject genderObj = new SCGenderObject();
            genderObj.setName(Arrays.asList(getResources().getStringArray(R.array.gender_array)).get(i));
            mListData.add(genderObj);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void requestGetData(final int requestType) {
        HashMap<String, Object> params = new HashMap<String, Object>();

        if(requestType == SCConstants.REQUEST_GET_UNIVERSITY) {
        } else if(requestType == SCConstants.REQUEST_GET_DEPARTMENT) {
            params.put(SCConstants.PARAM_UNIV_ID, mUniversityObj.getId());
            params.put(SCConstants.PARAM_CAMPUS_ID, mUniversityObj.getmCampusObj().getId());
        } else if(requestType == SCConstants.REQUEST_GET_MAJOR) {
            params.put(SCConstants.PARAM_DEPARTMENT_ID, mDepartmentObj.getId());
        } else if(requestType == SCConstants.REQUEST_GET_CAMPUS) {
            params.put(SCConstants.PARAM_UNIV_ID, mUniversityObj.getId());
        } else if(requestType == SCConstants.REQUEST_GET_PREFECTURE) {
        }

        mRequestAsync = new SCRequestAsyncTask(mContext, requestType, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);
                if(requestType != SCConstants.REQUEST_GET_UNIVERSITY) {
                    SCGlobalUtils.dismissLoadingProgress();
                }
                ArrayList<Object> resultList = null;

                if(requestType == SCConstants.REQUEST_GET_UNIVERSITY) {
                    resultList = (ArrayList<Object>) SCAPIUtils
                            .parseJSON(SCConstants.REQUEST_GET_UNIVERSITY, result).get(SCConstants.TAG_DATA);
                } else if(requestType == SCConstants.REQUEST_GET_DEPARTMENT) {
                    resultList = (ArrayList<Object>) SCAPIUtils
                            .parseJSON(SCConstants.REQUEST_GET_DEPARTMENT, result).get(SCConstants.TAG_DATA);
                } else if(requestType == SCConstants.REQUEST_GET_MAJOR) {
                    resultList = (ArrayList<Object>) SCAPIUtils
                            .parseJSON(SCConstants.REQUEST_GET_MAJOR, result).get(SCConstants.TAG_DATA);
                } else if(requestType == SCConstants.REQUEST_GET_CAMPUS) {
                    resultList = (ArrayList<Object>) SCAPIUtils
                            .parseJSON(SCConstants.REQUEST_GET_CAMPUS, result).get(SCConstants.TAG_DATA);
                } else if(requestType == SCConstants.REQUEST_GET_PREFECTURE) {
                    resultList = (ArrayList<Object>) SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_PREFECTURE, result).get(SCConstants.TAG_DATA);
                }

                mListData.clear();
                mListData.addAll(resultList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void progress() {
                if(requestType != SCConstants.REQUEST_GET_UNIVERSITY) {
                    SCGlobalUtils.showLoadingProgress(mContext);
                }
            }

            @Override
            public void onInterrupted(Exception e) {
                if(requestType != SCConstants.REQUEST_GET_UNIVERSITY) {
                    SCGlobalUtils.dismissLoadingProgress();
                }
            }

            @Override
            public void onException(Exception e) {
                if(requestType != SCConstants.REQUEST_GET_UNIVERSITY) {
                    SCGlobalUtils.dismissLoadingProgress();
                }
            }
        });

        mRequestAsync.execute();
    }
}
