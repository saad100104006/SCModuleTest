package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VNCCO on 8/12/2015.
 */
public class ECCategoryObject implements Parcelable {
    private String id = "";
    private String name = "";

    public ECCategoryObject() {
    }

    private ECCategoryObject(Parcel in) {
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


    public void readFromParcel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
    }

    public static final Creator<ECCategoryObject> CREATOR = new Creator<ECCategoryObject>() {
        @Override
        public ECCategoryObject createFromParcel(Parcel parcel) {
            return new ECCategoryObject(parcel);
        }

        @Override
        public ECCategoryObject[] newArray(int i) {
            return new ECCategoryObject[i];
        }
    };
}
