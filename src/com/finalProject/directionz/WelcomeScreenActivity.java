package com.finalProject.directionz;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

public class WelcomeScreenActivity extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	ArrayList<MarkerOptions> gameMarkers;
//	boolean isCreateGame = true;
	Fragment startFragment = new StartGameFragment();
	Fragment createFragment = new CreateGameFragment();
	Fragment joinFragment = new JoinGameFragment();
	Fragment playFragment = new MainGameFragment();
	GoogleMap map;

	/**
	 * @return the isCreateGame
	 */
//	public boolean isCreateGame() {
//		return isCreateGame;
//	}

	/**
	 * @param isCreateGame the isCreateGame to set
	 */
//	public void setCreateGame(boolean isCreateGame) {
//		this.isCreateGame = isCreateGame;
//	}

	/**
	 * @return the map
	 */
	public GoogleMap getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(GoogleMap map) {
		this.map = map;
	}

	/**
	 * @return the gameMarkers
	 */
	public ArrayList<MarkerOptions> getGameMarkers() {
		return gameMarkers;
	}

	/**
	 * @param gameMarkers
	 *            the gameMarkers to set
	 */
	public void setGameMarkers(ArrayList<MarkerOptions> gameMarkers) {
		this.gameMarkers = gameMarkers;
	}

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		switch (position) {
		case 0:
			fragmentManager.beginTransaction().replace(R.id.container, createFragment).commit();
			break;
		case 1:
			fragmentManager.beginTransaction().replace(R.id.container, startFragment).commit();
			break;
		case 2:
			fragmentManager.beginTransaction().replace(R.id.container, startFragment).commit();
			break;
		}
	}
	
	public void startMainGameFragment() {
		getSupportFragmentManager().beginTransaction().replace(R.id.container, playFragment).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_activity_create_game);
			break;
		case 2:
			mTitle = getString(R.string.title_activity_start_game);
			break;
		case 3:
			mTitle = getString(R.string.title_activity_join_game);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.welcome_screen, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WelcomeScreenActivity.this);
		alertDialogBuilder.setTitle("Exit");
		// set dialog message
		alertDialogBuilder.setMessage("Are you sure?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				finish();

			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				dialog.cancel();
			}
		});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
}
