package com.example.interfaces;


public interface SoundGenObserver {
	public void updateLineGraph(int[] data);
	
	public void setSubject(SoundGenSubject sub);
}
