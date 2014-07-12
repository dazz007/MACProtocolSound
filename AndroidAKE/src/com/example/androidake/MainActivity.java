package com.example.androidake;

import java.io.UnsupportedEncodingException;

import com.example.androidake.InitActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	public MutualAuthenticateChip mac;
//	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	/** Called when the user clicks the Send button */
	public void beInitializator(View view) {
		Intent intent = new Intent(this, InitActivity.class);
	    startActivity(intent);
	}
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		textView = new TextView(this);
//		mac = new MutualAuthenticateChip();
//		mac.prepareMACCPP();
//		byte[] result = mac.show_key_pair();
////		byte[] result = mac.getEphemeralKeyCPP();
//		// String result_str = result.toString();
//		String str = "";
//
//		byte[] ephemr = mac.getEphemeralKeyCPP();
//		str = ConverterJava.ByteToString(ephemr);
//		
//		textView.setText(str);
//		setContentView(textView);
////		setContentView(R.layout.activity_main);
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
