package jp.co.scmodule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.SCGroupObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;

public class SCGroupName extends SCMyActivity {
    private static final String TAG_LOG = "SCGroupName";
    private Button mBtnNext = null;
    private ImageView mBtnBack = null;
    private View.OnClickListener mOnClickListener = null;
    private SCRequestAsyncTask mRequestAsync = null;
    private Context mContext = null;
    private EditText group_name_et = null;
    private SCGroupObject group_object = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scgroup_name);
        SCGlobalUtils.sActivityArr.add(this);
        init();
    }
    @Override
    protected void init() {
        mContext = this;
        super.init();

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
        startActivity(new Intent(SCGroupName.this,SCAddGroup.class));
        finish();
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_right);
    }

    @Override
    protected void findViewById() {
        mBtnBack = (ImageView) findViewById(R.id.img_left_header);
        mBtnNext = (Button) findViewById(R.id.edit_info_one_btn_next);
        group_name_et = (EditText) findViewById(R.id.et_group_name);
    }

    @Override
    protected void initListeners() {
        mBtnBack.setContentDescription("back");
        mBtnNext.setContentDescription("next");
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("back")) {
                    afterClickBack();
                }else if (v.getContentDescription().equals("next")) {
                    afterClickNext();
                }
            }
        };
    }
    private void afterClickNext() {
        String group_name = group_name_et.getText().toString().trim();
        String dialogBody = null;
        if (group_name.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_group_empty_label);
            SCGlobalUtils.showInfoDialog(mContext, null, dialogBody, null, null);
            return;
        }
        request_create_group();

    }

    private void request_create_group() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(SCConstants.PARAM_GROUP_NAME, group_name_et.getText().toString().trim());
        mRequestAsync = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_REGISTER_GROUP, params, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {

                Log.e(TAG_LOG, result);
                SCGlobalUtils.dismissLoadingProgress();
                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_REGISTER_GROUP, result);

                if (returnHashMap.containsKey(SCConstants.TAG_DATA)) {
                    group_object = (SCGroupObject) returnHashMap.get(SCConstants.TAG_DATA);
                    SCUserObject userObj = SCUserObject.getInstance();

                    userObj.setStudent_group_name(group_object.getGruop_name());
                    userObj.setStudent_group_leader("true");
                    userObj.setStudent_group_id(group_object.getGroup_id());

                    SCUserObject.updateInstance(userObj);

                    Intent intent = new Intent(mContext, SCInviteMember.class);
                    intent.putExtra(SCGroupObject.class.toString(),
                            group_object);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_left);

                }else{
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
