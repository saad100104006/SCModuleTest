package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WebHawks IT on 6/9/2016.
 */
public class SCMemberObject implements Parcelable {
    private String nickname = null;
    private String icon = null;
    private String email = null;
    private String app_user_id = null;
    private String tel_mobile = null;
    private String position = null;
    private String created = null;
    private String updated = null;
    private String disable = null;
    private String memo = null;

    public String getCampus_point() {
        return campus_point;
    }

    public void setCampus_point(String campus_point) {
        this.campus_point = campus_point;
    }

    private String campus_point = null;
    private String student_group_id = null;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStudent_group_id() {
        return student_group_id;
    }

    public void setStudent_group_id(String student_group_id) {
        this.student_group_id = student_group_id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTel_mobile() {
        return tel_mobile;
    }

    public void setTel_mobile(String tel_mobile) {
        this.tel_mobile = tel_mobile;
    }

    public String getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(String app_user_id) {
        this.app_user_id = app_user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    public SCMemberObject() {
        super();
    }
    protected SCMemberObject(Parcel in) {
        nickname = in.readString();
        icon = in.readString();
        email = in.readString();
        app_user_id = in.readString();
        tel_mobile = in.readString();
        position = in.readString();
        created = in.readString();
        updated = in.readString();
        disable = in.readString();
        memo = in.readString();
        student_group_id = in.readString();
    }

    public static final Creator<SCMemberObject> CREATOR = new Creator<SCMemberObject>() {
        @Override
        public SCMemberObject createFromParcel(Parcel in) {
            return new SCMemberObject(in);
        }

        @Override
        public SCMemberObject[] newArray(int size) {
            return new SCMemberObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        dest.writeString(icon);
        dest.writeString(email);
        dest.writeString(app_user_id);
        dest.writeString(tel_mobile);
        dest.writeString(position);
        dest.writeString(created);
        dest.writeString(updated);
        dest.writeString(disable);
        dest.writeString(memo);
        dest.writeString(student_group_id);
    }
}
