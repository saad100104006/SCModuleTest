/**
 * TODO
 * <br>This is the class which call request to server and get respone
 * <br>
 * @author: Phan Tri
 * @date: Oct 15, 2014
 * 
 */
package jp.co.scmodule.apis;

import java.io.File;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.scmodule.utils.SCGlobalUtils;
import jp.co.scmodule.utils.SCImageUtils;
import jp.co.scmodule.utils.SCConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

public class SCJsonParser {

	// private static InputStream mIs = null;
	private static JSONObject mJObj = null;
	private static String mJson = null;

	// constructor
	public SCJsonParser() {
		super();
	}

	@SuppressWarnings("unchecked")
	public JSONObject getJSONFromUrl(String url, int restType, List<Object> multiParams) {

		// Making HTTP request
		try {
			// authen string
			String authStr = null;
			String authStrEncoded = null;


			if(SCConstants.AUTH_USERNAME != null && SCConstants.AUTH_PASSWORD != null) {
				authStr = SCConstants.AUTH_USERNAME + ":" + SCConstants.AUTH_PASSWORD;
				authStrEncoded = Base64
						.encodeToString(authStr.getBytes("UTF-8"), Base64.NO_WRAP);
			}
			Boolean addExtraHeader = SCGlobalUtils.addAditionalHeader;
			// defaultHttpClient
			HttpClient httpClient = getNewHttpClient();
			// DefaultHttpClient httpClient = SCGlobalUtils.basicAuthen(url);
			// set time out for connection
			HttpParams myParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(myParams, SCConstants.CONNECTION_TIME_OUT);
			HttpConnectionParams.setSoTimeout(myParams, SCConstants.CONNECTION_TIME_OUT);

			HttpResponse httpResponse = null;
			if (restType == SCConstants.REST_POST) {
				HttpPost httpPost = new HttpPost(url);
				if(authStrEncoded != null) {
					httpPost.addHeader("Authorization", "Basic " + authStrEncoded);
				}
				if(addExtraHeader){
					SCGlobalUtils.addAditionalHeader = false;
					httpPost.addHeader(SCGlobalUtils.additionalHeaderTag, SCGlobalUtils.additionalHeaderValue);
					Log.e("HeaderAddedPOST", SCGlobalUtils.additionalHeaderValue);
				}
				// httpPost.setEntity(new UrlEncodedFormEntity(params));
				// set entity	
				ArrayList<NameValuePair> nameValuesParams = (ArrayList<NameValuePair>) multiParams
						.get(0);
				ArrayList<Map.Entry<String, Bitmap>> bitmapParams = (ArrayList<Map.Entry<String, Bitmap>>) multiParams
						.get(1);
				
				MultipartEntity multiEntity = new MultipartEntity();
				for (int i = 0; i < nameValuesParams.size(); i++) {
					try {		
						multiEntity.addPart(nameValuesParams.get(i).getName(), 
								new StringBody(nameValuesParams.get(i).getValue()));
					} catch (Exception e) {

					}
				}
				
				// get bitmap params
				for(int i = 0; i < bitmapParams.size(); i++) {
					try {
						Bitmap bm = bitmapParams.get(i).getValue();
						File file = SCImageUtils.bitmapToFile(bm);
						
						multiEntity.addPart(bitmapParams.get(i).getKey(), new FileBody(file));
					} catch (Exception e) {

					}
				}
				// add entity
				httpPost.setEntity(multiEntity);
				//http execute
				httpResponse = httpClient.execute(httpPost);
			} else if (restType == SCConstants.REST_GET) {
				HttpGet httpGet = new HttpGet(url);
				if(authStrEncoded != null) {
					httpGet.addHeader("Authorization", "Basic " + authStrEncoded);
				}
				if(addExtraHeader){
					SCGlobalUtils.addAditionalHeader = false;
					httpGet.addHeader(SCGlobalUtils.additionalHeaderTag, SCGlobalUtils.additionalHeaderValue);
					Log.e("HeaderAddedGET",SCGlobalUtils.additionalHeaderValue);
				}
				//http execute
				httpResponse = httpClient.execute(httpGet);
			} else if (restType == SCConstants.REST_PUT) {
				HttpPut httpPut = new HttpPut(url);
				if(authStrEncoded != null) {
					httpPut.addHeader("Authorization", "Basic " + authStrEncoded);
				}
				if(addExtraHeader){
					SCGlobalUtils.addAditionalHeader = false;
					httpPut.addHeader(SCGlobalUtils.additionalHeaderTag, SCGlobalUtils.additionalHeaderValue);
					Log.e("HeaderAddedPUT", SCGlobalUtils.additionalHeaderValue);
				}
				
				ArrayList<NameValuePair> nameValuesParams = (ArrayList<NameValuePair>) multiParams
						.get(0);
				ArrayList<Map.Entry<String, Bitmap>> bitmapParams = (ArrayList<Map.Entry<String, Bitmap>>) multiParams
						.get(1);
				
				
				if(bitmapParams.size() != 0) {
					MultipartEntity multiEntity = new MultipartEntity();
					// get values params
					for (int i = 0; i < nameValuesParams.size(); i++) {
						try {		
							multiEntity.addPart(nameValuesParams.get(i).getName(), 
									new StringBody(nameValuesParams.get(i).getValue()));
						} catch (Exception e) {
	
						}
					}
					
					// get bitmap params
					for(int i = 0; i < bitmapParams.size(); i++) {
						try {
							Bitmap bm = bitmapParams.get(i).getValue();
							File file = SCImageUtils.bitmapToFile(bm);
							
							multiEntity.addPart(bitmapParams.get(i).getKey(), new FileBody(file));
						} catch (Exception e) {
	
						}
					}		
					
					// add entity
					//httpPut.setEntity(new UrlEncodedFormEntity(nameValuesParams));
					httpPut.setEntity(multiEntity);
				} else {
					httpPut.setEntity(new UrlEncodedFormEntity(nameValuesParams));
				}
				//http execute
				httpResponse = httpClient.execute(httpPut);
			} else if (restType == SCConstants.REST_DELETE) {
				HttpDelete httpDelete = new HttpDelete(url);
				if(authStrEncoded != null) {
					httpDelete.addHeader("Authorization", "Basic " + authStrEncoded);
				}
				if(addExtraHeader){
					SCGlobalUtils.addAditionalHeader = false;
					httpDelete.addHeader(SCGlobalUtils.additionalHeaderTag, SCGlobalUtils.additionalHeaderValue);
					Log.e("HeaderAddedDELETE", SCGlobalUtils.additionalHeaderValue);
				}



				//http execute
				httpResponse = httpClient.execute(httpDelete);
			}

			HttpEntity httpEntity = httpResponse.getEntity();
			mJson = EntityUtils.toString(httpEntity);
			if(mJson.equals("OK")){
				mJson = "{maintainence:true}";
			}
			mJObj = new JSONObject(mJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mJObj;
	}
	
	private HttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new SCMySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}
}
