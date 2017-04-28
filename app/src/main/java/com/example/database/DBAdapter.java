package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bean.User;

public class DBAdapter extends SQLiteOpenHelper {

	private String TABLE_USER = "user";
	private SQLiteDatabase db;
	private static String DB_NAME = "oga_drive";

	public DBAdapter(Context context) {
		super(context, DB_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	public void open() {
		// TODO Auto-generated method stub
		db = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String tbl_user = "CREATE TABLE 'user' ('_id' INTEGER , 'email' VARCHAR PRIMARY KEY, 'password' VARCHAR, 'name' VARCHAR, 'phone' VARCHAR, 'isLogin' BOOL DEFAULT false, 'token' VARCHAR, 'userId' VARCHAR)";

		db.execSQL(tbl_user);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public long registerUser(User user) {

		//SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("email", user.getEmail());
		contentValues.put("password", user.getPassword());
		contentValues.put("name", user.getName());
		contentValues.put("phone", user.getPhone());
		contentValues.put("userId", user.getUserId());
		contentValues.put("token", user.getToken());
		
		long rowId = db.insert(TABLE_USER, null, contentValues);
		Log.e("DB", "Inserted Id " + rowId);
		return rowId;

	}
	
	public long updateUser(User userNew, User userOld) {

		//SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("email", userNew.getEmail());
		contentValues.put("password", userNew.getPassword());
		contentValues.put("name", userNew.getName());
		contentValues.put("phone", userNew.getPhone());

		contentValues.put("userId", userNew.getUserId());
		contentValues.put("token", userNew.getToken());
 
		long rowId = db.update(TABLE_USER, contentValues, "email = ? and password = ? ", new String[]{userOld.getEmail(), userOld.getPassword() });
		Log.e("DB", "Updated rows " + rowId);
		return rowId;

	}
	
	public User login(String userName, String passWord) {
		
		 User user = null;
		Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+" where ( email ='"+userName +"' and password ='"+passWord+"')", null);
	 if(cursor != null && cursor.moveToFirst() ){
		 do {
			 user = new User();
			 user.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
			 user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
			 user.setName(cursor.getString(cursor.getColumnIndex("name")));
			 user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
			 user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
			 user.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
			 user.setToken(cursor.getString(cursor.getColumnIndex("token")));
			if( setLogin(userName, passWord, true)== false) {
				
				
				user = null;
			}
		 } while(cursor.moveToNext());
		 cursor.close();
	 } else {
		
		 //registerUser(user);
	 }
	 
	return user;
	}
	
	
	public long loginServer(User user) {
		
		//SQLiteDatabase db = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("email", user.getEmail());
		contentValues.put("password", user.getPassword());
		contentValues.put("name", user.getName());
		contentValues.put("phone", user.getPhone());
		contentValues.put("userId", user.getUserId());
		contentValues.put("token", user.getToken());
		contentValues.put("isLogin", true);
		
		long rowId = db.insert(TABLE_USER, null, contentValues);
		if(rowId == -1.00) {
	 rowId = db.update(TABLE_USER, contentValues, "email = ? and password = ? ", new String[]{user.getEmail(), user.getPassword() });
			
		}
		Log.e("DB", "Inserted Id " + rowId);
		return rowId;

	 

	}
	public boolean setLogin(String userName, String passWord, boolean isLogin) {
		
		
		ContentValues contentValues = new ContentValues();
		 
		contentValues.put("isLogin", isLogin);
		
		long rowId = db.update(TABLE_USER, contentValues, "email = ? and password = ? ", new String[]{userName, passWord });
		Log.e("DB", "Inserted Id " + rowId);
		if(rowId >0 && rowId < 2) {
			return true;
		}
		return false;
		
	}
	
	public User isLoogin( ) {
		
		 User user = null;
		Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+" where ( isLogin = '1')", null);
	 if(cursor != null ){
		 while(cursor.moveToNext()) {
			 user = new User();
			 user.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
			 user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
			 user.setName(cursor.getString(cursor.getColumnIndex("name")));
			 user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
			 user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
			 user.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
			 user.setToken(cursor.getString(cursor.getColumnIndex("token")));
			 
		 }
		 cursor.close();
	 }
	return user;
	}
	
	public boolean isUserNameAlreadyExist(String userName) {
		
		 User user = null;
		 boolean isExist = false;
		Cursor cursor =db.rawQuery("Select * from "+TABLE_USER+" where ( email ='"+userName+"')", null);
	 if(cursor != null ){
		 while(cursor.moveToNext()) {
			 user = new User();
			 user.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
			 user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
			 user.setName(cursor.getString(cursor.getColumnIndex("name")));
			 user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
			 user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
			 user.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
			 user.setToken(cursor.getString(cursor.getColumnIndex("token")));
			 
			 isExist = true;
		 }
		 cursor.close();
	 }
	return isExist;
	}
	
	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		db.close();
		super.close();
	}

}
