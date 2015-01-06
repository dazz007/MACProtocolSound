package com.example.important;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.example.minim.FFT;

public class Queue {
	
	private BlockingQueue<Buffer> producer;
	private BlockingQueue<Buffer> consumer;
	private BlockingQueue<FFT> fft;
	
	/**
	 * Constructor for class Queue
	 */
	public Queue(){
		producer = new LinkedBlockingQueue<Buffer>();
		consumer = new LinkedBlockingQueue<Buffer>();
		fft = new LinkedBlockingQueue<FFT>();
		
	}
	
	/**
	 * Add to producer
	 * @param data - Buffer data with sound
	 */
	public void addToProducer(Buffer data){
		try {
			producer.put(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get from producer
	 * @return Buffer - Buffer data with sound
	 */
	public Buffer getFromProducer(){
		if(producer != null){
			try {
				return producer.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Add to consumer
	 * @param data - Buffer data with sound
	 */
	public void addToConsumer(Buffer data){
		try {
			consumer.put(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get from consumer
	 * @return Buffer - Buffer data with sound
	 */
	public Buffer getFromConsumer(){
		if(consumer != null){
			try {
				return consumer.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Add to consumer
	 * @param data - FFT data with sound
	 */
	public void addFFTToConsumer(FFT data){
		try {
			fft.put(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get from consumer
	 * @return FFT - Buffer data with sound
	 */
	public FFT getFromFFTConsumer(){
		if(fft != null){
			try {
				return fft.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Clear buffers
	 */
	public void clearBuffers(){
		producer.clear();
		consumer.clear();
		fft.clear();
	}
}
