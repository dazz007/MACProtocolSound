package com.example.androidake;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class InitActivity extends Activity {
	public MutualAuthenticateChip mac;
	private TextView tv_key_gen_res;
	private TextView tv_ekey_gen_res;
	private TextView tv_info;
	private Button but_send_ephemeral_key;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		mac = new MutualAuthenticateChip();
		mac.set_initializator(true);
		mac.prepareMACCPP();
		tv_key_gen_res = (TextView) findViewById(R.id.key_gen_res);
		tv_ekey_gen_res = (TextView) findViewById(R.id.ekey_gen_res);
		tv_key_gen_res.setText("Done");
		tv_ekey_gen_res.setText("Done");
		View b = findViewById(R.id.info_text);
		b.setVisibility(View.GONE);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.init, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void sendEphemeralKey(View view){
		byte[] ephemeralKey = mac.getEphemeralKeyCPP();
		//Here will be conversion from byte to sound
		
		
		//------------------------------------------
		String str = "";
		str = ConverterJava.ByteToString(ephemeralKey);
		HideLayout(R.id.info_layout);
		View b = findViewById(R.id.info_text);
		b.setVisibility(View.VISIBLE);
//		tv_info.setEnabled(true);
		but_send_ephemeral_key = (Button) findViewById(R.id.send_eph_key);
		but_send_ephemeral_key.setVisibility(View.INVISIBLE);
	}
	
	private void HideLayout(int iid){
		LinearLayout myLayout = (LinearLayout) findViewById(iid);
		for ( int i = 0; i < myLayout.getChildCount();  i++ ){
		    View view = myLayout.getChildAt(i);
		    view.setVisibility(View.GONE); // Or whatever you want to do with the view.
		}
	}
}
