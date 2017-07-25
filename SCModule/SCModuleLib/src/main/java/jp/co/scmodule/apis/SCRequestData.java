package jp.co.scmodule.apis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.brickred.socialauth.util.Base64;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.devsmart.android.Utils;

import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCSharedPreferencesUtils;
import jp.co.scmodule.utils.SCUrlConstants;

/**
 * Request and get data from API
 *
 * @author PhanTri
 */
public class SCRequestData {

    private SCJsonParser mSCJsonParser = null;
    private String REQUEST_DATA_URL = null;
    private int mRestType = 0;
    private Context mContex = null;
    private String request_data_url;

    public SCRequestData(Context context) {
        mSCJsonParser = new SCJsonParser();
        mContex = context;
    }

    /**
     * TODO <br>
     * Function to get data
     *
     * @return json in string
     * @author Phan Tri
     * @date Oct 15, 2014
     */
    @SuppressWarnings("unchecked")
    public String getData(int typeOfRequest, final HashMap<String, Object> parameters) {
        ArrayList<Object> listParams = new ArrayList<Object>();
        ArrayList<NameValuePair> nameValueParams = new ArrayList<NameValuePair>();
        ArrayList<Map.Entry<String, Bitmap>> bitmapParams = new ArrayList<Map.Entry<String, Bitmap>>();
        JSONObject returnJson = null;

        switch (typeOfRequest) {
            case SCConstants.REQUEST_LOGIN_FACEBOOK:
                mRestType = SCConstants.REST_GET;
                REQUEST_DATA_URL = String.format(SCUrlConstants.URL_LOGIN_FACEBOOK,
                        SCConstants.PARAM_FACEBOOK_TOKEN,
                        parameters.get(SCConstants.PARAM_FACEBOOK_TOKEN),
                        SCConstants.PARAM_APPLICATION_ID,
                        parameters.get(SCConstants.PARAM_APPLICATION_ID),
                        SCConstants.PARAM_AGENT,
                        parameters.get(SCConstants.PARAM_AGENT));
                break;

            case SCConstants.REQUEST_LOGIN_TWITTER:
                mRestType = SCConstants.REST_GET;
                request_data_url = REQUEST_DATA_URL;

                request_data_url = String.format(SCUrlConstants.URL_LOGIN_TWITTER,
                        SCConstants.PARAM_TWITTER_ACCESS_TOKEN,
                        parameters.get(SCConstants.PARAM_TWITTER_ACCESS_TOKEN),
                        SCConstants.PARAM_TWITTER_ACCESS_TOKEN_SECRET,
                        parameters.get(SCConstants.PARAM_TWITTER_ACCESS_TOKEN_SECRET),
                        SCConstants.PARAM_APPLICATION_ID,
                        parameters.get(SCConstants.PARAM_APPLICATION_ID),
                        SCConstants.PARAM_AGENT,
                        parameters.get(SCConstants.PARAM_AGENT));
                break;

            case SCConstants.REQUEST_LOGIN_MAIL:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_LOGIN_MAIL;

                // add values params
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_EMAIL,
                        (String) parameters.get(SCConstants.PARAM_EMAIL)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_PASSWORD,
                        (String) parameters.get(SCConstants.PARAM_PASSWORD)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PARAM_APPLICATION_ID)));
                break;

            case SCConstants.REQUEST_GET_GROUP_LIST:
                mRestType = SCConstants.REST_GET;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_GROUP_LIST;
                break;

            case SCConstants.REQUEST_CHECK_VERSION_UPDATE:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_CHECK_VERSION_UPDATE;
                nameValueParams.add(new BasicNameValuePair(SCConstants.PAR_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PAR_APPLICATION_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_AGENT,
                        (String) parameters.get(SCConstants.PARAM_AGENT)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.TAG_VERSION,
                        (String) parameters.get(SCConstants.TAG_VERSION)));

                break;

            case SCConstants.REQUEST_REGISTER_MAIL:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_REGISTER_MAIL;

                // add values params
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_EMAIL,
                        (String) parameters.get(SCConstants.PARAM_EMAIL)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_PASSWORD,
                        (String) parameters.get(SCConstants.PARAM_PASSWORD)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_AGENT,
                        (String) parameters.get(SCConstants.PARAM_AGENT)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PARAM_APPLICATION_ID)));
                break;

            case SCConstants.REQUEST_REGISTER_GROUP:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_REGISTER_GROUP;

                // add values params
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_GROUP_NAME,
                        (String) parameters.get(SCConstants.PARAM_GROUP_NAME)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));

                break;


            case SCConstants.REQUEST_JOIN_GROUP:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_JOIN_GROUP;

                // add values params
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_GROUP_ID,
                        (String) parameters.get(SCConstants.PARAM_GROUP_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));

                break;

            case SCConstants.REQUEST_GET_UNIVERSITY:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_UNIVERSITY;
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_DEPARTMENT:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_DEPARTMENT;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_UNIV_ID,
                        (String) parameters.get(SCConstants.PARAM_UNIV_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_CAMPUS_ID,
                        (String) parameters.get(SCConstants.PARAM_CAMPUS_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_HISTORY_ITEMS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_POINT_HISTORY;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_BANNERS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCConstants.API_URL_GET_BANNER;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PAR_GET_BANNER_KEY,
                        (String) parameters.get(SCConstants.PAR_GET_BANNER_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PAR_GET_BANNER_APP_ID,
                        (String) parameters.get(SCConstants.PAR_GET_BANNER_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PAR_GET_BANNER_DATE,
                        (String) parameters.get(SCConstants.PAR_GET_BANNER_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PAR_GET_BANNER_LAST_UPDATE,
                        (String) parameters.get(SCConstants.PAR_GET_BANNER_LAST_UPDATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PAR_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PAR_APPLICATION_ID)));

                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));

                break;


            case SCConstants.REQUEST_GET_CAMPUS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_CAMPUS;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_UNIV_ID,
                        (String) parameters.get(SCConstants.PARAM_UNIV_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_MAJOR:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_MAJOR;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DEPARTMENT_ID,
                        (String) parameters.get(SCConstants.PARAM_DEPARTMENT_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_PREFECTURE:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_PREFECTURE;
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_REGISTER_USER:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_REGISTER_USER;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_NICKNAME,
                        (String) parameters.get(SCConstants.PARAM_NICKNAME)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_UNIV_ID,
                        (String) parameters.get(SCConstants.PARAM_UNIV_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_CAMPUS_ID,
                        (String) parameters.get(SCConstants.PARAM_CAMPUS_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DEPARTMENT_ID,
                        (String) parameters.get(SCConstants.PARAM_DEPARTMENT_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_MAJOR_ID,
                        (String) parameters.get(SCConstants.PARAM_MAJOR_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_ENROLLMENT_YEAR,
                        (String) parameters.get(SCConstants.PARAM_ENROLLMENT_YEAR)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PARAM_APPLICATION_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DEVICE_ID,
                        (String) parameters.get(SCConstants.PARAM_DEVICE_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_AGENT,
                        (String) parameters.get(SCConstants.PARAM_AGENT)));
                if (parameters.containsKey(SCConstants.PARAM_BIRTHDAY)) {
                    nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_BIRTHDAY,
                            (String) parameters.get(SCConstants.PARAM_BIRTHDAY)));
                }

                break;

            case SCConstants.REQUEST_GET_APPLICATION:

                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_APPLICATION;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_AGENT,
                        (String) parameters.get(SCConstants.PARAM_AGENT)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PARAM_APPLICATION_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_UPDATE_USER:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_UPDATE_USER;
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_NICKNAME,
                        (String) parameters.get(SCConstants.PARAM_NICKNAME)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_UNIV_ID,
                        (String) parameters.get(SCConstants.PARAM_UNIV_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_CAMPUS_ID,
                        (String) parameters.get(SCConstants.PARAM_CAMPUS_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DEPARTMENT_ID,
                        (String) parameters.get(SCConstants.PARAM_DEPARTMENT_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_ENROLLMENT_YEAR,
                        (String) parameters.get(SCConstants.PARAM_ENROLLMENT_YEAR)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_AGENT,
                        (String) parameters.get(SCConstants.PARAM_AGENT)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DEVICE_ID,
                        (String) parameters.get(SCConstants.PARAM_DEVICE_ID)));

                if (parameters.containsKey(SCConstants.PARAM_MAJOR_ID)) {
                    nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_MAJOR_ID,
                            (String) parameters.get(SCConstants.PARAM_MAJOR_ID)));
                }

                if (parameters.containsKey(SCConstants.PARAM_EMAIL)) {
                    nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_EMAIL,
                            (String) parameters.get(SCConstants.PARAM_EMAIL)));
                }

                if (parameters.containsKey(SCConstants.PARAM_SEX)) {
                    nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_SEX,
                            (String) parameters.get(SCConstants.PARAM_SEX)));
                }

                if (parameters.containsKey(SCConstants.PARAM_BIRTHDAY)) {
                    nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_BIRTHDAY,
                            (String) parameters.get(SCConstants.PARAM_BIRTHDAY)));
                }

                if (parameters.containsKey(SCConstants.PARAM_POST_CODE)) {
                    nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_POST_CODE,
                            (String) parameters.get(SCConstants.PARAM_POST_CODE)));
                }

                if (parameters.containsKey(SCConstants.PARAM_PREFECTURE_ID)) {
                    nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_PREFECTURE_ID,
                            (String) parameters.get(SCConstants.PARAM_PREFECTURE_ID)));
                }

                if (parameters.containsKey(SCConstants.PARAM_PHONE_NUMBER)) {
                    nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_PHONE_NUMBER,
                            (String) parameters.get(SCConstants.PARAM_PHONE_NUMBER)));
                }

                if (parameters.containsKey(SCConstants.PARAM_ADDRESS)) {
                    nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_ADDRESS,
                            (String) parameters.get(SCConstants.PARAM_ADDRESS)));
                }
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_USER:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_USER;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));


                break;
//            case SCConstants.REQUEST_GET_USER_BY_ACCESS_TOKEN:
//                mRestType = SCConstants.REST_POST;
//                REQUEST_DATA_URL = SCUrlConstants.URL_GET_USER_BY_ACCESS_TOKEN;
//
//                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
//                        (String) parameters.get(SCConstants.PARAM_DATE)));
//
//                SCGlobalUtils.addAditionalHeader = true ;
//                SCGlobalUtils.additionalHeaderTag = "Authorization";
//                SCGlobalUtils.additionalHeaderValue = "Bearer "+getBase64(SCSharedPreferencesUtils.getString(mContex,SCConstants.TAG_ACCESS_TOKEN, null));
//
//
//                break;
            case SCConstants.REQUEST_GET_ACCESSTOKEN:
                mRestType = SCConstants.REST_GET;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_ACCESSTOKEN
                        + "?" + SCConstants.PARAM_STATE + "=" + parameters.get(SCConstants.PARAM_STATE)
                        + "&" + SCConstants.PARAM_DATE + "=" + parameters.get(SCConstants.PARAM_DATE)
                        + "&" + "client_id" + "=" + parameters.get("client_id")
                        + "&" + SCConstants.PARAM_UUID + "=" + parameters.get(SCConstants.PARAM_UUID);

                break;

            case SCConstants.REQUEST_GET_GROUP_DETAILS:
                mRestType = SCConstants.REST_GET;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_GROUP_DETAILS + parameters.get(SCConstants.PARAM_GROUP_ID);
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));

                break;

            case SCConstants.REQUEST_CHECK_ACCESS_TOKEN_VALIDATION:
                mRestType = SCConstants.REST_GET;
                REQUEST_DATA_URL = SCUrlConstants.URL_CHECK_ACCESS_TOKEN_VALIDATION;
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_ACCESSTOKEN_USING_REFRESHTOKEN:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_ACCESSTOKEN_USING_REFRESHTOKEN;
                nameValueParams.add(new BasicNameValuePair("grant_type",
                        (String) parameters.get("grant_type")));
                nameValueParams.add(new BasicNameValuePair("refresh_token",
                        (String) parameters.get("refresh_token")));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Basic " + getBase64(SCConstants.TADACOPY_CLIENT_ID + ":" + SCConstants.TADACOPY_CLIENT_ID_SECRET);
                break;
            case SCConstants.REQUEST_GET_USER_BY_ACCESS_TOKEN:
                mRestType = SCConstants.REST_GET;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_USER_BY_ACCESS_TOKEN;
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;
            case SCConstants.REQUEST_LOGOUT:
                mRestType = SCConstants.REST_GET;
                REQUEST_DATA_URL = SCUrlConstants.URL_API_LOGOUT;
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_PASSWORD_REMINDER:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_PASSWORD_REMINDER;
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_EMAIL,
                        (String) parameters.get(SCConstants.PARAM_EMAIL)));
                break;

            case SCConstants.REQUEST_GET_INFORMATIONS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_INFORMATIONS;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_EMAIL,
                        (String) parameters.get(SCConstants.PARAM_EMAIL)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PARAM_APPLICATION_ID)));

                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_EXCHANGE_ITEMS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_EXCHANGE_ITEMS;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_SHOP_ID,
                        (String) parameters.get(SCConstants.PARAM_SHOP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_CATEGORY_ID,
                        (String) parameters.get(SCConstants.PARAM_CATEGORY_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_ORDER_TYPE,
                        (String) parameters.get(SCConstants.PARAM_ORDER_TYPE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEYWORD,
                        (String) parameters.get(SCConstants.PARAM_KEYWORD)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));

                break;

            case SCConstants.REQUEST_GET_SHOPS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_SHOPS;
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));

                break;

            case SCConstants.REQUEST_GET_CATEGORIES:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_CATEGORIES;
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));

                break;

            case SCConstants.REQUEST_GET_SHOP_DETAIL:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_SHOP_DETAIL;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_SHOP_ID,
                        (String) parameters.get(SCConstants.PARAM_SHOP_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_FOLLOW_SHOP:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_FOLLOW_SHOP;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_SHOP_ID,
                        (String) parameters.get(SCConstants.PARAM_SHOP_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_ADD_FAVORITE_ITEM:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_ADD_FAVORITE_ITEM;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_EXCHANGE_ITEM_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_EXCHANGE_ITEM_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_FOLLOW_SHOPS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_FOLLOW_SHOPS;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_FAVORITE_ITEMS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_FAVORITE_ITEMS;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_EXCHANGE_LOGS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_EXCHANGE_LOGS;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_PAY_EXCHANGE_ITEM:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_PAY_EXCHANGE_ITEM;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_EXCHANGE_ITEM_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_EXCHANGE_ITEM_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PARAM_APPLICATION_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_RECOMMEND_ITEM_DETAILS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_RECOMMEND_ITEM_DETAILS;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));

                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_ICON_UPDATE:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_ICON_UPDATE;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));

                if (parameters.containsKey(SCConstants.PARAM_ICON)) {
                    // create hash map to save avatar bitmap
                    Map.Entry<String, Bitmap> hashIcon = new Map.Entry<String, Bitmap>() {

                        @Override
                        public String getKey() {
                            // TODO Auto-generated method stub
                            return SCConstants.PARAM_ICON;
                        }

                        @Override
                        public Bitmap getValue() {
                            // TODO Auto-generated method stub
                            return (Bitmap) parameters.get(SCConstants.PARAM_ICON);
                        }

                        @Override
                        public Bitmap setValue(Bitmap object) {
                            // TODO Auto-generated method stub
                            return (Bitmap) parameters.get(SCConstants.PARAM_ICON);
                        }
                    };

                    bitmapParams.add(hashIcon);
                }
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_LESSONS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_LESSONS;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_REGISTER_DEVICE:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_REGISTER_DEVICE;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DEVICE_ID,
                        (String) parameters.get(SCConstants.PARAM_DEVICE_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_CHECK_SURVEY_STATUS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_CHECK_SURVEY_STATUS;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PAR_JOIN_CAMPUS_WORK_KEY,
                        (String) parameters.get(SCConstants.PAR_JOIN_CAMPUS_WORK_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PAR_JOIN_CAMPUS_WORK_DATE,
                        (String) parameters.get(SCConstants.PAR_JOIN_CAMPUS_WORK_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PAR_JOIN_CAMPUS_WORK_APP_ID,
                        (String) parameters.get(SCConstants.PAR_JOIN_CAMPUS_WORK_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PAR_JOIN_CAMPUS_WORK_CAMPUS_WORK_ID,
                        (String) parameters.get(SCConstants.PAR_JOIN_CAMPUS_WORK_CAMPUS_WORK_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_USER_BY_DEVICE_ID:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_USER_BY_DEVICE_ID;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DEVICE_ID,
                        (String) parameters.get(SCConstants.PARAM_DEVICE_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PARAM_APPLICATION_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
//                SCGlobalUtils.addAditionalHeader = true ;
//                SCGlobalUtils.additionalHeaderTag = "Authorization";
//                SCGlobalUtils.additionalHeaderValue = "Bearer "+getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_CHECK_POINT:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_CHECK_POINT;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_EXCHANGE_LOG_DETAIL:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_EXCHANGE_LOG_DETAIL;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_EXCHANGE_LOG_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_EXCHANGE_LOG_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_FOR_MAKE_FAV:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_FOR_MAKE_FAV;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_MAGAZINE_ID,
                        (String) parameters.get(SCConstants.PARAM_MAGAZINE_ID)));

                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_FOR_LOGIN_POINT:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_FOR_GET_LOGIN_POINT;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PARAM_APPLICATION_ID)));

                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_FOR_GET_MAGAZINES:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_FOR_GET_MAGAZINES;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID,
                    (String) parameters.get(SCConstants.PARAM_APPLICATION_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_LAST_UPDATE,
                        (String) parameters.get(SCConstants.PARAM_LAST_UPDATE)));

                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_FOR_GET_CAMPUS_WORK:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_FOR_GET_CAMPUS_WORK;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PARAM_APPLICATION_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_LAST_UPDATE,
                        (String) parameters.get(SCConstants.PARAM_LAST_UPDATE)));

                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_EXCHANGE_ITEM_DETAIL:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_EXCHANGE_ITEM_DETAIL;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_EXCHANGE_ITEM_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_EXCHANGE_ITEM_ID)));
                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            case SCConstants.REQUEST_GET_BANNERS_CAMPUSAN:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_BANNERS_CAMPUSAN;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_AUTH_KEY,
                        (String) parameters.get(SCConstants.PARAM_AUTH_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_AUTH_DATE,
                        (String) parameters.get(SCConstants.PARAM_AUTH_DATE)));
                break;

            case SCConstants.REQUEST_FOR_GET_BEACONS:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_BEACONS;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PARAM_APPLICATION_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_BEACON_TYPE_ID,
                        (String) parameters.get(SCConstants.PARAM_BEACON_TYPE_ID)));

                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;
            case SCConstants.REQUEST_FOR_GET_BEACONS_OLD:
                mRestType = SCConstants.REST_POST;
                REQUEST_DATA_URL = SCUrlConstants.URL_GET_BEACONS_OLD;

                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APP_ID,
                        (String) parameters.get(SCConstants.PARAM_APP_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_KEY,
                        (String) parameters.get(SCConstants.PARAM_KEY)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_DATE,
                        (String) parameters.get(SCConstants.PARAM_DATE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_APPLICATION_ID,
                        (String) parameters.get(SCConstants.PARAM_APPLICATION_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_BEACON_ID,
                        (String) parameters.get(SCConstants.PARAM_BEACON_ID)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_BEACON_LATITUDE,
                        (String) parameters.get(SCConstants.PARAM_BEACON_LATITUDE)));
                nameValueParams.add(new BasicNameValuePair(SCConstants.PARAM_BEACON_LONGITUDE,
                        (String) parameters.get(SCConstants.PARAM_BEACON_LONGITUDE)));

                SCGlobalUtils.addAditionalHeader = true;
                SCGlobalUtils.additionalHeaderTag = "Authorization";
                SCGlobalUtils.additionalHeaderValue = "Bearer " + getBase64(SCSharedPreferencesUtils.getString(mContex, SCConstants.TAG_ACCESS_TOKEN, null));
                break;

            default:
                break;
        }

        listParams.add(nameValueParams);
        listParams.add(bitmapParams);
        // Building Parameters
        Log.e("Request URL:", REQUEST_DATA_URL);
        returnJson = mSCJsonParser.getJSONFromUrl(REQUEST_DATA_URL, mRestType, listParams);

        return (returnJson != null) ? returnValues(returnJson) : null;
    }

    /**
     * TODO <br>
     * Function to return values from server after check <br>
     *
     * @param returnObj
     * @return
     * @author Phan Tri
     * @date Oct 15, 2014
     */
    public String returnValues(JSONObject returnObj) {
        return returnObj.toString();
    }

    public String getBase64(final String input) {
        return Base64.encodeBytes(input.getBytes(), Base64.DONT_BREAK_LINES);
    }


}
