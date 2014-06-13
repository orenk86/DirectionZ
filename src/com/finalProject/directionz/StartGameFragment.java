package com.finalProject.directionz;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.finalProject.util.ApiManager;
import com.finalProject.util.ApplicationUtils;
import com.finalProject.util.CallbackInterface;
import com.finalProject.util.GameListAdapter;
import com.google.android.gms.maps.model.MarkerOptions;

public class StartGameFragment extends Fragment implements OnItemClickListener {

	ListView startGameListView;
	String gamesString;
	GameListAdapter adapter;
	ArrayList<MarkerOptions> mMarkerPoints;
	ApiManager apiManager;
	CallbackInterface loadCallback;
	ApplicationUtils appUtils;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_start_game, container, false);
		startGameListView = (ListView) rootView.findViewById(R.id.newGameListView);
		// the following 4 commands are for initializing the listview and
		// binding it with our adapter and onClickListener
		adapter = new GameListAdapter(getActivity());
		startGameListView.setAdapter(adapter);
		startGameListView.setOnItemClickListener(this);
		// listview initialized and binded. now we fill it with data...
		adapter.updateList();
		appUtils = new ApplicationUtils(getActivity());
		apiManager = new ApiManager(getActivity());
		loadCallback = new CallbackInterface() {
			
			@Override
			public void onObjectResponseReturned(String response) {
				System.out.println("object: " + response);
				try {// TODO debugging....
					JSONObject responseObject = new JSONObject(response);
					JSONArray responseTrackJSON = responseObject.getJSONArray("waypoints");
					for (int i=0; i<responseTrackJSON.length(); i++) {
						mMarkerPoints.add(appUtils.getMarkerOptions(responseTrackJSON.getJSONObject(i)));
					}
					((WelcomeScreenActivity) getActivity()).setGameMarkers(mMarkerPoints);
//					((WelcomeScreenActivity) getActivity()).setCreateGame(false);
//					((WelcomeScreenActivity) getActivity()).onNavigationDrawerItemSelected(0);
					((WelcomeScreenActivity) getActivity()).startMainGameFragment();
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO send to server to retreive info
		mMarkerPoints = new ArrayList<MarkerOptions>();
		apiManager.ApiCall("track/" + adapter.getItem(position).id, ApiManager.GET, loadCallback);
	}
}
