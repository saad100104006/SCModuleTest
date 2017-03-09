package jp.co.scmodule.interfaces;

import jp.co.scmodule.objects.SCUserObject;

/**
 * Created by Administrator on 7/23/2015.
 */
public interface SCSNSLoginCallback {
    public void onLoginSuccess(SCUserObject userObj);
    public void onLoginFail();
}
