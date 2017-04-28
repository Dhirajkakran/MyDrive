package com.example.ogadrive;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragement extends Fragment       {

	//private GoogleMap googleMap;

	private int position;
	//private com.google.android.gms.maps.MapFragment fragmentMap;
	
	//Button btnHatchBack, btnSD,  btnSUV;
	
	private AutoCompleteTextView  txtAutoCompleteLocation, txtAutoCompleteLocationDest;

	private GetLocation selectedLocation;
	//private LocationManager locationManager;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
 
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.fragement_bingi_bus,
				container, false);

		getActivity().setTitle(R.string.app_name);

		// String[] source_array =
		// getActivity().getResources().getStringArray(R.array.source_array);
		// String[] dest_array =
		// getActivity().getResources().getStringArray(R.array.source_array);
		// String[] bus_array =
		// getActivity().getResources().getStringArray(R.array.bus_array);
		/*
		 * ArrayAdapter<String> sourceAdapter = new
		 * ArrayAdapter<String>(getActivity(),
		 * android.R.layout.simple_list_item_1, android.R.id.text1,
		 * source_array);
		 * 
		 * ArrayAdapter<String> destAdapter = new
		 * ArrayAdapter<String>(getActivity(),
		 * android.R.layout.simple_list_item_1, android.R.id.text1, dest_array);
		 * 
		 * ArrayAdapter<String> busAdapter = new
		 * ArrayAdapter<String>(getActivity(),
		 * android.R.layout.simple_list_item_1, android.R.id.text1, bus_array);
		 */


		
		//fragmentMap = new com.google.android.gms.maps.MapFragment();

		txtAutoCompleteLocation = (AutoCompleteTextView) rootView.findViewById(R.id.txtAutoCompleteLocation);
        txtAutoCompleteLocationDest = (AutoCompleteTextView) rootView.findViewById(R.id.txtAutoCompleteLocationDest);
		/*FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.containerMap, fragmentMap);*/

		//transaction.commit();

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);



        Bundle bundle = this.getArguments();
        final User user = (User) bundle.getSerializable("User");
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
            InstanceID instanceID = InstanceID.getInstance(getActivity());
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            //Toast.makeText(getActivity(), "Token "+token, Toast.LENGTH_SHORT).show();
                Log.e("Token", "Token : "+token);

                sendToken(token, user);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            }
    }).start();



		final ArrayList<GetLocation> listLocation =  volleyRequest(user);
		/*ArrayAdapter<GetLocation> locationAdapter = new ArrayAdapter<GetLocation>(getActivity(),
		          android.R.layout.simple_list_item_1, android.R.id.text1, listLocation);
		
		txtAutoCompleteLocation.setAdapter(locationAdapter);
		txtAutoCompleteLocation.setThreshold(1);*/
		txtAutoCompleteLocation.setOnTouchListener(new View.OnTouchListener(){
			   @Override
			   public boolean onTouch(View v, MotionEvent event){
				   txtAutoCompleteLocation.showDropDown();
			      return false;
			   }
			});

        txtAutoCompleteLocationDest.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                txtAutoCompleteLocationDest.showDropDown();
                return false;
            }
        });
		txtAutoCompleteLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				selectedLocation = listLocation.get(arg2);

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



	
	ArrayList<GetLocation> volleyRequest(User user) {
		// Tag used to cancel the request
	final	ArrayList<GetLocation> listLocation = new ArrayList<GetLocation>();
		if(Config.isLocal) {
			String string = Utility.readFile(getActivity(), "GetLocations");
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
						ArrayAdapter<GetLocation> locationAdapter = new ArrayAdapter<GetLocation>(getActivity(),
						          android.R.layout.simple_list_item_1, android.R.id.text1, listLocation);
						
						txtAutoCompleteLocation.setAdapter(locationAdapter);
						txtAutoCompleteLocation.setThreshold(1);


                        txtAutoCompleteLocationDest.setAdapter(locationAdapter);
                        txtAutoCompleteLocationDest.setThreshold(1);
					}
					
				} else {
					String str = jsonObj.getString("Message");
					//Toast.makeText(getActivity(), " "+str, Toast.LENGTH_SHORT).show();
    				
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			
			if(Utility.isNetworkAvailable(getActivity())) {
		String tag_json_obj = "json_obj_req";
		 
		final String TAG = "Volley" ;
		String url = "http://ogadrive.com/OgadriveiceServices.svc/Locations/"+user.getToken();
		         
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

                                    txtAutoCompleteLocationDest.setAdapter(locationAdapter);
                                    txtAutoCompleteLocationDest.setThreshold(1);
		        					
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
		                        //Toast.makeText(getActivity(), " "+error.getMessage(), Toast.LENGTH_SHORT).show();
		                    }
		                });
		 
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		} else {
			Toast.makeText(getActivity(), R.string.internet_not_access, Toast.LENGTH_SHORT).show();
		}
		}
		return listLocation;
	}


    void sendToken(String token, User user) {

        if(Utility.isNetworkAvailable(getActivity())) {
            String tag_json_obj = "json_obj_req";

            final String TAG = "Volley" ;
            String url = "http://ogadrive.com/OgadriveiceServices.svc/UpdateDevicePushToken";

           /* final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();*/

            JSONObject jsonObject = new JSONObject();

            try {

                jsonObject.put("Token", user.getToken());
                jsonObject.put("DevicePushToken", token);
                jsonObject.put("DeviceType", "Android");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject jsonObj) {
                            Log.d(TAG, jsonObj.toString());
                          //  pDialog.hide();

                            try {
                                if(jsonObj.getString("IsSuccess").equals("true")){


                                  //  Toast.makeText(getActivity(), " "+jsonObj.getString("Message"), Toast.LENGTH_SHORT).show();
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
                   // pDialog.hide();
                  //  Toast.makeText(getActivity(), " "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        } else {
           // Toast.makeText(getActivity(), R.string.internet_not_access, Toast.LENGTH_SHORT).show();
        }
    }
}
