package com.example.interfaces;


public interface AudioPlayerSubject {
	public void register(AudioPlayerObserver sgo);
	
	public void notifyObserver(int [] data);
}
