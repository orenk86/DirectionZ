package com.finalProject.directionz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.finalProject.util.GameListAdapter;

public class JoinGameFragment extends Fragment implements OnItemClickListener {

	ListView joingGameListView;
	GameListAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_join_game, container, false);
		joingGameListView = (ListView) rootView.findViewById(R.id.existingGameListView);

		// the following 4 commands are for initializing the listview and
		// binding it with our adapter and onClickListener
		adapter = new GameListAdapter(getActivity()); // in a fragment, instead
		// of "this", put in
		// "getActivity()"
		joingGameListView.setAdapter(adapter);
		joingGameListView.setOnItemClickListener(this);
		// listview initialized and binded. now we fill it with data...
		adapter.updateList(); // we call this every time there's a
		// change in the data (i.e. in the
		// callback of the API request)
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//TODO add call to server and get the info needed
		((WelcomeScreenActivity) getActivity()).onNavigationDrawerItemSelected(1);
//		FragmentManager fragmentManager = getChildFragmentManager();
//		MainGameFragment fragment = new MainGameFragment();
//		fragmentManager.beginTransaction().add(R.id.container, fragment).commit();

	}
}
