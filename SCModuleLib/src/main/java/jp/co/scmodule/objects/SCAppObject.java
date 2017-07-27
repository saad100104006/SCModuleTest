package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VNCCO on 6/30/2015.
 */
public class SCAppObject implements Parcelable {
    private String id = "";
    private String url = "";
    private String icon = "";
    private String name = "";
    private String app_id = "";
    private String isAutiLogin = null;

    public String getIsAutiLogin() {
        return isAutiLogin;
    }

    public void setIsAutiLogin(String isAutiLogin) {
        this.isAutiLogin = isAutiLogin;
    }
    public SCAppObject() {
        super();
    }

    private SCAppObject(Parcel in) {
        this();
        readFromParcel(in);
    }



    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(id);
        dest.writeString(url);
        dest.writeString(icon);
        dest.writeString(name);
        dest.writeString(app_id);
        dest.writeString(isAutiLogin);
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.url = in.readString();
        this.icon = in.readString();
        this.name = in.readString();
        this.app_id = in.readString();
        this.isAutiLogin = in.readString();

    }

    // default creator for parcelable
    public static final Creator<SCAppObject> CREATOR = new Creator<SCAppObject>() {

        @Override
        public SCAppObject[] newArray(int size) {
            return new SCAppObject[size];
        }

        @Override
        public SCAppObject createFromParcel(Parcel source) {
            return new SCAppObject(source);
        }
    };
}
