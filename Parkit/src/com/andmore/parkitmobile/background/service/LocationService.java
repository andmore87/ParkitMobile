package com.andmore.parkitmobile.background.service;

import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.andmore.parkitmobile.activity.ConfirmReserveActivity;
import com.andmore.parkitmobile.activity.R;
import com.andmore.parkitmobile.maps.util.PolyUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

public class LocationService extends Service implements LocationListener {

	private final static String TAG = "CheckRecentPlay";
	private static Long MILLISECS_PER_DAY = 86400000L;
	private static Long MILLISECS_PER_MIN = 60000L;

	private static long delay = MILLISECS_PER_MIN * 3; // 3 minutes (for
														// testing)
	// private static long delay = MILLISECS_PER_DAY * 3; // 3 days

	// Define a request code to send to Google Play services
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private double currentLatitude;
	private double currentLongitude;

	LocationManager locationManager;
	Location location;
	String provider;
	SharedPreferences.Editor editor = null;
	SharedPreferences settings;

	@Override
	public void onCreate() {
		super.onCreate();

		Log.v(TAG, "location Service started");

		  settings = getSharedPreferences(
				ConfirmReserveActivity.PREFS, MODE_PRIVATE);
	
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			// get latitude and longitude of the location
			currentLongitude = location.getLongitude();
			currentLatitude = location.getLatitude();
			Log.v(TAG, "Longitud: " + currentLongitude);
			Log.v(TAG, "Latitud: " + currentLatitude);
			Location deviceLocation = new Location("newlocation");
			deviceLocation.setLatitude(-23.589996);// latitude del divece
			deviceLocation.setLongitude(-46.645390);// latitude del divece
			float distance = location.distanceTo(deviceLocation);// resultado en metros
			Log.v(TAG, "distancia: " + distance);
			
			if (settings.getBoolean("enabled", true)) {
				if (distance >= 0 && distance <= 4) {//10 metros lo ideal
					Toast.makeText(this, "The car is comming to the spot",Toast.LENGTH_LONG).show();
					editor = settings.edit();
					editor.putBoolean("enabled", false);
				 	editor.putBoolean("carIn", true);                
				    editor.commit();        
					
					Log.v(TAG, "Notifications disabled");

				} else {
					Toast.makeText(this, "trying.....",Toast.LENGTH_LONG).show();
					editor = settings.edit();
					editor.putLong("countTry", settings.getLong("countTry",Long.MAX_VALUE)+1);
					editor.commit();
					setAlarm();
				}

			} else {
				Log.i(TAG, "Notifications are disabled");
				cancelAlarm();
				Log.i(TAG, "Location Alarm canceled");
			     
			}
			
			if (settings.getBoolean("carIn", true)) {
				//cancel alarm
				cancelAlarm();
				Log.i(TAG, "Location Alarm canceled");
			}
			
			if ((settings.getBoolean("carIn", false)==false) && (settings.getLong("countTry",Long.MAX_VALUE) >= 4) ) {
				//enviar notificacion de tiempo espirado
				sendNotification();
				editor = settings.edit();
				editor.putBoolean("enabled", false);
			    editor.commit();
				cancelAlarm();
				Log.i(TAG, "Location Alarm canceled");
			}
			
			stopSelf();
			Log.v(TAG, "LocationService stopped");   

		} else {
			Log.v(TAG, "No provider");
		}

	}
	
 
	public void setAlarm() {

		Intent serviceIntent = new Intent(this, LocationService.class);
		PendingIntent pi = PendingIntent.getService(this, 131313,
				serviceIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 60); // first time
		long frequency= 60 * 1000; // in ms 
		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pi);  

		//am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pi);
		Log.v(TAG, "Alarm set");
		
		
		
	}
	
	public void cancelAlarm(){
		
		Intent intent = new Intent(this, LocationService.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 131313, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	 

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private LatLng findNearestPoint(LatLng test, List<LatLng> target) {
		double distance = -1;
		LatLng minimumDistancePoint = test;

		if (test == null || target == null) {
			return minimumDistancePoint;
		}

		for (int i = 0; i < target.size(); i++) {
			LatLng point = target.get(i);

			int segmentPoint = i + 1;
			if (segmentPoint >= target.size()) {
				segmentPoint = 0;
			}

			double currentDistance = PolyUtil.distanceToLine(test, point,
					target.get(segmentPoint));
			if (distance == -1 || currentDistance < distance) {
				distance = currentDistance;
				minimumDistancePoint = findNearestPoint(test, point,
						target.get(segmentPoint));
			}
		}

		return minimumDistancePoint;
	}

	/**
	 * Based on `distanceToLine` method from
	 * https://github.com/googlemaps/android
	 * -maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java
	 */
	private LatLng findNearestPoint(final LatLng p, final LatLng start,
			final LatLng end) {
		if (start.equals(end)) {
			return start;
		}

		final double s0lat = Math.toRadians(p.latitude);
		final double s0lng = Math.toRadians(p.longitude);
		final double s1lat = Math.toRadians(start.latitude);
		final double s1lng = Math.toRadians(start.longitude);
		final double s2lat = Math.toRadians(end.latitude);
		final double s2lng = Math.toRadians(end.longitude);

		double s2s1lat = s2lat - s1lat;
		double s2s1lng = s2lng - s1lng;
		final double u = ((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng)
				/ (s2s1lat * s2s1lat + s2s1lng * s2s1lng);
		if (u <= 0) {
			return start;
		}
		if (u >= 1) {
			return end;
		}

		return new LatLng(start.latitude
				+ (u * (end.latitude - start.latitude)), start.longitude
				+ (u * (end.longitude - start.longitude)));

	}

	@Override
	public void onLocationChanged(Location location) {
		currentLatitude = location.getLatitude();
		currentLongitude = location.getLongitude();

		Toast.makeText(this,
				currentLatitude + " WORKS " + currentLongitude + "",
				Toast.LENGTH_LONG).show();

	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
	}

	/** calculates the distance between two locations in MILES */
	private double distance(double lat1, double lng1, double lat2, double lng2) {

		double earthRadius = 3958.75; // in miles, change to 6371 for kilometers

		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);

		double sindLat = Math.sin(dLat / 2);
		double sindLng = Math.sin(dLng / 2);

		double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
				* Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2));

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		double dist = earthRadius * c;

		return dist;
	}

    public void sendNotification() {

        Intent mainIntent = new Intent(this, ConfirmReserveActivity.class);
        @SuppressWarnings("deprecation")
        Notification noti = new Notification.Builder(this)
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                              PendingIntent.FLAG_UPDATE_CURRENT))
            .setContentTitle("Parkit Reserva")
            .setContentText("El tiempo de espera de reserva, esta apunto de caducar!!")
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_launcher)
            .setTicker("Todavia no te localizamos en la plaza reservada, el tiempo de espera de reserva esta apunto de caducar!!!")
            .setWhen(System.currentTimeMillis())
            .getNotification();

        NotificationManager notificationManager
            = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(131315, noti);

        Log.v(TAG, "Notification sent");        
    }
}
