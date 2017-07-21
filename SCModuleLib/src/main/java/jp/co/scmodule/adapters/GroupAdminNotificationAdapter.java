package jp.co.scmodule.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jp.co.scmodule.ECDetailProductActivity;
import jp.co.scmodule.R;
import jp.co.scmodule.SCMainActivity;
import jp.co.scmodule.SCSchemeActivity;
import jp.co.scmodule.adapters.holders.SCNotificationHolder;
import jp.co.scmodule.objects.LessonObject;
import jp.co.scmodule.objects.SCBannerItem;
import jp.co.scmodule.objects.SCInformationObject;
import jp.co.scmodule.objects.SCSectionObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;
import jp.co.scmodule.utils.SCUrlConstants;
import jp.co.scmodule.widgets.SCSectionedBaseAdapter;

/**
 * Created by WebHawks IT on 10/31/2016.
 */

public class GroupAdminNotificationAdapter extends SCSectionedBaseAdapter {
    private Context mContext = null;
    private Activity mActivity = null;
    private View.OnClickListener mOnClickListener = null;
    private SCNotificationHolder mHolder = null;
    private ArrayList<SCSectionObject> mListSection = null;
    private ArrayList<SCBannerItem> mListBanners = null;
    private int TORETAN_MAX_SIZE = 5;
    private int APP_MAX_SIZE = 2;
    private boolean check = false;
    private GroupAdminNotificationAdapter.hide listener;
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


    private int[] mMainOnBgs = {
            R.drawable.item_notifcation_on_1,
            R.drawable.item_notifcation_on_2,
            R.drawable.item_notifcation_on_3,
            R.drawable.item_notifcation_on_4,
            R.drawable.item_notifcation_on_5
    };


    public GroupAdminNotificationAdapter(Context context, ArrayList<SCSectionObject> listSection, int showitem) {
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
            if (section == 0) {
                TORETAN_MAX_SIZE = mListSection.get(section).getmListData().size();
            }

            if (section == 1) {
                APP_MAX_SIZE = mListSection.get(section).getmListData().size();
            }
        } else {
            //TODO: below codes for remove list lesson (temp)
            APP_MAX_SIZE = mListSection.get(section).getmListData().size();
        }
    }

    @Override
    public int getCountForSection(int section) {
        //TODO: below codes for remove list lesson (temp)
        if (section == 1) {
            if (mListSection.get(section).getmListData().size() < APP_MAX_SIZE) {
                APP_MAX_SIZE = mListSection.get(section).getmListData().size();
            }
            return APP_MAX_SIZE;
        }

        return mListSection.get(section).getmListData().size();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
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

    }


    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
//        if (convertView == null) {
        LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflator.inflate(R.layout.item_notification_header, null);
        mHolder = new SCNotificationHolder();

        mHolder.tvHeaderTitle = (TextView) convertView.findViewById(R.id.notification_header_tv_title);
        if (mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            mHolder.tvHeaderTitle.setTextColor(mContext.getResources().getColor(R.color.common_sc_main_color) );
        } else if (mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            mHolder.tvHeaderTitle.setTextColor(mContext.getResources().getColor(R.color.canpass_main) );
        }
        else if (mContext.getPackageName().equals(SCConstants.PACKAGE_TORETAN_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_TORETAN_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_TORETAN_STAGING)) {
            mHolder.tvHeaderTitle.setTextColor(mContext.getResources().getColor(R.color.toretan_main) );
        }

        new SCMultipleScreen(mContext);
        SCMultipleScreen.resizeAllView((ViewGroup) convertView);

//            convertView.setTag(mHolder);
//        } else {
//            mHolder = (SCNotificationHolder) convertView.getTag();
//        }
        String title = "";

        //TODO: below codes for remove list lesson (temp)
        SCSectionObject s1 = mListSection.get(0);
        if (section == 0 && s1.getmSectionSize() == 0) {
            title = mContext.getResources().getString(R.string.app_notification_on_empty_header);
        } else if (section == 0 && s1.getmSectionSize() != 0) {
            title = String.format(mContext.getResources().getString(R.string.app_notification_on_header),
                    s1.getmSectionSize());
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
    }


    private void setListenerForView() {
        mHolder.btnMain.setOnClickListener(mOnClickListener);
        mHolder.tvFooterViewAll.setOnClickListener(mOnClickListener);
        mHolder.tvFooterToretan.setOnClickListener(mOnClickListener);
    }

    private void initListener(final int section, final int position) {
        mOnClickListener = new View.OnClickListener() {

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
        viewAll(section);
        notifyDataSetChanged();


    }

    public static interface hide {
        public void hide_layout();
    }

    private void afterClickMain(int section, int position) {
        int pos = 0;

        if (section == pos) {
            SCInformationObject infoObj = (SCInformationObject) mListSection.get(section).getmListData().get(position);
            String url = "";
            if (infoObj.getAppObj() == null) {
                url = infoObj.getApplication_url();
                if (url != null && !url.equals("")) {
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
                    if (url != null && !url.equals("")) {
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
