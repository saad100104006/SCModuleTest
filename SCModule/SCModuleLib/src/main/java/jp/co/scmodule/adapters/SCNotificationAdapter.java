package jp.co.scmodule.adapters;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.sephiroth.android.library.widget.AbsHListView;
import it.sephiroth.android.library.widget.HListView;
import jp.co.scmodule.ECDetailProductActivity;
import jp.co.scmodule.R;
import jp.co.scmodule.SCMainActivity;
import jp.co.scmodule.SCSchemeActivity;
import jp.co.scmodule.WebviewActivity;
import jp.co.scmodule.adapters.holders.SCNotificationHolder;
import jp.co.scmodule.interfaces.SCTimerCallback;
import jp.co.scmodule.objects.CampusanAdObject;
import jp.co.scmodule.objects.LessonObject;
import jp.co.scmodule.objects.SCBannerItem;
import jp.co.scmodule.objects.SCInformationObject;
import jp.co.scmodule.objects.SCLessonObject;
import jp.co.scmodule.objects.SCSectionObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;
import jp.co.scmodule.utils.SCUrlConstants;
import jp.co.scmodule.widgets.SCSectionedBaseAdapter;
import jp.co.scmodule.widgets.SCSlideMenu;

import static com.facebook.FacebookSdk.getApplicationContext;


public class SCNotificationAdapter extends SCSectionedBaseAdapter {
    private Context mContext = null;
    private Activity mActivity = null;
    private OnClickListener mOnClickListener = null;
    private SCNotificationHolder mHolder = null;
    private ArrayList<SCSectionObject> mListSection = null;
    private ArrayList<SCBannerItem> mListBanners = null;
    private int TORETAN_MAX_SIZE = 5;
    private int APP_MAX_SIZE = 2;
    private boolean check = false;
    private hide listener;

    DisplayImageOptions option = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true)
            .showImageOnLoading(R.color.line_config)
            .showImageOnFail(R.color.line_config)
            .build();
    //private Thread mBannerAutoScrollThread = null;
    //public HListView mSlideAdv = null;
//    private int mFirstVisibleAdPosition = 0;
//    private int mVisibleItem = 0;
//    private boolean mIsTouchBanner = false;
//    private static final int AUTO_SCROLL_TIMER = 10000;
//    SCSlideBannerAdapter slideBannerAdapter;
//    private View.OnTouchListener mHeaderOnTouchListener = null;

    private ImageView ad_image_one = null;
    private ImageView ad_image_two = null;
    CampusanAdObject adObject_one = null;
    CampusanAdObject adObject_two = null;

    private int[] mMainOnBgs = {
            R.drawable.item_notifcation_on_1,
            R.drawable.item_notifcation_on_2,
            R.drawable.item_notifcation_on_3,
            R.drawable.item_notifcation_on_4,
            R.drawable.item_notifcation_on_5
    };

    public SCNotificationAdapter(Context context, ArrayList<SCSectionObject> listSection, boolean show_lesson) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mListSection = listSection;
        check = show_lesson;
        mActivity = (Activity) context;

        if (check) {

        } else {
            //TODO: below codes for remove list lesson (temp)
            mListSection.remove(1);
        }
    }

    public SCNotificationAdapter(Context context, ArrayList<SCSectionObject> listSection, boolean show_lesson, hide listener) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mListSection = listSection;
        check = show_lesson;
        this.listener = listener;
        if (check) {

        } else {
            //TODO: below codes for remove list lesson (temp)
            mListSection.remove(0);
        }
    }


    public SCNotificationAdapter(Context context, ArrayList<SCSectionObject> listSection, int showitem) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mListSection = listSection;
        APP_MAX_SIZE = showitem;
        if (check) {

        } else {
            //TODO: below codes for remove list lesson (temp)
            mListSection.remove(0);
        }
    }

    @Override
    public Object getItem(int section, int position) {
        return mListSection.get(section).getmListData().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return mListSection.size();
    }

    private void viewAll(int section) {
        if (check) {
            if (section == 1) {
                TORETAN_MAX_SIZE = mListSection.get(section).getmListData().size();
            }

            if (section == 2) {
                APP_MAX_SIZE = mListSection.get(section).getmListData().size();
            }
        } else {
            //TODO: below codes for remove list lesson (temp)
            APP_MAX_SIZE = mListSection.get(section).getmListData().size();
        }
    }

    @Override
    public int getCountForSection(int section) {

        if (check) {
            if (section == 1) {
                if (mListSection.get(section).getmListData().size() < TORETAN_MAX_SIZE) {
                    TORETAN_MAX_SIZE = mListSection.get(section).getmListData().size();
                }
                return TORETAN_MAX_SIZE;
            } else if (section == 2) {
                if (mListSection.get(section).getmListData().size() < APP_MAX_SIZE) {
                    APP_MAX_SIZE = mListSection.get(section).getmListData().size();
                }
                return APP_MAX_SIZE;
            } else if (section == 3) {
                return 1;
            } else if (section == 0) {
                return 1;
            }

            return mListSection.get(section).getmListData().size();
        } else {
            //TODO: below codes for remove list lesson (temp)
            if (section == 1) {
                if (mListSection.get(section).getmListData().size() < APP_MAX_SIZE) {
                    APP_MAX_SIZE = mListSection.get(section).getmListData().size();
                }
                return APP_MAX_SIZE;
            } else if (section == 2) {
                return 1;
            } else if (section == 0) {
                return 1;
            }

            return mListSection.get(section).getmListData().size();
        }
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {

        if ((check && (section == 1 || section == 2)) || (!check && section == 1)) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.item_notification, null);
            mHolder = new SCNotificationHolder();

            mHolder.tvNumber = (TextView) convertView.findViewById(R.id.notification_tv_number);
            mHolder.tvBody = (TextView) convertView.findViewById(R.id.notification_tv_body);
            mHolder.btnMain = (Button) convertView.findViewById(R.id.notification_btn_main);
            mHolder.rlMain = (RelativeLayout) convertView.findViewById(R.id.notification_rl_main);
            mHolder.rlFooter = (RelativeLayout) convertView.findViewById(R.id.notification_footer_rl);
            mHolder.tvFooterViewAll = (TextView) convertView.findViewById(R.id.notification_footer_tv_show_all);
            mHolder.tvFooterToretan = (TextView) convertView.findViewById(R.id.notification_footer_tv_toretan);
            mHolder.tvUnit = (TextView) convertView.findViewById(R.id.notification_tv_unit);

            new SCMultipleScreen(mContext);
            SCMultipleScreen.resizeAllView((ViewGroup) convertView);

            convertView.setTag(mHolder);
//        } else {
//            mHolder = (SCNotificationHolder) convertView.getTag();
//        }

            LessonObject lessonObj = null;
            SCInformationObject infoObj = null;

            Object item = mListSection.get(section).getmListData().get(position);

            if (item instanceof LessonObject) {
                lessonObj = (LessonObject) item;
                showViewWithLessonObject(lessonObj, position);
            }

            if (item instanceof SCInformationObject) {
                infoObj = (SCInformationObject) item;
                showViewWithInfoObject(infoObj, position);
            }

            mHolder.btnMain.setContentDescription("main");
            mHolder.tvFooterViewAll.setContentDescription("viewAll");
            mHolder.tvFooterToretan.setContentDescription("toretanText");

            // set listener
            initListener(section, position);
            setListenerForView();

            return convertView;
        } else {
            if (section == 0) {
                LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflator.inflate(R.layout.item_point_by_login, null);

                ImageView one = (ImageView) convertView.findViewById(R.id.image_one);
                ImageView two = (ImageView) convertView.findViewById(R.id.image_two);
                ImageView three = (ImageView) convertView.findViewById(R.id.image_three);
                ImageView four = (ImageView) convertView.findViewById(R.id.image_four);
                ImageView five = (ImageView) convertView.findViewById(R.id.image_five);

                final TextView tvOld = (TextView) convertView.findViewById(R.id.tv_old);
                final TextView tvNew = (TextView) convertView.findViewById(R.id.tv_new);
                final TextView tv_last_label = (TextView) convertView.findViewById(R.id.tv_last_label);

                View[] views = new View[]{};

                String old_value = SCGlobalUtils.old_point;
                String new_value = SCGlobalUtils.new_point;
                int remainng = 5;
                if (new_value != null && !new_value.equals("")) {
                    remainng = 5 - Integer.parseInt(new_value);
                } else {
                    SCGlobalUtils.new_point = new_value = "0";
                    if (old_value != null && !old_value.equals("")) {
                        new_value = old_value;
                        remainng = 5 - Integer.parseInt(new_value);
                    } else {
                        SCGlobalUtils.old_point = old_value = "0";
                    }
                }
                tv_last_label.setText("ポイントGET\n" +
                        "まであと" + remainng + "日");

                tvOld.setText(old_value);
                tvNew.setText(new_value);

                int coin_on = R.drawable.get_count_coin_image_on;

                if (mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
                    coin_on = R.drawable.get_count_coin_image_on;
                    tv_last_label.setBackgroundResource(R.drawable.tv_yellow_backgroung);
                } else if (mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
                    coin_on = R.drawable.get_count_coin_image_on_cp;
                    tv_last_label.setBackgroundResource(R.drawable.tv_yellow_backgroung_canpass);
                }
                switch (new_value) {
                    case "0":
                        break;
                    case "1":
                        one.setImageResource(coin_on);
                        views = new View[]{tvOld, one, tv_last_label};
                        break;
                    case "2":
                        one.setImageResource(coin_on);
                        two.setImageResource(coin_on);
                        views = new View[]{tvOld, one, two, tv_last_label};
                        break;
                    case "3":
                        one.setImageResource(coin_on);
                        two.setImageResource(coin_on);
                        three.setImageResource(coin_on);
                        views = new View[]{tvOld, one, two, three, tv_last_label};
                        break;
                    case "4":
                        one.setImageResource(coin_on);
                        two.setImageResource(coin_on);
                        three.setImageResource(coin_on);
                        four.setImageResource(coin_on);
                        views = new View[]{tvOld, one, two, three, four, tv_last_label};
                        break;
                    case "5":
                        one.setImageResource(coin_on);
                        two.setImageResource(coin_on);
                        three.setImageResource(coin_on);
                        four.setImageResource(coin_on);
                        five.setImageResource(coin_on);
                        views = new View[]{tvOld, one, two, three, four, five, tv_last_label};
                        break;
                }

                if (!SCGlobalUtils.old_point.equals(SCGlobalUtils.new_point)) {
                    SCGlobalUtils.old_point = SCGlobalUtils.new_point;
                    // 100ms delay between Animations
                    long delayBetweenAnimations = 500l;

                    for (int i = 0; i < views.length; i++) {
                        final View view = views[i];

                        // We calculate the delay for this Animation, each animation starts 100ms
                        // after the previous one
                        int delay = (int) (i * delayBetweenAnimations);

                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (view.getId() == R.id.tv_old) {
                                    Animation animMove = AnimationUtils.loadAnimation(getApplicationContext(),
                                            jp.co.scmodule.R.anim.move_animation);
                                    tvOld.startAnimation(animMove);
                                    tvNew.startAnimation(animMove);
                                } else if (view.getId() == R.id.tv_last_label) {
                                    Animation animblink = AnimationUtils.loadAnimation(getApplicationContext(),
                                            jp.co.scmodule.R.anim.blink_animation);
                                    tv_last_label.startAnimation(animblink);
                                } else {
                                    flipit(view);

                                }
                            }
                        }, delay);
                    }


                    Animation animMove = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.move_animation);
                    tvOld.startAnimation(animMove);
                    tvNew.startAnimation(animMove);
                } else {
                    Animation animMove = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.move_animation);
                    animMove.setDuration(0);
                    tvOld.startAnimation(animMove);
                    tvNew.startAnimation(animMove);
                }
                new SCMultipleScreen(mContext);
                SCMultipleScreen.resizeAllView((ViewGroup) convertView);
                return convertView;
            } else {
                LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflator.inflate(R.layout.item_ad_sc_dashboard, null);

                ad_image_one = (ImageView) convertView.findViewById(R.id.ad_image_one);
                ad_image_two = (ImageView) convertView.findViewById(R.id.ad_image_two);

                TextView ad_tv_one = (TextView) convertView.findViewById(R.id.ad_text_one);
                TextView ad_tv_two = (TextView) convertView.findViewById(R.id.ad_text_two);

                if (mListSection.get(section).getmListData().size() != 0) {
                    Object item_one = mListSection.get(section).getmListData().get(0);
                    Object item_two = mListSection.get(section).getmListData().get(1);

                    if (item_one instanceof CampusanAdObject) {
                        adObject_one = (CampusanAdObject) item_one;
                        ad_tv_one.setText(adObject_one.getName());
                        ImageLoader.getInstance().displayImage(adObject_one.getHalf_width_image_url(), ad_image_one, option);
                        ad_image_one.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Activity activity = (Activity) mContext;
                                Intent mIntent = new Intent(mContext, WebviewActivity.class);
                                mIntent.putExtra("url", "autoLoginWebview");
                                mIntent.putExtra("collectionUrl", adObject_one.getCollection_url());
                                mIntent.putExtra("title", adObject_one.getName());
                                mContext.startActivity(mIntent);
                                activity.overridePendingTransition(R.anim.anim_slide_in_bottom,
                                        R.anim.anim_scale_to_center);
                            }
                        });

                    } else {
                        ad_image_one.setVisibility(View.GONE);
                        ad_tv_one.setVisibility(View.GONE);

                    }

                    if (item_two instanceof CampusanAdObject) {
                        adObject_two = (CampusanAdObject) item_two;
                        ad_tv_two.setText(adObject_two.getName());
                        ImageLoader.getInstance().displayImage(adObject_two.getHalf_width_image_url(), ad_image_two, option);
                        ad_image_two.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //open a webview
                                Activity activity = (Activity) mContext;
                                Intent mIntent = new Intent(mContext, WebviewActivity.class);
                                mIntent.putExtra("url", "autoLoginWebview");
                                mIntent.putExtra("collectionUrl", adObject_two.getCollection_url());
                                mIntent.putExtra("title", adObject_two.getName());
                                mContext.startActivity(mIntent);
                                activity.overridePendingTransition(R.anim.anim_slide_in_bottom,
                                        R.anim.anim_scale_to_center);
                            }
                        });

                    } else {
                        ad_image_two.setVisibility(View.GONE);
                        ad_tv_two.setVisibility(View.GONE);

                    }

                }

                new SCMultipleScreen(mContext);
                SCMultipleScreen.resizeAllView((ViewGroup) convertView);
                return convertView;
            }

        }
    }

    private void flipit(final View viewToFlip) {
        ObjectAnimator flip = ObjectAnimator.ofFloat(viewToFlip, "rotationY", 0f, 360f);
        flip.setDuration(250);

        //flip.setRepeatCount(1);
        flip.start();

    }

    //added ads bt didnt need
//    public void LoadAdv(final ArrayList<SCBannerItem> mListBanners) {
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<SCBannerItem> mListBannerItem = mListBanners;
//                slideBannerAdapter = new SCSlideBannerAdapter(mActivity, mListBannerItem);
//                mSlideAdv.setAdapter(slideBannerAdapter);
//                mSlideAdv.setOnTouchListener(mHeaderOnTouchListener);
//                SCGlobalUtils.setTimer(AUTO_SCROLL_TIMER, new SCTimerCallback() {
//                    @Override
//                    public void timeUp() {
//                        autoSlide();
//                    }
//                },mActivity);
//                //set onscroll
//                mSlideAdv.setOnScrollListener(new AbsHListView.OnScrollListener() {
//                    @Override
//                    public void onScrollStateChanged(AbsHListView absHListView, int scrollState) {
//
//
//                    }
//
//                    @Override
//                    public void onScroll(AbsHListView absHListView, int firstVisibleItem, int visibleItem, int i2) {
//                        mFirstVisibleAdPosition = firstVisibleItem;
//                        mVisibleItem = visibleItem;
//
//                    }
//                });
//
//
//            }
//        });
//    }

    //ads autoslide
//    private void autoSlide() {
//        mBannerAutoScrollThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    while (!mIsTouchBanner) {
//                        mActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                View item = mSlideAdv.getChildAt(0);
//                                View item0 = mSlideAdv.getChildAt(1);
//                                int[] loc0 = new int[2];
//                                if (item0 != null) {
//                                    item0.getLocationOnScreen(loc0);
//                                }
//                                if (item != null && item0 != null) {
//                                    if (mFirstVisibleAdPosition == 0 && mVisibleItem == 3) {
//                                        //mSlideAdv.smoothScrollBy(Math.round((item0.getMeasuredWidth() + item.getMeasuredWidth() + mCorrectSize.getValueAfterResize(38 * 2)) / 2), 250);
//                                        mSlideAdv.smoothScrollBy(Math.round((item0.getMeasuredWidth() / 2 + item.getMeasuredWidth())+45), 250);
//                                    } else if (mFirstVisibleAdPosition == mSlideAdv.getAdapter().getCount() - 3) {
//                                        mSlideAdv.smoothScrollToPosition(0);
//                                    } else if (mFirstVisibleAdPosition == mSlideAdv.getAdapter().getCount() - 2) {
//                                        mSlideAdv.smoothScrollToPosition(0);
//                                    } else {
//                                        // mSlideAdv.smoothScrollBy(Math.round((item0.getMeasuredWidth() + item.getMeasuredWidth())), 250);
//                                        mSlideAdv.smoothScrollBy(Math.round((item0.getMeasuredWidth() + item.getMeasuredWidth())), 300);
//                                    }
//
//                                        View item1 = mSlideAdv.getChildAt(1);
//                                        if (item1 != null) {
//                                            Object itemViewed = item1.getTag();
//                                            if (itemViewed instanceof SCBannerItem) {
//                                                SCBannerItem bannerItem = (SCBannerItem) itemViewed;
//                                                //open it before release
//                                               // bannerItem.runAPILookBanner(userObj.getAppId(), ConstantsUtil.APPLICATION_ID, mActivity);
//                                                // Log.e("BannerSend",bannerItem.getBannerId());
//                                            }
//
//                                    }
//                                }
//                            }
//                        });
//
//
//                        Thread.sleep(AUTO_SCROLL_TIMER);
//                    }
//                } catch (Exception e) {
//
//                }
//
//            }
//        });
//
//        mBannerAutoScrollThread.start();
//    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
//        if (convertView == null) {
        LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflator.inflate(R.layout.item_notification_header, null);
        mHolder = new SCNotificationHolder();

        mHolder.tvHeaderTitle = (TextView) convertView.findViewById(R.id.notification_header_tv_title);
        if (mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            mHolder.tvHeaderTitle.setTextColor(mContext.getResources().getColor(R.color.common_sc_main_color));
        } else if (mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            mHolder.tvHeaderTitle.setTextColor(mContext.getResources().getColor(R.color.canpass_main));
        }
        new SCMultipleScreen(mContext);
        SCMultipleScreen.resizeAllView((ViewGroup) convertView);

//            convertView.setTag(mHolder);
//        } else {
//            mHolder = (SCNotificationHolder) convertView.getTag();
//        }
        String title = "";
        if (check) {
            SCSectionObject s1 = mListSection.get(1);
            SCSectionObject s2 = mListSection.get(2);

            if (section == 1 && s1.getmSectionSize() == 0) {
                title = mContext.getResources().getString(R.string.toretan_notification_empty_header);
            } else if (section == 1 && s1.getmSectionSize() != 0) {
                title = String.format(mContext.getResources().getString(R.string.toretan_notification_on_header),
                        s1.getmSectionSize());
            }

            if (section == 2 && s2.getmSectionSize() == 0) {
                title = mContext.getResources().getString(R.string.app_notification_on_empty_header);
            } else if (section == 2 && s2.getmSectionSize() != 0) {
                title = String.format(mContext.getResources().getString(R.string.app_notification_on_header),
                        s1.getmSectionSize());
            }

            if (section == 3) {
                title = "あなたにオススメのバイト特集";
            }
            if (section == 0) {
                mHolder.tvHeaderTitle.setVisibility(View.GONE);
            }
        } else {
            //TODO: below codes for remove list lesson (temp)
            SCSectionObject s1 = mListSection.get(1);
            if (section == 1 && s1.getmSectionSize() == 0) {
                title = mContext.getResources().getString(R.string.app_notification_on_empty_header);
            } else if (section == 1 && s1.getmSectionSize() != 0) {
                title = String.format(mContext.getResources().getString(R.string.app_notification_on_header),
                        s1.getmSectionSize());
            }

            if (section == 2) {
                title = "あなたにオススメのバイト特集";
            }
            if (section == 0) {
                mHolder.tvHeaderTitle.setVisibility(View.GONE);
            }
        }

        // set header text
        mHolder.tvHeaderTitle.setText(title);

        return convertView;
    }

    private void showViewWithInfoObject(SCInformationObject infoObj, int position) {
        if (infoObj == null) {
            return;
        }

        if (infoObj.getTitle() != null) {
            mHolder.tvBody.setText(infoObj.getTitle());
        }
        mHolder.tvUnit.setVisibility(View.GONE);
        mHolder.tvNumber.setText("");
        if (mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            mHolder.rlMain.setBackgroundResource(R.drawable.item_notifcation_specical);
        } else if (mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            mHolder.rlMain.setBackgroundResource(R.drawable.item_notifcation_canpass_specical);
        }
        if (position == APP_MAX_SIZE - 1) {
            mHolder.rlFooter.setVisibility(View.VISIBLE);
            mHolder.tvFooterToretan.setVisibility(View.GONE);
            int position_section = 0;
            if (check) {
                position_section = 1;
            } else {
                position_section = 0;
            }
//            if(mListSection.get(1).getmSectionSize() <= APP_MAX_SIZE) {
            //TODO: below codes for remove list lesson (temp)
            if (mListSection.get(position_section).getmSectionSize() <= APP_MAX_SIZE) {
                mHolder.rlFooter.setVisibility(View.GONE);
                mHolder.tvFooterViewAll.setVisibility(View.GONE);
            } else {
                mHolder.rlFooter.setVisibility(View.VISIBLE);
                mHolder.tvFooterViewAll.setVisibility(View.VISIBLE);
            }
        } else {
            mHolder.rlFooter.setVisibility(View.GONE);
        }
    }

    private void showViewWithLessonObject(LessonObject lessonObj, int position) {
        if (lessonObj == null) {
            return;
        }

        if (SCUserObject.getInstance().getIsGuest().equals("false")) {
            mHolder.tvUnit.setVisibility(View.VISIBLE);
            if (lessonObj.getmId() == null && position < 3) {
                mHolder.tvBody.setText("");
                mHolder.tvNumber.setTextColor(mContext.getResources().getColor(R.color.common_toretan_class_empty));
                mHolder.tvNumber.setText(String.valueOf(position + 1));
                mHolder.rlMain.setBackgroundResource(R.drawable.item_notifcation_off);
            }
            if (lessonObj.getmId() != null) {
                if (lessonObj.getmRooms().size() != 0 || lessonObj.getmTeachers().size() != 0) {
                    String s = "";
                    if (lessonObj.getmRooms().size() != 0) {
                        s = lessonObj.getmRooms().get(0);
                    }
                    if (lessonObj.getmTeachers().size() != 0) {
                        if (!s.equals(""))
                            s = s + "/" + lessonObj.getmTeachers().get(0);
                        else
                            s = lessonObj.getmTeachers().get(0);
                    }
                    mHolder.tvBody.setText(lessonObj.getmName() + "/" + s);
                } else {
                    mHolder.tvBody.setText(lessonObj.getmName());
                }
                mHolder.tvNumber.setTextColor(mContext.getResources().getColor(android.R.color.white));
                mHolder.tvNumber.setText(String.valueOf(position + 1));
                if (position < 5) {
                    mHolder.rlMain.setBackgroundResource(mMainOnBgs[position]);
                } else {
                    mHolder.rlMain.setBackgroundResource(mMainOnBgs[4]);
                }
            }

            if (position == TORETAN_MAX_SIZE - 1) {
                mHolder.rlFooter.setVisibility(View.VISIBLE);
                mHolder.tvFooterToretan.setVisibility(View.VISIBLE);

                if (isToretanInstalled()) {
                    mHolder.tvFooterToretan.setText(mContext.getResources().getString(R.string.toretan_installed_notification_empty_footer));
                } else {
                    mHolder.tvFooterToretan.setText(mContext.getResources().getString(R.string.toretan_not_install_notification_empty_footer));
                }

                if (mListSection.get(0).getmSectionSize() <= TORETAN_MAX_SIZE) {
                    mHolder.tvFooterViewAll.setVisibility(View.GONE);
                } else {
                    mHolder.tvFooterViewAll.setVisibility(View.VISIBLE);
                }

            } else {
                mHolder.rlFooter.setVisibility(View.GONE);
            }
        } else {
            mHolder.rlMain.setVisibility(View.GONE);
            mHolder.rlFooter.setVisibility(View.VISIBLE);
            mHolder.tvFooterToretan.setVisibility(View.VISIBLE);
            mHolder.tvFooterViewAll.setVisibility(View.GONE);

            if (SCUserObject.getInstance().getIsGuest().equals("true")) {
                mHolder.tvFooterToretan.setText(mContext.getResources().getString(R.string.toretan_guest_footer));
            }
        }

        if (mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            mHolder.tvFooterToretan.setTextColor(mContext.getResources().getColor(R.color.common_sc_main_color));
        } else if (mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            mHolder.tvFooterToretan.setTextColor(mContext.getResources().getColor(R.color.canpass_main));
        }
    }


    private void setListenerForView() {
        mHolder.btnMain.setOnClickListener(mOnClickListener);
        mHolder.tvFooterViewAll.setOnClickListener(mOnClickListener);
        mHolder.tvFooterToretan.setOnClickListener(mOnClickListener);
    }

    private void initListener(final int section, final int position) {
        mOnClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("main")) {
                    afterClickMain(section, position);
                } else if (v.getContentDescription().equals("viewAll")) {
                    afterClickViewAll(section, position);
                } else if (v.getContentDescription().equals("toretanText")) {
                    afterClickToretanText();
                }

            }
        };
//        mHeaderOnTouchListener = new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent ev) {
//                if (v.getId() == R.id.hlv_slide_banner) {
//                    switch (ev.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                        case MotionEvent.ACTION_MOVE:
//                            mIsTouchBanner = true;
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            mIsTouchBanner = false;
//                            break;
//                    }
//                }
//
//                return false;
//            }
//        };

    }

    private void afterClickToretanText() {
        if (SCUserObject.getInstance().getIsGuest().equals("false")) {
            boolean isAppIntall = false;
            Intent intent = null;
            PackageManager pm = mContext.getPackageManager();
            try {
                pm.getPackageInfo(SCConstants.PACKAGE_TORETAN, PackageManager.GET_ACTIVITIES);
                isAppIntall = true;
            } catch (PackageManager.NameNotFoundException e) {
                isAppIntall = false;
            }
            if (isAppIntall) {
                String packageName = SCConstants.PACKAGE_TORETAN;
                intent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
                mContext.startActivity(intent);
            } else {
                Intent intentMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + SCConstants.PACKAGE_TORETAN));
                mContext.startActivity(intentMarket);
            }
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SCUrlConstants.URL_INSTALL_TORETAN));
            mContext.startActivity(browserIntent);
        }
    }

    private void afterClickViewAll(int section, int position) {
        if (SCGlobalUtils.from_slide_menu) {
            listener.hide_layout();
        } else {
            viewAll(section);
            notifyDataSetChanged();
        }

    }

    public static interface hide {
        public void hide_layout();
    }

    private void afterClickMain(int section, int position) {
        int pos = 0;
        if (check) {
            pos = 2;
            if (section == 1) {
                afterClickToretanText();
            }
        } else {

        }

        if (section == pos) {
            SCInformationObject infoObj = (SCInformationObject) mListSection.get(section).getmListData().get(position);
            String url = "";
            if (infoObj.getAppObj() == null) {
                url = infoObj.getApplication_url();
                if (!url.equals("")) {
                    if (URLUtil.isValidUrl(url)) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mContext.startActivity(browserIntent);
                    }
                }
            } else {
                url = infoObj.getAppObj().getUrl();
                String page = null;
                String id = null;
                try {
                    List<NameValuePair> params = URLEncodedUtils.parse(new URI(url), "UTF-8");
                    page = params.get(0).getValue();
                    id = params.get(1).getValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (infoObj.getOpenType().equals("2")) {
                    if (page != null && id != null) {
                        if (page.equals(SCConstants.OPEN_PAGE_EXCHANGE_ITEM)) {
                            Intent i = new Intent(mContext, ECDetailProductActivity.class);
                            i.putExtra(String.class.toString(), id);
                            mContext.startActivity(i);
                            ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right,
                                    R.anim.anim_slide_out_left);
                        } else {
                            SCSharedPreferencesUtils.putString(mContext, SCConstants.TAG_PAGE, page);
                            SCSharedPreferencesUtils.putString(mContext, SCConstants.TAG_ID, id);

                            if (mContext instanceof SCMainActivity) {
                                ((SCMainActivity) mContext).finish();
                                ((SCMainActivity) mContext).overridePendingTransition(R.anim.anim_slide_in_left,
                                        R.anim.anim_slide_out_right);
                            } else {
                                Intent i = new Intent(mContext, SCSchemeActivity.class);
                                mContext.startActivity(i);
                            }
                        }
                    }
                } else {
                    if (!url.equals("")) {
                        if (URLUtil.isValidUrl(url)) {
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    mContext.startActivity(browserIntent);
//                }else{
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    mContext.startActivity(browserIntent);
//                }
                            //opening other apps or other URLS
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            mContext.startActivity(browserIntent);
                        }
                    }
                }

            }

        }
    }

    private boolean isToretanInstalled() {
        boolean isAppIntall = false;
        PackageManager pm = mContext.getPackageManager();
        try {
            pm.getPackageInfo(SCConstants.PACKAGE_TORETAN, PackageManager.GET_ACTIVITIES);
            isAppIntall = true;
        } catch (PackageManager.NameNotFoundException e) {
            isAppIntall = false;
        }

        return isAppIntall;
    }
}
