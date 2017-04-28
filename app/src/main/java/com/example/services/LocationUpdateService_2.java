package com.example.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bean.GetLocation;
import com.example.ogadrive.R;
import com.example.utility.StaticValues;
import com.example.utility.Utility;
import com.example.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LocationUpdateService_2 extends Service {

	
	private LocationManager _locationManager;
	private boolean _gpsEnabled,_networkEnabled;
    public static String token;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		_gpsEnabled = _locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		_networkEnabled = _locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		
	}
	
	public void onStart(Intent intent, int startId) {
		//setLocationSendingAlarm(1000 * 5);
		Log.e("onStart","onStart");
		
		if( _gpsEnabled ) {
			_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					1000, 0, locationListener);
		} else if ( _networkEnabled ){
			_locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					1000, 0, locationListener);
		}
		
	};
	
	

	public void onDestroy() {
		
		Log.e("onDestroy","onDestroy");
	};
	
	
	/**
	 * Location listener,to listen new location if available
	 */
	LocationListener locationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(final Location location) {
			final Location currentLocation = location;

			Log.e("onLocationChanged", " " + currentLocation);
			_locationManager.removeUpdates(locationListener);
		//	cancelLocationSendingAlarm();
            setLocationSendingAlarm(10 * 1000);
			//Toast.makeText(LocationUpdateService_2.this, "Location lat "+currentLocation.getLatitude()+ " long "+currentLocation.getLongitude() , Toast.LENGTH_LONG).show();
			
			
			
			final JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("Latitude", ""+currentLocation.getLatitude());
				jsonObj.put("Longitude", ""+currentLocation.getLongitude());
				//jsonObj.put("TrackTime", "");
				jsonObj.put("IsDriver", "true");
				jsonObj.put("Token", ""+token);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			 
			if (Utility.isNetworkAvailable(LocationUpdateService_2.this)) {

				// TODO Auto-generated method stub

                if(Utility.isNetworkAvailable(getApplicationContext())) {
                    String tag_json_obj = "json_obj_req";

                    final String TAG = "Volley" ;
                    String url = "http://ogadrive.com/OgadriveiceServices.svc/UpdateLocation";

                   /* final ProgressDialog pDialog = new ProgressDialog(getApplicationContext());
                    pDialog.setMessage("Loading...");
                    pDialog.show();
*/


                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            url, jsonObj,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject jsonObj) {
                                    Log.d(TAG, jsonObj.toString());
                                    //pDialog.hide();

                                    try {
                                        if(jsonObj.getString("IsSuccess").equals("true")){

                                          //  Toast.makeText(getApplicationContext(), " "+jsonObj.getString("Message"), Toast.LENGTH_SHORT).show();

                                        }
                                    } catch(Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            // hide the progress dialog
                          //  pDialog.hide();
                          //  Toast.makeText(getApplicationContext(), " "+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Adding request to request queue
                    AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.internet_not_access, Toast.LENGTH_SHORT).show();
                }


			} else {
				// logic for store data to local DB 
				 
			}
			//setLocationSendingAlarm(StaticValues.TIME_INTERVAL_LOCATION);
 
		}

	};
	
	
	
	
	private void setLocationSendingAlarm(long timeForAlarm) {

		Log.e("setLocationSendingAlarm","setLocationSendingAlarm ");
		AlarmManager alarmManager = (AlarmManager) getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(getApplicationContext(),
				LocationUpdateService_2.class);
		intent.putExtra("locationSendingAlarm", true);
		PendingIntent pendingIntent = PendingIntent.getService(this,
				StaticValues.PENDING_INTENET_LOCATION_SENDING_ALARM_ID, intent,
				0);
		try {
			alarmManager.cancel(pendingIntent);
		} catch (Exception e) {

		}
		//long timeForAlarm = StaticValues.TIME_INTERVAL_LOCATION;
		
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + timeForAlarm, timeForAlarm,
				pendingIntent);
	}
	
	
	
	private void cancelLocationSendingAlarm() {
		AlarmManager alarmManager = (AlarmManager) getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(getApplicationContext(),
				LocationUpdateService_2.class);
		intent.putExtra("locationSendingAlarm", true);
		PendingIntent pendingIntent;
		pendingIntent = PendingIntent.getService(this,
				StaticValues.PENDING_INTENET_LOCATION_SENDING_ALARM_ID, intent,
				0);
		try {
			alarmManager.cancel(pendingIntent);
		} catch (Exception e) {
		}
	}
	
}
