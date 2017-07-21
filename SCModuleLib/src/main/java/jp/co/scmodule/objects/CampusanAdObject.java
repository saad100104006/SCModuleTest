package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WebHawks IT on 11/8/2016.
 */

public class CampusanAdObject implements Parcelable {

    private String id = null;
    private String name = null;
    private String description = null;
    private String half_width_image_url = null;
    private String collection_url = null;
    private String created = null;

    public CampusanAdObject() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHalf_width_image_url() {
        return half_width_image_url;
    }

    public void setHalf_width_image_url(String half_width_image_url) {
        this.half_width_image_url = half_width_image_url;
    }

    public String getCollection_url() {
        return collection_url;
    }

    public void setCollection_url(String collection_url) {
        this.collection_url = collection_url;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    protected CampusanAdObject(Parcel in) {
        this();
        readFromParcel(in);
    }

    public static final Creator<CampusanAdObject> CREATOR = new Creator<CampusanAdObject>() {
        @Override
        public CampusanAdObject createFromParcel(Parcel in) {
            return new CampusanAdObject(in);
        }

        @Override
        public CampusanAdObject[] newArray(int size) {
            return new CampusanAdObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(half_width_image_url);
        dest.writeString(collection_url);
        dest.writeString(created);

    }

    public void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.half_width_image_url = in.readString();
        this.collection_url = in.readString();
        this.created = in.readString();


    }

}
