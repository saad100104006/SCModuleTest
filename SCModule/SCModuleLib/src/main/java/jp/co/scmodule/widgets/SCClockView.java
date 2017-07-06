package jp.co.scmodule.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import jp.co.scmodule.R;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCMultipleScreen;

/**
 * Created by Administrator on 6/8/15.
 */
public class SCClockView extends ImageView {

    private float mAngle = 0;
    Context mContext = null;

    public SCClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    public float getmAngle() {
        return mAngle;
    }

    public void setmAngle(float mAngle) {
        this.mAngle = mAngle;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int r = (w > h ? h : w) / 2;

        Paint p = new Paint();
        // smooths
        p.setAntiAlias(true);
        p.setColor(Color.TRANSPARENT);
        p.setStyle(Paint.Style.FILL);

        RectF rectF = new RectF(w / 2 - r, h / 2 - r, w / 2 + r, h / 2 + r);
        canvas.drawOval(rectF, p);
        p.setColor(getContext().getResources().getColor(R.color.clock_remain));
        canvas.drawArc(rectF, mAngle - 90, 360 - mAngle, true, p);

        super.onDraw(canvas);
        Drawable clockwise = getContext().getResources().getDrawable(R.drawable.clockwise);

        if (mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY_STAGING)) {
             clockwise = getContext().getResources().getDrawable(R.drawable.clockwise);
        } else if (mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_RELEASE) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_DEBUG) || mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS_STAGING)) {
             clockwise = getContext().getResources().getDrawable(R.drawable.clockwise_canpass);
        }

        new SCMultipleScreen(getContext());
        Bitmap bitmap = ((BitmapDrawable) clockwise).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (r - SCMultipleScreen.getValueAfterResize(10)) / (119 / 16), r - SCMultipleScreen.getValueAfterResize(10), false);
        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(mAngle, bw / 2, bh - bw / 2);
        matrix.postTranslate((w - bw) / 2, (h + bw) / 2 - bh);
        canvas.drawBitmap(bitmap, matrix, null);
    }
}
