package com.finalProject.directionz;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashWindowActivity extends Activity {
	private final static int SPLASH_TIME_OUT = 2000;
	Animation rotateAnim;
	TextView loadingImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_window);
		loadingImage = (TextView) findViewById(R.id.locadingImage);
		rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);

		// Initialize a LoadViewTask object and call the execute() method
		new LoadViewTask().execute();

	}

	// To use the AsyncTask, it must be subclassed
	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			loadingImage.startAnimation(rotateAnim);
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				synchronized (this) {
					this.wait(SPLASH_TIME_OUT);
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * new Handler().postDelayed(new Runnable() {
			 * 
			 * @Override public void run() { } }, SPLASH_TIME_OUT);
			 */
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(SplashWindowActivity.this, WelcomeScreenActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);

			// close this activity
			finish();
		}

	}

}
