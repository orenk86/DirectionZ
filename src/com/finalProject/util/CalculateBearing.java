package com.finalProject.util;


public class CalculateBearing {

//	static public double initial(double lat1, double long1, double lat2, double long2) {
//		return (_bearing(lat1, long1, lat2, long2) + 360.0) % 360;
//	}
//
//	static private double _bearing(double lat1, double long1, double lat2, double long2) {
//		double degToRad = Math.PI / 180.0;
//		double phi1 = lat1 * degToRad;
//		double phi2 = lat2 * degToRad;
//		double lam1 = long1 * degToRad;
//		double lam2 = long2 * degToRad;
//
//		return Math.atan2(Math.sin(lam2 - lam1) * Math.cos(phi2),
//				Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(lam2 - lam1))
//				* 180 / Math.PI;
//	}
	
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
