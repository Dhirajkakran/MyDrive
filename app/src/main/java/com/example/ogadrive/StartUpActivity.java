
package com.example.ogadrive;

import com.example.bean.User;
import com.example.database.DBAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartUpActivity extends ActionBarActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_up);
		
		Button buttonLogin = (Button) findViewById(R.id.btnLogin);
		//Button btnRegister = (Button) findViewById(R.id.btnRegister);
		buttonLogin.setOnClickListener(this);
		//btnRegister.setOnClickListener(this);
		
		DBAdapter dbAdapter = new DBAdapter(StartUpActivity.this);
		dbAdapter.open();
		
		User user = dbAdapter.isLoogin();
		dbAdapter.close();
		if(user != null) {
	
		Intent intent = new Intent(StartUpActivity.this, HomeActivity2.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("User", user);
		intent.putExtras(bundle);
	
		startActivity(intent);
		finish();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_up, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.btnRegister:
//			Intent intent = new Intent(StartUpActivity.this, RegisterActivity.class);
//			startActivity(intent);
//			break;
			
		case R.id.btnLogin :
			
			Intent intentLogin = new Intent(StartUpActivity.this, LoginActivity.class);
			startActivity(intentLogin);
		break;

		default:
			break;
		}
	}

}
