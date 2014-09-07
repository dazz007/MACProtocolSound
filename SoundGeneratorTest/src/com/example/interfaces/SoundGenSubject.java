package com.example.interfaces;


public interface SoundGenSubject {
	public void register(SoundGenObserver sgo);
	
	public void notifyObserver(int [] data);
}
