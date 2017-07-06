package jp.co.scmodule.adapters;

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
import android.widget.RelativeLayout;
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


public class SCChoosenAdapter extends BaseAdapter {
    private Context mContext = null;
    private OnClickListener mOnClickListener = null;
    private SCChoosenHolder mHolder = null;
    private ArrayList<Object> mListData = null;

    public SCChoosenAdapter(Context context, ArrayList<Object> listData) {
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
            convertView = inflator.inflate(R.layout.item_choosen, null);
            mHolder = new SCChoosenHolder();

            mHolder.tvName = (TextView) convertView.findViewById(R.id.item_choosen_tv_name);
            mHolder.btnMain = (RelativeLayout) convertView.findViewById(R.id.item_choosen_btn_main);
            mHolder.imgNarrow = (ImageView) convertView.findViewById(R.id.item_choosen_img_narrow);

            new SCMultipleScreen(mContext);
            SCMultipleScreen.resizeAllView((ViewGroup) convertView);

            convertView.setTag(mHolder);
        } else {
            mHolder = (SCChoosenHolder) convertView.getTag();
        }

        if (mListData.get(position) instanceof SCUniversityObject) {
            SCUniversityObject univObj = (SCUniversityObject) mListData.get(position);
            mHolder.tvName.setText(univObj.getName());
        }

        if(mListData.get(position) instanceof SCDepartmentObject) {
            SCDepartmentObject departmentObj = (SCDepartmentObject) mListData.get(position);
            mHolder.tvName.setText(departmentObj.getName());
        }

        if(mListData.get(position) instanceof SCCampusObject) {
            SCCampusObject campusObj = (SCCampusObject) mListData.get(position);
            mHolder.tvName.setText(campusObj.getName());
        }

        if(mListData.get(position) instanceof SCMajorObject) {
            SCMajorObject majorObj = (SCMajorObject) mListData.get(position);
            mHolder.tvName.setText(majorObj.getName());
        }

        if(mListData.get(position) instanceof SCPrefectureObject) {
            SCPrefectureObject prefectureObj = (SCPrefectureObject) mListData.get(position);
            mHolder.tvName.setText(prefectureObj.getName());
        }

        if(mListData.get(position) instanceof SCEnrollmentObject) {
            SCEnrollmentObject enrollmentObj = (SCEnrollmentObject) mListData.get(position);
            mHolder.tvName.setText(enrollmentObj.getName()
                    + mContext.getResources().getString(R.string.common_year));
        }

        if(mListData.get(position) instanceof SCGenderObject) {
            SCGenderObject genderObj = (SCGenderObject) mListData.get(position);
            mHolder.tvName.setText(genderObj.getName());
        }

        if(mListData.get(position) instanceof SCUniversityObject) {
            mHolder.imgNarrow.setVisibility(View.VISIBLE);
        } else {
            mHolder.imgNarrow.setVisibility(View.GONE);
        }

        if (mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
            mHolder.btnMain.setBackgroundResource(R.drawable.common_btn_yellow_selector);
        } else if (mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
            mHolder.btnMain.setBackgroundResource(R.drawable.common_btn_yellow_selector_canpass);
        }

        mHolder.btnMain.setContentDescription("main");

        // set listener
        initListener(position);
        setListenerForView();

        return convertView;
    }

    private void setListenerForView() {
        mHolder.btnMain.setOnClickListener(mOnClickListener);
    }

    private void initListener(final int position) {
        mOnClickListener = new OnClickListener() {

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
        Intent i = new Intent();
        if(mListData.get(position) instanceof SCUniversityObject) {
            Intent intent = new Intent(mContext, SCChoosenActivity.class);
            intent.putExtra(SCUniversityObject.class.toString(),
                    (SCUniversityObject) mListData.get(position));
            intent.putExtra(Integer.class.toString(), SCConstants.TYPE_CAMPUS);
            ((Activity)mContext).startActivityForResult(intent, SCConstants.TYPE_CAMPUS);
            ((Activity)mContext).overridePendingTransition(R.anim.anim_slide_in_right,
                    R.anim.anim_slide_out_left);
            return;
        }

        if(mListData.get(position) instanceof SCCampusObject) {
            SCUniversityObject universityObject = ((SCChoosenActivity) mContext).getmUniversityObj();
            universityObject.setmCampusObj((SCCampusObject) mListData.get(position));
            i.putExtra(SCUniversityObject.class.toString(), universityObject);
        }

        if(mListData.get(position) instanceof SCDepartmentObject) {
            i.putExtra(SCDepartmentObject.class.toString(),
                    (SCDepartmentObject) mListData.get(position));
        }

        if(mListData.get(position) instanceof SCMajorObject) {
            i.putExtra(SCMajorObject.class.toString(),
                    (SCMajorObject) mListData.get(position));
        }

        if(mListData.get(position) instanceof SCPrefectureObject) {
            i.putExtra(SCPrefectureObject.class.toString(),
                    (SCPrefectureObject) mListData.get(position));
        }

        if(mListData.get(position) instanceof SCEnrollmentObject) {
            i.putExtra(SCEnrollmentObject.class.toString(),
                    (SCEnrollmentObject) mListData.get(position));
        }

        if(mListData.get(position) instanceof SCGenderObject) {
            i.putExtra(SCGenderObject.class.toString(),
                    (SCGenderObject) mListData.get(position));
        }

        ((Activity) mContext).setResult(Activity.RESULT_OK, i);
        ((Activity) mContext).finish();
        ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_bottom,
                R.anim.anim_slide_out_bottom);
    }
}
