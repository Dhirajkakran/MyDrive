package com.example.ogadrive;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.bean.GetLocation;

public class RideEstimateActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ride_estimate);
		
		Bundle bundle = getIntent().getExtras();
		TextView txtPickUp = (TextView) findViewById(R.id.txtPickUp);
		TextView txtDrop = (TextView) findViewById(R.id.txtDrop);
		TextView txtCost = (TextView) findViewById(R.id.txtCost);
		TextView txtApproxTime = (TextView) findViewById(R.id.txtApproxTime);
		
		txtPickUp.setText(((GetLocation)bundle.getSerializable("SrcLocation")).getName());
		txtDrop.setText(((GetLocation)bundle.getSerializable("DestLocation")).getName());
		
		Location locationA = new Location("point A");     
		locationA.setLatitude(((GetLocation)bundle.getSerializable("SrcLocation")).getLatitude()); 
		locationA.setLongitude(((GetLocation)bundle.getSerializable("SrcLocation")).getLongitute());
		Location locationB = new Location("point B");
		locationB.setLatitude(((GetLocation)bundle.getSerializable("DestLocation")).getLatitude()); 
		locationB.setLongitude(((GetLocation)bundle.getSerializable("DestLocation")).getLongitute());
		final float distance = locationA.distanceTo(locationB) / 1000 ;
		
		final float fare = distance * 20;
		
		txtCost.setText(""+fare);
		float time = distance / 20;
		txtApproxTime.setText(""+time+" Hours");
		
		Button btnOK = (Button) findViewById(R.id.btnOK);
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(RideEstimateActivity.this, VehicleCategoryActivity.class);
				
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				Bundle bundle = new Bundle();
				
				bundle.putString("DistanceKM", ""+distance);
				bundle.putString("PickupPlaceID", ""+((GetLocation)getIntent().getExtras().getSerializable("SrcLocation")).getLocationID());
				bundle.putString("DropPlaceID", ""+((GetLocation)getIntent().getExtras().getSerializable("DestLocation")).getLocationID());
				bundle.putString("Fare", ""+fare);
				
				
				
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ride_estimate, menu);
		return true;
	}

	 
}
