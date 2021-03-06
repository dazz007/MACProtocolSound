package com.example.recorder;

import com.example.important.Buffer;
import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.interfaces.VoiceRecSubject;

public class VoiceGetter {
	private final static String TAG = "VoiceGetter";
	private RecorderAudio recorder_audio; //object responsible for record data
	private int state; //state of process
	private Callback callback; // callback
	
	/**
	 * Constructor VoiceGetter class
	 * @param ra - RecorderAudio object responsible for record data
	 * @param callback - callback
	 */
	public VoiceGetter(RecorderAudio ra, Callback callback){
		recorder_audio = ra;
		this.callback = callback;
	}
	
	/**
	 * Interface for callback class.
	 */
	public static interface Callback{
		public void putBufferToQueue(Buffer buffer);
	}
	
	/**
	 * Set callback
	 * @param callback - callback
	 */
	public void setCallback(Callback callback){
		this.callback = callback;
	}
	
	
	/**
	 * Start of getting frame of data
	 */
	public void start() {
		int temp_count = 0;
		int period = Constants.DEFAULT_NUM_SAMPLES /2;
		if (state == Constants.STOP_STATE) {
			state = Constants.START_STATE;
			//this.recorder_audio.setNotificationPeriod(Constants.DEFAULT_NUM_SAMPLES);
			while (state == Constants.START_STATE) {
				Buffer buffer = null;
				if(period % 2 != 0){
				if(temp_count % 2 == 0){
					buffer = this.recorder_audio.getFrameData(period);
				}else{
					buffer = this.recorder_audio.getFrameData(period+1);
				}
				}else{
					buffer = this.recorder_audio.getFrameData(period);
				}
//				Buffer buffer = this.recorder_audio.getFrameData(Constants.DEFAULT_NUM_SAMPLES/2);
				temp_count++;
				if(temp_count == 1000){
					temp_count = 0;
				}
				if (buffer != null) {
					callback.putBufferToQueue(buffer);
				}
			}
		}
	}
	/**
	 * Stop getting frame of data
	 */
	public void stop(){
		if(state == Constants.START_STATE){
			state = Constants.STOP_STATE;
		}
	}
	
}
