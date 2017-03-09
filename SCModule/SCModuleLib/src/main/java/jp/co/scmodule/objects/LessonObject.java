package jp.co.scmodule.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 
 * Schedule object class
 * 
 * @author Phan Tri
 * 
 */
public class LessonObject implements Parcelable {
	private String mId = null;
	private String mName = null;
	private String mSemester = null;
	private String mIsAttending = null;
	private String mAttendeeCount = null;
	private String mCategory = null;
	private String mState = null;
	private ArrayList<String> mTeachers = new ArrayList<String>();
	private ArrayList<String> mRooms = new ArrayList<String>();
	private ArrayList<ScheduleObject> mSchedules = new ArrayList<ScheduleObject>();

	public LessonObject(String mClassName, String mTeacherName, String mRoomInfo, String mDate,
						String mTime, String mType, String mIsRegistered, String mMemberCount) {
		super();
		// this.mClassName = mClassName;
		// this.mTeachers = mTeacherName;
		// this.mRooms = mRoomInfo;
		// this.mDate = mDate;
		// this.mPeriod = mTime;
		// this.mType = mType;
		// this.mIsRegistered = mIsRegistered;
		// this.mMemberCount = mMemberCount;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmSemester() {
		return mSemester;
	}

	public void setmSemester(String mSemester) {
		this.mSemester = mSemester;
	}

	public String getmIsAttending() {
		return mIsAttending;
	}

	public void setmIsAttending(String mIsAttending) {
		this.mIsAttending = mIsAttending;
	}

	public String getmAttendeeCount() {
		return mAttendeeCount;
	}

	public void setmAttendeeCount(String mAttendeeCount) {
		this.mAttendeeCount = mAttendeeCount;
	}

	public String getmCategory() {
		return mCategory;
	}

	public void setmCategory(String mCategory) {
		this.mCategory = mCategory;
	}

	public String getmState() {
		return mState;
	}

	public void setmState(String mState) {
		this.mState = mState;
	}

	public ArrayList<String> getmTeachers() {
		return mTeachers;
	}

	public void setmTeachers(ArrayList<String> mTeachers) {
		this.mTeachers = mTeachers;
	}

	public ArrayList<String> getmRooms() {
		return mRooms;
	}

	public void setmRooms(ArrayList<String> mRooms) {
		this.mRooms = mRooms;
	}

	public ArrayList<ScheduleObject> getmSchedules() {
		return mSchedules;
	}

	public void setmSchedules(ArrayList<ScheduleObject> mSchedules) {
		this.mSchedules = mSchedules;
	}

	public LessonObject() {
		super();
	}

	// constructor for parcelable
	private LessonObject(Parcel in) {
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
		dest.writeString(mId);
		dest.writeString(mName);
		dest.writeString(mSemester);
		dest.writeString(mIsAttending);
		dest.writeString(mAttendeeCount);
		dest.writeString(mCategory);
		dest.writeString(mState);
		dest.writeStringList(mTeachers);
		dest.writeStringList(mRooms);
		dest.writeTypedList(mSchedules);
	}

	public void readFromParcel(Parcel in) {
		this.mId = in.readString();
		this.mName = in.readString();
		this.mSemester = in.readString();
		this.mIsAttending = in.readString();
		this.mAttendeeCount = in.readString();
		this.mCategory = in.readString();
		this.mState = in.readString();
		in.readStringList(mTeachers);
		in.readStringList(mRooms);
		in.readTypedList(mSchedules, ScheduleObject.CREATOR);
	}

	// default creator for parcelable
	public static final Creator<LessonObject> CREATOR = new Creator<LessonObject>() {

		@Override
		public LessonObject[] newArray(int size) {
			return new LessonObject[size];
		}

		@Override
		public LessonObject createFromParcel(Parcel source) {
			return new LessonObject(source);
		}
	};

}
