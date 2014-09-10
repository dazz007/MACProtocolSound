package com.example.recorder;

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
	DoubleFFT_1D fftDo;
	FFT fft;
	int counter;
	float[] fftRealArray;

	@Override
	public void setSubject(VoiceRecSubject sub) {
		this.vrs = sub;
		this.fftDo = new DoubleFFT_1D(Constants.DEFAULT_BUFFER_SIZE * 2);
		counter = 0;
		queue_for_graph = new Queue();
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
		
		int peak = 0;
		
		for(int i = fft.specSize()/2; i < fft.specSize(); i++){
			if(fft.getBand(i) > 20 && fft.getBand(i) > peak){
				peak = i;
			}
		}
		int caught_frequency  = peak*(Constants.SAMPLING/2) / fft.specSize();
		if(caught_frequency>0)
			MessagesLog.d(TAG, Double.toString(caught_frequency));
		
//		for(float freq = Constants.SAMPLING/2 - 1; freq > 0.0; freq -= 150){
//			
//		}
		if(counter % 2 == 0){
			if(counter == 1000){
				counter = 0;
			}
			queue_for_graph.addFFTToConsumer(fft);
		}
		
		// fft.
		// double[] data = applyingHammingWindow(buffer.getBufferShort());
		// double[] fft = new double[data.length * 2];
		// System.arraycopy(data, 0, fft, 0, data.length);
		// fftDo.realForwardFull(fft);
		// Buffer buffer_fft = new Buffer();
		// buffer_fft.buffer_fft = fft;
		// queue_for_graph.addToConsumer(buffer_fft);
		// int peak = findPeak(fft);
		// MessagesLog.d(TAG, Integer.toString(peak));
	}

	private double[] applyingHammingWindow(short[] data) {
		double[] data_d = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			data_d[i] = 0.53836 - 0.46164 * Math.cos((2 * Math.PI * data[i])
					/ (data.length - 1));
			// data_d[i] = data[i];
		}

		return data_d;
	}

	private int findPeak(double[] fft) {
		double curr_peak = 0.00;
		int place_curr_peak = 0;
		int result = 0;
		// MessagesLog.d(TAG, "Rozmiar fft: "+Integer.toString(fft.length));
		for (int i = 1; i < fft.length; i++) {
			// MessagesLog.d(TAG, Integer.toString(i));
			// MessagesLog.d(TAG, Double.toString(fft[i]));
			result = Double.compare(curr_peak, Math.abs(fft[i]));
			if (result < 0) {

				// MessagesLog.d(TAG, "Wesz³o: "+Double.toString(fft[i]));
				// MessagesLog.d(TAG, Integer.toString(i));
				curr_peak = Math.abs(fft[i]);
				place_curr_peak = i;
			}
		}
		return place_curr_peak;
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
