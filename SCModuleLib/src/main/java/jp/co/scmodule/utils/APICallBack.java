package jp.co.scmodule.utils;

public interface APICallBack {
	/**
	 * Function:used to start loading dialog
	 * @author: Tran Quang Long
	 * @date: Aug 5, 2014
	 */
	public void uiStart();

	/**
	 * Function:used to return sucess state
	 * @param successString sucess string
	 * @param type replace for handle.message.what
	 * @author: Tran Quang Long
	 * @date: Aug 5, 2014
	 */
	public void success(String successString, int type);

	/**
	 * 
	 * Function:
	 * @param failString  return false state
	 * @author: Tran Quang Long
	 * @date: Aug 5, 2014
	 */
	public void fail(String failString);

	/**
	 * Function: used to close loading dialog
	 * @author: Tran Quang Long
	 * @date: Aug 5, 2014
	 */
	public void uiEnd();
}
