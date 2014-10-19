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
		edit_text.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/,.");
//		edit_text.setText("TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz"+
//					      "IHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg"+
//					      "dGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu"+
//					      "dWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo"+
//					      "ZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4");
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
