package com.example.sinvoicedemo;

import com.example.important.MessagesLog;
import com.example.recorder.VoiceRecognition;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
	private StringBuilder mTextBuilder;
	private TextView compare_text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive);
		recognized_txt = (TextView) findViewById(R.id.recognized_text);
		compare_text = (TextView) findViewById(R.id.text_to_compare);
		compare_text.setText("SWRlYWx5IHNhIGphayBnd2lhemR5IC0gbmllIG1vem5hIGljaCBvc2lhZ25hYywgYWxlIG1vem5hIHNpZSBuaW1pIGtpZXJvd2FjLg0K");
		mTextBuilder = new StringBuilder();
        handler = new RegHandler(recognized_txt, mTextBuilder);
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
        public static StringBuilder mTextBuilder; // = new StringBuilder();
        private TextView mRecognisedTextView;
        private boolean recognized = false;
        private String string_to_compare = "SWRlYWx5IHNhIGphayBnd2lhemR5IC0gbmllIG1vem5hIGljaCBvc2lhZ25hYywgYWxlIG1vem5hIHNpZSBuaW1pIGtpZXJvd2FjLg0K";
        public RegHandler(TextView textView, StringBuilder txtBuilder) {
            mRecognisedTextView = textView;
            mTextBuilder = txtBuilder;
        }
        
        public void setTxt(StringBuilder txt){
        	mTextBuilder = txt;
        }
        
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_SET_RECG_TEXT:
                char ch = (char) msg.arg1;
//                if(mTextBuilder.toString().charAt(mTextBuilder.toString().length()) == ','){
//                	mTextBuilder.delete(0, mTextBuilder.length());
//                }
                mTextBuilder.append(ch);
                if(recognized){
                	mTextBuilder.delete(0, mTextBuilder.length());
                	recognized = false;
                }
                if(string_to_compare.equals(mTextBuilder.toString())){
                	mTextBuilder.append(" --- no i piêknie");
                	recognized = true;
//                	mTextBuilder.delete(0, mTextBuilder.length());
                }
                
                if (null != mRecognisedTextView) {
                    mRecognisedTextView.setText(mTextBuilder.toString());
                }
                break;

            case MSG_RECG_START:
                mTextBuilder.delete(0, mTextBuilder.length());
                break;

            case MSG_RECG_END:
                MessagesLog.d(TAG, "recognition end");
                break;
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
		Message msg = new Message();
		msg.obj = str;
		for(int i = 0 ; i < str.length(); i++){
			handler.sendMessage(handler.obtainMessage(MSG_SET_RECG_TEXT, str.charAt(i), 0));
		}
		
	}
	
	public void onBackPressed() {
		voicerec.stop();
		this.finish();
	    //moveTaskToBack(true);
	}
	
	@Override
	public void onEndRecogntion() {
		// TODO Auto-generated method stub
		
	}
	
	
}
