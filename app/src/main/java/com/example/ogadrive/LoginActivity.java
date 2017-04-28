package com.example.ogadrive;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bean.User;
import com.example.database.DBAdapter;
import com.example.utility.Config;
import com.example.volley.AppController;


public class LoginActivity extends ActionBarActivity implements OnClickListener{

	private EditText edtPassword, edtEmail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		
		DBAdapter dbAdapter = new DBAdapter(LoginActivity.this);
		dbAdapter.open();
		
		User user = dbAdapter.isLoogin();
		dbAdapter.close();
		if(user != null) {
	
		Intent intent = new Intent(LoginActivity.this, HomeActivity2.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("User", user);
		intent.putExtras(bundle);
	
		startActivity(intent);
		finish();
		}
		
		Button buttonLogin = (Button) findViewById(R.id.btnNext);
		Button btnRegister = (Button) findViewById(R.id.btnForgotPassword);
		buttonLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		
		  edtEmail = (EditText) findViewById(R.id.edtEmail);
		  edtPassword = (EditText) findViewById(R.id.edtPassword);
		
		  final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

		    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
		        buildAlertMessageNoGps();
		    }
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnNext:
			
			if(Config.isLocal) {
			DBAdapter dbAdapter = new DBAdapter(LoginActivity.this);
			dbAdapter.open();
			User user = dbAdapter.login(edtEmail.getText().toString(), edtPassword.getText().toString());
			dbAdapter.close();
			if(user != null && user.getEmail().equals(edtEmail.getText().toString())) {
			Intent intent = new Intent(LoginActivity.this, HomeActivity2.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("User", user);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
			} else {
				Toast.makeText(LoginActivity.this, "Please Enter Correct UserName and Password", Toast.LENGTH_SHORT).show();
			}
			} else {
				volleyRequest(this, edtEmail.getText().toString(), edtPassword.getText().toString());
				
			}
			break;
			
		case R.id.btnForgotPassword :
			
			Intent intentLogin = new Intent(LoginActivity.this, LoginActivity.class);
			startActivity(intentLogin);
		break;

		default:
			break;
		}
	}

	
	static void volleyRequest(final LoginActivity loginActivity, String email, String password) {
		// Tag used to cancel the request
		 
	 
		String tag_json_obj = "json_obj_req_Request";
		 
		final String TAG = "Volley" ;
		String url = "http://ogadrive.com/OgadriveiceServices.svc/DriverLogin";
		         
		final ProgressDialog pDialog = new ProgressDialog(loginActivity);
		pDialog.setMessage("Loading...");
		pDialog.show();   
		
		
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("EmailAddress", email);
			jsonObj.put("Password", password);
			 
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
		            				 
		            				
		            				if(jsonObj.getString("IsSuccess").equals("true")){
		            					
		            				String str = jsonObj.getString("Message");
		            				
		            				//Toast.makeText(loginActivity, " "+str, Toast.LENGTH_SHORT).show();
		            				
		            				DBAdapter dbAdapter = new DBAdapter(loginActivity);
		            				dbAdapter.open();
		            				
		            				User user = new User();
		            				user.setEmail(loginActivity.edtEmail.getText().toString());
		            				user.setPassword(loginActivity.edtPassword.getText().toString());
		            				user.setName(jsonObj.getString("FullName"));
		            				user.setToken(jsonObj.getString("Token"));
		            				user.setUserId(jsonObj.getString("UserID"));
		            				user.setPhone(jsonObj.getString("PhoneNumber"));
		            				
		            				//User user = dbAdapter.login(edtEmail.getText().toString(), edtPassword.getText().toString());
		            				
		            				dbAdapter.loginServer(user);
		            				
		            				dbAdapter.close();
		            				if(user != null && user.getEmail().equals(loginActivity.edtEmail.getText().toString())) {
		            				Intent intent = new Intent(loginActivity, HomeActivity2.class);
		            				Bundle bundle = new Bundle();
		            				bundle.putSerializable("User", user);
		            				intent.putExtras(bundle);
		            				loginActivity.startActivity(intent);
		            				loginActivity.finish();
		            				} else {
		            					//Toast.makeText(loginActivity, "  "+str, Toast.LENGTH_SHORT).show();
		            				}
		            			 		            					
		            				} else {
		            					String str = jsonObj.getString("Message");
		            					//Toast.makeText(loginActivity, " "+str, Toast.LENGTH_SHORT).show();
			            				
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
		                        //Toast.makeText(loginActivity, " "+error.getMessage(), Toast.LENGTH_SHORT).show();
		                    }
		                });
		 
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		
			}
	
	  private void buildAlertMessageNoGps() {
		    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
		           .setCancelable(false)
		           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		               public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
		                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		               }
		           })
		           .setNegativeButton("No", new DialogInterface.OnClickListener() {
		               public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
		                    dialog.cancel();
		               }
		           });
		    final AlertDialog alert = builder.create();
		    alert.show();
		}

}
