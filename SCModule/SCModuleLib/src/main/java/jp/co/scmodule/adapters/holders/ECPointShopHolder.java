package jp.co.scmodule.adapters.holders;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by VNCCO on 8/13/2015.
 */
public class ECPointShopHolder {
    public ImageView imageImage;
    public TextView shopName;
    public LinearLayout llMain;

    public void resetView() {
        imageImage.setImageResource(0);
        shopName.setText("");
    }
}
