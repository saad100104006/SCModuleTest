package jp.co.scmodule.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.scmodule.ECDetailHistoryProductActivity;
import jp.co.scmodule.ECDetailProductActivity;
import jp.co.scmodule.R;
import jp.co.scmodule.adapters.holders.ECPointProductHolder;
import jp.co.scmodule.adapters.holders.SCProductHolder;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.objects.ECProductObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;

/**
 * Created by VNCCO on 8/13/2015.
 */
public class ECPointProductAdapter extends BaseAdapter {
    public static final int TYPE_EXCHANGE_HISTORY = 0;
    public static final int TYPE_FAVORITE = 1;
    private Activity mActivity;
    private ArrayList<ECProductObject> mListProduct;
    private ECPointProductHolder mHolder;
    private int maxItem = 3;
    private int mType = -1;

    public ECPointProductAdapter(Activity activity, ArrayList<ECProductObject> listProduct, int type) {
        this.mActivity = activity;
        this.mListProduct = listProduct;
        this.mType = type;
    }

    public void expand() {
        maxItem = mListProduct.size();
    }

    @Override
    public void notifyDataSetChanged() {
        if(mType == TYPE_FAVORITE) {
            for (Object obj : mListProduct) {
                if (((ECProductObject) obj).getFavorite().equals("false")) {
                    mListProduct.remove(obj);
                }
            }
        }
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mListProduct.size() < 3) {
            return mListProduct.size();
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
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_ecpoint_product, viewGroup, false);
            mHolder = new ECPointProductHolder();
            mHolder.imgImage = (ImageView) convertView.findViewById(R.id.item_ec_img);
            mHolder.tvProductName = (TextView) convertView.findViewById(R.id.item_ecpoint_product_name);
            mHolder.tvShopName = (TextView) convertView.findViewById(R.id.item_ecpoint_product_shop);
            mHolder.tvPoint = (TextView) convertView.findViewById(R.id.item_ecpoint_point);
            mHolder.llMain = (LinearLayout) convertView.findViewById(R.id.item_ecpoint_product_ll_main);

            new SCMultipleScreen(mActivity);
            SCMultipleScreen.resizeAllView((ViewGroup) convertView);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ECPointProductHolder) convertView.getTag();
        }
        //reset view before set info
        mHolder.resetView();

        ECProductObject productObject = mListProduct.get(position);
        //set infor:
        ImageLoader.getInstance().displayImage(productObject.getImage(), mHolder.imgImage, SCGlobalUtils.sOptForImgLoader);
        mHolder.tvProductName.setText(productObject.getName());
        mHolder.tvShopName.setText(productObject.getShop());
        mHolder.tvPoint.setText(productObject.getPoint() + " " + mActivity.getResources().getString(R.string.common_point));

        mHolder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (mType == TYPE_EXCHANGE_HISTORY) {
                    intent = new Intent(mActivity, ECDetailHistoryProductActivity.class);
                } else if (mType == TYPE_FAVORITE) {
                    intent = new Intent(mActivity, ECDetailProductActivity.class);
                } else {
                    return;
                }
                intent.putExtra(ECProductObject.class.toString(), mListProduct.get(position));
                intent.putExtra(Integer.class.toString(), position);
                mActivity.startActivityForResult(intent, SCConstants.CODE_SHOW_PRODUCT_DETAIL);
                mActivity.overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
            }
        });

        return convertView;
    }


}
