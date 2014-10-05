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
		freq_time = new ArrayList<FrequencyTime>();
	}

	public void start() {
		Buffer buf_from_queue;
		current_freq = null;
		if (state == Constants.STOP_STATE) {
			state = Constants.START_STATE;
			prepared_buffers = new ArrayList<Buffer>();
			this.vrs.onStartRecognition();
			while (state == Constants.START_STATE) {
				buf_from_queue = this.vrs.getBufferForDecoderQueue();
				if (buf_from_queue != null) {
					MessagesLog.d(TAG, "Wzielo super1111" );
					analyse(buf_from_queue);
				}else{
					 MessagesLog.e(TAG, "Wzielo puste" );
				}
			}
		}
	}

	public void start2() {
		int needed_size = 0;
		int MAX_SIZE = Constants.DEFAULT_NUM_SAMPLES / 2;
		Buffer buf_from_queue;
		Buffer prep_buf;
		if (state == Constants.STOP_STATE) {
			state = Constants.START_STATE;
			prepared_buffers = new ArrayList<Buffer>();
			this.vrs.onStartRecognition();
			while (state == Constants.START_STATE) {
				buf_from_queue = this.vrs.getBufferForDecoderQueue();
				if (buf_from_queue != null) {

					if (prepared_buffers.size() == 0) {
						prep_buf = new Buffer(MAX_SIZE);
						System.arraycopy(buf_from_queue.buffer_short, 0, prep_buf.buffer_short, 0, buf_from_queue.getSize());
						prep_buf.setSize(buf_from_queue.getSize());
						prepared_buffers.add(prep_buf);
					} else if (prepared_buffers.size() == 1) {
						prep_buf = prepared_buffers.get(0);
						needed_size = MAX_SIZE - prep_buf.getSize();
						if (needed_size == 0) {
							analyse(prep_buf);
							prep_buf = new Buffer(MAX_SIZE);
							prep_buf.setSize(buf_from_queue.getSize());
							System.arraycopy(buf_from_queue.buffer_short, 0, prep_buf.buffer_short, 0, buf_from_queue.getSize());
							prepared_buffers.set(0, prep_buf);
							// prepared_buffers.remove(0);
						} else if (buf_from_queue.getSize() > needed_size) {
							System.arraycopy(buf_from_queue.buffer_short, 0, prep_buf.buffer_short, prep_buf.getSize(), needed_size);
							prep_buf.setSize(prep_buf.getSize() + needed_size);
							analyse(prep_buf);
							prep_buf = new Buffer(MAX_SIZE);
							System.arraycopy(buf_from_queue.buffer_short, needed_size, prep_buf.buffer_short, 0, buf_from_queue.getSize() - needed_size);
							prep_buf.setSize(buf_from_queue.getSize() - needed_size);
							prepared_buffers.set(0, prep_buf);
							// prepared_buffers.add(prep_buf_next);

							// prepared_buffers.remove(0);
						} else if (buf_from_queue.getSize() < needed_size) {
							System.arraycopy(buf_from_queue.buffer_short, 0, prep_buf.buffer_short, prep_buf.getSize(), buf_from_queue.getSize());
							prep_buf.setSize(prep_buf.getSize() + buf_from_queue.getSize());
							prepared_buffers.set(0, prep_buf);
						}
					}
				}
			}
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
			if (fft.getBand(i) > 25 && fft.getBand(i) > max_band) {
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

//	private void checkFreqAgain(ArrayList<Integer> caught_frequency) {
//
//		boolean found1 = false;
//		boolean found2 = false;
//		if (caught_frequency.size() == 2) {
//			// long time = System.currentTimeMillis( );
//			for (int i = 0; i < this.freq_time.size(); i++) {
//				if (this.freq_time.get(i).getFrequency() >= caught_frequency
//						.get(0) - 45
//						&& this.freq_time.get(i).getFrequency() <= caught_frequency
//								.get(0) + 45) {
//					found1 = true;
//				}
//				if (this.freq_time.get(i).getFrequency() >= caught_frequency
//						.get(1) - 45
//						&& this.freq_time.get(i).getFrequency() <= caught_frequency
//								.get(1) + 45) {
//					found2 = true;
//				}
//			}
//
//			if (found1 && !found2) {
//				// MessagesLog.d(TAG, "znalazlo 1 bez 2");
//				String signs = freq_time.get(0).computeAndReturnString();
//				if (signs.compareTo(Constants.NOEND_STR) != 0) {
//					vrs.onRecognition(signs);
//				}
//				freq_time.remove(0);
//				FrequencyTime ft = new FrequencyTime();
//				ft.setFrequency(caught_frequency.get(1));
//				freq_time.add(ft);
//				// setNewFreqAndCheckAgain(caught_frequency.get(0));
//				// setNewFreq(caught_frequency.get(1));
//			}
//
//			if (!found1 && found2) {
//				// MessagesLog.d(TAG, "znalazlo 2 bez 1");
//				String signs = freq_time.get(0).computeAndReturnString();
//				if (signs.compareTo(Constants.NOEND_STR) != 0) {
//					vrs.onRecognition(signs);
//				}
//				freq_time.remove(0);
//				FrequencyTime ft = new FrequencyTime();
//				ft.setFrequency(caught_frequency.get(0));
//				freq_time.add(ft);
//				// setNewFreqAndCheckAgain(caught_frequency.get(1));
//				// setNewFreq(caught_frequency.get(0));
//			}
//
//			if (found1 && found2) {
//				// MessagesLog.d(TAG, "znalazlo oba co jest z³e³e³e³e");
//			}
//
//		} else {
//			// MessagesLog.d(TAG, "Banana "+ this.freq_time.size());
//			for (int i = 0; i < this.freq_time.size(); i++) {
//				if (this.freq_time.get(i).getFrequency() >= caught_frequency
//						.get(0) - 45
//						&& this.freq_time.get(i).getFrequency() <= caught_frequency
//								.get(0) + 45) {
//					found1 = true;
//					break;
//				}
//			}
//			if (found1) {
//				// MessagesLog.d(TAG, "znalazlo 1");
//				setNewFreqAndCheckAgain(caught_frequency.get(0));
//			} else {
//				// MessagesLog.d(TAG, "nie znalazlo");
//				setNewFreq(caught_frequency.get(0));
//			}
//
//		}
//	}

//	private void setNewFreq(int freq) {
//		FrequencyTime ft = new FrequencyTime();
//		ft.setFrequency(freq);
//		freq_time.add(ft);
//	}

//	private void setNewFreqAndCheckAgain(int freq) {
//		// if(freq_time.size() == 1){
//		String signs = freq_time.get(0).computeAndReturnString();
//		if (signs.compareTo(Constants.NOEND_STR) != 0) {
//			vrs.onRecognition(signs);
//		}
//		// if(freq_time.size() == 2){
//		freq_time.remove(0);
//		// }
//		setNewFreq(freq);
//	}

//	private ArrayList<Integer> findFrequenciesAgain(FFT fft) {
//		ArrayList<Integer> frequencies = new ArrayList<Integer>();
//		int dupa = 0;
//		for (int i = fft.specSize() / 2; i < fft.specSize(); i++) {
//
//			if (fft.getBand(i) > 100) {
//				if (dupa == 0) {
//					MessagesLog.d(TAG,
//							"Nastepna porcja ----------------------------");
//					dupa = 10;
//				}
//				MessagesLog.d(TAG, "Znalaz³o: " + i + " czestotliwosc: " + i
//						* (Constants.SAMPLING / 2) / fft.specSize());
//			}
//		}
//		return frequencies;
		// int peak_place = 0;
		// float curr_peak = 0;
		// ArrayList<Integer> peaks_places = new ArrayList<Integer>();
		// ArrayList<Integer> frequencies = new ArrayList<Integer>();
		// //
		// int delta = 45;
		// int frequency = 0;
		// if (Constants.ULTRASOUND == 1) {
		//
		// for (int i = fft.specSize() / 2; i < fft.specSize(); i++) {
		// if (fft.getBand(i) > 20) {
		// frequency = i * (Constants.SAMPLING / 2) / fft.specSize();
		// //if (curr_peak < fft.getBand(i)) {
		// if(frequencies.size() == 0){
		// frequencies.add(frequency);
		// peaks_places.add(i);
		// }else if(frequencies.size() == 1){
		// // Jeœli ju¿ jakiœ peak zosta³ znaleziony wczeœniej, sprawdzamy, czy
		// nie znaleziono ponownie na podobnej czestotliwoœci
		// if(!(frequencies.get(0) + delta >= frequency && frequencies.get(0) -
		// delta <= frequency)){
		// frequencies.add(frequency);
		// peaks_places.add(i);
		// // Jeœli taki peak zosta³ znaleziony to sprawdzamy gdzie jest wiêksza
		// amplitutda i zastêpujamy na wiêksz¹
		// }else if(fft.getBand(peaks_places.get(0)) < fft.getBand(i)){
		// frequencies.set(0, frequency);
		// peaks_places.set(0, i);
		// }
		// }else if(frequencies.size() == 2){
		// if(frequencies.get(0) + delta >= frequency && frequencies.get(0) -
		// delta <= frequency){
		// if(fft.getBand(peaks_places.get(0)) < fft.getBand(i)){
		// frequencies.set(0, frequency);
		// peaks_places.set(0, i);
		// }
		// }else if(frequencies.get(1) + delta >= frequency &&
		// frequencies.get(1) - delta <= frequency){
		// if(fft.getBand(peaks_places.get(1)) < fft.getBand(i)){
		// frequencies.set(1, frequency);
		// peaks_places.set(1, i);
		// }
		// }else{
		// if(fft.getBand(peaks_places.get(1)) <=
		// fft.getBand(peaks_places.get(0))){
		// frequencies.set(1, frequency);
		// peaks_places.set(1, i);
		// }else{
		// frequencies.set(0, frequency);
		// peaks_places.set(0, i);
		// }
		// }
		// }
		// }
		// }
		// } else {
		// // for (int i = 0; i < fft.specSize() / 2; i++) {
		// // if (fft.getBand(i) > 100) {
		// // if (curr_peak < fft.getBand(i)) {
		// // // MessagesLog.d(TAG, "no jejejjejejejej " + i);
		// // peak_place = i;
		// // curr_peak = fft.getBand(i);
		// // }
		// // }
		// // }
		// }
		//
		// // peaks.add(peak_place);
		// for(int i = 0; i< frequencies.size() ; i++){
		// MessagesLog.d(TAG,
		// "Znalaz³o: "+i+" czestotliwosc: "+frequencies.get(i));
		// }
		// return frequencies;
//	}

	// private void checkFreq(ArrayList<Integer> caught_frequency){
	//
	// for (int i = 0; i < Constants.FREQUENCIES.length; i++) {
	// if (Constants.FREQUENCIES[i] + 20 > caught_frequency.get(0)
	// && Constants.FREQUENCIES[i] - 35 < caught_frequency.get(0)) {
	// vrs.onRecognition(String.valueOf(Constants.AVAILABLE_SIGNS.charAt(i)));
	// //return sb.toString();
	// }
	// }
	// }

//	private void checkFrequencies(ArrayList<Integer> caught_frequency, long time) {
//		boolean found1 = false;
//		if (caught_frequency.size() > 0) {
//			// long time = System.currentTimeMillis( );
//			for (int i = 0; i < this.freq_time.size(); i++) {
//				if (this.freq_time.get(i).getFrequency() >= caught_frequency
//						.get(0) - 45
//						&& this.freq_time.get(i).getFrequency() <= caught_frequency
//								.get(0) + 45) {
//					found1 = true;
//
//					break;
//				}
//			}
//			if (!found1) {
//				// MessagesLog.d(TAG, "no kurwaksgdhasgdjas");
//				setNewFreqAndCheck(caught_frequency.get(0), time);
//			} else {
//				// setNewFreqAndCheck(caught_frequency.get(0), time);
//			}
//		}
//
//	}

//	private void checkFrequenciesTwo(ArrayList<Integer> caught_frequency,
//			long time) {
//		boolean found1 = false;
//		boolean found2 = false;
//		boolean two_freq = false;
//		if (caught_frequency.size() == 2)
//			two_freq = true;
//
//		if (caught_frequency.size() > 0) {
//			for (int i = 0; i < this.freq_time.size(); i++) {
//				if (this.freq_time.get(i).getFrequency() >= caught_frequency
//						.get(0) - 45
//						&& this.freq_time.get(i).getFrequency() <= caught_frequency
//								.get(0) + 45) {
//					found1 = true;
//				}
//				if (this.freq_time.get(i).getFrequency() >= caught_frequency
//						.get(1) - 45
//						&& this.freq_time.get(i).getFrequency() <= caught_frequency
//								.get(1) + 45) {
//					found2 = true;
//				}
//
//			}
//
//			if (!two_freq) {
//				setNewFreqAndCheck(caught_frequency.get(0), time);
//			} else {
//				if (!found1 && !found2) {
//					// it cannot happen!
//					gesLog.d(TAG, "Nie znalaz³ dwóch.");
//					setNewFreqAndCheck(caught_frequency.get(0), time);
//					setNewFreqAndCheck(caught_frequency.get(1), time);
//
//				} else if (found1 && !found2) {
//					MessagesLog.d(TAG, "Znalaz³o pierwszego.");
//					setNewFreqAndCheck(caught_frequency.get(1), time);
//				} else if (!found1 && found2) {
//					MessagesLog.d(TAG, "Znalaz³o drugiego.");
//					setNewFreqAndCheck(caught_frequency.get(0), time);
//				} else if (found1 && found2) {
//					MessagesLog.d(TAG, "¯adnego nie znalaz³o");
//					// it cannot happen too
//				}
//
//			}
//
//		} else {
//			clearFrequencies();
//		}
//
//	}

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

//	private void clearFrequencies() {
//		freq_time.clear();
//	}
//
//	private void setNewFreqAndCheck(int freq, long time) {
//
//		for (int i = 0; i < freq_time.size(); i++) {
//			freq_time.get(i).setEnd(time);
//			String signs = freq_time.get(i).computeAndReturnString();
//			if (signs.compareTo(Constants.NOEND_STR) != 0) {
//				vrs.onRecognition(signs);
//				// MessagesLog.d(TAG, string_builder.toString());
//			}
//			freq_time.remove(i);
//		}
//		FrequencyTime ft = new FrequencyTime();
//		ft.setFrequency(freq);
//		ft.setStart(time);
//		freq_time.add(0, ft);
//	}

	// private ArrayList<Integer> findFrequencies(FFT fft) {
	// int peak_place = 0;
	// float curr_peak = 0;
	// // ArrayList<Integer> peaks = new ArrayList<Integer>();
	// ArrayList<Integer> frequencies = new ArrayList<Integer>();
	// //
	// if (Constants.ULTRASOUND == 1) {
	//
	// for (int i = fft.specSize() / 2; i < fft.specSize(); i++) {
	// if (fft.getBand(i) > 20) {
	// if (curr_peak < fft.getBand(i)) {
	// // MessagesLog.d(TAG, "no jejejjejejejej " + i);
	// peak_place = i;
	// curr_peak = fft.getBand(i);
	// }
	// }
	// }
	// } else {
	// for (int i = 0; i < fft.specSize() / 2; i++) {
	// if (fft.getBand(i) > 20) {
	// if (curr_peak < fft.getBand(i)) {
	// // MessagesLog.d(TAG, "no jejejjejejejej " + i);
	// peak_place = i;
	// curr_peak = fft.getBand(i);
	// }
	// }
	// }
	// }
	//
	// // peaks.add(peak_place);
	// frequencies.add(peak_place * (Constants.SAMPLING / 2) / fft.specSize());
	// return frequencies;
	// }

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
//				if (fft.getBand(i) > 100) {
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

//	private ArrayList<Integer> findFrequenciesTwo(FFT fft) {
//		int peak_place = 0;
//		int preav_peak = 0;
//		ArrayList<Integer> peaks = new ArrayList<Integer>();
//		ArrayList<Integer> frequencies = new ArrayList<Integer>();
//		// for(int i = fft.specSize()/2; i < fft.specSize(); i++){
//		if (Constants.ULTRASOUND == 1) {
//			for (int i = fft.specSize() / 2; i < fft.specSize(); i++) {
//				if (fft.getBand(i) > 20) {
//					// int peak_dsada = i * (Constants.SAMPLING / 2)
//					// / fft.specSize();
//					// if (preav_peak >= peak_dsada - 50
//					// && preav_peak <= peak_dsada + 50) {
//					// // preav_peak = peak_dsada;
//					// } else {
//					// MessagesLog.d(TAG,
//					// "hmmm  " + Integer.toString(peak_dsada));
//					// preav_peak = peak_dsada;
//					// }
//
//					if (peaks.size() == 0 || peaks.size() == 1) {
//						peaks.add(i);
//					} else {
//						if (fft.getBand(peaks.get(0)) > fft.getBand(peaks
//								.get(1))) {
//							peaks.add(1, i);
//						} else {
//							peaks.add(0, i);
//						}
//					}
//				}
//			}
//		} else {
//
//			for (int i = 0; i < fft.specSize() / 2; i++) {
//				if (fft.getBand(i) > 100) {
//					int peak_dsada = i * (Constants.SAMPLING / 2)
//							/ fft.specSize();
//					if (preav_peak >= peak_dsada - 50
//							&& preav_peak <= peak_dsada + 50) {
//						// preav_peak = peak_dsada;
//					} else {
//						MessagesLog.d(TAG,
//								"hmmm  " + Integer.toString(peak_dsada));
//						preav_peak = peak_dsada;
//					}
//
//					if (peaks.size() == 0 || peaks.size() == 1) {
//						peaks.add(i);
//					} else {
//						if (fft.getBand(peaks.get(0)) > fft.getBand(peaks
//								.get(1))) {
//							peaks.set(1, i);
//						} else {
//							peaks.set(0, i);
//						}
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
