package jp.co.scmodule.adapters.holders;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by VNCCO on 8/13/2015.
 */
public class ECPointProductHolder {
    public ImageView imgImage;
    public TextView tvProductName;
    public TextView tvShopName;
    public TextView tvPoint;
    public LinearLayout llMain;

    public void resetView() {
        imgImage.setImageResource(0);
        tvProductName.setText("");
        tvShopName.setText("");
        tvPoint.setText("");
    }
}
