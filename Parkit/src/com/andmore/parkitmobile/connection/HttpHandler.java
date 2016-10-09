package com.andmore.parkitmobile.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
  

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
  

import android.util.Log;

@SuppressWarnings("deprecation")
public class HttpHandler
{
	
	static InputStream inpStream = null;
    static JSONObject jsonObj = null;
    static String jsonStr = "";
     
    static InputStream iStream = null;
    static JSONArray jarray = null;
  
 
    public HttpHandler() {
  
    }
    
    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
            List<NameValuePair> params) {
  
        // Making HTTP request
        try {
  
            // check for request method
            if(method == "POST"){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
  
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inpStream = httpEntity.getContent();
  
            }else if(method == "GET"){
                // request method is GET
            	
            	DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
           
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                inpStream = httpEntity.getContent();
            }          
  
        } catch (UnsupportedEncodingException e) {
            Log.e("Response Error", "Response Error " + e.toString());
        } catch (ClientProtocolException e) {
            Log.e("Response Error", "Response Error " + e.toString());
        } catch (IOException e) {
            Log.e("Response Error", "Response Error " + e.toString());
        }catch (Exception e) {
        	Log.e("Response Error", "Response Error " + e.toString());
		}
  
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inpStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inpStream.close();
            jsonStr = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
  
        // try parse the string to a JSON object
        try {
            jsonObj = new JSONObject(jsonStr);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
  
        // return JSON String
        return jsonObj;
  
    }
    
    
    public JSONArray getJSONFromUrl(String url) {
    	 
        StringBuilder builder = new StringBuilder();
         HttpClient client = new DefaultHttpClient();
         HttpGet httpGet = new HttpGet(url);
         try {
           HttpResponse response = client.execute(httpGet);
           StatusLine statusLine = response.getStatusLine();
           int statusCode = statusLine.getStatusCode();
           if (statusCode == 200) {
             HttpEntity entity = response.getEntity();
             InputStream content = entity.getContent();
             BufferedReader reader = new BufferedReader(new InputStreamReader(content));
             String line;
             while ((line = reader.readLine()) != null) {
               builder.append(line);
             }
           } else {
             Log.e("==>", "Failed to download file");
           }
         } catch (ClientProtocolException e) {
           e.printStackTrace();
         } catch (IOException e) {
           e.printStackTrace();
         }
        
     // Parse String to JSON object
     try {
         jarray = new JSONArray( builder.toString());
     } catch (JSONException e) {
         Log.e("JSON Parser", "Error parsing data " + e.toString());
     }

     // return JSON Object
     return jarray;

 }
  
  
  }
  
  