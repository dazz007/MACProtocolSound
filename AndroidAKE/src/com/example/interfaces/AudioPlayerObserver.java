package com.example.interfaces;

import com.example.important.AudioPlayer;

public interface AudioPlayerObserver {
	public void sendDataToGraph(int[] data);
	
	public void setStopStatus();
	
	public void setSubject(AudioPlayer sub);
}
