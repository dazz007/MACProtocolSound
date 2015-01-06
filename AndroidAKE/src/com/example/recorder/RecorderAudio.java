package com.example.recorder;


import com.example.important.Buffer;
import com.example.important.Constants;
import com.example.important.MessagesLog;

import android.media.AudioRecord;

public class RecorderAudio{
	private final static String TAG = "RecorderAudio";
	private AudioRecord recorder;
	private int state;
	
	/**
	 * Constructor of RecorderAudio class.
	 * @param source - MIC
	 * @param sample_rate - Sample rate 44100
	 * @param channel_config - channel in MONO
	 * @param audio_format - encoding PCM in 16 bit
	 * @param buffer_size_in_bytes - size of buffers
	 */
	public RecorderAudio(int source, int sample_rate, int channel_config, int audio_format, int buffer_size_in_bytes){
		int minBufferSize = AudioRecord.getMinBufferSize(sample_rate, channel_config, audio_format);
		if(minBufferSize < Constants.DEFAULT_NUM_SAMPLES){
			minBufferSize = Constants.DEFAULT_NUM_SAMPLES;
		}
		recorder = new AudioRecord(source,
								   sample_rate,
								   channel_config,
								   audio_format, 
								   minBufferSize);
		state = Constants.STOP_STATE;
	}
	
	/**
	 * Start recording
	 */
	public void start(){
		if(state == Constants.STOP_STATE){
			if(recorder != null){
				state = Constants.START_STATE;
				recorder.startRecording();
				MessagesLog.d(TAG, "Rozpoczêcie nagrywania");
			}
		}
	}
	
	/**
	 * Stop recording
	 */
	public void stop(){
		if(state == Constants.START_STATE){
			if(recorder != null){
				recorder.stop();
//				recorder.release();
			}
			state = Constants.STOP_STATE;
		}
	}
	
	/**
	 * Method to get frame of recorded data
	 * @param period - period in which have to be frame get
	 * @return Buffer with recorded data
	 */
	public Buffer getFrameData(int period){
		Buffer data = new Buffer(period);
		data.initializeBufferShort(period);
		data.setSize(period);
		long time = System.currentTimeMillis();
		int size = recorder.read(data.buffer_short,0,period);
		
//		for(int i = 0 ; i < size; i++){
//			data.buffer_short[i] = (short)Math.min((int)(data.buffer_short[i] * 1), (int)Short.MAX_VALUE);
//		}
		
		if(data.buffer_short != null){
			data.setTime(time);
			data.setSize(size);
			return data;
		}
		return null;
	}
	
	
}
