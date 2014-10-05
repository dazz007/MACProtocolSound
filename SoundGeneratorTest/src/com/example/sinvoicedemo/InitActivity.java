package com.example.sinvoicedemo;

import java.io.UnsupportedEncodingException;

import com.example.important.Constants;
import com.example.soundgeneratortest.MainActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class InitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.init, menu);
		return true;
	}
	
	public void startReceive(View view) throws UnsupportedEncodingException {
		Intent intent = new Intent(this, ReceiveActivity.class);
	    startActivity(intent);
	}
	
	
	public void startSending(View view) throws UnsupportedEncodingException {
		Intent intent = new Intent(this, SendingActivity.class);
	    startActivity(intent);
	}
	public void startOldActivity(View view) throws UnsupportedEncodingException {
		Intent intent = new Intent(this, MainActivity.class);
	    startActivity(intent);
	}
}
