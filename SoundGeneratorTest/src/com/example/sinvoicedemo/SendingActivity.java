package com.example.sinvoicedemo;

import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.important.SoundGenerator;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SendingActivity extends Activity {
	private final static String TAG = "SendingActivity";
    private SoundGenerator soundgen;
    private EditText edit_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sending);
		
		edit_text = (EditText) findViewById(R.id.text_to_send);
		
		soundgen = new SoundGenerator(Constants.SAMPLING);
		edit_text.setText("SWRlYWx5IHNhIGphayBnd2lhemR5IC0gbmllIG1vem5hIGljaCBvc2lhZ25hYywgYWxlIG1vem5hIHNpZSBuaW1pIGtpZXJvd2FjLg0K");
		Button playStart = (Button) this.findViewById(R.id.start_to_play);
		
		playStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String text = edit_text.getText().toString();
                MessagesLog.d(TAG, "Kliknalem");
                soundgen.setTextToEncode(text);
                soundgen.start();
            }
        });
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sending, menu);
		return true;
	}

}