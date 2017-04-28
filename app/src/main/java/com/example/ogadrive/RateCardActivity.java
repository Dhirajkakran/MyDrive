package com.example.ogadrive;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bean.User;
import com.example.bean.VehicleCategory;
import com.example.utility.Utility;
import com.example.volley.AppController;

public class RateCardActivity extends ActionBarActivity {

	private ArrayList<VehicleCategory> listVehicleCategory;
	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate_card);
		
		user = (User) getIntent().getExtras().getSerializable("User");
		String category = getIntent().getStringExtra("category");
		Button buttonOk = (Button) findViewById(R.id.btnOk);
		
		volleyRequest(user, category);
		buttonOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rate_card, menu);
		return true;
	}


	void volleyRequest(final User user, final String category) {
		// Tag used to cancel the request
	//final	ArrayList<GetLocation> listLocation = new ArrayList<GetLocation>();
		 
		listVehicleCategory = new ArrayList<VehicleCategory>();
		if(Utility.isNetworkAvailable(RateCardActivity.this)) {
		String tag_json_obj = "json_obj_req";
		 
		final String TAG = "Volley" ;
		String url = "http://ogadrive.com/OgadriveiceServices.svc/VehicleCategories/"+user.getToken();
		         
		final ProgressDialog pDialog = new ProgressDialog(RateCardActivity.this);
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
		        					
		        					String str = jsonObj.getString("Message");
		        					//Toast.makeText(RateCardActivity.this, " "+str, Toast.LENGTH_SHORT).show();
		        					
		         					
		        					JSONArray jsonArray = jsonObj.getJSONArray("VehichleCategories");
		        					for(int i=0; i < jsonArray.length(); i++) {
		        						JSONObject jsonObject = (JSONObject) jsonArray.get(i);
		    							
		    						 
		    							 
		        						VehicleCategory vehicleCategory = new VehicleCategory(); 
		        						vehicleCategory.setVehichleID(jsonObject.getString("VehichleID"));
		        						vehicleCategory.setName(jsonObject.getString("Name"));
		        						vehicleCategory.setRatePerKM(jsonObject.getString("RatePerKM"));
		        						vehicleCategory.setBaseFare(jsonObject.getString("BaseFare"));
		        						vehicleCategory.setWaitPerMinute(jsonObject.getString("WaitPerMinute"));
		        						vehicleCategory.setDescription(jsonObject.getString("Description"));
		        						vehicleCategory.setCurrency(jsonObject.getString("Currency"));
		        						
		        						if(vehicleCategory.getName().equalsIgnoreCase(category)) {
		        							
		        						TextView txtBaseFare	= ((TextView)findViewById(R.id.txtBaseFare));
		        						TextView txtRatePerKM	= ((TextView)findViewById(R.id.txtRatePerKM));
		        						TextView txtWaitCost	= ((TextView)findViewById(R.id.txtWaitCost));
		        						TextView txtCategory	= ((TextView)findViewById(R.id.txtCategory));
		        						TextView txtDescription	= ((TextView)findViewById(R.id.txtDescription));
		        						txtBaseFare.setText(vehicleCategory.getBaseFare());
		        						txtRatePerKM.setText(vehicleCategory.getRatePerKM());
		        						txtWaitCost.setText(vehicleCategory.getWaitPerMinute());
		        						txtCategory.setText(vehicleCategory.getName());
		        						txtDescription.setText(vehicleCategory.getDescription());
		        						listVehicleCategory.add(vehicleCategory);
			        						
		        							break;
		        						}
		        						
		        					}
		        					if(listVehicleCategory.size() < 1) {
		        						//Toast.makeText(RateCardActivity.this, "Category RateCard not Presented ", Toast.LENGTH_SHORT).show();
		        						finish();
		        					}
		        					
		        					
		        				} else {
		        					String str = jsonObj.getString("Message");
		        					//Toast.makeText(RateCardActivity.this, " "+str, Toast.LENGTH_SHORT).show();
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
		                        //Toast.makeText(RateCardActivity.this, " "+error.getMessage(), Toast.LENGTH_SHORT).show();
		                    }
		                });
		 
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		} else {
			Toast.makeText(RateCardActivity.this, R.string.internet_not_access, Toast.LENGTH_SHORT).show();
		}
		
		 
	}
}
