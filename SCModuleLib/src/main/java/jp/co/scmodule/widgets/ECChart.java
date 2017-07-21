package jp.co.scmodule.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import jp.co.scmodule.objects.ECChartItemObject;
import jp.co.scmodule.utils.SCMultipleScreen;

/**
 * Created by VNCCO on 8/14/2015.
 */
public class ECChart extends LinearLayout {
    Context mContext;

    public ECChart(Context context) {
        super(context);
        this.mContext = context;
    }

    private void init() {
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.RIGHT);
    }

    public void setUpChart(ArrayList<ECChartItemObject> listChartItem) {
        new SCMultipleScreen(mContext);
        // get all values of chart items
        List<Integer> values = new ArrayList<Integer>();
        for (ECChartItemObject obj : listChartItem) {
            values.add(obj.getIn());
            values.add(obj.getOut());
        }

        int max = getMaxValue(values);

        this.setWeightSum(4);

        //add empty item if listChartItem.size < 4
        if (listChartItem.size() == 0) {
            Calendar now = Calendar.getInstance();
            int month = now.get(Calendar.MONTH) + 1;
            ECChartItemObject objectEmpty = new ECChartItemObject();
            objectEmpty.setMonth(month);
            listChartItem.add(objectEmpty);
        }
        if (listChartItem.size() < 4) {
            for (int i = (4 - listChartItem.size()); i > 0; i--) {
                LinearLayout.LayoutParams paramsEmptyItem = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                ECChartItem chartEmptyItem = new ECChartItem(mContext);
                chartEmptyItem.setLayoutParams(paramsEmptyItem);
                ECChartItemObject objectEmpty = new ECChartItemObject();
                int month = listChartItem.get(0).getMonth() - i;
                if(month == 0){
                    month = 12;
                }else if( month < 0){
                    month=12+month;
                }
                objectEmpty.setMonth(month);
                chartEmptyItem.setChart(getPercent(max, objectEmpty.getIn()), getPercent(max, objectEmpty.getOut()), objectEmpty.getMonth());
                SCMultipleScreen.resizeAllView(chartEmptyItem);
                this.addView(chartEmptyItem);
            }
        }
        for (int i = 0; i < listChartItem.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            ECChartItem chartItem = new ECChartItem(mContext);
            chartItem.setLayoutParams(params);
            ECChartItemObject object = listChartItem.get(i);
            chartItem.setChart(getPercent(max, object.getIn()), getPercent(max, object.getOut()), object.getMonth());
            SCMultipleScreen.resizeAllView(chartItem);
            this.addView(chartItem);
        }
    }

    public ECChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ECChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private int getMaxValue(List<Integer> values) {
        if(values.size() != 0) {
            return Collections.max(values);
        } else {
            return 0;
        }
    }

    private int getPercent(int max, int target) {
        return Math.round(target * 1.0f * 100 / max);
    }
}
