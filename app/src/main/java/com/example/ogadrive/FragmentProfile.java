package com.example.ogadrive;




import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

 

public class FragmentProfile extends Fragment {
 
	User user;
	Button btnLogout, btnSave;
	EditText edtEmail, edtPassword,   edtFullName,
	edtPhoneNumber;
	
	ImageView imgEdtEmail,imgEdtPassword, imgEdtFullName, imgEdtPhoneNumber;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		  Bundle bundle = this.getArguments();
		  user = (User) bundle.getSerializable("User");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragment_profile,
				container, false);

		getActivity().setTitle(R.string.profile);

		  btnLogout = (Button) rootView.findViewById(R.id.btnLogout);
		  btnSave = (Button) rootView.findViewById(R.id.btnSave);
		  edtEmail = (EditText) rootView.findViewById(R.id.edtEmail);
			edtPassword = (EditText) rootView.findViewById(R.id.edtPassword);
			//edtConfirmPassword = (EditText) rootView.findViewById(R.id.edtConfirmPassword);
			edtFullName = (EditText) rootView.findViewById(R.id.edtFullName);
			edtPhoneNumber = (EditText) rootView.findViewById(R.id.edtPhoneNumber);
			
			imgEdtEmail = (ImageView) rootView.findViewById(R.id.imgEdtEmail);
			imgEdtFullName = (ImageView) rootView.findViewById(R.id.imgEdtFullName);

			imgEdtPassword = (ImageView) rootView.findViewById(R.id.imgEdtPassword);

			imgEdtPhoneNumber = (ImageView) rootView.findViewById(R.id.imgEdtPhoneNumber);

			 

			
			
 
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		edtEmail.setText(""+user.getEmail());
		edtPassword.setText(""+user.getPassword());
		edtFullName.setText(""+user.getName());
		edtPhoneNumber.setText(""+user.getPhone());
		
		btnLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(user != null) {
				
					volleyRequestLogout(user);
				}
			}
		});
 

		imgEdtEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				edtEmail.setEnabled(true);
				btnSave.setEnabled(true);
			}
		});
imgEdtPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				edtPassword.setEnabled(true);
				btnSave.setEnabled(true);
			}
		});
imgEdtFullName.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		edtFullName.setEnabled(true);
		btnSave.setEnabled(true);
	}
});

imgEdtPhoneNumber.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		edtPhoneNumber.setEnabled(true);
		btnSave.setEnabled(true);
	}
});

  btnSave.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (validate() ) {
			//btnSignUp.setEnabled(true);
			DBAdapter dbAdapter = new DBAdapter(getActivity());
			dbAdapter.open();
			
			//if(!dbAdapter.isUserNameAlreadyExist(edtEmail.getText().toString())) {
			User user = new User();
			user.setEmail(edtEmail.getText().toString());
			user.setPassword(edtPassword.getText().toString());
			user.setName(edtFullName.getText().toString());
			user.setPhone(edtPhoneNumber.getText().toString());
			
		long rowid =	dbAdapter.updateUser(user, (User)getArguments().getSerializable("User"));
			dbAdapter.close();
			if(rowid > 0){
				//Toast.makeText(getActivity(), "Updated SuccessFull", Toast.LENGTH_SHORT).show();
                ((HomeActivity2)getActivity()).setUser(user);
                getFragmentManager().popBackStackImmediate();
				//getActivity().finish();
				
			} else {
				//Toast.makeText(getActivity(), "Updated UnSuccessFull", Toast.LENGTH_SHORT).show();
				
			}
			// Post Data To Server for Registration
	/*	} else {
			Toast.makeText(getActivity(), "Email Already Exist", Toast.LENGTH_SHORT).show();
			dbAdapter.close();
		}*/
		}
	
	}		 
  
});
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
 
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	private boolean validate() {
		// TODO Auto-generated method stub
		if (!isEmailValid(edtEmail.getText())) {

			Toast.makeText(getActivity(),
					"Please Enter Valid Email Address",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (edtPassword.getText().toString().equals("")) {

			Toast.makeText(getActivity(),
					"Please Enter Password",
					Toast.LENGTH_SHORT).show();
			return false;
			
		}
		/*if (edtConfirmPassword.getText().toString().equals("")) {

			Toast.makeText(getActivity(),
					"Please Enter Confirm Password",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (!edtConfirmPassword.getText().toString().equals(edtPassword.getText().toString())) {

			Toast.makeText(getActivity(),
					"Password Mismatch",
					Toast.LENGTH_SHORT).show();
			return false;
		}*/

		if (edtFullName.getText().toString().equals("")) {

			Toast.makeText(getActivity(),
					"Please Enter Name",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (edtPhoneNumber.getText().toString().equals("")) {

			Toast.makeText(getActivity(),
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
 
	
	void volleyRequestLogout(final User user) {
		// Tag used to cancel the request
	final	ArrayList<GetLocation> listLocation = new ArrayList<GetLocation>();
		 
			
		if(Utility.isNetworkAvailable(getActivity())) {
		String tag_json_obj = "json_obj_req";
		 
		final String TAG = "Volley" ;
		String url = "http://ogadrive.com/OgadriveiceServices.svc/Logout";
		         
		final ProgressDialog pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage("Loading...");
		pDialog.show();   
		
		 
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("UserID", user.getUserId());
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
		        					Toast.makeText(getActivity(), " "+str, Toast.LENGTH_SHORT).show();
		        					
		        					DBAdapter dbAdapter = new DBAdapter(getActivity());
		        					dbAdapter.open();
		        					
		        					if(dbAdapter.setLogin(user.getEmail(), user.getPassword(), false)) {
		        						dbAdapter.close();
		        					//	Toast.makeText(getActivity(), "Loged Out Successfully", Toast.LENGTH_SHORT).show();
		        						Intent intent = new Intent(getActivity(), LoginActivity.class);
		        						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        						startActivity(intent);
		        						getActivity().finish();
		        					}
		        					dbAdapter.close();
		        					 
		        					
		        				} else {
	            					String str = jsonObj.getString("Message");
	            					Toast.makeText(getActivity(), " "+str, Toast.LENGTH_SHORT).show();
		            				
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
		                        Toast.makeText(getActivity(),  " "+error.getMessage(), Toast.LENGTH_SHORT).show();
		                    }
		                });
		 
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		} else {
			Toast.makeText(getActivity(), R.string.internet_not_access, Toast.LENGTH_SHORT).show();
		}
		
		 
	}
	
	
	/* void volleyRequestEditProfile(User user) {
		// Tag used to cancel the request
	
			
			if(Utility.isNetworkAvailable(getActivity())) {
		String tag_json_obj = "json_obj_req";
		 
		final String TAG = "Volley" ;
		String url = "http://ogadrive.com/OgadriveiceServices.svc/EditUser";
		         
		final ProgressDialog pDialog = new ProgressDialog(getActivity());
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
		        					ArrayAdapter<GetLocation> locationAdapter = new ArrayAdapter<GetLocation>(getActivity(),
		  						          android.R.layout.simple_list_item_1, android.R.id.text1, listLocation);
		  						
		  						txtAutoCompleteLocation.setAdapter(locationAdapter);
		  						txtAutoCompleteLocation.setThreshold(1);
		        					
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
		                    }
		                });
		 
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		} else {
			Toast.makeText(getActivity(), R.string.internet_not_access, Toast.LENGTH_SHORT).show();
		}
		
		 
	}*/
	

}
