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
	
	public RecorderAudio record_audio; //object responsible for prepare process to record data
	private int state; // state of the process
	private Thread thread_recorder; // thread for recorder
	private Thread thread_decoder; // thread for decoder
	private Thread thread_voice_getter; // thread for voice getter
	private Queue queue_for_analyzer; // queue for analyse
	private Queue queue_for_graph; // queue for graph
	private Decoder decoder; // object responsible for decode of recorded data
	private VoiceGetter voice_getter; // object responsible for get recorder data
	private Listener mListener; // listener
	
	
	private final static String TAG = "VoiceRecognition";

	/**
	 * Constructor of VoiceRecognition class.
	 */
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
	
	/**
	 * Method to start record_audio thread, voice_getter thread and voice_decoder thread
	 */
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
	
	/**
	 * Method to stop record_audio thread, voice_getter thread and voice_decoder thread
	 */
	public void stop() throws InterruptedException{
		if (state == Constants.START_STATE) {
			state = Constants.STOP_STATE;
			MessagesLog.d(TAG, "sending data is over");
			
			MessagesLog.d(TAG, "No bez przesady23232zmu");
			if (thread_decoder != null) {
					decoder.stop();
					MessagesLog.d(TAG, "Thread dobrze sie przerwa³");

			}
			MessagesLog.d(TAG, "sending data is over 222");
			
			voice_getter.stop();
			if (thread_recorder != null) {

			}
			MessagesLog.d(TAG, "sending data is over 333");
			
			record_audio.stop();
			if (thread_recorder != null) {
			}
			
			MessagesLog.d(TAG, "sending data is over 444");
		}
		MessagesLog.d(TAG, "sending data is over 2222");
		queue_for_graph.clearBuffers();
		queue_for_analyzer.clearBuffers();
		
	}



	@Override
	public void register(VoiceRecObserver vro) {
//		voice_rec_observer = vro;
		
	}

	@Override
	public void notifyObserver(int[] data) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Method to put buffer with data to queue for analyse
	 */
	public void putBufferToQueue(Buffer buffer) {
		queue_for_analyzer.addToConsumer(buffer);
		if(Constants.DRAW_IN_TIME)
			queue_for_graph.addToConsumer(buffer);
	}

	/**
	 * Method to get buffer with data for graph queue
	 */
	public Buffer getBufferForGraphQueue() {
		return queue_for_graph.getFromConsumer();
	}
	
	/**
	 * Method to get buffer with data for decoder queue
	 */
	public Buffer getBufferForDecoderQueue(){
		return queue_for_analyzer.getFromConsumer();
	}
	
	/**
	 * Method to return decoder
	 * @return decoder - object when recorded data are analyse
	 */
	
	public Decoder getDecoder() {
		return decoder;
	}

	/**
	 * Method to set decoder
	 * @param decoder - object when recorded data are analyse
	 */
	public void setDecoder(Decoder decoder) {
		this.decoder = decoder;
	}


	/**
	 * Method which start when recognition is over
	 */
	public void onEndRecogntion() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Method which start when recognition is start
	 * @return decoder - object when recorded data are analyse
	 */
	public void onStartRecognition() {
		mListener.onStartRecogntion();
		
	}

	/**
	 * Method which process during recognition.
	 * @param str - string with data
	 */
	public void onRecognition(String str) {
		mListener.onRecognition(str);
		
	}
}
