package com.andmore.parkitmobile.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.andmore.parkitmobile.entity.Centro_Comercial;
import com.andmore.parkitmobile.repository.SQLiteCCHandler;

public class DetailParkingActivity extends Activity implements OnClickListener {

	private ImageButton imgPhone;
	private RatingBar ratingBar;
	private ImageButton btnPlaces, btnSched, btnMaps;
	private SQLiteCCHandler dbCC;
	private static String strPhone;
	private String globalAddress, globalCCName;
	private Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_parking);
		globalAddress ="";
		globalCCName="";
		TextView txtParkingName = (TextView) findViewById(R.id.txt_parkingname);
		TextView txtParkingAddress = (TextView) findViewById(R.id.txt_parkingaddress);
		
		TextView txtParkingCurrency = (TextView) findViewById(R.id.txt_currency);
		TextView txtParkingPrice = (TextView) findViewById(R.id.txt_price);
		TextView txtParkingPrice_cent = (TextView) findViewById(R.id.txt_price_cent);
		TextView txtParkingPriceTime = (TextView) findViewById(R.id.txt_pricetime);

		dbCC = new SQLiteCCHandler(getApplicationContext());

		Long parkitId =0L;
		extras = getIntent().getExtras();
		if (extras != null) {
			 parkitId = extras.getLong("parkitId");
		}
		Centro_Comercial ccObject = dbCC.getCentralComercial(parkitId.intValue());
		if (ccObject.getId() == parkitId.intValue()) {
			addListenerOnLogoParking(ccObject.getWeb());
			addListenerOnLogoWeb(ccObject.getWeb());
			addListenerOnLogoInstagram(ccObject.getInstagram());
			addListenerOnLogoFacebook(ccObject.getFacebook());
			addListenerOnLogoTwitter(ccObject.getTwitter());
			txtParkingName.setText(ccObject.getNombre());
			txtParkingAddress.setText(ccObject.getDireccion());
			 
			String[] precio = ccObject.getPrecio().split("-");
			txtParkingCurrency.setText(precio[0]);
			txtParkingPrice.setText(precio[1]);
			txtParkingPrice_cent.setText(precio[2]);
			txtParkingPriceTime.setText(precio[3]);
			globalAddress = ccObject.getDireccion();
			globalCCName = ccObject.getNombre();
			
			strPhone = ccObject.getTelefono();
		}

		ratingBar = (RatingBar) findViewById(R.id.rankingBar);
		addListenerOnTextviewPhone();

		btnPlaces = (ImageButton) findViewById(R.id.img_avail);
		btnPlaces.setOnClickListener(this);

		btnSched = (ImageButton) findViewById(R.id.img_sched);
		btnSched.setOnClickListener(this);

		btnMaps = (ImageButton) findViewById(R.id.img_maps);
		btnMaps.setOnClickListener(this);

		// String fontPath = "fonts/linowrite.ttf";
		String fontPath = "fonts/Touch.ttf";

		// text view label

		// Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		// Applying font
		txtParkingName.setTypeface(tf, Typeface.BOLD);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_parking, menu);
		return true;
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

	public void addListenerOnTextviewPhone() {

		imgPhone = (ImageButton) findViewById(R.id.img_telephone);
		imgPhone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + strPhone));
				callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(callIntent);
			}
		});

	}

	public void addListenerOnLogoParking(final String url) {

		ImageView img = (ImageView) findViewById(R.id.imageview_logoparking);
		img.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		});

	}

	public void addListenerOnLogoFacebook(final String url) {

		ImageView img = (ImageView) findViewById(R.id.img_face);
		img.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url));
				startActivity(browserIntent);
			}
		});

	}
	
	public void addListenerOnLogoInstagram(final String url) {

		ImageView img = (ImageView) findViewById(R.id.img_instagram);
		img.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url));
				startActivity(browserIntent);
			}
		});

	}
	
	public void addListenerOnLogoTwitter(final String url) {

		ImageView img = (ImageView) findViewById(R.id.img_twitter);
		img.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url));
				startActivity(browserIntent);
			}
		});

	}
	
	public void addListenerOnLogoWeb(final String url) {

		ImageView img = (ImageView) findViewById(R.id.img_website);
		img.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(url));
				startActivity(browserIntent);
			}
		});

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.img_avail:
			Long ccId =0L;
			if (extras != null) {
				ccId = extras.getLong("parkitId");
			}
			Intent i = new Intent(this, SectionDetailActivity.class);
			i.putExtra("ccId", ccId);
			startActivity(i);
			break;
		case R.id.img_sched:
			
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			View dialogView = inflater.inflate(R.layout.alertdialg_parkingsched, null);
			dialogBuilder.setView(dialogView);
			
			//elementos del layout
			SimpleDateFormat gralFormatter = new SimpleDateFormat("dd-MMM-yyyy");
			
			StringBuffer strSched = new StringBuffer();
			strSched.append("Lun a Sab 8:30 am - 11:30 pm\n");
			strSched.append("Dom y fest 8:30 am - 9:30 pm");
			TextView txtInfoSched = (TextView) dialogView.findViewById(R.id.alertdialTxtSched);
			txtInfoSched.setText(strSched.toString());
			
			Calendar cal = Calendar.getInstance();
			
			TextView txtInfoDate = (TextView) dialogView.findViewById(R.id.alertdialTxtDate);
			txtInfoDate.setText(new SimpleDateFormat("EEEE").format(cal.getTime())+" "+gralFormatter.format(Calendar.getInstance().getTime()));

			AlertDialog alertDialog = dialogBuilder.create();
			alertDialog.setTitle("Horarios");
			alertDialog.setIcon(R.drawable.icon_schedulerbig);
			//alertDialog.setView(lyContent);
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			alertDialog.show();

			break;
	
		case R.id.img_maps:
			//globalAddress = "Cl. 30a #8226, MedellÌn, Antioquia, Colombia";
			Geocoder geocoder;
			List<Address> addresses = new ArrayList<Address>();
			try {
				geocoder = new Geocoder(this);
			    addresses = geocoder.getFromLocationName(globalAddress,5);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String geoUri = "http://maps.google.com/maps?q=loc:" + addresses.get(0).getLatitude() + "," + addresses.get(0).getLongitude() + " ( "+globalCCName+" )";
			Uri gmmIntentUri = Uri.parse(geoUri);
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
			mapIntent.setPackage("com.google.android.apps.maps");
			if (mapIntent.resolveActivity(getPackageManager()) != null) {
			    startActivity(mapIntent);
			}
			
			//Intent itMaps = new Intent(this, DetailParkingMapActivity.class);
			//startActivity(itMaps);

			break;
		default:
			break;
		}
	}

}
