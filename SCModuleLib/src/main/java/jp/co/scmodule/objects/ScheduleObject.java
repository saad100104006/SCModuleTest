package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class ScheduleObject implements Parcelable{
	private String mDayOfWeek = null;
	private String mPeroid = null;

	public String getmDayOfWeek() {
		return mDayOfWeek;
	}

	public void setmDayOfWeek(String mDayOfWeek) {
		this.mDayOfWeek = mDayOfWeek;
	}

	public String getmPeroid() {
		return mPeroid;
	}

	public void setmPeroid(String mPeroid) {
		this.mPeroid = mPeroid;
	}

	public ScheduleObject() {
		super();
	}
	
	// constructor for parcelable
	private ScheduleObject(Parcel in) {
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
		dest.writeString(mDayOfWeek);
		dest.writeString(mPeroid);
	}
	
	public void readFromParcel(Parcel in){
		this.mDayOfWeek = in.readString();
		this.mPeroid = in.readString();
	}
	
	// default creator for parcelable
	public static final Creator<ScheduleObject> CREATOR = new Creator<ScheduleObject>() {

		@Override
		public ScheduleObject[] newArray(int size) {
			return new ScheduleObject[size];
		}

		@Override
		public ScheduleObject createFromParcel(Parcel source) {
			return new ScheduleObject(source);
		}
	};

}
