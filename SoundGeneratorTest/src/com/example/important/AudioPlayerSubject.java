package com.example.important;

import com.example.graphic.SoundGenObserver;

public interface AudioPlayerSubject {
	public void register(AudioPlayerObserver sgo);
	
	public void notifyObserver(int [] data);
}
