package jp.co.scmodule.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.co.scmodule.R;
import jp.co.scmodule.SCGroupAdminPage;
import jp.co.scmodule.objects.SCMemberObject;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.widgets.SCCircleImageView;
import jp.co.scmodule.widgets.SCSingleLineTextView;

/**
 * Created by WebHawks IT on 6/20/2016.
 */
public class SCMemberListAdapter extends BaseAdapter {

    private Activity mActivity = null;
    private ArrayList<SCMemberObject> mListGroup = null;
    private int max_gauge  = 1000;

    private DisplayImageOptions mImageLoaderOpts = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_avatar)
            .showImageForEmptyUri(R.drawable.default_avatar)
            .showImageOnFail(R.drawable.default_avatar)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true).cacheOnDisk(false).considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();

    public SCMemberListAdapter(Activity activity, ArrayList<SCMemberObject> mListGroup) {
        this.mActivity = activity;
        this.mListGroup = mListGroup;

        Collections.sort(mListGroup, new Comparator<SCMemberObject>() {
            @Override
            public int compare(SCMemberObject o1, SCMemberObject o2) {

                    int newest1 = Integer.parseInt(o1.getCampus_point());
                    int newest2 = Integer.parseInt(o2.getCampus_point());;

                    return Integer.compare(newest2, newest1);

            }
        });

        max_gauge =  Integer.parseInt(this.mListGroup.get(0).getCampus_point());

    }

    @Override
    public int getCount() {
        return mListGroup.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.member_list_item, null);
            holder = new ViewHolder();
            holder.image = (SCCircleImageView) convertView.findViewById(R.id.scmain_img_avatar);
            holder.name = (TextView) convertView.findViewById(R.id.item_choosen_tv_name);
            holder.campus_point = (SCSingleLineTextView) convertView.findViewById(R.id.item_choosen_tv_no);
            holder.mPbMoneyLine = (ProgressBar) convertView.findViewById(R.id.scmain_pb_money_line);
            new SCMultipleScreen(mActivity);
            SCMultipleScreen.resizeAllView((ViewGroup) convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mActivity.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || mActivity.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || mActivity.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            holder.mPbMoneyLine.setProgressDrawable(mActivity.getResources().getDrawable(R.drawable.common_pb_horizontal));
        } else if (mActivity.getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || mActivity.getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || mActivity.getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            holder.mPbMoneyLine.setProgressDrawable(mActivity.getResources().getDrawable(R.drawable.common_pb_horizontal_canpass));
        }

        SCMemberObject memberObject = new SCMemberObject();
        memberObject = (SCMemberObject) mListGroup.get(position);
        holder.name.setText(memberObject.getNickname());
        holder.campus_point.setText(memberObject.getCampus_point()+"pt");
        long campus_point = 0;
        if (memberObject.getCampus_point() != null && !memberObject.getCampus_point().equals("")) {
            campus_point = Integer.parseInt(memberObject.getCampus_point());
        }

        Double gauge_value = ((double)campus_point/max_gauge) * 100;
        holder.mPbMoneyLine.setProgress(gauge_value.intValue());
        ImageLoader.getInstance().displayImage(memberObject.getIcon(), holder.image, mImageLoaderOpts);

        return convertView;
    }

    static class ViewHolder {
        SCCircleImageView image;
        TextView name;
        SCSingleLineTextView campus_point;
        ProgressBar mPbMoneyLine;
    }
}
