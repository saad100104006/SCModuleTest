package jp.co.scmodule.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.scmodule.objects.CampusanAdObject;
import jp.co.scmodule.objects.ECCategoryObject;
import jp.co.scmodule.objects.ECProductObject;
import jp.co.scmodule.objects.ECShopObject;
import jp.co.scmodule.objects.LessonObject;
import jp.co.scmodule.objects.SCAppObject;
import jp.co.scmodule.objects.SCCampusObject;
import jp.co.scmodule.objects.SCDepartmentObject;
import jp.co.scmodule.objects.SCGroupObject;
import jp.co.scmodule.objects.SCHistoryObject;
import jp.co.scmodule.objects.SCInformationObject;
import jp.co.scmodule.objects.SCLessonObject;
import jp.co.scmodule.objects.SCLocationObject;
import jp.co.scmodule.objects.SCMajorObject;
import jp.co.scmodule.objects.SCMemberObject;
import jp.co.scmodule.objects.SCPrefectureObject;
import jp.co.scmodule.objects.SCTypeObject;
import jp.co.scmodule.objects.SCUniversityObject;
import jp.co.scmodule.objects.SCUserObject;
import jp.co.scmodule.objects.SCTokenObject;
import jp.co.scmodule.objects.ScheduleObject;

public class SCAPIUtils {
    public static Context sContext = null;

    public SCAPIUtils(Context context) {
        sContext = context;
    }

    public static HashMap<String, Object> parseJSON(int type, String jsonString) {
        HashMap<String, Object> returnHashMap = new HashMap<String, Object>();
        try {
            JSONObject mainJsonObj = new JSONObject(jsonString);
            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArr = new JSONArray();
            String error = new String();
            String result = new String();
            int code = 0;

            switch (type) {
                case SCConstants.REQUEST_LOGIN_FACEBOOK:
                    if (mainJsonObj.has(SCConstants.TAG_DATA)) {
                        SCUserObject userObj = null;

                        if (mainJsonObj.has(SCConstants.TAG_IS_NEW_USER)) {
                            if (mainJsonObj.getString(SCConstants.TAG_IS_NEW_USER).equals("false")) {
                                userObj = (SCUserObject) parseUser(mainJsonObj.getJSONObject(SCConstants.TAG_DATA));
                                userObj.setIsNewUser("false");
                            } else {
                                userObj = (SCUserObject) parseSNSUser(mainJsonObj.getJSONObject(SCConstants.TAG_DATA));
                                userObj.setIsNewUser("true");
                            }

                            returnHashMap.put(SCConstants.TAG_DATA, userObj);
                        }
                    }

                    if (mainJsonObj.has(SCConstants.TAG_ERROR_CODE)) {
                        returnHashMap.put(SCConstants.TAG_ERROR_CODE, mainJsonObj.getString(SCConstants.TAG_ERROR_CODE));
                    }
                    break;

                case SCConstants.REQUEST_LOGIN_TWITTER:
                    if (mainJsonObj.has(SCConstants.TAG_DATA)) {
                        SCUserObject userObj = null;

                        if (mainJsonObj.has(SCConstants.TAG_IS_NEW_USER)) {
                            if (mainJsonObj.getString(SCConstants.TAG_IS_NEW_USER).equals("false")) {
                                userObj = (SCUserObject) parseUser(mainJsonObj.getJSONObject(SCConstants.TAG_DATA));
                                userObj.setIsNewUser("false");
                            } else {
                                userObj = (SCUserObject) parseSNSUser(mainJsonObj.getJSONObject(SCConstants.TAG_DATA));
                                userObj.setIsNewUser("true");
                            }

                            returnHashMap.put(SCConstants.TAG_DATA, userObj);
                        }
                    }

                    if (mainJsonObj.has(SCConstants.TAG_ERROR_CODE)) {
                        returnHashMap.put(SCConstants.TAG_ERROR_CODE, mainJsonObj.getString(SCConstants.TAG_ERROR_CODE));
                    }
                    break;

                case SCConstants.REQUEST_LOGIN_USER_DATA:
                    if (mainJsonObj.has("user_data")) {
                        SCUserObject userObj = null;

                                userObj = (SCUserObject) parseUser(mainJsonObj.getJSONObject("user_data"));



                            returnHashMap.put("user_data", userObj);

                    }

                    if (mainJsonObj.has(SCConstants.TAG_ERROR_CODE)) {
                        returnHashMap.put(SCConstants.TAG_ERROR_CODE, mainJsonObj.getString(SCConstants.TAG_ERROR_CODE));
                    }
                    break;

                case SCConstants.REQUEST_LOGIN_MAIL:
                    if (mainJsonObj.has(SCConstants.TAG_USER_DATA)) {
                        returnHashMap.put(SCConstants.TAG_USER_DATA, parseUser(mainJsonObj.getJSONObject(SCConstants.TAG_USER_DATA)));
                    }

                    if (mainJsonObj.has(SCConstants.TAG_ERROR_CODE)) {
                        returnHashMap.put(SCConstants.TAG_ERROR_CODE, mainJsonObj.getString(SCConstants.TAG_ERROR_CODE));
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }
                    break;

                case SCConstants.REQUEST_REGISTER_GROUP:
                    if (mainJsonObj.has(SCConstants.TAG_DATA)) {
                        returnHashMap.put(SCConstants.TAG_DATA, parseGroup(mainJsonObj.getJSONObject(SCConstants.TAG_DATA)));
                    }

                    if (mainJsonObj.has(SCConstants.TAG_ERROR_CODE)) {
                        returnHashMap.put(SCConstants.TAG_ERROR_CODE, mainJsonObj.getString(SCConstants.TAG_ERROR_CODE));
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }
                    break;

                case SCConstants.REQUEST_REGISTER_MAIL:
                    if (mainJsonObj.has(SCConstants.TAG_USER_DATA)) {
                        returnHashMap.put(SCConstants.TAG_USER_DATA, parseUser(mainJsonObj.getJSONObject(SCConstants.TAG_USER_DATA)));
                    }

                    if (mainJsonObj.has(SCConstants.TAG_ERROR_CODE)) {
                        returnHashMap.put(SCConstants.TAG_ERROR_CODE, mainJsonObj.getString(SCConstants.TAG_ERROR_CODE));
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));

                    }
                    break;

                case SCConstants.REQUEST_GET_UNIVERSITY:
                    if (mainJsonObj.has(SCConstants.TAG_DATA)) {
                        returnHashMap.put(SCConstants.TAG_DATA, parseUniversities(mainJsonObj.getJSONArray(SCConstants.TAG_DATA)));
                    }
                    break;

                case SCConstants.REQUEST_GET_GROUP_LIST:
                    if (mainJsonObj.has(SCConstants.TAG_DATA)) {
                        returnHashMap.put(SCConstants.TAG_DATA, parseGroups(mainJsonObj.getJSONArray(SCConstants.TAG_DATA)));
                    }
                    break;

                case SCConstants.REQUEST_GET_DEPARTMENT:
                    if (mainJsonObj.has(SCConstants.TAG_DATA)) {
                        returnHashMap.put(SCConstants.TAG_DATA, parseDepartments(mainJsonObj.getJSONArray(SCConstants.TAG_DATA)));
                    }
                    break;
                case SCConstants.REQUEST_GET_CAMPUS:
                    if (mainJsonObj.has(SCConstants.TAG_DATA)) {
                        returnHashMap.put(SCConstants.TAG_DATA, parseCampuses(mainJsonObj.getJSONArray(SCConstants.TAG_DATA)));
                    }
                    break;
                case SCConstants.REQUEST_GET_MAJOR:
                    if (mainJsonObj.has(SCConstants.TAG_DATA)) {
                        returnHashMap.put(SCConstants.TAG_DATA, parseMajors(mainJsonObj.getJSONArray(SCConstants.TAG_DATA)));
                    }
                    break;
                case SCConstants.REQUEST_GET_PREFECTURE:
                    if (mainJsonObj.has(SCConstants.TAG_DATA)) {
                        returnHashMap.put(SCConstants.TAG_DATA, parsePrefectures(mainJsonObj.getJSONArray(SCConstants.TAG_DATA)));
                    }
                    break;
                case SCConstants.REQUEST_REGISTER_USER:
                    if (mainJsonObj.has(SCConstants.TAG_APP_ID)) {
                        returnHashMap.put(SCConstants.TAG_APP_ID, parseAppId(mainJsonObj));
                    }

                    if (mainJsonObj.has(SCConstants.TAG_ERROR_CODE)) {
                        returnHashMap.put(SCConstants.TAG_ERROR_CODE, mainJsonObj.getString(SCConstants.TAG_ERROR_CODE));
                    }
                    break;
                case SCConstants.REQUEST_GET_APPLICATION:
                    if (mainJsonObj.has(SCConstants.TAG_APPLICATIONS)) {
                        returnHashMap.put(SCConstants.TAG_APPLICATIONS,
                                parseApplications(mainJsonObj.getJSONArray(SCConstants.TAG_APPLICATIONS)));
                    }

                    if (mainJsonObj.has(SCConstants.TAG_ERROR_CODE)) {
                        returnHashMap.put(SCConstants.TAG_ERROR_CODE, mainJsonObj.getString(SCConstants.TAG_ERROR_CODE));
                    }
                    break;
                case SCConstants.REQUEST_UPDATE_USER:
                    if (mainJsonObj.has(SCConstants.TAG_APP_USER)) {
                        JSONObject jsonObject = mainJsonObj.getJSONObject(SCConstants.TAG_APP_USER);
                        if(jsonObject.has("is_point_updated")) {
                            SCGlobalUtils.is_point_updated = jsonObject.getString("is_point_updated");
                        }
                        returnHashMap.put(SCConstants.TAG_APP_USER, parseUser(mainJsonObj.getJSONObject(SCConstants.TAG_APP_USER)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR_CODE)) {
                        returnHashMap.put(SCConstants.TAG_ERROR_CODE, mainJsonObj.getString(SCConstants.TAG_ERROR_CODE));
                    }

                    break;
                case SCConstants.REQUEST_GET_USER:
                    if (mainJsonObj.has(SCConstants.TAG_APP_USER)) {
                        returnHashMap.put(SCConstants.TAG_APP_USER, parseUser(mainJsonObj.getJSONObject(SCConstants.TAG_APP_USER)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR_CODE)) {
                        returnHashMap.put(SCConstants.TAG_ERROR_CODE, mainJsonObj.getString(SCConstants.TAG_ERROR_CODE));
                    }

                    break;

                case SCConstants.REQUEST_GET_INFORMATIONS:
                    if (mainJsonObj.has(SCConstants.TAG_INFORMATIONS)) {
                        returnHashMap.put(SCConstants.TAG_INFORMATIONS, parseInfomations(mainJsonObj.getJSONArray(SCConstants.TAG_INFORMATIONS)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR_CODE)) {
                        returnHashMap.put(SCConstants.TAG_ERROR_CODE, mainJsonObj.getString(SCConstants.TAG_ERROR_CODE));
                    }

                    break;

                case SCConstants.REQUEST_GET_EXCHANGE_ITEMS:
                    if (mainJsonObj.has(SCConstants.TAG_ITEMS)) {
                        returnHashMap.put(SCConstants.TAG_ITEMS, parseExchangeItems(mainJsonObj.getJSONArray(SCConstants.TAG_ITEMS)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_GET_SHOPS:
                    if (mainJsonObj.has(SCConstants.TAG_DATA)) {
                        returnHashMap.put(SCConstants.TAG_DATA, parseShops(mainJsonObj.getJSONArray(SCConstants.TAG_DATA)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_GET_CATEGORIES:
                    if (mainJsonObj.has(SCConstants.TAG_DATA)) {
                        returnHashMap.put(SCConstants.TAG_DATA, parseCategories(mainJsonObj.getJSONArray(SCConstants.TAG_DATA)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                case SCConstants.REQUEST_GET_SHOP_DETAIL:
                    if (mainJsonObj.has(SCConstants.TAG_SHOP)) {
                        returnHashMap.put(SCConstants.TAG_SHOP, parseShopDetail(mainJsonObj.getJSONObject(SCConstants.TAG_SHOP)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_FOLLOW_SHOP:
                    if (mainJsonObj.has(SCConstants.TAG_FOLLOWED)) {
                        returnHashMap.put(SCConstants.TAG_FOLLOWED, mainJsonObj.getString(SCConstants.TAG_FOLLOWED));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_ADD_FAVORITE_ITEM:
                    if (mainJsonObj.has(SCConstants.TAG_FAVORITE)) {
                        returnHashMap.put(SCConstants.TAG_FAVORITE, mainJsonObj.getString(SCConstants.TAG_FAVORITE));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_GET_FOLLOW_SHOPS:
                    if (mainJsonObj.has(SCConstants.TAG_SHOPS)) {
                        returnHashMap.put(SCConstants.TAG_SHOPS, parseFollowShops(mainJsonObj.getJSONArray(SCConstants.TAG_SHOPS)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_GET_FAVORITE_ITEMS:
                    if (mainJsonObj.has(SCConstants.TAG_ITEMS)) {
                        returnHashMap.put(SCConstants.TAG_ITEMS, parseFavoriteItems(mainJsonObj.getJSONArray(SCConstants.TAG_ITEMS)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_GET_EXCHANGE_LOGS:
                    if (mainJsonObj.has(SCConstants.TAG_APP_EXCHANGE_LOGS)) {
                        returnHashMap.put(SCConstants.TAG_APP_EXCHANGE_LOGS, parseExchangeLogs(mainJsonObj.getJSONArray(SCConstants.TAG_APP_EXCHANGE_LOGS)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;


                case SCConstants.REQUEST_GET_HISTORY_ITEMS:

                    if (mainJsonObj.has(SCConstants.TAG_POINT_HISTORY_LOGS)){
                        returnHashMap.put(SCConstants.TAG_POINT_HISTORY_LOGS, parseExchangePointLogs(mainJsonObj.getJSONArray(SCConstants.TAG_POINT_HISTORY_LOGS)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;



                case SCConstants.REQUEST_PAY_EXCHANGE_ITEM:
                    if (mainJsonObj.has(SCConstants.TAG_AFTER_POINT)) {
                        returnHashMap.put(SCConstants.TAG_AFTER_POINT, mainJsonObj.getString(SCConstants.TAG_AFTER_POINT));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR_CODE)) {
                        returnHashMap.put(SCConstants.TAG_ERROR_CODE, mainJsonObj.getString(SCConstants.TAG_ERROR_CODE));
                    }

                    break;

                case SCConstants.REQUEST_ICON_UPDATE:
                    if (mainJsonObj.has(SCConstants.TAG_URL)) {
                        returnHashMap.put(SCConstants.TAG_URL, mainJsonObj.getString(SCConstants.TAG_URL));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_GET_LESSONS:
                    if (mainJsonObj.has(SCConstants.TAG_LESSONS)) {
                        returnHashMap.put(SCConstants.TAG_LESSONS, parseLessons(mainJsonObj.getJSONArray(SCConstants.TAG_LESSONS)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_REGISTER_DEVICE:
                    if (mainJsonObj.has(SCConstants.TAG_DEVICE_ID)) {
                        returnHashMap.put(SCConstants.TAG_DEVICE_ID, mainJsonObj.getString(SCConstants.TAG_DEVICE_ID));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_GET_USER_BY_DEVICE_ID:
                    if (mainJsonObj.has(SCConstants.TAG_APP_USER)) {
                        returnHashMap.put(SCConstants.TAG_APP_USER, parseUserType(mainJsonObj.getJSONObject(SCConstants.TAG_APP_USER)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;
                case SCConstants.REQUEST_GET_USER_BY_ACCESS_TOKEN:
                    if (mainJsonObj.has(SCConstants.TAG_DATA)) {
                        returnHashMap.put(SCConstants.TAG_DATA, parseUser(mainJsonObj.getJSONObject(SCConstants.TAG_DATA)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_CHECK_POINT:
                    if (mainJsonObj.has(SCConstants.TAG_CAMPUS_POINT)) {
                        returnHashMap.put(SCConstants.TAG_CAMPUS_POINT, mainJsonObj.getString(SCConstants.TAG_CAMPUS_POINT));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_GET_EXCHANGE_LOG_DETAIL:
                    if (mainJsonObj.has(SCConstants.TAG_APP_EXCHANGE_LOG)) {
                        returnHashMap.put(SCConstants.TAG_APP_EXCHANGE_LOG, parseExchangeLog(mainJsonObj.getJSONObject(SCConstants.TAG_APP_EXCHANGE_LOG)));
                    }

                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;
                case SCConstants.REQUEST_GET_ACCESSTOKEN:
                  if(mainJsonObj.getString("success").equals("true")){
                      if(mainJsonObj.has(SCConstants.TAG_DATA)){
                          returnHashMap.put(SCConstants.TAG_DATA,parseToken(mainJsonObj.getJSONObject(SCConstants.TAG_DATA)));
                      }
                  }
                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;

                case SCConstants.REQUEST_GET_BANNERS_CAMPUSAN:
                    if (mainJsonObj.has("body")) {
                        returnHashMap.put(SCConstants.TAG_ITEM, parseAdCampusan(mainJsonObj.getJSONArray("body")));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }
                    break;

                case SCConstants.REQUEST_GET_EXCHANGE_ITEM_DETAIL:
                    if (mainJsonObj.has(SCConstants.TAG_ITEM)) {
                        returnHashMap.put(SCConstants.TAG_ITEM, parseExchangeItemDetail(mainJsonObj.getJSONObject(SCConstants.TAG_ITEM)));
                    }


                    if (mainJsonObj.has(SCConstants.TAG_ERROR)) {
                        returnHashMap.put(SCConstants.TAG_ERROR, mainJsonObj.getString(SCConstants.TAG_ERROR));
                    }

                    break;
                default:
                    break;
            }
        } catch (Exception e) {
                 e.printStackTrace();
        }

        return returnHashMap;
    }

    private static  ArrayList<SCHistoryObject> parseExchangePointLogs(JSONArray src) {

        ArrayList<SCHistoryObject> history = new ArrayList<SCHistoryObject>();
        JSONArray jsonArray = (JSONArray) src;

        SCHistoryObject historyObj;

        if (src instanceof JSONArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject log = jsonArray.getJSONObject(i);
                    historyObj = new SCHistoryObject();
                    historyObj.setExchange_item_name(log.getString("exchange_item_name"));
                    historyObj.setPoint_item_name(log.getString("point_item_name"));
                    historyObj.setCreated(log.getString("created"));
                    historyObj.setService(log.getString("service"));
                    historyObj.setDiff(log.getString("diff"));

                    history.add(historyObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return history;

    }

    private static SCGroupObject parseGroup(JSONObject src) {

        SCGroupObject groupObject = new SCGroupObject();
        JSONObject jObj = (JSONObject) src;
        try {
            groupObject.setGroup_id(jObj.getString(SCConstants.TAG_ID));
            groupObject.setGruop_name(jObj.getString(SCConstants.TAG_NAME));
            groupObject.setStudent_group_form_id(jObj.getString("student_group_form_id"));
            groupObject.setStudent_group_type_id(jObj.getString("student_group_type_id"));
            if(jObj.has("student_group_member_no"))
                groupObject.setGroup_member_count(jObj.getString("student_group_member_no"));
            groupObject.setImage(jObj.getString("image"));
            groupObject.setUniv_id(jObj.getString("univ_id"));
            groupObject.setPhone_number(jObj.getString("phone_number"));
            groupObject.setEmail(jObj.getString("email"));
            groupObject.setOfficial_url(jObj.getString("official_url"));
            groupObject.setFacebook_url(jObj.getString("facebook_url"));
            groupObject.setTwitter_url(jObj.getString("twitter_url"));
            groupObject.setRate(jObj.getString("rate"));
            groupObject.setFacebook_url(jObj.getString("facebook_url"));
            groupObject.setTwitter_url(jObj.getString("twitter_url"));
            groupObject.setRate(jObj.getString("rate"));
            groupObject.setLocation(jObj.getString("location"));
            groupObject.setDescription(jObj.getString("description"));
            groupObject.setLeader_member_id(jObj.getString("leader_member_id"));
            groupObject.setBank_name(jObj.getString("bank_name"));
            groupObject.setBank_shop_name(jObj.getString("bank_shop_name"));
            groupObject.setBank_category_id(jObj.getString("bank_category_id"));
            groupObject.setBank_number(jObj.getString("bank_number"));
            groupObject.setMemo(jObj.getString("memo"));
            groupObject.setCreated(jObj.getString("created"));
            groupObject.setUpdated(jObj.getString("updated"));
            groupObject.setDisable(jObj.getString("disable"));

            groupObject.setCampus_point(jObj.getString("campus_point"));
            groupObject.setNext_grade_point(jObj.getString("next_grade_point"));

            if(jObj.has("members")){
                JSONArray jsonArray = jObj.getJSONArray("members");
                ArrayList<SCMemberObject> memberList = new ArrayList<SCMemberObject>();
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    SCMemberObject memberObject = new SCMemberObject();
                    memberObject.setNickname(jsonObject.getString("nickname"));
                    memberObject.setIcon(jsonObject.getString("icon"));
                    memberObject.setEmail(jsonObject.getString("email"));
                    memberObject.setApp_user_id(jsonObject.getString("app_user_id"));
                    memberObject.setTel_mobile(jsonObject.getString("tel_mobile"));
                    memberObject.setPosition(jsonObject.getString("position"));
                    memberObject.setCreated(jsonObject.getString("created"));
                    memberObject.setUpdated(jsonObject.getString("updated"));
                    memberObject.setDisable(jsonObject.getString("disable"));
                    memberObject.setMemo(jsonObject.getString("memo"));
                    memberObject.setStudent_group_id(jsonObject.getString("student_group_id"));
                    memberObject.setCampus_point(jsonObject.getString("campus_point"));
                    memberList.add(memberObject);
                }
                groupObject.setMemberList(memberList);
            }

            if(jObj.has(SCConstants.TAG_INFORMATIONS)){
                groupObject.setInformationList(parseInfomationsForgroup(jObj.getJSONArray(SCConstants.TAG_INFORMATIONS)));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return groupObject;
    }

    private static ArrayList<SCGroupObject> parseGroups(JSONArray src) {
        ArrayList<SCGroupObject> groups = new ArrayList<SCGroupObject>();
        JSONArray jGroups = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jGroups.length(); i++) {
                try {
                    JSONObject jObj = jGroups.getJSONObject(i);
                    SCGroupObject groupObject = new SCGroupObject();

                    groupObject.setGroup_id(jObj.getString(SCConstants.TAG_ID));
                    groupObject.setGruop_name(jObj.getString(SCConstants.TAG_NAME));
                    groupObject.setStudent_group_form_id(jObj.getString("student_group_form_id"));
                    groupObject.setStudent_group_type_id(jObj.getString("student_group_type_id"));
                    groupObject.setGroup_member_count(jObj.getString("student_group_member_no"));
                    groupObject.setImage(jObj.getString("image"));
                    groupObject.setUniv_id(jObj.getString("univ_id"));
                    groupObject.setPhone_number(jObj.getString("phone_number"));
                    groupObject.setEmail(jObj.getString("email"));
                    groupObject.setOfficial_url(jObj.getString("official_url"));
                    groupObject.setFacebook_url(jObj.getString("facebook_url"));
                    groupObject.setTwitter_url(jObj.getString("twitter_url"));
                    groupObject.setRate(jObj.getString("rate"));
                    groupObject.setFacebook_url(jObj.getString("facebook_url"));
                    groupObject.setTwitter_url(jObj.getString("twitter_url"));
                    groupObject.setRate(jObj.getString("rate"));
                    groupObject.setLocation(jObj.getString("location"));
                    groupObject.setDescription(jObj.getString("description"));
                    groupObject.setLeader_member_id(jObj.getString("leader_member_id"));
                    groupObject.setBank_name(jObj.getString("bank_name"));
                    groupObject.setBank_shop_name(jObj.getString("bank_shop_name"));
                    groupObject.setBank_category_id(jObj.getString("bank_category_id"));
                    groupObject.setBank_number(jObj.getString("bank_number"));
                    groupObject.setMemo(jObj.getString("memo"));
                    groupObject.setCreated(jObj.getString("created"));
                    groupObject.setUpdated(jObj.getString("updated"));
                    groupObject.setDisable(jObj.getString("disable"));


                    groups.add(groupObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        return groups;
    }

    private static ArrayList<SCLessonObject> parseLessons(Object src) {
        ArrayList<SCLessonObject> lessons = new ArrayList<SCLessonObject>();
        JSONArray jsonArray = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    SCLessonObject lessonObj = new SCLessonObject();

                    lessonObj.setId(jsonObject.getString(SCConstants.TAG_ID));
                    lessonObj.setName(jsonObject.getString(SCConstants.TAG_NAME));
                    lessonObj.setRoom(jsonObject.getString(SCConstants.TAG_ROOM));
                    lessonObj.setTeacher(jsonObject.getString(SCConstants.TAG_TEACHER));
                    lessonObj.setPeriod(jsonObject.getString(SCConstants.TAG_PERIOD));

                    lessons.add(lessonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return lessons;
    }

    private static ArrayList<ECShopObject> parseFollowShops(Object src) {
        ArrayList<ECShopObject> shops = new ArrayList<ECShopObject>();
        JSONArray jsonArray = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ECShopObject shopObj = new ECShopObject();

                    shopObj.setId(jsonObject.getString(SCConstants.TAG_SHOP_ID));
                    shopObj.setName(jsonObject.getString(SCConstants.TAG_NAME));
                    shopObj.setImage(jsonObject.getString(SCConstants.TAG_IMAGE));
                    shopObj.setOfficialUrl(jsonObject.getString(SCConstants.TAG_OFFICIAL_URL));

                    shops.add(shopObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return shops;
    }

    private static ArrayList<ECProductObject> parseFavoriteItems(Object src) {
        ArrayList<ECProductObject> items = new ArrayList<ECProductObject>();
        JSONArray jsonArray = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject itemJObject = jsonArray.getJSONObject(i).getJSONObject(SCConstants.TAG_ITEM);
                    JSONObject shopJObject = jsonArray.getJSONObject(i).getJSONObject(SCConstants.TAG_SHOP);
                    ECProductObject productObj = new ECProductObject();

                    productObj.setId(itemJObject.getString(SCConstants.TAG_ID));
                    productObj.setName(itemJObject.getString(SCConstants.TAG_NAME));
                    productObj.setImage(itemJObject.getString(SCConstants.TAG_IMAGE));
                    productObj.setCategoryId(itemJObject.getString(SCConstants.TAG_CATEGORY_ID));
                    productObj.setPoint(itemJObject.getString(SCConstants.TAG_POINT));
                    productObj.setDescription(itemJObject.getString(SCConstants.TAG_DESCRIPTION));
                    productObj.setRank(itemJObject.getString(SCConstants.TAG_RANK));
                    productObj.setOfficialUrl(itemJObject.getString(SCConstants.TAG_OFFICIAL_URL));
                    productObj.setFavorite("true");

                    productObj.setShopId(shopJObject.getString(SCConstants.TAG_ID));
                    productObj.setShop(shopJObject.getString(SCConstants.TAG_NAME));

                    items.add(productObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return items;
    }

    private static ECProductObject parseExchangeLog(Object src) {
        ECProductObject product = new ECProductObject();
        JSONObject jObj = (JSONObject) src;
        try {
            JSONObject itemJObject = jObj.getJSONObject(SCConstants.TAG_ITEM);
            JSONObject shopJObject = jObj.getJSONObject(SCConstants.TAG_SHOP);

            product.setId(jObj.getString(SCConstants.TAG_ID));
            product.setCode(jObj.getString(SCConstants.TAG_CODE));
            product.setExchangeDate(jObj.getString(SCConstants.TAG_EXCHANGE_DATE));
            product.setSendDate(jObj.getString(SCConstants.TAG_SEND_DATE));
            product.setLimitDate(jObj.getString(SCConstants.TAG_LIMIT_DATE));

            product.setName(itemJObject.getString(SCConstants.TAG_NAME));
            product.setImage(itemJObject.getString(SCConstants.TAG_IMAGE));
            product.setOfficialUrl(itemJObject.getString(SCConstants.TAG_OFFICIAL_URL));

            product.setShopId(shopJObject.getString(SCConstants.TAG_ID));
            product.setShop(shopJObject.getString(SCConstants.TAG_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }

    private static ArrayList<ECProductObject> parseExchangeLogs(Object src) {
        ArrayList<ECProductObject> items = new ArrayList<ECProductObject>();
        JSONArray jsonArray = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject itemJObject = jsonArray.getJSONObject(i).getJSONObject(SCConstants.TAG_ITEM);
                    JSONObject shopJObject = jsonArray.getJSONObject(i).getJSONObject(SCConstants.TAG_SHOP);
                    ECProductObject productObj = new ECProductObject();

                    productObj.setId(jsonArray.getJSONObject(i).getString(SCConstants.TAG_ID));

                    productObj.setName(itemJObject.getString(SCConstants.TAG_NAME));
                    productObj.setImage(itemJObject.getString(SCConstants.TAG_IMAGE));
                    productObj.setPoint(itemJObject.getString(SCConstants.TAG_POINT));
                    productObj.setCategoryId(itemJObject.getString(SCConstants.TAG_CATEGORY_ID));
                    productObj.setRank(itemJObject.getString(SCConstants.TAG_RANK));
                    productObj.setOfficialUrl(itemJObject.getString(SCConstants.TAG_OFFICIAL_URL));

                    productObj.setShopId(shopJObject.getString(SCConstants.TAG_ID));
                    productObj.setShop(shopJObject.getString(SCConstants.TAG_NAME));

                    items.add(productObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return items;
    }

    private static ECShopObject parseShopDetail(Object src) {
        ECShopObject shop = new ECShopObject();
        JSONObject jObj = (JSONObject) src;
        try {
            shop.setId(jObj.getString(SCConstants.TAG_ID));
            shop.setName(jObj.getString(SCConstants.TAG_NAME));
            shop.setImage(jObj.getString(SCConstants.TAG_IMAGE));
            shop.setOfficialUrl(jObj.getString(SCConstants.TAG_OFFICIAL_URL));
            shop.setFollowed(jObj.getString(SCConstants.TAG_FOLLOWED));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return shop;
    }

    private static ECProductObject parseExchangeItemDetail(Object src) {
        ECProductObject productObj = new ECProductObject();
        JSONObject jObj = (JSONObject) src;
        try {
            productObj.setId(jObj.getString(SCConstants.TAG_ID));
            productObj.setName(jObj.getString(SCConstants.TAG_NAME));
            productObj.setImage(jObj.getString(SCConstants.TAG_IMAGE));
            productObj.setShop(jObj.getString(SCConstants.TAG_SHOP));
            productObj.setShopId(jObj.getString(SCConstants.TAG_SHOP_ID));
            productObj.setCategory(jObj.getString(SCConstants.TAG_CATEGORY));
            productObj.setPoint(jObj.getString(SCConstants.TAG_POINT));
            productObj.setDescription(jObj.getString(SCConstants.TAG_DESCRIPTION));
            productObj.setOfficialUrl(jObj.getString(SCConstants.TAG_OFFICIAL_URL));
            productObj.setFavorite(jObj.getString(SCConstants.TAG_FAVORITE));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return productObj;
    }


    private static ArrayList<CampusanAdObject> parseAdCampusan(Object src) {
        ArrayList<CampusanAdObject> ads = new ArrayList<CampusanAdObject>();
        JSONArray jsonArray = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    CampusanAdObject campusanAdObject = new CampusanAdObject();

                    campusanAdObject.setId(jsonObject.getString(SCConstants.TAG_ID));
                    campusanAdObject.setName(jsonObject.getString(SCConstants.TAG_NAME));
                    campusanAdObject.setDescription(jsonObject.getString(SCConstants.TAG_DESCRIPTION));
                    campusanAdObject.setHalf_width_image_url(jsonObject.getString("half_width_image_url"));
                    campusanAdObject.setCreated(jsonObject.getString("created"));
                    campusanAdObject.setCollection_url(jsonObject.getString("collection_url"));



                    ads.add(campusanAdObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return ads;
    }

    private static ArrayList<ECCategoryObject> parseCategories(Object src) {
        ArrayList<ECCategoryObject> categories = new ArrayList<ECCategoryObject>();
        JSONArray jsonArray = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ECCategoryObject categoryObj = new ECCategoryObject();

                    categoryObj.setId(jsonObject.getString(SCConstants.TAG_ID));
                    categoryObj.setName(jsonObject.getString(SCConstants.TAG_NAME));

                    categories.add(categoryObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return categories;
    }

    private static ArrayList<ECShopObject> parseShops(Object src) {
        ArrayList<ECShopObject> shops = new ArrayList<ECShopObject>();
        JSONArray jsonArray = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ECShopObject shopObj = new ECShopObject();

                    shopObj.setId(jsonObject.getString(SCConstants.TAG_ID));
                    shopObj.setName(jsonObject.getString(SCConstants.TAG_NAME));
                    shopObj.setImage(jsonObject.getString(SCConstants.TAG_IMAGE));
                    shopObj.setOfficialUrl(jsonObject.getString(SCConstants.TAG_OFFICIAL_URL));

                    shops.add(shopObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return shops;
    }

    private static ArrayList<ECProductObject> parseExchangeItems(Object src) {
        ArrayList<ECProductObject> items = new ArrayList<ECProductObject>();
        JSONArray jsonArray = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ECProductObject productObj = new ECProductObject();

                    productObj.setId(jsonObject.getString(SCConstants.TAG_ID));
                    productObj.setName(jsonObject.getString(SCConstants.TAG_NAME));
                    productObj.setImage(jsonObject.getString(SCConstants.TAG_IMAGE));
                    productObj.setShop(jsonObject.getString(SCConstants.TAG_SHOP));
                    productObj.setShopId(jsonObject.getString(SCConstants.TAG_SHOP_ID));
                    productObj.setCategory(jsonObject.getString(SCConstants.TAG_CATEGORY));
                    productObj.setPoint(jsonObject.getString(SCConstants.TAG_POINT));
                    productObj.setDescription(jsonObject.getString(SCConstants.TAG_DESCRIPTION));
                    productObj.setRank(jsonObject.getString(SCConstants.TAG_RANK));
                    productObj.setFavorite(jsonObject.getString(SCConstants.TAG_FAVORITE));
                    productObj.setOfficialUrl(jsonObject.getString(SCConstants.TAG_OFFICIAL_URL));

                    items.add(productObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return items;
    }

    private static ArrayList<SCAppObject> parseApplications(Object src) {
        ArrayList<SCAppObject> applications = new ArrayList<SCAppObject>();
        JSONArray jsonArray = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    SCAppObject appObject = new SCAppObject();
                    appObject.setId(jsonObject.getString(SCConstants.TAG_ID));
                    appObject.setUrl(jsonObject.getString(SCConstants.TAG_URL));
                    if (jsonObject.has(SCConstants.TAG_NAME))
                        appObject.setName(jsonObject.getString(SCConstants.TAG_NAME));
                    if (jsonObject.has(SCConstants.TAG_ICON))
                        appObject.setIcon(jsonObject.getString(SCConstants.TAG_ICON));
                    if (jsonObject.has(SCConstants.TAG_APP_ID))
                        appObject.setApp_id(jsonObject.getString(SCConstants.TAG_APP_ID));
                    if (jsonObject.has(SCConstants.TAG_IS_AUTO_LOGIN))
                        appObject.setIsAutiLogin(jsonObject.getString(SCConstants.TAG_IS_AUTO_LOGIN));
                    applications.add(appObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return applications;
    }

    private static SCAppObject parseApplication(Object src) {
        SCAppObject application = new SCAppObject();
        JSONObject jObj = (JSONObject) src;
        try {
            application.setId(jObj.getString(SCConstants.TAG_ID));
            application.setUrl(jObj.getString(SCConstants.TAG_URL));
            if (jObj.has(SCConstants.TAG_NAME))
                application.setName(jObj.getString(SCConstants.TAG_NAME));
            if (jObj.has(SCConstants.TAG_ICON))
                application.setIcon(jObj.getString(SCConstants.TAG_ICON));
            if (jObj.has(SCConstants.TAG_APP_ID))
                application.setApp_id(jObj.getString(SCConstants.TAG_APP_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return application;
    }

    private static SCTokenObject parseToken(Object src) {
        SCTokenObject token = new SCTokenObject();
        JSONObject jObj = (JSONObject) src;
        try {
            token.setAccess_token(jObj.getString("access_token"));
            token.setRefresh_Token(jObj.getString("refresh_token"));
            token.setlogin_type(jObj.getString("user_type"));
            token.setEmail(jObj.getString("email"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }


    private static ArrayList<SCInformationObject> parseInfomations(Object src) {
        ArrayList<SCInformationObject> informations = new ArrayList<SCInformationObject>();
        JSONArray jInformations = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jInformations.length(); i++) {
                try {
                    JSONObject jObj = jInformations.getJSONObject(i);
                    SCInformationObject infoObj = new SCInformationObject();
                    infoObj.setId(jObj.getString(SCConstants.TAG_ID));
                    infoObj.setTitle(jObj.getString(SCConstants.TAG_TITLE));
                    if(jObj.has(SCConstants.TAG_OPEN_TYPE))
                        infoObj.setOpenType(jObj.getString(SCConstants.TAG_OPEN_TYPE));
                    infoObj.setAppObj(parseApplication(jObj.getJSONObject(SCConstants.TAG_APPLICATION)));

                    informations.add(infoObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        return informations;
    }

    private static ArrayList<SCInformationObject> parseInfomationsForgroup(Object src) {
        ArrayList<SCInformationObject> informations = new ArrayList<SCInformationObject>();
        JSONArray jInformations = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jInformations.length(); i++) {
                try {
                    JSONObject jObj = jInformations.getJSONObject(i);
                    SCInformationObject infoObj = new SCInformationObject();
                    infoObj.setId(jObj.getString(SCConstants.TAG_ID));
                    infoObj.setTitle(jObj.getString(SCConstants.TAG_TITLE));
                    infoObj.setApplication_url(jObj.getString("application_url"));
                    infoObj.setIcon(jObj.getString("icon"));
                    informations.add(infoObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        return informations;
    }

    private static ArrayList<SCUniversityObject> parseUniversities(Object src) {
        ArrayList<SCUniversityObject> universities = new ArrayList<SCUniversityObject>();
        JSONArray jUniversities = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jUniversities.length(); i++) {
                try {
                    JSONObject jObj = jUniversities.getJSONObject(i);
                    SCUniversityObject univObj = new SCUniversityObject();

                    univObj.setId(jObj.getString(SCConstants.TAG_ID));
                    univObj.setName(jObj.getString(SCConstants.TAG_NAME));
                    univObj.setKana(jObj.getString(SCConstants.TAG_KANA));

                    universities.add(univObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        return universities;
    }

    private static ArrayList<SCDepartmentObject> parseDepartments(Object src) {
        ArrayList<SCDepartmentObject> departments = new ArrayList<SCDepartmentObject>();
        JSONArray jDepartments = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jDepartments.length(); i++) {
                try {
                    JSONObject jObj = jDepartments.getJSONObject(i);
                    SCDepartmentObject departmentObj = new SCDepartmentObject();

                    departmentObj.setId(jObj.getString(SCConstants.TAG_ID));
                    departmentObj.setName(jObj.getString(SCConstants.TAG_NAME));
                    departmentObj.setHaveMajor(jObj.getString(SCConstants.TAG_HAVE_MAJOR));

                    departments.add(departmentObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        return departments;
    }

    private static ArrayList<SCCampusObject> parseCampuses(Object src) {
        ArrayList<SCCampusObject> campuses = new ArrayList<SCCampusObject>();
        JSONArray jCampuses = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jCampuses.length(); i++) {
                try {
                    JSONObject jObj = jCampuses.getJSONObject(i);
                    SCCampusObject campusObj = new SCCampusObject();

                    campusObj.setId(jObj.getString(SCConstants.TAG_ID));
                    campusObj.setName(jObj.getString(SCConstants.TAG_NAME));

                    campuses.add(campusObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        return campuses;
    }

    private static ArrayList<SCMajorObject> parseMajors(Object src) {
        ArrayList<SCMajorObject> majors = new ArrayList<SCMajorObject>();
        JSONArray jMajors = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jMajors.length(); i++) {
                try {
                    JSONObject jObj = jMajors.getJSONObject(i);
                    SCMajorObject majorObj = new SCMajorObject();

                    majorObj.setId(jObj.getString(SCConstants.TAG_ID));
                    majorObj.setName(jObj.getString(SCConstants.TAG_NAME));

                    majors.add(majorObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        return majors;
    }

    private static ArrayList<SCPrefectureObject> parsePrefectures(Object src) {
        ArrayList<SCPrefectureObject> prefectures = new ArrayList<SCPrefectureObject>();
        JSONArray jPrefectures = (JSONArray) src;
        if (src instanceof JSONArray) {
            for (int i = 0; i < jPrefectures.length(); i++) {
                try {
                    JSONObject jObj = jPrefectures.getJSONObject(i);
                    SCPrefectureObject prefectureObj = new SCPrefectureObject();

                    prefectureObj.setId(jObj.getString(SCConstants.TAG_ID));
                    prefectureObj.setName(jObj.getString(SCConstants.TAG_NAME));

                    prefectures.add(prefectureObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        return prefectures;
    }

    private static Object parseAppId(Object src) {
        String appId = null;
        try {
            appId = ((JSONObject) src).getString(SCConstants.TAG_APP_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appId;
    }



    private static Object parseUser(Object src) {
        SCUserObject userObj = SCUserObject.getInstance();
        try {
            if (((JSONObject) src).has(SCConstants.TAG_APP_ID)) {
                if (!((JSONObject) src).getString(SCConstants.TAG_APP_ID).equals("null")) {
                    userObj.setAppId(((JSONObject) src).getString(SCConstants.TAG_APP_ID));
                } else {
                    userObj.setAppId("");
                }

            }

            if (((JSONObject) src).has(SCConstants.TAG_PREFECTURE_ID)) {
                if (!((JSONObject) src).getString(SCConstants.TAG_PREFECTURE_ID).equals("null")) {
                    userObj.setPrefectureId(((JSONObject) src).getString(SCConstants.TAG_PREFECTURE_ID));
                } else {
                    userObj.setPrefectureId("");
                }
            }

            if (((JSONObject) src).has(SCConstants.TAG_STUDENT_GROUP_NAME)) {
                if (!((JSONObject) src).getString(SCConstants.TAG_STUDENT_GROUP_NAME).equals("null")) {
                    userObj.setStudent_group_name(((JSONObject) src).getString(SCConstants.TAG_STUDENT_GROUP_NAME));
                } else {
                    userObj.setStudent_group_name("");
                }
            }

            if (((JSONObject) src).has(SCConstants.TAG_STUDENT_GROUP_ID)) {
                if (!((JSONObject) src).getString(SCConstants.TAG_STUDENT_GROUP_ID).equals("null")) {
                    userObj.setStudent_group_id(((JSONObject) src).getString(SCConstants.TAG_STUDENT_GROUP_ID));
                } else {
                    userObj.setStudent_group_id("");
                }
            }

            if (((JSONObject) src).has(SCConstants.TAG_STUDENT_GROUP_LEADER)) {
                if (!((JSONObject) src).getString(SCConstants.TAG_STUDENT_GROUP_LEADER).equals("null")) {
                    userObj.setStudent_group_leader(((JSONObject) src).getString(SCConstants.TAG_STUDENT_GROUP_LEADER));
                } else {
                    userObj.setStudent_group_leader("");
                }
            }

            if (((JSONObject) src).has(SCConstants.TAG_ICON)) {
                if (!((JSONObject) src).getString(SCConstants.TAG_ICON).equals("null")) {
                    userObj.setIcon(((JSONObject) src).getString(SCConstants.TAG_ICON));
                } else {
                    userObj.setIcon("");
                }
            }

            if (((JSONObject) src).has(SCConstants.TAG_CAMPUS_POINT)) {
                if (!((JSONObject) src).getString(SCConstants.TAG_CAMPUS_POINT).equals("null")) {
                    userObj.setCampusPoint(((JSONObject) src).getString(SCConstants.TAG_CAMPUS_POINT));
                } else {
                    userObj.setCampusPoint("");
                }
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_NICKNAME).equals("null")) {
                userObj.setNickname(((JSONObject) src).getString(SCConstants.TAG_NICKNAME));
            } else {
                userObj.setNickname("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_SEX).equals("null")) {
                userObj.setSex(((JSONObject) src).getString(SCConstants.TAG_SEX));
            } else {
                userObj.setSex("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_UNIV_ID).equals("null")) {
                userObj.setUniversityId(((JSONObject) src).getString(SCConstants.TAG_UNIV_ID));
            } else {
                userObj.setUniversityId("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_UNIV_NAME).equals("null")) {
                userObj.setUniversityName(((JSONObject) src).getString(SCConstants.TAG_UNIV_NAME));
            } else {
                userObj.setUniversityName("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_CAMPUS_ID).equals("null")) {
                userObj.setCampusId(((JSONObject) src).getString(SCConstants.TAG_CAMPUS_ID));
            } else {
                userObj.setCampusId("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_CAMPUS_NAME).equals("null")) {
                userObj.setCampusName(((JSONObject) src).getString(SCConstants.TAG_CAMPUS_NAME));
            } else {
                userObj.setCampusName("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_DEPARTMENT_ID).equals("null")) {
                userObj.setDepartmentId(((JSONObject) src).getString(SCConstants.TAG_DEPARTMENT_ID));
            } else {
                userObj.setDepartmentId("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_DEPARTMENT_NAME).equals("null")) {
                userObj.setDepartmentName(((JSONObject) src).getString(SCConstants.TAG_DEPARTMENT_NAME));
            } else {
                userObj.setDepartmentName("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_MAJOR_ID).equals("null")) {
                userObj.setMajorId(((JSONObject) src).getString(SCConstants.TAG_MAJOR_ID));
            } else {
                userObj.setMajorId("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_MAJOR_NAME).equals("null")) {
                userObj.setMajorName(((JSONObject) src).getString(SCConstants.TAG_MAJOR_NAME));
            } else {
                userObj.setMajorName("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_ENROLLMENT_YEAR).equals("null")) {
                userObj.setEnrollmentYear(((JSONObject) src).getString(SCConstants.TAG_ENROLLMENT_YEAR));
            } else {
                userObj.setEnrollmentYear("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_SAVING_MONEY).equals("null")) {
                userObj.setSavingMoney(((JSONObject) src).getString(SCConstants.TAG_SAVING_MONEY));
            } else {
                userObj.setSavingMoney("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_EMAIL).equals("null")) {
                userObj.setEmail(((JSONObject) src).getString(SCConstants.TAG_EMAIL));
            } else {
                userObj.setEmail("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_BIRTHDAY).equals("null")) {
                userObj.setBirthday(((JSONObject) src).getString(SCConstants.TAG_BIRTHDAY));
            } else {
                userObj.setBirthday("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_POST_CODE).equals("null")) {
                userObj.setPostCode(((JSONObject) src).getString(SCConstants.TAG_POST_CODE));
            } else {
                userObj.setPostCode("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_PREFECTURE).equals("null")) {
                userObj.setPrefecture(((JSONObject) src).getString(SCConstants.TAG_PREFECTURE));
            } else {
                userObj.setPrefecture("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_ADDRESS).equals("null")) {
                userObj.setAddress(((JSONObject) src).getString(SCConstants.TAG_ADDRESS));
            } else {
                userObj.setAddress("");
            }

            if (!((JSONObject) src).getString(SCConstants.TAG_PHONE_NUMBER).equals("null")) {
                userObj.setPhoneNumber(((JSONObject) src).getString(SCConstants.TAG_PHONE_NUMBER));
            } else {
                userObj.setPhoneNumber("");
            }

            if (((JSONObject) src).has(SCConstants.TAG_TC_POINT)) {
                if (!((JSONObject) src).getString(SCConstants.TAG_TC_POINT).equals("null")) {
                    userObj.setTcPoint(((JSONObject) src).getString(SCConstants.TAG_TC_POINT));
                } else {
                    userObj.setTcPoint("");
                }

            }

            if (((JSONObject) src).has(SCConstants.TAG_AGENT)) {
                if (!((JSONObject) src).getString(SCConstants.TAG_AGENT).equals("null")) {
                    userObj.setAgent(((JSONObject) src).getString(SCConstants.TAG_AGENT));
                } else {
                    userObj.setAgent("");
                }

            }

            if (((JSONObject) src).has(SCConstants.TAG_IS_COUPON)) {
                if (((JSONObject) src).getBoolean(SCConstants.TAG_IS_COUPON)) {
                    SCGlobalUtils.coupon_item = "true";
                } else {
                    SCGlobalUtils.coupon_item = null;

                }

            }
            ArrayList<LessonObject> mUserLesson = new ArrayList<LessonObject>();
            if (((JSONObject) src).has(SCConstants.TAG_LESSONS)) {
                    JSONArray jArrayLessons = ((JSONObject) src).getJSONArray(SCConstants.TAG_LESSONS);
                    for(int i = 0; i < jArrayLessons.length(); i++) {
                        LessonObject lessonObj = parseLessonObj(jArrayLessons.get(i));
                        mUserLesson.add(lessonObj);
                    }
                userObj.setmUserLesson(mUserLesson);

            }


            if (((JSONObject) src).has(SCConstants.TAG_LOGIN_COUNT)) {
                if (!((JSONObject) src).getString(SCConstants.TAG_LOGIN_COUNT).equals("null")) {
                    userObj.setLoginCount(((JSONObject) src).getString(SCConstants.TAG_LOGIN_COUNT));
                } else {
                    userObj.setLoginCount("");
                }

            }

            //location for tadacopy machines
            ArrayList<SCLocationObject> mTCLocations = new ArrayList<SCLocationObject>();
            if (((JSONObject) src).has(SCConstants.TAG_LOCATIONS)) {
                JSONArray jArrayLocations = ((JSONObject) src).getJSONArray(SCConstants.TAG_LOCATIONS);
                for(int i = 0; i < jArrayLocations.length(); i++) {
                    SCLocationObject locationObject = parseLocationObj(jArrayLocations.get(i));
                    mTCLocations.add(locationObject);
                }
                userObj.setmUserLocation(mTCLocations);
                if(mTCLocations.size() != 0){
                    SCGlobalUtils.tc_device_available_in_campus = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userObj;
    }

    private static SCLocationObject parseLocationObj(Object src) {
        SCLocationObject location = null;
        try {
            JSONObject jObj = (JSONObject) src;
            location = new SCLocationObject();
            if (!((JSONObject) src).getString("id").equals("null")) {
                location.setId(jObj.getString("id"));
            }
            if (!((JSONObject) src).getString("campus_id").equals("null")) {
                location.setCampus_id(jObj.getString("campus_id"));
            }
            if (!((JSONObject) src).getString("location").equals("null")) {
                location.setLocation(jObj.getString("location"));
            }
            if (!((JSONObject) src).getString("created").equals("null")) {
                location.setCreated(jObj.getString("created"));
            }
            if (!((JSONObject) src).getString("updated").equals("null")) {
                location.setUpdated(jObj.getString("updated"));
            }
            if (!((JSONObject) src).getString("disable").equals("null")) {

                location.setDisable(jObj.getString("disable"));
            }

        } catch (Exception e) {

        }

        return location;
    }


    public static LessonObject parseLessonObj(Object src) {
        LessonObject lessonObj = null;
        try {
            JSONObject jsonObj = (JSONObject) src;
            lessonObj = new LessonObject();
            lessonObj.setmId(jsonObj.getString(SCConstants.TAG_ID));
            lessonObj.setmName(jsonObj.getString(SCConstants.TAG_NAME));
            lessonObj.setmSemester(jsonObj.getString(SCConstants.TAG_SEMESTER));
            lessonObj.setmIsAttending(jsonObj.getString(SCConstants.TAG_IS_ATTENDING));
            lessonObj.setmAttendeeCount(jsonObj.getString(SCConstants.TAG_ATTENDEE_COUNT));
            lessonObj.setmCategory(jsonObj.getString(SCConstants.TAG_CATEGORY));
            lessonObj.setmState(jsonObj.getString(SCConstants.TAG_STATE));

            // get list teachers
            ArrayList<String> listTeacher = new ArrayList<String>();
            try {
                JSONArray jArrayTeacher = jsonObj.getJSONArray(SCConstants.TAG_TEACHERS);
                for(int j = 0; j < jArrayTeacher.length(); j++) {
                    String teacher = jArrayTeacher.getString(j);
                    listTeacher.add(teacher);
                }
            } catch (Exception e) {
            }

            lessonObj.setmTeachers(listTeacher);

            // get list rooms
            ArrayList<String> listRoom = new ArrayList<String>();
            try {
                JSONArray jArrayRoom = jsonObj.getJSONArray(SCConstants.TAG_ROOMS);
                for(int j = 0; j < jArrayRoom.length(); j++) {
                    String room = jArrayRoom.getString(j);
                    listRoom.add(room);
                }
            } catch (Exception e) {
            }

            lessonObj.setmRooms(listRoom);

            // get schedule list
            ArrayList<ScheduleObject> listSchedule = new ArrayList<ScheduleObject>();
            try {
                JSONArray jsonSchedArr = jsonObj.getJSONArray(SCConstants.TAG_LESSON_SCHEDULES);
                for(int j = 0; j < jsonSchedArr.length(); j++) {
                    ScheduleObject scheduleObj = parseScheduleObj(jsonSchedArr.get(j));
                    // check null before add into list
                    if(scheduleObj != null) {
                        listSchedule.add(scheduleObj);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            lessonObj.setmSchedules(listSchedule);

        } catch (Exception e) {

        }

        return lessonObj;
    }
    public static ScheduleObject parseScheduleObj(Object src) {
        ScheduleObject scheduleObj = null;
        try {
            JSONObject jsonObj = (JSONObject) src;
            scheduleObj = new ScheduleObject();
            scheduleObj.setmDayOfWeek(jsonObj.getString(SCConstants.TAG_DAY_OF_WEEK));
            scheduleObj.setmPeroid(jsonObj.getString(SCConstants.TAG_PERIOD));
        } catch (Exception e) {

        }

        return scheduleObj;
    }

    private static Object parseSNSUser(Object src) {
        SCUserObject userObj = SCUserObject.getInstance();
        try {
            if (((JSONObject) src).has(SCConstants.TAG_APP_ID)) {
                userObj.setAppId(((JSONObject) src).getString(SCConstants.TAG_APP_ID));
            }
            userObj.setNickname(((JSONObject) src).getString(SCConstants.TAG_NICKNAME));
            userObj.setIcon(((JSONObject) src).getString(SCConstants.TAG_ICON));
            userObj.setEmail(((JSONObject) src).getString(SCConstants.TAG_EMAIL));
            userObj.setCampusPoint(((JSONObject) src).getString(SCConstants.TAG_CAMPUS_POINT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userObj;
    }

    private static Object parseUserType(Object src) {
        SCTypeObject typeObject = new SCTypeObject();
        JSONObject jObj = (JSONObject) src;
        try {
            if (!((JSONObject) src).getString("email").equals("null")) {
                typeObject.setEmail(jObj.getString("email"));
            }
            if (!((JSONObject) src).getString("facebook_id").equals("null")) {
                typeObject.setFacebook_id(jObj.getString("facebook_id"));
            }
            if (!((JSONObject) src).getString("facebook_token").equals("null")) {
                typeObject.setFacebook_token(jObj.getString("facebook_token"));
            }
            if (!((JSONObject) src).getString("twitter_id").equals("null")) {
                typeObject.setTwitter_id(jObj.getString("twitter_id"));
            }
            if (!((JSONObject) src).getString("twitter_token").equals("null")) {
                typeObject.setTwitter_token(jObj.getString("twitter_token"));
            }
            if (!((JSONObject) src).getString("twitter_token_secret").equals("null")) {

                typeObject.setTwitter_token_secret(jObj.getString("twitter_token_secret"));
            }






        } catch (Exception e) {
            e.printStackTrace();
        }

        return typeObject;
    }

}
