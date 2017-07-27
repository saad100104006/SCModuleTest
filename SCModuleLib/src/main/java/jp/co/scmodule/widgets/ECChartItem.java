package jp.co.scmodule.widgets;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import jp.co.scmodule.R;

/**
 * Created by VNCCO on 8/14/2015.
 */
public class ECChartItem extends RelativeLayout {
    Context mContext;
    private View vInBelow;
    private View vInAbove;
    private View vOutBelow;
    private View vOutAbove;
    private TextView tvMonth;

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.item_ecchart, this);
        vInAbove = (View) findViewById(R.id.ec_point_in_above);
        vInBelow = (View) findViewById(R.id.ec_point_in_below);
        vOutAbove = (View) findViewById(R.id.ec_point_out_above);
        vOutBelow = (View) findViewById(R.id.ec_point_out_below);
        tvMonth = (TextView) findViewById(R.id.ec_point_tv_month);
    }

    public void setChart(int PercentIn, int PercentOut, int month) {
        LinearLayout.LayoutParams vInAboveParams = (LinearLayout.LayoutParams) vInAbove.getLayoutParams();
        LinearLayout.LayoutParams vInBelowParams = (LinearLayout.LayoutParams) vInBelow.getLayoutParams();
        LinearLayout.LayoutParams vOutAboveParams = (LinearLayout.LayoutParams) vOutAbove.getLayoutParams();
        LinearLayout.LayoutParams vOutBelowParams = (LinearLayout.LayoutParams) vOutBelow.getLayoutParams();
        vInAboveParams.weight = 100 - PercentIn;
        vInBelowParams.weight = PercentIn;
        vOutAboveParams.weight = 100 - PercentOut;
        vOutBelowParams.weight = PercentOut;
        vInAbove.setLayoutParams(vInAboveParams);
        vInBelow.setLayoutParams(vInBelowParams);
        vOutAbove.setLayoutParams(vOutAboveParams);
        vOutBelow.setLayoutParams(vOutBelowParams);
        tvMonth.setText(month + getResources().getString(R.string.ec_point_manager_common_month));
    }

    public ECChartItem(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public ECChartItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ECChartItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }
}