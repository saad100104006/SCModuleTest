package jp.co.scmodule.interfaces;

import jp.co.scmodule.objects.SCTypeObject;
import jp.co.scmodule.objects.SCUserObject;

/**
 * Created by WebHawks IT on 1/12/2016.
 */
public interface SCTypeUserCallback {
    public void onLoginSuccess(SCTypeObject userObj);
    public void onLoginFail();
}
