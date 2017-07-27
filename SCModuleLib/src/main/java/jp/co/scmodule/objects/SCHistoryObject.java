package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WebHawks IT on 8/22/2016.
 */
public class SCHistoryObject implements Parcelable {
    private String point_item_name = null;
    private String exchange_item_name = null;

    private String service = null;
    private String diff = null;
    private String created = null;


    public String getPoint_item_name() {
        return point_item_name;
    }

    public void setPoint_item_name(String point_item_name) {
        this.point_item_name = point_item_name;
    }

    public String getExchange_item_name() {
        return exchange_item_name;
    }

    public void setExchange_item_name(String exchange_item_name) {
        this.exchange_item_name = exchange_item_name;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public SCHistoryObject() {
        super();
    }

    // constructor for parcelable
    private SCHistoryObject(Parcel in) {
        this();
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(point_item_name);
        dest.writeString(exchange_item_name);
        dest.writeString(service);
        dest.writeString(diff);
        dest.writeString(created);
    }

    public void readFromParcel(Parcel in){
        this.point_item_name = in.readString();
        this.exchange_item_name = in.readString();
        this.service = in.readString();
        this.diff = in.readString();
        this.created = in.readString();
    }

    // default creator for parcelable
    public static final Creator<SCHistoryObject> CREATOR = new Creator<SCHistoryObject>() {

        @Override
        public SCHistoryObject[] newArray(int size) {
            return new SCHistoryObject[size];
        }

        @Override
        public SCHistoryObject createFromParcel(Parcel source) {
            return new SCHistoryObject(source);
        }
    };

}
