package jp.co.scmodule.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

import jp.co.scmodule.R;
import jp.co.scmodule.adapters.holders.SCListAppHolder;
import jp.co.scmodule.objects.SCAppObject;
import jp.co.scmodule.objects.SCGroupObject;
import jp.co.scmodule.objects.SCMemberObject;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.widgets.SCCircleImageView;

/**
 * Created by WebHawks IT on 6/6/2016.
 */
public class SCGroupAdapter extends BaseAdapter {

    private Activity mActivity = null;
    private ArrayList<SCMemberObject> mListGroup = null;

    private DisplayImageOptions mImageLoaderOpts = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_avatar)
            .showImageForEmptyUri(R.drawable.default_avatar)
            .showImageOnFail(R.drawable.default_avatar)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(true).cacheOnDisk(false).considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();

    public SCGroupAdapter(Activity activity, ArrayList<SCMemberObject> mListGroup) {
        this.mActivity = activity;
        this.mListGroup = mListGroup;
    }

    @Override
    public int getCount() {
        return mListGroup.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.grid_item_layout, null);
            holder = new ViewHolder();
            holder.image = (SCCircleImageView) convertView.findViewById(R.id.profile_img_avatar);

            new SCMultipleScreen(mActivity);
            SCMultipleScreen.resizeAllView((ViewGroup) convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SCMemberObject memberObject = new SCMemberObject();
        memberObject = (SCMemberObject) mListGroup.get(position);

        ImageLoader.getInstance().displayImage(memberObject.getIcon(), holder.image, mImageLoaderOpts);

        return convertView;
    }

    static class ViewHolder {
        SCCircleImageView image;
    }
}
