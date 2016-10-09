package com.andmore.parkitmobile.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.andmore.parkitmobile.repository.SessionManager;

public class SplashActivity extends Activity {

	// Set the duration of the splash screen
	private static final long SPLASH_SCREEN_DELAY = 3000;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set portrait orientation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// Hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_splash);

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				
				session = new SessionManager(getApplicationContext());

				if (session.isLoggedIn()) {
					// User is already logged in. Take him to main activity
					 Intent intent = new Intent(SplashActivity.this,
					 ListParkingActivity.class);
					 startActivity(intent);
					 
				}else{
					// Start the login activity
					Intent localIntent2 = new Intent().setClass(
							SplashActivity.this, LoginActivity.class);
					SplashActivity.this.startActivity(localIntent2);
				}
							// Close the activity so the user won't able to go back this
				// activity pressing Back button
				finish();
			}
		};

		// Simulate a long loading process on application startup.
		Timer timer = new Timer();
		timer.schedule(task, SPLASH_SCREEN_DELAY);
	}
}
