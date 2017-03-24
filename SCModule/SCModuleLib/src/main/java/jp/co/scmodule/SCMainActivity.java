package jp.co.scmodule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devsmart.android.ui.HorizontalListView;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import jp.co.scmodule.adapters.SCListAppAdapter;
import jp.co.scmodule.adapters.SCNotificationAdapter;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.classes.SCMyActivity;
import jp.co.scmodule.interfaces.SCDialogCallback;
import jp.co.scmodule.objects.CampusanAdObject;
import jp.co.scmodule.objects.LessonObject;
import jp.co.scmodule.objects.SCAppObject;
import jp.co.scmodule.objects.SCBannerItem;
import jp.co.scmodule.objects.SCGroupObject;
import jp.co.scmodule.objects.SCInformationObject;
import jp.co.scmodule.objects.SCLessonObject;
import jp.co.scmodule.objects.SCSectionObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.CorrectSizeUtil;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;
import jp.co.scmodule.widgets.SCCircleImageView;
import jp.co.scmodule.widgets.SCPinnedHeaderListView;
import jp.co.scmodule.widgets.SCPixelScrollDetector;
import jp.co.scmodule.widgets.SCClockView;
import jp.co.scmodule.widgets.SCSingleLineTextView;
/*
* This is SCDashboard
* */
public class SCMainActivity extends SCMyActivity {
    private static final String TAG_LOG = "SCMainActivity";
    private Context mContext = null;
    private Activity mActivity = null;
    private SCRequestAsyncTask mSCRequestAsyncTask = null;
    private ArrayList<Object> mListApp = null;
    private SCListAppAdapter mListAppAdapter = null;
    private HorizontalListView mHorizontalListView = null;
    private View.OnClickListener mOnClickListener = null;
    private SCPixelScrollDetector mPixelScrollDetector = null;
    private SCNotificationAdapter mAdapter = null;
    CorrectSizeUtil mCorrectSize = null;
    private SCPinnedHeaderListView mLvNotification = null;
    private RelativeLayout mRlNameLayout = null;
    private SCSingleLineTextView mTvName = null;
    private SCCircleImageView mImgAvatar = null;
    private RelativeLayout mRlPointsLayout = null;
    private SCClockView mCvSC = null;
    private TextView mTvRemainDate0 = null;
    private TextView mTvRemainDate1 = null;
    private TextView mTvRemainDate2 = null;
    private TextView mTvRemainDate3 = null;
    private SCSingleLineTextView mTvPoint = null;
    private SCSingleLineTextView mTvSaveMoney = null;
    private ProgressBar mPbMoneyLine = null;
    private TextView mTvMarkerLeft = null;
    private TextView mTvMarkerMiddle = null;
    private TextView mTvMarkerRight = null;
    private ImageButton mIbtnChangePoint = null;
    private ImageButton mIbtnSCLogo = null;
    private ImageButton btn_add_group = null;
    private SCSingleLineTextView tv_add_group = null;
    private SCGroupObject groupObject = null;
    private ArrayList<Object> mListInformations = null;
    private ArrayList<Object> mListLesson = null;
    public ArrayList<Object> mListCampusAnBanner = null;


    private boolean mIsLoadInfoDone = false;
    private boolean mIsLoadLessons = false;

    private SCUserObject mUserObj = null;
    Handler handler = null;
    private float mY = 0;
    private View DialogView, DialogView2, DialogView3;
    Dialog mDialog_tut1 = null;
    Dialog mDialog_tut2 = null;
    Dialog mDialog_tut3 = null;

    public ArrayList<Object> mListBannerTop = new ArrayList<Object>();
    public ArrayList<Object> mListBannerMain = new ArrayList<Object>();
    public ArrayList<Object> mListBannerSub = new ArrayList<Object>();

//    public static HashMap<String, ArrayList<Object>> mListBanner;


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SCGlobalUtils.sActivityArr.remove(this);
    }

    @Override
    protected void onRestart() {
        mUserObj = SCUserObject.getInstance();
        initGlobalUtils();
        initInfo();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCheckPoint();
    }

    @Override
    public void onBackPressed() {
        afterClickBack();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scmain);

        SCGlobalUtils.sActivityArr.add(this);
        //init variables
        mUserObj = SCUserObject.getInstance();
        mListInformations = new ArrayList<Object>();
        mListLesson = new ArrayList<Object>();
        mListCampusAnBanner = new ArrayList<Object>();
        mCorrectSize = CorrectSizeUtil.getInstance(mActivity);
        init();
    }

    @Override
    protected void init() {
        super.init();

        mContext = this;
        mActivity = this;

        SCGlobalUtils.mListBanner.put(SCConstants.TAG_GET_BANNER_TYPE_TOP, mListBannerTop);
        SCGlobalUtils.mListBanner.put(SCConstants.TAG_GET_BANNER_TYPE_MAIN, mListBannerMain);
        SCGlobalUtils.mListBanner.put(SCConstants.TAG_GET_BANNER_TYPE_SUB, mListBannerSub);

        requestGetBannersFromCampusanApi();
        if (mUserObj.getIsGuest() != null && mUserObj.getIsGuest().equals("false")) {
            requestGetInfomations();
            mIsLoadLessons = true;
            //requestGetLessons();
            //requestGetAdvertise();

//            if (SCGlobalUtils.mListBanner != null && SCGlobalUtils.mListBanner.size() != 0 && SCGlobalUtils.mListBanner.get(SCConstants.TAG_GET_BANNER_TYPE_SUB).size() > 0) {
//                mListBannerSub = SCGlobalUtils.mListBanner.get(SCConstants.TAG_GET_BANNER_TYPE_SUB);
//            } else {
//                requestGetAdvertise();
//            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean isLoading = true;
                    do {
                        try {
                            if (mIsLoadInfoDone == true && mIsLoadLessons == true) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initNotificationList();
                                    }
                                });
                                isLoading = false;
                            }

                            Thread.sleep(200);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (isLoading);
                }
            }).start();
        } else {
            initNotificationList();
        }

        initInfo();
        if (SCGlobalUtils.showTutInSCPage) {
            SCGlobalUtils.showTutInSCPage = false;
            show_tut_one();
        } else {
            if (mUserObj.getIsGuest().equals("true")) {
                show_dialog_guest_register();
            }
        }

//        if(!mUserObj.getStudent_group_id().equals("")){
//            requestGetGroupDetails();
//        }
    }


    private void requestGetGroupDetails() {
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_GROUP_ID, mUserObj.getStudent_group_id());

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_GROUP_DETAILS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG + "Details", result);

                HashMap<String, Object> returnHashMap = SCAPIUtils.parseJSON(SCConstants.REQUEST_REGISTER_GROUP, result);

                if (returnHashMap.containsKey(SCConstants.TAG_DATA)) {
                    groupObject = (SCGroupObject) returnHashMap.get(SCConstants.TAG_DATA);
//                    if (groupObject != null)
//                        tv_add_group.setText(groupObject.getGruop_name());
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

    private void show_tut_one() {

        View v = DialogView;
        final RippleBackground rippleBackground = (RippleBackground) v.findViewById(R.id.content);
        if (Build.VERSION.SDK_INT >= 21)
            rippleBackground.setClipToOutline(true);

        ImageView imageView = (ImageView) v.findViewById(R.id.scmain_img_scicon);
        ImageView close = (ImageView) v.findViewById(R.id.btn_close_copy_code);
        Button webpage = (Button) v.findViewById(R.id.btn_goto_web);
        webpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(SCMainActivity.this, WebviewActivity.class);
                mIntent.putExtra("url", "http://mag.smart-campus.jp/smart-campus%E3%81%A8%E3%81%AF");
                startActivity(mIntent);
                overridePendingTransition(R.anim.anim_slide_in_bottom,
                        R.anim.anim_scale_to_center);

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog_tut1.dismiss();
                show_tut_two();
            }
        });
        rippleBackground.startRippleAnimation();
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        mDialog_tut1.setCancelable(false);
        mDialog_tut1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog_tut1.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        mDialog_tut1.setContentView(v);

        handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                mDialog_tut1.show();
            }
        };
        handler.postDelayed(run, 1000);

    }

    private void show_tut_two() {
        View v = DialogView2;
        final RippleBackground rippleBackground2 = (RippleBackground) v.findViewById(R.id.content);
        if (Build.VERSION.SDK_INT >= 21)
            rippleBackground2.setClipToOutline(true);
        ImageView imageView = (ImageView) v.findViewById(R.id.scmain_img_scicon);
        ImageView close = (ImageView) v.findViewById(R.id.btn_close_copy_code);
        Button webpage = (Button) v.findViewById(R.id.btn_goto_web);
        webpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(SCMainActivity.this, WebviewActivity.class);
                mIntent.putExtra("url", "http://mag.smart-campus.jp/campusworks%E3%81%A7%E3%83%9D%E3%82%A4%E3%83%B3%E3%83%88get");
                startActivity(mIntent);
                overridePendingTransition(R.anim.anim_slide_in_bottom,
                        R.anim.anim_scale_to_center);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog_tut2.dismiss();
                show_tut_three();

            }
        });
        rippleBackground2.startRippleAnimation();

        SCMultipleScreen.resizeAllView((ViewGroup) v);
        mDialog_tut2.setCancelable(false);
        mDialog_tut2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog_tut2.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        mDialog_tut2.setContentView(v);

        handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                mDialog_tut2.show();
            }
        };
        handler.postDelayed(run, 500);


    }

    private void show_tut_three() {
        View v = DialogView3;
        final RippleBackground rippleBackground3 = (RippleBackground) v.findViewById(R.id.content);
        if (Build.VERSION.SDK_INT >= 21)
            rippleBackground3.setClipToOutline(true);
        ImageView imageView = (ImageView) v.findViewById(R.id.scmain_img_scicon);
        ImageView close = (ImageView) v.findViewById(R.id.btn_close_copy_code);
        Button webpage = (Button) v.findViewById(R.id.btn_goto_web);
        webpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(SCMainActivity.this, WebviewActivity.class);
                mIntent.putExtra("url", "http://cp.smart-campus.jp");
                startActivity(mIntent);
                overridePendingTransition(R.anim.anim_slide_in_bottom,
                        R.anim.anim_scale_to_center);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog_tut3.dismiss();
                if (mUserObj.getIsGuest().equals("true")) {
                    show_dialog_guest_register();
                }
            }
        });
        rippleBackground3.startRippleAnimation();

        SCMultipleScreen.resizeAllView((ViewGroup) v);
        mDialog_tut3.setCancelable(false);
        mDialog_tut3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog_tut3.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        mDialog_tut3.setContentView(v);

        handler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                mDialog_tut3.show();
            }
        };
        handler.postDelayed(run, 500);


    }


    @Override
    protected void findViewById() {
        mLvNotification = (SCPinnedHeaderListView) findViewById(R.id.scmain_lv_notification);
        mRlNameLayout = (RelativeLayout) findViewById(R.id.scmain_rl_name_layout);
        mTvName = (SCSingleLineTextView) findViewById(R.id.scmain_tv_name);
        mImgAvatar = (SCCircleImageView) findViewById(R.id.scmain_img_avatar);
        mRlPointsLayout = (RelativeLayout) findViewById(R.id.scmain_rl_points_layout);
        mCvSC = (SCClockView) findViewById(R.id.scmain_img_sclock);
        mTvRemainDate0 = (TextView) findViewById(R.id.scmain_tv_remain_date_0);
        mTvRemainDate1 = (TextView) findViewById(R.id.scmain_tv_remain_date_1);
        mTvRemainDate2 = (TextView) findViewById(R.id.scmain_tv_remain_date_2);
        mTvRemainDate3 = (TextView) findViewById(R.id.scmain_tv_remain_date_3);
        mTvPoint = (SCSingleLineTextView) findViewById(R.id.scmain_tv_point);
        mTvSaveMoney = (SCSingleLineTextView) findViewById(R.id.scmain_tv_save_money);
        mPbMoneyLine = (ProgressBar) findViewById(R.id.scmain_pb_money_line);
        mTvMarkerLeft = (TextView) findViewById(R.id.scmain_tv_money_marker_left);
        mTvMarkerMiddle = (TextView) findViewById(R.id.scmain_tv_money_marker_middle);
        mTvMarkerRight = (TextView) findViewById(R.id.scmain_tv_money_marker_right);
        mHorizontalListView = (HorizontalListView) findViewById(R.id.scmain_lv_list_app);
        mIbtnChangePoint = (ImageButton) findViewById(R.id.scmain_ibtn_change_point);
        mIbtnSCLogo = (ImageButton) findViewById(R.id.scmain_img_scicon);

        DialogView = this.getLayoutInflater().inflate(R.layout.sclogo_tut_dialog, null);
        DialogView2 = this.getLayoutInflater().inflate(R.layout.scpoint_tut_page, null);
        DialogView3 = this.getLayoutInflater().inflate(R.layout.scgroup_tut_page, null);

        mDialog_tut1 = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        mDialog_tut2 = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);
        mDialog_tut3 = new Dialog(this, android.R.style.Theme_Black_NoTitleBar);

        btn_add_group = (ImageButton) findViewById(R.id.btn_add_group);
        tv_add_group = (SCSingleLineTextView) findViewById(R.id.tv_add_group);
    }

    @Override
    protected void initListeners() {
        mTvName.setContentDescription("name");
        mImgAvatar.setContentDescription("avatar");
        mIbtnChangePoint.setContentDescription("changePoint");
        mIbtnSCLogo.setContentDescription("sclogo");
        btn_add_group.setContentDescription("addGroup");
        tv_add_group.setContentDescription("tv_addGroup");
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("name")) {
                    afterClickShowInfo();
                } else if (v.getContentDescription().equals("avatar")) {
                    afterClickShowInfo();
                } else if (v.getContentDescription().equals("changePoint")) {
                    afterClickChangePoint();
                } else if (v.getContentDescription().equals("sclogo")) {
                    afterClickLogo();
                } else if (v.getContentDescription().equals("addGroup")) {
                    afterClickAddGroup();
                } else if (v.getContentDescription().equals("tv_addGroup")) {
                    afterClickName();
                }
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

    private void afterClickName() {
        Intent intent;
        if (mUserObj.getStudent_group_leader().equals("true")) {
            intent = new Intent(this, SCGroupAdminPage.class);
            intent.putExtra("group_id",
                    mUserObj.getStudent_group_id());
        } else {
            intent = new Intent(this, SCGroupProfile.class);
            intent.putExtra("group_id",
                    mUserObj.getStudent_group_id());
        }
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void afterClickAddGroup() {
//        Intent intent = new Intent(this, SCAddGroup.class);
        if (mUserObj.getIsGuest().equals("true")) {
            show_dialog_guest_register();
        } else {
            Intent intent = new Intent(this, SCAddGroup.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
        }

    }

    @Override
    protected void setListenerForViews() {
        mLvNotification.setOnScrollListener(mPixelScrollDetector);

        mImgAvatar.setOnClickListener(mOnClickListener);
        mIbtnChangePoint.setOnClickListener(mOnClickListener);
        mTvName.setOnClickListener(mOnClickListener);
        mIbtnSCLogo.setOnClickListener(mOnClickListener);
        btn_add_group.setOnClickListener(mOnClickListener);
        tv_add_group.setOnClickListener(mOnClickListener);
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

    private void initInfo() {
        if (mUserObj.getNickname() != null && !mUserObj.getNickname().equals("")) {
            mTvName.setText(mUserObj.getNickname());
        }

        if (mUserObj.getStudent_group_id() == null || mUserObj.getStudent_group_id().equals("")) {
            tv_add_group.setVisibility(View.GONE);
            btn_add_group.setVisibility(View.VISIBLE);
        } else {
            tv_add_group.setVisibility(View.VISIBLE);
            btn_add_group.setVisibility(View.GONE);
            tv_add_group.setText(mUserObj.getStudent_group_name());
        }

        if (mUserObj.getIcon() != null && !mUserObj.getIcon().equals("")) {
            if (mUserObj.getIconInstance() == null) {
                ImageLoader.getInstance().displayImage(mUserObj.getIcon(), mImgAvatar, SCGlobalUtils.sOptForUserIcon);
            } else {
                mImgAvatar.setImageBitmap(mUserObj.getIconInstance());
            }
        }

        if (mUserObj.getCampusPoint() != null && !mUserObj.getCampusPoint().equals("")) {
            mTvPoint.setText(mUserObj.getCampusPoint());
        } else {
            if (mUserObj.getIsGuest() != null && mUserObj.getIsGuest().equals("true")) {
                mTvPoint.setText("00000");
            } else {
                mTvPoint.setText("0");
            }

        }

        long userCash = 0;
        if (mUserObj.getSavingMoney() != null && !mUserObj.getSavingMoney().equals("")) {
            userCash = Integer.parseInt(mUserObj.getSavingMoney());
        }
        double cash = getMax(userCash);

        if (mUserObj.getIsGuest() != null && mUserObj.getIsGuest().equals("true")) {
            mTvSaveMoney.setText(getResources().getString(R.string.common_yen) + "00000");
        } else {
            mTvSaveMoney.setText(getResources().getString(R.string.common_yen) + NumberFormat.getNumberInstance(Locale.US).format(userCash));
        }

        mPbMoneyLine.setProgress((int) (userCash * 100 / cash));
        mTvMarkerLeft.setText((int) cash + "");
        mTvMarkerMiddle.setText((int) (cash / 2) + "");
        mTvMarkerRight.setText("0");

        if (mUserObj.getEnrollmentYear() != null && !mUserObj.getEnrollmentYear().equals("")) {
            Calendar currentDay = Calendar.getInstance();
            int currentYear = currentDay.get(Calendar.YEAR);
            int currentMonth = currentDay.get(Calendar.MONTH);
            int currentDayOfMonth = currentDay.get(Calendar.DAY_OF_MONTH);

            Calendar startDay = Calendar.getInstance();
            if (currentYear - Integer.parseInt(mUserObj.getEnrollmentYear()) >= 4) {
                if (currentMonth > 3 || (currentMonth == 3 && currentDayOfMonth > 1)) {
                    startDay.set(currentYear - 3, Calendar.APRIL, 1);
                } else {
                    startDay.set(currentYear - 4, Calendar.APRIL, 1);
                }
            } else {
                startDay.set(Integer.parseInt(mUserObj.getEnrollmentYear()), Calendar.APRIL, 1);
            }

            Calendar endDay = Calendar.getInstance();
            endDay.setTimeInMillis(startDay.getTimeInMillis());
            endDay.add(Calendar.YEAR, 4);
            Calendar nowDay = Calendar.getInstance();
            int days = getDaysBetweenDates(startDay, endDay);
            int passedDays = getDaysBetweenDates(startDay, nowDay);
            if ((days - passedDays) > 0) {
                mTvRemainDate3.setText("0");
                mTvRemainDate2.setText("0");
                mTvRemainDate1.setText("0");
                mTvRemainDate0.setText("0");
                mCvSC.setmAngle((float) getPassTimeRemaining(days, passedDays));
                String[] remainDate = String.valueOf(days - passedDays).trim().split("");
                for (int i = remainDate.length - 1; i >= 0; i--) {
                    if (i == remainDate.length - 1 && !remainDate[i].equals("")) {
                        mTvRemainDate3.setText(remainDate[i]);
                    } else if (i == remainDate.length - 2 && !remainDate[i].equals("")) {
                        mTvRemainDate2.setText(remainDate[i]);
                    } else if (i == remainDate.length - 3 && !remainDate[i].equals("")) {
                        mTvRemainDate1.setText(remainDate[i]);
                    } else if (i == remainDate.length - 4 && !remainDate[i].equals("")) {
                        mTvRemainDate0.setText(remainDate[i]);
                    }
                }
            } else {
                mCvSC.setmAngle(360.0f);
                mTvRemainDate3.setText("0");
                mTvRemainDate2.setText("0");
                mTvRemainDate1.setText("0");
                mTvRemainDate0.setText("0");
            }

        }

        getListApp();
    }

    public static void change() {

    }

    private void initNotificationList() {
        // add header for list notification
        View v = new View(mContext);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, SCMultipleScreen.getValueAfterResize(419));
        v.setLayoutParams(lp);

        mLvNotification.addHeaderView(v);

        ArrayList<SCSectionObject> listSection = new ArrayList<SCSectionObject>();

        SCSectionObject s0 = new SCSectionObject();
        s0.setmSectionSize(1);

        SCSectionObject s1 = new SCSectionObject();
        mListLesson.addAll(mUserObj.getmUserLesson());
        s1.setmSectionSize(mListLesson.size());

        SCSectionObject s2 = new SCSectionObject();
        s2.setmSectionSize(mListInformations.size());

        SCSectionObject s3 = new SCSectionObject();
        s3.setmSectionSize(1);

        listSection.add(s0);
        listSection.add(s1);
        listSection.add(s2);
        listSection.add(s3);


        if (mUserObj.getIsGuest() != null && mUserObj.getIsGuest().equals("false")) {
            if (s0.getmSectionSize() == 0) {
                for (int i = 0; i < 3; i++) {
                    mListLesson.add(i, new LessonObject());
                }
            }
        } else {
            mListLesson.clear();
            mListLesson.add(new LessonObject());
        }
        s0.setmListData(mListBannerSub);
        s1.setmListData(mListLesson);
        s2.setmListData(mListInformations);
        s3.setmListData(mListCampusAnBanner);

        boolean show_lesson = false;
        if (mUserObj.getmUserLesson().size() != 0) {
            show_lesson = true;
        }

        mAdapter = new SCNotificationAdapter(mContext, listSection, show_lesson);

        mLvNotification.setAdapter(mAdapter);
    }

    private void afterClickLogo() {
        afterClickBack();
    }

    private void afterClickChangePoint() {
        if (mUserObj.getIsGuest().equals("true")) {
            show_dialog_guest_register();
        } else {
            Intent intent = new Intent(this, ECMainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
        }
    }

    private void afterClickShowInfo() {
        if (mUserObj.getIsGuest().equals("true")) {
            show_dialog_guest_register();
        } else {
            Intent intent = new Intent(this, SCProfileInfoActivity.class);
            intent.putExtra(SCUserObject.class.toString(), mUserObj);
            startActivity(intent);
//        overridePendingTransition(R.anim.anim_slide_in_right,
//                R.anim.anim_slide_out_left);
        }
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
    }

    private void logoutANDgotomultiloginpage() {

        HashMap<String, Object> params = new HashMap<String, Object>();

        SCRequestAsyncTask requestAsync = new SCRequestAsyncTask(SCMainActivity.this, SCConstants.REQUEST_LOGOUT, params, new SCRequestAsyncTask.AsyncCallback() {

            @Override
            public void done(String result) {
                Log.e("SCMainActivity", result);
                SCGlobalUtils.dismissLoadingProgress();
                try {
                    JSONObject jObj = new JSONObject(result);
                    if (jObj.getString("success").equals("true")) {
                        if (SCSharedPreferencesUtils.getString(SCMainActivity.this, SCConstants.TAG_LOGIN_TYPE, null).equals(SCConstants.PROVIDER_GUEST)) {
                            SCSharedPreferencesUtils.putBoolean(SCMainActivity.this, SCConstants.TAG_USED_GUEST_ONCE, true);
                        }

                        SCSharedPreferencesUtils.removeComponent(SCMainActivity.this, SCConstants.TAG_APP_ID);
                        SCSharedPreferencesUtils.removeComponent(SCMainActivity.this, SCConstants.TAG_LOGIN_TYPE);
                        SCSharedPreferencesUtils.removeComponent(SCMainActivity.this, SCConstants.TAG_ACCESS_TOKEN);
                        SCSharedPreferencesUtils.removeComponent(SCMainActivity.this, SCConstants.TAG_REFRESH_TOKEN);
                        SCSharedPreferencesUtils.removeComponent(SCMainActivity.this, SCConstants.TWITTER_USER_NAME);
                        SCSharedPreferencesUtils.removeComponent(SCMainActivity.this, SCConstants.CODE);
                        SCSharedPreferencesUtils.removeComponent(SCMainActivity.this, SCConstants.TIME);
                        SCSharedPreferencesUtils.removeComponent(SCMainActivity.this, SCConstants.TAG_NOTIFICATION);
                        SCGlobalUtils.campus_work_status = null;
                        SCGlobalUtils.campus_work_category = null;
                        SCUserObject.resetInstant();
                        if (SCGlobalUtils.ishashpass.equals("1")) {
                            SCGlobalUtils.ishashpass = "0";
                            SCSharedPreferencesUtils.putString(SCMainActivity.this, SCConstants.TAG_PASSWORD, "");
                        }
//                            Intent myIntent = new Intent(SCMainActivity.this, Class.forName("tcapp.com.LoginActivity"));
                        Intent myIntent = new Intent(SCMainActivity.this, SCMultiLoginPage.class);

                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(myIntent);

                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_right,
                                R.anim.anim_slide_out_left);
                    } else {
                        Toast.makeText(SCMainActivity.this, jObj.getString("error"), Toast.LENGTH_LONG).show();
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

    private double getMax(double userCash) {
        int n = 0;
        double result = 1000 * Math.pow(2, n);
        if (userCash >= 128000) {
            result = 256000;
        } else {
            while (result <= userCash) {
                n = n + 1;
                result = 1000 * Math.pow(2, n);
            }
        }
        return result;
    }

    public void afterClickBack() {
        finish();
        overridePendingTransition(R.anim.anim_nothing,
                R.anim.anim_slide_out_left);
    }

    public void getListApp() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();
        String applicationId = "";
        //if (mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
        applicationId = SCConstants.APP_ID_TADACOPY;
//        } else if (mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
//            applicationId = SCConstants.APP_ID_CANPASS;
//        }
        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);
        parameter.put(SCConstants.PARAM_AGENT, SCConstants.AGENT);
        parameter.put(SCConstants.PARAM_APPLICATION_ID, applicationId);
        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_APPLICATION, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG + "APLCATION", result);
                mListApp = new ArrayList<Object>();
                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_APPLICATION, result);
                if (returnHash.containsKey(SCConstants.TAG_APPLICATIONS)) {
                    mListApp.addAll((ArrayList<SCAppObject>) returnHash.get(SCConstants.TAG_APPLICATIONS));
                    mListAppAdapter = new SCListAppAdapter(mActivity, mListApp);
                    mHorizontalListView.setAdapter(mListAppAdapter);
                }

            }

            @Override
            public void progress() {

            }

            @Override
            public void onInterrupted(Exception e) {

            }

            @Override
            public void onException(Exception e) {

            }
        });
        mSCRequestAsyncTask.execute();
    }

    private int getDaysBetweenDates(Calendar StartDate, Calendar EndDate) {
        long diffMillis = Math.abs(EndDate.getTimeInMillis() - StartDate.getTimeInMillis());
        long differenceInDays = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);
        return (int) differenceInDays;
    }

    private float getPassTimeRemaining(int totalDays, int passDays) {
        int passPercent = passDays * 360 / totalDays;
        return passPercent;
    }

    private void requestCheckPoint() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();
        //String appId = "188TZ";

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);

        SCRequestAsyncTask mSCRequestAsyncTask = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_CHECK_POINT, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG + "checkpnt", result);
                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_CHECK_POINT, result);
                if (returnHash.containsKey(SCConstants.TAG_CAMPUS_POINT)) {
                    String campusPoint = (String) returnHash.get(SCConstants.TAG_CAMPUS_POINT);
                    mUserObj.setCampusPoint(campusPoint);
                    mTvPoint.setText(campusPoint);
                }
            }

            @Override
            public void progress() {
            }

            @Override
            public void onInterrupted(Exception e) {
            }

            @Override
            public void onException(Exception e) {
            }
        });

        mSCRequestAsyncTask.execute();
    }

    private void requestGetLessons() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();
        //String appId = "188TZ";

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);

        SCRequestAsyncTask mSCRequestAsyncTask = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_GET_LESSONS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                mIsLoadLessons = true;
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG + "LESSON", result);
                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_LESSONS, result);
                if (returnHash.containsKey(SCConstants.TAG_LESSONS)) {
                    mListLesson.addAll((ArrayList<SCLessonObject>) returnHash.get(SCConstants.TAG_LESSONS));
                }
            }

            @Override
            public void progress() {
                SCGlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void onInterrupted(Exception e) {
                mIsLoadLessons = true;
                SCGlobalUtils.dismissLoadingProgress();
            }

            @Override
            public void onException(Exception e) {
                mIsLoadLessons = true;
                SCGlobalUtils.dismissLoadingProgress();
            }
        });

        mSCRequestAsyncTask.execute();
    }

    private void requestGetInfomations() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();
        String applicationId = "";
//        if (getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
        applicationId = SCConstants.APP_ID_TADACOPY;
//        } else if (getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
//            applicationId = SCConstants.APP_ID_CANPASS;
//        }
        //String appId = "188TZ";

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);
        parameter.put(SCConstants.PARAM_EMAIL, mUserObj.getEmail());
        parameter.put(SCConstants.PARAM_APPLICATION_ID, applicationId);

        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_INFORMATIONS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                mIsLoadInfoDone = true;
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG + "INFORMATION", result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_INFORMATIONS, result);

                if (returnHash.containsKey(SCConstants.TAG_INFORMATIONS)) {
                    mListInformations.addAll((ArrayList<SCInformationObject>) returnHash.get(SCConstants.TAG_INFORMATIONS));
                }
            }

            @Override
            public void progress() {
                SCGlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void onInterrupted(Exception e) {
                mIsLoadInfoDone = true;
                SCGlobalUtils.dismissLoadingProgress();
            }

            @Override
            public void onException(Exception e) {
                mIsLoadInfoDone = true;
                SCGlobalUtils.dismissLoadingProgress();
            }
        });

        mSCRequestAsyncTask.execute();
    }


    private void requestGetBannersFromCampusanApi() {
        String secretKey = SCConstants.SECRET_KEY_CAMPUSAN;
        String date = String.valueOf(System.currentTimeMillis() / 1000);


        String src = secretKey + date;
        String key = SCGlobalUtils.md5Hash(src);

        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_AUTH_KEY, key);
        parameter.put(SCConstants.PARAM_AUTH_DATE, date);


        mSCRequestAsyncTask = new SCRequestAsyncTask(this, SCConstants.REQUEST_GET_BANNERS_CAMPUSAN, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                mIsLoadInfoDone = true;
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG + "CAMPUSAN", result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_BANNERS_CAMPUSAN, result);

                if (returnHash.containsKey(SCConstants.TAG_ITEM)) {
                    mListCampusAnBanner.addAll((ArrayList<CampusanAdObject>) returnHash.get(SCConstants.TAG_ITEM));
                    mAdapter.notifyDataSetChanged();
                } else {
                    //if error occurs
                    requestGetBannersFromCampusanApi();
                }
            }

            @Override
            public void progress() {
                SCGlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void onInterrupted(Exception e) {
                mIsLoadInfoDone = true;
                SCGlobalUtils.dismissLoadingProgress();
            }

            @Override
            public void onException(Exception e) {
                mIsLoadInfoDone = true;
                SCGlobalUtils.dismissLoadingProgress();
            }
        });

        mSCRequestAsyncTask.execute();
    }


}
