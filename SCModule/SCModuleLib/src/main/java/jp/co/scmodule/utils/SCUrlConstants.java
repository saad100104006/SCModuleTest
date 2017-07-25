package jp.co.scmodule.utils;

/**
 * Created by Munir on 6/1/2016.
 */
public class SCUrlConstants {
    //public static final String URL_DOMAIN = "http://oceanize.accjpn.com/scmodule/";
//    //public static final String URL_DOMAIN = "http://api.smart-campus.jp";

    //Developement Server after implementing protocol
    public static final String URL_DOMAIN = "https://devapi2.smart-campus.jp";
    public static final String URL_BASE_LOGIN = "https://devapi2.smart-campus.jp/api/";

//  public static final String URL_GET_BANNERS_CAMPUSAN = "http://devcanapi.smart-campus.jp/sc/collections/pickup_banner";

    //Live Server after implementing protocol
//     public static final String URL_DOMAIN = "https://api2.smart-campus.jp";
//     public static final String URL_BASE_LOGIN = "https://api2.smart-campus.jp/api/";


    //Staging Server after implementing protocol
//      public static final String URL_DOMAIN = "https://stgapi.smart-campus.jp";
//     public static final String URL_BASE_LOGIN = "https://stgapi.smart-campus.jp/api/";
    public static final String URL_GET_BANNERS_CAMPUSAN = "https://canapi.smart-campus.jp/sc/collections/pickup_banner";



    public static final String URL_LOGIN_FACEBOOK = URL_DOMAIN + "/api_user/login_facebook?%1$s=%2$s&%3$s=%4$s&%5$s=%6$s";
    public static final String URL_LOGIN_TWITTER = URL_DOMAIN + "/api/user/login_twitter?%1$s=%2$s&%3$s=%4$s&%5$s=%6$s&%7$s=%8$s";
    public static final String URL_LOGIN_MAIL = URL_DOMAIN + "/api/user/login_mail";
    public static final String URL_REGISTER_MAIL = URL_DOMAIN + "/api_user/register_mail";
    public static final String URL_GET_MAJOR = URL_DOMAIN + "/api_user/get_major";
    public static final String URL_GET_UNIVERSITY = URL_DOMAIN + "/api_user/get_univ";
    public static final String URL_GET_CAMPUS = URL_DOMAIN + "/api_user/get_campus";
    public static final String URL_GET_DEPARTMENT = URL_DOMAIN + "/api_user/get_department";
    public static final String URL_GET_PREFECTURE = URL_DOMAIN + "/api_user/get_prefecture";
    public static final String URL_REGISTER_USER = URL_DOMAIN + "/api_user/register_user";
    public static final String URL_GET_APPLICATION = URL_DOMAIN + "/api_contents/get_applications";
    public static final String URL_UPDATE_USER = URL_DOMAIN + "/api_user/update_user";
    public static final String URL_GET_USER = URL_DOMAIN + "/api_user/get_user";
    public final static String URL_ICON_UPDATE = URL_DOMAIN + "/api_user/icon_upload";
    public static final String URL_GET_INFORMATIONS = URL_DOMAIN + "/api_contents/get_informations";
    public final static String URL_UPDATE_MAIL = URL_DOMAIN + "/api_user/update_mail";
    public final static String URL_GET_EXCHANGE_ITEMS = URL_DOMAIN + "/api_ec/get_exchange_items";
    public final static String URL_GET_EXCHANGE_ITEM_DETAIL = URL_DOMAIN + "/api_ec/get_exchange_item_detail";
    public final static String URL_GET_SHOPS = URL_DOMAIN + "/api_ec/get_shops";
    public final static String URL_GET_CATEGORIES = URL_DOMAIN + "/api_ec/get_categories";
    public final static String URL_GET_SHOP_DETAIL = URL_DOMAIN + "/api_ec/get_shop_detail";
    public final static String URL_FOLLOW_SHOP = URL_DOMAIN + "/api_ec/follow_shop";
    public final static String URL_ADD_FAVORITE_ITEM = URL_DOMAIN + "/api_ec/add_favorite_item";
    public final static String URL_GET_POINT_LOGS = URL_DOMAIN + "/api_ec/get_point_logs";
    public final static String URL_GET_FOLLOW_SHOPS = URL_DOMAIN + "/api_ec/get_follow_shops";
    public final static String URL_GET_FAVORITE_ITEMS = URL_DOMAIN + "/api_ec/get_favorite_items";
    public final static String URL_GET_EXCHANGE_LOGS = URL_DOMAIN + "/api_ec/get_exchange_logs";
    public final static String URL_PAY_EXCHANGE_ITEM = URL_DOMAIN + "/api_ec/pay_exchange_item";
    public final static String URL_GET_LESSONS = URL_DOMAIN + "/api_user/get_lessons";
    public final static String URL_REGISTER_DEVICE = URL_DOMAIN + "/api_user/register_device";
    public final static String URL_GET_USER_BY_DEVICE_ID = URL_DOMAIN + "/api_user/get_user_by_device";
    public final static String URL_CHECK_POINT = URL_DOMAIN + "/api_point/check_point";
    public final static String URL_GET_EXCHANGE_LOG_DETAIL = URL_DOMAIN + "/api_ec/get_exchange_log_detail";
    public final static String URL_GET_RECOMMEND_ITEM_DETAILS = URL_DOMAIN + "/api_ec/get_recommend_item";

    public final static String URL_INSTALL_TORETAN = "http://app.tadacopy.jp/app_users/install_toretan";
    public static final String URL_GET_ACCESSTOKEN = URL_DOMAIN + "/api/oauth_tokens";
    public static final String URL_GET_ACCESSTOKEN_USING_REFRESHTOKEN = URL_DOMAIN + "/api/renew_auth_tokens";
    public static final String URL_GET_USER_BY_ACCESS_TOKEN = URL_DOMAIN + "/api/user";
    public static final String URL_API_LOGOUT = URL_DOMAIN + "/api/logout";
    public static final String URL_PASSWORD_REMINDER = URL_DOMAIN + "/api/forget_password";
    public static final String URL_CHECK_ACCESS_TOKEN_VALIDATION = URL_DOMAIN + "/api/validate_access_token";
    public static final String URL_CHECK_SURVEY_STATUS = URL_DOMAIN + "/api_contents/get_campus_work_status";

    public static final String URL_GET_GROUP_LIST = URL_DOMAIN + "/api_studentgroup/list";

    public static final String URL_REGISTER_GROUP = URL_DOMAIN + "/api_studentgroup/register_student_group";


    public static final String URL_JOIN_GROUP = URL_DOMAIN + "/api_studentgroup/join_student_group";
    public static final String URL_GET_GROUP_DETAILS = URL_DOMAIN + "/api_studentgroup/detail/";
    public static final String URL_CHECK_VERSION_UPDATE = URL_DOMAIN + "/api/application_version";
    public static final String URL_GET_POINT_HISTORY = URL_DOMAIN + "/api_ec/get_point_history";

    public static final String URL_FOR_MAKE_FAV = URL_DOMAIN + "/api_contents/add_favorite_magazine";
    public static final String URL_FOR_GET_LOGIN_POINT = URL_DOMAIN + "/api_point/get_login_point";


    public static final String URL_AUTO_LOGIN = URL_DOMAIN + "/api/auto_login";

    public static final String URL_FOR_GET_MAGAZINES = URL_DOMAIN + "/api_contents/get_magazines";
    public static final String URL_FOR_GET_CAMPUS_WORK = URL_DOMAIN + "/api_contents/get_campus_works";
    public static final String URL_GET_BEACONS = URL_DOMAIN + "/api_contents/get_beacons";
    public static final String URL_GET_BEACONS_OLD = URL_DOMAIN + "/api_point/checkin_beacon_old";
}
