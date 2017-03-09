package jp.co.scmodule.objects;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import org.brickred.socialauth.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import jp.co.scmodule.ECDetailProductActivity;
import jp.co.scmodule.utils.SCConstants;


/**
 * Created by VNCCO on 7/13/2015.
 */
public class SCBannerItem {
    private String Name = "";
    private String Url = "";
    private Boolean AddAppId = false;
    private String Tag = "";
    private String BannerId = "";
    private String BannerImage = "";
    private int OpenType = 0;

    public SCBannerItem() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Boolean getAddAppId() {
        return AddAppId;
    }

    public void setAddAppId(Boolean addAppId) {
        AddAppId = addAppId;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String name) {
        Tag = name;
    }

    public String getBannerId() {
        return BannerId;
    }

    public void setBannerId(String bannerId) {
        BannerId = bannerId;
    }

    public String getBannerImage() {
        return BannerImage;
    }

    public void setBannerImage(String bannerImage) {
        BannerImage = bannerImage;
    }

    public int getOpenType() {
        return OpenType;
    }

    public void setOpenType(int openType) {
        OpenType = openType;
    }

    public void ParseJSONGetBanner(String tag, JSONObject jsonObject) {
        setTag(tag);
        try {
            if (jsonObject.has(SCConstants.RESULT_GET_BANNER_ID)) {
                setBannerId(jsonObject.getString(SCConstants.RESULT_GET_BANNER_ID));
            }
            if (jsonObject.has(SCConstants.RESULT_GET_BANNER_IMAGE)) {
                setBannerImage(jsonObject.getString(SCConstants.RESULT_GET_BANNER_IMAGE));
            }
            if (jsonObject.has(SCConstants.RESULT_GET_BANNER_TYPE)) {
                setOpenType(jsonObject.getInt(SCConstants.RESULT_GET_BANNER_TYPE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    public void ParseJSONClickBanner(JSONObject jsonObject) {
//        try {
//            if (jsonObject.has(ConstantsUtil.RESULT_CLICK_BANNER_NAME)) {
//                setName(jsonObject.getString(ConstantsUtil.RESULT_CLICK_BANNER_NAME));
//            }
//            if (jsonObject.has(ConstantsUtil.RESULT_CLICK_BANNER_URL)) {
//                setUrl(jsonObject.getString(ConstantsUtil.RESULT_CLICK_BANNER_URL));
//            }
//            if (jsonObject.has(ConstantsUtil.RESULT_CLICK_BANNER_OPEN_TYPE)) {
//                setOpenType(jsonObject.getInt(ConstantsUtil.RESULT_CLICK_BANNER_OPEN_TYPE));
//            }
//            if (jsonObject.has(ConstantsUtil.RESULT_CLICK_BANNER_ADD_APP_ID)) {
//                if (jsonObject.getString(ConstantsUtil.RESULT_CLICK_BANNER_ADD_APP_ID).equals("true")) {
//                    setAddAppId(true);
//                } else {
//                    setAddAppId(false);
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void ParseJSONRemainingBanner(JSONObject jsonObject) {
//        try {
//            if (jsonObject.has("id")) {
//                setBannerId(jsonObject.getString("id"));
//            }
//            if (jsonObject.has("image")) {
//                setBannerImage(jsonObject.getString("image"));
//            }
//            if (jsonObject.has("type")) {
//                setOpenType(jsonObject.getInt("type"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void runAPILookBanner(String app_id, String applicationID, Activity activity) {
//        long time = Utils.getTimeInMilisecond();
//        String key = Utils.getMD5Key(ConstantsUtil.SECRET_KEY, app_id, time);
//        HashMap<String, String> param = new HashMap<>();
//        param.put(ConstantsUtil.PAR_LOOK_BANNER_KEY, key);
//        param.put(ConstantsUtil.PAR_LOOK_BANNER_ADVERTISE_ID, this.BannerId + "");
//        param.put(ConstantsUtil.PAR_LOOK_BANNER_APP_ID, app_id);
//        param.put(ConstantsUtil.PAR_LOOK_BANNER_APPLICATION_ID, applicationID);
//        param.put(ConstantsUtil.PAR_LOOK_BANNER_DATE, time + "");
//        String url = ConstantsUtil.API_URL_LOOK_BANNER;
////        SCGlobalUtils.addAditionalHeader = true ;
////        SCGlobalUtils.additionalHeaderTag = "Authorization";
////        SCGlobalUtils.additionalHeaderValue = "Bearer "+getBase64(SCSharedPreferencesUtils.getString(activity, SCConstants.TAG_ACCESS_TOKEN, null));
//        APIUtils.LoadJSON(activity, param, url, new APICallBack() {
//            @Override
//            public void uiStart() {
//
//            }
//
//            @Override
//            public void success(String successString, int type) {
//                Log.e("API LOOK BANNER", successString);
//            }
//
//            @Override
//            public void fail(String failString) {
//
//            }
//
//            @Override
//            public void uiEnd() {
//
//            }
//        });
//    }
//
//    public void runAPIClickBanner(final String app_id, String applicationID, final Activity activity) {
//        long time = Utils.getTimeInMilisecond();
//        String key = Utils.getMD5Key(ConstantsUtil.SECRET_KEY, app_id, time);
//        HashMap<String, String> param = new HashMap<>();
//        param.put(ConstantsUtil.PAR_CLICK_BANNER_KEY, key);
//        param.put(ConstantsUtil.PAR_CLICK_BANNER_ADVERTISE_ID, this.BannerId);
//        param.put(ConstantsUtil.PAR_CLICK_BANNER_APP_ID, app_id);
//        param.put(ConstantsUtil.PAR_CLICK_BANNER_APPLICATION_ID, applicationID);
//        param.put(ConstantsUtil.PAR_CLICK_BANNER_DATE, time + "");
//        String url = ConstantsUtil.API_URL_CLICK_BANNER;
////        SCGlobalUtils.addAditionalHeader = true ;
////        SCGlobalUtils.additionalHeaderTag = "Authorization";
////        SCGlobalUtils.additionalHeaderValue = "Bearer "+getBase64(SCSharedPreferencesUtils.getString(activity, SCConstants.TAG_ACCESS_TOKEN, null));
//        APIUtils.LoadJSON(activity, param, url, new APICallBack() {
//            @Override
//            public void uiStart() {
//                Utils.showLoadingProgress(activity);
//            }
//
//            @Override
//            public void success(String successString, int type) {
//                try {
//                    JSONObject jsonObject = new JSONObject(successString);
//                    JSONObject bannerJSONObject = jsonObject.getJSONObject(ConstantsUtil.TAG_CLICK_BANNER_BANNER);
//                    final SCBannerItem bannerItem = new SCBannerItem();
//                    bannerItem.ParseJSONClickBanner(bannerJSONObject);
////                    bannerItem.setOpenType(2); for test open in webview
//                    if (bannerItem.getAddAppId()) {
//                        String bannerUrl = bannerItem.getUrl();
//                        bannerItem.setUrl(addAppIdClickBanner(bannerUrl, app_id));
//                    }
//                    if (bannerItem.getOpenType() == ConstantsUtil.OPEN_TYPE_CLICK_BANNER_BY_BROWSER) {
//
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bannerItem.getUrl()));
//                                activity.startActivity(browserIntent);
//                            }
//                        });
//
//                    } else if (bannerItem.getOpenType() == ConstantsUtil.OPEN_TYPE_CLICK_BANNER_BY_WEBVIEW) {
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                Intent i = new Intent(activity, AdvActivity.class);
//                                i.putExtra(ConstantsUtil.ADV_URL_INTENT, bannerItem.getUrl());
//                                i.putExtra(ConstantsUtil.ADV_TITLE_INTENT, bannerItem.getName());
//                                activity.startActivity(i);
//                                activity.overridePendingTransition(R.anim.anim_slide_in_bottom,
//                                        R.anim.anim_slide_out_bottom);
//                            }
//                        });
//                    } else {
//                        String action = "";
//                        String page = "";
//                        String id = "";
//                        if (bannerItem.getUrl() != "") {
//                            Uri uri = Uri.parse(bannerItem.getUrl());
//                            if (uri != null) {
//                                action = uri.getQueryParameter(ConstantsUtil.SCHEME_ACTION_TAG);
//                                page = uri.getQueryParameter(ConstantsUtil.SCHEME_PAGE_TAG);
//                                id = uri.getQueryParameter(ConstantsUtil.SCHEME_ID_TAG);
//                                FragmentTransaction ft;
//                                if (!page.equals("") & page.equals(ConstantsUtil.SCHEME_CAMPUS_TAG)) {
//                                    Bundle bundle = new Bundle();
//                                    CampusWorkItem item = new CampusWorkItem();
//                                    item.setId(Integer.parseInt(id));
//                                    bundle.putSerializable(ConstantsUtil.TAG_CAMPUS_WORK_DETAIL_CAMPUS_WORK, item);
//                                    bundle.putInt("position", 0);
//                                    FragmentCampusWorkDetail detail = new FragmentCampusWorkDetail();
//
//                                    detail.setArguments(bundle);
//                                    ft = ((DefaultActivity) activity).getSupportFragmentManager().beginTransaction();
//                                    ft.setCustomAnimations(R.anim.view_transition_in_left,
//                                            R.anim.view_transition_out_right,
//                                            R.anim.view_transition_in_left,
//                                            R.anim.view_transition_out_right);
//                                    ft.add(R.id.lnl_content_default_activity, detail, "fragment_campus_work_detail");
//                                    ft.addToBackStack(null);
//                                    ft.commit();
//                                } else if (!page.equals("") & page.equals(ConstantsUtil.SCHEME_MAGAZINE_TAG)) {
//                                    Bundle bundle = new Bundle();
//                                    StudentMagazineItem item = new StudentMagazineItem();
//                                    item.setId(id);
//                                    bundle.putSerializable(ConstantsUtil.TAG_STUDENT_MAGAZINE_DETAIL_MAGAZINE, item);
//                                    FragmentStudentMagazineDetail detail = new FragmentStudentMagazineDetail();
//                                    detail.setArguments(bundle);
//                                    ft = ((DefaultActivity) activity).getSupportFragmentManager().beginTransaction();
//                                    ft.setCustomAnimations(R.anim.view_transition_in_left,
//                                            R.anim.view_transition_out_right,
//                                            R.anim.view_transition_in_left,
//                                            R.anim.view_transition_out_right);
//                                    ft.add(R.id.lnl_content_default_activity, detail, "fragment_student_magazine_detail");
//                                    ft.addToBackStack(null);
//                                    ft.commit();
//                                } else if (!page.equals("") & page.equals(ConstantsUtil.SCHEME_EXCHANGE_TAG)) {
//                                    Intent i = new Intent(activity, ECDetailProductActivity.class);
//                                    i.putExtra(String.class.toString(), id);
//                                    activity.startActivity(i);
//                                }
//                            }
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void fail(String failString) {
//
//            }
//
//            @Override
//            public void uiEnd() {
//                Utils.dismissLoadingProgress();
//            }
//        });
//    }

    public static String addAppIdClickBanner(String url, String app_id) {
        String result = url;
        if (url.contains("?")) {
            result += "&app_id=" + app_id;
        } else {
            result += "?app_id=" + app_id;
        }
        return result;
    }
    public String getBase64(final String input) {
        return Base64.encodeBytes(input.getBytes(), Base64.DONT_BREAK_LINES);
    }
}
