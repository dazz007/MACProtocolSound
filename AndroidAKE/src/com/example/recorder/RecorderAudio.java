package com.example.recorder;


import com.example.important.Buffer;
import com.example.important.Constants;
import com.example.important.MessagesLog;

import android.media.AudioRecord;

public class RecorderAudio{
	private final static String TAG = "RecorderAudio";
	private AudioRecord recorder;
	private int state;
	
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
		//buffer_size = Constants.DEFAULT_BUFFER_SIZE_REC;
//		buffer_size = Constants.DEFAULT_NUM_SAMPLES;
	}
	
	public void start(){
		if(state == Constants.STOP_STATE){
			if(recorder != null){
				state = Constants.START_STATE;
				recorder.startRecording();
				MessagesLog.d(TAG, "Rozpoczêcie nagrywania");
			}
		}
	}
	
	public void stop(){
		if(state == Constants.START_STATE){
			if(recorder != null){
				recorder.stop();
				recorder.release();
			}
			state = Constants.STOP_STATE;
		}
	}
	
	public Buffer getFrameData(int period){
		Buffer data = new Buffer(period);
		data.initializeBufferShort(period);
		data.setSize(period);
		long time = System.currentTimeMillis();
		int size = recorder.read(data.buffer_short,0,period);
		
		if(data.buffer_short != null){
			data.setTime(time);
			data.setSize(size);
			return data;
		}
		return null;
	}
	
//	public Buffer getFrameData(){
//		Buffer data = new Buffer(buffer_size);
//		data.initializeBufferShort(buffer_size);
//		data.setSize(buffer_size);
//		long time = System.currentTimeMillis();
//		int size = recorder.read(data.buffer_short,0,buffer_size);
//		
//		if(data.buffer_short != null){
//			data.setTime(time);
//			data.setSize(size);
//			return data;
//		}
//		return null;
//	}

//	@Override
//	public void notifyObserver(int[] data) {
//		this.recorder_audio_observer.sendDataToGraph(data);
//		
//	}
//
//	@Override
//	public void notifyObserverByte(byte[] data) {
//		this.recorder_audio_observer.sendDataToGraphByte(data);
//		
//	}

//	@Override
//	public void notifyObserverBuffer(Buffer buffer) {
//		this.recorder_audio_observer.putBufferToQueue(buffer);
//		
//	}
	
}
