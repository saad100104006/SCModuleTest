package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class SCDepartmentObject implements Parcelable{
	private String id = null;
	private String name = null;
	private String haveMajor = null;

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

	public String getHaveMajor() {
		return haveMajor;
	}

	public void setHaveMajor(String haveMajor) {
		this.haveMajor = haveMajor;
	}

	public SCDepartmentObject() {
		super();
	}

	private SCDepartmentObject(Parcel in) {
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
		dest.writeString(haveMajor);
	}

	public void readFromParcel(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.haveMajor = in.readString();
	}

	// default creator for parcelable
	public static final Creator<SCDepartmentObject> CREATOR = new Creator<SCDepartmentObject>() {

		@Override
		public SCDepartmentObject[] newArray(int size) {
			return new SCDepartmentObject[size];
		}

		@Override
		public SCDepartmentObject createFromParcel(Parcel source) {
			return new SCDepartmentObject(source);
		}
	};
}
