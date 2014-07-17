package com.example.important;

public interface AudioPlayerObserver {
	public void sendDataToGraph(int[] data);
	
	public void setStopStatus();
	
	public void setSubject(AudioPlayer sub);
}
