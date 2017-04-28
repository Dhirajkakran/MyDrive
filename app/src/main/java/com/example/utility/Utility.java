package com.example.utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utility {

 public static java.lang.String readFile(Context context, String fileName) {
	
	 String str = null;

	 StringBuilder buf=new StringBuilder();
	 try {
	    InputStream json=context.getAssets().open(""+fileName+".txt");
	    BufferedReader in=
	        new BufferedReader(new InputStreamReader(json, "UTF-8"));
	    

	    while ((str=in.readLine()) != null) {
	      buf.append(str);
	    }

	    in.close();
	 } catch(Exception ex) {
		 ex.printStackTrace();
	 }
	    return   buf.toString();
	}
 
 
 public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
