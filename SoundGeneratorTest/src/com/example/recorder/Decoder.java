package com.example.recorder;

import java.util.ArrayList;

import org.jtransforms.fft.DoubleFFT_1D;

import com.example.interfaces.VoiceRecSubject;
import com.example.important.Buffer;
import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.important.Queue;
import com.example.interfaces.DecoderSubject;
import com.example.interfaces.VoiceRecObserver;
import com.example.minim.FFT;

public class Decoder implements VoiceRecObserver, DecoderSubject {
	private final static String TAG = "DECODER";
	private VoiceRecSubject vrs;
	private int state;
	private Queue queue_for_graph;
	// DoubleFFT_1D fftDo;
	private FFT fft;
	private int counter;
	private float[] fftRealArray;
	private ArrayList<FrequencyTime> freq_time;
	private ArrayList<Buffer> prepared_buffers;
	private FrequencyTime current_freq;

	public static interface Listener {
		public void onStartRecogntion();

		public void onRecognition(String str);

		public void onEndRecogntion();
	}

	@Override
	public void setSubject(VoiceRecSubject sub) {
		this.vrs = sub;
		// this.fftDo = new DoubleFFT_1D(Constants.DEFAULT_BUFFER_SIZE * 2);
		counter = 0;

		queue_for_graph = new Queue();
	}

	public void start() {
		Buffer buf_from_queue;
		current_freq = null;
		if (state == Constants.STOP_STATE) {
			state = Constants.START_STATE;
			prepared_buffers = new ArrayList<Buffer>();
			this.vrs.onStartRecognition();

			fft = new FFT(Constants.DEFAULT_BUFFER_SIZE, Constants.SAMPLING);
			while (state == Constants.START_STATE) {
				buf_from_queue = this.vrs.getBufferForDecoderQueue();
				if (buf_from_queue != null) {
					//MessagesLog.d(TAG, "Wzielo super1111" );
					analyse(buf_from_queue);
				}else{
					// MessagesLog.e(TAG, "Wzielo puste" );
				}
			}
		}
	}
	
	public void stop(){
		if(state == Constants.START_STATE){
			state = Constants.STOP_STATE;
		}
	}

	private void analyse(Buffer buffer) {
//		MessagesLog.d(TAG, ""+ buffer.getSize());
		int buffer_size = buffer.getSize();
//		int buffer_size_fft = buffer_size;
		short[] buffer_short = buffer.getBufferShort();
//		long time = buffer.getTime();
		
		// verify that is power of two
//		if ((buffer_size_fft & (buffer_size_fft - 1)) != 0)
//			buffer_size_fft = 2 << (int) (Math.log(buffer_size_fft) / Math
//					.log(2));

		fftRealArray = new float[Constants.DEFAULT_BUFFER_SIZE];
		for (int i = 0; i < buffer_size; i++) {
			fftRealArray[i] = (float) buffer_short[i] / Short.MAX_VALUE;
		}

		// apply windowing
		for (int i = 0; i < buffer_size / 2; ++i) {
			// Calculate & apply window symmetrically around center point
			// Hanning (raised cosine) window
			float winval = (float) (0.5 + 0.5 * Math.cos(Math.PI * (float) i
					/ (float) (buffer_size / 2)));
			if (i > buffer_size / 2)
				winval = 0;
			fftRealArray[buffer_size / 2 + i] *= winval;
			fftRealArray[buffer_size / 2 - i] *= winval;
		}
		// zero out first point (not touched by odd-length window)
		fftRealArray[0] = 0;
		fft.forward(fftRealArray);

		int caught_freq = findPitch(fft);
		checkPitch(caught_freq);

		if (Constants.DRAW_FFT) {
			counter++;
			if (counter % 2 == 0) {
				if (counter == 1000) {
					counter = 0;
				}
				queue_for_graph.addFFTToConsumer(fft);
			}
		}
	}

	private int findPitch(FFT fft) {
//		MessagesLog.d(TAG, "fft.specsize() "+ fft.specSize());
		float max_band = 0;
		int max_peak = 0;
		//int start = 40*fft.specSize() / 100;
		for (int i = 1800; i < fft.specSize(); i++) {
			if (fft.getBand(i) > 7)
					if(fft.getBand(i) > max_band) {
					max_peak = i;
					max_band = fft.getBand(i);
			}
		}

		// if(max_peak != 0)
		// MessagesLog.d(TAG, "Znalaz³o czestotliwosc: "+max_peak *
		// (Constants.SAMPLING / 2) / fft.specSize());
		return max_peak * (Constants.SAMPLING / 2) / fft.specSize();

	}

	private void checkPitch(int freq) {
		String sign = "";
		if (freq != 0) {
			if (current_freq != null) {
				if (current_freq.getFrequency() - 50 < freq
						&& current_freq.getFrequency() + 50 > freq) {
					current_freq.increaseCount();
					sign = current_freq.foundAndReturnChar();
					if (!sign.equals(Constants.NOEND_STR)) {
						vrs.onRecognition(String.valueOf(sign));
					}
				} else {
					sign = current_freq.returnChar();
					if (!sign.equals(Constants.NOEND_STR)) {
						vrs.onRecognition(String.valueOf(sign));
					}
					current_freq = new FrequencyTime();
					current_freq.setFrequency(freq);
				}
			} else {
				current_freq = new FrequencyTime();
				current_freq.setFrequency(freq);
			}
		} else {
			if (current_freq != null) {
				sign = current_freq.returnChar();
				if (!sign.equals(Constants.NOEND_STR)) {
					//MessagesLog.d(TAG, "Znalazlo znak 2: " + sign);
					vrs.onRecognition(String.valueOf(sign));
				}
				current_freq = null;
			}
		}
	}


	@Override
	public Buffer getBufferFFTForGraphQueue() {
		return queue_for_graph.getFromConsumer();
	}

	@Override
	public FFT getBufferFFTForGraphQueueFFT() {
		// TODO Auto-generated method stub
		return queue_for_graph.getFromFFTConsumer();
	}
}
