package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class SCUniversityObject implements Parcelable{
	private String id = null;
	private String name = null;
	private String kana = null;
	private SCCampusObject mCampusObj = null;

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

	public String getKana() {
		return kana;
	}

	public void setKana(String kana) {
		this.kana = kana;
	}

	public SCCampusObject getmCampusObj() {
		return mCampusObj;
	}

	public void setmCampusObj(SCCampusObject mCampusObj) {
		this.mCampusObj = mCampusObj;
	}

	public SCUniversityObject() {
		super();
	}

	private SCUniversityObject(Parcel in) {
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
		dest.writeString(kana);
		dest.writeParcelable(mCampusObj, flags);
	}

	public void readFromParcel(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.kana = in.readString();
		this.mCampusObj = in.readParcelable(SCCampusObject.class.getClassLoader());
	}

	// default creator for parcelable
	public static final Parcelable.Creator<SCUniversityObject> CREATOR = new Creator<SCUniversityObject>() {

		@Override
		public SCUniversityObject[] newArray(int size) {
			return new SCUniversityObject[size];
		}

		@Override
		public SCUniversityObject createFromParcel(Parcel source) {
			return new SCUniversityObject(source);
		}
	};
}
