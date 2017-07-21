package jp.co.scmodule;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import jp.co.scmodule.adapters.SCGroupAdapter;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.SCGroupObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.CorrectSizeUtil;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;

public class SCGroupProfile extends SCMyActivity {
    private static final String TAG_LOG = "SCGroupProfile";
    private Context mContext = null;
    private Activity mActivity = null;
    CorrectSizeUtil mCorrectSize = null;
    private EditText name = null;
    private GridView gridView = null;
    private SCGroupAdapter gridadapter = null;
    private ImageButton profile_ibtn_back = null;
    private View.OnClickListener mOnClickListener = null;
    private String group_id = "";
    private SCRequestAsyncTask mSCRequestAsyncTask = null;
    private SCGroupObject groupObject = null;
    private View DialogView;
    private Dialog mDialog = null;
    private Button share_line;
    private TextView header_tv = null;

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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scgroup_profile);
        SCGlobalUtils.sActivityArr.add(this);
        try {
            group_id = getIntent().getStringExtra("group_id");
            //groupObject = getIntent().getParcelableExtra(SCGroupObject.class.toString());
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
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        if (group_id != null && !group_id.equals("")) {
            //requestGetInfomations();
            requestGetGroupDetails();

        }
    }

    private void requestGetGroupDetails() {
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_GROUP_ID, group_id);

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_GROUP_DETAILS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG + "Details", result);

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_REGISTER_GROUP, result);

                if (returnHashMap.containsKey(SCConstants.TAG_DATA)) {
                    groupObject = (SCGroupObject) returnHashMap.get(SCConstants.TAG_DATA);
                    if (groupObject != null)
                        showInfo();
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

        mSCRequestAsyncTask.execute();
    }


    private void showInfo() {
        name.setText(groupObject.getGruop_name());
        gridadapter = new SCGroupAdapter(SCGroupProfile.this, groupObject.getMemberList());
        gridView.setAdapter(gridadapter);
        setDynamicHeight(gridView);
    }

    @Override
    protected void findViewById() {
        header_tv = (TextView) findViewById(R.id.header_tv);
        name = (EditText) findViewById(R.id.group_et_name);
        gridView = (GridView) findViewById(R.id.gridView);
        profile_ibtn_back = (ImageButton) findViewById(R.id.profile_ibtn_back);
        DialogView = this.getLayoutInflater().inflate(R.layout.dialog_share, null);
        share_line = (Button) findViewById(R.id.edit_info_one_btn_next);
        mDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);

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
        header_tv.setTextColor(getResources().getColor(R.color.canpass_main));
        share_line.setBackgroundResource(R.drawable.selector_btn_next_canpass);
    }

    private void setUpViewsForToretan() {
        header_tv.setTextColor(getResources().getColor(R.color.toretan_main));
        share_line.setBackgroundResource(R.drawable.selector_btn_next_toretan);
    }


    @Override
    protected void initListeners() {
        profile_ibtn_back.setContentDescription("back");
        share_line.setContentDescription("share");
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("back")) {
                    afterClickBack();
                } else if (v.getContentDescription().equals("share")) {
                    afterClickShare();
                }
            }
        };

    }

    private void afterClickShare() {
        show_share_dialog();
    }

    @Override
    public void onBackPressed() {
        afterClickBack();
        super.onBackPressed();
    }


    @Override
    protected void setListenerForViews() {
        profile_ibtn_back.setOnClickListener(mOnClickListener);
        share_line.setOnClickListener(mOnClickListener);
    }

    private void afterClickBack() {
        finish();
        overridePendingTransition(R.anim.anim_nothing,
                R.anim.anim_slide_out_right);
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


    public void show_share_dialog() {
        View v = DialogView;
        FrameLayout frmContainer = (FrameLayout) v.findViewById(R.id.frm_container);
        RelativeLayout rltCopyCode = (RelativeLayout) v.findViewById(R.id.rlt_code_dialog);
        TextView close = (TextView) v.findViewById(R.id.btn_close_copy_code);
        TextView share_line = (TextView) v.findViewById(R.id.tv_line);
        TextView share_email = (TextView) v.findViewById(R.id.tv_mail);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        share_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SCInviteMember.class);
                intent.putExtra(SCGroupObject.class.toString(),
                        groupObject);
                intent.putExtra("type",
                        "only_share");
                startActivity(intent);
                mDialog.dismiss();
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
            }
        });
        share_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://goo.gl/n5Fh9k"));
//                mContext.startActivity(browserIntent);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(SCGlobalUtils.make_url_for_line(SCUserObject.getInstance())));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                mDialog.dismiss();
            }
        });

        mCorrectSize.correctSize(v);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        mDialog.setContentView(v);
        mDialog.show();
    }

    private void setDynamicHeight(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = 0;

        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > 5) {
            x = items / 5;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

    public void btn_edit_pressed(View view) {
        show_option_dialog();
    }

    public void show_option_dialog() {
        DialogView = this.getLayoutInflater().inflate(R.layout.dialog_share, null);
        View v = DialogView;
        TextView close = (TextView) v.findViewById(R.id.btn_close_copy_code);
        TextView change_group = (TextView) v.findViewById(R.id.tv_line);
        TextView change_group_name = (TextView) v.findViewById(R.id.tv_mail);
        change_group_name.setVisibility(View.GONE);
        close.setText("キャンセル");
        change_group.setText("別の団体に登録する");
        change_group.setTextColor(getResources().getColor(R.color.common_sc_main_color));
        change_group_name.setText("団体名を変更する");
        change_group_name.setTextColor(getResources().getColor(R.color.common_sc_main_color));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        change_group_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                //not visible this feature currently
                Toast.makeText(SCGroupProfile.this, "change group name is pressed", Toast.LENGTH_LONG).show();

            }
        });
        change_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                //  Toast.makeText(SCGroupProfile.this, "change group is pressed", Toast.LENGTH_LONG).show();
                afterClickGroupChange();

            }
        });

        mCorrectSize.correctSize(v);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        mDialog.setContentView(v);
        mDialog.show();

    }

    private void afterClickGroupChange() {
//        startActivity(new Intent(SCGroupProfile.this, SCChooseGroup.class));
//        finish();
//        overridePendingTransition(R.anim.anim_slide_in_bottom,
//                R.anim.anim_slide_out_bottom);
        Intent intent = new Intent(this, SCAddGroup.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);

    }

}
