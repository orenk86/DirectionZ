package com.finalProject.directionz;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.finalProject.googleMap.util.GoogleMapsUtil;
import com.finalProject.util.ApplicationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CreateGameFragment extends Fragment implements LocationListener {

	View rootView;
	GoogleMap map;
	ArrayList<MarkerOptions> mMarkerPoints;
	Location location;
	LatLng markerPoint;
	Context context;
	Geocoder geoCoder;
	Dialog dialog;
	String gameName = null;
	double mLatitude = 0;
	double mLongitude = 0;
	ApplicationUtils appUtils;
//	private boolean isCreateGame = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appUtils = new ApplicationUtils(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (rootView != null) {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null)
				parent.removeView(rootView);
		}

		try {
			if (rootView == null) {
				rootView = inflater.inflate(R.layout.fragment_create_game, container, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		initializeMap();
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {
//				if (isCreateGame) {
					markerPoint = point;
					openMarkerAddDialog();
//				}
			}

		});
		// Setting click event handler for InfoWIndow
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				// Remove the marker
//				if (isCreateGame) {
					for (int i = 0; i < mMarkerPoints.size(); i++) {
						if (mMarkerPoints.get(i).equals(marker)) {
							mMarkerPoints.remove(i);
						}
					}
					marker.remove();
//				}
			}
		});
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		isCreateGame = ((WelcomeScreenActivity) getActivity()).isCreateGame();
		setHasOptionsMenu(true);
//		if (!isCreateGame) {
//			mMarkerPoints = ((WelcomeScreenActivity) getActivity()).getGameMarkers();
//		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
//		((WelcomeScreenActivity) getActivity()).setCreateGame(true);
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
				map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
				mMarkerPoints = new ArrayList<MarkerOptions>();
				
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
				location = locationManager.getLastKnownLocation(provider);

				if (location != null) {
					onLocationChanged(location);
				}

				locationManager.requestLocationUpdates(provider, 20000, 0, this);

				mLatitude = location.getLatitude();
				mLongitude = location.getLongitude();
				LatLng coordinate = new LatLng(mLatitude, mLongitude);

				map.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
				map.animateCamera(CameraUpdateFactory.zoomTo(15));
				GoogleMapOptions options = new GoogleMapOptions();
				options.mapType(GoogleMap.MAP_TYPE_HYBRID).compassEnabled(true).rotateGesturesEnabled(true).tiltGesturesEnabled(false);
				((WelcomeScreenActivity) getActivity()).setMap(map);
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.create_game, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.action_send:
			saveGameAlretDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void saveGameAlretDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("Game Name");
		alert.setMessage("Choose Game Name");

		// Set an EditText view to get user input
		final EditText input = new EditText(getActivity());
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				gameName = input.getText().toString();
				appUtils.saveTrack(gameName, mMarkerPoints);
				((WelcomeScreenActivity) getActivity()).onNavigationDrawerItemSelected(1);
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();

	}

	@Override
	public void onLocationChanged(Location location) {
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();
		
//		if (isCreateGame) {
			LatLng point = new LatLng(mLatitude, mLongitude);
			map.moveCamera(CameraUpdateFactory.newLatLng(point));
//		} else {
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
//		}

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
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onProviderDisabled(String provider) {}

	public void openMarkerAddDialog() {
		if (dialog == null) {

			dialog = new Dialog(getActivity());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.add_marker_dialog);

			final EditText markerName = (EditText) dialog.findViewById(R.id.MarkerName);

			final EditText markerDisc = (EditText) dialog.findViewById(R.id.MarkerDisc);

			Button setMarker = (Button) dialog.findViewById(R.id.AddHintButton);
			setMarker.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					GoogleMapsUtil.drawMarker(map, markerPoint, markerName.getText().toString(), markerDisc.getText().toString(), mMarkerPoints);
					dialog.dismiss();
				}
			});

			Button cancelMarker = (Button) dialog.findViewById(R.id.CancelHintButton);
			cancelMarker.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});

			dialog.show();
		} else {
			dialog.show();
		}

	}
}