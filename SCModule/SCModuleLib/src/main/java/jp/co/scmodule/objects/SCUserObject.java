package jp.co.scmodule.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import jp.co.scmodule.R;

public class SCUserObject implements Parcelable {
    private static SCUserObject mInstance = null;
    private static Bitmap mBmIcon = null;
    public static DisplayImageOptions sOptForUserIcon = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.color.common_gray).showImageForEmptyUri(R.drawable.default_avatar)
            .showImageOnFail(R.drawable.default_avatar).bitmapConfig(Bitmap.Config.RGB_565)
            .cacheInMemory(false).cacheOnDisk(false).considerExifParams(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .build();

    private String appId = null; // string
    private String nickname = null; // string
    private String sex = null; // string
    private String universityId = null; // int
    private String universityName = null; // string
    private String campusId = null; // int
    private String departmentId = null; // int
    private String departmentName = null; // string
    private String majorId = null; // int
    private String majorName = null; // string
    private String enrollmentYear = null; // int
    private String savingMoney = null; // string
    private String email = null; // string
    private String birthday = null; // string
    private String postCode = null; // string
    private String prefectureId = null; // int
    private String prefecture = null; // string
    private String address = null; // string
    private String phoneNumber = null; // string
    private String isGuest = null; // string
    private String icon = null; // string
    private String campusPoint = null; // int
    private String isNewUser = null; // string
    private String campusName = null; // string
    private String loginCount = null; // string

    public String getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(String loginCount) {
        this.loginCount = loginCount;
    }

    private ArrayList<LessonObject> mUserLesson = new ArrayList<LessonObject>();

    private ArrayList<SCLocationObject> mUserLocation = new ArrayList<SCLocationObject>();

    public ArrayList<SCLocationObject> getmUserLocation() {
        return mUserLocation;
    }

    public void setmUserLocation(ArrayList<SCLocationObject> mUserLocation) {
        this.mUserLocation = mUserLocation;
    }

    public ArrayList<LessonObject> getmUserLesson() {
        return mUserLesson;
    }

    public void setmUserLesson(ArrayList<LessonObject> mUserLesson) {
        this.mUserLesson = mUserLesson;
    }

    public String getStudent_group_id() {
        return student_group_id;
    }

    public void setStudent_group_id(String student_group_id) {
        this.student_group_id = student_group_id;
    }

    public String getStudent_group_leader() {
        return student_group_leader;
    }

    public void setStudent_group_leader(String student_group_leader) {
        this.student_group_leader = student_group_leader;
    }

    private String student_group_id = null;
    private String student_group_leader = null;
    private String student_group_name = null;

    private String tcPoint = null;
    private String agent = null;

    public String getStudent_group_name() {
        return student_group_name;
    }

    public void setStudent_group_name(String student_group_name) {
        this.student_group_name = student_group_name;
    }

    public static SCUserObject getInstance() {
        if (mInstance == null) {

            mInstance = new SCUserObject();
        }
        return mInstance;
    }

    public static void resetInstant() {
        mInstance = null;
        mBmIcon = null;
    }

    public String getTcPoint() {
        return tcPoint;
    }

    public void setTcPoint(String tcPoint) {
        this.tcPoint = tcPoint;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(String isNewUser) {
        this.isNewUser = isNewUser;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCampusPoint() {
        return campusPoint;
    }

    public void setCampusPoint(String campusPoint) {
        this.campusPoint = campusPoint;
    }

    public String getIsGuest() {
        return isGuest;
    }

    public void setIsGuest(String isGuest) {
        this.isGuest = isGuest;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getSavingMoney() {
        return savingMoney;
    }

    public void setSavingMoney(String savingMoney) {
        this.savingMoney = savingMoney;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPrefectureId() {
        return prefectureId;
    }

    public void setPrefectureId(String prefectureId) {
        this.prefectureId = prefectureId;
    }

    public String getPrefecture() {
        return prefecture;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public String getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(String enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public SCUserObject() {
        super();
    }

    private SCUserObject(Parcel in) {
        this();
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(appId);
        dest.writeString(nickname);
        dest.writeString(sex);
        dest.writeString(universityId);
        dest.writeString(universityName);
        dest.writeString(campusId);
        dest.writeString(departmentId);
        dest.writeString(departmentName);
        dest.writeString(majorId);
        dest.writeString(majorName);
        dest.writeString(enrollmentYear);
        dest.writeString(savingMoney);
        dest.writeString(email);
        dest.writeString(birthday);
        dest.writeString(postCode);
        dest.writeString(prefectureId);
        dest.writeString(prefecture);
        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeString(isGuest);
        dest.writeString(icon);
        dest.writeString(campusPoint);
        dest.writeString(isNewUser);
        dest.writeString(campusName);
        dest.writeString(tcPoint);
        dest.writeString(agent);
        dest.writeString(student_group_id);
        dest.writeString(student_group_leader);
        dest.writeString(student_group_name);
        dest.writeTypedList(mUserLesson);
        dest.writeTypedList(mUserLocation);
    }

    public void readFromParcel(Parcel in) {
        this.appId = in.readString();
        this.nickname = in.readString();
        this.sex = in.readString();
        this.universityId = in.readString();
        this.universityName = in.readString();
        this.campusId = in.readString();
        this.departmentId = in.readString();
        this.departmentName = in.readString();
        this.majorId = in.readString();
        this.majorName = in.readString();
        this.enrollmentYear = in.readString();
        this.savingMoney = in.readString();
        this.email = in.readString();
        this.birthday = in.readString();
        this.postCode = in.readString();
        this.prefectureId = in.readString();
        this.prefecture = in.readString();
        this.address = in.readString();
        this.phoneNumber = in.readString();
        this.isGuest = in.readString();
        this.icon = in.readString();
        this.campusPoint = in.readString();
        this.isNewUser = in.readString();
        this.campusName = in.readString();
        this.tcPoint = in.readString();
        this.agent = in.readString();
        this.student_group_id = in.readString();
        this.student_group_leader = in.readString();
        this.student_group_name = in.readString();
        in.readTypedList(mUserLesson, LessonObject.CREATOR);
        in.readTypedList(mUserLocation, SCLocationObject.CREATOR);
    }

    public static void updateInstance(SCUserObject userObj) {
        mInstance.appId = userObj.getAppId();
        mInstance.nickname = userObj.getNickname();
        mInstance.sex = userObj.getSex();
        mInstance.universityId = userObj.getUniversityId();
        mInstance.universityName = userObj.getUniversityName();
        mInstance.campusId = userObj.getCampusId();
        mInstance.departmentId = userObj.getDepartmentId();
        mInstance.departmentName = userObj.getDepartmentName();
        mInstance.majorId = userObj.getMajorId();
        mInstance.majorName = userObj.getMajorName();
        mInstance.enrollmentYear = userObj.getEnrollmentYear();
        mInstance.savingMoney = userObj.getSavingMoney();
        mInstance.email = userObj.getEmail();
        mInstance.birthday = userObj.getBirthday();
        mInstance.postCode = userObj.getPostCode();
        mInstance.prefectureId = userObj.getPrefectureId();
        mInstance.prefecture = userObj.getPrefecture();
        mInstance.address = userObj.getAddress();
        mInstance.phoneNumber = userObj.getPhoneNumber();
        mInstance.isGuest = userObj.getIsGuest();
        mInstance.icon = userObj.getIcon();
        mInstance.campusPoint = userObj.getCampusPoint();
        mInstance.isNewUser = userObj.getIsNewUser();
        mInstance.campusName = userObj.getCampusName();
        mInstance.tcPoint = userObj.getTcPoint();
        mInstance.agent = userObj.getAgent();
        mInstance.student_group_id = userObj.getStudent_group_id();
        mInstance.student_group_leader = userObj.getStudent_group_leader();
        mInstance.student_group_name = userObj.getStudent_group_name();
        mInstance.mUserLesson = userObj.getmUserLesson();
        mInstance.mUserLocation = userObj.getmUserLocation();
    }

    // default creator for parcelable
    public static final Creator<SCUserObject> CREATOR = new Creator<SCUserObject>() {

        @Override
        public SCUserObject[] newArray(int size) {
            return new SCUserObject[size];
        }

        @Override
        public SCUserObject createFromParcel(Parcel source) {
            return new SCUserObject(source);
        }
    };

    public void setIconInstance(Bitmap bitmap) {
        mBmIcon = bitmap;
    }

    public Bitmap getIconInstance() {
        if(mBmIcon == null) {
            ImageLoader.getInstance().loadImage(this.icon, sOptForUserIcon, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    mBmIcon = bitmap;
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }

        return  mBmIcon;
    }
}
