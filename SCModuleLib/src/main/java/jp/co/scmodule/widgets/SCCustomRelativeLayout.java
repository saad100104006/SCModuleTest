package jp.co.scmodule.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import jp.co.scmodule.R;
import jp.co.scmodule.SCGroupAdminPage;
import jp.co.scmodule.SCMainActivity;

/**
 * Created by VCC1 on 9/25/2015.
 */
public class SCCustomRelativeLayout extends RelativeLayout {
    //private static final int SWIPE_MIN_DISTANCE = 338;
    private static final int SWIPE_MIN_DISTANCE = 150;
    private float startX = 0;
    private float startY = 0;
    private Context mContext = null;

    public SCCustomRelativeLayout(Context context) {
        super(context);

        mContext = context;
    }

    public SCCustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
    }

    public SCCustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getRawX();
                startY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
//                int distanceX = Math.round(ev.getRawX() - startX);
//                int distanceY = Math.round(ev.getRawY() - startY);
                int distanceX = Math.round(startX - ev.getRawX());
                int distanceY = Math.round(startY - ev.getRawY());
                if (distanceX > SWIPE_MIN_DISTANCE && Math.abs(distanceX) > Math.abs(distanceY)) {
                    backActivity();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getRawX();
                startY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = Math.round(ev.getRawX() - startX);
                int distanceY = Math.round(ev.getRawY() - startY);
                if (distanceX > SWIPE_MIN_DISTANCE && Math.abs(distanceX) > Math.abs(distanceY)) {
                    backActivity();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return false;
    }

    private void backActivity() {
        if (mContext instanceof SCMainActivity) {
            ((SCMainActivity) mContext).afterClickBack();
        }
        if (mContext instanceof SCGroupAdminPage) {
            ((SCGroupAdminPage) mContext).afterClickBack();
        }
    }
}
