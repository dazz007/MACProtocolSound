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
	ArrayList<FrequencyTime> freq_time;

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
		freq_time = new ArrayList<FrequencyTime>();
	}


	public void start() {
		if (state == Constants.STOP_STATE) {
			state = Constants.START_STATE;
			this.vrs.onStartRecognition();
			while (state == Constants.START_STATE) {
				Buffer buffer = vrs.getBufferForDecoderQueue();
				if (buffer != null) {
					analyse(buffer);
				}
			}
		}
	}

	private void analyse(Buffer buffer) {
		int buffer_size = buffer.getBufferSizeShort();
		int buffer_size_fft = buffer_size;
		short[] buffer_short = buffer.getBufferShort();
		long time = buffer.getTime();
		counter++;
		// verify that is power of two
		if ((buffer_size_fft & (buffer_size_fft - 1)) != 0)
			buffer_size_fft = 2 << (int) (Math.log(buffer_size_fft) / Math
					.log(2));

		fftRealArray = new float[Constants.DEFAULT_BUFFER_SIZE];
		fft = new FFT(Constants.DEFAULT_BUFFER_SIZE, Constants.SAMPLING);

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

		ArrayList<Integer> caught_frequency = findFrequencies(fft);
		checkFrequencies(caught_frequency, time);
		
		if(Constants.DRAW_FFT){
			if (counter % 2 == 0) {
				if (counter == 1000) {
					counter = 0;
				}
				queue_for_graph.addFFTToConsumer(fft);
			}
		}
	}

	private void checkFrequencies(ArrayList<Integer> caught_frequency, long time) {
		boolean found1 = false;
		if (caught_frequency.size() > 0) {
			// long time = System.currentTimeMillis( );
			for (int i = 0; i < this.freq_time.size(); i++) {
				if (this.freq_time.get(i).getFrequency() >= caught_frequency
						.get(0) - 45
						&& this.freq_time.get(i).getFrequency() <= caught_frequency
								.get(0) + 45) {
					found1 = true;

					break;
				}
			}
			if (!found1) {
				// MessagesLog.d(TAG, "no kurwaksgdhasgdjas");
				setNewFreqAndCheck(caught_frequency.get(0), time);
			}
		}

	}

	// private void checkFrequencies(ArrayList<Integer> caught_frequency){
	// boolean found1 = false;
	// boolean found2 = false;
	// boolean two_freq = false;
	// if(caught_frequency.size() == 2) two_freq = true;
	//
	// if(caught_frequency.size() > 0){
	// for(int i = 0; i < this.freq_time.size() ; i++){
	// if(this.freq_time.get(i).getFrequency() >= caught_frequency.get(0) - 50
	// && this.freq_time.get(i).getFrequency() <= caught_frequency.get(0) + 50){
	// found1 = true;
	// }
	//
	// if(two_freq){
	// if(this.freq_time.get(i).getFrequency() >= caught_frequency.get(1) - 50
	// && this.freq_time.get(i).getFrequency() <= caught_frequency.get(1) + 50){
	// found2 = true;
	// }
	// }
	// }
	//
	// if(!two_freq){
	// if(!found1){
	// //MessagesLog.d(TAG, "Jestem tu, ludzi t³um");
	// setNewFreqAndCheck(caught_frequency.get(0));
	// //freq_time = addAndSort(freq_time, ft); // Add and sort
	//
	// }
	// }else{
	// if(!found1 && !found2){
	// // it cannot happen!
	// //MessagesLog.d(TAG, "Nie znalaz³ dwóch.");
	// setNewFreqAndCheck(caught_frequency.get(0));
	// setNewFreqAndCheck(caught_frequency.get(1));
	//
	// }else if(found1 && !found2){
	// //MessagesLog.d(TAG, "Znalaz³o pierwszego.");
	// setNewFreqAndCheck(caught_frequency.get(1));
	// }else if(!found1 && found2){
	// //MessagesLog.d(TAG, "Znalaz³o drugiego.");
	// setNewFreqAndCheck(caught_frequency.get(0));
	// }else if(found1 && found2){
	// //MessagesLog.d(TAG, "¯adnego nie znalaz³o");
	// // it cannot happen too
	// }
	// }
	//
	//
	// }else{
	// clearFrequencies();
	// }
	//
	//
	//
	// }

	private void clearFrequencies() {
		freq_time.clear();
	}

	private void setNewFreqAndCheck(int freq, long time) {

		for (int i = 0; i < freq_time.size(); i++) {
			freq_time.get(i).setEnd(time);
			String signs = freq_time.get(i).computeAndReturnString();
			if (signs.compareTo(Constants.NOEND_STR) != 0) {
				vrs.onRecognition(signs);
				// MessagesLog.d(TAG, string_builder.toString());
			}
			freq_time.remove(i);
		}
		FrequencyTime ft = new FrequencyTime();
		ft.setFrequency(freq);
		ft.setStart(time);
		freq_time.add(0, ft);
	}

//	private ArrayList<Integer> findFrequencies(FFT fft) {
//		int peak_place = 0;
//		float curr_peak = 0;
//		// ArrayList<Integer> peaks = new ArrayList<Integer>();
//		ArrayList<Integer> frequencies = new ArrayList<Integer>();
//		//
//		if (Constants.ULTRASOUND == 1) {
//
//			for (int i = fft.specSize() / 2; i < fft.specSize(); i++) {
//				if (fft.getBand(i) > 20) {
//					if (curr_peak < fft.getBand(i)) {
//						// MessagesLog.d(TAG, "no jejejjejejejej " + i);
//						peak_place = i;
//						curr_peak = fft.getBand(i);
//					}
//				}
//			}
//		} else {
//			for (int i = 0; i < fft.specSize() / 2; i++) {
//				if (fft.getBand(i) > 20) {
//					if (curr_peak < fft.getBand(i)) {
//						// MessagesLog.d(TAG, "no jejejjejejejej " + i);
//						peak_place = i;
//						curr_peak = fft.getBand(i);
//					}
//				}
//			}
//		}
//
//		// peaks.add(peak_place);
//		frequencies.add(peak_place * (Constants.SAMPLING / 2) / fft.specSize());
//		return frequencies;
//	}
	
	
	private ArrayList<Integer> findFrequencies(FFT fft) {
		int peak_place = 0;
		float curr_peak = 0;
		// ArrayList<Integer> peaks = new ArrayList<Integer>();
		ArrayList<Integer> frequencies = new ArrayList<Integer>();
		//
		if (Constants.ULTRASOUND == 1) {

			for (int i = fft.specSize() / 2; i < fft.specSize(); i++) {
				if (fft.getBand(i) > 20) {
					if (curr_peak < fft.getBand(i)) {
						// MessagesLog.d(TAG, "no jejejjejejejej " + i);
						peak_place = i;
						curr_peak = fft.getBand(i);
					}
				}
			}
		} else {
			for (int i = 0; i < fft.specSize() / 2; i++) {
				if (fft.getBand(i) > 100) {
					if (curr_peak < fft.getBand(i)) {
						// MessagesLog.d(TAG, "no jejejjejejejej " + i);
						peak_place = i;
						curr_peak = fft.getBand(i);
					}
				}
			}
		}

		// peaks.add(peak_place);
		frequencies.add(peak_place * (Constants.SAMPLING / 2) / fft.specSize());
		return frequencies;
	}
	
	
//	private ArrayList<Integer> findFrequencies(FFT fft) {
//		int peak_place = 0;
//		ArrayList<Integer> peaks = new ArrayList<Integer>();
//		ArrayList<Integer> frequencies = new ArrayList<Integer>();
//		// for(int i = fft.specSize()/2; i < fft.specSize(); i++){
//		for (int i = 0; i < fft.specSize() / 2; i++) {
//			if (fft.getBand(i) > 100) {
//				int peak_dsada = i * (Constants.SAMPLING / 2) / fft.specSize();
//				if (preav_peak >= peak_dsada - 50
//						&& preav_peak <= peak_dsada + 50) {
//					// preav_peak = peak_dsada;
//				} else {
//					MessagesLog.d(TAG, "hmmm  " + Integer.toString(peak_dsada));
//					preav_peak = peak_dsada;
//				}
//
//				if (peaks.size() == 0 || peaks.size() == 1) {
//					peaks.add(i);
//				} else {
//					if (fft.getBand(peaks.get(0)) > fft.getBand(peaks.get(1))) {
//						peaks.add(1, i);
//					} else {
//						peaks.add(0, i);
//					}
//				}
//			}
//		}
//
//		for (Integer peak : peaks) {
//			// int peak_dsada = peak * (Constants.SAMPLING/2) / fft.specSize();
//			// MessagesLog.d(TAG, Integer.toString(peak_dsada));
//			frequencies.add(peak * (Constants.SAMPLING / 2) / fft.specSize());
//		}
//
//		return frequencies;
//	}

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
