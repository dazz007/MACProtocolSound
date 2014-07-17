package com.example.important;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.example.graphic.SoundGenObserver;
import com.example.important.SoundGenerator.Observer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioPlayer implements AudioPlayerSubject{
	private final static String TAG = "AudioPlayer";
	private AudioTrack audiotrack;
	private int state;
	List<Integer> indexesOfSigns;
	private ArrayList<Buffer> queueWithDataAL;
	AudioPlayerObserver observer;
	byte[] audioData;

	
	public AudioPlayer(int sampleRate) {

		int minSize = AudioTrack.getMinBufferSize(sampleRate,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

		audiotrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
				minSize, AudioTrack.MODE_STREAM);
		queueWithDataAL = new ArrayList<Buffer>();
		// queueWithData = new LinkedBlockingQueue<Buffer>(4);
		indexesOfSigns = new ArrayList<Integer>();
		state = Constants.STOP_STATE;
	}

	public void play(byte[] audioData, int sizeOfBuffer) {
		audiotrack.write(audioData, 0, sizeOfBuffer);
	}


	public void start() {
		MessagesLog.d(TAG, "Weszlo w start");
		if (state == Constants.STOP_STATE) {
			MessagesLog.d(TAG, "Weszlo w ifa");
			state = Constants.START_STATE;
			int startLength = 0;
			while (state == Constants.START_STATE) {
				Buffer buffer = getBufferFromQueueAL();
				MessagesLog.d(TAG, "Pobiera");
				if (buffer != null) {
					byte[] data = buffer.getBuffer();
					
					int sizeOfData = buffer.getBufferSize();
					int[] dataValues = buffer.getBufferValues();
					notifyObserver(dataValues);
					if (data != null) {
						int len = audiotrack.write(data, 0, sizeOfData);
						if (startLength == 0) {
							
							audiotrack.play();
//							state = Constants.STOP_STATE;
						}
						startLength += len;
					} else {
						MessagesLog.d(TAG, "End of data. Stop transmission");
						break;
					}

				} else {
					MessagesLog.e(TAG, "get null data");
					break;
				}
			}

			if (audiotrack != null) {
				audiotrack.pause();
				audiotrack.flush();
				audiotrack.stop();
			}
			state = Constants.STOP_STATE;
			MessagesLog.d(TAG, "end of transferring data");
			observer.setStopStatus();

		}
	}
	
	public void setBufferToPlay(ArrayList<Buffer> queueBuffer){
		queueWithDataAL = queueBuffer;
	}
	
	private Buffer getBufferFromQueueAL() {

		if (queueWithDataAL.size() > 0) {
			Buffer buffer = queueWithDataAL.get(0);
			queueWithDataAL.remove(0);
			return buffer;
		}
		return null;

	}


	public void stop() {
		if (state == Constants.START_STATE) {
			state = Constants.STOP_STATE;
			queueWithDataAL.clear();
//			queueWithData.clear();
		}
	}

	@Override
	public void notifyObserver(int[] data) {
		observer.sendDataToGraph(data);
	}

	@Override
	public void register(AudioPlayerObserver apo) {
		this.observer = apo;
		
	}
}
