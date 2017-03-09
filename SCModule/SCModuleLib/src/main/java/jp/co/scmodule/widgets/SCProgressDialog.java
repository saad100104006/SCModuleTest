package jp.co.scmodule.widgets;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

/**
 * Custom loading progress dialog will provide closing dialog when click back button
 *
 * @author Phan Tri
 */
public class SCProgressDialog extends ProgressDialog {

    public static int sPdCount = 0;

    public SCProgressDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.setCancelable(false);
    }

    public SCProgressDialog(Context context, int style) {
        super(context, style);
        // TODO Auto-generated constructor stub
        this.setCancelable(false);
    }

    @Override
    public void onBackPressed() {
        sPdCount = 0;
        this.dismiss();
    }
}
