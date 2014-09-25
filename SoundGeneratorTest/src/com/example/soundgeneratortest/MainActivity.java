package com.example.soundgeneratortest;

import org.achartengine.GraphicalView;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.graphic.LineGraph;
import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.important.SoundGenerator;
import com.example.recorder.VoiceRecognition;
import com.example.sinvoicedemo.R;

public class MainActivity extends Activity implements VoiceRecognition.Listener{
    private final static String TAG = "MainActivity";
    private SoundGenerator soundgen;
    private VoiceRecognition voicerec;
//    private final static String CODEBOOK = "123456789ABCDEF";
    private final static String CODEBOOK = "12345";
    private LinearLayout chart_container;
    private LinearLayout chart_container_fft;
    private static GraphicalView graph_view;
    private static GraphicalView graph_view_fft;
	private LineGraph line = new LineGraph(false);
	private LineGraph line_fft = new LineGraph(true);
	private static Thread graphThread;
	private Handler handler;
    private final static int MSG_SET_RECG_TEXT = 1;
    private final static int MSG_RECG_START = 2;
    private final static int MSG_RECG_END = 3;
    private EditText edit_text;
    private TextView recognized_txt;
    private StringBuilder mTextBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chart_container = (LinearLayout)findViewById(R.id.Chart_layout);
        graph_view = line.getView(getBaseContext());
        chart_container.addView(graph_view);
        
        chart_container_fft = (LinearLayout)findViewById(R.id.Chart_layout2);
        graph_view_fft = line_fft.getView(getBaseContext());
        chart_container_fft.addView(graph_view_fft);
        
        
        edit_text = (EditText) findViewById(R.id.launch_codes);
        recognized_txt = (TextView) findViewById(R.id.regtext2);
        mTextBuilder = new StringBuilder();
        handler = new RegHandler(recognized_txt, mTextBuilder);
        
        final TextView playTextView = (TextView) findViewById(R.id.playtext);
        TextView recognisedTextView = (TextView) findViewById(R.id.regtext);
        
        soundgen = new SoundGenerator(Constants.SAMPLING);
        voicerec = new VoiceRecognition();
        voicerec.setListener(this);
        line.setSubject(voicerec);
        voicerec.register(line);
        line_fft.setDecSubject(voicerec.getDecoder());
        edit_text.setText("1234567777");
        Button playStart = (Button) this.findViewById(R.id.start_play);
        playStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String text = edit_text.getText().toString();
                mTextBuilder.delete(0, mTextBuilder.length());
                recognized_txt.setText("");
                MessagesLog.d(TAG, "Kliknalem");
                playTextView.setText(text);
                soundgen.setTextToEncode(text);
                soundgen.start();
            }
        });
        
        line.start(false);
        line_fft.start(true);
        voicerec.start();
        
    }
    
    
    private static class RegHandler extends Handler {
        public static StringBuilder mTextBuilder; // = new StringBuilder();
        private TextView mRecognisedTextView;

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
                mTextBuilder.append(ch);
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
		checkPattern();
	}


	@Override
	public void onEndRecogntion() {
		// TODO Auto-generated method stub
		
	}

	
	public void checkPattern(){
//		if(RegHandler.mTextBuilder.toString().equals(edit_text.getText().toString())){
//			String str = edit_text.getText().toString();
//			edit_text.setText(str+"    Hurrra!!!!!!!!");
//		}
	}

}
