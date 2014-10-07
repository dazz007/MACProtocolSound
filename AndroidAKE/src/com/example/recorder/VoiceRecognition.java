package com.example.recorder;

import com.example.important.Buffer;
import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.important.Queue;
import com.example.interfaces.VoiceRecObserver;
import com.example.interfaces.VoiceRecSubject;

import android.media.AudioFormat;
import android.media.MediaRecorder;

public class VoiceRecognition implements VoiceRecSubject, VoiceGetter.Callback{
	
	public RecorderAudio record_audio;
	private int state;
	private Thread thread_recorder;
	private Thread thread_decoder;
	private Thread thread_voice_getter;
	private Queue queue_for_analyzer;
	private Queue queue_for_graph;
	private Decoder decoder;
	private VoiceGetter voice_getter;
	private Listener mListener;
	

	
	
	private final static String TAG = "VoiceRecognition";

	
	public VoiceRecognition(){
		record_audio = new RecorderAudio(MediaRecorder.AudioSource.MIC,
										 Constants.SAMPLING,
										 AudioFormat.CHANNEL_IN_MONO,
										 AudioFormat.ENCODING_PCM_16BIT, 
										 //Constants.DEFAULT_BUFFER_SIZE
										 Constants.DEFAULT_BUFFER_SIZE_REC
										 );
		queue_for_analyzer = new Queue();
		queue_for_graph = new Queue();
		//record_audio.register(this);
		decoder = new Decoder();
		decoder.setSubject(this);
		voice_getter = new VoiceGetter(record_audio, this);
		
		state = Constants.STOP_STATE;
	}
	
	public static interface Listener{
		public void onStartRecogntion();
		public void onRecognition(String str);
		public void onEndRecognition();
	}
	
	public void setListener(Listener listener){
		mListener = listener;
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
			
			thread_voice_getter = new Thread() {
				public void run(){
					voice_getter.start();
				}
			};
			if(thread_voice_getter != null){
				thread_voice_getter.start();
			}
			
			//decoder.setRecorderAudio(record_audio);
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
	
	
	public void stop(){
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
			voice_getter.stop();
			if (thread_recorder != null) {
				try {
					thread_voice_getter.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					thread_voice_getter = null;
				}
			}
			decoder.stop();
			if (thread_decoder != null) {
				try {
					thread_decoder.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					thread_decoder = null;
				}
			}
		}
		
		queue_for_graph.clearBuffers();
		queue_for_analyzer.clearBuffers();
		
	}
//	@Override
//	public void sendDataToGraph(int[] data) {
//		voice_rec_observer.updateLineGraph(data);
//		MessagesLog.d(TAG, "Wesz³o w send DataToGraph");
//		
//	}

//	@Override
//	public void setStopStatus() {
//		if (state == Constants.START_STATE) {
//			state = Constants.STOP_STATE;
//			MessagesLog.d(TAG, "sending data is over");
//			record_audio.stop();
//			if (thread_recorder != null) {
//				try {
//					thread_recorder.join();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} finally {
//					thread_recorder = null;
//				}
//			}
//		}
//		
//	}


	@Override
	public void register(VoiceRecObserver vro) {
//		voice_rec_observer = vro;
		
	}

	@Override
	public void notifyObserver(int[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void putBufferToQueue(Buffer buffer) {
		queue_for_analyzer.addToConsumer(buffer);
		if(Constants.DRAW_IN_TIME)
			queue_for_graph.addToConsumer(buffer);
	}

	@Override
	public Buffer getBufferForGraphQueue() {
		return queue_for_graph.getFromConsumer();
	}
	
	@Override
	public Buffer getBufferForDecoderQueue(){
		return queue_for_analyzer.getFromConsumer();
	}
	
	public Decoder getDecoder() {
		return decoder;
	}

	public void setDecoder(Decoder decoder) {
		this.decoder = decoder;
	}


	@Override
	public void onEndRecogntion() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartRecognition() {
		mListener.onStartRecogntion();
		
	}

	@Override
	public void onRecognition(String str) {
		mListener.onRecognition(str);
		
	}
}
