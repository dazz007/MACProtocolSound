package com.example.androidake;

import java.io.UnsupportedEncodingException;

import org.achartengine.GraphicalView;

import com.example.androidake.InitActivity;
import com.example.graphic.LineGraph;
import com.example.important.Constants;
import com.example.important.Constants.RANGE;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemSelectedListener {
	public MutualAuthenticateChip mac;
    private RANGE range; 
	
//	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		range = Constants.RANGE.RA_150;
		
		Spinner spinner = (Spinner) findViewById(R.id.frequencies_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.frequencies_spinner, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(this);
		
		Button but_init = (Button) findViewById(R.id.confirm_settings);
		but_init.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	beInitializator(arg0, range);
            }
        });
		
//		Button but_init = (Button) findViewById(R.id.button1);
//		but_init.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//            	beInitializator(arg0, false);
//            }
//        });
//
//		Button but_receiv = (Button) findViewById(R.id.button2);
//		but_receiv.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//            	beInitializator(arg0, true);
//            }
//        });
	}
	
	/** Called when the user clicks the Send button */
	public void beInitializator(View view, RANGE i_range) {
		Intent intent = new Intent(this, InitActivity.class);
		intent.putExtra(Constants.bundle_init_id, i_range);
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		
		Toast.makeText(parent.getContext(), 
		        "Selected : " + parent.getItemAtPosition(pos).toString(),
		        Toast.LENGTH_SHORT).show();
		
		if(pos == 0){
			range = Constants.RANGE.RA_150;
		}else if(pos == 1){
			range = Constants.RANGE.RA_130;
		}else if(pos == 2){
			range = Constants.RANGE.RA_100;
		}else if(pos == 3){
			range = Constants.RANGE.RA_100_2;
		}else if(pos == 4){
			range = Constants.RANGE.RA_100_3;
		}else{
			range = Constants.RANGE.RA_150;
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

}
