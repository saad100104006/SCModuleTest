package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VNCCO on 8/12/2015.
 */
public class ECShopObject implements Parcelable {
    private String id = "";
    private String name = "";
    private String image = "";
    private String officialUrl = "";
    private String followed = "";

    public ECShopObject() {
    }

    private ECShopObject(Parcel in) {
        readFromParcel(in);
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }

    public String getFollowed() {
        return followed;
    }

    public void setFollowed(String followed) {
        this.followed = followed;
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.image = in.readString();
        this.officialUrl = in.readString();
        this.followed = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(officialUrl);
        parcel.writeString(followed);
    }

    public static final Creator<ECShopObject> CREATOR = new Creator<ECShopObject>() {
        @Override
        public ECShopObject createFromParcel(Parcel parcel) {
            return new ECShopObject(parcel);
        }

        @Override
        public ECShopObject[] newArray(int i) {
            return new ECShopObject[i];
        }
    };
}
