package jp.co.scmodule.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.scmodule.ECDetailProductActivity;
import jp.co.scmodule.ECMainActivity;
import jp.co.scmodule.R;
import jp.co.scmodule.SCProfileInfoActivity;
import jp.co.scmodule.adapters.holders.SCProductHolder;
import jp.co.scmodule.apis.SCRequestAsyncTask;
import jp.co.scmodule.interfaces.SCDialogCallback;
import jp.co.scmodule.objects.ECProductObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.utils.SCAPIUtils;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCMultipleScreen;
import jp.co.scmodule.widgets.SCSingleLineTextView;

/**
 * Created by VNCCO on 6/30/2015.
 */
public class ECProductAdapter extends BaseAdapter {
    private static final String TAG_LOG = "ECProductAdapter";

    private static final String TAG_RECENT = "recent";
    private static final String TAG_FAVOR = "favor";
    private static final String TAG_POINT = "point";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_SHOP = "shop";
    private static final String TAG_KEYWORD = "keyword";

    private SCProductHolder mHolder = null;
    private Context mContext = null;
    private ArrayList<Object> mListData = null;
    private View.OnClickListener mOnClickListener = null;
    private String mTag = null;
    private int mActivePos = -1;


    public ECProductAdapter(Context context, ArrayList<Object> listData) {
        this.mContext = context;
        this.mListData = listData;


    }

    public int getActivePosition() {
        return mActivePos;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.item_ec_product, null);
            mHolder = new SCProductHolder();

            mHolder.imgProduct = (ImageView) convertView.findViewById(R.id.item_ec_img);
            mHolder.tvName = (TextView) convertView.findViewById(R.id.item_ec_tv_name);
            mHolder.tvOwner = (TextView) convertView.findViewById(R.id.item_ec_tv_owner);
            mHolder.tvPoint = (SCSingleLineTextView) convertView.findViewById(R.id.item_ec_tv_point);
            mHolder.btnLike = (Button) convertView.findViewById(R.id.item_ec_btn_like);
            mHolder.btnExchangePoint = (Button) convertView.findViewById(R.id.item_ec_btn_exchange_point);
            mHolder.imgRank = (ImageView) convertView.findViewById(R.id.item_ec_img_rank);
            mHolder.vBlankFooter = convertView.findViewById(R.id.item_v_blank_footer);
            mHolder.vBlankHeader = convertView.findViewById(R.id.item_v_blank_header);
            mHolder.tv_special_discount = (TextView) convertView.findViewById(R.id.tv_discount_badge);

            new SCMultipleScreen(mContext);
            SCMultipleScreen.resizeAllView((ViewGroup) convertView);

            convertView.setTag(mHolder);
        } else {
            mHolder = (SCProductHolder) convertView.getTag();
        }

        mHolder.resetView();

        ECProductObject productObj = (ECProductObject) mListData.get(position);

        if (SCGlobalUtils.discount_rate == 0) {
            mHolder.tv_special_discount.setVisibility(View.GONE);
            mHolder.tvPoint.setText(productObj.getPoint()
                    + " " + mContext.getResources().getString(R.string.common_point));
        } else {
            mHolder.tv_special_discount.setVisibility(View.VISIBLE);
            mHolder.tv_special_discount.setText(SCGlobalUtils.discount_rate + "%\nOFF");

            int discount_point = (Integer.parseInt(productObj.getPoint()) * SCGlobalUtils.discount_rate) / 100;
            int updated_point = Integer.parseInt(productObj.getPoint()) - discount_point;
            mHolder.tvPoint.setText(updated_point
                    + " " + mContext.getResources().getString(R.string.common_point));
        }
        mHolder.btnExchangePoint.setContentDescription("exchangePoint");
        mHolder.imgProduct.setContentDescription("detail");
        mHolder.btnLike.setContentDescription("like");

        ImageLoader.getInstance().displayImage(productObj.getImage(), mHolder.imgProduct, SCGlobalUtils.sOptForImgLoader);

        mHolder.tvName.setText(productObj.getName());
        mHolder.tvOwner.setText(productObj.getShop());
//        mHolder.tvPoint.setText(productObj.getPoint()
//                + " " + mContext.getResources().getString(R.string.common_point));

        if (productObj.getFavorite().equals("true")) {
            mHolder.btnLike.setSelected(true);
            // mHolder.btnLike.setTextColor(mContext.getResources().getColor(R.color.common_ec_red));
            // mHolder.btnLike.setText(mContext.getResources().getString(R.string.common_unlike_product));
            //mHolder.btnLike.setVisibility(View.GONE);
        } else {
            mHolder.btnLike.setSelected(false);
            //mHolder.btnLike.setTextColor(mContext.getResources().getColor(android.R.color.white));
            //mHolder.btnLike.setText(mContext.getResources().getString(R.string.common_like_product));
            mHolder.btnLike.setVisibility(View.VISIBLE);
        }

        if (mTag != null && mTag.equals(TAG_FAVOR)) {
            if (position == 0) {
                mHolder.imgRank.setVisibility(View.VISIBLE);
                mHolder.imgRank.setImageResource(R.drawable.img_1st);
            }

            if (position == 1) {
                mHolder.imgRank.setVisibility(View.VISIBLE);
                mHolder.imgRank.setImageResource(R.drawable.img_2nd);
            }

            if (position == 2) {
                mHolder.imgRank.setVisibility(View.VISIBLE);
                mHolder.imgRank.setImageResource(R.drawable.img_3rd);
            }
        }

        if (position == 0 || position == 1) {
            mHolder.vBlankHeader.setVisibility(View.VISIBLE);
        }

        if (position == getCount() - 1 || position == getCount() - 2) {
            mHolder.vBlankFooter.setVisibility(View.VISIBLE);
        }

        initListener(position);
        setListenerForView();

        return convertView;
    }

    private void setListenerForView() {
        mHolder.btnExchangePoint.setOnClickListener(mOnClickListener);
        mHolder.imgProduct.setOnClickListener(mOnClickListener);
        mHolder.btnLike.setOnClickListener(mOnClickListener);
    }

    private void initListener(final int position) {
        mOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getContentDescription() == null) {
                    return;
                } else if (v.getContentDescription().equals("detail")) {
                    afterClickDetail(position);
                } else if (v.getContentDescription().equals("exchangePoint")) {
                    afterClickExchangePoint(position);
                } else if (v.getContentDescription().equals("like")) {
                    afterClickLike(position);
                }

            }
        };
    }

    public void afterClickExchangePoint(int position) {

        ECProductObject productObj = (ECProductObject) mListData.get(position);
        mActivePos = position;
        if (SCUserObject.getInstance().getIsGuest().equals("true")) {
            if (mContext instanceof ECMainActivity) {
                ((ECMainActivity) mContext).showLoginDialog(SCConstants.CODE_LOGIN_FOR_PAY_ECHANGE_ITEM);
            }
        } else {
            int userPoint = Integer.parseInt(SCUserObject.getInstance().getCampusPoint());
            int productPoint = Integer.parseInt(productObj.getPoint());
            if (userPoint < productPoint) {
                showDialogNotEnoughPoint(productObj);
                return;
            }

            if (!productObj.getCategoryId().equals("2")) {
                if ((SCUserObject.getInstance().getNickname().equals("")
                        || SCUserObject.getInstance().getPostCode().equals("")
                        || SCUserObject.getInstance().getAddress().equals("")
                        || SCUserObject.getInstance().getPhoneNumber().equals(""))) {
                    showDialogNotEnoughInfoField(productObj);
                } else if (!SCUserObject.getInstance().getNickname().equals("")
                        && !SCUserObject.getInstance().getPostCode().equals("")
                        && !SCUserObject.getInstance().getAddress().equals("")
                        && !SCUserObject.getInstance().getPhoneNumber().equals("")) {
                    showDialogEnoughInfoField(productObj);
                }
            } else {
                showDialogConfirmExchangeStage1(productObj);
            }
        }
    }

    private void showDialogConfirmExchangeStage1(final ECProductObject productObj) {
        String title = mContext.getResources().getString(R.string.dialog_exchange_stage_1_title);
        String body = mContext.getResources().getString(R.string.dialog_body_exchange_point_stage_1);
        body = String.format(
                body,
                SCUserObject.getInstance().getNickname(),
                SCUserObject.getInstance().getEmail(),
                productObj.getName(),
                productObj.getPoint(),
                SCUserObject.getInstance().getCampusPoint(),
                String.valueOf(Integer.parseInt(SCUserObject.getInstance().getCampusPoint()) - Integer.parseInt(productObj.getPoint())));
        String action1 = mContext.getResources().getString(R.string.common_ok_label);
        String action2 = mContext.getResources().getString(R.string.common_cancel_label);
        String action3 = mContext.getResources().getString(R.string.common_edit_label);
        SCGlobalUtils.showThreeActionDialog(mContext, title, body, action1, action2, action3, new SCDialogCallback() {
            @Override
            public void onAction1() {
                showDialogConfirmExchangeStage2(productObj);
            }

            @Override
            public void onAction2() {

            }

            @Override
            public void onAction3() {
                Intent intent = new Intent(mContext, SCProfileInfoActivity.class);
                intent.putExtra(SCUserObject.class.toString(), SCUserObject.getInstance());
                ((Activity) mContext).startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
            }

            @Override
            public void onAction4() {

            }
        });
    }

    private void showDialogConfirmExchangeStage2(ECProductObject productObj) {
        requestPayExchangeItem();
        ((ECMainActivity) mContext).after_success_show_thanks_page();

//        String title = mContext.getResources().getString(R.string.dialog_exchange_stage_2_title);
//        String body = mContext.getResources().getString(R.string.dialog_body_exchange_point_stage_2);
//        SCGlobalUtils.showInfoDialog(mContext, title, body, null, new SCDialogCallback() {
//            @Override
//            public void onAction1() {
//                requestPayExchangeItem();
//            }
//
//            @Override
//            public void onAction2() {
//
//            }
//
//            @Override
//            public void onAction3() {
//
//            }
//
//            @Override
//            public void onAction4() {
//
//            }
//        });
    }

    private void showDialogNotEnoughPoint(ECProductObject productObj) {
        String title = mContext.getResources().getString(R.string.dialog_error_title);
        String body = mContext.getResources().getString(R.string.dialog_pay_exchange_not_enough_point);
        SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
    }

    private void showDialogEnoughInfoField(final ECProductObject productObj) {
        String title = mContext.getResources().getString(R.string.dialog_confirm_title);
        int color = mContext.getResources().getColor(R.color.red);
        String body = mContext.getResources().getString(R.string.dialog_confirm_info_before_exchange, color);
        String action1 = mContext.getResources().getString(R.string.common_ok_label);
        String action2 = mContext.getResources().getString(R.string.common_cancel_label);
        String action3 = mContext.getResources().getString(R.string.common_edit_label);
        SCGlobalUtils.showThreeActionDialog(mContext, title, body, action1, action2, action3, new SCDialogCallback() {
            @Override
            public void onAction1() {
                showDialogConfirmExchangeStage1(productObj);
            }

            @Override
            public void onAction2() {

            }

            @Override
            public void onAction3() {
                Intent intent = new Intent(mContext, SCProfileInfoActivity.class);
                intent.putExtra(SCUserObject.class.toString(), SCUserObject.getInstance());
                ((Activity) mContext).startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
            }

            @Override
            public void onAction4() {

            }
        });
    }

    private void showDialogNotEnoughInfoField(final ECProductObject productObj) {
        String title = mContext.getResources().getString(R.string.dialog_confirm_title);
        int color = mContext.getResources().getColor(R.color.red);
        String body = mContext.getResources().getString(R.string.dialog_confirm_info_before_exchange, color);
        String action1 = mContext.getResources().getString(R.string.common_edit_label);
        String action2 = mContext.getResources().getString(R.string.common_cancel_label);
        SCGlobalUtils.showConfirmDialog(mContext, title, body, action1, action2, new SCDialogCallback() {
            @Override
            public void onAction1() {
                Intent intent = new Intent(mContext, SCProfileInfoActivity.class);
                intent.putExtra(SCUserObject.class.toString(), SCUserObject.getInstance());
                ((Activity) mContext).startActivity(intent);
                ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_left);
            }

            @Override
            public void onAction2() {

            }

            @Override
            public void onAction3() {

            }

            @Override
            public void onAction4() {

            }
        });
    }

    private void afterClickDetail(int position) {
        Intent intent = new Intent(mContext, ECDetailProductActivity.class);
        intent.putExtra(ECProductObject.class.toString(), (ECProductObject) mListData.get(position));
        intent.putExtra(Integer.class.toString(), position);
        ((Activity) mContext).startActivityForResult(intent, SCConstants.CODE_SHOW_PRODUCT_DETAIL);
        ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left);
    }

    private void afterClickLike(int position) {
        mActivePos = position;
        if (SCUserObject.getInstance().getIsGuest().equals("true")) {
            if (mContext instanceof ECMainActivity) {
                ((ECMainActivity) mContext).showLoginDialog(SCConstants.CODE_LOGIN_FOR_ADD_FAVORITE_ITEM);
            }
        } else {
            requestAddFavorite();
        }
    }

    public void notifyDataSetChanged(String tag) {
        mTag = tag;
        super.notifyDataSetChanged();
    }

    public void requestAddFavorite() {
        final ECProductObject productObj = (ECProductObject) mListData.get(mActivePos);

        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();
        //String appId = "188TZ";

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);
        parameter.put(SCConstants.PARAM_APP_EXCHANGE_ITEM_ID, productObj.getId());

        SCRequestAsyncTask SCRequestAsyncTask = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_ADD_FAVORITE_ITEM, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_ADD_FAVORITE_ITEM, result);

                if (returnHash.containsKey(SCConstants.TAG_FAVORITE)) {
                    String favorite = (String) returnHash.get(SCConstants.TAG_FAVORITE);

                    productObj.setFavorite(favorite);

                    notifyDataSetChanged();
                }
            }

            @Override
            public void progress() {
                SCGlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void onInterrupted(Exception e) {
                SCGlobalUtils.dismissLoadingProgress();
            }

            @Override
            public void onException(Exception e) {
                SCGlobalUtils.dismissLoadingProgress();
            }
        });

        SCRequestAsyncTask.execute();
    }

    public void requestPayExchangeItem() {
        ECProductObject productObj = (ECProductObject) mListData.get(mActivePos);

        String secretKey = SCConstants.SECRET_KEY;
        String date = String.valueOf(System.currentTimeMillis());
        String appId = SCUserObject.getInstance().getAppId();
        String applicationId = "";
        //if (mContext.getPackageName().equals(SCConstants.PACKAGE_TADACOPY)) {
        applicationId = SCConstants.APP_ID_TADACOPY;
//        } else if (mContext.getPackageName().equals(SCConstants.PACKAGE_CANPASS)) {
//            applicationId = SCConstants.APP_ID_CANPASS;
//        }
        //String appId = "188TZ";

        String src = secretKey + appId + date;
        String key = SCGlobalUtils.md5Hash(src);
        HashMap<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(SCConstants.PARAM_KEY, key);
        parameter.put(SCConstants.PARAM_DATE, date);
        parameter.put(SCConstants.PARAM_APP_ID, appId);
        parameter.put(SCConstants.PARAM_APP_EXCHANGE_ITEM_ID, productObj.getId());
        parameter.put(SCConstants.PARAM_APPLICATION_ID, applicationId);

        SCRequestAsyncTask SCRequestAsyncTask = new SCRequestAsyncTask(mContext, SCConstants.REQUEST_PAY_EXCHANGE_ITEM, parameter, new SCRequestAsyncTask.AsyncCallback() {
            @Override
            public void done(String result) {
                SCGlobalUtils.dismissLoadingProgress();

                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHash = SCAPIUtils.parseJSON(SCConstants.REQUEST_PAY_EXCHANGE_ITEM, result);

                if (returnHash.containsKey(SCConstants.TAG_AFTER_POINT)) {
                    String afterPoint = (String) returnHash.get(SCConstants.TAG_AFTER_POINT);

                    SCUserObject.getInstance().setCampusPoint(afterPoint);

                    if (mContext instanceof ECMainActivity) {
                        ((ECMainActivity) mContext).updateUserPoint();
                    }
                }

                if (returnHash.containsKey(SCConstants.TAG_ERROR_CODE)) {
                    int errCode = Integer.parseInt((String) returnHash.get(SCConstants.TAG_ERROR_CODE));
                    String title = mContext.getResources().getString(R.string.dialog_error_title);
                    String body = null;
                    if (errCode == ECProductObject.ERROR_CONNECTION) {
                        body = mContext.getResources().getString(R.string.dialog_pay_exchange_connection_fail);
                    } else if (errCode == ECProductObject.ERROR_LOGIN_FAIL) {
                        body = mContext.getResources().getString(R.string.dialog_pay_exchange_login_fail);
                    } else if (errCode == ECProductObject.ERROR_NOT_ENOUGH_POINT) {
                        body = mContext.getResources().getString(R.string.dialog_pay_exchange_not_enough_point);
                    }

                    if (body != null) {
                        SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
                    }
                }
            }

            @Override
            public void progress() {
                SCGlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void onInterrupted(Exception e) {
                SCGlobalUtils.dismissLoadingProgress();
            }

            @Override
            public void onException(Exception e) {
                SCGlobalUtils.dismissLoadingProgress();
            }
        });

        SCRequestAsyncTask.execute();
    }
}
