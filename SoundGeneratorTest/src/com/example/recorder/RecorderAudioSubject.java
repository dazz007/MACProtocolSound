package com.example.recorder;

import com.example.important.Buffer;


public interface RecorderAudioSubject {
	public void register(RecorderAudioObserver rao);
	
//	public void notifyObserver(int [] data);
//	
//	public void notifyObserverByte(byte [] data);
	
	public void notifyObserverBuffer(Buffer buffer);
}
