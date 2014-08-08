package com.finalProject.googleMap.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapsUtil {
	Location myLocation;
	Context context;
	EditText searchBox;
	ImageButton searchButton;
	Geocoder geoCoder;
	
	static float angle;

	/**
	 * @return the angle
	 */
	public static float getAngle() {
		return angle;
	}

	/**
	 * @param angle the angle to set
	 */
	public static void setAngle(float angle) {
		GoogleMapsUtil.angle = angle;
	}

	public static void drawMarker(GoogleMap map, LatLng point, String title, String snippet, ArrayList<MarkerOptions> mMarkerPoints) {

		// Creating MarkerOptions
		MarkerOptions options = new MarkerOptions();

		// Setting the position of the marker
		options.position(point);
		if (null != title) {
			options.title(title);
		}
		if (null != snippet) {
			options.snippet(snippet);
		}

		/**
		 * For the start location, the color of marker is GREEN and for the end
		 * location, the color of marker is RED.
		 */
		switch (mMarkerPoints.size()) {
		case 0:
			options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			break;
		case 1:
			options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			break;
		case 2:
			options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
			break;
		case 3:
			options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
			break;
		default:
			options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		}
		map.moveCamera(CameraUpdateFactory.newLatLng(point));
		map.animateCamera(CameraUpdateFactory.zoomTo(15));

		// Add new marker to the Google Map Android API V2

		map.addMarker(options);
		mMarkerPoints.add(options);

	}

	/**
	 * get the current location and search the markers to see if we reached the
	 * marker needed
	 * 
	 * @param map
	 * @param context
	 * @param mLongitude2 
	 * @param mLatitude2 
	 * @param location
	 * @param mMarkerPoints
	 * @param bearing 
	 * @return true if we have false otherwise
	 */
	public static  String locationChange(GoogleMap map, Context context, Location location, ArrayList<MarkerOptions> mMarkerPoints) {
		String result = null;

		LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
		
		map.moveCamera(CameraUpdateFactory.newLatLng(point));
		map.animateCamera(CameraUpdateFactory.zoomTo(20));
//		if (!mMarkerPoints.isEmpty()) {
			if (mMarkerPoints.size() > 0) {
				result = checkIfHints(point , mMarkerPoints);
				if (null== result) {
					calculateP2PDirection(point, mMarkerPoints);
					 result = checkNextDirection(location, mMarkerPoints);

				}
			} else {
				result = "endGame";
			}
//		}

		return result;

	}

	private static  String checkNextDirection(Location location, ArrayList<MarkerOptions> mMarkerPoints) {
//		LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
		Location dest = new Location(location);
		dest.setLatitude(mMarkerPoints.get(0).getPosition().latitude);
		dest.setLatitude(mMarkerPoints.get(0).getPosition().longitude);
		float bearing = location.bearingTo(dest);
		float heading = 0;
		
		GeomagneticField geoField = new GeomagneticField(
				Double.valueOf(location.getLatitude()).floatValue(),
				Double.valueOf(location.getLongitude()).floatValue(),
				Double.valueOf(location.getAltitude()).floatValue(),
				System.currentTimeMillis());
		heading += geoField.getDeclination();
		heading -= bearing;
		angle = (heading + 360) % 360;
		
//		double absLong = Math.abs(point.longitude) - Math.abs(mMarkerPoints.get(0).getPosition().latitude);
//		double absLat = Math.abs(point.latitude) - Math.abs(mMarkerPoints.get(0).getPosition().latitude);
		String result = null;
//		double bearing = CalculateBearing.initial(point.latitude, point.longitude, mMarkerPoints.get(0).getPosition().latitude, mMarkerPoints.get(0).getPosition().latitude);
//		if (absLat > 0.0 && absLong == 0.0) {
//			result = "down";
//		}
//		if (absLat == 0.0 && absLong > 0.0) {
//			result = "left";
//		}
//		if (absLat < 0.0 && absLong == 0.0) {
//			result = "up";
//		}
//		if (absLat == 0.0 && absLong < 0.0) {
//			result = "right";
//		}
//		if (absLat > 0.0 && absLong > 0.0) {
//			result = "downLeft";
//		}
//		if (absLat > 0.0 && absLong < 0.0) {
//			result = "downRight";
//		}
//		if (absLat < 0.0 && absLong > 0.0) {
//			result = "upLeft";
//		}
//		if (absLat < 0.0 && absLong < 0.0) {
//			result = "upRight";
//		}

		if (angle >135 && angle <225) {
			result = "down";
		} 
		if (angle >225 && angle <315) {
			result = "left";
		} 
		if ((angle >315 && angle <360) || (angle <45 && angle >0)) {
			result = "up";
		} 
		if (angle >45 && angle <135) {
			result = "right";
		} 
		if (angle >115 && angle <155) {
			result = "downLeft";
		} 
		if (angle >205 && angle <245) {
			result = "downRight";
		} 
		if (angle >295 && angle <335) {
			result = "upLeft";
		} 
		if (angle <60 && angle >25) {
			result = "upRight";
		}
		return result;
	}

	private static  String checkIfHints(LatLng point, ArrayList<MarkerOptions> mMarkerPoints) {
		String result = null;
		if (mMarkerPoints.size() >= 1) {
			double abslong = Math.abs(point.longitude - mMarkerPoints.get(0).getPosition().longitude);
			double abslat = Math.abs(point.latitude - mMarkerPoints.get(0).getPosition().latitude);
			if (abslong <= 0.00005 && abslat <= 0.00005) {
				result = "hintReached";
			}
		} else {
			result = "gameEnd";
		}
		return result;
	}

	/**
	 * for testing ALONE! REMOVE!!!!
	 * 
	 * @param map
	 * @param mMarkerPoints
	 */
	public static void mainGameTempDrawMarkerArry(GoogleMap map, ArrayList<MarkerOptions> mMarkerPoints) {
		for (MarkerOptions options : mMarkerPoints) {
			map.addMarker(options);
		}

	}

	/**
	 * does the calculation of the markers to draw and draws sends to the google
	 * api
	 * @param point 
	 */
	private static  void calculateP2PDirection(LatLng point, ArrayList<MarkerOptions> mMarkerPoints) {
		// Checks, whether start and end locations are captured

		if (!mMarkerPoints.isEmpty()) {
			LatLng origin = new LatLng(point.latitude, point.longitude);
			LatLng dest = mMarkerPoints.get(0).getPosition();
			String url = getDirectionsUrl(origin, dest);

			DownloadTask downloadTask = new DownloadTask();
			// Start downloading json data from Google Directions API
			downloadTask.execute(url);
		}
	}

	/**
	 * get the direction from the google map url and process it
	 * 
	 * @param origin
	 * @param dest
	 * @return
	 */
	private static String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		String mode = "mode=walking";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

		return url;
	}

	/** A method to download json data from url */
	public static String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

}
