package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class SCGenderObject implements Parcelable{
	private String name = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SCGenderObject() {
		super();
	}

	private SCGenderObject(Parcel in) {
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
	public static final Creator<SCGenderObject> CREATOR = new Creator<SCGenderObject>() {

		@Override
		public SCGenderObject[] newArray(int size) {
			return new SCGenderObject[size];
		}

		@Override
		public SCGenderObject createFromParcel(Parcel source) {
			return new SCGenderObject(source);
		}
	};
}
