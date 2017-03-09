package jp.co.scmodule.adapters.holders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import jp.co.scmodule.widgets.SCSingleLineTextView;

/**
 * Created by VNCCO on 6/30/2015.
 */
public class SCProductHolder {
    public ImageView imgProduct = null;
    public TextView tvName = null;
    public TextView tvOwner = null;
    public SCSingleLineTextView tvPoint = null;
    public Button btnLike = null;
    public Button btnExchangePoint = null;
    public ImageView imgRank = null;
    public View vBlankHeader = null;
    public View vBlankFooter = null;
    public TextView tv_special_discount = null;

    public void resetView() {
        tvName.setText("");
        tvOwner.setText("");
        tvPoint.setText("");
        tv_special_discount.setText("");
        imgRank.setVisibility(View.GONE);
        vBlankHeader.setVisibility(View.GONE);
        vBlankFooter.setVisibility(View.GONE);
    }
}
