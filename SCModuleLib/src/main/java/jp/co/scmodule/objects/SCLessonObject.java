package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class SCLessonObject implements Parcelable{
	private String id = null;
	private String name = null;
	private String room = null;
	private String teacher = null;
	private String period = null;

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

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public SCLessonObject() {
		super();
	}

	private SCLessonObject(Parcel in) {
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
		dest.writeString(room);
		dest.writeString(teacher);
		dest.writeString(period);
	}

	public void readFromParcel(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.room = in.readString();
		this.teacher = in.readString();
		this.period = in.readString();
	}

	// default creator for parcelable
	public static final Creator<SCLessonObject> CREATOR = new Creator<SCLessonObject>() {

		@Override
		public SCLessonObject[] newArray(int size) {
			return new SCLessonObject[size];
		}

		@Override
		public SCLessonObject createFromParcel(Parcel source) {
			return new SCLessonObject(source);
		}
	};
}
