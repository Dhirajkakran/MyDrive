package com.example.ogadrive;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.example.bean.BookingHistory;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HistoryDetailsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_details);
		
		BookingHistory bookingHistory = (BookingHistory) getIntent().getExtras().getSerializable("BookingHistory");
		
		TextView txtBookingID = (TextView) findViewById(R.id.txtBookingID);
		TextView txtPickupLocation = (TextView) findViewById(R.id.txtPickupLocation);
		TextView txtDropLocation = (TextView) findViewById(R.id.txtDropLocation);
		TextView txtFare = (TextView) findViewById(R.id.txtFare);
		TextView txtDistanceKM = (TextView) findViewById(R.id.txtDistanceKM);
		TextView txtStatus = (TextView) findViewById(R.id.txtStatus);
		TextView txtBookingDateTime = (TextView) findViewById(R.id.txtBookingDateTime);
		
		txtBookingID.setText(bookingHistory.getBookingId());
		txtPickupLocation.setText(bookingHistory.getPickupLocation());
		txtDropLocation.setText(bookingHistory.getDropLocation());
		txtFare.setText(bookingHistory.getFare());
		txtDistanceKM.setText(bookingHistory.getDistanceKM());
		txtStatus.setText(bookingHistory.getStatus());
		try {
		SimpleDateFormat fmt = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy");
		 String date = fmt.format(new Date(Long.parseLong(bookingHistory.getBookingDateTime())));
		
		txtBookingDateTime.setText(date);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		Button button = (Button) findViewById(R.id.btnOk);
		button.setOnClickListener(new OnClickListener() {
			
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
		getMenuInflater().inflate(R.menu.history_details, menu);
		return true;
	}

}
