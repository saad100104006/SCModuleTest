package jp.co.scmodule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.scmodule.adapters.SCChoosenAdapter;
import jp.co.scmodule.adapters.SCGroupChooseAdapter;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.SCDepartmentObject;
import jp.co.scmodule.objects.SCGroupObject;
import jp.co.scmodule.objects.SCMajorObject;
import jp.co.scmodule.objects.SCPrefectureObject;
import jp.co.scmodule.objects.SCUniversityObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;

public class SCChooseGroup extends SCMyActivity {
    private String TAG_LOG = "SCChooseGroup";
    private Context mContext = null;
    private Activity mActivity = null;
    private SCRequestAsyncTask mRequestAsync = null;
    private TextWatcher mTextWatcher = null;
    private ListView mLvItem = null;
    private TextView mTvTitle = null;
    private RelativeLayout mRlSearch = null;
    private Button btn_new_group = null;
    private EditText mEtSearch = null;
    private SCGroupChooseAdapter mAdapter = null;
    private ArrayList<Object> mListData = null;
    private View.OnClickListener mOnClickListener = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
        SCGlobalUtils.sActivityArr.add(this);


        init();
    }
    @Override
    protected void init() {
        super.init();

        mContext = this;
        mActivity = this;

        initListItem();
        requestGetGroupData();


    }

    private void requestGetGroupData() {
        HashMap<String, Object> params = new HashMap<String, Object>();

        mRequestAsync = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_GET_GROUP_LIST, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {

                Log.e(TAG_LOG, result);
                SCGlobalUtils.dismissLoadingProgress();
                ArrayList<Object> resultList = null;
                resultList = (ArrayList<Object>) SCAPIUtils
                        .parseJSON(SCConstants.REQUEST_GET_GROUP_LIST, result).get(SCConstants.TAG_DATA);

                mListData.clear();
                mListData.addAll(resultList);
                mAdapter.notifyDataSetChanged();
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

    private void initListItem() {
        mListData = new ArrayList<Object>();
        mAdapter = new SCGroupChooseAdapter(mContext, mListData);
        mLvItem.setAdapter(mAdapter);
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

    private void afterClickBack() {
        startActivity(new Intent(SCChooseGroup.this,SCAddGroup.class));
        finish();
        overridePendingTransition(R.anim.anim_slide_in_bottom,
                R.anim.anim_slide_out_bottom);
    }

    public void back_button_pressed(View v){
        afterClickBack();
    }

    @Override
    protected void findViewById() {
        mLvItem = (ListView) findViewById(R.id.choosen_lv);
        mTvTitle = (TextView) findViewById(R.id.choosen_tv_title);
        mRlSearch = (RelativeLayout) findViewById(R.id.choosen_rl_search);
        mEtSearch = (EditText) findViewById(R.id.choosen_et_search);
        btn_new_group = (Button) findViewById(R.id.btn_new_group);
        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            setUpViewsForTadacopy();
        } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            setUpViewsForCanpass();
        }
    }

    private void setUpViewsForTadacopy() {
    }

    private void setUpViewsForCanpass() {
        btn_new_group.setBackgroundResource(R.drawable.selector_btn_next_canpass);

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
//                    if(obj instanceof SCUniversityObject) {
//                        if(((SCUniversityObject) obj).getName().toLowerCase()
//                                .contains(s.toString().toLowerCase())
//                                || ((SCUniversityObject) obj).getKana().toLowerCase()
//                                .contains(s.toString().toLowerCase())) {
//                            listSearched.add(obj);
//                        }
//                    }

                    if (obj instanceof SCGroupObject) {
                        if (((SCGroupObject) obj).getGruop_name().toLowerCase()
                                .contains(s.toString().toLowerCase())) {
                            listSearched.add(obj);
                        }
                    }
                }


                SCGroupChooseAdapter adapter = new SCGroupChooseAdapter(mContext, listSearched);
                mLvItem.setAdapter(adapter);
            }
        };

        btn_new_group.setContentDescription("group");

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("group")) {
                    afterClickGroup();
                }
            }
        };
    }

    private void afterClickGroup() {
        startActivity(new Intent(SCChooseGroup.this,SCGroupName.class));
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    @Override
    protected void setListenerForViews() {
        mEtSearch.addTextChangedListener(mTextWatcher);
        btn_new_group.setOnClickListener(mOnClickListener);
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
