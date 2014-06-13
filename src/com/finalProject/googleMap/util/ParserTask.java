package com.finalProject.googleMap.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;

import com.finalProject.util.DirectionsJSONParser;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/** A class to parse the Google Directions in JSON format */
public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

	// Parsing the data in non-ui thread
	@Override
	protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

		JSONObject jObject;
		List<List<HashMap<String, String>>> routes = null;

		try {
			jObject = new JSONObject(jsonData[0]);
			DirectionsJSONParser parser = new DirectionsJSONParser();

			// Starts parsing data
			routes = parser.parse(jObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return routes;
	}

	// Executes in UI thread, after the parsing process
	@Override
	protected void onPostExecute(List<List<HashMap<String, String>>> result) {
		ArrayList<LatLng> points = null;
		PolylineOptions lineOptions = null;

		// Traversing through all the routes
		for (int i = 0; i < result.size(); i++) {
			points = new ArrayList<LatLng>();
			lineOptions = new PolylineOptions();

			// Fetching i-th route
			List<HashMap<String, String>> path = result.get(i);

			// Fetching all the points in i-th route
			for (int j = 0; j < path.size(); j++) {
				HashMap<String, String> point = path.get(j);

				double lat = Double.parseDouble(point.get("lat"));
				double lng = Double.parseDouble(point.get("lng"));
				LatLng position = new LatLng(lat, lng);

				points.add(position);
			}

		}
	}
}