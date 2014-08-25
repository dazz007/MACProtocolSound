package com.example.recorder;

import com.example.graphic.VoiceRecObserver;
import com.example.important.Buffer;
import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.important.Queue;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class VoiceRecognition implements VoiceRecSubject, RecorderAudioObserver{
	
	public RecorderAudio record_audio;
	private int state;
	private Thread thread_recorder;
	private Queue queue_for_analyzer;
	private Queue queue_for_graph;
	private VoiceRecObserver voice_rec_observer;
	private Decoder decoder;
	private Thread thread_decoder;
	
	private final static String TAG = "VoiceRecognition";
	public VoiceRecognition(){
		record_audio = new RecorderAudio(MediaRecorder.AudioSource.MIC,
										 Constants.SAMPLING,
										 AudioFormat.CHANNEL_IN_MONO,
										 AudioFormat.ENCODING_PCM_16BIT, 
										 2*Constants.DEFAULT_BUFFER_SIZE);
		queue_for_analyzer = new Queue();
		queue_for_graph = new Queue();
		record_audio.register(this);
		decoder = new Decoder();
		decoder.setSubject(this);
		state = Constants.STOP_STATE;
	}
	
	public void start(){
		if(state == Constants.STOP_STATE){
			state = Constants.START_STATE;
			thread_recorder = new Thread() {
				public void run(){
					record_audio.start();
				}
			};
			if(thread_recorder != null){
				thread_recorder.start();
			}
			
			thread_decoder = new Thread(){
				public void run(){
					decoder.start();
				}
			};
			if(thread_decoder != null){
				thread_decoder.start();
			}
		}
	}

//	@Override
//	public void sendDataToGraph(int[] data) {
//		voice_rec_observer.updateLineGraph(data);
//		MessagesLog.d(TAG, "Wesz³o w send DataToGraph");
//		
//	}

	@Override
	public void setStopStatus() {
		if (state == Constants.START_STATE) {
			state = Constants.STOP_STATE;
			MessagesLog.d(TAG, "sending data is over");
			record_audio.stop();
			if (thread_recorder != null) {
				try {
					thread_recorder.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					thread_recorder = null;
				}
			}
		}
		
	}

	@Override
	public void setSubject(RecorderAudio sub) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(VoiceRecObserver vro) {
		voice_rec_observer = vro;
		
	}

	@Override
	public void notifyObserver(int[] data) {
		// TODO Auto-generated method stub
		
	}
//
//	@Override
//	public void sendDataToGraphByte(byte[] data) {
//		
//		voice_rec_observer.updateLineGraphByte(data);
//		
//		MessagesLog.d(TAG, "Wesz³o w send DataToGraph");
//		
//	}

	@Override
	public void putBufferToQueue(Buffer buffer) {
		queue_for_analyzer.addToConsumer(buffer);
		queue_for_graph.addToConsumer(buffer);
		
	}

	@Override
	public Buffer getBufferForGraphQueue() {
		return queue_for_graph.getFromConsumer();
	}
	
	public Buffer getBufferForDecoderQueue(){
		return queue_for_analyzer.getFromConsumer();
	}
}
