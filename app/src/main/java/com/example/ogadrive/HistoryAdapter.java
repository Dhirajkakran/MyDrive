package com.example.ogadrive;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bean.BookingHistory;
import com.example.bean.PushResponse;
import com.example.bean.User;
import com.example.utility.Utility;
import com.example.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoryAdapter extends ArrayAdapter<PushResponse> {

    private Context context;
    private User user;
	private ArrayList<PushResponse>  listHistory;
	HistoryAdapter(Context context, ArrayList<PushResponse> listHistory, User user) {
		super(context, android.R.id.text1);
		this.listHistory = listHistory;
        this.context = context;
        this.user = user;

		}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listHistory.size();
	}

	@Override
	public  PushResponse getItem(int arg0) {
		// TODO Auto-generated method stub
		return listHistory.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View row, ViewGroup arg2) {
		// TODO Auto-generated method stub
		 Holder holder ;
		if(row == null) {
			  LayoutInflater layoutInflater=LayoutInflater.from(getContext());
			    row=layoutInflater.inflate(R.layout.history_row,null,false);
			      holder = new Holder();
			      holder.txtViewMessage =  (TextView) row.findViewById(R.id.txtViewMessage);
			      holder.txtViewUserId =  (TextView) row.findViewById(R.id.txtViewUserId);
                  holder.btnAccept = (Button) row.findViewById(R.id.btnAccept);
                  holder.btnNotification = (Button) row.findViewById(R.id.btnNotification);
			      row.setTag(holder);
			    
		} else {
			holder = (Holder) row.getTag();
		}

      final  PushResponse pushResponse = listHistory.get(position);
		


		holder.txtViewMessage.setText(pushResponse.getMessage());

		holder.txtViewUserId.setText(pushResponse.getUserId());


        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptBooking(pushResponse);
            }
        });

        holder.btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   sendNotificationToUser(pushResponse);
            }
        });

        if(pushResponse.isAccepted()) {
            holder.btnAccept.setVisibility(View.INVISIBLE);
            holder.btnNotification.setVisibility(View.VISIBLE);
        } else {
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnNotification.setVisibility(View.INVISIBLE);
        }




		return row;
	}

    private void sendNotificationToUser(PushResponse pushResponse) {




            if(Utility.isNetworkAvailable(context)) {
                String tag_json_obj = "json_obj_req";

                final String TAG = "Volley" ;
                String url = "http://ogadrive.com//OgadriveiceServices.svc/SendNotificationToCustomer";

                final ProgressDialog pDialog = new ProgressDialog(context);
                pDialog.setMessage("Loading...");
                pDialog.show();


                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Token", ""+user.getToken());
                    jsonObject.put("Mode", "1");
                    jsonObject.put("UserIDForSendPushNotification", pushResponse.getUserId() );
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject jsonObj) {
                                Log.d(TAG, jsonObj.toString());
                                pDialog.hide();


                                try {
                                    if(jsonObj.getString("IsSuccess").equals("true")) {

                                        String str = jsonObj.getString("Message");
                                        //Toast.makeText(context, " " + str, Toast.LENGTH_SHORT).show();




                                    } else {
                                        String str = jsonObj.getString("Message");
                                       // Toast.makeText(context, " "+str, Toast.LENGTH_SHORT).show();
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
                       // Toast.makeText(context, " "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
            } else {
                Toast.makeText(context, R.string.internet_not_access, Toast.LENGTH_SHORT).show();
            }




    }

    private void acceptBooking(final PushResponse pushResponse) {



            if(Utility.isNetworkAvailable(context)) {
                String tag_json_obj = "json_obj_req";

                final String TAG = "Volley" ;
               String url = "http://Ogadrive.com/OgadriveiceServices.svc/AcceptBooking";

               // String url = "http://localhost:1615/OgadriveiceServices.svc/SendPush/";

                final ProgressDialog pDialog = new ProgressDialog(context);
                pDialog.setMessage("Loading...");
                pDialog.show();


                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Token", ""+user.getToken());
                    jsonObject.put("Mode", "0");
                    jsonObject.put("UserIDForSendPushNotification", pushResponse.getUserId() );
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject jsonObj) {
                                Log.d(TAG, jsonObj.toString());
                                pDialog.hide();


                                try {
                                    if(jsonObj.getString("IsSuccess").equals("true")) {

                                        String str = jsonObj.getString("Message");
                                       // Toast.makeText(context, " " + str, Toast.LENGTH_SHORT).show();

                                        pushResponse.setAccepted(true);

                                        notifyDataSetChanged();

                                    } else {
                                        String str = jsonObj.getString("Message");
                                       // Toast.makeText(context, " "+str, Toast.LENGTH_SHORT).show();
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
                      //  Toast.makeText(context, " "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
            } else {
                Toast.makeText(context, R.string.internet_not_access, Toast.LENGTH_SHORT).show();
            }



    }





    static class Holder {
		TextView txtViewMessage;
		TextView txtViewUserId;
        Button btnAccept;
        Button btnNotification;

	}
}
