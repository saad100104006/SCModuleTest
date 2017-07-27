package jp.co.scmodule.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

import org.brickred.socialauth.util.Base64;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.scmodule.SCMainActivity;
import jp.co.scmodule.adapters.SCNotificationAdapter;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.objects.LessonObject;
import jp.co.scmodule.objects.SCInformationObject;
import jp.co.scmodule.objects.SCLessonObject;
import jp.co.scmodule.objects.SCSectionObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;

import jp.co.scmodule.R;
import jp.co.scmodule.utils.SCMultipleScreenToretan;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;

/**
 * Created by minhtrihp on 6/8/15.
 * <p/>
 * This is class of sliding menu view. Set this view into your layout as RelativeLayout.
 * Showing by calling showMenu() function and hiding by calling hideMenu() function.
 * <p/>
 * Note: Do not add any view child into this layout because it has own children.
 */
public class SCSlideMenu extends RelativeLayout implements SCNotificationAdapter.hide {
    private static final String TAG_LOG = "SCSlideMenu";
    private Context mContext = null;
    private SCNotificationAdapter mAdapter = null;
    private GestureDetector mGestureDetector = null;

    private ArrayList<Object> mListInformations = null;
    private ArrayList<Object> mListLesson = null;

    private SCUserObject mUserObj = null;

    private float mContactY = 0.0f;

    private TextView mTvName = null;
    private SCPinnedHeaderListView mLvNotification = null;
    private ImageButton mIbtnSlideUp = null;
    private RelativeLayout mRlRoot = null;
    private ImageView mIVsclogo = null;

    private static final int SWIPE_MIN_DISTANCE = 30;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private static final int SLIDE_MENU_HEIGHT_MAX = 1205;
    private static final int SLIDE_MENU_HEIGHT_MEDIUM = 848;
    private static final int SLIDE_MENU_HEIGHT_MIN = 397;
    private static final int LIVE_TIME = 3000;

    private int mActuallySize = 1077;

    private boolean mIsLoadInfoDone = false;
    private boolean mIsLoadLessonsDone = false;

    public SCSlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mListInformations = new ArrayList<Object>();
        mListLesson = new ArrayList<Object>();

        mGestureDetector = new GestureDetector(new GestureListener());

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_slide_menu, this, true);

        init();

        mIbtnSlideUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideMenu();
            }
        });
        mIVsclogo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) mContext;
                Intent i = new Intent(mContext, SCMainActivity.class);
                i.putExtra(SCUserObject.class.toString(), mUserObj);
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_scale_to_center);
            }
        });
        mIbtnSlideUp.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchEvent(event);
                return false;
            }
        });

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // do nothing
            }
        });
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    private void init() {
        mTvName = (TextView) findViewById(R.id.slide_menu_tv_name);
        mLvNotification = (SCPinnedHeaderListView) findViewById(R.id.slide_menu_lv_notification);
        mIbtnSlideUp = (ImageButton) findViewById(R.id.slide_menu_ibtn_slide_up);
        mIVsclogo = (ImageView) findViewById(R.id.sclogo);
        mRlRoot = (RelativeLayout) findViewById(R.id.slide_menu_rl_root);
    }

    private void initNotificationList() {
        // add header for list notification
        SCGlobalUtils.from_slide_menu = true;
        ArrayList<SCSectionObject> listSection = new ArrayList<SCSectionObject>();
        SCSectionObject s0 = new SCSectionObject();
        s0.setmSectionSize(1);
        SCSectionObject s1 = new SCSectionObject();
        mListLesson.addAll(mUserObj.getmUserLesson());
        s1.setmSectionSize(mListLesson.size());
        SCSectionObject s2 = new SCSectionObject();
        s2.setmSectionSize(mListInformations.size());

        listSection.add(s0);
        listSection.add(s1);
        listSection.add(s2);

        if (mUserObj.getIsGuest() != null && mUserObj.getIsGuest().equals("false")) {
            if (s1.getmSectionSize() == 0) {
                for (int i = 0; i < 3; i++) {
                    mListLesson.add(i, new LessonObject());
                }
            }
        } else {
            mListLesson.clear();
           // mListLesson.add(new LessonObject());
        }

       // s0.setmListData(mListLesson);
        s1.setmListData(mListLesson);
        s2.setmListData(mListInformations);
        boolean show_lesson = false;
        if (mUserObj.getmUserLesson().size() != 0) {
            show_lesson = true;
        }
        mAdapter = new SCNotificationAdapter(mContext, listSection, show_lesson,this);

        mLvNotification.setAdapter(mAdapter);
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

        SCGlobalUtils.addAditionalHeader = true;
        SCGlobalUtils.additionalHeaderTag = "Authorization";
        SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_ACCESS_TOKEN, null));

        SCRequestAsyncTask mSCRequestAsyncTask = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_GET_LESSONS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                //SCGlobalUtils.dismissLoadingProgress();
                Log.e(TAG_LOG + "GET_LESSON", result);
                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_LESSONS, result);
                if (returnHash.containsKey(SCConstants.TAG_LESSONS)) {
                    mListLesson.addAll((ArrayList<SCLessonObject>) returnHash.get(SCConstants.TAG_LESSONS));
                }

                mIsLoadLessonsDone = true;
            }

            @Override
            public void progress() {
                //SCGlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void onInterrupted(Exception e) {
                //SCGlobalUtils.dismissLoadingProgress();
                mIsLoadLessonsDone = true;
            }

            @Override
            public void onException(Exception e) {
                //SCGlobalUtils.dismissLoadingProgress();
                mIsLoadLessonsDone = true;
            }
        });

        mSCRequestAsyncTask.execute();
    }

    private void requestGetInformations() {
        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = mUserObj.getAppId();
        String applicationId = "";
       // if (mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
            applicationId = SCConstants.APP_ID_TADACOPY;
//        } else if (mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
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
        SCGlobalUtils.addAditionalHeader = true;
        SCGlobalUtils.additionalHeaderTag = "Authorization";
        SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContext, SCConstants.TAG_ACCESS_TOKEN, null));

        SCRequestAsyncTask mSCRequestAsyncTask = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_GET_INFORMATIONS, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                //SCGlobalUtils.dismissLoadingProgress();
                Log.e(TAG_LOG + "INFORMATION", result);
                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_GET_INFORMATIONS, result);
                if (returnHash.containsKey(SCConstants.TAG_INFORMATIONS)) {
                    mListInformations.addAll((ArrayList<SCInformationObject>) returnHash.get(SCConstants.TAG_INFORMATIONS));
                }

                mIsLoadInfoDone = true;
            }

            @Override
            public void progress() {
                //SCGlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void onInterrupted(Exception e) {
                //SCGlobalUtils.dismissLoadingProgress();
                mIsLoadInfoDone = true;
            }

            @Override
            public void onException(Exception e) {
                //SCGlobalUtils.dismissLoadingProgress();
                mIsLoadInfoDone = true;
            }
        });

        mSCRequestAsyncTask.execute();
    }

    private Animation slideDown(final float from, final float to) {
        Animation anim = new TranslateAnimation(0, 0, from, to);
        anim.setDuration(300);
        anim.setFillAfter(false);
        final RelativeLayout menu = this;
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ViewHelper.setTranslationY(menu, 0.0f);
                menu.clearAnimation();
                menu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return anim;
    }

    private Animation slideUp(float from, float to) {
        Animation anim = new TranslateAnimation(0, 0, from, -((this.getMeasuredHeight() == 0) ? mActuallySize : this.getMeasuredHeight() + to));
        anim.setDuration(300);
        anim.setFillAfter(false);

        final RelativeLayout menu = this;
        menu.setVisibility(View.VISIBLE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ViewHelper.setTranslationY(menu, 0.0f);
                menu.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return anim;
    }

    public void showMenu() {
        Log.e(TAG_LOG, "run to show slide menu!!!");
        // animation layout
        if (ViewHelper.getY(this) == 0) {
            startAnimation(slideDown(-((this.getMeasuredHeight() == 0) ? mActuallySize : this.getMeasuredHeight()), 0.0f));
        } else {
            startAnimation(slideDown(0.0f, -ViewHelper.getY(this)));
        }

        // close slide menu after for a while
        final SCSlideMenu menu = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (LIVE_TIME != 0) {
                        Thread.sleep(LIVE_TIME);
                        if (menu.getVisibility() != View.GONE) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideMenu();
                                }
                            });
                        }
                    }
                } catch (Exception e) {

                }
            }
        }).start();
    }

    public void hideMenu() {
        startAnimation(slideUp(0.0f, -ViewHelper.getY(this)));
        SCGlobalUtils.from_slide_menu = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mContactY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                //Log.e("SCSlideMenu action move", String.valueOf(event.getRawY() - mContactY) + "");
                if (event.getRawY() - mContactY < 0) {
                    ViewHelper.setTranslationY(this, event.getRawY() - mContactY);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (event.getRawY() - mContactY == 0) {
                    break;
                }

                if (event.getRawY() - mContactY > -(this.getMeasuredHeight() / 2)) {
                    if (mListLesson.size() != 0 || mListInformations.size() != 0) {
                        Object item = mListLesson.get(0);
                        if (item instanceof LessonObject) {
                            LessonObject lessonObj = (LessonObject) item;
                            if (lessonObj.getmId() != null){
                                showMenu();
                            }
                        }
                    }
                } else {
                    hideMenu();
                }
                break;
        }

        return true;

    }


    @Override
    public void hide_layout() {
        Log.e("Interface","hide layout interface called");
        hideMenu();
        SCGlobalUtils.from_slide_menu = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                        Thread.sleep(1000);
                        Intent i = new Intent(mContext, SCMainActivity.class);
                        mContext.startActivity(i);
                        ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_left);

                } catch (Exception e) {

                }
            }
        }).start();

    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                hideMenu();
                Log.e("SCSlideMenu fling up", "Flinging upppp!");
                return true; // Bottom to top
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                //setVisibility(View.VISIBLE);
                //Log.e("SCSlideMenu fling down", "Flinging downnnnn!");
                return false; // Top to bottom
            }

            return false;
        }
    }

    public void setUserInfo(SCUserObject userObj) {
        final SCSlideMenu slideMenu = this;
        mUserObj = userObj;

        if (mUserObj.getIsGuest() != null && mUserObj.getIsGuest().equals("false")) {
            //requestGetLessons();
            mIsLoadLessonsDone =  true;
            requestGetInformations();
        } else {
            mIsLoadInfoDone = true;
            mIsLoadLessonsDone = true;
        }

        showInfo();

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isNotLoadDone = true;
                try {
                    while (isNotLoadDone) {
                        if (mIsLoadInfoDone && mIsLoadLessonsDone) {
                            isNotLoadDone = false;

                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initNotificationList();

                                    if (mContext.getPackageName().equals(SCConstants.PACKAGE_TORETAN_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_TORETAN_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_TORETAN_STAGING)) {


                                        new SCMultipleScreenToretan(mContext);

                                        if (mUserObj.getIsGuest().equals("false")) {
                                            if (mListInformations.size() == 0) {
                                                //mActuallySize = SCMultipleScreen.getValueAfterResize(SLIDE_MENU_HEIGHT_MEDIUM);
                                                mActuallySize = SCMultipleScreenToretan.getValueAfterResize(SLIDE_MENU_HEIGHT_MIN);
                                            } else {
                                                //mActuallySize = SCMultipleScreen.getValueAfterResize(SLIDE_MENU_HEIGHT_MAX);
                                                mActuallySize = SCMultipleScreenToretan.getValueAfterResize(400);
                                            }
                                        } else {
                                            mActuallySize = SCMultipleScreenToretan.getValueAfterResize(200);
                                        }
                                    }

                                    else{
                                        new SCMultipleScreen(mContext);

                                        if (mUserObj.getIsGuest()!= null && !mUserObj.getIsGuest().equals("") && mUserObj.getIsGuest().equals("false")) {
                                            if (mListInformations.size() == 0) {
                                                //mActuallySize = SCMultipleScreen.getValueAfterResize(SLIDE_MENU_HEIGHT_MEDIUM);
                                                mActuallySize = SCMultipleScreen.getValueAfterResize(SLIDE_MENU_HEIGHT_MIN);
                                            } else {
                                                //mActuallySize = SCMultipleScreen.getValueAfterResize(SLIDE_MENU_HEIGHT_MAX);
                                                mActuallySize = SCMultipleScreen.getValueAfterResize(SLIDE_MENU_HEIGHT_MEDIUM);
                                            }
                                        } else {
                                            mActuallySize = SCMultipleScreen.getValueAfterResize(SLIDE_MENU_HEIGHT_MIN);
                                        }
                                    }




                                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                            mActuallySize);
                                    mRlRoot.setLayoutParams(lp);


                                    if (mListLesson.size() != 0 || mListInformations.size() != 0) {
                                        Object item = mListLesson.get(0);
                                        if (item instanceof LessonObject) {
                                            LessonObject lessonObj = (LessonObject) item;
                                            if (lessonObj.getmId() != null){
                                                slideMenu.showMenu();
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }

                    Thread.sleep(200);
                } catch (Exception e) {
                    Log.e(TAG_LOG, e.getMessage().toString());
                }
            }
        }).start();
    }

    private void showInfo() {
        if (mUserObj.getNickname() != null && !mUserObj.getNickname().equals("")) {
            mTvName.setText(String.format(
                    getResources().getString(R.string.slide_menu_welcome), mUserObj.getNickname()));
        }
    }

    public String getBase64(final String input) {
        return Base64.encodeBytes(input.getBytes(), Base64.DONT_BREAK_LINES);
    }
}
