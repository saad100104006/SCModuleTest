package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class SCInformationObject implements Parcelable {
    private String id = null;
    private String title = null;
    private String openType = null;
    private String icon = null;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getApplication_url() {
        return application_url;
    }

    public void setApplication_url(String application_url) {
        this.application_url = application_url;
    }

    private String application_url = null;
    private SCAppObject appObj = null;

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public SCAppObject getAppObj() {
        return appObj;
    }

    public void setAppObj(SCAppObject appObj) {
        this.appObj = appObj;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SCInformationObject() {
        super();
    }

    private SCInformationObject(Parcel in) {
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
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(openType);
        dest.writeParcelable(appObj, flags);
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.openType = in.readString();
        this.appObj = in.readParcelable(SCAppObject.class.getClassLoader());
    }

    // default creator for parcelable
    public static final Creator<SCInformationObject> CREATOR = new Creator<SCInformationObject>() {

        @Override
        public SCInformationObject[] newArray(int size) {
            return new SCInformationObject[size];
        }

        @Override
        public SCInformationObject createFromParcel(Parcel source) {
            return new SCInformationObject(source);
        }
    };
}
