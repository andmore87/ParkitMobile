package com.andmore.parkitmobile.repository;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class SQLiteUserHandler extends SQLiteOpenHelper {
	
	private static final String TAG = SQLiteUserHandler.class.getSimpleName();
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "parkit";
 
    // Login table name
    private static final String TABLE_USER = "user";
    // Login table name
    private static final String TABLE_CC = "central_comercial";
 
    // Login Table user Columns names
    //private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    //private static final String KEY_UID = "uid";
    //private static final String KEY_CREATED_AT = "created_at";
   
    // Login Table cc Columns names
    private static final String KEY_ID = "id";
 
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_SCHEDULE = "schedule";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_WEB = "web";
    private static final String KEY_FACEBOOK = "facebook";
    private static final String KEY_TWITTER = "twitter";
    private static final String KEY_INSTAGRAM = "instagram";
    private static final String KEY_PRECIO = "precio";
    
    //  seccion table name
    private static final String TABLE_SECCION = "seccion";
    // nodo table name
    private static final String TABLE_NODO = "nodo";
    
    // local table name
    private static final String TABLE_LOCAL = "local";
   
    // Table SECCION Columns names
    
    private static final String KEY_CC_ID = "centro_comercial_id";
   
    //  Table NODO Columns names
    private static final String KEY_SECCION_ID = "seccion_id";
    private static final String KEY_STATE = "estado";
    

    // Table LOCAL Columns names
    private static final String KEY_NAMEC = "nombre";
    private static final String KEY_NUMBER = "numero";
    private static final String KEY_DESC = "descripcion";
    private static final String KEY_PHOTOL = "foto";
    private static final String KEY_CATEGORY = "categoria";
    private static final String KEY_LOCAL_ID = "local_id";
 
 
    public SQLiteUserHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_PASSWORD + " TEXT"
                + ")";
        String CREATE_CC_TABLE = "CREATE TABLE " + TABLE_CC + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT," + KEY_SCHEDULE + " TEXT,"
                + KEY_PHONE + " TEXT," + KEY_PHOTO + " TEXT,"
                + KEY_WEB + " TEXT," + KEY_FACEBOOK + " TEXT,"
                + KEY_TWITTER + " TEXT," 
                + KEY_INSTAGRAM + " TEXT,"  
                + KEY_PRECIO + " TEXT"
                + ")";
        
        String CREATE_SESSION_TABLE = "CREATE TABLE " + TABLE_SECCION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAMEC + " TEXT," + KEY_CC_ID + " INTEGER"
                + ")";
        String CREATE_NODO_TABLE = "CREATE TABLE " + TABLE_NODO + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAMEC + " TEXT,"
                + KEY_SECCION_ID + " INTEGER," + KEY_STATE + " INTEGER"
                + ")";
        String CREATE_LOCAL_TABLE = "CREATE TABLE " + TABLE_LOCAL + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMBER + " INTEGER,"
                + KEY_NAMEC + " TEXT," + KEY_DESC + " TEXT,"
                + KEY_PHOTOL + " TEXT," + KEY_CATEGORY + " TEXT,"
                + KEY_CC_ID + " INTEGER," + KEY_SECCION_ID + " INTEGER"
                + ")";
        
        
        db.execSQL(CREATE_SESSION_TABLE);
        db.execSQL(CREATE_NODO_TABLE);
        db.execSQL(CREATE_LOCAL_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CC_TABLE);
     
        Log.d(TAG, "Database tables created");
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECCION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAL);
       
        // Create tables again
        onCreate(db);
    }
 
    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_PASSWORD, email); // pass
      
        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
 
        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
 
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("password", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
 
        return user;
    }
 
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();
 
        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
