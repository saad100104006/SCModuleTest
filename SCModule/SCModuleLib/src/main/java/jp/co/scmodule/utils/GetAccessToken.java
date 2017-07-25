package jp.co.scmodule.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.brickred.socialauth.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by WebHawks IT on 11/27/2015.
 */
public class GetAccessToken {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    private static String address = SCUrlConstants.URL_BASE_LOGIN + "get_auth_tokens";
    public GetAccessToken() {
    }
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    Map<String, String> mapn;
    DefaultHttpClient httpClient;
    HttpPost httpPost;

    public JSONObject gettoken(Context context,String token,String client_id,String client_secret,String redirect_uri) {
        // Making HTTP request
        try {
            // DefaultHttpClient
            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            httpClient = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(httpClient.getParams(), registry);

            // Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

            httpPost = new HttpPost(address);
            String UUID="";
            if( SCSharedPreferencesUtils.getString(context, SCConstants.TAG_DEVICE_ID, null) == null){
                if(SCGlobalUtils.DEVICEUUID != null)
                    UUID = SCGlobalUtils.DEVICEUUID;
                else
                {
                    UUID = SCGlobalUtils.getDeviceUUID(context);
                    if(UUID.equals("")) {
                        Toast.makeText(context, "UUID Missing", Toast.LENGTH_LONG).show();
                    }
                }
            }else{
                UUID = SCSharedPreferencesUtils.getString(context, SCConstants.TAG_DEVICE_ID, null);
            }
            params.add(new BasicNameValuePair("code", token));
            params.add(new BasicNameValuePair("uuid",UUID));
            Log.e("UUID",UUID);
            params.add(new BasicNameValuePair("grant_type", "authorization_code"));
            String custom_header = "Basic "+getBase64(client_id+":"+client_secret);

            httpPost.setHeader("Authorization", custom_header);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "");
            }
            is.close();

            json = sb.toString();
            Log.e("JSONStr", json);
        } catch (Exception e) {
            e.getMessage();
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        // Parse the String to a JSON Object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // Return JSON String
        return jObj;
    }


    public String getBase64(final String input) {
        return Base64.encodeBytes(input.getBytes(),Base64.DONT_BREAK_LINES);
    }
}
