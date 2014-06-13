package com.finalProject.datatype;

import java.util.ArrayList;

import com.google.android.gms.maps.model.Marker;

public class GameMarkerListDataType {

	public ArrayList<Marker> gameMarkers;

	/**
	 * @return the gameMarkers
	 */
	public ArrayList<Marker> getGameMarkers() {
		return gameMarkers;
	}

	/**
	 * @param gameMarkers
	 *            the gameMarkers to set
	 */
	public void setGameMarkers(ArrayList<Marker> gameMarkers) {
		this.gameMarkers = gameMarkers;
	}

}
