package com.example.ogadrive;

 
 



import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bean.BookingHistory;
import com.example.bean.GetLocation;
import com.example.bean.PushResponse;
import com.example.bean.User;
import com.example.database.DBAdapter;
import com.example.utility.Config;
import com.example.utility.Utility;
import com.example.volley.AppController;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;


public class FragmentHistory extends ListFragment {
 
	User user;
	 public static ArrayList<PushResponse> listHistory = new ArrayList<PushResponse>();
	 static HistoryAdapter historyAdapter;


    public static  void addPush(PushResponse pushResponse) {
        listHistory.add(pushResponse);
        if(historyAdapter != null) {
            historyAdapter.notifyDataSetChanged();
        }
    }
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
		/*View rootView = inflater.inflate(R.layout.fragment_profile,
				container, false);*/

		getActivity().setTitle(R.string.app_name);


/*
PushResponse pushResponse = new PushResponse();
        pushResponse.setUserId("123456");
        pushResponse.setAccepted(true);
        pushResponse.setMessage("Heello! this is not a phsylosphy class");
*/

        //listHistory.add(pushResponse);
		  historyAdapter = new HistoryAdapter(getActivity(), listHistory, user);
		setListAdapter(historyAdapter); 

		  
		   return super.onCreateView(inflater, container, savedInstanceState);  
			
 
		 
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		Intent intent = new Intent(getActivity(), HistoryDetailsActivity.class);
        PushResponse pushResponse = listHistory.get(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable("PushResponse", pushResponse);
		intent.putExtras(bundle);
		startActivity(intent);
		
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//volleyRequest(user);

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

        View  emptyView = getActivity().getLayoutInflater().inflate(R.layout.empty_view, null);

        ((ViewGroup)getListView().getParent()).addView(emptyView);
        getListView().setEmptyView(emptyView);
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

 


	boolean isEmailValid(CharSequence target) {
		if (target == null)
			return false;
		else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}

	}
 
	
	void volleyRequest(final User user) {
		// Tag used to cancel the request
	//final	ArrayList<GetLocation> listLocation = new ArrayList<GetLocation>();
		 
			
		if(Utility.isNetworkAvailable(getActivity())) {
		String tag_json_obj = "json_obj_req";
		 
		final String TAG = "Volley" ;
		String url = "http://ogadrive.com/OgadriveiceServices.svc/BookingHistory/"+user.getToken();
		         
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
		        					
		        					String str = jsonObj.getString("Message");
		        					//Toast.makeText(getActivity(), " "+str, Toast.LENGTH_SHORT).show();
		        					
		         					
		        					JSONArray jsonArray = jsonObj.getJSONArray("BookingHistory");
		        					for(int i=0; i < jsonArray.length(); i++) {
		        						BookingHistory bookingHistory = new BookingHistory();
		        					JSONObject bookingObj =	(JSONObject) jsonArray.get(i);
		        						bookingHistory.setPickupLocation(bookingObj.getString("PickupLocation"));
		        						bookingHistory.setDropLocation(bookingObj.getString("DropLocation"));
		        						bookingHistory.setStatus(bookingObj.getString("Status"));
		        						
		        						bookingHistory.setBookingDateTime(bookingObj.getString("BookingDateTime"));
		        						bookingHistory.setFare(bookingObj.getString("Fare"));
		        						bookingHistory.setDistanceKM(bookingObj.getString("DistanceKM"));
		        						bookingHistory.setBookingId(bookingObj.getString("BookingID"));
		        						
		        						//listHistory.add(bookingHistory);
		        					}
	historyAdapter.notifyDataSetChanged();	        					
		        					
		        				} else {
		        					String str = jsonObj.getString("Message");
		        					//Toast.makeText(getActivity(), " "+str, Toast.LENGTH_SHORT).show();
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
		                       // Toast.makeText(getActivity(), " "+error.getMessage(), Toast.LENGTH_SHORT).show();
		                    }
		                });
		 
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
		} else {
			Toast.makeText(getActivity(), R.string.internet_not_access, Toast.LENGTH_SHORT).show();
		}
		
		 
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
                   // Toast.makeText(getActivity(), " "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        } else {
            Toast.makeText(getActivity(), R.string.internet_not_access, Toast.LENGTH_SHORT).show();
        }
    }


}
