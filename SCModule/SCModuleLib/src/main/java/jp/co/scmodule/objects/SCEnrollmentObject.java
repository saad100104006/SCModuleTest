package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class SCEnrollmentObject implements Parcelable{
	private String name = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SCEnrollmentObject() {
		super();
	}

	private SCEnrollmentObject(Parcel in) {
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
		dest.writeString(name);
	}

	public void readFromParcel(Parcel in) {
		this.name = in.readString();
	}

	// default creator for parcelable
	public static final Creator<SCEnrollmentObject> CREATOR = new Creator<SCEnrollmentObject>() {

		@Override
		public SCEnrollmentObject[] newArray(int size) {
			return new SCEnrollmentObject[size];
		}

		@Override
		public SCEnrollmentObject createFromParcel(Parcel source) {
			return new SCEnrollmentObject(source);
		}
	};
}
