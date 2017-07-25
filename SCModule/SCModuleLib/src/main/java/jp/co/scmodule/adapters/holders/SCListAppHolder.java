package jp.co.scmodule.adapters.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by VNCCO on 6/30/2015.
 */
public class SCListAppHolder {
    public ImageView imgApp = null;
    public TextView tvName = null;
    public View vPaddingRight = null;
    public View vPaddingLeft = null;

    public void resetView() {
        tvName.setText("");
        vPaddingRight.setVisibility(View.VISIBLE);
        vPaddingLeft.setVisibility(View.GONE);
    }
}
