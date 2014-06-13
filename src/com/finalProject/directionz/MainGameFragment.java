package com.finalProject.directionz;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.finalProject.googleMap.util.GoogleMapsUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainGameFragment extends Fragment implements LocationListener, AnimationListener {

	View rootView;
	GoogleMap map;
	ArrayList<MarkerOptions> mMarkerPoints;
	Location myLocation;
	Context context;
	ImageView directionImage;
	Animation directionAnim;
	Geocoder geoCoder;
	Dialog dialog;
	double mLatitude = 0;
	double mLongitude = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (rootView != null) {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null)
				parent.removeView(rootView);
		}

		try {
			if (rootView == null) {
				rootView = inflater.inflate(R.layout.fragment_main_game, container, false);
				initializeMap();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		directionImage = (ImageView) rootView.findViewById(R.id.directionImage);
		directionAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_fade_out);
		return rootView;
	}

	private void initializeMap() {
		if (map == null) {

			int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

			if (status != ConnectionResult.SUCCESS) { // Google Play Services
														// are not available
				int requestCode = 10;
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
				dialog.show();

			} else {
//				map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mainGameMap)).getMap();
				map = ((WelcomeScreenActivity) getActivity()).getMap();
				mMarkerPoints = new ArrayList<MarkerOptions>();
				mMarkerPoints = ((WelcomeScreenActivity) getActivity()).getGameMarkers();
				GoogleMapsUtil.mainGameTempDrawMarkerArry(map, mMarkerPoints);
				map.setMyLocationEnabled(true);

				map.setOnMarkerClickListener((OnMarkerClickListener) context);

				// Getting LocationManager object from System Service
				// LOCATION_SERVICE
				LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

				// Creating a criteria object to retrieve provider
				Criteria criteria = new Criteria();

				// Getting the name of the best provider
				String provider = locationManager.getBestProvider(criteria, true);

				// Getting Current Location From GPS
				Location location = locationManager.getLastKnownLocation(provider);

				if (location != null) {
					onLocationChanged(location);
				}

				locationManager.requestLocationUpdates(provider, 20000, 0, this);

				mLatitude = location.getLatitude();
				mLongitude = location.getLongitude();
				LatLng coordinate = new LatLng(mLatitude, mLongitude);

				map.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
				map.animateCamera(CameraUpdateFactory.zoomTo(15));
				// Zoom in, animating the camera.
				GoogleMapOptions options = new GoogleMapOptions();
				options.mapType(GoogleMap.MAP_TYPE_HYBRID).compassEnabled(true).rotateGesturesEnabled(true).tiltGesturesEnabled(false);

			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();
		String result = GoogleMapsUtil.locationChange(map, getActivity(),mLatitude, mLongitude, mMarkerPoints);

		if (null != result) {
			switch (result) {
			case "up":
				// directionImage.setImageResource(R.drawable.up_arrow);
				Toast.makeText(getActivity(), "Continue Straight", Toast.LENGTH_SHORT).show();
				break;
			case "down":
				// directionImage.setImageResource(R.drawable.down_arrow);
				Toast.makeText(getActivity(), "Go Back", Toast.LENGTH_SHORT).show();
				break;
			case "left":
				// directionImage.setImageResource(R.drawable.left_arrow);
				Toast.makeText(getActivity(), "Turn Left", Toast.LENGTH_SHORT).show();
				break;
			case "right":
				// directionImage.setImageResource(R.drawable.right_arrow);
				Toast.makeText(getActivity(), "Turn Right", Toast.LENGTH_SHORT).show();
				break;
			case "upRight":
				// directionImage.setImageResource(R.drawable.up_right_arrow);
				Toast.makeText(getActivity(), "Continue Straight and to the Right", Toast.LENGTH_SHORT).show();
				break;
			case "upLeft":
				// directionImage.setImageResource(R.drawable.up_left_arrow);
				Toast.makeText(getActivity(), "Continue Straight and to the Left", Toast.LENGTH_SHORT).show();
				break;
			case "downRight":
				// directionImage.setImageResource(R.drawable.down_right_arrow);
				Toast.makeText(getActivity(), "Turn around and go Right", Toast.LENGTH_SHORT).show();
				break;
			case "downLeft":
				// directionImage.setImageResource(R.drawable.down_left_arrow);
				Toast.makeText(getActivity(), "Turn around and go Left", Toast.LENGTH_SHORT).show();
				break;
			default:
				openDialog(result);
				break;
			}
		}
	}

	private void openDialog(String result) {
		if (dialog == null) {

			dialog = new Dialog(getActivity());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			if (result.equals("hintReached")) {
				dialog.setContentView(R.layout.hint_popup);
				final TextView hintTitle = (TextView) dialog.findViewById(R.id.hintTitle);
				final TextView hint = (TextView) dialog.findViewById(R.id.hint);
				hintTitle.setText(mMarkerPoints.get(0).getTitle());
				hint.setText(mMarkerPoints.get(0).getSnippet());
				mMarkerPoints.remove(mMarkerPoints.get(0));
				
			} else {
				dialog.setContentView(R.layout.end_game_popup);
			}

			dialog.show();
		} else {
			dialog.show();
		}

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}
}