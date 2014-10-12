package com.example.sinvoicedemo;

import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.recorder.VoiceRecognition;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiveActivity extends Activity implements VoiceRecognition.Listener{
	private final static String TAG = "ReceiveActivity";
	private VoiceRecognition voicerec;
	private Handler handler;
	private final static int MSG_SET_RECG_TEXT = 1;
	private final static int MSG_RECG_START = 2;
	private final static int MSG_RECG_END = 3;
	private TextView recognized_txt;
	private StringBuilder m_text_builder;
	private TextView compare_text;
	private boolean start_of_message = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive);
		recognized_txt = (TextView) findViewById(R.id.recognized_text);
		compare_text = (TextView) findViewById(R.id.text_to_compare);
		compare_text.setText("TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz"+
		        "IHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg"+
		        "dGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu"+
		        "dWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo"+
		        "ZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4");
		m_text_builder = new StringBuilder();
        handler = new RegHandler(recognized_txt, m_text_builder);
        voicerec = new VoiceRecognition();
        voicerec.setListener(this);
        voicerec.start();
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receive, menu);
		return true;
	}
	
	private static class RegHandler extends Handler {
        public static StringBuilder m_text_builder; // = new StringBuilder();
        private TextView m_recognised_text_view;
        private boolean recognized = false;
        private String string_to_compare = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz"+
							               "IHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg"+
							               "dGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu"+
							               "dWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo"+
							               "ZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4";
        public RegHandler(TextView textView, StringBuilder txtBuilder) {
            m_recognised_text_view = textView;
            m_text_builder = txtBuilder;
        }
        
        public void setTxt(StringBuilder txt){
        	m_text_builder = txt;
        }
        
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_SET_RECG_TEXT:
                char ch = (char) msg.arg1;
                m_text_builder.append(ch);
                if (null != m_recognised_text_view) {
                    m_recognised_text_view.setText(m_text_builder.toString());
                }
                break;

            case MSG_RECG_START:
            	m_recognised_text_view.setBackgroundColor(Color.WHITE);
            	m_text_builder.delete(0, m_text_builder.length());
				m_recognised_text_view.setText("");
                break;

            case MSG_RECG_END:
            	MessagesLog.d(TAG, "Weszlo tutej 3");
            	if(string_to_compare.compareTo(m_text_builder.toString()) == 0){
            		m_recognised_text_view.setBackgroundColor(Color.GREEN);
            	}else{
            		m_recognised_text_view.setBackgroundColor(Color.RED);
            	}
            	MessagesLog.d(TAG, "recognition end");
				break;
			default:
				String message = msg.getData().getString(null);
				m_recognised_text_view.setText("");
				m_recognised_text_view.setText(message);
            }
            super.handleMessage(msg);
        }
    }

	@Override
	public void onStartRecogntion() {
		
		handler.sendEmptyMessage(MSG_RECG_START);
	}

	@Override
	public void onRecognition(String str) {
//		Message msg = new Message();
//		msg.obj = str;
//		for(int i = 0 ; i < str.length(); i++){
//			handler.sendMessage(handler.obtainMessage(MSG_SET_RECG_TEXT, str.charAt(i), 0));
//		}
		if (str.length() > 0)
			if (start_of_message == false) {
				if (str.charAt(0) == Constants.STANDARD_ALPHABET[Constants.STANDARD_ALPHABET.length - 2]) { // start
					start_of_message = true;
					onStartRecogntion();
				}
			} else {
				if (str.charAt(0) == Constants.STANDARD_ALPHABET[Constants.STANDARD_ALPHABET.length - 2]) { // start

				} else if (str.charAt(0) == Constants.STANDARD_ALPHABET[Constants.STANDARD_ALPHABET.length - 1]) { // end data																				
					start_of_message = false;
					MessagesLog.d(TAG, "Weszlo tutej");
					onEndRecogntion();
				} else {
					Message msg = new Message();
					msg.obj = str;
					for (int i = 0; i < str.length(); i++) {
						handler.sendMessage(handler.obtainMessage(
								MSG_SET_RECG_TEXT, str.charAt(i), 0));
					}
				}
			}
	}
	
	public void onBackPressed() {
		voicerec.stop();
		this.finish();
	    //moveTaskToBack(true);
	}
	
	@Override
	public void onEndRecogntion() {
		MessagesLog.d(TAG, "Weszlo tutej 2");
		handler.sendEmptyMessage(MSG_RECG_END);
		
	}
	
	
}
