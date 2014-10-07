package com.example.important;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.example.minim.FFT;

public class Queue {
	
	private BlockingQueue<Buffer> producer;
	private BlockingQueue<Buffer> consumer;
	private BlockingQueue<FFT> fft;
	
	public Queue(){
		producer = new LinkedBlockingQueue<Buffer>();
		consumer = new LinkedBlockingQueue<Buffer>();
		fft = new LinkedBlockingQueue<FFT>();
		
	}
	
	public void addToProducer(Buffer data){
		try {
			producer.put(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
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
	
	public void addToConsumer(Buffer data){
		try {
			consumer.put(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	
	public void addFFTToConsumer(FFT data){
		try {
			fft.put(data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	
	
	public void clearBuffers(){
		producer.clear();
		consumer.clear();
		fft.clear();
	}
	
}
