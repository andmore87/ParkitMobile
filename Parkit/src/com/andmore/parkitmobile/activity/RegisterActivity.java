package com.andmore.parkitmobile.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andmore.parkitmobile.connection.HttpHandler;
import com.andmore.parkitmobile.entity.Centro_Comercial;
import com.andmore.parkitmobile.entity.Local;
import com.andmore.parkitmobile.entity.Nodo;
import com.andmore.parkitmobile.entity.Seccion;
import com.andmore.parkitmobile.repository.SQLiteCCHandler;
import com.andmore.parkitmobile.repository.SQLiteSeccionHandler;
import com.andmore.parkitmobile.repository.SQLiteUserHandler;
import com.andmore.parkitmobile.repository.SessionManager;
import com.google.gson.Gson;

@SuppressLint("NewApi")
public class RegisterActivity extends Activity implements OnClickListener {
	private EditText inputFullName, inputLastName;
	private EditText inputEmail, inputPlate;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	private Button bRegister, bBackLogin;
	private SessionManager session;
	private SQLiteUserHandler dbUser;
	private SQLiteCCHandler dbCC;
	private SQLiteSeccionHandler dbSeccion;

	// Clase HttpHandler
	HttpHandler httpHandler = new HttpHandler();
	private static final String SAVEUSER_URL = "http://192.168.0.105:8088/ParkitServer/rest/userservice/saveuser";
	//private static final String SAVEUSER_URL = "http://10.0.0.110:8088/ParkitServer/rest/userservice/saveuser";
	
	// La respuesta del JSON es
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ERROR = "Error";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		inputFullName = (EditText) findViewById(R.id.name);
		inputLastName = (EditText) findViewById(R.id.lastName);
		inputEmail = (EditText) findViewById(R.id.email);
		inputPlate = (EditText) findViewById(R.id.plate);
		inputPassword = (EditText) findViewById(R.id.password);
		bRegister = (Button) findViewById(R.id.btnRegister);
		bBackLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
		// Progress dialog
		// pDialog = new ProgressDialog(this);
		// pDialog.setCancelable(false);

		// register listeners
		bRegister.setOnClickListener(this);
		bBackLogin.setOnClickListener(this);

		// http://192.168.1.5:8088/ParkitServer/rest/userservice/saveuser?name=Sara&lastName=Jesihar&email=sara%40gmail.com&password=pika&username=sara%40gmail.com&plate=Hdj233&descuentos=0&notificaciones=1&role=1
		// Session manager
		session = new SessionManager(getApplicationContext());

		// SQLite database handler
		dbUser = new SQLiteUserHandler(getApplicationContext());
		dbCC = new SQLiteCCHandler(getApplicationContext());
		dbSeccion = new SQLiteSeccionHandler(getApplicationContext());

		// Check if user is already logged in or not
		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(RegisterActivity.this,
					ListParkingActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String name = inputFullName.getText().toString().trim();
		String lastName = inputLastName.getText().toString().trim();
		String email = inputEmail.getText().toString().trim();
		String password = inputPassword.getText().toString().trim();
		String plate = inputPlate.getText().toString().trim();

		inputFullName.setError(null);
		inputLastName.setError(null);
		inputEmail.setError(null);
		inputPassword.setError(null);
		inputPlate.setError(null);

		switch (v.getId()) {
		case R.id.btnRegister:
			boolean response = validateFields(name, email, password, lastName,
					plate);
			if (!response) {
				new AttemptLogin().execute();
				break;

			} else {
				Toast.makeText(
						getApplicationContext(),
						"Se ha presentado un error, Por favor verifique la información ingresada.",
						Toast.LENGTH_LONG).show();
				break;
			}
		case R.id.btnLinkToLoginScreen:
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}

	private boolean validateFields(String name, String userEmail, String psw,
			String lastName, String plate) {
		boolean response = false;

		if (name.isEmpty()) {
			inputFullName.setError("Campo requerido!");
			response = true;
		}

		// userEmail
		if (userEmail.isEmpty()) {
			inputEmail.setError("Campo requerido!");
			response = true;
		} else {
			if (!userEmail.contains("@")) {
				inputEmail.setError("E-mail no valido!");
				response = true;
			}
		}
		// password
		if (psw.isEmpty()) {
			inputPassword.setError("Campo requerido!");
			response = true;
		} else {
			if (psw.length() <= 4) {
				inputPassword
						.setError("La contrasena debe contener mas de 4 digitos!");
				response = true;
			}
		}
		// lastName
		if (lastName.isEmpty()) {
			inputLastName.setError("Campo requerido!");
			response = true;
		}
		// plate
		if (plate.isEmpty()) {
			inputPlate.setError("Campo requerido!");
			response = true;
		} else {
			if (plate.length() < 7) {
				inputPlate.setError("Placa no valida!");
				response = true;
			}
		}

		return response;
	}

	class AttemptLogin extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity.this);
			pDialog.setMessage("Registrando......");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			boolean success = true;
			String name = inputFullName.getText().toString().trim();
			String lastName = inputLastName.getText().toString().trim();
			String email = inputEmail.getText().toString().trim();
			String password = inputPassword.getText().toString().trim();
			String plate = inputPlate.getText().toString().trim();

			try {
				// Building Parameters
				List params = new ArrayList();
				params.add(new BasicNameValuePair("name", name));
				params.add(new BasicNameValuePair("lastName", lastName));
				params.add(new BasicNameValuePair("email", email));
				params.add(new BasicNameValuePair("password", password));
				params.add(new BasicNameValuePair("username", email));
				params.add(new BasicNameValuePair("plate", plate));
				params.add(new BasicNameValuePair("descuentos", "0"));
				params.add(new BasicNameValuePair("notificaciones", "1"));
				params.add(new BasicNameValuePair("role", "1"));

				Log.d("request!", "starting");
				// getting product details by making HTTP request
				JSONObject json = httpHandler.makeHttpRequest(SAVEUSER_URL,
						"GET", params);
				Log.d("Login attempt", json.toString());

				// check your log for json response
				JSONArray centralComercial_array = json
						.getJSONArray("centroComercial");
				if (centralComercial_array != null) {
					if (centralComercial_array.length() <= 1) {
						for (int i = 0; i < centralComercial_array.length(); i++) {
							JSONObject ccObject = centralComercial_array
									.getJSONObject(i);
							String cc_id = ccObject.getString("id");
							String cc_name = ccObject.getString("nombre");
							if (cc_id.equals("0") && cc_name.equals("error")) {
								success = false;
								Log.d("Is Login Successful: ", "false");
								break;
							}
						}
					}

					if (success) {
						Log.d("Login Successful!", "User: " + email);
						// Create login session
						session.setLogin(true);
						// Inserting row in users and central comercial tables
						dbUser.addUser(name, email, password);
						
						for (int i = 0; i < centralComercial_array.length(); i++) {
						JSONObject ccObject = centralComercial_array.getJSONObject(i);
						try {
						
						//CENTRO COMERCIAL
						Centro_Comercial centerComercial =  new Gson().fromJson(ccObject.toString(), Centro_Comercial.class);
						Log.d("centro comercial name", centerComercial.getNombre());
						dbCC.addCentralComercial(centerComercial);
						
						//SECCION
						//List<Seccion> ListSeccion = centerComercial.getSeccion();
						JSONArray jsonSeccion =  ccObject.getJSONArray("seccion");
						
						if(jsonSeccion!= null){
						
							for (int isec = 0; isec < jsonSeccion.length(); isec++) {
								JSONObject seccionObject = jsonSeccion.getJSONObject(isec);
								
								if(seccionObject!= null){
								//SECCION		
									Seccion seccion =  new Gson().fromJson(seccionObject.toString(), Seccion.class);
									seccion.setCentro_comercial_id(centerComercial.getId());
									dbSeccion.addSeccion(seccion);
									Log.d("seccion ID: ", seccion.getId()+"");
									Log.d("seccion name: ", seccion.getNombre());
									Log.d("seccion ccID: ", seccion.getCentro_comercial_id()+"");
								
								//NODO
								try {
									JSONArray jsonArrayNodo = seccionObject.getJSONArray("nodo");
									if(jsonArrayNodo!= null){
										for (int jnodo = 0; jnodo < jsonArrayNodo.length(); jnodo++) {
											JSONObject nodoObject = jsonArrayNodo.getJSONObject(jnodo);
											Nodo nodo =  new Gson().fromJson(nodoObject.toString(), Nodo.class);
											nodo.setSeccion_id(seccion.getId());
											dbSeccion.addNodo(nodo);
											Log.d("nodo name: ", nodo.getNombre());
										}
									}
										
								} catch (Exception e) {
									Log.d( "without element: ", e.getMessage());
								}
								
								//LOCAL
								try {
									JSONArray jsonArrayLocal = seccionObject.getJSONArray("local");
									if(jsonArrayLocal!= null){
										for (int jlocal = 0; jlocal < jsonArrayLocal.length(); jlocal++) {
											JSONObject localObject = jsonArrayLocal.getJSONObject(jlocal);
											Local local =  new Gson().fromJson(localObject.toString(), Local.class);
											//local.setSeccion(seccion.getId());
											dbSeccion.addLocal(local);
											Log.d("local name: ", local.getNombre());
									 	}
									}
								
								} catch (Exception e) {
									Log.d( "without element: ", e.getMessage());
								}
								
							 }
					 		
						 	 }
				 		  }
						
							} catch (Exception e) {
								Log.d("Error in the json process!", TAG_ERROR+e.getMessage());
							}
						
						}
						
					
						Intent i = new Intent(RegisterActivity.this,
								ListParkingActivity.class);
						i.putExtra("parkingArray",
								centralComercial_array.toString());
						finish();
						startActivity(i);
						return TAG_SUCCESS;
					}
				} else {
					Log.d("Login Failure!", TAG_ERROR);
					return TAG_ERROR;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(RegisterActivity.this, file_url,
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
