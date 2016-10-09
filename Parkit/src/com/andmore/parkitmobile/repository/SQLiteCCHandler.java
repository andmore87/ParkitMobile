package com.andmore.parkitmobile.repository;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.andmore.parkitmobile.entity.Centro_Comercial;
public class SQLiteCCHandler extends SQLiteOpenHelper {
	
	private static final String TAG = SQLiteCCHandler.class.getSimpleName();
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "parkit";
 
    // Login table name
    private static final String TABLE_CC = "central_comercial";
 
    // Login Table user Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_SCHEDULE = "schedule";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_WEB = "web";
    private static final String KEY_FACEBOOK = "facebook";
    private static final String KEY_TWITTER = "twitter";
    private static final String KEY_INSTAGRAM = "instagram";
    private static final String KEY_PRECIO = "precio";
    
   
 
    public SQLiteCCHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_CC + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT," + KEY_SCHEDULE + " TEXT,"
                + KEY_PHONE + " TEXT," + KEY_PHOTO + " TEXT,"
                + KEY_WEB + " TEXT," + KEY_FACEBOOK + " TEXT,"
                + KEY_TWITTER + " TEXT," 
                + KEY_INSTAGRAM + " TEXT,"
                + KEY_PRECIO + " TEXT"
                + ")";
        
       
        
        db.execSQL(CREATE_LOGIN_TABLE);
 
        Log.d(TAG, "Database tables created");
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CC);
        // Create tables again
        onCreate(db);
    }
 
    /**
     * Storing user details in database
     * */
    public void addCentralComercial(Centro_Comercial centroComercial) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, centroComercial.getId());
        values.put(KEY_NAME, centroComercial.getNombre()); // Name
        values.put(KEY_ADDRESS, centroComercial.getDireccion()); // Address
        values.put(KEY_SCHEDULE, centroComercial.getHorario()); // Schedule
        values.put(KEY_PHONE, centroComercial.getTelefono()); // Email
        values.put(KEY_PHOTO, centroComercial.getFoto()); // Created At
        values.put(KEY_WEB, centroComercial.getWeb()); // Created At
        values.put(KEY_FACEBOOK, centroComercial.getFacebook()); // Created At
        values.put(KEY_TWITTER, centroComercial.getTwitter()); // Created At
        values.put(KEY_INSTAGRAM, centroComercial.getInstagram()); // Created At
        values.put(KEY_PRECIO, centroComercial.getPrecio()); // Created At
  
        // Inserting Row
        long id = db.insert(TABLE_CC, null, values);
        db.close(); // Closing database connection
 
        Log.d(TAG, "New central comercial inserted into parkit: " + id);
    }
 
    /**
     * Getting parking data from database
     * */
    public List<Centro_Comercial> getCCDetails() {
        List<Centro_Comercial> parkingList = new ArrayList<Centro_Comercial>();
        String selectQuery = "SELECT  * FROM " + TABLE_CC;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        //cursor.moveToFirst();
        if (cursor.moveToFirst()) {
        	do{
        	Centro_Comercial objccParking = new Centro_Comercial();
            objccParking.setId(cursor.getInt(0));
            objccParking.setNombre(cursor.getString(1));
            objccParking.setDireccion(cursor.getString(2));
            objccParking.setHorario(cursor.getString(3));
            objccParking.setTelefono(cursor.getString(4)); 
            objccParking.setFoto(cursor.getString(5));
            objccParking.setWeb(cursor.getString(6));
            objccParking.setFacebook(cursor.getString(7));
            objccParking.setTwitter(cursor.getString(8));
            objccParking.setInstagram(cursor.getString(9));
            objccParking.setPrecio(cursor.getString(10));
            parkingList.add(objccParking);
        	}while(cursor.moveToNext());
            
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching ccParking from Sqlite: " + parkingList.toString());
 
        return parkingList;
    }
 
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteParkings() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_CC, null, null);
        db.close();
 
        Log.d(TAG, "Deleted all user info from sqlite");
    }
    
    /**
     * Get all information about Central comercial table
     * */
    public List<Centro_Comercial> getAllCentralComercials() {
       List<Centro_Comercial> centralComercialList = new ArrayList<Centro_Comercial>();
       // Select All Query
       String selectQuery = "SELECT  * FROM " + TABLE_CC;
    
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (cursor.moveToFirst()) {
           do {
        	   Centro_Comercial centralComercial = new Centro_Comercial();
        	   centralComercial.setId(Integer.parseInt(cursor.getString(0)));
        	   centralComercial.setNombre(cursor.getString(1));
        	   centralComercial.setDireccion(cursor.getString(2));
        	   centralComercial.setHorario(cursor.getString(3));
        	   centralComercial.setTelefono(cursor.getString(4));
        	   centralComercial.setWeb(cursor.getString(5));
        	   centralComercial.setFacebook(cursor.getString(6));
        	   centralComercial.setTwitter(cursor.getString(7));
        	   centralComercial.setInstagram(cursor.getString(8));
        	   centralComercial.setPrecio(cursor.getString(9));
               // Adding contact to list
               centralComercialList.add(centralComercial);
           } while (cursor.moveToNext());
       }
    
       // return contact list
       return centralComercialList;
   }
    
    
    public Centro_Comercial getCentralComercial(int centralComercialId) {
        Centro_Comercial centralComercial = new Centro_Comercial();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CC +" WHERE "+ KEY_ID +"="+centralComercialId;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
         	 
         	   centralComercial.setId(Integer.parseInt(cursor.getString(0)));
         	   centralComercial.setNombre(cursor.getString(1));
         	   centralComercial.setDireccion(cursor.getString(2));
         	   centralComercial.setHorario(cursor.getString(3));
         	   centralComercial.setTelefono(cursor.getString(4));
         	  centralComercial.setFoto(cursor.getString(5));
         	   centralComercial.setWeb(cursor.getString(6));
         	   centralComercial.setFacebook(cursor.getString(7));
         	   centralComercial.setTwitter(cursor.getString(8));
         	   centralComercial.setInstagram(cursor.getString(9));
         	   centralComercial.setPrecio(cursor.getString(10));
                // Adding contact to list
               
            } while (cursor.moveToNext());
        }
     
        // return contact list
        return centralComercial;
    }

}
