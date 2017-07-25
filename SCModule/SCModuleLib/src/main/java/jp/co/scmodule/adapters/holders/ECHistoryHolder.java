package jp.co.scmodule.adapters.holders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import jp.co.scmodule.widgets.SCSingleLineTextView;

/**
 * Created by WebHawks IT on 8/22/2016.
 */
public class ECHistoryHolder {
    public TextView tvDate = null;
    public TextView tvName = null;
    public TextView tvValue = null;


    public void resetView() {
        tvName.setText("");
        tvDate.setText("");
        tvValue.setText("");

    }
}
