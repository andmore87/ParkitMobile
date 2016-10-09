package com.andmore.parkitmobile.activity;

import java.util.EnumMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andmore.parkitmobile.background.service.ExpiredPreReserveReceiver;
import com.andmore.parkitmobile.background.service.LocationService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

@SuppressLint("NewApi")
public class ConfirmReserveActivity extends ActionBarActivity implements
		OnClickListener {

	CheckBox checkPrintTiquet;
	private AdView mAdView;
	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;
	private static final String NAMEPOPUP = "Codigo de Ingreso y Salida";
	private ImageButton btnFindCar;
	private final static String TAG = "ConfirmReserveActivity";
	public final static String PREFS = "PrefsFile";
	private SharedPreferences settings = null;
	private SharedPreferences.Editor editor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_reserve);

		btnFindCar = (ImageButton) findViewById(R.id.img_findcar);
		btnFindCar.setOnClickListener(this);

		addListenerOncheckPrintTiquet();
		
		 // Save time of run:
	    settings = getSharedPreferences(PREFS, MODE_PRIVATE);
	    editor = settings.edit();
	    
	 // First time running app?
	    //if (!settings.contains("lastRun")){
	      if (settings.getBoolean("enabled", true)){
	    //inicia sempre que a tela seja executada
	        enableNotification(null);
	      
	    } else{
	  	recordRunTime();
	    }
	
	    //Log.v(TAG, "Starting ExpiredPreReserveService...");
	    //startService(new Intent(this,  ExpiredPreReserveService.class));
	    
	    Log.v(TAG, "Starting LocationService...");
	    startService(new Intent(this,  LocationService.class));
		//callNotification();
	    
	    //enableNotification(null);
	}
	
	public void recordRunTime() {
	    editor.putLong("lastRun", System.currentTimeMillis());
	    editor.commit();        
	}
	
	public void enableNotification(View v) {
	    editor.putLong("lastRun", System.currentTimeMillis());
	    editor.putBoolean("enabled", true);  
	    editor.putLong("countTry", 0);
	    editor.putBoolean("carIn", false); 
	    editor.commit();        
	    Log.v(TAG, "Notifications enabled");
	}

	public void disableNotification(View v) {
	    editor.putBoolean("enabled", false);                
	    editor.commit();        
	    Log.v(TAG, "Notifications disabled");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.confirm_reserve, menu);
		return true;
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.img_findcar:
			//Intent i = new Intent(this, FindCarActivity.class);
			Intent i = new Intent(this, CarLocationActivity.class);
			startActivity(i);
			break;
		default:
			break;

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuconfirm_listparking:
			Toast.makeText(ConfirmReserveActivity.this, "Bookmark is Selected",
					Toast.LENGTH_SHORT).show();
			return true;

		case R.id.menuconfirm_logout:
			Toast.makeText(ConfirmReserveActivity.this, "Bookmark is Selected",
					Toast.LENGTH_SHORT).show();
			return true;

		case R.id.menuconfirm_about:
			Toast.makeText(ConfirmReserveActivity.this, "Bookmark is Selected",
					Toast.LENGTH_SHORT).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void addListenerOncheckPrintTiquet() {

		checkPrintTiquet = (CheckBox) findViewById(R.id.chkPrintTiquet);
		checkPrintTiquet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					showPopupPrintTiquet();
				}
			}
		});
	}

	private void showPopupPrintTiquet() {
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.alertdialg_confirm_reserve,
				null);

		mAdView = (AdView) dialogView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		TextView txtinfo = (TextView) dialogView
				.findViewById(R.id.alertconfirm_Txtinfo);
		txtinfo.setText("Presente este codigo al momento de ingresar y salir del parqueadero");
		// barcode data
		String barcode_data = "123456";
		createBarcode(barcode_data, dialogView);

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(NAMEPOPUP);
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.setView(dialogView);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alertDialog.show();

	}

	private void createBarcode(String barcode_data, View dialogView) {

		// barcode image
		Bitmap bitmap = null;
		ImageView alertconfirm_code = (ImageView) dialogView
				.findViewById(R.id.alertconfirm_code);

		try {
			bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600,
					300);
			alertconfirm_code.setImageBitmap(bitmap);

		} catch (WriterException e) {
			e.printStackTrace();
		}

		// barcode text
		TextView tv = new TextView(this);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setText(barcode_data);
	}

	private Bitmap encodeAsBitmap(String contents, BarcodeFormat format,
			int img_width, int img_height) throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contentsToEncode);
		if (encoding != null) {
			hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result;
		try {
			result = writer.encode(contentsToEncode, format, img_width,
					img_height, hints);
		} catch (IllegalArgumentException iae) {
			// Unsupported format
			return null;
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// preventing default implementation previous to
			// android.os.Build.VERSION_CODES.ECLAIR
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@SuppressLint("NewApi")
	private void callNotification(){
		
		/*AlarmManager alarmManager=(AlarmManager) this.getApplicationContext().getSystemService(this.getApplicationContext().ALARM_SERVICE);
		Intent intent = new Intent(this.getApplicationContext(), ExpiredPreReserveReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast( this.getApplicationContext(), 0, intent, 0);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),30000,pendingIntent);
		*/
		//startService(new Intent(this, UpdaterServiceManager.class));
		   //Intent myIntent = new Intent(ConfirmReserveActivity.this , ExpiredPreReserveReceiver.class);     
	       //AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	       //PendingIntent pendingIntent = PendingIntent.getService(ConfirmReserveActivity.this, 0, myIntent, 0);
	       //Calendar calendar = Calendar.getInstance();
	      // calendar.set(Calendar.HOUR_OF_DAY, 12);
	       //calendar.set(Calendar.MINUTE, 00);
	      // calendar.set(Calendar.SECOND, 00);
	     // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 30000 , pendingIntent);  //set repeating every 24 hours
	       
	      
	        
	      // define sound URI, the sound to be played when there's a notification

	        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	        // intent triggered, you can add other intent for other actions
	        Intent intent = new Intent(ConfirmReserveActivity.this, ExpiredPreReserveReceiver.class);
	        PendingIntent pIntent = PendingIntent.getActivity(ConfirmReserveActivity.this, 0, intent, 0);
	        // this is it, we'll build the notification!
	        // in the addAction method, if you don't want any icon, just set the first param to 0
	        Notification mNotification = new Notification.Builder(this)
	            .setContentTitle("New Post andmore!")
	            .setContentText("Here's an awesome update for you andmore!")
	            .setSmallIcon(R.drawable.ic_launcher)
	            .setContentIntent(pIntent)
	            .setSound(soundUri)
	            .addAction(R.drawable.ic_launcher, "View", pIntent)
	            .addAction(0, "Remind", pIntent)
	            .build();
	        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        // If you want to hide the notification after it was selected, do the code below
	        // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
	        notificationManager.notify(0, mNotification);

	      
	      
		                                                                      
	}
}
