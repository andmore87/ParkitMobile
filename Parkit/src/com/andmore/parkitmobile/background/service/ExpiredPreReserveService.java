package com.andmore.parkitmobile.background.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.andmore.parkitmobile.activity.ConfirmReserveActivity;
import com.andmore.parkitmobile.activity.R;

 
public class ExpiredPreReserveService extends Service {
	
	 
    private final static String TAG = "CheckRecentPlay";
   // private static Long MILLISECS_PER_DAY = 86400000L;
    private static Long MILLISECS_PER_MIN = 60000L;

	private static long delay = MILLISECS_PER_MIN * 3;   // 3 minutes (for testing)
  //private static long delay = MILLISECS_PER_DAY * 3;   // 3 days
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		 
		super.onCreate();
        Log.v(TAG, "Service started");                
        SharedPreferences settings = getSharedPreferences(ConfirmReserveActivity.PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = null;
        

        // Are notifications enabled?
        if (settings.getBoolean("enabled", true)) {
            // Is it time for a notification?
            if (settings.getLong("lastRun", Long.MAX_VALUE) < System.currentTimeMillis() - delay){
            	 sendNotification();
                 editor = settings.edit();
                 editor.putBoolean("enabled", false);                
         	       editor.commit();        
         	       Log.v(TAG, "Notifications disabled");
            	
            }else{
            	 setAlarm();
            }
   
        } else {        
            Log.i(TAG, "Notifications are disabled");
        }

        // Set an alarm for the next time this service should run:
       // setAlarm();

        Log.v(TAG, "Service stopped");        
        stopSelf();
        
    }
	
	
    public void setAlarm() {

        Intent serviceIntent = new Intent(this, ExpiredPreReserveService.class);
        PendingIntent pi = PendingIntent.getService(this, 131313, serviceIntent,
                                                    PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pi);
        Log.v(TAG, "Alarm set");        
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
 
	 
	@Override
	public void onDestroy() {
         super.onDestroy();
	}
 
}
