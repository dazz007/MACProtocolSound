package com.example.recorder;

import com.example.important.Buffer;


public interface RecorderAudioObserver {
//	public void sendDataToGraph(int[] data);
//	
//	public void sendDataToGraphByte(byte [] data);
	
	public void putBufferToQueue(Buffer buffer);
	
	public void setStopStatus();
	
	public void setSubject(RecorderAudio sub);
}
