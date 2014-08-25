package com.example.recorder;

import org.jtransforms.fft.DoubleFFT_1D;
import com.example.graphic.VoiceRecObserver;
import com.example.important.Buffer;
import com.example.important.Constants;

public class Decoder implements VoiceRecObserver{
	private VoiceRecSubject vrs;
	private int state;
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
		
	}
	
	private double[] applyingHammingWindow(short[] data){
		double[] data_d = new double[data.length];
		for(int i = 0; i < data.length; i++){
			data_d[i] = 0.53836 - 0.46164 * Math.cos((2 * Math.PI * data[i]) / ( data.length - 1 ));
		}
		
		return data_d;
	}
}
