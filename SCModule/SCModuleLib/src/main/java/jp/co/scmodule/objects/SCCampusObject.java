package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class SCCampusObject implements Parcelable{
	private String id = null;
	private String name = null;

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

	public SCCampusObject() {
		super();
	}

	private SCCampusObject(Parcel in) {
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
		dest.writeString(name);
	}

	public void readFromParcel(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
	}

	// default creator for parcelable
	public static final Creator<SCCampusObject> CREATOR = new Creator<SCCampusObject>() {

		@Override
		public SCCampusObject[] newArray(int size) {
			return new SCCampusObject[size];
		}

		@Override
		public SCCampusObject createFromParcel(Parcel source) {
			return new SCCampusObject(source);
		}
	};
}
