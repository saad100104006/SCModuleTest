package jp.co.scmodule.adapters;

/**
 * Created by WebHawks IT on 6/3/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jp.co.scmodule.R;
import jp.co.scmodule.SCAddGroup;
import jp.co.scmodule.SCChooseGroup;
import jp.co.scmodule.SCChoosenActivity;
import jp.co.scmodule.adapters.holders.SCChoosenHolder;
import jp.co.scmodule.objects.SCCampusObject;
import jp.co.scmodule.objects.SCDepartmentObject;
import jp.co.scmodule.objects.SCEnrollmentObject;
import jp.co.scmodule.objects.SCGenderObject;
import jp.co.scmodule.objects.SCGroupObject;
import jp.co.scmodule.objects.SCMajorObject;
import jp.co.scmodule.objects.SCPrefectureObject;
import jp.co.scmodule.objects.SCUniversityObject;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCMultipleScreen;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import jp.co.scmodule.R;

import jp.co.scmodule.SCChoosenActivity;
import jp.co.scmodule.adapters.holders.SCChoosenHolder;
import jp.co.scmodule.objects.SCCampusObject;
import jp.co.scmodule.objects.SCDepartmentObject;
import jp.co.scmodule.objects.SCEnrollmentObject;
import jp.co.scmodule.objects.SCGenderObject;
import jp.co.scmodule.objects.SCMajorObject;
import jp.co.scmodule.objects.SCPrefectureObject;
import jp.co.scmodule.objects.SCUniversityObject;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.utils.SCConstants;


public class SCGroupChooseAdapter extends BaseAdapter {
    private Context mContext = null;
    private View.OnClickListener mOnClickListener = null;
    private SCChoosenHolder mHolder = null;
    private ArrayList<Object> mListData = null;

    public SCGroupChooseAdapter(Context context, ArrayList<Object> listData) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mListData = listData;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.item_chosen_group, null);
            mHolder = new SCChoosenHolder();

            mHolder.tvName = (TextView) convertView.findViewById(R.id.item_choosen_tv_name);
            mHolder.tvNo = (TextView) convertView.findViewById(R.id.item_choosen_tv_no);
            mHolder.btnMain = (RelativeLayout) convertView.findViewById(R.id.item_choosen_btn_main);
            mHolder.imgNarrow = (ImageView) convertView.findViewById(R.id.item_choosen_img_narrow);

            new SCMultipleScreen(mContext);
            SCMultipleScreen.resizeAllView((ViewGroup) convertView);

            convertView.setTag(mHolder);
        } else {
            mHolder = (SCChoosenHolder) convertView.getTag();
        }

        if (mListData.get(position) instanceof SCGroupObject) {
            SCGroupObject groupObject = (SCGroupObject) mListData.get(position);
            mHolder.tvName.setText(groupObject.getGruop_name());
            mHolder.tvNo.setText(groupObject.getGroup_member_count() +"人");
        }

        mHolder.btnMain.setContentDescription("main");

        if (mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            mHolder.tvNo.setTextColor(mContext.getResources().getColor(R.color.common_sc_main_color));
            mHolder.btnMain.setBackgroundResource(R.drawable.common_btn_yellow_selector);
        } else if (mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            mHolder.tvNo.setTextColor(mContext.getResources().getColor(R.color.canpass_main));
            mHolder.btnMain.setBackgroundResource(R.drawable.common_btn_yellow_selector_canpass);
        }
        else if (mContext.getPackageName().equals(SCConstants.PACKAGE_TORETAN_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_TORETAN_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_TORETAN_STAGING)) {
            mHolder.tvNo.setTextColor(mContext.getResources().getColor(R.color.toretan_main));
            mHolder.btnMain.setBackgroundResource(R.drawable.common_btn_blue_selector_toretan);
        }

        // set listener
        initListener(position);
        setListenerForView();

        return convertView;
    }

    private void setListenerForView() {
        mHolder.btnMain.setOnClickListener(mOnClickListener);
    }

    private void initListener(final int position) {
        mOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("main")) {
                    afterClickMain(position);
                }

            }
        };
    }

    private void afterClickMain(int position) {
        if(mListData.get(position) instanceof SCGroupObject) {
            Intent intent = new Intent(mContext, SCAddGroup.class);
            intent.putExtra(SCGroupObject.class.toString(),
                    (SCGroupObject) mListData.get(position));
            ((Activity) mContext).startActivity(intent);
            ((Activity) mContext).finish();
            ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_bottom,
                    R.anim.anim_slide_out_bottom);
            return;
        }


    }
}

