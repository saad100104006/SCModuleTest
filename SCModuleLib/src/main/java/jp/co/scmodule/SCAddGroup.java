package jp.co.scmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.SCGroupObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;

public class SCAddGroup extends SCMyActivity {
    private static final String TAG_LOG = "SCAddGroup";
    private Button mBtnGroup = null;
    private Button mBtnNext = null;
    private ImageView mBtnBack = null;
    private View.OnClickListener mOnClickListener = null;
    private SCGroupObject groupObject = null;
    private SCRequestAsyncTask mRequestAsync = null;
    private Context mContext = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        SCGlobalUtils.sActivityArr.add(this);
        try {
            groupObject = getIntent().getParcelableExtra(SCGroupObject.class.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
    }

    @Override
    protected void init() {
        super.init();
        mContext = this;
        if (groupObject != null) {
            mBtnGroup.setText(groupObject.getGruop_name());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SCGlobalUtils.sActivityArr.remove(this);
    }


    @Override
    protected void findViewById() {
        mBtnGroup = (Button) findViewById(R.id.edit_info_one_btn_group);
        mBtnBack = (ImageView) findViewById(R.id.img_left_header);
        mBtnNext = (Button) findViewById(R.id.edit_info_one_btn_next);

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
        mBtnNext.setBackgroundResource(R.drawable.login_mail_canpass);
        mBtnBack.setImageResource(R.drawable.yellow_left_arrow_canpass);

    }
    private void setUpViewsForToretan() {
        mBtnNext.setBackgroundResource(R.drawable.login_mail_toretan);
        mBtnBack.setImageResource(R.drawable.blue_left_arrow_toretan);

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
    protected void initListeners() {
        mBtnGroup.setContentDescription("group");
        mBtnBack.setContentDescription("back");
        mBtnNext.setContentDescription("next");

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("group")) {
                    afterClickGroup();
                } else if (v.getContentDescription().equals("back")) {
                    afterClickBack();
                } else if (v.getContentDescription().equals("next")) {
                    afterClickNext();
                }
            }
        };
    }

    private void afterClickGroup() {
        startActivity(new Intent(SCAddGroup.this, SCChooseGroup.class));
        finish();
        overridePendingTransition(R.anim.anim_slide_in_bottom,
                R.anim.anim_slide_out_bottom);
    }

    private void afterClickBack() {
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }

    private void afterClickNext() {
        if (groupObject != null)
            requestJoinAPi();
        else {
            SCGlobalUtils.showInfoDialog(mContext, "エラー", "学生団体名を選択してください", null, null);
        }

    }

    private void requestJoinAPi() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_GROUP_ID, groupObject.getGroup_id());
        mRequestAsync = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_JOIN_GROUP, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {

                Log.e(TAG_LOG, result);
                SCGlobalUtils.dismissLoadingProgress();
                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_REGISTER_GROUP, result);

                if (returnHashMap.containsKey(SCConstants.TAG_DATA)) {
                    groupObject = (SCGroupObject) returnHashMap.get(SCConstants.TAG_DATA);

                    SCUserObject userObj = SCUserObject.getInstance();

                    userObj.setStudent_group_name(groupObject.getGruop_name());
                    userObj.setStudent_group_leader("false");
                    userObj.setStudent_group_id(groupObject.getGroup_id());

                    SCUserObject.updateInstance(userObj);

                    Intent intent = new Intent(mContext, SCGroupProfile.class);
                    intent.putExtra("group_id",
                            groupObject.getGroup_id());
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_left);

                } else {
                    if (returnHashMap.containsKey(SCConstants.TAG_ERROR_CODE)) {
                        String errCode = null;

                        String title = null;
                        String body = null;
                        errCode = (String) returnHashMap.get(SCConstants.TAG_ERROR_CODE);
                        body = (String) returnHashMap.get(SCConstants.TAG_ERROR);

                        title = getResources().getString(R.string.dialog_error_title);
                        SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
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

        mRequestAsync.execute();

    }

    @Override
    protected void setListenerForViews() {
        mBtnGroup.setOnClickListener(mOnClickListener);
        mBtnBack.setOnClickListener(mOnClickListener);
        mBtnNext.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void resizeScreen() {
        new SCMultipleScreen(this);
        SCMultipleScreen.resizeAllView(this);
    }

    @Override
    protected void initGlobalUtils() {

    }
}
