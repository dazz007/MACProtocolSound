package com.example.important;

import java.util.ArrayList;
import java.util.List;

import com.example.interfaces.AudioPlayerObserver;
import com.example.interfaces.SoundGenObserver;
import com.example.interfaces.SoundGenSubject;


public class SoundGenerator implements SoundGenSubject, AudioPlayerObserver{
	private final static String TAG = "SoundGenerator";
	private int state;
	private String textToPlay;
	private AudioPlayer player;
	private Thread threadPlayer;
	private SoundGenObserver sgo;
	private Listener listener;
	
	
	public static interface Listener {
		public void EndOfSending();
	}
	
	public SoundGenerator(int sampleRate, Listener lstnr) {
		listener = lstnr;
		player = new AudioPlayer(sampleRate);
		player.register(this);
		state = Constants.STOP_STATE;
	}


	public void setTextToEncode(String text) {
		textToPlay = text;
	}

	public List<Integer> encodeText() {
		List<Integer> indexesOfSigns = new ArrayList<Integer>();
		// add start of data
//		for (int i = 0; i < Constants.START_OF_DATA.length(); i++) {
//			int index = Constants.AVAILABLE_SIGNS
//					.indexOf(Constants.START_OF_DATA.charAt(i));
//			indexesOfSigns.add(index);
//		}
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
		for (int i = 0; i < Constants.END_OF_DATA.length(); i++) {
			for(int j = 0; j < Constants.STANDARD_ALPHABET.length; j++){
				if(Constants.END_OF_DATA.charAt(i) == Constants.STANDARD_ALPHABET[j] ){
					indexesOfSigns.add(j);
					break;
				}
			}
		}
//		for (int i = 0; i < Constants.END_OF_DATA.length(); i++) {
//			int index = Constants.AVAILABLE_SIGNS.indexOf(Constants.END_OF_DATA
//					.charAt(i));
//			indexesOfSigns.add(index);
//		}

		return indexesOfSigns;
	}
	
	

//	public ArrayList<Buffer> encodesDataToBuffers(List<Integer> inofsign) {
//	
//		ArrayList<Buffer> queue_with_data_AL = new ArrayList<Buffer>();
//		for (int index : inofsign) {
//			int n = Constants.BITS_16/2;
//			
//			int totalCount = Constants.DEFAULT_NUM_SAMPLES;
//			double per = (Constants.FREQUENCIES[index] / (double) Constants.SAMPLING) * 2 * Math.PI;
//			double d = 0;
//			
//			int mFilledSize = 0;
//			Buffer buffer = new Buffer();
//			
//			short[] buffer_data = new short[Constants.DEFAULT_BUFFER_SIZE];
//			
//			for(int i = 0; i < totalCount ; ++i){
//				int out = (int) (Math.sin(d) * n) + 128;
//				
//				if(mFilledSize >= Constants.DEFAULT_BUFFER_SIZE - 1){
//					buffer.setBufferShort(buffer_data);
//					buffer.setBufferSizeShort(mFilledSize);
//					mFilledSize = 0;
//					queue_with_data_AL.add(buffer);
//					buffer = new Buffer();
//					buffer_data = new short[Constants.DEFAULT_BUFFER_SIZE];
//				}
//				
//				buffer_data[mFilledSize++] = (short) ( out );
//				
//				
//				d+=per;
//				//index_in_buffer++;
//			}
//			
//			
//			buffer.setBufferShort(buffer_data);
//			buffer.setBufferSize(mFilledSize);
//			
//			
//			mFilledSize = 0;
//			
//			queue_with_data_AL.add(buffer);
//		}
//		return queue_with_data_AL;
//
//	}
	
	public ArrayList<Buffer> encodesDataToBuffers(List<Integer> inofsign) {
		
		ArrayList<Buffer> queue_with_data_AL = new ArrayList<Buffer>();
		for (int index : inofsign) {
			
			int totalCount = Constants.DEFAULT_NUM_SAMPLES;
			double per = (double) (( Constants.FREQUENCIES[index] * 2 * Math.PI )  / Constants.SAMPLING);
			double d = 0;
			
			int mFilledSize = 0;
			Buffer buffer = new Buffer();
			int ramp = totalCount / 40;
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
	

	@Override
	public void setStopStatus() {
		// TODO Auto-generated method stub
		if (state == Constants.START_STATE) {
			state = Constants.STOP_STATE;
			MessagesLog.d(TAG, "sending data is over");
			player.stop();
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
		
		listener.EndOfSending();
	}

	@Override
	public void register(SoundGenObserver sgo) {
		this.sgo = sgo;
		
	}

	@Override
	public void notifyObserver(int [] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendDataToGraph(int[] data) {
		this.sgo.updateLineGraph(data);
		MessagesLog.d(TAG, "Wesz³o w send DataToGraph");
	}

	@Override
	public void setSubject(AudioPlayer sub) {
		// TODO Auto-generated method stub
		
	}

}
