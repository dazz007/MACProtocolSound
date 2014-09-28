package com.example.recorder;

import com.example.important.Buffer;
import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.interfaces.VoiceRecSubject;

public class VoiceGetter {
	private final static String TAG = "VoiceGetter";
	private RecorderAudio recorder_audio;
	private int state;
	private Callback callback;
	private long start;
	private long stop;
	private long diff;
	public VoiceGetter(RecorderAudio ra, Callback callback){
		recorder_audio = ra;
		this.callback = callback;
	}
	
	public static interface Callback{
		public void putBufferToQueue(Buffer buffer);
	}
	
	public void setCallback(Callback callback){
		this.callback = callback;
	}
	
	public void start() {
		if (state == Constants.STOP_STATE) {
			state = Constants.START_STATE;
			//this.recorder_audio.setNotificationPeriod(Constants.DEFAULT_NUM_SAMPLES);
			while (state == Constants.START_STATE) {
				Buffer buffer = this.recorder_audio.getFrameData(Constants.DEFAULT_NUM_SAMPLES);
				if (buffer != null) {
					callback.putBufferToQueue(buffer);
				}
			}
		}
	}
	
}
