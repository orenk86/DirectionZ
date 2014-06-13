package com.finalProject.util;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.android.gms.maps.model.MarkerOptions;

public class AppPreferences {
	public static final String KEY_GAME = "game";
	final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName();
	private SharedPreferences _sharedPrefs;
	private Editor _prefsEditor;

	public AppPreferences(Context context) {
		this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
		this._prefsEditor = _sharedPrefs.edit();
	}

	public ArrayList<MarkerOptions> getTrack() {
		ArrayList<MarkerOptions> markerArray = null; // TODO change to Marker
		try {
			JSONObject gameJSON = new JSONObject(_sharedPrefs.getString(KEY_GAME, "{}"));
			markerArray.add((MarkerOptions) gameJSON.get("track"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return markerArray;
	}

	public void saveGame(JSONObject gameJSON) {
		_prefsEditor.putString(KEY_GAME, gameJSON.toString());
		_prefsEditor.commit();
	}
}