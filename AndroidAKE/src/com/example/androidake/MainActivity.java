package com.example.androidake;

import java.io.UnsupportedEncodingException;

import org.achartengine.GraphicalView;

import com.example.androidake.InitActivity;
import com.example.graphic.LineGraph;
import com.example.important.Constants;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity{
	public MutualAuthenticateChip mac;
    
//	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		Button but_init = (Button) findViewById(R.id.button1);
		but_init.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	beInitializator(arg0, false);
            }
        });

		Button but_receiv = (Button) findViewById(R.id.button2);
		but_receiv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	beInitializator(arg0, true);
            }
        });
	}
	
	/** Called when the user clicks the Send button */
	public void beInitializator(View view, boolean is_initializator) {
		Intent intent = new Intent(this, InitActivity.class);
		intent.putExtra(Constants.bundle_init_id, is_initializator);
	    startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onBackPressed() {
            this.finish();
	}

}
