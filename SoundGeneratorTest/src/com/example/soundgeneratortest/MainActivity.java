package com.example.soundgeneratortest;

import org.achartengine.GraphicalView;

import com.example.graphic.MockData;
import com.example.graphic.Point;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.graphic.LineGraph;
import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.important.SoundGenerator;
import com.example.recorder.VoiceRecognition;
import com.example.sinvoicedemo.R;

public class MainActivity extends Activity {
    private final static String TAG = "MainActivity";
    private SoundGenerator soundgen;
    private VoiceRecognition voicerec;
//    private final static String CODEBOOK = "123456789ABCDEF";
    private final static String CODEBOOK = "12345";
    private LinearLayout chart_container;
    private LinearLayout chart_container_fft;
    private static GraphicalView graph_view;
    private static GraphicalView graph_view_fft;
	private LineGraph line = new LineGraph();
	private LineGraph line_fft = new LineGraph();
	private static Thread graphThread;
	
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
        
        final TextView playTextView = (TextView) findViewById(R.id.playtext);
        TextView recognisedTextView = (TextView) findViewById(R.id.regtext);
        
        soundgen = new SoundGenerator(Constants.SAMPLING);
        voicerec = new VoiceRecognition();
//        mHanlder = new RegHandler(recognisedTextView);
        line.setSubject(voicerec);
        voicerec.register(line);
        line_fft.setDecSubject(voicerec.getDecoder());
//        soundgen.register(line);
        Button playStart = (Button) this.findViewById(R.id.start_play);
        playStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
//            	 String text = genText(7);
                String text = "888888888888888";
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



}
