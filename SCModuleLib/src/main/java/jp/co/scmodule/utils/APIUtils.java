/**
 * Desciption: create Connection to server and return result string
 * Author: Tran Quang Long
 * Create date: Feb 25, 2014
 */

package jp.co.scmodule.utils;

import android.app.Activity;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class APIUtils {

    public static int TIMEOUT_TIME = 30000;

    public static void LoadJSON(final Activity activity,
                                final HashMap<String, String> data, final String url,
                                final APICallBack apiCallBack) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                apiCallBack.uiStart();
            }
        });
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {

                    // final String url = "http://ip.jsontest.com/";

                    ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
                    if (data != null) {
                        Set<String> set = data.keySet();
                        for (String key : set) {
                            BasicNameValuePair value = new BasicNameValuePair(
                                    key, data.get(key));
                            list.add(value);
                        }
                    }

                    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                    HttpParams params = new BasicHttpParams();
                    HttpConnectionParams.setSoTimeout(params, TIMEOUT_TIME);
                    HttpConnectionParams.setConnectionTimeout(params,
                            TIMEOUT_TIME);

                    DefaultHttpClient client = new DefaultHttpClient(params);

                    SchemeRegistry registry = new SchemeRegistry();
                    SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
                    socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                    registry.register(new Scheme("https", socketFactory, 443));
                    SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);

                    // Set verifier
                    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);


                    HttpPost post = new HttpPost(url);
                    if (SCGlobalUtils.addAditionalHeader) {
                        SCGlobalUtils.addAditionalHeader = false;
                        post.addHeader(SCGlobalUtils.additionalHeaderTag, SCGlobalUtils.additionalHeaderValue);
                        Log.e("HeaderAddedPOST", SCGlobalUtils.additionalHeaderValue);
                    }
                    post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
                    HttpResponse response = null;
                    response = client.execute(post);
                    final StringBuilder builder = new StringBuilder();
                    if (response != null
                            && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                        InputStream inputStream = response.getEntity()
                                .getContent();
                        Scanner scanner = new Scanner(inputStream);
                        while (scanner.hasNext()) {
                            builder.append(scanner.nextLine());
                        }
                        inputStream.close();
                        scanner.close();
                        apiCallBack.success(builder.toString(), 0);
                    } else {
                        apiCallBack.fail(response != null
                                && response.getStatusLine() != null ? "response null"
                                : "" + response.getStatusLine().getStatusCode());
                    }
                } catch (final Exception e) {
                    apiCallBack.fail(e.getMessage());
                } finally {
                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            apiCallBack.uiEnd();
                        }
                    });
                }
            }
        }).start();
    }
}
