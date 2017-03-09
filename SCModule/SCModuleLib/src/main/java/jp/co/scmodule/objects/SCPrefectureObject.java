package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class SCPrefectureObject implements Parcelable{
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

	public SCPrefectureObject() {
		super();
	}

	private SCPrefectureObject(Parcel in) {
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
	public static final Creator<SCPrefectureObject> CREATOR = new Creator<SCPrefectureObject>() {

		@Override
		public SCPrefectureObject[] newArray(int size) {
			return new SCPrefectureObject[size];
		}

		@Override
		public SCPrefectureObject createFromParcel(Parcel source) {
			return new SCPrefectureObject(source);
		}
	};
}
