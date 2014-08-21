package com.example.recorder;

import com.example.graphic.VoiceRecObserver;

public interface VoiceRecSubject {
	public void register(VoiceRecObserver vro);
	
	public void notifyObserver(int [] data);
}
