package jp.co.scmodule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

import org.brickred.socialauth.util.Base64;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import jp.co.scmodule.adapters.SCMemberListAdapter;
import jp.co.scmodule.adapters.GroupAdminNotificationAdapter;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.objects.SCGroupObject;
import jp.co.scmodule.objects.SCLessonObject;
import jp.co.scmodule.objects.SCMemberObject;
import jp.co.scmodule.objects.SCSectionObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.CorrectSizeUtil;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;
import jp.co.scmodule.utils.SCUrlConstants;
import jp.co.scmodule.widgets.SCPinnedHeaderListView;
import jp.co.scmodule.widgets.SCPixelScrollDetector;
import jp.co.scmodule.widgets.SCSingleLineTextView;

public class SCGroupAdminPage extends SCMyActivity {
    private static final String TAG_LOG = "SCGroupAdminPage";
    private float mY = 0;
    private Context mContext = null;
    private Activity mActivity = null;
    private RelativeLayout mRlPointsLayout = null;
    private SCRequestAsyncTask mSCRequestAsyncTask = null;
    private View.OnClickListener mOnClickListener = null;
    private SCPixelScrollDetector mPixelScrollDetector = null;
    private GroupAdminNotificationAdapter mAdapter = null;
    CorrectSizeUtil mCorrectSize = null;
    private boolean mIsLoadInDone = false;
    private ProgressBar mPbMoneyLine = null;
    private ArrayList<Object> mListLesson = null;
    private ArrayList<Object> mListInformations = null;
    private SCUserObject mUserObj = null;
    private SCGroupObject groupObject = null;
    private View DialogView;
    private SCGroupObject group_object = null;
    private ListView member_list = null;
    private TextView group_name = null;
    private ImageButton btn_sort = null;
    private SCSingleLineTextView group_point = null;
    private SCSingleLineTextView total_group_point = null;
    Button btn_circlepass_web = null;
    private SCMemberListAdapter listAdapter = null;
    private SCPinnedHeaderListView mLvNotification = null;
    private boolean sort_asc = true;
    private View.OnTouchListener mOnTouchListener = null;
    private String group_id = "";
    Dialog mDialog = null;
    private ImageButton share_line, dialog_manual;
    private ImageView image_content = null;
    private ImageButton scmain_img_scicon = null;
    private TextView tv_label = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scgroup_admin_page);
        SCGlobalUtils.sActivityArr.add(this);

        mUserObj = SCUserObject.getInstance();
        groupObject = SCGroupObject.getInstance();
        try {
            group_id = getIntent().getStringExtra("group_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        mListInformations = new ArrayList<Object>();
        mListLesson = new ArrayList<Object>();
        mCorrectSize = CorrectSizeUtil.getInstance(this);
        init();
    }

    @Override
    protected void init() {
        super.init();

        mContext = this;
        mActivity = this;


        if (!group_id.equals("")) {
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
                mIsLoadInDone = true;
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG + "Details", result);

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_REGISTER_GROUP, result);

                if (returnHashMap.containsKey(SCConstants.TAG_DATA)) {
                    group_object = (SCGroupObject) returnHashMap.get(SCConstants.TAG_DATA);
                    groupObject.updateInstance(group_object);
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
                mIsLoadInDone = true;
                SCGlobalUtils.dismissLoadingProgress();
            }

            @Override
            public void onException(Exception e) {
                mIsLoadInDone = true;
                SCGlobalUtils.dismissLoadingProgress();
            }
        });

        mSCRequestAsyncTask.execute();
    }

    private void showInfo() {
        group_name.setText(groupObject.getGruop_name());
        group_point.setText(groupObject.getCampus_point());
        total_group_point.setText(groupObject.getNext_grade_point());

        if (group_object.getRate() != null && !group_object.getRate().equals("") && !group_object.getRate().equals("1")) {
            switch (group_object.getRate()) {
                case "2":
                    scmain_img_scicon.setImageResource(R.drawable.circle_pass_rank_two);
                    break;
                case "3":
                    scmain_img_scicon.setImageResource(R.drawable.circle_pass_rank_three);
                    break;
                case "4":
                    scmain_img_scicon.setImageResource(R.drawable.circle_pass_rank_four);
                    break;
                case "5":
                    scmain_img_scicon.setImageResource(R.drawable.circle_pass_rank_five);
                    break;
            }
        } else {
            scmain_img_scicon.setImageResource(R.drawable.circle_pass_rank_one);
        }

        long campus_point = 0;
        if (groupObject.getCampus_point() != null && !groupObject.getCampus_point().equals("")) {
            campus_point = Integer.parseInt(groupObject.getCampus_point());
        }
        long next_grade_point = 0;
        if (groupObject.getNext_grade_point() != null && !groupObject.getNext_grade_point().equals("")) {
            next_grade_point = Integer.parseInt(groupObject.getNext_grade_point());
        }
//        campus_point = 1200;
        Double gauge_value = ((double) campus_point / (campus_point + next_grade_point)) * 100;
        mPbMoneyLine.setProgress(gauge_value.intValue());

////for testing
//        ArrayList<SCInformationObject> a = new ArrayList<SCInformationObject>();
//        SCInformationObject sf = new SCInformationObject();
//        sf.setTitle("uhguhi");
//        sf.setApplication_url("https://www.google.com");
//
//        SCInformationObject sfr = new SCInformationObject();
//        sfr.setTitle("yhjkh");
//        SCInformationObject sfw = new SCInformationObject();
//        sfw.setTitle("gkuy");
//        SCInformationObject swf = new SCInformationObject();
//        swf.setTitle("uhgkukuhi");
//
//        a.add(sf);
//        a.add(sfr);
//        a.add(sfw);
//        a.add(swf);

        listAdapter = new SCMemberListAdapter(SCGroupAdminPage.this, groupObject.getMemberList());
        member_list.setAdapter(listAdapter);
        setListViewHeightBasedOnChildren(member_list);

        mListInformations.addAll(group_object.getInformationList());
        //for testing
//       mListInformations.addAll(a);

        initNotificationList();


    }

    /****
     * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView
     ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new AbsListView.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        afterClickBack();
        super.onBackPressed();
    }

    public void afterClickBack() {
        finish();
        overridePendingTransition(R.anim.anim_nothing,
                R.anim.anim_slide_out_right);
    }


    @Override
    protected void findViewById() {
        group_name = (TextView) findViewById(R.id.scmain_tv_name);
        group_point = (SCSingleLineTextView) findViewById(R.id.scmain_tv_point);
        total_group_point = (SCSingleLineTextView) findViewById(R.id.tv_total_point);
        btn_circlepass_web = (Button) findViewById(R.id.btn_circlepass_web);
        mPbMoneyLine = (ProgressBar) findViewById(R.id.scmain_pb_money_line);
        member_list = (ListView) findViewById(R.id.member_list);
        mLvNotification = (SCPinnedHeaderListView) findViewById(R.id.scmain_lv_notification);
        mRlPointsLayout = (RelativeLayout) findViewById(R.id.infolist);
        btn_sort = (ImageButton) findViewById(R.id.sort_btn);
        share_line = (ImageButton) findViewById(R.id.share_line);
        dialog_manual = (ImageButton) findViewById(R.id.dialog_manual);
        scmain_img_scicon = (ImageButton) findViewById(R.id.scmain_img_scicon);
        mDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        tv_label = (TextView) findViewById(R.id.tv_label);
        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            setUpViewsForTadacopy();
        } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            setUpViewsForCanpass();
        }
    }

    private void setUpViewsForTadacopy() {
    }

    private void setUpViewsForCanpass() {
        tv_label.setTextColor(getResources().getColor(R.color.canpass_main));
        btn_circlepass_web.setBackgroundResource(R.drawable.circle_pass_btn_selector_canpass);
    }

    @Override
    protected void initListeners() {
        btn_circlepass_web.setContentDescription("circlepass_web");
        btn_sort.setContentDescription("sort");
        share_line.setContentDescription("share");
        dialog_manual.setContentDescription("dialog_manual");
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("circlepass_web")) {
                    afterClickOpenPage();
                } else if (v.getContentDescription().equals("sort")) {
                    afterClickSort();
                } else if (v.getContentDescription().equals("share")) {
                    afterClickShare();
                } else if (v.getContentDescription().equals("dialog_manual")) {
                    afterClickManual();
                }
            }
        };

        mOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        };


        mPixelScrollDetector = new SCPixelScrollDetector(
                new SCPixelScrollDetector.PixelScrollListener() {
                    @Override
                    public void onScroll(AbsListView view, float deltaY) {
                        // TODO Auto-generated method stub
                        mY = mY + deltaY;

                        if (mY > 0) {
                            return;
                        }

                        if (mY > -mRlPointsLayout.getLayoutParams().height) {
                            ViewHelper.setTranslationY(mRlPointsLayout, mY);
                        }

                        if (mY < -mRlPointsLayout.getLayoutParams().height) {
                            ViewHelper.setTranslationY(mRlPointsLayout,
                                    -(float) mRlPointsLayout.getLayoutParams().height);
                        }

                        ViewHelper.setTranslationY(mRlPointsLayout, mY);
                    }
                });
    }

    private void afterClickManual() {
        show_group_manual_dialog();
    }

    private void afterClickShare() {
        show_share_dialog();
    }

    private void afterClickSort() {
        if (sort_asc) {
            //sort descending
            sort_asc = false;
            btn_sort.setBackgroundResource(R.drawable.sort_dsc);
            sort(sort_asc);
            listAdapter.notifyDataSetChanged();
        } else {
            //sort ascending
            sort_asc = true;
            btn_sort.setBackgroundResource(R.drawable.sort_asc);
            sort(sort_asc);
            listAdapter.notifyDataSetChanged();
        }
    }

    private void sort(final boolean sort_asc) {
        Collections.sort(group_object.getMemberList(), new Comparator<SCMemberObject>() {
            @Override
            public int compare(SCMemberObject o1, SCMemberObject o2) {

                int newest1 = Integer.parseInt(o1.getCampus_point());
                int newest2 = Integer.parseInt(o2.getCampus_point());

                if (sort_asc) {
                    return Integer.compare(newest2, newest1);
                } else {
                    return Integer.compare(newest1, newest2);
                }

            }
        });


    }

    private void afterClickOpenPage() {
        //opening other apps or other URLS
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cp.smart-campus.jp/"));
        mContext.startActivity(browserIntent);

        SCUserObject mUserObj = SCUserObject.getInstance();
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();
        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);


        String UUID = "";
        if (SCSharedPreferencesUtils.getString(this, SCConstants.TAG_DEVICE_ID, null) == null) {
            if (SCGlobalUtils.DEVICEUUID != null)
                UUID = SCGlobalUtils.DEVICEUUID;
            else {
                UUID = SCGlobalUtils.getDeviceUUID(this);
                if (UUID.equals("")) {
                    Toast.makeText(this, "UUID Missing", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            UUID = SCSharedPreferencesUtils.getString(this, SCConstants.TAG_DEVICE_ID, null);
        }


        String url = SCUrlConstants.URL_DOMAIN + "/api/auto_login?"
                + "app_id=" + appId
                + "&client_id=" + SCConstants.TADACOPY_CLIENT_ID
                + "&redirect_uri=" + "http://cp.smart-campus.jp/"
                + "&key=" + key
                + "&date=" + date
                + "&uuid=" + UUID
                + "&application_id=" + SCConstants.TADACOPY_DEFAULT_APP_ID_SECRET;

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Bundle bundle = new Bundle();
        Map<String, String> headers = new HashMap<>();
        SCGlobalUtils.additionalHeaderTag = "Authorization";
        SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(this, SCConstants.TAG_ACCESS_TOKEN, null));
        bundle.putString(SCGlobalUtils.additionalHeaderTag, SCGlobalUtils.additionalHeaderValue);
        i.putExtra(Browser.EXTRA_HEADERS, bundle);
        startActivity(i);

//        Intent mIntent = new Intent(SCGroupAdminPage.this, WebviewActivity.class);
//        mIntent.putExtra("url", "http://webhawks.oceanize.co.jp/circle_pass/");
//        startActivity(mIntent);
//        overridePendingTransition(R.anim.anim_slide_in_bottom,
//                R.anim.anim_scale_to_center);
    }

    public String getBase64(final String input) {
        return Base64.encodeBytes(input.getBytes(), Base64.DONT_BREAK_LINES);
    }

    @Override
    protected void setListenerForViews() {
        btn_circlepass_web.setOnClickListener(mOnClickListener);
        btn_sort.setOnClickListener(mOnClickListener);
        mLvNotification.setOnScrollListener(mPixelScrollDetector);
        mLvNotification.setOnTouchListener(mOnTouchListener);
        share_line.setOnClickListener(mOnClickListener);
        dialog_manual.setOnClickListener(mOnClickListener);

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

    private void initNotificationList() {
        // add header for list notification
        View v = new View(mContext);
        ListView.LayoutParams lp = new ListView.LayoutParams(
                ListView.LayoutParams.MATCH_PARENT, SCMultipleScreen.getValueAfterResize(419));
        v.setLayoutParams(lp);

        mLvNotification.addHeaderView(v);

        ArrayList<SCSectionObject> listSection = new ArrayList<SCSectionObject>();
        SCSectionObject s0 = new SCSectionObject();
        s0.setmSectionSize(mListLesson.size());
        SCSectionObject s1 = new SCSectionObject();
        s1.setmSectionSize(mListInformations.size());
        listSection.add(s0);
        listSection.add(s1);

        if (mUserObj.getIsGuest() != null && mUserObj.getIsGuest().equals("false")) {
            if (s0.getmSectionSize() == 0) {
                for (int i = 0; i < 3; i++) {
                    mListLesson.add(i, new SCLessonObject());
                }
            }
        } else {
            mListLesson.clear();
            mListLesson.add(new SCLessonObject());
        }

        s0.setmListData(mListLesson);
        s1.setmListData(mListInformations);

        mAdapter = new GroupAdminNotificationAdapter(mContext, listSection, 3);
        mLvNotification.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(mLvNotification);
    }


    public void show_share_dialog() {
        DialogView = this.getLayoutInflater().inflate(R.layout.dialog_share, null);
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

                mDialog.dismiss();
                Intent intent = new Intent(mContext, SCInviteMember.class);
                intent.putExtra(SCGroupObject.class.toString(),
                        group_object);
                intent.putExtra("type",
                        "only_share");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
            }
        });
        share_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


    public void show_group_manual_dialog() {

        DialogView = this.getLayoutInflater().inflate(R.layout.group_manual_page, null);
        View v = DialogView;
        FrameLayout frmContainer = (FrameLayout) v.findViewById(R.id.frm_container);
        RelativeLayout rltCopyCode = (RelativeLayout) v.findViewById(R.id.rlt_code_dialog);
        ImageView close = (ImageView) v.findViewById(R.id.btn_close_copy_code);

        ImageView blue_coin = (ImageView) v.findViewById(R.id.btn_blue_icon);
        ImageView red_coin = (ImageView) v.findViewById(R.id.btn_red_icon);
        ImageView silver_coin = (ImageView) v.findViewById(R.id.btn_silver_icon);
        ImageView gold_coin = (ImageView) v.findViewById(R.id.btn_gold_icon);
        ImageView black_coin = (ImageView) v.findViewById(R.id.btn_black_icon);

        image_content = (ImageView) v.findViewById(R.id.content_image);


        blue_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_content.setImageResource(R.drawable.blue_content);

            }
        });
        red_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_content.setImageResource(R.drawable.red_content);

            }
        });
        silver_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_content.setImageResource(R.drawable.silver_content);

            }
        });
        gold_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_content.setImageResource(R.drawable.gold_content);

            }
        });

        black_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_content.setImageResource(R.drawable.black_content);

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mCorrectSize.correctSize(v);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        mDialog.setContentView(v);
        mDialog.show();
    }

    public void sc_main_back_button_pressed(View v) {
        afterClickBack();
    }

    public void show_option_dialog(View view) {
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
                Toast.makeText(SCGroupAdminPage.this, "change group name is pressed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SCGroupAdminPage.this, SCUpdateGroupName.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);

            }
        });
        change_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(SCGroupAdminPage.this, "change group is pressed", Toast.LENGTH_LONG).show();
                //leave from group
                //remove the group related informations from the userObject
//                mUserObj.setStudent_group_id(null);
//                mUserObj.setStudent_group_leader(null);
//                mUserObj.setStudent_group_name(null);
                //open the add group page
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
