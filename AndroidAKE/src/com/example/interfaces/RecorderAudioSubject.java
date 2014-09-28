package com.example.interfaces;

import com.example.important.Buffer;


public interface RecorderAudioSubject {
	public void register(RecorderAudioObserver rao);
	
	public void notifyObserverBuffer(Buffer buffer);
}
