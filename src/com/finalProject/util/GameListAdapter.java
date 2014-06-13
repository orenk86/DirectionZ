package com.finalProject.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.finalProject.datatype.GameListItem;
import com.finalProject.directionz.R;

public class GameListAdapter extends BaseAdapter{

	Context context;
	List<GameListItem> games;
	private ApiManager apiManager;
	private CallbackInterface trackListCallback;
	
	
	public GameListAdapter(Context context) {
		this.context = context;
		apiManager = new ApiManager(context);
		games = new ArrayList<GameListItem>();
		trackListCallback = new CallbackInterface() {
			
			@Override
			public void onObjectResponseReturned(String response) {}
			
			@Override
			public void onErrorResponseReturned(String response) {
				System.out.println("Problem contacting server: " + response);
			}
			
			@Override
			public void onArrayResponseReturned(String response) {
				games = new ArrayList<GameListItem>();
				try {
					JSONArray gameList = new JSONArray(response);
					for (int i=0; i<gameList.length(); i++) {
						games.add(new GameListItem(gameList.getJSONObject(i).getString("name"), gameList.getJSONObject(i).getString("_id")));
					}
					notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	public void updateList() {
		apiManager.ApiCall("track/", ApiManager.GET, trackListCallback);
	}
	
	@Override
	public int getCount() {
		return games.size();
	}

	@Override
	public GameListItem getItem(int position) {
		return games.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_game_item, parent, false);
        TextView name = (TextView) row.findViewById(R.id.games_list_item_name);
        name.setText(getItem(position).name);
        
        return row;
	}

}
