package com.example.ogadrive;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bean.GetLocation;
import com.example.bean.User;
import com.example.database.DBAdapter;
import com.example.utility.Config;
import com.example.utility.Utility;
import com.example.volley.AppController;

public class RegisterActivity extends ActionBarActivity {

	EditText edtEmail, edtPassword, edtConfirmPassword, edtFullName,
			edtPhoneNumber;
	Button btnSignUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
		edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
		edtFullName = (EditText) findViewById(R.id.edtFullName);
		edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
		btnSignUp = (Button) findViewById(R.id.btnSignUp);

		btnSignUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (validate() ) {
					//btnSignUp.setEnabled(true);
					
					if(Utility.isNetworkAvailable(RegisterActivity.this)) {
						
					
					DBAdapter dbAdapter = new DBAdapter(RegisterActivity.this);
					dbAdapter.open();
					
					if(!dbAdapter.isUserNameAlreadyExist(edtEmail.getText().toString())) {
					User user = new User();
					user.setEmail(edtEmail.getText().toString());
					user.setPassword(edtPassword.getText().toString());
					user.setName(edtFullName.getText().toString());
					user.setPhone(edtPhoneNumber.getText().toString());
					dbAdapter.close();
					volleyRequest(user);
				
					
					// Post Data To Server for Registration
				} else {
					Toast.makeText(RegisterActivity.this, "Email Already Exist", Toast.LENGTH_SHORT).show();
					dbAdapter.close();
				}
				
					} else {
						Toast.makeText(RegisterActivity.this, R.string.internet_not_access, Toast.LENGTH_SHORT).show();
						
					}
					}
			}
		});
		edtPhoneNumber.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (!arg1) {
					
				}
			}

			
		});

	}
	private boolean validate() {
		// TODO Auto-generated method stub
		if (!isEmailValid(edtEmail.getText())) {

			Toast.makeText(RegisterActivity.this,
					"Please Enter Valid Email Address",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (edtPassword.getText().toString().equals("")) {

			Toast.makeText(RegisterActivity.this,
					"Please Enter Password",
					Toast.LENGTH_SHORT).show();
			return false;
			
		}
		if (edtConfirmPassword.getText().toString().equals("")) {

			Toast.makeText(RegisterActivity.this,
					"Please Enter Confirm Password",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (!edtConfirmPassword.getText().toString().equals(edtPassword.getText().toString())) {

			Toast.makeText(RegisterActivity.this,
					"Password Mismatch",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (edtFullName.getText().toString().equals("")) {

			Toast.makeText(RegisterActivity.this,
					"Please Enter Name",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (edtPhoneNumber.getText().toString().equals("")) {

			Toast.makeText(RegisterActivity.this,
					"Please Enter Phone Number",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}


	boolean isEmailValid(CharSequence target) {
		if (target == null)
			return false;
		else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	void volleyRequest(final User user) {
		// Tag used to cancel the request
		 
	 
		String tag_json_obj = "json_obj_req_Request";
		 
		final String TAG = "Volley" ;
		String url = "http://ogadrive.com/OgadriveiceServices.svc/RegisterUser";
		         
		final ProgressDialog pDialog = new ProgressDialog(RegisterActivity.this);
		pDialog.setMessage("Loading...");
		pDialog.show();   
		
		
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("FullName", user.getName());
			jsonObj.put("Password", user.getPassword());
			jsonObj.put("EmailAddress", user.getEmail());
			jsonObj.put("PhoneNumber", user.getPhone());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		         
		        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
		                url, jsonObj,
		                new Response.Listener<JSONObject>() {
		 
		                    @Override
		                    public void onResponse(JSONObject jsonObj) {
		                        Log.d(TAG, jsonObj.toString());
		                        pDialog.hide();
		                        
		                        try {
		            				 
		                        	String str = jsonObj.getString("Message");
		            				if(jsonObj.getString("IsSuccess").equals("true")){
		            					
		            				
		            				
		            			//	Toast.makeText(RegisterActivity.this, ""+str, Toast.LENGTH_SHORT).show();

		            				user.setUserId(jsonObj.getString("UserID"));
		            				user.setToken(jsonObj.getString("Token"));
		            				
		            				DBAdapter dbAdapter = new DBAdapter(RegisterActivity.this);
		        					dbAdapter.open();
		            				long rowid =	dbAdapter.registerUser(user);
		        					dbAdapter.close();
		        					if(rowid > 0){
		        						//Toast.makeText(RegisterActivity.this, "  "+str, Toast.LENGTH_SHORT).show();
		        						finish();
		        						
		        					} else {
		        						//Toast.makeText(RegisterActivity.this, " "+str, Toast.LENGTH_SHORT).show();
		        						
		        					}


		            					
		            				} else {
		            					//Toast.makeText(RegisterActivity.this, "  "+str, Toast.LENGTH_SHORT).show();
		            				}
		            				
		            			} catch (JSONException e) {
		            				// TODO Auto-generated catch block
		            				e.printStackTrace();
		            			}
		                    }
		                }, new Response.ErrorListener() {
		 
		                    @Override
		                    public void onErrorResponse(VolleyError error) {
		                        VolleyLog.d(TAG, "Error: " + error.getMessage());
		                        // hide the progress dialog
		                        pDialog.hide();
		                        //Toast.makeText(RegisterActivity.this, " "+error.getMessage(), Toast.LENGTH_SHORT).show();
		                    }
		                });
		 
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		
			}

}
