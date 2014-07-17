package com.example.important;

import com.example.graphic.SoundGenObserver;

public interface SoundGenSubject {
	public void register(SoundGenObserver sgo);
	
	public void notifyObserver(int [] data);
}
