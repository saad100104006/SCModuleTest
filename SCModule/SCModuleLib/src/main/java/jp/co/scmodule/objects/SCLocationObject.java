package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WebHawks IT on 9/20/2016.
 */
public class SCLocationObject implements Parcelable {
    private String id = null;
    private String campus_id = null;
    private String location = null;
    private String created = null;
    private String updated = null;
    private String disable = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCampus_id() {
        return campus_id;
    }

    public void setCampus_id(String campus_id) {
        this.campus_id = campus_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable;
    }

    public SCLocationObject() {
        super();
    }

    private SCLocationObject(Parcel in) {
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
        dest.writeString(campus_id);
        dest.writeString(location);
        dest.writeString(created);
        dest.writeString(updated);
        dest.writeString(disable);
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.campus_id = in.readString();
        this.location = in.readString();
        this.created = in.readString();
        this.updated = in.readString();
        this.disable = in.readString();
    }

    // default creator for parcelable
    public static final Creator<SCLocationObject> CREATOR = new Creator<SCLocationObject>() {

        @Override
        public SCLocationObject[] newArray(int size) {
            return new SCLocationObject[size];
        }

        @Override
        public SCLocationObject createFromParcel(Parcel source) {
            return new SCLocationObject(source);
        }
    };
}

