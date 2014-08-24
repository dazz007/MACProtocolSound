package com.example.recorder;

import com.example.graphic.VoiceRecObserver;
import com.example.important.Buffer;

public interface VoiceRecSubject {
	public void register(VoiceRecObserver vro);
	
	public void notifyObserver(int [] data);
	
	public Buffer getBufferForGraphQueue();
	
	public Buffer getBufferForDecoderQueue();
}
