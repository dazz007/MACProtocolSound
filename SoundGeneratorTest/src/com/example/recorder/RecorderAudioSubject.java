package com.example.recorder;


public interface RecorderAudioSubject {
	public void register(RecorderAudioObserver rao);
	
	public void notifyObserver(int [] data);
	
	public void notifyObserverByte(byte [] data);
}
