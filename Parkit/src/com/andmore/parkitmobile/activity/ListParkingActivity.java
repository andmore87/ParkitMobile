package com.andmore.parkitmobile.activity;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.andmore.parkitmobile.entity.Centro_Comercial;
import com.andmore.parkitmobile.image.util.LazyAdapter;
import com.andmore.parkitmobile.repository.SQLiteCCHandler;
import com.andmore.parkitmobile.repository.SQLiteUserHandler;
import com.andmore.parkitmobile.repository.SessionManager;

public class ListParkingActivity extends Activity {

    
    ListView list;
    LazyAdapter adapter;
    public static final String KEY_NAME = "name"; // parent node
	static final String KEY_ID = "id";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_SCHEDULE = "schedule";
	public static final String KEY_PHOTO = "photo";
	public List<Centro_Comercial> parkingList;
	private SQLiteUserHandler dbUser;
	private SQLiteCCHandler dbCC;
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_parking);
        dbUser = new SQLiteUserHandler(getApplicationContext());
        dbCC = new SQLiteCCHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        
        parkingList = new ArrayList<Centro_Comercial>();
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	parkingList =  loadParkingInfo(extras.getString("parkingArray"));
        }else{
        	parkingList = dbCC.getAllCentralComercials();
        }
        
        list=(ListView)findViewById(R.id.list);
        adapter=new LazyAdapter(this, parkingList);
        //adapter=new LazyAdapter(this, mStrings);
        list.setAdapter(adapter);
        adapter.imageLoader.clearCache();
        adapter.notifyDataSetChanged(); 

        
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

    		@Override
    		public void onItemClick(AdapterView<?> parent, View view,
    				int position, long id) {
    						
    			Log.d("item selected", position+": "+id);
    			 Intent i = new Intent(ListParkingActivity.this, DetailParkingActivity.class);
				 i.putExtra("parkitId", id);
				 startActivity(i);
    		}
    	});
        
    }
    
    @Override
    public void onDestroy()
    {
        list.setAdapter(null);
        super.onDestroy();
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        
        MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.list_parking, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        
    	switch (item.getItemId()) {  	
        case R.id.about:
        //startActivity(new Intent(this, About.class));
        return true;
        case R.id.logout:
         logout();
        return true;
        default:
        return super.onOptionsItemSelected(item);
    }
     
    }
    
    
    private List<Centro_Comercial> loadParkingInfo(String parkArray){
    	List<Centro_Comercial> parkingList = new ArrayList<Centro_Comercial>();
    	
    	try {
		JSONArray localJSONArray = new JSONArray(parkArray);
		Centro_Comercial centro_comercial =null;
		for (int i = 0; i < localJSONArray.length(); i++) {
			JSONObject ccObject = localJSONArray.getJSONObject(i);
			centro_comercial = new Centro_Comercial();
			centro_comercial.setId(Integer.parseInt(ccObject.getString("id")));
			centro_comercial.setNombre(ccObject.getString("nombre"));
			centro_comercial.setDireccion(ccObject.getString("direccion"));
			centro_comercial.setTelefono(ccObject.getString("telefono"));
			centro_comercial.setHorario(ccObject.getString("horario"));
			centro_comercial.setFoto(ccObject.getString("foto"));
			centro_comercial.setWeb(ccObject.getString("web"));
			centro_comercial.setFacebook(ccObject.getString("facebook"));
			centro_comercial.setTwitter(ccObject.getString("twitter"));
			centro_comercial.setInstagram(ccObject.getString("instagram"));
			
			parkingList.add(centro_comercial);
			}
		
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		Log.d("Error getting parking information on listParking",e.getMessage());
	}
    
    return parkingList;
    }
 
    private void logout() {
        session.setLogin(false);
        dbUser.deleteUsers();
        dbCC.deleteParkings();
        // Launching the login activity
        Intent intent = new Intent(ListParkingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    
    
    
}
