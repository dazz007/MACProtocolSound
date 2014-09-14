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
	//DoubleFFT_1D fftDo;
	private FFT fft;
	private int counter;
	private float[] fftRealArray;
	ArrayList<FrequencyTime> freq_time;
	private String result_in_string = "";
	private StringBuilder string_builder;
	@Override
	public void setSubject(VoiceRecSubject sub) {
		this.vrs = sub;
		//this.fftDo = new DoubleFFT_1D(Constants.DEFAULT_BUFFER_SIZE * 2);
		counter = 0;
		queue_for_graph = new Queue();
		freq_time = new ArrayList<FrequencyTime>();
		string_builder = new StringBuilder();
	}

	public void start() {
		if (state == Constants.STOP_STATE) {
			state = Constants.START_STATE;
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
		checkFrequencies(caught_frequency);
		
		
		MessagesLog.d(TAG, string_builder.toString());
//		int peak = 0;
//		
//		for(int i = fft.specSize()/2; i < fft.specSize(); i++){
//			if(fft.getBand(i) > 20 && fft.getBand(i) > peak){
//				peak = i;
//			}
//		}
//		int caught_frequency  = peak*(Constants.SAMPLING/2) / fft.specSize();
//		if(caught_frequency>0)
//			MessagesLog.d(TAG, Double.toString(caught_frequency));
		
		if(counter % 2 == 0){
			if(counter == 1000){
				counter = 0;
			}
			queue_for_graph.addFFTToConsumer(fft);
		}
	}
	
	private void checkFrequencies(ArrayList<Integer> caught_frequency){
		boolean found1 = false;
		boolean found2 = false;
		boolean two_freq = false;
		if(caught_frequency.size() == 2) two_freq = true;
		
		if(caught_frequency.size() > 0){
			for(int i = 0; i < this.freq_time.size() ; i++){
				if(this.freq_time.get(i).getFrequency() == caught_frequency.get(0)){
					found1 = true;
				}
				
				if(two_freq){
					if(this.freq_time.get(i).getFrequency() == caught_frequency.get(1)){
						found2 = true;
					}
				}
			}
		}
		
		if(!two_freq){
			if(!found1){
				if(caught_frequency.size()>0)
					setNewFreqAndCheck(caught_frequency.get(0));
				//freq_time = addAndSort(freq_time, ft); // Add and sort
				
			}
		}else{
			if(!found1 && !found2){
//				it cannot happen!
			}else if(found1 && !found2){
				setNewFreqAndCheck(caught_frequency.get(1));
			}else if(!found1 && found2){
				setNewFreqAndCheck(caught_frequency.get(0));
			}else if(found1 && found2){
//				it cannot happen too
			}
		}
		
	}
	
	private void setNewFreqAndCheck(int freq){
		for(int i = 0; i < freq_time.size(); i++){
			freq_time.get(i).setEnd(System.currentTimeMillis( ));
			String sign = freq_time.get(i).computeAndReturnString();
			if(sign.compareTo(Constants.NOEND_STR) != 0){
				string_builder.append(sign);
			}
			freq_time.remove(i);
		}
		FrequencyTime ft = new FrequencyTime();
		ft.setFrequency(freq);
		ft.setStart(System.currentTimeMillis());
		freq_time.add(0, ft);
	}
	
	
	private ArrayList<Integer> findFrequencies(FFT fft){
		int peak_place = 0;
		ArrayList<Integer> peaks = new ArrayList<Integer>();
		ArrayList<Integer> frequencies = new ArrayList<Integer>();
		for(int i = fft.specSize()/2; i < fft.specSize(); i++){
			if(fft.getBand(i) > 20){
				if(peaks.size() == 0 || peaks.size() == 1){
					peaks.add(i);
				}else{
					if(fft.getBand(peaks.get(0)) > fft.getBand(peaks.get(1))){
						peaks.add(1, i);
					}else{
						peaks.add(0,i);
					}
				}
			}
		}
		
		for (Integer peak : peaks) {
			frequencies.add(peak * (Constants.SAMPLING/2) / fft.specSize());
		}
		
		return frequencies;
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
