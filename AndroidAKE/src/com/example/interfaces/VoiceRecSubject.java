package com.example.interfaces;

import com.example.important.Buffer;

public interface VoiceRecSubject {
	public void register(VoiceRecObserver vro);
	
	public void notifyObserver(int [] data);
	
	public Buffer getBufferForGraphQueue();
	
	public Buffer getBufferForDecoderQueue();
	
	public void onStartRecognition();
	
	public void onRecognition(String str);
	
	public void onEndRecogntion();
}