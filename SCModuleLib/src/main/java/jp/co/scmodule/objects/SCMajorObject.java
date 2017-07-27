package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class SCMajorObject implements Parcelable{
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

	public SCMajorObject() {
		super();
	}

	private SCMajorObject(Parcel in) {
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
	public static final Creator<SCMajorObject> CREATOR = new Creator<SCMajorObject>() {

		@Override
		public SCMajorObject[] newArray(int size) {
			return new SCMajorObject[size];
		}

		@Override
		public SCMajorObject createFromParcel(Parcel source) {
			return new SCMajorObject(source);
		}
	};
}
