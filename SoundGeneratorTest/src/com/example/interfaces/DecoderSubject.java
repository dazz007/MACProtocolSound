package com.example.interfaces;

import com.example.important.Buffer;
import com.example.minim.FFT;
import com.example.minim.FourierTransform;

public interface DecoderSubject {

		public Buffer getBufferFFTForGraphQueue();
		public FFT getBufferFFTForGraphQueueFFT();
}
