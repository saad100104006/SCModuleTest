package jp.co.scmodule.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.co.scmodule.ECDetailHistoryProductActivity;
import jp.co.scmodule.ECDetailProductActivity;
import jp.co.scmodule.R;
import jp.co.scmodule.adapters.holders.ECHistoryHolder;
import jp.co.scmodule.adapters.holders.ECPointProductHolder;
import jp.co.scmodule.adapters.holders.SCProductHolder;
import jp.co.scmodule.objects.ECProductObject;
import jp.co.scmodule.objects.SCHistoryObject;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.widgets.SCSingleLineTextView;

/**
 * Created by Munir @WebHawks IT on 8/19/2016.
 */
public class ECPointHistoryAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<SCHistoryObject> mListProduct;
    private ECHistoryHolder mHolder;
    private int maxItem = 0;
    private int mType = -1;

    public ECPointHistoryAdapter(Activity activity, ArrayList<SCHistoryObject> listProduct) {
        this.mActivity = activity;
        this.mListProduct = listProduct;
    }

    public void expand() {
        maxItem = mListProduct.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mListProduct.size() < maxItem) {
            return mListProduct.size();
        } else {
            return maxItem;
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_ecpoint_history, viewGroup, false);
            mHolder = new ECHistoryHolder();

            mHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            mHolder.tvValue = (TextView) convertView.findViewById(R.id.tv_value);


            new SCMultipleScreen(mActivity);
            SCMultipleScreen.resizeAllView((ViewGroup) convertView);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ECHistoryHolder) convertView.getTag();
        }
        //reset view before set info
        mHolder.resetView();

        SCHistoryObject historyObject = mListProduct.get(position);

        int point = Integer.parseInt(historyObject.getDiff());
        if(point >= 0){
            if(historyObject.getPoint_item_name() == null || historyObject.getPoint_item_name().equals("")){
                mHolder.tvName.setText(historyObject.getService());
            }else {
                mHolder.tvName.setText(historyObject.getPoint_item_name());
            }
        }else if(point < 0){
            if(historyObject.getExchange_item_name() == null || historyObject.getExchange_item_name().equals("")){
                mHolder.tvName.setText("その他");
            }else {
                mHolder.tvName.setText(historyObject.getExchange_item_name());
            }
        }

        mHolder.tvValue.setText(historyObject.getDiff());
        mHolder.tvDate.setText(formateDateFromstring("yyyy-MM-dd", "yyyy/MM/dd", historyObject.getCreated()));

        return convertView;
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
           Log.e("Date Conversion", "ParseException - dateFormat");
        }

        return outputDate;

    }
}
