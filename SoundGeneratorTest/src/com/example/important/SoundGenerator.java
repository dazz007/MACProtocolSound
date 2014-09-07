package com.example.important;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.example.interfaces.AudioPlayerObserver;
import com.example.interfaces.SoundGenObserver;
import com.example.interfaces.SoundGenSubject;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SoundGenerator implements SoundGenSubject, AudioPlayerObserver{
	private final static String TAG = "SoundGenerator";
	private int state;
	private String textToPlay;
	private AudioPlayer player;
	private Thread threadPlayer;
	private SoundGenObserver sgo;
	
	
	public SoundGenerator(int sampleRate) {
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

		for (int i = 0; i < textToPlay.length(); i++) {
			int index = Constants.AVAILABLE_SIGNS.indexOf(textToPlay.charAt(i));
			indexesOfSigns.add(index);
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
//		int numSamples = Constants.DEFAULT_NUM_SAMPLES;     //44100
//		float x = 0;
//		
//		int ramp = numSamples / 20;
//		
//		for (int index : inofsign) {
//			int n = Constants.BITS_16/2;
//			int index_in_buffer = 0;
//			
//			
//			//int n = Constants.BITS_16 - 1;// / 2;
////			int totalCount = (Constants.DEFAULT_GEN_DURATION * Constants.SAMPLING) / 1000;
//			
////			double per = (Constants.FREQUENCIES[index] / (double) Constants.SAMPLING)
////					* 2 * Math.PI;
//			//double d = 0;
//			Buffer buffer = new Buffer();
//			//buffer.initializeBufferByte(Constants.DEFAULT_BUFFER_SIZE);
//			byte[] buffer_data = new byte[Constants.DEFAULT_BUFFER_SIZE];
//			short[] buffer_values = new short[Constants.DEFAULT_BUFFER_SIZE];
//			float[] samples = new float[Constants.DEFAULT_BUFFER_SIZE];
//			float d = (float) (( Constants.FREQUENCIES[index] * 2 * Math.PI )  / Constants.SAMPLING);
//			
//			
//			for(int i = 0; i < numSamples; i++){
//				if(index_in_buffer >= Constants.DEFAULT_BUFFER_SIZE){
//					buffer.setBufferShort(buffer_values);
//					buffer.setBufferSizeShort(index_in_buffer);
//					queue_with_data_AL.add(buffer);
//					buffer_values = new short[Constants.DEFAULT_BUFFER_SIZE];
//					samples = new float[Constants.DEFAULT_BUFFER_SIZE];
//					index_in_buffer = 0;
//				}
//				
//				samples[index_in_buffer] = (float) Math.sin(x);
////				
////				if(i < ramp){
////					buffer_values[index_in_buffer] = (short) (samples[index_in_buffer] * Short.MAX_VALUE * i / ramp);
////					//val = (short) ((out * n * i / ramp));					
////				}else if(i < numSamples - ramp){
////					buffer_values[index_in_buffer] = (short) (samples[index_in_buffer] * Short.MAX_VALUE);
////					//val = (short) ((out * n));
////				}else{
////					buffer_values[index_in_buffer] = (short) (samples[index_in_buffer] * Short.MAX_VALUE * (numSamples-i)/ramp );
////					//val = (short) ((out * n * (numSamples-i)/ramp));
////				}
//				buffer_values[index_in_buffer] = (short) (samples[index_in_buffer] * Short.MAX_VALUE);
//				
//				x += d;
//				index_in_buffer++;
//			}
//			
////			for (int i = 0; i < numSamples; ++i) {
//////				int out = (int) (Math.sin(d) * n) + 128;
////				//short dupa = Short.MAX_VALUE;
////				
////				double out = (double) (Math.sin(x));
////				if (indexInBuffer >= Constants.DEFAULT_BUFFER_SIZE - 1) {
////					//buffer.setBuffer(buffer_data);
////					//buffer.setBufferSize(indexInBuffer);
////					buffer.setBufferSizeShort(indexInBuffer);
////					buffer.setBufferShort(buffer_values);
////					queue_with_data_AL.add(buffer);
////					buffer_data = new byte[Constants.DEFAULT_BUFFER_SIZE];
////					buffer_values = new short[Constants.DEFAULT_BUFFER_SIZE];
////					indexInBuffer = 0;
////				}
////				
////				x += d;
////				short val = (short) (out * Short.MAX_VALUE);
//////				if(i < ramp){
//////					val = (short) ((out * n * i / ramp));					
//////				}else if(i < numSamples - ramp){
//////					val = (short) ((out * n));
//////				}else{
//////					val = (short) ((out * n * (numSamples-i)/ramp));
//////				}
////				//val = 
////				
////				buffer_values[indexInBuffer++] = val;
////				//buffer_values[indexInBuffer++] = val;
////				//buffer_data[indexInBuffer++] = (byte) (val & 0x00ff);
////				//buffer_data[indexInBuffer++] = (byte) ((val & 0xff00) >>> 8);
////
////				//d += per;
////
////			}
//
//			//buffer.setBuffer(buffer_data);
//			//buffer.setBufferSize(indexInBuffer);
//			buffer.setBufferShort(buffer_values);
//			buffer.setBufferSizeShort(index_in_buffer);
//			queue_with_data_AL.add(buffer);
//			
//			index_in_buffer = 0;
//			
//		}
//		return queue_with_data_AL;
//
//	}
	

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
			int n = Constants.BITS_16/2;
			
			int totalCount = Constants.DEFAULT_NUM_SAMPLES;
			double per = (double) (( Constants.FREQUENCIES[index] * 2 * Math.PI )  / Constants.SAMPLING);
			double d = 0;
			
			int mFilledSize = 0;
			Buffer buffer = new Buffer();
			
			short[] buffer_data = new short[Constants.DEFAULT_BUFFER_SIZE];
			
			for(int i = 0; i < totalCount ; ++i){
				
				if(mFilledSize >= Constants.DEFAULT_BUFFER_SIZE - 1){
					buffer.setBufferShort(buffer_data);
					buffer.setBufferSizeShort(mFilledSize);
					mFilledSize = 0;
					queue_with_data_AL.add(buffer);
					buffer = new Buffer();
					buffer_data = new short[Constants.DEFAULT_BUFFER_SIZE];
				}
				double out = (double) Math.sin(d);
				buffer_data[mFilledSize++] = (short) ( out * Short.MAX_VALUE );
				
				
				d+=per;
			}
			
			
			buffer.setBufferShort(buffer_data);
			buffer.setBufferSizeShort(mFilledSize);
			queue_with_data_AL.add(buffer);
			
			mFilledSize = 0;
			
			
		}
		return queue_with_data_AL;

	}
	
//public ArrayList<Buffer> encodesDataToBuffers(List<Integer> inofsign) {
//		
//		ArrayList<Buffer> queue_with_data_AL = new ArrayList<Buffer>();
//		int numSamples = Constants.DEFAULT_NUM_SAMPLES;     //44100
//		float x = 0;
//		
//		
//		for (int index : inofsign) {
//			int n = Constants.BITS_16/2;
//			int index_in_buffer = 0;
//			
//			int totalCount = Constants.DEFAULT_NUM_SAMPLES;
//			double per = (Constants.FREQUENCIES[index] / (double) Constants.SAMPLING) * 2 * Math.PI;
//			double d = 0;
//			
//			int mFilledSize = 0;
//			Buffer buffer = new Buffer();
//			
//			byte[] buffer_data = new byte[Constants.DEFAULT_BUFFER_SIZE];
//			
//			for(int i = 0; i < totalCount ; ++i){
//				int out = (int) (Math.sin(d) * n) + 128;
//				
//				if(mFilledSize >= Constants.DEFAULT_BUFFER_SIZE - 1){
//					buffer.setBuffer(buffer_data);
//					buffer.setBufferSize(mFilledSize);
//					mFilledSize = 0;
//					queue_with_data_AL.add(buffer);
//					buffer = new Buffer();
//					buffer_data = new byte[Constants.DEFAULT_BUFFER_SIZE];
//				}
//				
//				buffer_data[mFilledSize++] = (byte) (out & 0xff);
//				buffer_data[mFilledSize++] = (byte) ((out >> 8) & 0xff);
//				
//				d+=per;
//			}
//			
//			
//			buffer.setBuffer(buffer_data);
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
	

	
//	public void start() {
//		if (state == Constants.STOP_STATE) {
//			state = Constants.START_STATE;
//			List<Integer> indexesOfSigns = encodeText();
//			player.setBufferToPlay(encodesDataToBuffers(indexesOfSigns));
//			MessagesLog.d(TAG, "Weszlo w start");
//			threadPlayer = new Thread() {
//				@Override
//				public void run() {
//					player.start();
//				}
//			};
//			if (threadPlayer != null) {
//				threadPlayer.start();
//			}
//		}
//	}
	
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
