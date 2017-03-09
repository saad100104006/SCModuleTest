package jp.co.scmodule.objects;

import java.util.ArrayList;

public class SCSectionObject {
	private int mSectionId = 0;
	private String mSectionLabel = null;
	private int mSectionSize = 0;
	private ArrayList<Object> mListData = null;
	
	public int getmSectionId() {
		return mSectionId;
	}
	public void setmSectionId(int mSectionId) {
		this.mSectionId = mSectionId;
	}
	public String getmSectionLabel() {
		return mSectionLabel;
	}
	public void setmSectionLabel(String mSectionLabel) {
		this.mSectionLabel = mSectionLabel;
	}
	public int getmSectionSize() {
		return mSectionSize;
	}
	public void setmSectionSize(int mSectionSize) {
		this.mSectionSize = mSectionSize;
	}

	public ArrayList<Object> getmListData() {
		return mListData;
	}

	public void setmListData(ArrayList<Object> mListData) {
		this.mListData = mListData;
	}
}
