package com.example.important;

import java.util.ArrayList;
import java.util.List;

import com.example.graphic.SoundGenObserver;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SoundGenerator implements SoundGenSubject, AudioPlayerObserver{
	private final static String TAG = "SoundGenerator";
	private int state;
	private String textToPlay;
	private AudioPlayer player;
	private Thread threadPlayer;
	private Thread threadEncoding;
	private SoundGenObserver sgo;
	
	
	public SoundGenerator(int sampleRate) {

		player = new AudioPlayer(sampleRate);
		player.register(this);
		state = Constants.STOP_STATE;
	}

	public static interface Observer {
		public void update();
	}

	public void setTextToEncode(String text) {
		textToPlay = text;
	}

	public List<Integer> encodeText() {
		List<Integer> indexesOfSigns = new ArrayList<Integer>();
		// add start of data
		for (int i = 0; i < Constants.START_OF_DATA.length(); i++) {
			int index = Constants.AVAILABLE_SIGNS
					.indexOf(Constants.START_OF_DATA.charAt(i));
			indexesOfSigns.add(index);
		}

		for (int i = 0; i < textToPlay.length(); i++) {
			int index = Constants.AVAILABLE_SIGNS.indexOf(textToPlay.charAt(i));
			indexesOfSigns.add(index);
		}

		for (int i = 0; i < Constants.END_OF_DATA.length(); i++) {
			int index = Constants.AVAILABLE_SIGNS.indexOf(Constants.END_OF_DATA
					.charAt(i));
			indexesOfSigns.add(index);
		}

		return indexesOfSigns;
	}
	
	public ArrayList<Buffer> encodesDataToBuffers(List<Integer> inofsign) {
		
		ArrayList<Buffer> queueWithDataAL = new ArrayList<Buffer>();
		
		for (int index : inofsign) {
			int n = Constants.BITS_16 / 2;
//			int totalCount = (Constants.DEFAULT_GEN_DURATION * Constants.SAMPLING) / 1000;
			int numSamples = Constants.DEFAULT_NUM_SAMPLES;
			double per = (Constants.FREQUENCIES[index] / (double) Constants.SAMPLING)
					* 2 * Math.PI;
			double d = 0;
			Buffer buffer = new Buffer(Constants.DEFAULT_BUFFER_SIZE);
			byte[] bufferData = new byte[Constants.DEFAULT_BUFFER_SIZE];
			int[] bufferValues = new int[Constants.SAMPLING]; 
			int indexInBuffer = 0;
			int ramp = numSamples / 20;
			for (int i = 0; i < numSamples; ++i) {
//				int out = (int) (Math.sin(d) * n) + 128;
				
				
				double out = (double) (Math.sin(Constants.FREQUENCIES[index] * 2 * Math.PI  * i / Constants.SAMPLING) );
				if (indexInBuffer >= Constants.DEFAULT_BUFFER_SIZE - 1) {
					buffer.setBuffer(bufferData);
					buffer.setBufferSize(indexInBuffer);
					buffer.setBufferValues(bufferValues);
					buffer.setBufferValuesSize(i);
					queueWithDataAL.add(buffer);
					bufferData = new byte[Constants.DEFAULT_BUFFER_SIZE];
					indexInBuffer = 0;
				}
				
				
				final short val;
				if(i < ramp){
					val = (short) ((out * n * i / ramp));					
				}else if(i < numSamples - ramp){
					val = (short) ((out * n));
				}else{
					val = (short) ((out * n * (numSamples-i)/ramp));
				}
				bufferValues[i] = val;
				bufferData[indexInBuffer++] = (byte) (val & 0x00ff);
				bufferData[indexInBuffer++] = (byte) ((val & 0xff00) >>> 8);

				d += per;

			}

			buffer.setBuffer(bufferData);
			buffer.setBufferSize(indexInBuffer);
			buffer.setBufferValues(bufferValues);
			buffer.setBufferValuesSize(indexInBuffer/2);
			queueWithDataAL.add(buffer);

			indexInBuffer = 0;
		}
		return queueWithDataAL;

	}
	

	
	public void start() {
		if (state == Constants.STOP_STATE) {
			state = Constants.START_STATE;
			List<Integer> indexesOfSigns = encodeText();
			player.setBufferToPlay(encodesDataToBuffers(indexesOfSigns));
			MessagesLog.d(TAG, "Weszlo w start");
			threadPlayer = new Thread() {
				@Override
				public void run() {
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
