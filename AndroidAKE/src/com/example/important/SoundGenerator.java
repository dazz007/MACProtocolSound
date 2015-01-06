package com.example.important;

import java.util.ArrayList;
import java.util.List;

import com.example.interfaces.AudioPlayerObserver;
import com.example.interfaces.SoundGenObserver;
import com.example.interfaces.SoundGenSubject;


public class SoundGenerator implements SoundGenSubject, AudioPlayerObserver{
	private final static String TAG = "SoundGenerator";
	private int state; //state of process
	private String textToPlay; //text to play
	private AudioPlayer player; // Object responsible for Play sound
	private Thread threadPlayer; //thread for object player
	private SoundGenObserver sgo; //observer
	private Listener listener; // listener
	
	/**
	 * Interface for listener of this class
	 * @return sign - sign adequate to found frequency
	 */
	public static interface Listener {
		public void EndOfSending();
	}
	
	/**
	 * Constructor SoundGenerator class
	 * @param sampleRate - sample rate
	 */
	public SoundGenerator(int sampleRate) {
		
		player = new AudioPlayer(sampleRate);
		player.register(this);
		state = Constants.STOP_STATE;
	}
	/**
	 * Set listener
	 * @param lstnr - listener
	 */
	public void setListener(Listener lstnr){
		listener = lstnr;
	}
	
	/**
	 * Set text to encode
	 * @param text - text to encode
	 */
	public void setTextToEncode(String text) {
		textToPlay = text;
	}
	
	/**
	 * Method to encode text to the adequate indexes
	 * 
	 * @return  indexes of sign
	 */
	public List<Integer> encodeText() {
		List<Integer> indexesOfSigns = new ArrayList<Integer>();
		// add start of data
		for (int i = 0; i < Constants.START_OF_DATA.length(); i++) {
			for(int j = 0; j < Constants.STANDARD_ALPHABET.length; j++){
				if(Constants.START_OF_DATA.charAt(i) == Constants.STANDARD_ALPHABET[j] ){
					indexesOfSigns.add(j);
					break;
				}
			}
		}
		for (int i = 0; i < textToPlay.length(); i++) {
			for(int j = 0; j < Constants.STANDARD_ALPHABET.length; j++){
				if(textToPlay.charAt(i) == Constants.STANDARD_ALPHABET[j] ){
					indexesOfSigns.add(j);
					break;
				}
			}
		}
		
		// add end of data
		for (int i = 0; i < Constants.END_OF_DATA.length(); i++) {
			for(int j = 0; j < Constants.STANDARD_ALPHABET.length; j++){
				if(Constants.END_OF_DATA.charAt(i) == Constants.STANDARD_ALPHABET[j] ){
					indexesOfSigns.add(j);
					break;
				}
			}
		}

		return indexesOfSigns;
	}
	
	/**
	 * Method to encode data to sinus waves. This data is transmitted.
	 * @param inofsign 
	 * @return queue with sinus waves
	 */
	public ArrayList<Buffer> encodesDataToBuffers(List<Integer> inofsign) {
		
		ArrayList<Buffer> queue_with_data_AL = new ArrayList<Buffer>();
		for (int index : inofsign) {
			
			int totalCount = Constants.DEFAULT_NUM_SAMPLES;
			double per = (double) (( Constants.FREQUENCIES[index] * 2 * Math.PI )  / Constants.SAMPLING);
			double d = 0;
			
			int mFilledSize = 0;
			Buffer buffer = new Buffer();
			int ramp = totalCount / 30;
			short[] buffer_data = new short[Constants.DEFAULT_BUFFER_SIZE];
			
			for(int i = 0; i < totalCount ; ++i){
				
				if(mFilledSize >= Constants.DEFAULT_BUFFER_SIZE - 1){
					buffer.setBufferShort(buffer_data);
					buffer.setSize(mFilledSize);
					mFilledSize = 0;
					queue_with_data_AL.add(buffer);
					buffer = new Buffer();
					buffer_data = new short[Constants.DEFAULT_BUFFER_SIZE];
				}
				double out = (double) Math.sin(d);
				final short val;
				if(i < ramp){
					val = (short) ((out * Short.MAX_VALUE * i / ramp));
				}else if(i < totalCount - ramp){
					val = (short) ((out * Short.MAX_VALUE));
				}else{
					val = (short) ((out * Short.MAX_VALUE * (totalCount-i)/ramp));
				}
				
//				buffer_data[mFilledSize++] = (short) ( out * Short.MAX_VALUE );
				buffer_data[mFilledSize++] = (short) ( val );
				
				d+=per;
			}
			
			
			buffer.setBufferShort(buffer_data);
			buffer.setSize(mFilledSize);
			queue_with_data_AL.add(buffer);
			
			mFilledSize = 0;
			
			
		}
		return queue_with_data_AL;

	}
	
	/**
	 * Method to start sending data.
	 */
	public void start() {
		if (state == Constants.STOP_STATE) {
			state = Constants.START_STATE;
			
			MessagesLog.d(TAG, "Weszlo w start");
			threadPlayer = new Thread() {
				@Override
				public void run() {
					List<Integer> indexesOfSigns = encodeText();
					player.setBufferToPlay(encodesDataToBuffers(indexesOfSigns));
					player.start();
				}
			};
			if (threadPlayer != null) {
				threadPlayer.start();
			}
		}
	}
	

	/**
	 * Method to stop sending data.
	 */
	public void setStopStatus() {
		MessagesLog.d(TAG, "Weszlo sobie tutaj po zakonczeniu? 22222");
		MessagesLog.d(TAG, "Weszlo sobie tutaj po zakonczeniu? 323232323235454");
		
		// TODO Auto-generated method stub
		if (state == Constants.START_STATE) {
			state = Constants.STOP_STATE;
			MessagesLog.d(TAG, "sending data is over");
			player.stop();
			listener.EndOfSending();
			if (threadPlayer != null) {
				try {
					threadPlayer.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					threadPlayer = null;
				}
			}
		}
		
	}

	@Override
	public void register(SoundGenObserver sgo) {
		this.sgo = sgo;
		
	}

	@Override
	public void notifyObserver(int [] data) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Send data to graph
	 * @param data - sound data to print
	 */
	public void sendDataToGraph(int[] data) {
		this.sgo.updateLineGraph(data);
		//MessagesLog.d(TAG, "Wesz�o w send DataToGraph");
	}

	@Override
	public void setSubject(AudioPlayer sub) {
		// TODO Auto-generated method stub
		
	}

}
