package jp.co.scmodule.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import jp.co.scmodule.ECDetailProductActivity;
import jp.co.scmodule.ECDetailShopActivity;
import jp.co.scmodule.R;
import jp.co.scmodule.adapters.holders.ECPointShopHolder;
import jp.co.scmodule.objects.ECProductObject;
import jp.co.scmodule.objects.ECShopObject;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;

/**
 * Created by VNCCO on 8/13/2015.
 */
public class ECPointShopAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<ECShopObject> mListShop;
    private ECPointShopHolder mHolder;
    private int maxItem = 3;

    public ECPointShopAdapter(Activity activity, ArrayList<ECShopObject> ListShop) {
        this.mActivity = activity;
        this.mListShop = ListShop;
    }

    public void expand() {
        maxItem = mListShop.size();
    }

    @Override
    public int getCount() {
        if(mListShop.size() < 3) {
            return mListShop.size();
        } else {
            return maxItem;
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_ecpoint_shop, viewGroup, false);
            mHolder = new ECPointShopHolder();
            mHolder.imageImage = (ImageView) convertView.findViewById(R.id.ec_point_img_image);
            mHolder.shopName = (TextView) convertView.findViewById(R.id.ec_point_tv_shop_name);
            mHolder.llMain = (LinearLayout) convertView.findViewById(R.id.ec_point_ll_main);

            new SCMultipleScreen(mActivity);
            SCMultipleScreen.resizeAllView((ViewGroup) convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ECPointShopHolder) convertView.getTag();
        }
        //reset view
        mHolder.resetView();

        ECShopObject shopObject = mListShop.get(position);
        //set infor
        ImageLoader.getInstance().displayImage(shopObject.getImage(), mHolder.imageImage, SCGlobalUtils.sOptForImgLoader);
        mHolder.shopName.setText(shopObject.getName());

        mHolder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ECDetailShopActivity.class);
                intent.putExtra(String.class.toString(), mListShop.get(position).getId());
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);

            }
        });


        return convertView;
    }
}
