package jp.co.scmodule.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import jp.co.scmodule.R;
import jp.co.scmodule.adapters.holders.SCSlideBannerHolder;
import jp.co.scmodule.objects.SCBannerItem;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.CorrectSizeUtil;


/**
 * Created by VNCCO on 9/8/2015.
 */
public class SCSlideBannerAdapter extends BaseAdapter {
    SCSlideBannerHolder mHolder;
    Activity mActivity;
    ArrayList<SCBannerItem> mListItem;
    DisplayImageOptions option = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true)
            .showImageOnLoading(R.color.line_config)
            .showImageOnFail(R.color.line_config)
            .build();
    SCUserObject mUser;

    public SCSlideBannerAdapter(Activity activity, ArrayList<SCBannerItem> listBanner) {
        this.mActivity = activity;
        this.mListItem = listBanner;
        mUser = SCUserObject.getInstance();
    }

    @Override
    public int getCount() {
        return mListItem.size()+2;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.sc_item_slide_ad, null);
            mHolder = new SCSlideBannerHolder();
            mHolder.imageView = (ImageView) convertView.findViewById(R.id.item_slide_ad_img);
            mHolder.vPadding = (View) convertView.findViewById(R.id.v_padding_right_slide_adv);
            CorrectSizeUtil.getInstance(mActivity).correctSize(convertView);

            CorrectSizeUtil.getInstance(mActivity).correctSize(convertView);
          //  convertView.setTag(mHolder);
//        } else {
//            mHolder = (SlideBannerHolder) convertView.getTag();
//        }

        if(position == 0 || position == this.getCount()-1 ){
            mHolder.imageView.setBackgroundColor(Color.BLACK);
            if(position == 0){
                mHolder.imageView.setLayoutParams(new LinearLayout.LayoutParams(280, 240));
            }else {
                mHolder.imageView.setLayoutParams(new LinearLayout.LayoutParams(280, 240));

            }
            mHolder.vPadding.setVisibility(View.GONE);
            //mHolder.vPadding.setVisibility(View.GONE);
            CorrectSizeUtil.getInstance(mActivity).correctSize(mHolder.imageView);
        }else {
            //show infor
            if (position == this.getCount() - 2) {
                mHolder.vPadding.setVisibility(View.GONE);
            } else {
              //  mHolder.vPadding.setVisibility(View.VISIBLE);
            }
            ImageLoader.getInstance().displayImage(mListItem.get(position - 1).getBannerImage(), mHolder.imageView, option);
            //action
            mHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mListItem.get(position - 1).runAPIClickBanner(mUser.getAppId(), ConstantsUtil.APPLICATION_ID, mActivity);
                }
            });
            convertView.setTag(mListItem.get(position - 1));
        }
        return convertView;
    }
}
