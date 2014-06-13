package com.finalProject.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ApiManager {
	private String URL_PREFIX = "http://54.189.155.83:3000/v1.0/";
	final private static int CONNECTION_TIMEOUT = 5000;
	final private static int SOCKET_TIMEOUT = 5000;
	
	private static HttpParams httpParameters = new BasicHttpParams();
	
	private Context context;
	
	final public static int POST = 1;
	final public static int GET = 2;
	final public static int PUT = 3;
	final public static int DELETE = 4;
	
    // Constructor
    public ApiManager(Context context) {
    	this.context = context;
    }
    
    public void ApiCall(String uri, int method, CallbackInterface callback) {
    	new ApiCallTask(method, "", context, callback).execute(URL_PREFIX + uri);
    }
    
    public void ApiCall(String uri, int method, String data, CallbackInterface callback) {
    	new ApiCallTask(method, data, context, callback).execute(URL_PREFIX + uri);
    }
    
    class ApiCallTask extends AsyncTask<String, Void, HttpResponse> {
    	private int method;
    	private String data;
    	private ProgressDialog dialog;
    	final CallbackInterface callback;
    	
    	public ApiCallTask(int method, String data, Context context, CallbackInterface callback) {
    		this.method = method;
    		this.data = data;
    		this.dialog = new ProgressDialog(context);
    		this.callback = callback;
    	}
    	
		@Override
		protected void onPreExecute() {
			dialog.setMessage("Loading ...");
			dialog.setCancelable(false);
			dialog.setIndeterminate(true);
			dialog.show();
		}
		
		@Override
		protected HttpResponse doInBackground(String... uri) {
			switch (method) {
			case POST:
				return httpPost(uri[0], data);
			case GET:
				return httpGet(uri[0]);
			case PUT:
				return httpPut(uri[0], data);
			case DELETE:
				return httpDelete(uri[0]);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(HttpResponse response) {
			try {
				if (response != null) {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						String responseBody = EntityUtils.toString(entity);
//						JSONObject responseObject = new JSONObject(responseBody);
//						if (responseObject.getInt("error") == 1) {
//							callback.onErrorResponseReturned(responseBody);
//						}
//						else {
							switch (responseBody.charAt(0)) {
							case '{':
								callback.onObjectResponseReturned(responseBody);
								break;
							case '[':
								callback.onArrayResponseReturned(responseBody);
								break;
							default:
								break;
							}
//						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			catch (JSONException e) {
//				e.printStackTrace();
//			}
			
			if (dialog != null) {
				dialog.dismiss();
			}
		}
    }

    private static HttpResponse httpPost(String uri, String data) {
	    try {
	        HttpPost httpPost = new HttpPost(uri);
	        httpPost.setEntity(new StringEntity(data));
	        httpPost.setHeader("Accept", "application/json");
	        httpPost.setHeader("Content-type", "application/json");
	        
	        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
	        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
	        
	        return httpClient.execute(httpPost);
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
    private static HttpResponse httpGet(String uri) {
	    try {
	        HttpGet httpGet = new HttpGet(uri);
	        
	        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
	        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
	        
	        return httpClient.execute(httpGet);
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
    private static HttpResponse httpPut(String uri, String data) {
	    try {
	        HttpPut httpPut = new HttpPut(uri);
	        httpPut.setEntity(new StringEntity(data));
	        httpPut.setHeader("Accept", "application/json");
	        httpPut.setHeader("Content-type", "application/json");
	        
	        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
	        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
	        
	        return httpClient.execute(httpPut);
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
    private static HttpResponse httpDelete(String uri) {
	    try {
	        HttpDelete httpDelete = new HttpDelete(uri);
	        
	        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
	        HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
	        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
	        
	        return httpClient.execute(httpDelete);
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
