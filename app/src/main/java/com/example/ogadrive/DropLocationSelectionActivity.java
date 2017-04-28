package com.example.ogadrive;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bean.GetLocation;
import com.example.bean.User;
import com.example.utility.Config;
import com.example.utility.Utility;
import com.example.volley.AppController;

public class DropLocationSelectionActivity extends ActionBarActivity {

	GetLocation selectedLocation;
	AutoCompleteTextView txtAutoCompleteLocation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drop_location_selection);
		
		  txtAutoCompleteLocation = (AutoCompleteTextView) findViewById(R.id.autoCompleteDropLocation);
		
		AutoCompleteTextView autoCompletePopularLocation = (AutoCompleteTextView) findViewById(R.id.autoCompletePopularLocation);
		
		User user = (User) getIntent().getExtras().getSerializable("User");
		
		final ArrayList<GetLocation> listLocation =  volleyRequest(user);
		ArrayAdapter<GetLocation> locationAdapter = new ArrayAdapter<GetLocation>(DropLocationSelectionActivity.this,
		          android.R.layout.simple_list_item_1, android.R.id.text1, listLocation);
		
		txtAutoCompleteLocation.setOnTouchListener(new View.OnTouchListener(){
			   @Override
			   public boolean onTouch(View v, MotionEvent event){
				   txtAutoCompleteLocation.showDropDown();
			      return false;
			   }
			});
		
		txtAutoCompleteLocation.setOnTouchListener(new View.OnTouchListener(){
			   @Override
			   public boolean onTouch(View v, MotionEvent event){
				   txtAutoCompleteLocation.showDropDown();
			      return false;
			   }
			});
		txtAutoCompleteLocation.setAdapter(locationAdapter);
		txtAutoCompleteLocation.setThreshold(1);
		txtAutoCompleteLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				 selectedLocation = listLocation.get(arg2);
			}
		});
		
		autoCompletePopularLocation.setAdapter(locationAdapter);
		autoCompletePopularLocation.setThreshold(1);
		
		Button btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(selectedLocation != null) {
				Intent intent = new Intent(DropLocationSelectionActivity.this, RideEstimateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("SrcLocation", getIntent().getExtras().getSerializable("SelectedLocation"));
                bundle.putSerializable("DestLocation", selectedLocation);
                intent.putExtras(bundle);
				startActivity(intent);
				} else {
					Toast.makeText(DropLocationSelectionActivity.this, "Please Select Drop Location", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drop_location_selection, menu);
		return true;
	}
	
	
	ArrayList<GetLocation> volleyRequest(User user) {
		// Tag used to cancel the request
	final	ArrayList<GetLocation> listLocation = new ArrayList<GetLocation>();
		if(Config.isLocal) {
			String string = Utility.readFile(DropLocationSelectionActivity.this, "GetLocations");
			try {
				JSONObject jsonObj = new JSONObject(string);
				
				if(jsonObj.getString("IsSuccess").equals("true")){
					
					JSONArray jsonArrayLocation = jsonObj.getJSONArray("Locations");
					if(jsonArrayLocation != null ) {
						for(int i=0; i<jsonArrayLocation.length(); i++){
							JSONObject jsonObject = (JSONObject) jsonArrayLocation.get(i);
							
							GetLocation getLocation = new GetLocation();
							getLocation.setLocationID(jsonObject.getInt("LocationID"));
							getLocation.setName(jsonObject.getString("Name"));
							getLocation.setLatitude(jsonObject.getDouble("Latitude"));
							getLocation.setLongitute(jsonObject.getDouble("Longitde"));
							getLocation.setState(jsonObject.getString("State"));
							listLocation.add(getLocation);
						}
						ArrayAdapter<GetLocation> locationAdapter = new ArrayAdapter<GetLocation>(DropLocationSelectionActivity.this,
						          android.R.layout.simple_list_item_1, android.R.id.text1, listLocation);
						
						txtAutoCompleteLocation.setAdapter(locationAdapter);
						txtAutoCompleteLocation.setThreshold(1);
					}
					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			
			if(Utility.isNetworkAvailable(DropLocationSelectionActivity.this)) {
		String tag_json_obj = "json_obj_req";
		 
		final String TAG = "Volley" ;
		String url = "http://ogadrive.com/OgadriveiceServices.svc/Locations/"+user.getToken();
		         
		final ProgressDialog pDialog = new ProgressDialog(DropLocationSelectionActivity.this);
		pDialog.setMessage("Loading...");
		pDialog.show();   
		
		 
		         
		        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
		                url, null,
		                new Response.Listener<JSONObject>() {
		 
		                    @Override
		                    public void onResponse(JSONObject jsonObj) {
		                        Log.d(TAG, jsonObj.toString());
		                        pDialog.hide();
		                        
		                        try {
		        				if(jsonObj.getString("IsSuccess").equals("true")){
		        					
		        					JSONArray jsonArrayLocation = jsonObj.getJSONArray("Locations");
		        					if(jsonArrayLocation != null ) {
		        						for(int i=0; i<jsonArrayLocation.length(); i++){
		        							JSONObject jsonObject = (JSONObject) jsonArrayLocation.get(i);
		        							
		        							GetLocation getLocation = new GetLocation();
		        							getLocation.setLocationID(jsonObject.getInt("LocationID"));
		        							getLocation.setName(jsonObject.getString("Name"));
		        							getLocation.setLatitude(jsonObject.getDouble("Latitude"));
		        							getLocation.setLongitute(jsonObject.getDouble("Longitde"));
		        							getLocation.setState(jsonObject.getString("State"));
		        							listLocation.add(getLocation);
		        						}
		        					}
		        					ArrayAdapter<GetLocation> locationAdapter = new ArrayAdapter<GetLocation>(DropLocationSelectionActivity.this,
		  						          android.R.layout.simple_list_item_1, android.R.id.text1, listLocation);
		  						
		  						txtAutoCompleteLocation.setAdapter(locationAdapter);
		  						txtAutoCompleteLocation.setThreshold(1);
		        					
		        				 } else {
		        					 
			            					String str = jsonObj.getString("Message");
			            					//Toast.makeText(DropLocationSelectionActivity.this, " "+str, Toast.LENGTH_SHORT).show();
				            				
			            			 
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
		                        pDialog.hide();
		                       // Toast.makeText(DropLocationSelectionActivity.this, " "+error.getMessage(), Toast.LENGTH_SHORT).show();
		                    }
		                });
		 
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		} else {
			//Toast.makeText(DropLocationSelectionActivity.this, R.string.internet_not_access, Toast.LENGTH_SHORT).show();
		}
		}
		return listLocation;
	}

}
