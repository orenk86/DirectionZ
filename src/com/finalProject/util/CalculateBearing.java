package com.finalProject.util;


public class CalculateBearing {
	
	public static double getBearing(double lat1, double lng1, double lat2, double lng2){
		// calculate bearing between 2 points
		double dLon = (lng2-lng1);
		double y = Math.sin(dLon) * Math.cos(lat2);
		double x = Math.cos(lat1)*Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
		double brng = Math.toDegrees((Math.atan2(y, x)));
		brng = (360 - ((brng + 360) % 360));
		return brng;
	}
}
