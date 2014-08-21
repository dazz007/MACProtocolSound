package com.example.recorder;


public interface RecorderAudioObserver {
	public void sendDataToGraph(int[] data);
	
	public void sendDataToGraphByte(byte [] data);
	
	public void setStopStatus();
	
	public void setSubject(RecorderAudio sub);
}
