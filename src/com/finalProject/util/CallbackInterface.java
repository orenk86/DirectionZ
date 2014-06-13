package com.finalProject.util;

public interface CallbackInterface {
	public void onObjectResponseReturned(String response);
	public void onArrayResponseReturned(String response);
	public void onErrorResponseReturned(String response);
}
