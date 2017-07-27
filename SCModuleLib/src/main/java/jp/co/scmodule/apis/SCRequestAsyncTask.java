package jp.co.scmodule.apis;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;

import jp.co.scmodule.R;
import jp.co.scmodule.utils.SCConstants;
import jp.co.scmodule.classes.SCMySafeAsyncTask;
import jp.co.scmodule.utils.SCGlobalUtils;

public class SCRequestAsyncTask extends SCMySafeAsyncTask<String> {
    private HashMap<String, Object> mParameters = null;
    private AsyncCallback mAsyncCallback = null;
    private int mTypeOfRequest = 0;
    private Context mContext = null;

    public SCRequestAsyncTask(final Context context, final int typeOfRequest, final HashMap<String, Object> parameters, final AsyncCallback asyncCallback) {
        mTypeOfRequest = typeOfRequest;
        mParameters = parameters;
        mAsyncCallback = asyncCallback;
        mContext = context;
    }

    @Override
    protected void onPreExecute() throws Exception {
        mAsyncCallback.progress();
    }

    @Override
    public String call() throws Exception {
        if(SCGlobalUtils.isNetworkConnected(mContext)) {
            SCRequestData getDataObj = new SCRequestData(mContext);
            return getDataObj.getData(mTypeOfRequest, mParameters);
        } else {
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String title = mContext.getResources().getString(R.string.dialog_error_title);
                    String body = mContext.getResources().getString(R.string.dialog_no_internet_connection);
                    SCGlobalUtils.showInfoDialog(mContext, title, body, null, null);
                }
            });

            return null;
        }
    }

    @Override
    protected void onSuccess(String result) throws Exception {
        mAsyncCallback.done(result);
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        mAsyncCallback.onException(e);
    }

    @Override
    protected void onInterrupted(Exception e) {
        mAsyncCallback.onInterrupted(e);
    }

    public interface AsyncCallback {
        // when asynctask done it call done()
        public void done(String result);

        // when asynctask begin it call progress()
        public void progress();

        public void onInterrupted(Exception e);

        public void onException(Exception e);
    }

    public interface ObjectCallback {
        public void done(Object result);
    }
}
