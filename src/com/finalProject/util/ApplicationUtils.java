package com.finalProject.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ApplicationUtils {
	
	public static final int LOAD_TIMEOUT = 5000; // 3 seconds
	public static final int LOAD_INTERVAL = 100; // 0.1 seconds
	
	Context context;
	private ApiManager apiManager;
	private CallbackInterface saveCallback;
	int timer;
	
	public ApplicationUtils(Context context) {
		this.context = context;
		apiManager = new ApiManager(context);
		saveCallback = new CallbackInterface() {
			
			@Override
			public void onObjectResponseReturned(String response) {
				System.out.println("object: " + response);
			}
			
			@Override
			public void onErrorResponseReturned(String response) {
				System.out.println("error: " + response);
			}
			
			@Override
			public void onArrayResponseReturned(String response) {
				System.out.println("array: " + response);
			}
		};
	}
	
	public void saveTrack (String name, ArrayList<MarkerOptions> waypoints) {
		JSONObject trackToSend = new JSONObject();
		JSONArray waypointsJSON = new JSONArray();
		
		try {
			for (int i=0; i<waypoints.size(); i++) {
				waypointsJSON.put(getJSONObject(waypoints.get(i)));
			}
			trackToSend.put("name", name);
			trackToSend.put("waypoints", waypointsJSON);
			apiManager.ApiCall("track/", ApiManager.POST, trackToSend.toString(), saveCallback);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject getJSONObject(MarkerOptions marker) {
		JSONObject result = new JSONObject();
		try {
			result.put("title", marker.getTitle());
			result.put("snippet", marker.getSnippet());
			result.put("lat", marker.getPosition().latitude);
			result.put("lng", marker.getPosition().longitude);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public MarkerOptions getMarkerOptions(JSONObject json) {
		MarkerOptions result = new MarkerOptions();
		try {
			LatLng position = new LatLng(json.getDouble("lat"), json.getDouble("lng"));
			result.title(json.getString("title"));
			result.snippet(json.getString("snippet"));
			result.position(position);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}
