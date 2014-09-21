package com.example.recorder;

import java.util.ArrayList;

import com.example.important.Buffer;
import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.interfaces.RecorderAudioObserver;
import com.example.interfaces.RecorderAudioSubject;

import android.media.AudioRecord;

public class RecorderAudio implements RecorderAudioSubject{
	private final static String TAG = "RecorderAudio";
	private AudioRecord recorder;
	private int state;
	private int buffer_size = Constants.DEFAULT_BUFFER_SIZE;
	private RecorderAudioObserver recorder_audio_observer;
	private ArrayList<Buffer> queueWithBufferToPrintAL;
	public RecorderAudio(int source, int sample_rate, int channel_config, int audio_format, int buffer_size_in_bytes){
		int minBufferSize = AudioRecord.getMinBufferSize(sample_rate, channel_config, audio_format);
		recorder = new AudioRecord(source,
								   sample_rate,
								   channel_config,
								   audio_format, 
								   minBufferSize);
		buffer_size = Constants.DEFAULT_BUFFER_SIZE_REC;
		queueWithBufferToPrintAL = new ArrayList<Buffer>();
	}
	
	public void start(){
		if(state == Constants.STOP_STATE){
			if(recorder != null){
				state = Constants.START_STATE;
				recorder.startRecording();
				MessagesLog.d(TAG, "Rozpoczęcie nagrywania");
				while(state == Constants.START_STATE){
					Buffer data = new Buffer();
					data.initializeBufferShort(buffer_size);
					data.setBufferSizeShort(buffer_size);
					int size = recorder.read(data.buffer_short,0,buffer_size);
					if(data.buffer_short != null){
						data.setTime(System.currentTimeMillis());
						data.setBufferSizeShort(size);
						notifyObserverBuffer(data);
					}
//					notifyObserverByte(data.buffer);
				}
				recorder.stop();
                recorder.release();
			}
			
			
		}
	}
	
	public void stop() {
        if (state == Constants.START_STATE) {
        	state = Constants.STOP_STATE;
        }
    }

	@Override
	public void register(RecorderAudioObserver rao) {
		this.recorder_audio_observer = rao;
	}

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

	@Override
	public void notifyObserverBuffer(Buffer buffer) {
		this.recorder_audio_observer.putBufferToQueue(buffer);
		
	}
	
}
