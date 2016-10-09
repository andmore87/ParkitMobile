package com.andmore.parkitmobile.repository;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.andmore.parkitmobile.entity.Local;
import com.andmore.parkitmobile.entity.Nodo;
import com.andmore.parkitmobile.entity.Seccion;
public class SQLiteSeccionHandler extends SQLiteOpenHelper {
	
	private static final String TAG = SQLiteSeccionHandler.class.getSimpleName();
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "parkit";
 
    //  seccion table name
    private static final String TABLE_SECCION = "seccion";
    // nodo table name
    private static final String TABLE_NODO = "nodo";
    
    // local table name
    private static final String TABLE_LOCAL = "local";
 
    // Table SECCION Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "nombre";
    private static final String KEY_CC_ID = "centro_comercial_id";
   
    //  Table NODO Columns names
    private static final String KEY_SECCION_ID = "seccion_id";
    private static final String KEY_STATE = "estado";
    

    // Table LOCAL Columns names
    private static final String KEY_NUMBER = "numero";
    private static final String KEY_DESC = "descripcion";
    private static final String KEY_PHOTO = "foto";
    private static final String KEY_CATEGORY = "categoria";
 
 
    public SQLiteSeccionHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SESSION_TABLE = "CREATE TABLE " + TABLE_SECCION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT," + KEY_CC_ID + " INTEGER"
                + ")";
        String CREATE_NODO_TABLE = "CREATE TABLE " + TABLE_NODO + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SECCION_ID + " INTEGER," + KEY_STATE + " INTEGER"
                + ")";
        String CREATE_LOCAL_TABLE = "CREATE TABLE " + TABLE_LOCAL + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUMBER + " INTEGER,"
                + KEY_NAME + " TEXT," + KEY_DESC + " TEXT,"
                + KEY_PHOTO + " TEXT," + KEY_CATEGORY + " TEXT,"
                + KEY_CC_ID + " INTEGER," + KEY_SECCION_ID + " INTEGER"
                + ")";
        
        
        db.execSQL(CREATE_SESSION_TABLE);
        db.execSQL(CREATE_NODO_TABLE);
        db.execSQL(CREATE_LOCAL_TABLE);
    
        Log.d(TAG, "Database tables created");
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECCION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAL);
  
        // Create tables again
        onCreate(db);
    }
 
    /**
     * Storing seccion details in database
     * */
    public void addSeccion(Seccion seccion) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, seccion.getId());  
        values.put(KEY_NAME, seccion.getNombre());  
        values.put(KEY_CC_ID, seccion.getCentro_comercial_id()); 
      
        // Inserting Row
        long id = db.insert(TABLE_SECCION, null, values);
        db.close(); // Closing database connection
 
        Log.d(TAG, "New seccion inserted into sqlite: " + id);
    }
    
    /**
     * Storing nodo details in database
     * */
    public void addNodo(Nodo nodo) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_ID, nodo.getId()); 
        values.put(KEY_NAME, nodo.getNombre()); 
        values.put(KEY_SECCION_ID, nodo.getSeccion_id());
        values.put(KEY_STATE, nodo.getEstado());
      
        // Inserting Row
        long id = db.insert(TABLE_NODO, null, values);
        db.close(); // Closing database connection
 
        Log.d(TAG, "New NODO inserted into sqlite: " + id);
    }
    
    /**
     * Storing user details in database
     * */
    public void addLocal(Local local) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_ID, local.getId());  
        values.put(KEY_NUMBER, local.getNumero());
        values.put(KEY_NAME, local.getNombre()); 
        values.put(KEY_DESC, local.getDescripcion());
        values.put(KEY_PHOTO, local.getFoto());
        values.put(KEY_CATEGORY, local.getCategoria());
        values.put(KEY_CC_ID, local.getCentro_comercial_id());
        values.put(KEY_SECCION_ID, local.getSeccion());
      
        // Inserting Row
        long id = db.insert(TABLE_LOCAL, null, values);
        db.close(); // Closing database connection
 
        Log.d(TAG, "New LOCAL inserted into sqlite: " + id);
    }
    
   
 
   
    
    public  List<Seccion> getSeccionByCC(int centralComercialId) {
        
        List<Seccion> seccionList = new ArrayList<Seccion>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SECCION +" WHERE "+ KEY_CC_ID +"="+centralComercialId;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Seccion seccion = new Seccion();
            	seccion.setId(Integer.parseInt(cursor.getString(0)));
            	seccion.setNombre(cursor.getString(1));
            	seccion.setCentro_comercial_id(Integer.parseInt(cursor.getString(2)));
            	
            	// Adding seccion to list
            	seccionList.add(seccion);
               
            } while (cursor.moveToNext());
        }
     
        // return seccion list
        return seccionList;
    }
    
    public  List<Local> getLocalsBySeccion(int seccionId) {
        
        List<Local> localList = new ArrayList<Local>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOCAL +" WHERE "+ KEY_SECCION_ID +"="+seccionId;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Local local = new Local();
            	local.setId(Integer.parseInt(cursor.getString(0)));
            	local.setNumero(cursor.getString(1));
            	local.setNombre(cursor.getString(2));
            	local.setDescripcion(cursor.getString(3));
            	local.setFoto(cursor.getString(4));
            	local.setCategoria(cursor.getString(5));
            	local.setCentro_comercial_id(Integer.parseInt(cursor.getString(6)));
            	local.setSeccion(Integer.parseInt(cursor.getString(7)));
            	
            	// Adding local to list
            	localList.add(local);
               
            } while (cursor.moveToNext());
        }
     
        // return local list
        return localList;
    }
    


}
