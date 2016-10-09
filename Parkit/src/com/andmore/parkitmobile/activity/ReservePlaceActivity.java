package com.andmore.parkitmobile.activity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

@SuppressWarnings("deprecation")
public class ReservePlaceActivity extends Activity implements OnClickListener {

	private static PayPalConfiguration config = new PayPalConfiguration()
			.environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
			.clientId(
					"AR1QrzCl--SsLB9mR3STDTtmVjBs8YDyrEN5TtEi1Reel5TmOh9QswJaX0SVShP8srS4XfzChRJ-r1ze");
	private EditText btnftDate, btnftTime, btnEndDate, btnEndTime;

	// Variable for storing current date and time
	private int mYear, mMonth, mDay, mHour, mMinute;
	private Calendar gralCalendar;
	private SimpleDateFormat gralFormatter;
	private AdView mAdView;
	private Button electPayment, cashPayment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reserve_place);

		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
		startService(intent);

		// buttons -EditText with action
		btnftDate = (EditText) findViewById(R.id.btnPickFtDate);
		btnftTime = (EditText) findViewById(R.id.btnPickFtTime);
		btnEndDate = (EditText) findViewById(R.id.btnPickEndDate);
		btnEndTime = (EditText) findViewById(R.id.btnPickEndTime);

		electPayment = (Button) findViewById(R.id.pay_elect_link);
		cashPayment = (Button) findViewById(R.id.pay_cash_link);

		gralCalendar = Calendar.getInstance();
		gralFormatter = new SimpleDateFormat("dd/MMM/yyyy");
		mYear = gralCalendar.get(Calendar.YEAR);
		mMonth = gralCalendar.get(Calendar.MONTH);
		mDay = gralCalendar.get(Calendar.DAY_OF_MONTH);

		// btnftDate.setText(gralFormatter.format(gralCalendar.getTime()));
		// btnftTime.setText(Calendar.HOUR_OF_DAY + ":" + Calendar.MINUTE);

		// register listeners
		btnftDate.setOnClickListener(this);
		btnftTime.setOnClickListener(this);
		btnEndDate.setOnClickListener(this);
		btnEndTime.setOnClickListener(this);
		electPayment.setOnClickListener(this);
		cashPayment.setOnClickListener(this);

		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.btnPickFtDate:

			btnftDate.setError(null);
			DatePickerDialog dpFrDate = new DatePickerDialog(this,
					AlertDialog.THEME_DEVICE_DEFAULT_DARK,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							Calendar datetime = Calendar.getInstance();
							datetime.set(Calendar.YEAR, year);
							datetime.set(Calendar.MONTH, monthOfYear);
							datetime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
							SimpleDateFormat formatter = new SimpleDateFormat(
									"dd/MMM/yyyy");
							btnftDate.setText(formatter.format(datetime
									.getTime()));
						}
					}, mYear, mMonth, mDay);
			dpFrDate.setTitle("Fecha ingreso");
			dpFrDate.show();
			break;

		case R.id.btnPickFtTime:

			btnftTime.setError(null);
			TimePickerDialog tpFtTime = new TimePickerDialog(this,
					AlertDialog.THEME_DEVICE_DEFAULT_DARK,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							Calendar datetime = Calendar.getInstance();
							datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
							datetime.set(Calendar.MINUTE, minute);
							SimpleDateFormat formatter = new SimpleDateFormat(
									"hh:mm a");

							String strHrsToShow = (datetime
									.get(Calendar.HOUR_OF_DAY) == 0) ? "12"
									: datetime.get(Calendar.HOUR_OF_DAY) + "";
							btnftTime.setText(formatter.format(datetime
									.getTime()));
						}
					}, mHour, mMinute, false);

			tpFtTime.setTitle("Hora Inicio");
			tpFtTime.show();
			break;

		case R.id.btnPickEndDate:

			btnEndDate.setError(null);
			DatePickerDialog dpEndDate = new DatePickerDialog(this,
					AlertDialog.THEME_DEVICE_DEFAULT_DARK,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							Calendar datetime = Calendar.getInstance();
							datetime.set(Calendar.YEAR, year);
							datetime.set(Calendar.MONTH, monthOfYear);
							datetime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
							SimpleDateFormat formatter = new SimpleDateFormat(
									"dd/MMM/yyyy");
							btnEndDate.setText(formatter.format(datetime
									.getTime()));
						}
					}, mYear, mMonth, mDay);
			dpEndDate.setTitle("Fecha salida");
			dpEndDate.show();

			break;

		case R.id.btnPickEndTime:

			btnEndTime.setError(null);
			TimePickerDialog tpEndTime = new TimePickerDialog(this,
					AlertDialog.THEME_DEVICE_DEFAULT_DARK,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							Calendar datetime = Calendar.getInstance();
							datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
							datetime.set(Calendar.MINUTE, minute);
							SimpleDateFormat formatter = new SimpleDateFormat(
									"hh:mm a");

							String strHrsToShow = (datetime
									.get(Calendar.HOUR_OF_DAY) == 0) ? "12"
									: datetime.get(Calendar.HOUR_OF_DAY) + "";
							btnEndTime.setText(strHrsToShow + ":"
									+ datetime.get(Calendar.MINUTE));
							btnEndTime.setText(formatter.format(datetime
									.getTime()));
						}
					}, mHour, mMinute, false);

			tpEndTime.setTitle("Hora Inicio");
			tpEndTime.show();

			break;

		case R.id.pay_elect_link:

			// PAYMENT_INTENT_SALE will cause the payment to complete
			// immediately.
			// Change PAYMENT_INTENT_SALE to
			// - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture
			// funds later.
			// - PAYMENT_INTENT_ORDER to create a payment for authorization and
			// capture
			// later via calls from your server.

			btnftDate.setError(null);
			btnEndDate.setError(null);
			btnftTime.setError(null);
			btnEndTime.setError(null);

			try {
				boolean response = validateFields();
				if (!response) {

					PayPalPayment payment = new PayPalPayment(new BigDecimal(
							"1.75"), "USD", "Parkit reserve place",
							PayPalPayment.PAYMENT_INTENT_SALE);

					Intent intent = new Intent(this, PaymentActivity.class);
					// send the same configuration for restart resiliency
					intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,
							config);
					intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
					startActivityForResult(intent, 0);

				} else {
					Toast.makeText(
							getApplicationContext(),
							"Se ha presentado un error, Por favor verifique la información ingresada.",
							Toast.LENGTH_LONG).show();

				}

			} catch (Exception e) {
				Log.e("validation",
						"an extremely unlikely failure occurred -  validating fields: ",
						e);
			}

			break;

		case R.id.pay_cash_link:
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reserve_place, menu);
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

	@Override
	public void onPause() {
		if (mAdView != null) {
			mAdView.pause();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdView != null) {
			mAdView.resume();
		}
	}

	@Override
	public void onDestroy() {
		if (mAdView != null) {
			mAdView.destroy();
		}
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			PaymentConfirmation confirm = data
					.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			if (confirm != null) {
				try {
					Log.i("paymentExample", confirm.toJSONObject().toString(4));
					JSONObject jsPaymentResponse = confirm.toJSONObject();
					JSONObject jsReponse = jsPaymentResponse
							.getJSONObject("response");

					String js_id = jsReponse.getString("id");
					String js_create_time = jsReponse.getString("create_time");
					String js_intent = jsReponse.getString("intent");
					String js_state = jsReponse.getString("state");

					if (js_state.equals("approved") && js_intent.equals("sale")) {
						Log.i("payment successful: ", "true");
						// TODO: send 'confirm' to your server for verification.
						// see
						// https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
						// for more details.
						Toast.makeText(getApplicationContext(),
								"Pagamento realizado con exito.",
								Toast.LENGTH_LONG).show();
						Intent intConf = new Intent(ReservePlaceActivity.this,
								ConfirmReserveActivity.class);
						finish();
						startActivity(intConf);
					} else {
						Toast.makeText(
								getApplicationContext(),
								"El Pagamento no fue efectuado, por favor verifique las informaciones ingresadas.",
								Toast.LENGTH_LONG).show();
					}

				} catch (JSONException e) {
					Log.e("paymentExample",
							"an extremely unlikely failure occurred: ", e);
				}
			}
		} else if (resultCode == Activity.RESULT_CANCELED) {
			Log.i("paymentExample", "The user canceled.");
		} else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
			Log.i("paymentExample",
					"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
		}
	}

	@SuppressLint("SimpleDateFormat")
	private boolean validateFields() throws ParseException {
		boolean retorno = false;
		String ftDate = btnftDate.getText().toString();
		String endDate = btnEndDate.getText().toString();
		String ftTime = btnftTime.getText().toString();
		String endTime = btnEndTime.getText().toString();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");

		Date currentDate = new Date();
		String strCurrentDate = formatter.format(currentDate);
		Date frmCurrentDate = formatter.parse(strCurrentDate);

		// firstDate
		if (ftDate.isEmpty()) {
			btnftDate.setError("Campo requerido!");
			retorno = true;
		} else {
			Date dateFirst = formatter.parse(ftDate);
			if (dateFirst.before(frmCurrentDate)
					&& !dateFirst.equals(frmCurrentDate)) {
				btnftDate.setError("fecha menor que la actual.");
				retorno = true;
			}
		}

		// EndDate
		if (endDate.isEmpty()) {
			btnEndDate.setError("Campo requerido!");
			retorno = true;
		} else {
			Date dateEnd = formatter.parse(endDate);
			if (dateEnd.before(frmCurrentDate)
					&& !dateEnd.equals(frmCurrentDate)) {
				btnEndDate.setError("fecha menor que la actual.");
				retorno = true;
			}
		}

		if (!ftDate.isEmpty() && !endDate.isEmpty()) {
			Date dateFirst = formatter.parse(ftDate);
			Date dateEnd = formatter.parse(endDate);

			if ((!dateFirst.before(dateEnd)) && (!dateFirst.equals(dateEnd))) {
				retorno = true;
			}
			// TODO poner la validacion del horario del shooping

		}

		// Time
		if (ftTime.isEmpty()) {
			retorno = true;
			btnftTime.setError("Campo requerido!");
		}

		if (endTime.isEmpty()) {
			retorno = true;
			btnEndTime.setError("Campo requerido!");

		}

		SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");
		Date formFtTime = formatTime.parse(ftTime);
		Date formEndTime = formatTime.parse(endTime);

		if (!ftTime.isEmpty() && !endTime.isEmpty()) {
			if (formFtTime.after(formEndTime) && (!formEndTime.equals(formFtTime))) {
				retorno = true;
			}
			// TODO poner la validacion del horario del shooping
		}

		return retorno;

	}
}
