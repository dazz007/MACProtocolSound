package com.example.recorder;

import org.jtransforms.fft.DoubleFFT_1D;
import com.example.graphic.VoiceRecObserver;
import com.example.important.Buffer;
import com.example.important.Constants;
import com.example.important.MessagesLog;
import com.example.important.Queue;

public class Decoder implements VoiceRecObserver, DecoderSubject{
	private final static String TAG = "DECODER";
	private VoiceRecSubject vrs;
	private int state;
	private Queue queue_for_graph;
	DoubleFFT_1D fftDo;
	
	
	
	@Override
	public void setSubject(VoiceRecSubject sub) {
		this.vrs = sub;
		this.fftDo = new DoubleFFT_1D(Constants.DEFAULT_BUFFER_SIZE*2);
	}
	
	public void start(){
		if(state == Constants.STOP_STATE){
			state = Constants.START_STATE;
					while(state == Constants.START_STATE){
						Buffer buffer = vrs.getBufferForDecoderQueue();
						if(buffer != null){
							analyse(buffer);
						}
					}
		}
	}
	
	
	private void analyse(Buffer buffer){
		double[] data = applyingHammingWindow(buffer.getBufferShort());
		double[] fft = new double[data.length * 2];
		System.arraycopy(data, 0, fft, 0, data.length);
		fftDo.realForwardFull(fft);
		Buffer buffer_fft = new Buffer();
		buffer_fft.buffer_fft = fft;
		queue_for_graph.addToConsumer(buffer);
		int peak = findPeak(fft);
		MessagesLog.d(TAG, Integer.toString(peak));
	}
	
	private double[] applyingHammingWindow(short[] data){
		double[] data_d = new double[data.length];
		for(int i = 0; i < data.length; i++){
			data_d[i] = 0.53836 - 0.46164 * Math.cos((2 * Math.PI * data[i]) / ( data.length - 1 ));
		}
		
		return data_d;
	}
	
	private int findPeak(double[] fft){
		double curr_peak = 0.00;
		int place_curr_peak = 0;
		int result = 0;
		MessagesLog.d(TAG, "Rozmiar fft: "+Integer.toString(fft.length));
		for(int i = 1; i < fft.length ; i++){
			//MessagesLog.d(TAG, Integer.toString(i));
			//MessagesLog.d(TAG, Double.toString(fft[i]));
			result = Double.compare(curr_peak, Math.abs(fft[i]));
			if(result < 0){
				
				//MessagesLog.d(TAG, "Wesz³o: "+Double.toString(fft[i]));
				//MessagesLog.d(TAG, Integer.toString(i));
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
}
