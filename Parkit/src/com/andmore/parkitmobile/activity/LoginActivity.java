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
public class LoginActivity extends Activity implements OnClickListener {

	private EditText user, pass;
	private Button mSubmit, mRegister;

	private ProgressDialog pDialog;

	// Clase HttpHandler
	HttpHandler httpHandler = new HttpHandler();

	private static final String LOGIN_URL = "http://192.168.0.105:8088/ParkitServer/rest/userservice/getuser";
	//private static final String LOGIN_URL = "http://10.0.0.110:8088/ParkitServer/rest/userservice/getuser";
	
	// private static final String LOGIN_URL =
	// "http://jsonplaceholder.typicode.com/posts/1/comments";

	// La respuesta del JSON es
	private static final String TAG_ERROR = "Error";

	private SessionManager session;
	private SQLiteUserHandler dbUser;
	private SQLiteCCHandler dbCC;
	private SQLiteSeccionHandler dbSeccion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// setup input fields
		user = (EditText) findViewById(R.id.email);
		pass = (EditText) findViewById(R.id.password);

		// setup buttons
		mSubmit = (Button) findViewById(R.id.login);
		mRegister = (Button) findViewById(R.id.register_link);

		// register listeners
		mSubmit.setOnClickListener(this);
		mRegister.setOnClickListener(this);

		// SQLite database handler
		dbUser = new SQLiteUserHandler(getApplicationContext());
		dbCC = new SQLiteCCHandler(getApplicationContext());
		dbSeccion = new SQLiteSeccionHandler(getApplicationContext());
		// Session manager
		session = new SessionManager(getApplicationContext());

		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			 Intent intent = new Intent(LoginActivity.this,
			  ListParkingActivity.class);
			  finish();
			  startActivity(intent);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String username = user.getText().toString();
		String password = pass.getText().toString();
		switch (v.getId()) {
		
		case R.id.login:
			// Check for empty data in the form
			user.setError(null);
			pass.setError(null);
			boolean response = validateFields(username, password);
			if (!response) {
				new AttemptLogin().execute();
				break;

			} else {
				Toast.makeText(getApplicationContext(),
						"Se ha presentado un error, Por favor verifique la información ingresada.",
						Toast.LENGTH_LONG).show();
				break;
			}
		
		case R.id.register_link:
			Intent i = new Intent(this, RegisterActivity.class);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}
	}
	
	private boolean validateFields(String userEmail, String psw) {
		boolean response = false;

		if (userEmail.isEmpty()) {
			user.setError("Campo requerido!");
			response = true;
		} else {
			if (!userEmail.contains("@")) {
				user.setError("E-mail no valido!");
				response = true;
			}
		}
		if (psw.isEmpty()) {
			pass.setError("Campo requerido!");
			response = true;
		} else {
			if (psw.length() <= 4) {
				pass.setError("La contrasena debe contener mas de 4 digitos!");
				response = true;
			}
		}
		return response;
	}

	class AttemptLogin extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("login.........");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			String username = user.getText().toString();
			String password = pass.getText().toString();
			boolean success = true;
			try {
				// Building Parameters
				List params = new ArrayList();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));

				Log.d("request!", "starting");
				// getting product details by making HTTP request
				JSONObject json = httpHandler.makeHttpRequest(LOGIN_URL, "GET",
						params);

				// check your log for json response
				JSONArray centralComercial_array = json.getJSONArray("centroComercial");
				if (centralComercial_array != null) {
							for (int i = 0; i < centralComercial_array.length(); i++) {
								JSONObject ccObject = centralComercial_array.getJSONObject(i);
								String cc_id = ccObject.getString("id");
								String cc_name = ccObject.getString("nombre");
									if (cc_id.equals("0") && cc_name.equals("error")){
										success = false;
										Log.d("Is Login Successful: ", "false");
										break;
									}
							}
					if (success) {
						String msgLoginSuccess ="Bienvenido: ";
						Log.d("Login Successful!", "User: " + username);
						// Create login session
						session.setLogin(true);
						// Inserting row in users table
						dbUser.addUser(username, username, password);
						
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
											local.setSeccion(seccion.getId());
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
								Log.d("Error in the json process!", TAG_ERROR+e.getCause());
							}
						
						}
						 Intent i = new Intent(LoginActivity.this,
						 ListParkingActivity.class);
						 i.putExtra("parkingArray", centralComercial_array.toString());
						 finish();
						 startActivity(i);
						return msgLoginSuccess+username;
					} else {
						String msgLoginFail ="Usuario no registrado, por favor verifique sus datos.";
						Log.d("Login Failure!", TAG_ERROR);
						//publishProgress(msgLoginFail, msgLoginFail);
						return msgLoginFail;
					}

				} else {
					Log.d("Login Failure!", TAG_ERROR);
					return TAG_ERROR;
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Json error: " + e.getMessage(), Toast.LENGTH_LONG)
						.show();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG)
						.show();
			}
		}
		
		@Override
		protected void onProgressUpdate(String... values) {
		    if (values != null) {
		        for (String value : values) {
		            // shows a toast for every value we get
		        	Toast.makeText(
							getApplicationContext(),
							value,
							Toast.LENGTH_LONG).show();
		        }
		    }
		}
	}
}
