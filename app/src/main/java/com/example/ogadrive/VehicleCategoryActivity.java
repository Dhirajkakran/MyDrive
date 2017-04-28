package com.example.ogadrive;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bean.GetLocation;
import com.example.bean.User;
import com.example.utility.Config;
import com.example.utility.Utility;
import com.example.volley.AppController;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VehicleCategoryActivity extends ActionBarActivity implements
		OnClickListener {

	User user;
	String 	pickupPlaceID, dropPlaceID, fare, distanceKM;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle_category);

		Intent intent = getIntent();
		String category = intent.getStringExtra("category");
		Bundle bundle = intent.getExtras();
		user = (User) bundle.getSerializable("User");
		//GetLocation selectedLocation = (GetLocation) bundle.getSerializable("SelectedLocation"); 

		
		TextView txtView = (TextView) findViewById(R.id.txtViewCategory);
		txtView.setText(category);

		Button btnRideEstimate = (Button) findViewById(R.id.btnRideEstimate);
		Button btnCancel = (Button) findViewById(R.id.btnCancel);

		Button btnBookLater = (Button) findViewById(R.id.btnBookLater);
		
		Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
		Button btnRateCard = (Button) findViewById(R.id.btnRateCard);

		btnRideEstimate.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnBookLater.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
		btnRateCard.setOnClickListener(this);
		btnConfirm.setEnabled(false);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		Bundle bundle = intent.getExtras();
		if(bundle != null) {
	  	pickupPlaceID = bundle.getString("PickupPlaceID");
	  	dropPlaceID = bundle.getString("DropPlaceID");
	  	fare = bundle.getString("Fare");
	  	distanceKM = bundle.getString("DistanceKM");
		
		
		 findViewById(R.id.btnConfirm).setEnabled(true);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vehicle_category, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btnRideEstimate:
			Intent intent = new Intent(VehicleCategoryActivity.this,
					DropLocationSelectionActivity.class);
			intent.putExtras(getIntent().getExtras());
			// intent.set;intent.getExtras();
			startActivity(intent);
			break;
		case R.id.btnCancel:
			finish();
			break;

		case R.id.btnBookLater:
			finish();
			break;
			
		case R.id.btnConfirm:
			// Write Code to Post Details To server
			volleyRequest(user);
			//finish();
			break;
			
		case R.id.btnRateCard :
			Intent intentRateUs = new Intent(VehicleCategoryActivity.this,
					RateCardActivity.class);
			intentRateUs.putExtras(getIntent().getExtras());
		 
			startActivity(intentRateUs);
			break;
		}
	}

	
	void volleyRequest(User user) {
		// Tag used to cancel the request
	
			
			if(Utility.isNetworkAvailable(VehicleCategoryActivity.this)) {
		String tag_json_obj = "json_obj_req";
		 
		final String TAG = "Volley" ;
		String url = "http://ogadrive.com/OgadriveiceServices.svc/Booking";
		         
		final ProgressDialog pDialog = new ProgressDialog(VehicleCategoryActivity.this);
		pDialog.setMessage("Loading...");
		pDialog.show();   
		
		 JSONObject jsonObject = new JSONObject();
		 try {
			jsonObject.put("UserID", user.getUserId() );
			jsonObject.put("PickupPlaceID", ""+pickupPlaceID);
			 jsonObject.put("DropPlaceID", ""+dropPlaceID );
			 jsonObject.put("Fare", ""+fare );
			 jsonObject.put("DistanceKM", ""+distanceKM);
			 jsonObject.put("Token", user.getToken());
			 jsonObject.put("BookingDateTime", System.currentTimeMillis());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		         
		        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
		                url, jsonObject,
		                new Response.Listener<JSONObject>() {
		 
		                    @Override
		                    public void onResponse(JSONObject jsonObj) {
		                        Log.d(TAG, jsonObj.toString());
		                        pDialog.hide();
		                        
		                        try {
		        				if(jsonObj.getString("IsSuccess").equals("true")){
		        					
		        					String str = jsonObj.getString("Message");
		        					//Toast.makeText(VehicleCategoryActivity.this, ""+str, Toast.LENGTH_SHORT).show();
		        					 
		        					finish();
		        					
		        				} else {
	            					String str = jsonObj.getString("Message");
	            					//Toast.makeText(VehicleCategoryActivity.this, " "+str, Toast.LENGTH_SHORT).show();
		            				
	            				}
		                        } catch(Exception ex) {
		                        	ex.printStackTrace();
		                        }
		                    }
		                }, new Response.ErrorListener() {
		 
		                    @Override
		                    public void onErrorResponse(VolleyError error) {
		                        VolleyLog.d(TAG, "Error: " + error.getMessage());
		                        //Toast.makeText(VehicleCategoryActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
		                        // hide the progress dialog
		                        pDialog.hide();
		                    }
		                });
		 
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		} else {
			Toast.makeText(VehicleCategoryActivity.this, R.string.internet_not_access, Toast.LENGTH_SHORT).show();
		}
		
		}
}
